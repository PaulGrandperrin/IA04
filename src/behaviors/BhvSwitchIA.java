package behaviors;

import java.security.Timestamp;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import messages.AIDSerializer;
import messages.ProtoPaquet;
import messages.ProtoSTP;
import agents.SwitchAgent;
import agents.UserAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BhvSwitchIA extends Behaviour {
	private static final long serialVersionUID = -1724978096775931705L;
	SwitchAgent myAgent;
	private Gson gson;
	private GsonBuilder gsonb;
	
	public BhvSwitchIA(SwitchAgent a) {
		myAgent=a;
		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
		this.gson = gsonb.create();
	}
	
	@Override
	public void action() {
		

			//Je suis le switch racine (LOL)
			//J'envoie des BPDUs à tout le monde
			ProtoSTP stp=new ProtoSTP();
			stp.type="BPDU";
			stp.rootID=myAgent.bridgeID;
			
			ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
			msg.setContent(gson.toJson(stp));
			for(String s:myAgent.LinkTable)
			{
				msg.addReceiver(myAgent.getAIDByName(s));
				myAgent.addBehaviour(new BhvNotifyMaster(myAgent.getLocalName(), s));
			}
			myAgent.send(msg);
		
			
			long time=new Date().getTime();
			while((new Date().getTime())<time+5000) //Pendant 5 secondes
			{
				//Maintenant, j'attend des BPDUs pendant 5 secondes
				msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
				if(msg!=null)
				{
					ProtoSTP mess = gson.fromJson(msg.getContent(), ProtoSTP.class);
					
					// Je met à jour le rootID si nécéssaire
					if(mess.type.equals("BPDU")&&mess.rootID<myAgent.rootID)
					{
						myAgent.rootID=mess.rootID;
						//Ce BPDU m'a été utile, il peut l'être aussi pour mes voisins
						
						ACLMessage msg2 = new ACLMessage(ACLMessage.PROPAGATE);
						msg2.setContent(gson.toJson(mess));
						for(String s:myAgent.LinkTable)
						{
							if(!s.equals(msg.getSender().getLocalName())) //On renvoie pas à l'envoyeur
							{
								msg2.addReceiver(myAgent.getAIDByName(s));
								myAgent.addBehaviour(new BhvNotifyMaster(myAgent.getLocalName(), s));
							}
						}
						myAgent.send(msg2);
					}
				}
			}
			
			//On attend 5 sec
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(myAgent.bridgeID==myAgent.rootID) // Si je suis le switch racine
			{
				//J'envoie un msg à tout le monde
				stp=new ProtoSTP();
				stp.type="PATHFINDING";
				
				
				msg = new ACLMessage(ACLMessage.PROPAGATE);
				msg.setContent(gson.toJson(stp));
				for(String s:myAgent.LinkTable)
				{
					msg.addReceiver(myAgent.getAIDByName(s));
					myAgent.addBehaviour(new BhvNotifyMaster(myAgent.getLocalName(), s));
				}
				myAgent.send(msg);
			}
			else
			{
				//J'attend un et un seul msg de pathfinding et je le transmet
				msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
				if(msg!=null)
				{
					ProtoSTP mess = gson.fromJson(msg.getContent(), ProtoSTP.class);
					
					// Je met à jour le rootID si nécéssaire
					if(mess.type.equals("PATHFINDING"))
					{
						
					}
			}
			
		
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}

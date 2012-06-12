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
		
		
		
			myAgent.log("STP Etape 1: Envoie des BPDUs initiaux (bridgeID: "+myAgent.bridgeID+")");
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
		
			myAgent.log("STP Etape 2: Propagation des BPDUs et détermination du rootID");
			
			long time=new Date().getTime()+1000;
			long time2=new Date().getTime();
			while(time2<time) //Pendant 1 secondes
			{
				//Maintenant, j'attend des BPDUs pendant 2 secondes
				msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
				if(msg!=null)
				{
					ProtoSTP mess = gson.fromJson(msg.getContent(), ProtoSTP.class);
					
					// Je met à jour le rootID si nécéssaire
					if(mess.type.equals("BPDU")&&mess.rootID<myAgent.rootID)
					{
						myAgent.log("STP Etape 2: Reception d'un BPDU: "+mess.rootID);
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
				time2=new Date().getTime();
			}
			
			myAgent.log("STP Etape 2: Le rootID : "+myAgent.rootID);
			
			//On attend 2 sec
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			if(myAgent.bridgeID==myAgent.rootID) // Si je suis le switch racine
			{
				//On attend 2 sec
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				myAgent.log("STP Etape 3: je suis le switch root, j'envoie une propagation 'PATHFINDING'");
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
				myAgent.log("STP Etape 3: je ne suis pas le switch root, je recoie et propage les paquets 'PATHFINDING'");
				//J'attend un et un seul msg de pathfinding et je le transmet
				Boolean cont=true;
				while(cont)
				{
					msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
					if(msg!=null)
					{
						ProtoSTP mess = gson.fromJson(msg.getContent(), ProtoSTP.class);
						
						// Je met à jour le rootID si nécéssaire
						if(mess.type.equals("PATHFINDING"))
						{
							myAgent.rootPort=msg.getSender().getLocalName();
							//Puis je transmet le message
							
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
							cont=false;
						}
					}
				}
			}
			
			myAgent.log("STP Etape 3: Mon rootPort est "+ myAgent.rootPort);
			
			//On attend 2 sec
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			myAgent.log("STP Etape 4: Détermination des Determined Ports (Reverse Path Finding)");
			
			//Tout les switchs sauf le master envoie un paquet sur leur RootPort
			
			if(myAgent.bridgeID!=myAgent.rootID) // Si je ne suis pas le switch racine
			{
				stp=new ProtoSTP();
				stp.type="REVERSEPATHFINDING";
				
				msg = new ACLMessage(ACLMessage.PROPAGATE);
				msg.setContent(gson.toJson(stp));
				
				msg.addReceiver(myAgent.getAIDByName(myAgent.rootPort));
				myAgent.addBehaviour(new BhvNotifyMaster(myAgent.getLocalName(), myAgent.rootPort));
			
				myAgent.send(msg);
				myAgent.openedPorts.add(myAgent.rootPort);
			}
			
			//On attend tout les msgs de REVERSEPATHFINDING pour ouvrir les ports associés
			
			time=new Date().getTime()+1000;
			time2=new Date().getTime();
			while(time2<time) //Pendant 2 secondes
			{
				msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
				if(msg!=null)
				{
					ProtoSTP mess = gson.fromJson(msg.getContent(), ProtoSTP.class);
					if(mess.type.equals("REVERSEPATHFINDING"))
					{
						myAgent.openedPorts.add(msg.getSender().getLocalName());
					}
				}
				time2=new Date().getTime();
			}
			
			for(String s:myAgent.LinkTable)
			{
				if(s.contains("user"))
				{
					myAgent.openedPorts.add(s);
				}
				
			}
			
			myAgent.log("STP Etape 5: Fin! "+myAgent.openedPorts);
			
			myAgent.addBehaviour(new BhvSwitchPaquet((SwitchAgent)myAgent));
			
	}

	@Override
	public boolean done() {
		return true;
	}

}

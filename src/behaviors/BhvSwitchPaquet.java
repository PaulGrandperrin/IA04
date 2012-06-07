package behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import messages.AIDSerializer;
import messages.ProtoPaquet;
import agents.BaseAgent;
import agents.SwitchAgent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BhvSwitchPaquet extends CyclicBehaviour {

	private static final long serialVersionUID = -8645230578588902973L;
	SwitchAgent myAgent;
	private GsonBuilder gsonb;
	
	public BhvSwitchPaquet(Agent a) {
		super(a);
		myAgent=((SwitchAgent)a);
		myAgent.log("création du behavior SwitchPaquet");
		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
	}

//	private void answer(ACLMessage msg) {
//		GsonBuilder gsonb = new GsonBuilder();
//		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
//		Gson json = gsonb.create();
//
//		ProtoPaquet mess = json.fromJson(msg.getContent(), ProtoPaquet.class);
//
//		System.out.println("recu master:");
//		System.out.println(mess.content);
//		System.out.println(mess.src);
//	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		
		if(msg!=null)
		{
			String sender=msg.getSender().getLocalName();
			
			//Log
			Gson gson = gsonb.create();			
			ProtoPaquet p = gson.fromJson(msg.getContent(), ProtoPaquet.class);	
			myAgent.logPaquet(p);
			
			//Save source adresse in routeTable if needed
			if(!myAgent.routeTable.containsKey(p.src))
			{
				myAgent.routeTable.put(p.src,msg.getSender().getLocalName());
			}
			
			if(myAgent.routeTable.containsKey(p.dest))
			{
				String dst=myAgent.routeTable.get(p.dest);
				myAgent.log("transmission du paquet à "+dst);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ACLMessage jadeMsgInit = new ACLMessage(ACLMessage.REQUEST);
				jadeMsgInit.addReceiver(myAgent.getAIDByName(dst));				
				jadeMsgInit.setContent(msg.getContent());
				myAgent.send(jadeMsgInit);
								
				myAgent.addBehaviour(new BhvNotifyMaster(myAgent.getLocalName(), dst));
			}
			else //L'adresse de destination n'est pas dans la table de routage => on broadcast
			{
			
				for(String dst:((SwitchAgent)myAgent).getLinkTable())
				{
					if(dst.equals(sender) || dst.equals(p.src)) continue;
					myAgent.log("transmission du paquet (broadcast) à "+dst);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ACLMessage jadeMsgInit = new ACLMessage(ACLMessage.REQUEST);
					jadeMsgInit.addReceiver(myAgent.getAIDByName(dst));				
					jadeMsgInit.setContent(msg.getContent());
					myAgent.send(jadeMsgInit);
									
					myAgent.addBehaviour(new BhvNotifyMaster(myAgent.getLocalName(), dst));
				}
			}
		}
		
	}
}

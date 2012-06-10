package behaviors.master;

import messages.AIDSerializer;
import messages.ProtoPaquet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import agents.MasterAgent;
import atlas.lib.Pair;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BhvMasterHandleNotifications extends CyclicBehaviour {

	GsonBuilder gsonb;
	Gson gson;
	
	public BhvMasterHandleNotifications() {
		super();
		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
		gson = gsonb.create();
	}
	
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
		MasterAgent ag = (MasterAgent) myAgent;
		 
		if (msg != null) {			
			ProtoPaquet mess = gson.fromJson(msg.getContent(), ProtoPaquet.class);
			Pair<String, String> p = new Pair<String, String>(mess.src, mess.dest);
			ag.pushPair(p);
		}
		

	}

}

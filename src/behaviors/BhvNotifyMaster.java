package behaviors;

import messages.AIDSerializer;
import messages.ProtoPaquet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import agents.BaseAgent;

@SuppressWarnings("serial")
public class BhvNotifyMaster extends OneShotBehaviour {

	String src, dest;
	GsonBuilder gsonb;
	Gson gson;
	
	public BhvNotifyMaster(String src, String dest) {
		super();
		this.src = src;
		this.dest = dest;
		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
		gson = gsonb.create();
	} 
	/* notify the master agent that a communication between two node is happening */
	
	public void action() {
		BaseAgent ag = (BaseAgent) myAgent;
		AID mAID = ag.getMasterAID();
		// met les infos dans un protopaquet. cela évite d'avoir à créer une nouvelle structure
		// pour encapsuler les données.
		ProtoPaquet paquet = new ProtoPaquet();
		paquet.src = src;
		paquet.dest = dest;
		paquet.stp = false;
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(mAID);
		msg.setContent(gson.toJson(paquet));
		
		myAgent.send(msg);
	}

}

package behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import messages.AIDSerializer;
import messages.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PacketProcessingBhv extends WakerBehaviour {

	private static final long serialVersionUID = -8645230578588902973L;

	public PacketProcessingBhv(Agent a, long timeout) {
		super(a, timeout);
		System.out.println("Hi");
	}

	@Override
	protected void onWake() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		
		if (msg != null) {
			answer(msg);
		}
		reset(1000);
	}
	
	private void answer(ACLMessage msg) {		
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
		Gson json = gsonb.create();
		
		Message mess = json.fromJson(msg.getContent(), Message.class);

		System.out.println("recu master:");		
		System.out.println(mess.content);
		System.out.println(mess.src);
		
	}
}

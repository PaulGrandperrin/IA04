package behaviors;

import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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
	}
	
	private void answer(ACLMessage msg) {
		
		
	}
}

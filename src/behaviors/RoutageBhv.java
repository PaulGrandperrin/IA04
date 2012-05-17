package behaviors;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RoutageBhv extends WakerBehaviour {

	private static final long serialVersionUID = 7836366259657542510L;

	public RoutageBhv(Agent a, long timeout) {
		super(a, timeout);
		// TODO Auto-generated constructor stub
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
		
		System.out.println("J'ai recu un msg");
		ACLMessage reponse = msg.createReply();
		myAgent.send(reponse);
	}

}

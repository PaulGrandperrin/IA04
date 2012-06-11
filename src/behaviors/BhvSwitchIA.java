package behaviors;

import agents.SwitchAgent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BhvSwitchIA extends Behaviour {
	private static final long serialVersionUID = -1724978096775931705L;
	SwitchAgent myAgent;
	
	public BhvSwitchIA(SwitchAgent a) {
		myAgent=a;
	}
	
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
		
		if(msg!=null)
		{
			
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}

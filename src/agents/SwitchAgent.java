package agents;

import behaviors.PacketProcessingBhv;
import behaviors.RoutageBhv;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class SwitchAgent extends Agent {
	private static final long serialVersionUID = 7486410164729026372L;

	
	protected void setup() {
//		DFAgentDescription dfd = new DFAgentDescription();
//		dfd.setName(getAID());
//		ServiceDescription sd = new ServiceDescription();
//		sd.setType("MasterAgent");
//		sd.setName(getLocalName());
//		dfd.addServices(sd);
//		try {
//			DFService.register(this, dfd);
//		} catch (FIPAException e) {
//			e.printStackTrace();
//		}
		
		
		addBehaviour(new RoutageBhv(this,1000));
	}

}

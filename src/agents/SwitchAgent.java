package agents;

import com.google.gson.Gson;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import projet.main.Message;
import behaviors.RoutageBhv;
import behaviors.TopologyBhv;

public class SwitchAgent extends Agent {
	private static final long serialVersionUID = 7486410164729026372L;

	private Gson gson;
	
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
		
	
		gson = new Gson();
		addBehaviour(new RoutageBhv(this,1000));
		addBehaviour(new TopologyBhv());
		sendTo(getAID(), "trolol");
	}
	
	public AID searchMasterAgent() {
		
		AID masterAgent = null;
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("MasterAgent");
		template.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(this, template);
		
			if (result.length >= 1)
				masterAgent = result[0].getName();
			
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		return masterAgent;
	}
	
	public void sendTo(AID dest, String msg) {
		if (dest == null)
			return;
				
		ACLMessage mess = new ACLMessage(ACLMessage.REQUEST);
		
		AID masterAid = searchMasterAgent();
		mess.addReceiver(masterAid);
		Message msgStruct = new Message();
		msgStruct.dest = dest.toString();
		msgStruct.content = msg;
		msgStruct.src = getAID().toString();
		mess.setContent(gson.toJson(msgStruct));
		send(mess);
		System.out.println("message envoyé");
	}

}

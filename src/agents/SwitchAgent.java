package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import messages.AIDSerializer;
import messages.Message;
import behaviors.RoutageBhv;
import behaviors.TopologyBhv;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SwitchAgent extends Agent {
	private static final long serialVersionUID = 7486410164729026372L;

	private GsonBuilder gsonb;
	
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
		
	
		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
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
		
		Gson gson = gsonb.create();		
		ACLMessage mess = new ACLMessage(ACLMessage.REQUEST);
		
		AID masterAid = searchMasterAgent();
		mess.addReceiver(masterAid);
		Message msgStruct = new Message();
		msgStruct.dest = dest;
		msgStruct.content = msg;
		msgStruct.src = getAID();
		mess.setContent(gson.toJson(msgStruct));
		send(mess);
		System.out.println("message envoyé");
	}

}

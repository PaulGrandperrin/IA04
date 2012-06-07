package agents;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import behaviors.BhvSwitchInfoLink;
import behaviors.BhvSwitchPaquet;

import com.google.gson.GsonBuilder;

public class SwitchAgent extends BaseAgent {
	private static final long serialVersionUID = 7486410164729026372L;

	private GsonBuilder gsonb;

	private List<String> LinkTable;
	public Map<String, String> routeTable;

	protected void setup() {
		log("initialisation");
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("SwitchAgent");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		routeTable=new HashMap<String, String>();
		
		addBehaviour(new BhvSwitchInfoLink(this));
		addBehaviour(new BhvSwitchPaquet(this));
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

//	public void sendTo(AID dest, String msg) {
//		if (dest == null)
//			return;
//
//		Gson gson = gsonb.create();
//		ACLMessage mess = new ACLMessage(ACLMessage.REQUEST);
//
//		AID masterAid = searchMasterAgent();
//		mess.addReceiver(masterAid);
//		ProtoPaquet msgStruct = new ProtoPaquet();
//		msgStruct.dest = dest;
//		msgStruct.content = msg;
//		msgStruct.src = getAID();
//		mess.setContent(gson.toJson(msgStruct));
//		send(mess);
//		System.out.println("message envoyé");
//	}


}

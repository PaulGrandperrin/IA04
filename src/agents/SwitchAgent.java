package agents;

import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import messages.AIDSerializer;
import messages.ProtoPaquet;
import behaviors.BhvSwitchInfoLink;
import behaviors.BhvSwitchIA;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SwitchAgent extends Agent {
	private static final long serialVersionUID = 7486410164729026372L;

	private GsonBuilder gsonb;

	private List<String> LinkTable;

	protected void setup() {
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

		addBehaviour(new BhvSwitchInfoLink(this));
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

	public List<String> getLinkTable() {
		return LinkTable;
	}

	public void setLinkTable(List<String> linkTable) {
		System.out.println("LinkTable of SwitchAgent "+getLocalName()+" changed to ");
		for (String s:linkTable)
		{
			System.out.print(s+",");
		}
		System.out.println();
		LinkTable = linkTable;
	}

}

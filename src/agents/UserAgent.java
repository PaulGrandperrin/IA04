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

public class UserAgent extends BaseAgent {


	private GsonBuilder gsonb;

	private List<String> LinkTable;

	protected void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("UserAgent");
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

}

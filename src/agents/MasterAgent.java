package agents;

import java.util.List;
import java.util.Map;

import messages.AIDSerializer;
import messages.ProtoPaquet;
import messages.ProtoInfoLink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import gui.NetworkGraphFrame;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import behaviors.BhvSwitchPaquet;

public class MasterAgent extends GuiAgent {
	private static final long serialVersionUID = -6011994599868680168L;

	private GsonBuilder gsonb;
	private Map<String, List<String>> graphAgent;
	
	@SuppressWarnings("unchecked")
	protected void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("MasterAgent");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		NetworkGraphFrame gui = new NetworkGraphFrame(this);

		//addBehaviour(new PacketProcessingBhv(this,1000));


		//Envoie à tous les agents les infos de connections
		Object[] args = getArguments();
		graphAgent=(Map<String, List<String>>) args[0];


		gsonb = new GsonBuilder();

		Gson gson = gsonb.create();
		for(String agentName : graphAgent.keySet()){
			AID agent = getAIDByName(agentName);
			ACLMessage jadeMsg = new ACLMessage(ACLMessage.INFORM);
			jadeMsg.addReceiver(agent);
			ProtoInfoLink infoLink = new ProtoInfoLink();
			infoLink.links = graphAgent.get(agentName);
			jadeMsg.setContent(gson.toJson(infoLink));
			send(jadeMsg);
			System.out.println("InfoLink envoyé à "+agentName);
		}

	}


	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	private AID getSwitchAID(String name)
	{
		DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "SwitchAgent" );
        sd.setName(name);
        dfd.addServices(sd);
        
        try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			return result[0].getName();
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private AID getAIDByName(String name)
	{
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd  = new ServiceDescription();
		sd.setName(name);
		dfd.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			return result[0].getName();
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

package agents;

import gui.NetworkGraphFrame;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import messages.ProtoInfoLink;
import atlas.lib.Pair;
import behaviors.BhvMasterHandleNotifications;
import behaviors.GUIUpdateBehaviour;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MasterAgent extends GuiAgent {
	private static final long serialVersionUID = -6011994599868680168L;

	private GsonBuilder gsonb;
	private Map<String, List<String>> graphAgent;
	private List<Pair<String, String>> displayedConnections;
	
	@SuppressWarnings("unchecked")
	protected void setup() {
		displayedConnections = new ArrayList();
		
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

		
		addBehaviour(new GUIUpdateBehaviour(this, 1000, gui.vv));
		addBehaviour(new BhvMasterHandleNotifications());
		
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
	
	public void pushPair(Pair<String, String> p) {
		synchronized (this) {
			displayedConnections.add(p);
		}
	}
	
	public void clearDisplayedConnections() {
		synchronized (this) {
			displayedConnections.clear();
		}
	}

	public boolean connectionBetweenEdges(String first, String second) {
		// scan les connections affiches au cas
		for(int i = 0; i < displayedConnections.size(); i++) {
			Pair<String, String> p = displayedConnections.get(i);
			if((p.car().equals(first) && p.cdr().equals(second)) ||
			   (p.car().equals(second) && p.cdr().equals(first))) {
				return true;
			}
		}
		return false;
	}
}

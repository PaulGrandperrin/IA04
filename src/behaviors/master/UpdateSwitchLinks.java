package behaviors.master;

import java.util.List;
import java.util.Map;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import messages.ProtoInfoLink;

import agents.MasterAgent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.uci.ics.jung.graph.Graph;
import gui.NetworkGraphFrame;

@SuppressWarnings("serial")
public class UpdateSwitchLinks extends OneShotBehaviour {
	Map<String, List<String>> links;
	NetworkGraphFrame ng = null;
	
	public UpdateSwitchLinks(Map<String, List<String>> l) {
		links = l;
	}

	public UpdateSwitchLinks(NetworkGraphFrame networkGraphFrame) {
		ng = networkGraphFrame;		
		block(5000);
	}

	public void action() {
		MasterAgent ag = (MasterAgent) myAgent;
		ag.clearStp();
		GsonBuilder gsonb = new GsonBuilder();
		if(ng != null) {
			links = ng.getLinks();
		}
		
		Gson gson = gsonb.create();
		for(String agentName : links.keySet()){
			AID agent = ag.getAIDByName(agentName);
			ACLMessage jadeMsg = new ACLMessage(ACLMessage.INFORM);
			jadeMsg.addReceiver(agent);
			ProtoInfoLink infoLink = new ProtoInfoLink();
			infoLink.links = links.get(agentName);
			jadeMsg.setContent(gson.toJson(infoLink));
			ag.log("envoie de la liste des liens à "+agentName);
			ag.send(jadeMsg);
		}

	}

}

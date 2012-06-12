package behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.media.j3d.Behavior;

import agents.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import messages.AIDSerializer;
import messages.ProtoInfoLink;

public class BhvSwitchInfoLink extends CyclicBehaviour {

	private GsonBuilder gsonb;
	BaseAgent myAgent;

	public BhvSwitchInfoLink(Agent a) {
		super();
		myAgent=(BaseAgent)a;
		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
		myAgent.log("création du behavior SwitchInfoLink");
		
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		if (msg != null) {
			Gson gson = gsonb.create();

			ProtoInfoLink infoLink = gson.fromJson(msg.getContent(), ProtoInfoLink.class);			
			((BaseAgent)myAgent).setLinkTable(infoLink.links);
			if(myAgent instanceof SwitchAgent)
			{
				if(((SwitchAgent) myAgent).ia!=null)
				{
					myAgent.removeBehaviour(((SwitchAgent) myAgent).ia);
				}
				if(((SwitchAgent) myAgent).switchPaquet!=null)
				{
					myAgent.removeBehaviour(((SwitchAgent) myAgent).switchPaquet);
				}
				
				((SwitchAgent) myAgent).routeTable=new HashMap<String, String>();
				((SwitchAgent) myAgent).openedPorts=new ArrayList<String>();
				((SwitchAgent) myAgent).rootPort="";
				
				((SwitchAgent) myAgent).rootID =((SwitchAgent) myAgent).bridgeID;
				
				myAgent.addBehaviour(new BhvSwitchIA((SwitchAgent)myAgent)); ///// AVEC STP
				//myAgent.addBehaviour(new BhvSwitchPaquet((SwitchAgent)myAgent));//   SANS STP
			}
		}
	}




}

package behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Enumeration;

import javax.media.j3d.Behavior;

import agents.SwitchAgent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import messages.AIDSerializer;
import messages.ProtoInfoLink;

public class BhvSwitchInfoLink extends CyclicBehaviour {

	private GsonBuilder gsonb;
	Agent myAgent;

	public BhvSwitchInfoLink(Agent a) {
		super();
		myAgent=a;
		System.out.println("Un switch s'éveille :D");

		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());

	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		if (msg != null) {
			Gson gson = gsonb.create();

			ProtoInfoLink infoLink=gson.fromJson(msg.getContent(), ProtoInfoLink.class);
			((SwitchAgent)myAgent).setLinkTable(infoLink.links);

			System.out.println("Je suis un switch et j'ai recu ma liste de liens :D je suis content");
		}
	}




}

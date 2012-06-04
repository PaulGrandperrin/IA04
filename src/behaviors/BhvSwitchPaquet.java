package behaviors;

import sun.security.action.GetLongAction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import messages.AIDSerializer;
import messages.ProtoPaquet;

import agents.BaseAgent;
import agents.SwitchAgent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BhvSwitchPaquet extends CyclicBehaviour {

	private static final long serialVersionUID = -8645230578588902973L;

	public BhvSwitchPaquet(Agent a) {
		super(a);
		System.out.println("Hi");
	}

	private void answer(ACLMessage msg) {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
		Gson json = gsonb.create();

		ProtoPaquet mess = json.fromJson(msg.getContent(), ProtoPaquet.class);

		System.out.println("recu master:");
		System.out.println(mess.content);
		System.out.println(mess.src);
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		BaseAgent ag = (BaseAgent) myAgent;
		
		if(msg!=null)
		{
			String sender=msg.getSender().getLocalName();
						
			System.out.println("contenu du message : " + msg.getContent());
			for(String dst:((SwitchAgent)myAgent).getLinkTable())
			{
				if(dst.equals(sender)) continue;
				
				System.out.println("le message est envoye a " + dst);
				ACLMessage jadeMsgInit = new ACLMessage(ACLMessage.REQUEST);
				jadeMsgInit.addReceiver(ag.getAIDByName(dst));				
				jadeMsgInit.setContent(msg.getContent());
				myAgent.send(jadeMsgInit);
			}
		}
		
	}
}

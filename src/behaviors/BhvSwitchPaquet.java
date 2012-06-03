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
		if(msg!=null)
		{
			String sender=msg.getSender().getLocalName();
			
			System.out.println("Je suis "+ myAgent.getLocalName()+ ", je viens de recevoir un msg, et je vais l'envoyer à tt le monde :D");
			
			for(String dst:((SwitchAgent)myAgent).getLinkTable())
			{
				if(dst.equals(sender)) continue;
				
				ACLMessage jadeMsgInit = new ACLMessage(ACLMessage.INFORM);
				jadeMsgInit.addReceiver(getSwitchAID(dst));
				
				jadeMsgInit.setContent(msg.getContent());
				myAgent.send(jadeMsgInit);
			}
		}
		
	}
	
	private AID getSwitchAID(String name)
	{
		DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType("SwitchAgent");
        sd.setName(name);
        dfd.addServices(sd);
        
        try {
			DFAgentDescription[] result = DFService.search(this.myAgent, dfd);
			return result[0].getName();
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

package behaviors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import messages.AIDSerializer;
import messages.ProtoInfoLink;
import messages.ProtoPaquet;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BhvUserIncCom extends CyclicBehaviour{

	Agent myAgent;
	Integer counter;
	String dst=null;
	
	private GsonBuilder gsonb;
	
	public BhvUserIncCom(Agent a, int counter, String dst) {
		myAgent=a;
		this.dst=dst;
		this.counter=counter;
		
		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
		
	}

	@Override
	public void action() {
		
		Gson gson = gsonb.create();
		
		if(!dst.isEmpty())
		{
			//On envoie le msg en premier
			ProtoPaquet p=new ProtoPaquet();
			p.content=counter.toString();
			p.dest=dst;
			p.src=myAgent.getLocalName();
			AID user=getUserAID(dst);
			ACLMessage jadeMsgInit = new ACLMessage(ACLMessage.INFORM);
			jadeMsgInit.addReceiver(user);
			
			jadeMsgInit.setContent(gson.toJson(p));
			myAgent.send(jadeMsgInit);
			System.out.println(myAgent.getLocalName()+ "envoie le msg initial '"+counter+"' à "+dst);
		}
		
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
		ProtoPaquet mess = gson.fromJson(msg.getContent(), ProtoPaquet.class);
		
		ACLMessage jadeMsg = new ACLMessage(ACLMessage.INFORM);
		counter=Integer.parseInt(mess.content)+1;
		mess.content=String.valueOf(counter);
		dst=mess.src;
		
		mess.dest=dst;
		mess.src=myAgent.getLocalName();
		System.out.println(myAgent.getLocalName()+ "envoie le msg '"+counter+"' à "+dst);
		jadeMsg.addReceiver(getUserAID(dst));
		myAgent.send(jadeMsg);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private AID getUserAID(String name)
	{
		DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType("UserAgent");
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

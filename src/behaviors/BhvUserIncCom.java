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

@SuppressWarnings("serial")
public class BhvUserIncCom extends CyclicBehaviour{
	
	Integer counter;
	String dst=null;
	
	private GsonBuilder gsonb;
	private Gson gson;
	
	public BhvUserIncCom(Agent a, int counter, String dst) {
		super(a);
		gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(AID.class, new AIDSerializer());
				
		this.dst=dst;
		this.counter=counter;
		this.gson = gsonb.create();
				
		if(dst != null && !dst.isEmpty())
		{
			//On envoie le msg en premier
			ProtoPaquet p=new ProtoPaquet();
			p.content= this.counter.toString();
			p.dest=dst;
			p.src=myAgent.getLocalName();
			AID user=getUserAID(dst);
			ACLMessage jadeMsgInit = new ACLMessage(ACLMessage.REQUEST);
			jadeMsgInit.addReceiver(user);
			
			jadeMsgInit.setContent(gson.toJson(p));
			System.out.println(gson.toJson(p));
			myAgent.send(jadeMsgInit);
			System.out.println(myAgent.getLocalName()+ "envoie le msg initial '"+counter+"' à "+dst);
		}
	}

	@Override
	public void action() {
							
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		
		if (msg != null) {
			ProtoPaquet mess = gson.fromJson(msg.getContent(), ProtoPaquet.class);
			
			ACLMessage jadeMsg = msg.createReply();
			System.out.println("messg contents " + msg.getContent());
			counter=Integer.parseInt(mess.content)+1;
			mess.content=String.valueOf(counter);
			dst=mess.src;
			
			mess.dest=dst;
			mess.src=myAgent.getLocalName();
			jadeMsg.setContent(gson.toJson(mess));
			System.out.println(myAgent.getLocalName()+ "envoie le msg '"+counter+"' à "+dst);
			
			myAgent.send(jadeMsg);
		} 
		
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

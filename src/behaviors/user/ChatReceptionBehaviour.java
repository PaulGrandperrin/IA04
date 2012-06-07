package behaviors.user;

import messages.AIDSerializer;
import messages.ProtoPaquet;
import agents.BaseAgent;
import agents.UserAgent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ChatReceptionBehaviour extends CyclicBehaviour {
		BaseAgent myAgent;
		Integer counter;
		String dst=null;
		
		private GsonBuilder gsonb;
		private Gson gson;
		private boolean sentInitialMessage = false;
		
		public ChatReceptionBehaviour(Agent a, int counter) {
			super(a);
			myAgent = (BaseAgent)a;
			myAgent.log("création du behavior ChatReception");
			gsonb = new GsonBuilder();
			gsonb.registerTypeAdapter(AID.class, new AIDSerializer());					

			this.gson = gsonb.create();
		}
	
		public void action() {
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
			
			 
			if (msg != null) {
				ProtoPaquet mess = gson.fromJson(msg.getContent(), ProtoPaquet.class);
				
				if(mess.dest.equals(myAgent.getLocalName())) {					
					GuiEvent e = new GuiEvent(this, UserAgent.RECEIVED_MESSAGE_EVENT);
					e.addParameter(mess.content);
					myAgent.postGuiEvent(e);
				}
			}
			
			block(5000);
		}
	

}

package agents;

import java.util.List;

import com.google.gson.GsonBuilder;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public abstract class BaseAgent extends Agent {
	private static final long serialVersionUID = 1L;
	private List<String> LinkTable;
	private GsonBuilder gsonb;

	public BaseAgent() {
		gsonb = new GsonBuilder();
	}
	
	public List<String> getLinkTable() {
		return LinkTable;
	}

	public void setLinkTable(List<String> linkTable) {
		System.out.println("LinkTable of SwitchAgent "+getLocalName()+" changed to ");
		for (String s:linkTable)
		{
			System.out.print(s+",");
		}
		System.out.println();
		LinkTable = linkTable;
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
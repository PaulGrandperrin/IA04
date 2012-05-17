package projet.main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Runtime rt = Runtime.instance();
			Profile p = new ProfileImpl("agent.properties");
			AgentContainer mainC = rt.createMainContainer(p);
			AgentController masterAgent = mainC.createNewAgent("Master", "agents.MasterAgent", new Object[]{});
			masterAgent.start();
			
			AgentController switch1 = mainC.createNewAgent("switch1", "agents.SwitchAgent", new Object[]{});
			switch1.start();
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}

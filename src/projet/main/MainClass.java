package projet.main;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Runtime rt = Runtime.instance();
			Profile p = new ProfileImpl("agent.properties");
			AgentContainer mainC = rt.createMainContainer(p);
			
			//Lancement des agents switch
			Map<AID, List<AID>> graphAgent=createSwitchsFromOntologie(mainC,"/mesCouilles.txt");
			
			
			//Lancement de l'agent master avec en argument la liste des agents switch+ leurs liens.
			AgentController masterAgent = mainC.createNewAgent("Master", "agents.MasterAgent", new Object[]{graphAgent});
			masterAgent.start();
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	private static Map<AID, List<AID>> createSwitchsFromOntologie(AgentContainer ac,String path) throws StaleProxyException
	{
		/*
		 * Ici, vous êtes priés de lire le fichier d'ontologie, de construire et de lancer les agents.
		 * Et en code de retour vous renvoyez la liste des liens des agents.
		 * Merci Bien
		 * Tchao bonsoir
		 */
		
		
//		Map<AID, List<AID>> a = new HashMap<AID, List<AID>>();
//		for(AID key : a.keySet()){ 
//			a.put(key,new ArrayList<AID>());
//		}
		
		AgentController switch1 = ac.createNewAgent("switch1", "agents.SwitchAgent", new Object[]{});
		switch1.start();
		return null;
		
	
	}

}

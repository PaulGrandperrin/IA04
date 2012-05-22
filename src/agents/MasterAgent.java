package agents;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import gui.NetworkGraphFrame;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import behaviors.PacketProcessingBhv;

public class MasterAgent extends GuiAgent {
	private static final long serialVersionUID = -6011994599868680168L;

	
	protected void setup() {
		//
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("MasterAgent");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		
		addBehaviour(new PacketProcessingBhv(this,1000));
		NetworkGraphFrame gui = new NetworkGraphFrame(this);
	}


	protected void queryKnowledgeBase(String filename) {
		OntModel model = ModelFactory.createOntologyModel();
		model.read("file:" + filename, null, "N3");
		//model.write(System.out);
		
		
		//Query qu = QueryFactory.read("./requete_base_sur.rq");
		String req1 = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> select ?user where {?user lgdp:continent .}";
		Query qu = QueryFactory.create(req1) ;
		
		QueryExecution qexec = QueryExecutionFactory.create(qu, model) ;
		ResultSet results = qexec.execSelect() ;
		System.out.println(results.hasNext());
		while(results.hasNext()){
			QuerySolution sol = results.next() ;
			//Resource property = sol.getResource("user") ;
			Resource property = sol.getResource("chose") ;
			System.out.println(property.getLocalName());
		}

		qexec.close() ;

	}
	
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

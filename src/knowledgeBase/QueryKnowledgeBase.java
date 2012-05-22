package knowledgeBase;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class QueryKnowledgeBase {

	String fileName;
	public QueryKnowledgeBase(String filename) {
		this.fileName = filename;
	}
	
	public Graph<String, String> getGraph() {
		Graph<String, String> graph = new SparseMultigraph<String, String>();
		
		OntModel model = ModelFactory.createOntologyModel();
		model.read("file:"+fileName, null, "N3");
		
		Query qu = QueryFactory.read("requete_base_sur.rq");
		
		QueryExecution qexec = QueryExecutionFactory.create(qu, model) ;
		ResultSet results = qexec.execSelect() ;
		
		int i = 0;
		while(results.hasNext()){
			QuerySolution sol = results.next() ;
			Resource property = sol.getResource("machine");
			Resource property2 = sol.getResource("autre_machine") ;

			String src = property.getLocalName();
			String dest = property2.getLocalName();
			
			if(!graph.containsVertex(src))
				graph.addVertex(src);
			
			if(!graph.containsVertex(dest))
				graph.addVertex(dest);
			
			graph.addEdge("Edge" + i, src, dest);
			i++;
			System.out.println(property.getLocalName() + " -> " + property2.getLocalName());
		}

		qexec.close() ;

		return graph;
	}
	
	
	public static void main(String[] agrs) {
		QueryKnowledgeBase q = new QueryKnowledgeBase("./network.n3");
		q.getGraph();
	}

}

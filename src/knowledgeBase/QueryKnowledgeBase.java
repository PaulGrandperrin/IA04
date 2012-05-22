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

public class QueryKnowledgeBase {

	String fileName;
	public QueryKnowledgeBase(String filename) {
		this.fileName = filename;
	}
	
	public void getGraph() {
		
		OntModel model = ModelFactory.createOntologyModel();
		model.read("file:"+fileName, null, "N3");
		
		Query qu = QueryFactory.create(" PREFIX reseau:  <http://utc.fr/projetIA04reseau#>"+
										"SELECT ?machine ?autre_machine where {?machine reseau:linked_to ?autre_machine.}");
		
		QueryExecution qexec = QueryExecutionFactory.create(qu, model) ;
		ResultSet results = qexec.execSelect() ;
		while(results.hasNext()){
			QuerySolution sol = results.next() ;
			Resource property = sol.getResource("machine");
			Resource property2 = sol.getResource("autre_machine") ;

			System.out.println(property.getLocalName() + " -> " + property2.getLocalName());
		}

		qexec.close() ;

	}
	
	
	public static void main(String[] agrs) {
		QueryKnowledgeBase q = new QueryKnowledgeBase("./network.n3");
		q.getGraph();
	}

}

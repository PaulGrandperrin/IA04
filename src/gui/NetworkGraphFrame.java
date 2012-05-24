package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import knowledgeBase.QueryKnowledgeBase;

import agents.MasterAgent;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.samples.SimpleGraphDraw;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

@SuppressWarnings("serial")
public class NetworkGraphFrame extends JFrame {
	MasterAgent ag;

	public NetworkGraphFrame(MasterAgent agent) {
		super();
		this.setSize(640, 480);
		this.setVisible(true);
		this.ag = agent;
		QueryKnowledgeBase kb = new QueryKnowledgeBase("./network.n3");
		Graph<String, String> g = kb.getGraph();
		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<String, String> layout = new CircleLayout<String, String>(g);
		layout.setSize(new Dimension(300,300)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<String,String> vv = new BasicVisualizationServer<String,String>(layout);
		vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

		this.getContentPane().add(vv);
		this.pack();
		this.setTitle("Graphe basique");
	}
}

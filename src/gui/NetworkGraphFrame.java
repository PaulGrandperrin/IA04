package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

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
		
		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		// Add some vertices. From above we defined these to be type Integer.
		g.addVertex((Integer)1);
		g.addVertex((Integer)2);
		g.addVertex((Integer)3);
		g.addVertex((Integer)4);
		// Add some edges. From above we defined these to be of type String
		// Note that the default is for undirected edges.
		g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes primitives
		g.addEdge("Edge-B", 2, 3);
		g.addEdge("Edge-C", 2, 4);
		
		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<Integer, String> layout = new CircleLayout<Integer, String>(g);
		layout.setSize(new Dimension(300,300)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
		vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
		
		this.getContentPane().add(vv);
		this.pack();
		this.setTitle("Graphe basique");
	}
}

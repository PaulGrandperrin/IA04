package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import knowledgeBase.QueryKnowledgeBase;
import agents.MasterAgent;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

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
		VisualizationViewer<String,String> vv = new VisualizationViewer<String,String>(layout);
		
		vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		/*
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(gm);
		*/
		EditingModalGraphMouse<String, String> em = new EditingModalGraphMouse<String, String>(
													    vv.getRenderContext(), null, null, 0, 0);
		vv.setGraphMouse(em);
		this.getContentPane().add(vv);
		this.pack();
		this.setTitle("Graphe basique");
	}
}

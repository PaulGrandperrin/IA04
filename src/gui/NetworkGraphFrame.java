package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;

import javax.swing.JFrame;

import knowledgeBase.KBMachine;
import knowledgeBase.QueryKnowledgeBase;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import agents.MasterAgent;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

@SuppressWarnings({ "serial", "rawtypes" })
public class NetworkGraphFrame extends JFrame implements Factory {
	MasterAgent ag;

	public VisualizationViewer<String,String> vv;
	
	public NetworkGraphFrame(final MasterAgent agent) {
		super();
		this.setSize(640, 480);
		this.setVisible(true);
		this.ag = agent;
		final QueryKnowledgeBase kb = new QueryKnowledgeBase("./network.n3");
		final Graph<String, String> g = kb.getGraph();
		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<String, String> layout = new CircleLayout<String, String>(g);
		layout.setSize(new Dimension(300,300)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		vv = new VisualizationViewer<String,String>(layout);
		
		Transformer<String,Paint> vertexPaint = new Transformer<String,Paint>() {
			public Paint transform(String s) {
				if (kb.getMachineMap().get(s) == null)
					return Color.RED;
				
				if (kb.getMachineMap().get(s).machineType == KBMachine.type.USER) {
					return Color.GREEN;	
				} else {
					return Color.RED;
				}
			
			}
		};
				
		Transformer<String,Paint> edgePaint = new Transformer<String,Paint>() {
			public Paint transform(String s) {
				Pair<String> p = g.getEndpoints(s);
				
				if(agent.connectionBetweenEdges(p.getFirst(), p.getSecond())) {
					return Color.red;
				} else
					return Color.black;
			}
		};
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		
		vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
		
		/*
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(gm);
		*/
		EditingModalGraphMouse<String, String> em = new EditingModalGraphMouse<String, String>(
													    vv.getRenderContext(), new Factory<String>() {
															public String create() {
																return "Vertex_i";
															}
														}, this, 0, 0);
		vv.setGraphMouse(em);
		this.getContentPane().add(vv);
		this.pack();
		this.setTitle("Graphe basique");

	}

	// gestion des edge
	public Object create() {
		System.out.println("asked to create an edge");
		return "Edge-i";		
	}
	
	
}

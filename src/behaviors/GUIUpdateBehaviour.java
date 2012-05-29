package behaviors;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import edu.uci.ics.jung.visualization.VisualizationViewer;

@SuppressWarnings("serial")
public class GUIUpdateBehaviour extends WakerBehaviour {

	VisualizationViewer<String,String> vv;
	public GUIUpdateBehaviour(Agent a, long timeout, VisualizationViewer<String,String> vv) {
		super(a, timeout);
		this.vv = vv;
	}


	protected void onWake() {
		vv.repaint();
		reset(1000);
	}
}

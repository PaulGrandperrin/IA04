package behaviors;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

public class PacketProcessingBhv extends WakerBehaviour {

	private static final long serialVersionUID = -8645230578588902973L;

	public PacketProcessingBhv(Agent a, long timeout) {
		super(a, timeout);
		System.out.println("Hi");
	}

}

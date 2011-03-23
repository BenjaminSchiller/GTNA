package gtna.routing.twoPhase;

import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.node.IDNode;
import gtna.routing.node.identifier.Identifier;

import java.util.Random;

public class TwoPhaseGreedy extends TwoPhase implements RoutingAlgorithm {
	public TwoPhaseGreedy() {
		super("TWO_PHASE_GREEDY", new String[] {}, new String[] {});
	}

	protected TwoPhaseGreedy(String key, String[] configKeys,
			String[] configValues) {
		super(key, configKeys, configValues);
	}

	protected Route phase2(IDNode src, IDNode current, Identifier dest,
			Random rand, Route route) {
		 return Greedy.route(src, current, dest, route);
	}

}

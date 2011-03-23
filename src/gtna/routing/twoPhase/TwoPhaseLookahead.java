package gtna.routing.twoPhase;

import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.lookahead.Lookahead;
import gtna.routing.node.IDNode;
import gtna.routing.node.identifier.Identifier;

import java.util.HashSet;
import java.util.Random;

public class TwoPhaseLookahead extends TwoPhase implements RoutingAlgorithm {

	public TwoPhaseLookahead() {
		super("TWO_PHASE_LOOKAHEAD", new String[] {}, new String[] {});
	}

	protected TwoPhaseLookahead(String key, String[] configKeys,
			String[] configValues) {
		super(key, configKeys, configValues);
	}

	protected Route phase2(IDNode src, IDNode current, Identifier dest, Random rand,
			Route route) {
		return Lookahead.route(src, current, dest, route, new HashSet<IDNode>());
	}

}

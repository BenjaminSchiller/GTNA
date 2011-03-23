package gtna.routing.twoPhase;

import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.routing.IDRouteImpl;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;
import gtna.routing.greedy.Greedy;
import gtna.routing.node.IDNode;
import gtna.routing.node.identifier.Identifier;

import java.util.Random;

public abstract class TwoPhase extends RoutingAlgorithmImpl implements
		RoutingAlgorithm {
	protected TwoPhase(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}

	public boolean applicable(NodeImpl[] nodes) {
		return nodes[0] instanceof IDNode;
	}

	public void init(NodeImpl[] nodes) {
	}

	public Route randomRoute(NodeImpl[] nodes, NodeImpl src, Random rand) {
		IDNode s = (IDNode) src;
		Identifier dest = s.randomID(rand, nodes);
		return this.phase1(s, s, dest, rand, new IDRouteImpl(dest));
	}

	protected Route phase1(IDNode src, IDNode current, Identifier dest,
			Random rand, Route route) {
		route.add(current);
		if (current.contains(dest)) {
			route.setSuccess(true);
			return route;
		}

		IDNode max = null;
		int d = current.out().length + current.in().length;
		Node[] out = current.out();
		for (int i = 0; i < out.length; i++) {
			if (out[i].out().length + out[i].in().length > d) {
				max = (IDNode) out[i];
				d = out[i].out().length + out[i].in().length;
			}
		}
		if (max == null) {
			route.incMessages();
			return Greedy.route(src, current, dest, route);
		} else {
			route.incMessages();
			return this.phase1(src, max, dest, rand, route);
		}
	}

	protected abstract Route phase2(IDNode src, IDNode current,
			Identifier dest, Random rand, Route route);
}

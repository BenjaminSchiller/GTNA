package gtna.routing.greedy;

import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;
import gtna.routing.node.IDNode;
import gtna.routing.node.identifier.Identifier;

import java.util.Random;

public class Greedy extends RoutingAlgorithmImpl implements RoutingAlgorithm {
	public Greedy() {
		super("GREEDY", new String[] {}, new String[] {});
	}

	public boolean applicable(NodeImpl[] nodes) {
		return nodes[0] instanceof IDNode;
	}

	public void init(NodeImpl[] nodes) {
		return;
	}

	public Route randomRoute(NodeImpl[] nodes, NodeImpl src, Random rand) {
		IDNode SRC = (IDNode) src;
		Identifier DEST = ((IDNode) nodes[rand.nextInt(nodes.length)])
				.randomID(rand, nodes);
		while (SRC.contains(DEST)) {
			DEST = ((IDNode) nodes[rand.nextInt(nodes.length)]).randomID(rand,
					nodes);
		}
		return route(SRC, SRC, DEST, new RouteImpl());
	}

	public static Route route(IDNode src, IDNode current, Identifier dest,
			Route route) {
		route.add(current);
		if (current.contains(dest)) {
			route.setSuccess(true);
			return route;
		}

		Node[] out = current.out();
		double minDist = current.dist(dest);
		IDNode nextHop = null;
		for (int i = 0; i < out.length; i++) {
			IDNode Out = (IDNode) out[i];
			double dist = Out.dist(dest);
			if (dist < minDist) {
				minDist = dist;
				nextHop = Out;
			}
		}
		if (nextHop == null) {
			route.setSuccess(false);
			return route;
		}
		route.incMessages();
		return route(src, nextHop, dest, route);
	}
}

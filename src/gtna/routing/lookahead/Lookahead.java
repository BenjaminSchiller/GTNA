package gtna.routing.lookahead;

import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;
import gtna.routing.node.IDNode;
import gtna.routing.node.identifier.Identifier;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Lookahead extends RoutingAlgorithmImpl implements RoutingAlgorithm {
	public Lookahead() {
		super("LOOKAHEAD", new String[] {}, new String[] {});
	}

	public boolean applicable(NodeImpl[] nodes) {
		return nodes[0] instanceof IDNode;
	}

	public void init(NodeImpl[] nodes) {

	}

	public Route randomRoute(NodeImpl[] nodes, NodeImpl src, Random rand) {
		IDNode SRC = (IDNode) src;
		Identifier DST = SRC.randomID(rand, nodes);
		while (SRC.contains(DST)) {
			DST = SRC.randomID(rand, nodes);
		}
		return route(SRC, SRC, DST, new RouteImpl(), new HashSet<IDNode>());
	}

	public static Route route(IDNode src, IDNode current, Identifier dest, Route route,
			Set<IDNode> seen) {
		seen.add(current);
		route.add(current);
		if (current.contains(dest)) {
			route.setSuccess(true);
			return route;
		}

		Node[] out = current.out();
		double minDist = current.dist(dest);
		IDNode nextHop = null;
		int outIndex = -1;
		for (int i = 0; i < out.length; i++) {
			IDNode Out = (IDNode) out[i];
			if (Out.dist(dest) < minDist && !seen.contains(Out)) {
				minDist = Out.dist(dest);
				nextHop = Out;
				outIndex = i;
			}
			Node[] lookahead = Out.out();
			for (int j = 0; j < lookahead.length; j++) {
				IDNode Lookahead = (IDNode) lookahead[j];
				if (Lookahead.dist(dest) < minDist && !seen.contains(Out)) {
					minDist = Lookahead.dist(dest);
					nextHop = Out;
					outIndex = i;
				}
			}
		}
		for (int i = 0; i < outIndex; i++) {
			IDNode Out = (IDNode) out[i];
			if (Out.dist(dest) <= minDist && seen.contains(Out)) {
				route.incMessages(2);
				break;
			}
			Node[] lookahead = Out.out();
			for (int j = 0; j < lookahead.length; j++) {
				IDNode Lookahead = (IDNode) lookahead[j];
				if (Lookahead.dist(dest) <= minDist && seen.contains(Out)) {
					route.incMessages(2);
					break;
				}
			}
		}
		if (nextHop == null) {
			route.setSuccess(false);
			return route;
		}
		route.incMessages();
		return route(src, nextHop, dest, route, seen);
	}
}

package gtna.routing.twoPhase;

import gtna.graph.NodeImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.node.IDNode;
import gtna.routing.node.RingNode;
import gtna.routing.node.identifier.RingID;

import java.util.HashSet;
import java.util.Set;

public class TwoPhaseLookaheadRegistration extends TwoPhaseLookahead implements
		RoutingAlgorithm {
	public TwoPhaseLookaheadRegistration() {
		super("TWO_PHASE_LOOKAHEAD_REGISTRATION", new String[] {},
				new String[] {});
	}

	public boolean applicable(NodeImpl n) {
		return n instanceof RingNode;
	}

	public void init(NodeImpl[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			this.register((RingNode) nodes[i]);
		}
	}

	private void register(RingNode n) {
		this.registerPhase1(n, ((RingNode) n).getID());
	}

	private void registerPhase1(RingNode current, RingID id) {
		NodeImpl[] out = current.out();
		NodeImpl max = null;
		int maxD = current.out().length + current.in().length;
		for (int i = 0; i < out.length; i++) {
			if (maxD < out[i].out().length + out[i].in().length) {
				max = out[i];
				maxD = out[i].out().length + out[i].in().length;
			}
		}
		if (max == null) {
			this.registerPhase2(current, id,
					new HashSet<RingNode>());
		} else {
			this.registerPhase1((RingNode) max, id);
		}
	}

	private void registerPhase2(RingNode n, RingID id,
			Set<RingNode> seen) {
		seen.add(n);
		NodeImpl[] out = n.out();
		NodeImpl min = null;
		double minD = n.dist(id);
		for (int i = 0; i < out.length; i++) {
			if (((IDNode) out[i]).dist(id) < minD) {
				min = out[i];
				minD = ((IDNode) out[i]).dist(id);
			}
			NodeImpl[] lookahead = out[i].out();
			for (int j = 0; j < lookahead.length; j++) {
				if (((IDNode) lookahead[j]).dist(id) < minD
						&& !seen
								.contains((RingNode) lookahead[j])) {
					min = lookahead[j];
					minD = ((IDNode) lookahead[j]).dist(id);
				}
			}
		}
		if (min == null) {
			n.register(id);
		} else {
			this.registerPhase2((RingNode) min, id, seen);
		}
	}
}

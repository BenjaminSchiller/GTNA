package gtna.routing.flooding;

import gtna.graph.NodeImpl;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;
import gtna.routing.node.IDNode;
import gtna.routing.node.identifier.Identifier;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

public class GreedyFlooding extends RoutingAlgorithmImpl implements
		RoutingAlgorithm {

	public static final String FLOODING_FIRST = "FF";

	public static final String GREEDY_FIRST = "GF";

	private int ttl;

	private String mode;

	public GreedyFlooding(int ttl, String mode) {
		super("GREEDY_FLOODING", new String[] { "TTL", "MODE" }, new String[] {
				"" + ttl, mode });
		this.ttl = ttl;
		this.mode = mode;
	}

	public boolean applicable(NodeImpl[] nodes) {
		return nodes[0] instanceof IDNode;
	}

	public void init(NodeImpl[] nodes) {

	}

	// TODO adapt to return Route object
	public Route randomRoute(NodeImpl[] nodes, NodeImpl src, Random rand) {
		IDNode s = (IDNode) src;
		Identifier dest = s.randomID(rand, nodes);
		// return this.route(s, dest, nodes.length);
		return null;
	}

	private double[] route(IDNode start, Identifier dest, int nodes) {
		int messages = 0;
		boolean[] seen = new boolean[nodes];
		Hashtable<IDNode, ArrayList<IDNode>> paths = new Hashtable<IDNode, ArrayList<IDNode>>();
		ArrayList<IDNode> sp = new ArrayList<IDNode>();
		sp.add(start);
		paths.put(start, sp);
		double[] prog = null;
		LinkedList<IDNode> queue = new LinkedList<IDNode>();
		queue.add(start);
		seen[start.index()] = true;
		while (!queue.isEmpty()) {
			IDNode current = queue.pop();
			ArrayList<IDNode> currentPath = paths.get(current);
			if (current.contains(dest)) {
				prog = this.toProg(currentPath, dest);
			}
			int hop = currentPath.size() - 1;
			if (hop < this.ttl && GREEDY_FIRST.equals(this.mode)
					|| hop >= this.ttl && FLOODING_FIRST.equals(this.mode)) {
				// GREEDY
				double min = current.dist(dest);
				IDNode nextHop = null;
				for (int i = 0; i < current.out().length; i++) {
					IDNode out = (IDNode) current.out()[i];
					if (out.dist(dest) < min) {
						min = out.dist(dest);
						nextHop = out;
					}
				}
				if (nextHop != null) {
					ArrayList<IDNode> p = new ArrayList<IDNode>();
					p.addAll(paths.get(current));
					p.add(nextHop);
					paths.put(nextHop, p);
					seen[nextHop.index()] = true;
					queue.push(nextHop);
					messages++;
				}
			} else if (hop >= this.ttl && GREEDY_FIRST.equals(this.mode)
					|| hop < this.ttl && FLOODING_FIRST.equals(this.mode)) {
				// FLOODING
				for (int i = 0; i < current.out().length; i++) {
					IDNode out = (IDNode) current.out()[i];
					boolean closer = out.dist(dest) < current.dist(dest);
					if (closer && !seen[out.index()]) {
						ArrayList<IDNode> p = new ArrayList<IDNode>();
						p.addAll(paths.get(current));
						p.add(out);
						paths.put(out, p);
						seen[out.index()] = true;
						queue.push(out);
						messages++;
					} else if (closer) {
						messages++;
					}
				}
			}
		}
		return prog;
	}

	private double[] toProg(ArrayList<IDNode> path, Identifier dest) {
		double start = path.get(0).dist(dest);
		double[] prog = new double[path.size()];
		for (int i = 0; i < path.size() - 1; i++) {
			prog[i] = path.get(i).dist(dest) / start;
		}
		prog[prog.length - 1] = 0.0;
		return prog;
	}

}

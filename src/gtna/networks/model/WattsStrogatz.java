package gtna.networks.model;

import java.util.Random;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

public class WattsStrogatz extends NetworkImpl implements Network {
	private int LINKS;

	private double BETA;

	public WattsStrogatz(int nodes, int SUCCESSORS, double BETA,
			RoutingAlgorithm ra, Transformation[] t) {
		super("WATTS_STROGATZ", nodes, new String[] { "SUCCESSORS", "BETA" },
				new String[] { "" + SUCCESSORS, "" + BETA }, ra, t);
		this.LINKS = SUCCESSORS;
		this.BETA = BETA;
	}

	public static WattsStrogatz[] get(int[] n, int s, double b,
			RoutingAlgorithm ra, Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[n.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n[i], s, b, ra, t);
		}
		return nw;
	}

	public static WattsStrogatz[] get(int n, int s[], double b,
			RoutingAlgorithm ra, Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[s.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n, s[i], b, ra, t);
		}
		return nw;
	}

	public static WattsStrogatz[] get(int n, int s, double b[],
			RoutingAlgorithm ra, Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[b.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n, s, b[i], ra, t);
		}
		return nw;
	}

	public Graph generate() {
		Timer timer = new Timer();
		Random rand = new Random(System.currentTimeMillis());
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		Edges edges = new Edges(nodes, this.LINKS * 2 * nodes.length);
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 1; j <= this.LINKS; j++) {
				if (rand.nextDouble() <= this.BETA) {
					NodeImpl dest = nodes[rand.nextInt(nodes.length)];
					while (edges.contains(i, dest.index())) {
						dest = nodes[rand.nextInt(nodes.length)];
					}
					edges.add(nodes[i], dest);
					edges.add(dest, nodes[i]);
				} else {
					NodeImpl succ = nodes[(i + j) % nodes.length];
					edges.add(nodes[i], succ);
					edges.add(succ, nodes[i]);
				}
			}
		}
		edges.fill();
		timer.end();
		Graph g = new Graph(this.description(), nodes, timer);
		return g;
	}
}

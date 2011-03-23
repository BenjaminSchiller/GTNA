package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

/**
 * Implements a network generator for De Bruijn graph, a deterministic network
 * topology with small diameter despite low node degrees.
 * 
 * http://en.wikipedia.org/wiki/De_Bruijn_graph
 * 
 * Parameters are the base and the dimensions. This results in networks with
 * base^{dimensions} nodes.
 * 
 * @author benni
 * 
 */
public class DeBruijn extends NetworkImpl implements Network {
	private int BASE;

	public DeBruijn(int BASE, int DIMENSIONS, RoutingAlgorithm ra,
			Transformation[] t) {
		super("DE_BRUIJN", numberOfNodes(BASE, DIMENSIONS), new String[] {
				"BASE", "DIMENSIONS" }, new String[] { "" + BASE,
				"" + DIMENSIONS }, ra, t);
		this.BASE = BASE;
	}

	public static DeBruijn[] get(int[] b, int d, RoutingAlgorithm ra,
			Transformation[] t) {
		DeBruijn[] nw = new DeBruijn[b.length];
		for (int i = 0; i < b.length; i++) {
			nw[i] = new DeBruijn(b[i], d, ra, t);
		}
		return nw;
	}

	public static DeBruijn[] get(int b, int[] d, RoutingAlgorithm ra,
			Transformation[] t) {
		DeBruijn[] nw = new DeBruijn[d.length];
		for (int i = 0; i < d.length; i++) {
			nw[i] = new DeBruijn(b, d[i], ra, t);
		}
		return nw;
	}

	public static int numberOfNodes(int base, int dimensions) {
		int mod = 1;
		for (int i = 0; i < dimensions; i++) {
			mod *= base;
		}
		return mod;
	}

	public Graph generate() {
		Timer timer = new Timer();
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		Edges edges = new Edges(nodes, this.nodes() * this.BASE - this.BASE);
		for (int i = 0; i < nodes.length; i++) {
			int shiftedId = (i * this.BASE) % nodes.length;
			for (int j = 0; j < this.BASE; j++) {
				if (i != shiftedId) {
					edges.add(nodes[i], nodes[shiftedId]);
				}
				shiftedId++;
			}
		}
		edges.fill();
		System.out.println((this.nodes() * this.BASE - this.BASE) + " == "
				+ edges.size());
		timer.end();
		Graph g = new Graph(this.description(), nodes, timer);
		return g;
	}
}

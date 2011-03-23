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

/**
 * Implements a network generator for the so-called Gilbert model G(n,p). It
 * creates a random graph G(n,p) with a given number of nodes (n). Every
 * possible edge between two nodes ist created with the given possibility (p).
 * While this construction results in basically the same random networks as the
 * Erdos-Renyi model, the number of edges is not fixed fluctuates around the
 * average value of p*n*n.
 * 
 * http://en.wikipedia.org/wiki/Gilbert_Model
 * 
 * Parameters are the number of edges and a flag for the bidirectionality of
 * edges.
 * 
 * Note that in this implementation, loops are not permitted, i.e., there is no
 * edge of the form (a,a).
 * 
 * @author benni
 * 
 */
public class Gilbert extends NetworkImpl implements Network {
	private double EDGES;

	private boolean BIDIRECTIONAL;

	public Gilbert(int nodes, double EDGES, boolean BIDIRECTIONAL,
			RoutingAlgorithm ra, Transformation[] t) {
		super("GILBERT", nodes, new String[] { "EDGES", "BIDIRECTIONAL" },
				new String[] { "" + EDGES, "" + BIDIRECTIONAL }, ra, t);
		this.EDGES = EDGES;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
	}

	public Graph generate() {
		Timer timer = new Timer();
		Random rand = new Random(System.currentTimeMillis());
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		double p = (double) this.EDGES / (double) (this.nodes() * this.nodes());
		if (this.BIDIRECTIONAL) {
			p /= 2;
		}
		Edges edges = new Edges(nodes, (int) (this.EDGES * 1.05));
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i != j) {
					if (rand.nextDouble() < p) {
						if (this.BIDIRECTIONAL) {
							edges.add(nodes[i], nodes[j]);
							edges.add(nodes[j], nodes[i]);
						} else {
							edges.add(nodes[i], nodes[j]);
						}
					}
				}
			}
		}
		edges.fill();
		timer.end();
		return new Graph(this.description(), nodes, timer);
	}
}

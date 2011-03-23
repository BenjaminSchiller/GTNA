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
 * Implements a network generator for GNC, the Growing Network with copying.
 * This network growth model was introduced by Krapivsky and Redner in their
 * paper "Network growth by copying" (2005) which is based on their work with GN
 * and GNR. In each step, a new node joins the network. It connects to a random
 * bootstrap node already in the system and established connections to all
 * neighbors of that node ("copies" the boostrap node's links).
 * 
 * Parameters are a flag for the bidirectionality of links and a flag for the
 * creation of an reverse edge to the bootstrap node. If bidirectionality is
 * true, every edge is added in both directions. if edgeBack is true, an edge
 * between bootstrap node and joining node is established at all times
 * independent from the bidirectionality flag. Note that in the original model
 * proposed by Krapivsky and Redner both parameters would be set to false since
 * all connections are unidirectional.
 * 
 * @author benni
 * 
 */
public class GNC extends NetworkImpl implements Network {
	private boolean BIDIRECTIONAL = false;

	private boolean EDGE_BACK = true;

	public GNC(int nodes, boolean BIDIRECTIONAL, boolean EDGE_BACK,
			RoutingAlgorithm ra, Transformation[] t) {
		super("GNC", nodes, new String[] { "BIDIRECTIONAL", "EDGE_BACK" },
				new String[] { "" + BIDIRECTIONAL, "" + EDGE_BACK }, ra, t);
		this.BIDIRECTIONAL = BIDIRECTIONAL;
		this.EDGE_BACK = EDGE_BACK;
	}

	public Graph generate() {
		Timer timer = new Timer();
		Random rand = new Random(System.currentTimeMillis());
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		Edges edges = new Edges(nodes, 100);
		for (int i = 1; i < nodes.length; i++) {
			NodeImpl bootstrap = nodes[rand.nextInt(i)];
			edges.add(nodes[i], bootstrap);
			if (this.BIDIRECTIONAL || this.EDGE_BACK) {
				edges.add(bootstrap, nodes[i]);
			}
			NodeImpl[] out = bootstrap.out();
			for (int j = 0; j < out.length; j++) {
				edges.add(nodes[i], out[j]);
				if (this.BIDIRECTIONAL) {
					edges.add(out[j], nodes[i]);
				}
			}
		}
		edges.fill();
		timer.end();
		Graph g = new Graph(this.description(), nodes, timer);
		return g;
	}
}

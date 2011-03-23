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
 * Implements a network generator for GN, the Growing Network. This network
 * growth model was introduced by Krapivsky and Redner in their paper
 * "Organization of growing random networks" (2001) together with GNR. In each
 * step, a new node join the network and builds a connection to a randomly
 * chosen node already part of the network.
 * 
 * The only parameter is a flag for bidirectionality. If it is true, every edge
 * is added in both directions. Note that in the original model, this parameter
 * would be set to false as all connections are unidirectional.
 * 
 * @author benni
 * 
 */
public class GN extends NetworkImpl implements Network {
	private boolean BIDIRECTIONAL = false;

	public GN(int nodes, boolean BIDIRECTIONAL, RoutingAlgorithm ra,
			Transformation[] t) {
		super("GNC", nodes, new String[] { "BIDIRECTIONAL", "EDGE_BACK" },
				new String[] { "" + BIDIRECTIONAL }, ra, t);
		this.BIDIRECTIONAL = BIDIRECTIONAL;
	}

	public Graph generate() {
		Timer timer = new Timer();
		Random rand = new Random(System.currentTimeMillis());
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		Edges edges = new Edges(nodes, 100);
		for (int i = 1; i < nodes.length; i++) {
			NodeImpl bootstrap = nodes[rand.nextInt(i)];
			edges.add(nodes[i], bootstrap);
			if (this.BIDIRECTIONAL) {
				edges.add(bootstrap, nodes[i]);
			}
		}
		edges.fill();
		timer.end();
		Graph g = new Graph(this.description(), nodes, timer);
		return g;
	}
}

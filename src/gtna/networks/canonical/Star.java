package gtna.networks.canonical;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.networks.NetworkImpl;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Timer;

/**
 * Implements the network generator for star network of given size. In a star
 * topology, one central node is connected to all other nodes which are not
 * interconnected themselves.
 * 
 * @author benni
 * 
 */
public class Star extends NetworkImpl implements Network {
	public Star(int nodes, RoutingAlgorithm ra, Transformation[] t) {
		super("STAR", nodes, new String[] {}, new String[] {}, ra, t);
	}

	public Graph generate() {
		Timer timer = new Timer();
		NodeImpl[] nodes = new NodeImpl[this.nodes()];
		nodes[0] = new NodeImpl(0);
		NodeImpl[] edges = new NodeImpl[nodes.length - 1];
		NodeImpl[] single = new NodeImpl[] { nodes[0] };
		for (int i = 1; i < nodes.length; i++) {
			nodes[i] = new NodeImpl(i);
			nodes[i].init(single, single);
			edges[i - 1] = nodes[i];
		}
		nodes[0].init(edges, edges);
		timer.end();
		return new Graph(this.description(), nodes, timer);
	}

	public String compareName(Network nw) {
		return Config.get("NETWORK_COMPARE_NODES");
	}
}

package gtna.networks.canonical;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.networks.NetworkImpl;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

/**
 * Implements the network generator for a ring network of given size. In a ring,
 * every node has exactly two undirected connection to its predecessor and
 * successor in a logical ring, forming one cycle.
 * 
 * @author benni
 * 
 */
public class Ring extends NetworkImpl implements Network {
	public Ring(int nodes, RoutingAlgorithm ra, Transformation[] t) {
		super("RING", nodes, new String[] {}, new String[] {}, ra, t);
	}

	public Graph generate() {
		Timer timer = new Timer();
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		for (int i = 0; i < nodes.length; i++) {
			NodeImpl[] edges = new NodeImpl[2];
			edges[0] = nodes[(nodes.length + i - 1) % nodes.length];
			edges[1] = nodes[(i + 1) % nodes.length];
			nodes[i].init(edges, edges);
		}
		timer.end();
		return new Graph(this.description(), nodes, timer);
	}
}

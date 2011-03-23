package gtna.networks.canonical;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

/**
 * Implements the network generator for a complete / fully-connected network of
 * given size. In a fully-connected network, every node has a connection to
 * every other node.
 * 
 * @author benni
 * 
 */
public class Complete extends NetworkImpl implements Network {
	public Complete(int nodes, RoutingAlgorithm ra, Transformation[] t) {
		super("COMPLETE", nodes, new String[] {}, new String[] {}, ra, t);
	}

	public Graph generate() {
		Timer timer = new Timer();
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		for (int i = 0; i < nodes.length; i++) {
			NodeImpl[] edges = new NodeImpl[nodes.length - 1];
			int index = 0;
			for (int j = 0; j < nodes.length; j++) {
				if (j != i) {
					edges[index++] = nodes[j];
				}
			}
			nodes[i].init(edges, edges);
		}
		timer.end();
		return new Graph(this.description(), nodes, timer);
	}
}

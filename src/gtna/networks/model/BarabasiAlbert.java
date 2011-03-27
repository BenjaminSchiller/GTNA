package gtna.networks.model;

import java.util.Random;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

/**
 * Implements the so-called Barabasi Albert network growth model described by
 * Barabasi and Albert in their publication
 * "Emergence of scaling in random networks" (1999).
 * 
 * http://en.wikipedia.org/wiki/Barabasi–Albert_model
 * 
 * Parameters are the initial network size and the number of edges per added
 * node.
 * 
 * @author benni
 * 
 */
public class BarabasiAlbert extends NetworkImpl implements Network {
	private int INIT_NETWORK_SIZE = 10;

	private int EDGES_PER_NODE = 3;

	public BarabasiAlbert(int nodes, int EDGES_PER_NODE, RoutingAlgorithm ra,
			Transformation[] t) {
		super("BARABASI_ALBERT", nodes, new String[] { "EDGES_PER_NODE" },
				new String[] { "" + EDGES_PER_NODE }, ra, t);
		this.EDGES_PER_NODE = EDGES_PER_NODE;
	}

	public Graph generate() {
		Timer timer = new Timer();
		Random rand = new Random(System.currentTimeMillis());
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		int[] in = new int[nodes.length];
		int[] out = new int[nodes.length];

		int initNodes = Math.max(this.INIT_NETWORK_SIZE,
				this.EDGES_PER_NODE + 5);
		int initEdges = initNodes * this.EDGES_PER_NODE;
		Graph temp = new ErdosRenyi(initNodes, initEdges, true, this
				.routingAlgorithm(), this.transformations()).generate();
		Edges edges = new Edges(nodes, initEdges + (nodes.length - initNodes)
				* this.EDGES_PER_NODE);
		for (int i = 0; i < temp.nodes.length; i++) {
			in[i] = temp.nodes[i].in().length;
			out[i] = temp.nodes[i].out().length;
			Node[] Out = temp.nodes[i].out();
			for (int j = 0; j < Out.length; j++) {
				edges.add(nodes[i], nodes[Out[j].index()]);
			}
		}

		int edgeCounter = initEdges;
		for (int i = initNodes; i < nodes.length; i++) {
			int added = 0;
			while (added < this.EDGES_PER_NODE) {
				int dest = rand.nextInt(i);
				if (edges.contains(i, dest)) {
					continue;
				}
				double pi = (double) (in[i] + out[i])
						/ (double) (2 * edgeCounter);
				if (rand.nextDouble() <= pi) {
					in[i]++;
					out[i]++;
					in[dest]++;
					out[dest]++;
					edgeCounter++;
					added++;
				}
			}
		}

		edges.fill();

		timer.end();
		Graph g = new Graph(this.description(), nodes, timer);
		return g;
	}
}

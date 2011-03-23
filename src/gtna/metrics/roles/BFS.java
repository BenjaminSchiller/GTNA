package gtna.metrics.roles;

import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @deprecated
 */
public class BFS {
	// global private variables
	private final int WHITE = 0;
	private final int BLUE = 1;
	private final int RED = 2;
	private int[] color;
	private int[] scc;
	private int counter;
	private ArrayList<Integer> nodeList;
	private Graph g;

	// constructor
	public BFS() {

	}

	// get list index of given node
	private int getIndex(int key) {
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i) == key) {
				return i;
			}
		}
		return -1;
	}

	// Bright First Search
	public void calculateBFS(ArrayList<Integer> nodeList, Graph g) {
		// initialize variables
		this.nodeList = nodeList;
		this.g = g;
		this.color = new int[nodeList.size()];
		this.scc = new int[nodeList.size()];
		int u;
		counter = 0;
		ArrayList<Integer> neighbours;

		// dye all nodes WHITE
		Arrays.fill(color, WHITE);
		// dye start node BLUE
		color[0] = BLUE;
		PriorityQueue<Integer> queue = new PriorityQueue<Integer>();
		// add start node to queue
		queue.add(0);
		int nb;
		while (!queue.isEmpty()) {
			u = queue.poll();
			// assign node neighbors
			neighbours = neighboursOfNode(nodeList.get(u), g);
			// dye each unvisited neighbor BLUE and add it to the queue
			for (int i = 0; i < neighbours.size(); i++) {
				nb = getIndex(neighbours.get(i));
				if (nb > 0) {
					if (color[nb] == WHITE) {
						color[nb] = BLUE;
						queue.add(nb);
					}
				}
			}
			// dye actual node RED
			color[u] = RED;
			scc[u] = counter;
			// check if there are more unvisited nodes if the queue is empty and
			// increase the number of scc's
			if (queue.isEmpty()) {
				for (int i = 0; i < nodeList.size(); i++) {
					// add the first unvisited node to the queue
					if (color[i] == WHITE) {
						color[i] = BLUE;
						counter++;
						queue.add(i);
						i = nodeList.size();
					}
				}
			}
		}
	}

	// calculate the neighbors of given node
	private ArrayList<Integer> neighboursOfNode(int node, Graph g) {
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		// check all incoming links
		Node[] in = g.nodes[node].in();
		for (int i = 0; i < in.length; i++) {
			if (node != in[i].index()) {
				neighbours.add(in[i].index());
			}
		}
		// check all outgoing links
		Node[] out = g.nodes[node].out();
		for (int i = 0; i < out.length; i++) {
			if (node != out[i].index()) {
				neighbours.add(out[i].index());
			}
		}

		return neighbours;
	}

	// return number of scc's
	public int getNumberSCC() {
		return counter + 1;
	}

	// return scc's node-array
	public int[] getSCC() {
		return scc;
	}

}

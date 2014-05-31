/* ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 *
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 *
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 *
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * ---------------------------------------
 * WoTModelX.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Dirk;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.wot;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A model for the Web of Trust
 * 
 * @author Dirk
 * 
 */
public class WoTModelSingleCommunity extends Network {
	private double alpha;
	private double beta;
	private int m;
	private double b;
	private String method;

	private Graph g;
	private Node[] nodes;
	private Edges edges;

	private int nodeCounter;
	private int edgeCounter;

	private int[] dIn;
	private int[] dOut;

	private List<Integer>[] inNeighbors;
	private List<Integer>[] outNeighbors;

	private int bidEdges;
	private int unidEdges;

	private List<Integer> specialNodes;

	// c2 == c2b == c2c

	private boolean c1, c2, c2b, c2c, c3, c3b;
	private boolean optimizeTransitivity;

	private double BID_TOLERANCE = 0.03;

	Random rnd;

	/**
	 * @param nodes
	 * @param m
	 *            insert edges operations per step
	 * @param alpha
	 *            "Special" Node probability
	 * @param beta
	 *            Clustering probability
	 * @param b
	 *            bidirectionality factor
	 * @param transformations
	 */
	public WoTModelSingleCommunity(int nodes, int m, double alpha, double beta, double b,
			String method, Transformation[] t) {
		super("WOTMODELSC", nodes, new Parameter[] { new IntParameter("M", m),
				new DoubleParameter("ALPHA", alpha),
				new DoubleParameter("BETA", beta),
				new DoubleParameter("BIDIRECTIONALITY", b),
				new StringParameter("METHOD", method) }, t);

		this.alpha = alpha;
		this.beta = beta;
		this.method = method;
		this.m = m;
		this.b = b;

		if (method.equals("C1"))
			c1 = true;

		if (method.equals("C2"))
			c2 = true;

		if (method.equals("C3"))
			c3 = true;

		if (method.equals("C2b")) {
			c2 = true;
			c2b = true;
		}

		if (method.equals("C2o")) {
			c2 = true;
			optimizeTransitivity = true;
		}

		if (method.equals("C3b")) {
			c3 = true;
			c3b = true;
		}

	}

	@Override
	public Graph generate() {
		initFields();

		initStartGraph();

		growGraph();

		edges.fill();
		g.setNodes(nodes);
		return g;
	}

	/*
	 * Creates a small star graph to start with.
	 */
	private void initStartGraph() {// TODO gut schlecht ?!
		//System.out.println("INIT_START_GRAPH");

		int n0 = 2 * m + 2;

		nodeCounter = n0;
		edgeCounter = 0;

		for (int i = 1; i <= n0; i++) {
			addEdge(0, i);
			addEdge(i, 0);
		}

	}

	/*
	 * Generates the graph.
	 */
	public void growGraph() {
		//System.out.println("GROW_GRAPH");
		while (nodeCounter < getNodes()) {
			// Add one Node per Step
			addNode();

			if (c1 || c2)
				addEdges();
		}
	}

	/*
	 * Adds a node.
	 */
	private void addNode() {
		//System.out.println(" ADD_NODE");

		if (rnd.nextDouble() < alpha)
			addSpecialNode();
		else if (c3) {
			// int m = rnd.nextInt(this.m * 2 + 1);

			if (rnd.nextDouble() < beta) {
				addNodeWithCopying(m);
			} else
				addNodeWithRandomLinks(m);
		} else
			addPrefAttachementNode(1);
	}

	/*
	 * Adds edges.
	 */
	private void addEdges() {
		//System.out.println(" ADD_EDGES");

		// int m = rnd.nextInt(this.m * 2 + 1);

		if (rnd.nextDouble() < beta) {
			if (c1)
				doClustering1(m);
			else if (c2)
				doClustering2(m);
			else if (c2b) {
				doClustering3(m);
			}
		} else
			addRandomEdges(m);
	}

	/*
	 * Adds a special node.
	 */
	private void addSpecialNode() {
		//System.out.println("  ADD_SPECIAL_NODE");

		specialNodes.add(nodeCounter);

		// Add Anti Preferential Attachement edge;
		double sumAll = 0;

		for (int i = 0; i < nodeCounter; i++)
			sumAll = sumAll + Math.pow(1 / ((double) (dIn[i] + dOut[i])), 2);

		double p[] = new double[nodeCounter];

		for (int i = 0; i < nodeCounter; i++)
			p[i] = (Math.pow(1 / ((double) (dIn[i] + dOut[i])), 2)) / sumAll;

		int node2 = 0;
		double sum = p[0];

		double zz = rnd.nextDouble();

		while (zz > sum) {
			node2++;
			sum += p[node2];
		}

		addEdge(nodeCounter, node2);
		addEdge(node2, nodeCounter);

		nodeCounter++;
	}

	/*
	 * Adds a new node with given number of preferential attachement edges.
	 */
	private void addPrefAttachementNode(int edges) {
		//System.out.println("  ADD_PREFERENTIAL_ATTACHEMENT_NODE");
		for (int i = 0; i < edges; i++)
			addBidPrefAttachementEdge(nodeCounter);
		nodeCounter++;
	}

	/*
	 * Adds a new node that copyings the given numbr of edges from random
	 * bootstrapping node.
	 */
	private void addNodeWithCopying(int edges) {
		//System.out.println("  ADD_NODE_WITH_COPYING");

		int bNode = getRandomNode();

		if (c3b) {
			// TODO Pref Attachement bootstraping node
		}

		// new node signs bootstrapping node
		addEdge(nodeCounter, bNode);

		int inserted = -1;

		while (inserted < edges) {
			// Bootstrapping Node signs new node
			addEdge(bNode, nodeCounter);
			inserted++;

			inserted += copyInEdges(nodeCounter, bNode, edges - inserted);

			// In case not enough neighbors
			bNode = getRandomNode();
		}

		//System.out.println("   -> edges: " + inserted);

		nodeCounter++;
	}

	/*
	 * Adds a new node with given number of random edges.
	 */
	private void addNodeWithRandomLinks(int edges) {
		//System.out.println("  ADD_NODE_WITH_RANDOM_LINKS");

		int firstNode = rnd.nextInt(nodeCounter);

		addEdge(firstNode, nodeCounter);
		addEdge(nodeCounter, firstNode);

		for (int i = 0; i < edges; i++) {
			int randNode = rnd.nextInt(nodeCounter);
			addEdge(nodeCounter, randNode);
			if (mustInsertBidirectional()) {
				addEdge(randNode, nodeCounter);
				i++;
			}
		}

		nodeCounter++;
	}

	/*
	 * Adds given amount of edges in a way to increase clustering coefficient.
	 * Similar top copying models.
	 */
	private void doClustering1(int edges) {
		//System.out.println("  DO_CLUSTERING");
		int inserted = 0;

		while (inserted < edges) {
			int node = getRandomNode();

			while (inserted < edges && dOut[node] < getMaxOutDegree()) {
				inserted += copyFromOutNeighbors(node, edges);

				// New random link, if not enough Out-Neighbors
				if (inserted < edges && dOut[node] < getMaxOutDegree()) {
					addRandomOutEdge(node);
					inserted++;
				}
			}
		}
	}

	private void doClustering2(int edges) {
		//System.out.println("  DO_CLUSTERING");
		int inserted = 0;


		while (inserted < edges) {
			int node = getRandomNode();
			int inserted2 = 0;

			inserted2 += copyFromOutNeighbors(node, 1);

			// TODO quatsch?!
			/*
			if (!c2c)
				// if no edge inserted -> new random link
				if (inserted2 == 0 && dOut[node] < getMaxOutDegree()) {
					addRandomOutEdge(node);
					inserted2++;
					inserted2 += copyFromOutNeighbors(node, 1);
				}
			*/
			

			inserted += inserted2;
		}
	}

	private void doClustering3(int edges) {
		//System.out.println("  DO_CLUSTERING");
		int inserted = 0;

		while (inserted < edges) {
			int node = getRandomNode();

			inserted += copyFromInNeighbors(node, 1);

		}
	}

	private int copyFromOutNeighbors(int src, int edges) {
		//System.out.println("   COPY_FROM_OUT_NEIGHBOR [src=" + src + ", edges="
		//		+ edges + "]");
		int inserted = 0;
		List<Integer> visited = new ArrayList<Integer>();
		while (inserted < edges && visited.size() < dOut[src]) {
			int neighbor = getRandomOutNeighbor(src);
			visited.add(neighbor);
			if (!isSpecialNode(neighbor)) {
				inserted += copyEdges(src, neighbor, edges);
			}
		}
		return inserted;
	}

	private int copyFromInNeighbors(int dst, int edges) {
		//System.out.println("   COPY_FROM_IN_NEIGHBOR [dst=" + dst + ", edges="
		//		+ edges + "]");

		int inserted = 0;
		List<Integer> visited = new ArrayList<Integer>();
		while (inserted < edges && visited.size() < dIn[dst]) {
			int neighbor = getRandomInNeighbor(dst);
			visited.add(neighbor);
			if (!isSpecialNode(neighbor)) {
				inserted += copyInEdges(dst, neighbor, edges);
			}
		}

		return inserted;
	}

	private int copyEdges(int src, int neighbor, int edges) {
		//System.out.println("   COPY_EDGES [src=" + src + ", neighbor="
		//		+ neighbor + "]");
		int inserted = 0;
		List<Integer> visited = new ArrayList<Integer>();
		while (inserted < edges
				&& visited.size() < (/* dIn[neighbor] */+dOut[neighbor])) {
			int dst = getRandomOutNeighbor(neighbor);
			visited.add(dst);
			if (!isSpecialNode(dst))
				inserted += copyEdge(src, neighbor, dst);
		}
		return inserted;
	}

	private int copyInEdges(int dst, int neighbor, int edges) {
		//System.out.println("   COPY_IN_EDGES [src=" + dst + ", neighbor="
		//		+ neighbor + "]");
		int inserted = 0;
		List<Integer> visited = new ArrayList<Integer>();
		while (inserted < edges && visited.size() < dIn[neighbor]) {
			int src = getRandomInNeighbor(neighbor);
			visited.add(src);
			if (!isSpecialNode(src))
				inserted += copyEdge(src, neighbor, dst);
		}
		return inserted;
	}

	private int copyEdge(int src, int neighbor, int dst) {
		//System.out.println("    COPY_EDGE [src=" + src + ", neighbor="
		//		+ neighbor + ", dst=" + dst + "]");

		int inserted = 0;

		if (addEdge(src, dst)) {
			inserted++;
			// TODO gut???
			if (mayInsertBidirectional()) {
				if (edges.contains(dst, neighbor))
					if (addEdge(dst, src))
						inserted++;

			}

		}
		return inserted;
	}

	/*
	 * private int addBidirectionalEdgeIfPossible(int src, int dst) { if
	 * (addEdge(src, dst)) { if (addEdge(dst, src)) return 2; return 1; } return
	 * 0; }
	 */

	/*
	 * Returns a random neighbor of a node (outgoing edge)
	 */
	private int getRandomOutNeighbor(int node) {
		return outNeighbors[node].get(rnd.nextInt(dOut[node]));
	}

	/*
	 * Returns a random neighbor of a node (outgoing edge)
	 */
	private int getRandomInNeighbor(int node) {
		return inNeighbors[node].get(rnd.nextInt(dIn[node]));
	}

	/*
	 * Returns a random neighbor of a node
	 */
	private int getRandomNeighbor(int node) {
		if (rnd.nextDouble() < ((double) dIn[node]) / (dIn[node] + dOut[node]))
			return inNeighbors[node].get(rnd.nextInt(dIn[node]));
		else
			return outNeighbors[node].get(rnd.nextInt(dOut[node]));
	}

	/*
	 * Adds a bidirectional preferential attachement edge to src.
	 */
	private void addBidPrefAttachementEdge(int src) {
		//System.out.println("   ADD_PREFERENTIAL_ATTACHEMENT_EDGE");
		int dst;
		int sum;

		int ss = 0;
		do {
			dst = 0;
			sum = dIn[0] + dOut[0];

			int zz;
			if (edgeCounter > 0)
				zz = rnd.nextInt(edgeCounter * 2); // z <- [0; sum of node
			// degrees)
			else
				zz = 0;

			ss = 0;
			for (int i = 0; i < nodeCounter; i++)
				ss = ss + dIn[i] + dOut[i];

			while (zz > sum) {
				dst++;
				sum += dIn[dst] + dOut[dst];
			}

		} while (addEdgeIfNotSpecial(src, dst) == 0);
		addEdge(dst, src);
	}

	/*
	 * Adds m random edges
	 */
	private void addRandomEdges(int m) {
		//System.out.println("  ADD_RANDOM_EDGES");
		int inserted = 0;
		while (inserted < m) {
			inserted += addRandomEdge();
		}
	}

	/*
	 * Adds a random edge
	 */
	private int addRandomEdge() {
		//System.out.println("   ADD_RANDOM_EDGE");
		int i;
		while ((i = addEdgeIfNotSpecial(rnd.nextInt(nodeCounter),
				rnd.nextInt(nodeCounter))) == 0) {
		}
		return i;
	}

	private int addRandomOutEdge(int src) {
		//System.out.println("     ADD_RANDOM_OUT_EDGE");
		while (!addEdge(src, getRandomNode())) {
		}
		return 1;
	}

	private int getRandomNode() {
		int node;
		do {
			node = rnd.nextInt(nodeCounter);
		} while (isSpecialNode(node));
		return node;
	}

	private int getMaxOutDegree() {
		return nodeCounter - 1 - specialNodes.size();
	}

	/*
	 * initializes required fields
	 */
	private void initFields() {
		//System.out.println("INIT_FIELDS");
		bidEdges = 0;
		unidEdges = 0;

		dIn = new int[this.getNodes()];
		dOut = new int[this.getNodes()];

		inNeighbors = new ArrayList[this.getNodes()];
		outNeighbors = new ArrayList[this.getNodes()];

		for (int i = 0; i < this.getNodes(); i++) {
			inNeighbors[i] = new ArrayList<Integer>(50);
			outNeighbors[i] = new ArrayList<Integer>(50);
		}

		g = new Graph(this.getDescription());
		nodes = Node.init(this.getNodes(), g);
		edges = new Edges(nodes, m * getNodes());

		specialNodes = new ArrayList<Integer>();

		rnd = new Random(System.currentTimeMillis());
	}

	private int addEdgeIfNotSpecial(int src, int dst) {
		if (!isSpecialNode(src) && !isSpecialNode(dst)) {
			if (addEdge(src, dst))
				if (mustInsertBidirectional() && addEdge(dst, src))
					return 2;
			return 1;
		}
		return 0;
	}

	private boolean isSpecialNode(int node) {
		return specialNodes.contains(node);
	}

	/*
	 * Checks whether an edge may be inserted bidirectional
	 */
	private boolean mayInsertBidirectional() {
		return ((((double) bidEdges / (bidEdges + unidEdges) < b
				+ BID_TOLERANCE)));
	}

	/*
	 * Checks whether an edge must be inserted bidirectional
	 */
	private boolean mustInsertBidirectional() {
		return ((((double) bidEdges / (bidEdges + unidEdges) < b
				- BID_TOLERANCE)));
	}

	/*
	 * Adds an Edge
	 */
	private boolean addEdge(int src, int dst) {
		if (src != dst && !edges.contains(src, dst)) {
			edges.add(src, dst);

			dOut[src]++;
			dIn[dst]++;

			outNeighbors[src].add(dst);
			inNeighbors[dst].add(src);

			edgeCounter++;

			if (edges.contains(dst, src)) {
				bidEdges++;
				unidEdges--;

			} else {
				unidEdges++;
			}
			return true;
		}
		return false;
	}
}

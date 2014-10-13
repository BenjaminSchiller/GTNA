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
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.transformation.Transformation;
import gtna.transformation.partition.LargestStronglyConnectedComponent;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A model for the Web of Trust
 * 
 * @author Dirk
 * 
 */
public class WoTModelSingleCommunity extends Network {
	private double alpha1; // Join
	private double beta;
	private double m;
	private double b;

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

	Random rnd;
	private double alpha2; // Copying
	private double alpha3;

	/**
	 * @param nodes
	 *            number of nodes
	 * @param m
	 *            edges per step
	 * @param alpha
	 *            Preferential attachement propability
	 * @param beta
	 *            Copying probability
	 * @param b
	 *            bidirectionality factor
	 * @param transformations
	 */
	public WoTModelSingleCommunity(int nodes, double m, double alpha1,
			double alpha2, double alpha3, double beta, double b,
			Transformation[] t) {
		super("WOTMODELSC", nodes, new Parameter[] {
				new DoubleParameter("M", m),
				new DoubleParameter("ALPHA1", alpha1),
				new DoubleParameter("ALPHA2", alpha2),
				new DoubleParameter("ALPHA3", alpha2),
				new DoubleParameter("BETA", beta),
				new DoubleParameter("BIDIRECTIONALITY", b) }, t);

		this.alpha1 = alpha1;
		this.beta = beta;
		this.m = m;
		this.b = b;
		this.alpha2 = alpha2;
		this.alpha3 = alpha3;

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
	 * Creates a small random graph to start with.
	 */
	private void initStartGraph() {
		int n0 = (int) m;

		LargestStronglyConnectedComponent scc = new LargestStronglyConnectedComponent();
		ErdosRenyi er = new ErdosRenyi(n0, m + 2, false,
				new Transformation[] { scc });

		Graph initGraph = scc.transform(er.generate());

		nodeCounter = initGraph.getNodeCount();

		// Copy
		for (Node n : initGraph.getNodes()) {
			int out[] = n.getOutgoingEdges();

			for (int dst : out)
				addEdge(n.getIndex(), dst);

		}
	}

	/*
	 * Generates the graph.
	 */
	public void growGraph() {
		while (nodeCounter < getNodes()) {
			// Add one Node per Step
			addNode();
			addEdges();
		}
	}

	/*
	 * Adds a node.
	 */
	private void addNode() {
		if (rnd.nextDouble() < alpha1)
			addPrefAttachementNode(1);
		else
			addNodeWithRandomLinks(1);
	}

	/*
	 * Adds edges.
	 */
	private void addEdges() {
		int mm = (int) m;
		double z = rnd.nextDouble();
		if (z < m - mm)
			mm++;

		if (rnd.nextDouble() < beta) {
			doClustering(mm);
		} else
			addRandomEdges(mm);
	}

	/*
	 * Adds a new node with given number of preferential attachement edges.
	 */
	private void addPrefAttachementNode(int edges) {
		for (int i = 0; i < edges; i++)
			addBidPrefAttachementEdge(nodeCounter);
		nodeCounter++;
	}

	/*
	 * Adds a new node with given number of random edges.
	 */
	private void addNodeWithRandomLinks(int edges) {
		int firstNode = rnd.nextInt(nodeCounter);

		addEdge(firstNode, nodeCounter);
		addEdge(nodeCounter, firstNode);

		for (int i = 0; i < edges; i++) {
			int randNode = rnd.nextInt(nodeCounter);
			addEdge(nodeCounter, randNode);
			if (shouldInsertBidirectional()) {
				addEdge(randNode, nodeCounter);
				i++;
			}
		}

		nodeCounter++;
	}

	/*
	 * Adds edges using copying
	 */
	private void doClustering(int edges) {
		int inserted = 0;

		while (inserted < edges) {

			int node = -1;
			if (rnd.nextDouble() < alpha2)
				node = getPANode();
			else
				node = getRandomNode();

			int inserted2 = 0;

			inserted2 += copyFromOutNeighbors(node, 1);

			inserted += inserted2;
		}
	}

	/*
	 * Copys edges from random out-neighbors
	 */
	private int copyFromOutNeighbors(int src, int edges) {
		int inserted = 0;
		List<Integer> visited = new ArrayList<Integer>();
		while (inserted < edges && visited.size() < dOut[src]) {
			int neighbor = -1;

			if (rnd.nextDouble() < alpha3)
				neighbor = getPAOutNeighbor(src);
			else
				neighbor = getRandomOutNeighbor(src);
			visited.add(neighbor);

			inserted += copyEdges(src, neighbor, edges);
		}
		return inserted;
	}

	/*
	 * Coypys edges from given neighbor
	 */
	private int copyEdges(int src, int neighbor, int edges) {
		int inserted = 0;
		List<Integer> visited = new ArrayList<Integer>();
		while (inserted < edges && visited.size() < (dOut[neighbor])) {
			int dst = getRandomOutNeighbor(neighbor);
			visited.add(dst);

			inserted += copyEdge(src, neighbor, dst);
		}
		return inserted;
	}

	/*
	 * Coypys a single edge
	 */
	private int copyEdge(int src, int neighbor, int dst) {
		int inserted = 0;

		if (addEdge(src, dst)) {
			inserted++;
			if (shouldInsertBidirectional()) {
				if (addEdge(dst, src))
					inserted++;

			}

		}
		return inserted;
	}

	/*
	 * Returns a random neighbor of a node (outgoing edge)
	 */
	private int getRandomOutNeighbor(int node) {
		return outNeighbors[node].get(rnd.nextInt(dOut[node]));
	}

	private int getPAOutNeighbor(int node) {
		int sumAll = 0;

		for (int n : outNeighbors[node])
			sumAll += dOut[n];

		int z = rnd.nextInt(sumAll);

		int nId = 0;
		int sum = dOut[outNeighbors[node].get(nId)];

		while (z > sum) {
			nId++;
			sum += dOut[outNeighbors[node].get(nId)];
		}

		return outNeighbors[node].get(nId);

	}

	/*
	 * Adds a bidirectional preferential attachement edge to src.
	 */
	private void addBidPrefAttachementEdge(int src) {
		int dst;
		int sum;

		int ss = 0;
		do {
			dst = 0;
			sum = dIn[0] + dOut[0];

			int zz;
			if (edgeCounter > 0)
				zz = rnd.nextInt(edgeCounter * 2);
			else
				zz = 0;

			ss = 0;
			for (int i = 0; i < nodeCounter; i++)
				ss = ss + dIn[i] + dOut[i];

			while (zz > sum) {
				dst++;
				sum += dIn[dst] + dOut[dst];
			}

		} while (addNewEdge(src, dst) == 0);
		addEdge(dst, src);
	}

	/*
	 * Adds m random edges
	 */
	private void addRandomEdges(int m) {
		int inserted = 0;
		while (inserted < m) {
			inserted += addRandomEdge();
		}
	}

	/*
	 * Adds a random edge
	 */
	private int addRandomEdge() {
		int i;
		while ((i = addNewEdge(rnd.nextInt(nodeCounter),
				rnd.nextInt(nodeCounter))) == 0) {
		}
		return i;
	}

	/*
	 * Returns a rondom node
	 */
	private int getRandomNode() {
		int node;
		node = rnd.nextInt(nodeCounter);
		return node;
	}

	/*
	 * Returns a node using preferential attachement
	 */
	private int getPANode() {
		int node = 0;
		int sum = dOut[0];

		int zz;
		if (edgeCounter > 0)
			zz = rnd.nextInt(edgeCounter);
		else
			zz = 0;

		while (zz > sum) {
			node++;
			sum += dOut[node];
		}

		return node;
	}

	/*
	 * initializes required fields
	 */
	private void initFields() {
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
		edges = new Edges(nodes, (int) (m * getNodes()) + 1);

		rnd = new Random(System.currentTimeMillis());
	}

	/*
	 * Adds a new edge and (possibly) its inverse
	 */
	private int addNewEdge(int src, int dst) {

		if (addEdge(src, dst)) {
			if (shouldInsertBidirectional() && addEdge(dst, src))
				return 2;
			return 1;
		}
		return 0;
	}

	/*
	 * Determines wether an edge should be inserted bidirectional
	 */
	private boolean shouldInsertBidirectional() {
		return ((double) bidEdges / (bidEdges + unidEdges)) < b;
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

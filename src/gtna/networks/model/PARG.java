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
 * PARG.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: truong;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author truong
 * 
 */
public class PARG extends Network {

	private double nAdd;
	private double nDel;
	private double nCut;
	private int numStartNode;

	// variables to algorithms
	// private double[] nodePref;
	private HashMap<String, Point> edgesList;
	private Integer[] nodeDegree;
	private int nCutUsed;

	private int nDelUsed;
	Random nDelRandom = new Random();
	int nDelLow;
	int nDelHigh;
	double nDelProb;
	double nDelCheck = 0;
	int nDelIter = 0;

	private int nAddUsed;
	Random nAddRandom = new Random();
	int nAddLow;
	int nAddHigh;
	double nAddProb;
	double nAddCheck = 0;
	int nAddIter = 0;

	ArrayList<Integer> highestDegreedNodes;
	Random delRandom = new Random();
	Random addRandom = new Random();

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */

	public PARG(int nodes, int numStartNode, double nAdd, double nDel,
			double nCut, Transformation[] t) {
		super("PARG", nodes, new Parameter[] {
				new DoubleParameter("NADD", nAdd),
				new DoubleParameter("NDEL", nDel),
				new DoubleParameter("NCUT", nCut),
				new IntParameter("START_NODES", numStartNode) }, t);
		this.nAdd = nAdd;
		this.nDel = nDel;
		this.nCut = nCut;
		this.numStartNode = numStartNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		edgesList = new HashMap<String, Point>();
		nodeDegree = new Integer[nodes.length];
		for (int i = 0; i < nodeDegree.length; i++) {
			nodeDegree[i] = 0;
		}
		highestDegreedNodes = new ArrayList<Integer>();

		this.calculateParameter();
		int maxIter = 100;
		int temp;

		// generate original random graph
		Network nw = new BarabasiAlbert(this.numStartNode, 3, null);
		Graph startGraph = nw.generate();
		for (Edge e : startGraph.getEdges().getEdges()) {
			int src = e.getSrc();
			int dst = e.getDst();
			this.addEdge(src, dst);
		}
		// ---

		for (int i = this.numStartNode; i < nodeDegree.length; i++) {
			System.out.println("Step " + i);
			// at each time step a new node is added to the network. The new
			// node stochastically makes nAdd number of links with existing
			// nodes
			this.calculateNAddUsed();
			// System.out.println("NAddUsed calculated!");
			for (int j = 0; j < this.nAddUsed; j++) {
				int node = i;
				temp = 0;
				while ((node == i || this.hasEdge(i, node)) && temp < maxIter) {
					node = this.selectNodeUsingPref(i - 1);
				}
				this.addEdge(i, node);
			}
			// System.out.println("Edges added!");

			// after each node addition, probabilisticallly choose and delete
			// nDel number of assortative links in the network
			// 1. choose nCut number of the highest degreed nodes from the
			// network
			// 2. each link in the selected node is stochastically deleted with
			// a probability that is inversely proportional to the degree of
			// that node
			// 3. the actual probability is calculated so that the expected
			// number of link deletions is maintained at nDel
			this.nCutUsed = (int) (1 + this.nCut * i);
			// System.out.println("NCUT: " + nCutUsed);
			this.calculateHighestDegreeNodes(i + 1);
			// System.out.println("Highest calculated");
			for (int highNodeIndex : this.highestDegreedNodes) {
				this.calculateNDelUsed();
				double prob = (double) this.nDelUsed
						/ (this.nodeDegree[highNodeIndex].intValue() * this.nCutUsed);
				for (int neighbor = 0; neighbor <= i; neighbor++) {
					if (this.hasEdge(highNodeIndex, neighbor)
							&& (delRandom.nextDouble() < prob)) {
						this.deleteEdge(highNodeIndex, neighbor);

						// For each deleted link, add two links to the network
						int src;
						if (this.nodeDegree[highNodeIndex].intValue() > this.nodeDegree[neighbor]
								.intValue()) {
							src = highNodeIndex;
						} else {
							src = neighbor;
						}

						Integer[] sorted = new Integer[i + 1];
						for (int j = 0; j < i + 1; j++) {
							sorted[j] = j;
						}
						Arrays.sort(sorted, new DegreeDesc());

						int dst1 = src;
						temp = 0;
						while ((dst1 == src || this.hasEdge(src, dst1)
								&& temp < maxIter)) {
							dst1 = this.chooseNodeUsingDegreeDsc(sorted, i);
							temp++;
						}
						// System.out.println("while 1 done!");

						int dst2 = src;
						temp = 0;
						while ((dst2 == dst1 || dst2 == src || this.hasEdge(
								src, dst2)) && temp < maxIter) {
							dst2 = this.chooseNodeUsingDegreeDsc(sorted, i);
							temp++;
						}
						// System.out.println("while 2 done!");

						this.addEdge(src, dst1);
						this.addEdge(src, dst2);

					}
				}
			}
		}

		System.out.println("NADD : " + this.nAddIter + " -- "
				+ (this.nAddCheck / nAddIter));
		System.out.println("NDEL : " + this.nDelIter + " -- "
				+ (this.nDelCheck / nDelIter));

		// copy generated edges to graph
		Edges edges = new Edges(nodes, this.edgesList.size());
		for (Point p : this.edgesList.values()) {
			edges.add(p.x, p.y);
		}

		edges.fill();
		graph.setNodes(nodes);

		return graph;
	}

	/**
	 * 
	 */
	private void calculateParameter() {
		this.nAddLow = (int) Math.floor(nAdd);
		this.nAddHigh = (int) Math.ceil(nAdd);
		this.nAddProb = ((double) (nAddHigh - nAdd)) / (nAddHigh - nAddLow);
		System.out.println("NADD : " + nAddLow + " -- " + nAddHigh + " -- "
				+ nAddProb);

		this.nDelLow = (int) Math.floor(nDel);
		this.nDelHigh = (int) Math.ceil(nDel);
		this.nDelProb = ((double) (nDelHigh - nDel)) / (nDelHigh - nDelLow);
		System.out.println("NDEL : " + nDelLow + " -- " + nDelHigh + " -- "
				+ nDelProb);
	}

	/**
	 * 
	 */
	private void calculateNDelUsed() {
		if (this.nDelRandom.nextDouble() < nDelProb) {
			this.nDelUsed = nDelLow;
		} else {
			this.nDelUsed = nDelHigh;
		}
		nDelCheck += nDelUsed;
		nDelIter++;
	}

	/**
	 * 
	 */
	private void calculateNAddUsed() {
		if (this.nAddRandom.nextDouble() < nAddProb) {
			this.nAddUsed = nAddLow;
		} else {
			this.nAddUsed = nAddHigh;
		}
		nAddCheck += nAddUsed;
		nAddIter++;
	}

	private int chooseNodeUsingDegreeDsc(Integer[] sorted, int i) {
		int sumOfRank = (i + 1) * (i + 2) / 2;
		double r = addRandom.nextDouble();
		double threshold = r * sumOfRank;
		int sum = 0;
		for (int j = 0; j < i + 1; j++) {
			sum += j + 1;
			if (sum >= threshold) {
				System.out.println("Degree = " + this.nodeDegree[sorted[j]]);
				return sorted[j];
			}
		}

		System.out.println("ERROR: while chosing node using degree dsc!");
		return Integer.MAX_VALUE;
	}

	private void addEdge(int src, int dst) {
		if (src == dst) {
			System.out.println("src = dst: " + src + " --> " + dst);
			return;
		}
		if (this.hasEdge(src, dst)) {
			// System.out.println("Edge is already existed");
			return;
		}
		this.edgesList.put(this.edge(src, dst), new Point(src, dst));
		this.edgesList.put(this.edge(dst, src), new Point(dst, src));
		this.nodeDegree[src]++;
		this.nodeDegree[dst]++;
	}

	private String edge(int src, int dst) {
		return "from " + src + " to " + dst;
	}

	private boolean hasEdge(int src, int dst) {
		if (this.edgesList.containsKey(this.edge(src, dst)))
			return true;
		if (this.edgesList.containsKey(this.edge(dst, src)))
			return true;
		return false;
	}

	private void deleteEdge(int src, int dst) {
		this.edgesList.remove(this.edge(src, dst));
		this.edgesList.remove(this.edge(dst, src));
	}

	/*
	 * private void updatePreference(int nodeIndex) { double degree = (double)
	 * (nodeDegree[nodeIndex]); nodePref[nodeIndex] = 1 + delta *
	 * Math.log(degree); }
	 */

	private Random selectNodeRand = new Random();

	private int selectNodeUsingPref(int maxIndex) {
		double prefSum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			prefSum += nodeDegree[i].intValue();
		}
		double r = selectNodeRand.nextDouble();
		double threshold = r * prefSum;
		double sum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			sum += nodeDegree[i].intValue();
			if (sum >= threshold) {
				return i;
			}
		}
		System.out.println("ERROR: while chosing nodes!!!");
		return (int) (r * maxIndex);
	}

	private void calculateHighestDegreeNodes(int size) {
		this.highestDegreedNodes = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			if (this.highestDegreedNodes.size() < this.nCutUsed) {
				this.highestDegreedNodes.add(i);
			} else {
				int temp = this.getMin(this.highestDegreedNodes);
				if (this.nodeDegree[i].intValue() > this.highestDegreedNodes
						.get(temp).intValue()) {
					this.highestDegreedNodes.remove(temp);
					this.highestDegreedNodes.add(i);
				}
			}
		}
		if (this.highestDegreedNodes.size() != this.nCutUsed) {
			System.out.println("Error when chosing nCut highest degree nodes!");
		}
	}

	private int getMin(ArrayList<Integer> list) {
		int result = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) < list.get(result)) {
				result = i;
			}
		}
		return result;
	}

	private class DegreeDesc implements Comparator<Integer> {
		public int compare(Integer n1, Integer n2) {
			return PARG.this.nodeDegree[n2].intValue()
					- PARG.this.nodeDegree[n1].intValue();
		}
	}
}

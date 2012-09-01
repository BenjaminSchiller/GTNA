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
import java.util.Comparator;
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author truong
 * 
 */
public class PARG extends Network {

	private int nAdd;
	private int nDel;
	private int nCut;

	// variables to algorithms
	// private double[] nodePref;
	private ArrayList<Point> edgesList;
	private double[] nodeDegree;
	ArrayList<Integer> highestDegreedNodes;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */

	public PARG(int nodes, int nAdd, int nDel, int nCut, Transformation[] t) {
		super("PARG", nodes,
				new Parameter[] { new IntParameter("nAdd", nAdd),
						new IntParameter("nDel", nDel),
						new IntParameter("nCut", nCut) }, t);
		this.nAdd = nAdd;
		this.nDel = nDel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		// Graph graph = new Graph(this.getDescription());
		Graph graph = new Graph("test");
		Node[] nodes = Node.init(this.getNodes(), graph);
		edgesList = new ArrayList<Point>();
		nodeDegree = new double[nodes.length];
		highestDegreedNodes = new ArrayList<Integer>();
		// nodePref = new double[nodes.length];

		for (int i = 0; i < nodeDegree.length; i++) {
			// TODO: generate original random graph
			// ---Test---
			if (i < 5) {
				for (int j = 0; j < i; j++) {
					this.addEdge(i, j);
				}
				continue;
			}
			// ---

			Random rand = new Random();

			// at each time step a new node is added to the network. The new
			// node stochastically makes nAdd number of links with existing
			// nodes
			for (int j = 0; j < nAdd; j++) {
				// TODO: are they diff???
				int node = this.selectNodeUsingPref(i - 1, rand);
				this.addEdge(i, node);
			}

			// after each node addition, probabilisticallly choose and delete
			// nDel number of assortative links in the network
			// 1. choose nCut number of the highest degreed nodes from the
			// network
			// 2. each link in the selected node is stochastically deleted with
			// a probability that is inversely proportional to the degree of
			// that node
			// 3. the actual probability is calculated so that the expected
			// number of link deletions is maintained at nDel
			// TODO:
			this.calculateHighestDegreeNodes();
		}

		return null;
	}

	private void addEdge(int src, int dst) {
		edgesList.add(new Point(src, dst));
		nodeDegree[src]++;
		nodeDegree[dst]++;
		// updatePreference(src);
		// updatePreference(dst);
	}

	private void deleteEdge(int src, int dst) {
		for (Point edge : this.edgesList) {
			if (edge.equals(new Point(src, dst))
					|| edge.equals(new Point(dst, src))) {
				this.edgesList.remove(edge);
			}
		}
	}

	/*
	 * private void updatePreference(int nodeIndex) { double degree = (double)
	 * (nodeDegree[nodeIndex]); nodePref[nodeIndex] = 1 + delta *
	 * Math.log(degree); }
	 */

	private int selectNodeUsingPref(int maxIndex, Random rand) {
		double prefSum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			prefSum += nodeDegree[i];
		}
		double r = rand.nextDouble();
		double threshold = r * prefSum;
		double sum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			sum += nodeDegree[i];
			if (sum >= threshold) {
				return i;
			}
		}
		return (int) (r * maxIndex);
	}

	private void calculateHighestDegreeNodes() {
		for (int i = 0; i < this.nodeDegree.length; i++) {
			if (this.highestDegreedNodes.size() < this.nCut) {
				this.highestDegreedNodes.add(i);
			} else {
				int temp = this.getMin(this.highestDegreedNodes);
				if (this.nodeDegree[i] > this.highestDegreedNodes.get(temp)
						.intValue()) {
					this.highestDegreedNodes.remove(temp);
					this.highestDegreedNodes.add(i);
				}
			}
		}
		if (this.highestDegreedNodes.size() != this.nCut) {
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
}

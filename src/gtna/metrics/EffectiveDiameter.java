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
 * EffectiveDiameter.java
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
package gtna.metrics;

import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.graph.sorting.NodeSorter;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Timer;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Implement the Effective Diameter described by Christopher R. Palmer and
 * Phillip B. Gibbons in "Fast Approximation of the "Neighbourhood" Function for
 * Massive Graphs" (2001)
 * 
 * @author truong
 * 
 */
public class EffectiveDiameter extends Metric {

	private int effectiveDiameter;
	private int k;
	private int r;

	private double[] effectiveDiameters;
	private Timer runtime;
	private NodeSorter sorter;

	private boolean isBroken;

	/**
	 * @param key
	 * @param parameters
	 */
	public EffectiveDiameter(int k, int r, NodeSorter sorter) {
		super("EFFECTIVE_DIAMETER", new Parameter[] { new IntParameter("K", k),
				new IntParameter("R", r),
				new StringParameter("SORTER", sorter.getKey()) });
		this.k = k;
		this.r = r;
		this.sorter = sorter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.runtime = new Timer();

		this.effectiveDiameters = new double[g.getNodes().length];
		Random rand = new Random();
		Node[] sorted = sorter.sort(g, rand);

		this.isBroken = false;
		for (int i = 0; i < sorted.length; i++) {
			// TODO:
			if (this.isBroken) {
				this.effectiveDiameters[i] = this.effectiveDiameters[i - 1];
				continue;
			}
			myCalculate(g);
			this.effectiveDiameters[i] = this.effectiveDiameter;
			// remove node
			Node node = sorted[i];
			for (int index : node.getIncomingEdges()) {
				// System.out.println("" + g.getEdges().getEdges().size());

				node.removeIn(index);
				node.removeOut(index);
				g.getNode(index).removeIn(node.getIndex());
				g.getNode(index).removeOut(node.getIndex());

				// g.getEdges().removeEdge(node.getIndex(), index);
				// g.getEdges().removeEdge(index, node.getIndex());

			}
		}

		this.runtime.end();
	}

	public void calculate(Graph g) {
		HashMap<String, boolean[]> M = new HashMap<String, boolean[]>();
		int n = g.getNodes().length;
		int length = (int) (Math.log(n) / Math.log(2) + this.r);
		for (Node u : g.getNodes()) {
			ArrayList<boolean[]> concat = new ArrayList<boolean[]>();
			for (int i = 0; i < this.k; i++) {
				// System.out.println("generateBitmask");
				concat.add(this.generateBitmask(length));
			}

			M.put(this.toKey(u, 0), this.concat(concat));
		}

		System.out.println("Initialized");

		int it = 1;
		int numOfConnectedPairs = this.numOfConnectedPair(g);
		while (it < g.getNodes().length) {
			System.out.println("it = " + it);
			for (Node u : g.getNodes()) {
				M.put(this.toKey(u, it), M.get(this.toKey(u, it - 1)));
			}

			for (Edge e : g.getEdges().getEdges()) {
				int src = e.getSrc();
				int dst = e.getDst();
				// TODO: directed???
				Node u = g.getNode(src);
				Node v = g.getNode(dst);
				M.put(this.toKey(u, it),
						this.myOR(M.get(this.toKey(u, it)),
								M.get(this.toKey(v, it - 1))));
			}

			// estimation
			double sum = 0;
			for (Node u : g.getNodes()) {
				double b = this.getB(M.get(this.toKey(u, it)), length);
				double bias = 1.0 + 0.31 / this.k;
				sum += Math.pow(2, b) / (0.7731 * bias);
			}

			if (sum >= 0.9 * numOfConnectedPairs) {
				this.effectiveDiameter = it;
				return;
			}
			it++;
		}

		System.out.println("Cannot estimated!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.effectiveDiameters,
				"EFFECTIVE_DIAMETER_DIAMETER", folder);
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single RT = new Single("EFFECTIVE_DIAMETER_RUNTIME",
				this.runtime.getRuntime());
		return new Single[] { RT };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	private Random generateRand = new Random();
	private double lambda = 1;

	private boolean[] generateBitmask(int length) {
		boolean[] result = new boolean[length];
		double[] probs = new double[length];
		double probSum = 0;
		for (int i = 0; i < length; i++) {
			probs[i] = lambda * Math.exp(-lambda * i);
			probSum += probs[i];
		}
		double r = generateRand.nextDouble();
		double threshold = r * probSum;
		int index = 0;
		double sum = 0;
		for (int i = 0; i < length; i++) {
			sum += probs[i];
			if (sum >= threshold) {
				index = i;
				break;
			}
		}

		for (int i = 0; i < length; i++) {
			result[i] = false;
		}

		// TODO: from left to right or inverse?
		result[index] = true;

		return result;
	}

	private boolean[] concat(ArrayList<boolean[]> con) {
		int length = 0;
		for (boolean[] m : con) {
			length += m.length;
		}

		if (length == 0) {
			return null;
		}

		int index = 0;
		boolean[] result = new boolean[length];
		for (boolean[] m : con) {
			for (int j = 0; j < m.length; j++) {
				result[index] = m[j];
				index++;
			}
		}
		return result;
	}

	private boolean[] myOR(boolean[] a, boolean[] b) {
		boolean[] result = new boolean[a.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = a[i] || b[i];
		}
		return result;
	}

	private double getB(boolean[] bools, int length) {
		// TODO: lowest order zero bit? left or right???
		ArrayList<boolean[]> temp = this.extract(bools, length);
		int myK = temp.size();
		if (myK != this.k) {
			System.out.println("uncorrect k!!!");
		}
		int sumB = 0;
		for (boolean[] bool : temp) {
			sumB += this.getLowestOrderZeroBit(bool);
		}
		return ((double) sumB) / myK;
	}

	private ArrayList<boolean[]> extract(boolean[] bools, int length) {
		ArrayList<boolean[]> result = new ArrayList<boolean[]>();
		int index = 0;
		while (index + length <= bools.length) {
			boolean[] temp = new boolean[length];
			for (int i = 0; i < length; i++) {
				temp[i] = bools[index + i];
			}
			index += length;
			result.add(temp);
		}
		return result;
	}

	private int getLowestOrderZeroBit(boolean[] bool) {
		int result = 0;
		while (bool[bool.length - 1 - result]) {
			result++;
			if (result == bool.length) {
				break;
			}
		}
		return result;
	}

	private int numOfConnectedPair(Graph g) {
		/*
		 * WeakConnectivityPartition wp = new WeakConnectivityPartition(); g =
		 * wp.transform(g); Partition p = (Partition) g
		 * .getProperty("WEAK_CONNECTIVITY_PARTITION_0");
		 */
		Partition p = WeakConnectivityPartition.getWeakPartition(g,
				new boolean[g.getNodes().length]);
		int[][] components = p.getComponents();
		int numOfComp = components.length;
		int sumOfConnectedPairs = 0;
		for (int i = 0; i < numOfComp; i++) {
			int size = components[i].length;
			// TODO: directed???
			sumOfConnectedPairs += size * (size - 1);
		}
		return sumOfConnectedPairs;
	}

	private String toKey(Node u, int n) {
		return "" + u.getIndex() + " " + n;
	}

	// Test
	private void myCalculate(Graph g) {
		int totalPairs = this.numOfConnectedPair(g);

		Node[] nodes = g.getNodes();
		HashMap<Node, HashMap<String, Node>> neighbors = new HashMap<Node, HashMap<String, Node>>();
		HashMap<Node, HashMap<String, Node>> outside = new HashMap<Node, HashMap<String, Node>>();
		for (Node n : nodes) {
			HashMap<String, Node> neigh = new HashMap<String, Node>();
			HashMap<String, Node> out = new HashMap<String, Node>();
			neigh.put("" + n.getIndex(), n);
			out.put("" + n.getIndex(), n);
			neighbors.put(n, neigh);
			outside.put(n, out);
		}
		for (int i = 1; i < nodes.length; i++) {
			int numOfPairs = 0;
			for (Node n : nodes) {
				HashMap<String, Node> newOut = new HashMap<String, Node>();
				for (Node out : outside.get(n).values()) {
					int[] neighsOfOut = out.getIncomingEdges();
					for (int index : neighsOfOut) {
						neighbors.get(n).put("" + nodes[index].toString(),
								nodes[index]);
						newOut.put("" + nodes[index].getIndex(), nodes[index]);
					}
				}
				outside.put(n, newOut);
				numOfPairs += neighbors.get(n).size() - 1;
			}
			if (numOfPairs >= 0.9 * totalPairs) {
				this.effectiveDiameter = i;
				System.out.println("" + i);
				return;
			}
		}
		System.out.println("Cannot reach 90% number of pairs");
		this.isBroken = true;
	}
}

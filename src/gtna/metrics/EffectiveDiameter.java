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
import gtna.graph.sorting.NodeSorter;
import gtna.io.DataWriter;
import gtna.networks.Network;
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

		for (int i = 0; i < sorted.length; i++) {
			calculate(g);
			this.effectiveDiameters[i] = this.effectiveDiameter;
			// remove node
			Node node = sorted[i];
			for (int index : node.getIncomingEdges()) {
				node.removeIn(index);
				node.removeOut(index);
				g.getNode(index).removeIn(node.getIndex());
				g.getNode(index).removeOut(node.getIndex());
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
				concat.add(this.generateBitmask(length));
			}

			M.put("" + u.toString() + 0, this.concat(concat));
		}

		int it = 1;
		while (it < g.getNodes().length) {
			for (Node u : g.getNodes()) {
				M.put("" + u.toString() + it,
						M.get("" + u.toString() + (it - 1)));
			}

			for (Edge e : g.getEdges().getEdges()) {
				int src = e.getSrc();
				int dst = e.getDst();
				// TODO: directed???
				Node u = g.getNode(src);
				Node v = g.getNode(dst);
				M.put("" + u.toString() + it,
						this.myOR(M.get("" + u.toString() + it),
								M.get("" + v.toString() + (it - 1))));
			}

			// estimation
			double sum = 0;
			for (Node u : g.getNodes()) {
				double b = this.getB(M.get("" + u.toString() + it), length);
				sum += Math.pow(2, b) / (0.7731 * (1 + 0.31 / this.k));
			}

			// TODO: number of connected pairs???
			int numOfConnectedPairs = this.numOfConnectedPair(g);
			if (sum >= numOfConnectedPairs) {
				this.effectiveDiameter = it;
				return;
			}
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

	private boolean[] generateBitmask(int length) {
		boolean[] result = new boolean[length];
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			if (rand.nextInt(2) == 0) {
				result[i] = true;
			} else {
				result[i] = false;
			}
		}
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
		while (bool[result]) {
			result++;
			if (result == bool.length) {
				break;
			}
		}
		return result;
	}

	private int numOfConnectedPair(Graph g) {
		// TODO:
		return 0;
	}
}

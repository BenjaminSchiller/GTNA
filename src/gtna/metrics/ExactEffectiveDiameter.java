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
 * ExactEffectiveDiameter.java
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
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.algorithms.GraphSPallFloyd;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Timer;
import gtna.util.parameter.*;

import java.util.HashMap;
import java.util.Random;

/**
 * @author truong
 * 
 */
public class ExactEffectiveDiameter extends Metric {
	private double[] effectiveDiameters;
	private Timer runtime;
	private NodeSorter sorter;
	private int effectiveDiameter = 0;

	private boolean stop = false;

	public ExactEffectiveDiameter(NodeSorter sorter) {
		super(
				"EXACT_EFFECTIVE_DIAMETER",
				new Parameter[] { new StringParameter("SORTER", sorter.getKey()) });
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

			if (this.stop) {
				this.effectiveDiameter = 0;
			} else {
				this.calculate(g);
			}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.effectiveDiameters,
				"EXACT_EFFECTIVE_DIAMETER_DIAMETER", folder);
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single RT = new Single("EXACT_EFFECTIVE_DIAMETER_RUNTIME",
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

	private void calculate(Graph g) {
		GraphSPallFloyd allpairs = new GraphSPallFloyd(g);
		int sumOfPairs = this.numOfConnectedPair(g);
		System.out.println("Total Pair: " + sumOfPairs);
		for (int distance = 1; distance < g.getNodes().length; distance++) {
			double sum = 0;
			int N = g.getNodes().length;
			for (int i = 0; i < N - 1; i++) {
				for (int j = i + 1; j < N; j++) {
					if (allpairs.dist(i, j) <= distance)
						sum++;
				}
			}
			if (sum >= 0.9 * ((double) sumOfPairs)) {
				this.effectiveDiameter = distance;
				System.out.println("Found: " + distance);
				return;
			}

		}
		System.out.println("Cannot reach 90% number of pairs!");
	}

	private int numOfConnectedPair(Graph g) {
		Partition p = WeakConnectivityPartition.getWeakPartition(g,
				new boolean[g.getNodes().length]);
		int[][] components = p.getComponents();
		int numOfComp = components.length;
		int sumOfConnectedPairs = 0;
		for (int i = 0; i < numOfComp; i++) {
			int size = components[i].length;
			sumOfConnectedPairs += size * (size - 1) / 2;
		}
		if (sumOfConnectedPairs == 0) {
			stop = true;
		}
		return sumOfConnectedPairs;
	}

}

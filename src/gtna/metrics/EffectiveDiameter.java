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
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.graph.sorting.NodeSorter;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * @author truong
 * 
 */
public class EffectiveDiameter extends Metric {

	private NodeSorter sorter;
	private double mixedPercent;
	private Timer runtime;
	private double[][] effectiveDiameter;

	private int mixed;
	private boolean[] excludedNode;
	private int excluded;
	private int numberOfRounds;

	public EffectiveDiameter(NodeSorter sorter, double mixedPercent) {
		super("EFFECTIVE_DIAMETER", new Parameter[] { new StringParameter(
				"SORTER", sorter.getKey()) });
		this.sorter = sorter;
		this.mixedPercent = mixedPercent;
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

		// init variables
		int N = g.getNodes().length;
		this.mixed = (int) (this.mixedPercent * N);
		this.excludedNode = new boolean[N];
		for (int i = 0; i < N; i++) {
			this.excludedNode[i] = false;
		}
		this.excluded = 0;
		this.numberOfRounds = this.mixed + 100;
		this.numberOfRounds = Math.min(this.numberOfRounds, N);
		this.effectiveDiameter = new double[this.numberOfRounds][2];

		// compute
		allPairs = new int[g.getNodes().length][g.getNodes().length];
		Node[] sorted = this.sorter.sort(g, new Random());
		for (int i = 0; i < this.numberOfRounds; i++) {
			System.out.println("==========" + i);
			int d = this.compute(g, i);
			System.out.println("D = " + d);
			this.effectiveDiameter[i][0] = excluded;
			this.effectiveDiameter[i][1] = d;

			// exclude nodes
			if (i < this.mixed) {
				this.excludeNode(i, sorted);
				this.lastRemoved = sorted[i].getIndex();
			} else {
				int percent = i - this.mixed + 1;
				int next = this.mixed - 1 + (percent * (N - this.mixed)) / 100;
				for (int j = this.excluded; j < next; j++) {
					this.excludeNode(j, sorted);
				}
				this.lastRemoved = -1;
			}
		}

		this.runtime.end();
	}

	private int compute(Graph g, int index) {
		int totalConnectedPairs = 0;
		boolean[] seen = this.excludedNode.clone();
		Partition partition = WeakConnectivityPartition.getWeakPartition(g,
				seen);
		int[][] components = partition.getComponents();
		for (int[] comp : components) {
			int add = comp.length * (comp.length - 1);
			totalConnectedPairs += add;
		}
		System.out.println("Total = " + totalConnectedPairs);
		System.out.println("Number of comps " + components.length);
		this.mustBeComputed = 0;
		int diameter = 1;
		if (totalConnectedPairs > 0) {
			while (true) {
				System.out.println("Round " + diameter);
				int connectedPairs = 0;
				for (int[] comp : components) {
					connectedPairs += this.computePairs(g, comp, diameter);
				}
				System.out.println("Connected Pairs = " + connectedPairs);
				if (connectedPairs >= (0.9 * totalConnectedPairs)) {
					break;
				}
				diameter++;
				if (diameter > 10)
					break;
			}
		}
		System.out.println("Comps computed " + this.mustBeComputed);
		return diameter;
	}

	private int[][] allPairs;
	private int lastRemoved = -1;
	private int mustBeComputed;

	/**
	 * calculate number of distances in a component that are smaller than the
	 * diamter
	 * 
	 * @param g
	 *            the graph
	 * @param comp
	 *            the current component
	 * @param diameter
	 *            the diameter
	 * @return the number of distances
	 */
	private int computePairs(Graph g, int[] comp, int diameter) {
		int pairs = 0;

		// for the first check, we must recalculate the distances
		if (diameter == 1) {
			boolean alreadyComputed = true;
			// if nothing was removed or more than 1 node were removed
			if (lastRemoved == -1) {
				alreadyComputed = false;
			} else {
				int temp = allPairs[lastRemoved][comp[0]];
				// if the last removed is connected to the current component
				// then the component must be recomputed
				if (temp > 0) {
					alreadyComputed = false;
				}
			}
			// recomputed
			if (!alreadyComputed) {
				this.mustBeComputed++;
				for (int i = 0; i < comp.length; i++) {
					int iIndex = comp[i];
					Queue<Integer> queue = new LinkedList<Integer>();
					for (int j = 0; j < g.getNodes().length; j++) {
						allPairs[iIndex][j] = -1;
					}
					allPairs[iIndex][iIndex] = 0;
					queue.add(iIndex);
					while (!queue.isEmpty()) {
						Node current = g.getNode(queue.poll());
						for (int outIndex : current.getOutgoingEdges()) {
							if (this.excludedNode[outIndex]) {
								continue;
							}
							if (allPairs[iIndex][outIndex] != -1) {
								continue;
							}
							allPairs[iIndex][outIndex] = allPairs[iIndex][current
									.getIndex()] + 1;
							queue.add(outIndex);
							// System.out.println("from " + iIndex + " to " +
							// outIndex
							// + ": " + allPairs[iIndex][outIndex]);
							// if (allPairs[iIndex][outIndex] <= diameter)
							// pairs++;
						}
					}
				}
			}
		}
		for (int i : comp) {
			for (int j : comp) {
				// System.out.println("" + i + " -> " + j + " : "
				// + allPairs[i][j]);
				if (allPairs[i][j] > 0 && allPairs[i][j] <= diameter)
					pairs++;
			}
		}

		// System.out.println("pairs = " + pairs);
		return pairs;
	}

	private void excludeNode(int index, Node[] sorted) {
		Node nodeToExclude = sorted[index];
		this.excludedNode[nodeToExclude.getIndex()] = true;
		this.excluded++;
		// System.out.println("Exclude " + nodeToExclude.getIndex());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.effectiveDiameter,
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
}

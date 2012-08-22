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
 * ASPL.java
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
import gtna.graph.sorting.NodeSorter;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Timer;
import gtna.util.Util;
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
public class ASPL extends Metric {

	private double[][] ASPL;
	private double[][] AISPL;
	private double[][] diameter;

	private NodeSorter sorter;

	// variable for exclude nodes
	private double mixedPercent;
	private int mixed;
	private int numberOfRound;
	private int excluded;
	private boolean[] excludedNode;

	private Timer runtime;
	private boolean stop;

	public ASPL(NodeSorter sorter, double mixedPercent) {
		super(
				"AVERAGE_SHORTEST_PATH_LENGTH",
				new Parameter[] { new StringParameter("SORTER", sorter.getKey()) });
		this.mixedPercent = mixedPercent;
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

		// init
		int N = g.getNodes().length;
		this.mixed = (int) (this.mixedPercent * N);
		this.excludedNode = new boolean[N];
		for (int i = 0; i < N; i++) {
			this.excludedNode[i] = false;
		}
		this.stop = false;
		this.excluded = 0;
		this.numberOfRound = this.mixed + 100;
		this.numberOfRound = Math.min(this.numberOfRound, N);
		this.ASPL = new double[numberOfRound][2];
		this.AISPL = new double[numberOfRound][2];
		this.diameter = new double[numberOfRound][2];

		// compute
		Node[] sorted = this.sorter.sort(g, new Random());
		for (int i = 0; i < this.numberOfRound; i++) {
			this.compute(g, i);

			// exclude nodes
			if (i < mixed) {
				System.out.println("Remove: " + i);
				this.excludeNode(i, sorted);
			} else {
				int percent = i - this.mixed + 1;
				System.out.println("Remove: " + percent + " %");
				int next = this.mixed - 1 + (percent * (N - this.mixed)) / 100;
				System.out.println("The last excluded Node = "
						+ (this.excluded - 1));
				System.out.println("Remove until = " + (next - 1));
				for (int j = this.excluded; j < next; j++) {
					this.excludeNode(j, sorted);
				}
			}
		}

		this.runtime.end();
	}

	private void compute(Graph g, int index) {
		if (this.stop) {
			this.ASPL[index][0] = this.excluded;
			this.ASPL[index][1] = 0;
			this.AISPL[index][0] = this.excluded;
			this.AISPL[index][1] = 0;
			this.diameter[index][0] = this.excluded;
			this.diameter[index][1] = 0;
			System.out.println("Nothing to compute!");
			return;
		}
		double ASPLsum = 0;
		double AISPLsum = 0;
		double diameter = 0;
		int found = 0;
		Node[] nodes = g.getNodes();

		for (int i = 0; i < nodes.length; i++) {
			if (this.excludedNode[i]) {
				continue;
			}
			// single source shortest path
			Queue<Integer> queue = new LinkedList<Integer>();
			int[] spl = Util.initIntArray(nodes.length, -1);
			spl[i] = 0;
			queue.add(i);

			while (!queue.isEmpty()) {
				Node current = nodes[queue.poll()];
				for (int outIndex : current.getOutgoingEdges()) {
					if (this.excludedNode[outIndex]) {
						continue;
					}
					if (spl[outIndex] != -1) {
						continue;
					}
					spl[outIndex] = spl[current.getIndex()] + 1;
					queue.add(outIndex);
					found++;
					ASPLsum += spl[outIndex];
					AISPLsum += 1.0 / spl[outIndex];
					diameter = Math.max(diameter, spl[outIndex]);
				}
			}
		}
		System.out.println("found = " + found);
		if (found > 0) {
			this.ASPL[index][0] = this.excluded;
			this.ASPL[index][1] = ASPLsum / found;

			int numNodes = nodes.length - this.excluded;
			int totalPairs = numNodes * (numNodes - 1); // 2 ways
			this.AISPL[index][0] = this.excluded;
			this.AISPL[index][1] = AISPLsum / totalPairs;

			this.diameter[index][0] = this.excluded;
			this.diameter[index][1] = diameter;
		} else {
			this.ASPL[index][0] = this.excluded;
			this.AISPL[index][0] = this.excluded;
			this.diameter[index][0] = this.excluded;

			this.ASPL[index][1] = 0;
			this.AISPL[index][1] = 0;
			this.diameter[index][1] = 0;

			this.stop = true;
		}
	}

	/**
	 * delete a node
	 * 
	 * @param index
	 * @param sorted
	 */
	private void excludeNode(int index, Node[] sorted) {
		Node nodeToExclude = sorted[index];
		this.excludedNode[nodeToExclude.getIndex()] = true;
		this.excluded++;
		System.out.println("Node " + nodeToExclude.getIndex() + " was deleted");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.ASPL,
				"AVERAGE_SHORTEST_PATH_LENGTH_ASPL", folder);
		success &= DataWriter.writeWithoutIndex(this.AISPL,
				"AVERAGE_SHORTEST_PATH_LENGTH_AISPL", folder);
		success &= DataWriter.writeWithoutIndex(this.diameter,
				"AVERAGE_SHORTEST_PATH_LENGTH_DIAMETER", folder);
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single RT = new Single("AVERAGE_SHORTEST_PATH_LENGTH_RUNTIME",
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

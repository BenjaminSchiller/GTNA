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
 * AverageShortestPathLength.java
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
public class AverageShortestPathLength extends Metric {

	private double[][] ASPL;
	private double[][] AISPL;
	private double[][] diameters;

	private NodeSorter sorter;
	private boolean[] exclude;
	private int excluded;
	private Timer runtime;

	private boolean stop;
	private Resolution resolution;
	private double mixedPoint;
	private int mixed;

	public static enum Resolution {
		SINGLE, PERCENT, MIXED
	}

	public AverageShortestPathLength(NodeSorter sorter, Resolution resolution,
			double mixedPoint) {
		super(
				"AVERAGE_SHORTEST_PATH_LENGTH",
				new Parameter[] { new StringParameter("SORTER", sorter.getKey()) });
		this.sorter = sorter;
		this.resolution = resolution;
		this.mixedPoint = mixedPoint;
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

		this.mixed = (int) (this.mixedPoint * g.getNodes().length);
		System.out.println("Mixed = " + this.mixed);
		int length;
		if (this.resolution == Resolution.SINGLE) {
			length = g.getNodes().length;
		} else if (this.resolution == Resolution.PERCENT) {
			length = 100;
		} else {
			length = this.mixed + 100;
		}

		this.ASPL = new double[length][2];
		this.AISPL = new double[length][2];
		this.diameters = new double[length][2];

		this.stop = false;

		this.exclude = new boolean[g.getNodes().length];
		for (int i = 0; i < this.exclude.length; i++) {
			this.exclude[i] = false;
		}
		this.excluded = 0;

		Node[] sorted = this.sorter.sort(g, new Random());

		for (int i = 0; i < length; i++) {
			this.compute(g, i);

			// int excludeIndex = sorted[i].getIndex();
			// this.exclude[excludeIndex] = true;
			this.excludeNodes(sorted, i);
		}

		this.runtime.end();
	}

	private void excludeNodes(Node[] sorted, int round) {
		if (this.resolution == Resolution.SINGLE) {
			int excludeIndex = sorted[round].getIndex();
			this.exclude[excludeIndex] = true;
			this.excluded++;
			System.out.println("Removed until " + (this.excluded - 1));
		} else if (this.resolution == Resolution.PERCENT) {

			// PERCENT
			// (round - 1)% of nodes
			int newPercent = (int) (((double) (round + 1)) / 100 * sorted.length);

			for (int i = this.excluded; i < newPercent; i++) {
				int excludeIndex = sorted[i].getIndex();
				this.exclude[excludeIndex] = true;
				this.excluded++;
			}
			System.out.println("Remove from " + this.excluded + " to "
					+ (newPercent - 1));
		} else {
			if (round < this.mixed) {
				int excludeIndex = sorted[round].getIndex();
				this.exclude[excludeIndex] = true;
				this.excluded++;
				System.out.println("Removed until " + (this.excluded - 1));
			} else {
				int percent = round - this.mixed + 1;
				System.out.println("" + percent + "%");

				int nextRemoved = this.mixed - 1
						+ (percent * (sorted.length - this.mixed + 1)) / 100;
				System.out.println("Removed until " + (this.excluded - 1));
				System.out.println("Next Removed = " + nextRemoved);
				for (int i = this.excluded; i < nextRemoved; i++) {
					int excludeIndex = sorted[i].getIndex();
					this.exclude[excludeIndex] = true;
					this.excluded++;
				}
				System.out.println("Removed until " + (this.excluded - 1));
			}
		}
	}

	private void compute(Graph g, int index) {
		if (stop) {
			this.AISPL[index][0] = this.excluded;
			this.ASPL[index][0] = this.excluded;
			this.diameters[index][0] = this.excluded;
			this.AISPL[index][1] = 0;
			this.ASPL[index][1] = 0;
			this.diameters[index][1] = 0;
			return;
		}
		double ASPLsum = 0;
		double AISPLsum = 0;
		int diameter = 0;
		int found = 0;
		Node[] nodes = g.getNodes();

		for (int i = 0; i < nodes.length; i++) {
			if (this.exclude[i]) {
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
					if (this.exclude[outIndex]) {
						continue;
					}
					if (spl[outIndex] != -1) {
						continue;
					}
					spl[outIndex] = spl[current.getIndex()] + 1;
					queue.add(outIndex);
					found++;
					ASPLsum += spl[outIndex];
					AISPLsum += ((double) 1) / spl[outIndex];
					diameter = Math.max(diameter, spl[outIndex]);
				}
			}
		}

		System.out.println("found " + found);
		if (found > 0) {
			this.ASPL[index][0] = this.excluded;
			this.ASPL[index][1] = ASPLsum / found;
			// this.AISPL[index] = AISPLsum / found;
			// System.out.println("AISPLsum = " + AISPLsum);
			// System.out.println("AISPL average = " + (AISPLsum / found));
			int numNodes = nodes.length - this.excluded;
			int totalPairs = numNodes * (numNodes - 1); // 2 ways
			this.AISPL[index][0] = this.excluded;
			this.AISPL[index][1] = AISPLsum / totalPairs;
			this.diameters[index][0] = this.excluded;
			this.diameters[index][1] = diameter;
		} else {
			this.AISPL[index][0] = this.excluded;
			this.ASPL[index][0] = this.excluded;
			this.diameters[index][0] = this.excluded;
			this.AISPL[index][1] = 0;
			this.ASPL[index][1] = 0;
			this.diameters[index][1] = 0;
			this.stop = true;
		}

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
		success &= DataWriter.writeWithoutIndex(this.diameters,
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

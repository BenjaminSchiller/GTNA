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

	private double[] ASPL;
	private double[] AISPL;

	private NodeSorter sorter;
	private boolean[] exclude;
	private Timer runtime;

	public AverageShortestPathLength(NodeSorter sorter) {
		super(
				"AVERAGE_SHORTEST_PATH_LENGTH",
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

		this.ASPL = new double[g.getNodes().length];
		this.AISPL = new double[g.getNodes().length];

		this.exclude = new boolean[g.getNodes().length];
		for (int i = 0; i < this.exclude.length; i++) {
			this.exclude[i] = false;
		}

		Node[] sorted = this.sorter.sort(g, new Random());

		for (int i = 0; i < sorted.length; i++) {
			System.out.println("Round " + i);
			this.compute(g, i);

			int excludeIndex = sorted[i].getIndex();
			this.exclude[excludeIndex] = true;
		}

		this.runtime.end();
	}

	private void compute(Graph g, int index) {
		double ASPLsum = 0;
		double AISPLsum = 0;
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
					AISPLsum += 1 / spl[outIndex];
				}
			}
		}

		System.out.println("found " + found);
		if (found > 0) {
			this.ASPL[index] = ASPLsum / found;
			this.AISPL[index] = 1.0 / (AISPLsum / found);
		} else {
			this.ASPL[index] = 0;
			this.AISPL[index] = 0;
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
		success &= DataWriter.writeWithIndex(this.ASPL,
				"AVERAGE_SHORTEST_PATH_LENGTH_ASPL", folder);
		success &= DataWriter.writeWithIndex(this.AISPL,
				"AVERAGE_SHORTEST_PATH_LENGTH_AISPL", folder);
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

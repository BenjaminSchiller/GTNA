/*
 * ===========================================================
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
 * DegreeDistribution.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.Timer;

import java.util.HashMap;

public class DegreeDistribution extends Metric {
	private Distribution degreeDistribution;

	private Distribution inDegreeDistribution;

	private Distribution outDegreeDistribution;

	private int nodes;

	private int edges;

	private Timer runtime;

	public DegreeDistribution() {
		super("DEGREE_DISTRIBUTION");
	}

	public void computeData(Graph graph, Network nw, HashMap<String, Metric> m) {
		this.runtime = new Timer();

		double[] dd = new double[this.maxDegree(graph) + 1];
		double[] ddi = new double[this.maxInDegree(graph) + 1];
		double[] ddo = new double[this.maxOutDegree(graph) + 1];

		for (Node n : graph.getNodes()) {
			dd[n.getDegree()]++;
			ddi[n.getInDegree()]++;
			ddo[n.getOutDegree()]++;
		}
		for (int i = 0; i < dd.length; i++) {
			dd[i] /= (double) graph.getNodes().length;
		}
		for (int i = 0; i < ddi.length; i++) {
			ddi[i] /= (double) graph.getNodes().length;
		}
		for (int i = 0; i < ddo.length; i++) {
			ddo[i] /= (double) graph.getNodes().length;
		}

		this.degreeDistribution = new Distribution(dd);
		this.inDegreeDistribution = new Distribution(ddi);
		this.outDegreeDistribution = new Distribution(ddo);

		this.nodes = graph.getNodes().length;
		this.edges = graph.generateEdges().length;

		this.runtime.end();
	}

	private int maxDegree(Graph graph) {
		int max = 0;
		for (Node n : graph.getNodes()) {
			max = Math.max(max, n.getDegree());
		}
		return max;
	}

	private int maxInDegree(Graph graph) {
		int max = 0;
		for (Node n : graph.getNodes()) {
			max = Math.max(max, n.getInDegree());
		}
		return max;
	}

	private int maxOutDegree(Graph graph) {
		int max = 0;
		for (Node n : graph.getNodes()) {
			max = Math.max(max, n.getOutDegree());
		}
		return max;
	}

	public Value[] getValues() {
		Value nodes = new Value("DEGREE_DISTRIBUTION_NODES", this.nodes);
		Value edges = new Value("DEGREE_DISTRIBUTION_EDGES", this.edges);

		Value degreeMin = new Value("DEGREE_DISTRIBUTION_DEGREE_MIN",
				this.degreeDistribution.getMin());
		Value degreeMed = new Value("DEGREE_DISTRIBUTION_DEGREE_MED",
				this.degreeDistribution.getMedian());
		Value degreeAvg = new Value("DEGREE_DISTRIBUTION_DEGREE_AVG",
				this.degreeDistribution.getAverage());
		Value degreeMax = new Value("DEGREE_DISTRIBUTION_DEGREE_MAX",
				this.degreeDistribution.getMax());

		Value inDegreeMin = new Value("DEGREE_DISTRIBUTION_IN_DEGREE_MIN",
				this.inDegreeDistribution.getMin());
		Value inDegreeMed = new Value("DEGREE_DISTRIBUTION_IN_DEGREE_MED",
				this.inDegreeDistribution.getMedian());
		Value inDegreeAvg = new Value("DEGREE_DISTRIBUTION_IN_DEGREE_AVG",
				this.inDegreeDistribution.getAverage());
		Value inDegreeMax = new Value("DEGREE_DISTRIBUTION_IN_DEGREE_MAX",
				this.inDegreeDistribution.getMax());

		Value outDegreeMin = new Value("DEGREE_DISTRIBUTION_OUT_DEGREE_MIN",
				this.outDegreeDistribution.getMin());
		Value outDegreeMed = new Value("DEGREE_DISTRIBUTION_OUT_DEGREE_MED",
				this.outDegreeDistribution.getMedian());
		Value outDegreeAvg = new Value("DEGREE_DISTRIBUTION_OUT_DEGREE_AVG",
				this.outDegreeDistribution.getAverage());
		Value outDegreeMax = new Value("DEGREE_DISTRIBUTION_OUT_DEGREE_MAX",
				this.outDegreeDistribution.getMax());

		Value runtime = new Value("DEGREE_DISTRIBUTION_RUNTIME",
				this.runtime.getRuntime());

		return new Value[] { nodes, edges, degreeMin, degreeMed, degreeAvg,
				degreeMax, inDegreeMin, inDegreeMed, inDegreeAvg, inDegreeMax,
				outDegreeMin, outDegreeMed, outDegreeAvg, outDegreeMax, runtime };
	}

	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.degreeDistribution.getDistribution(),
				"DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.degreeDistribution.getCdf(),
				"DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.inDegreeDistribution.getDistribution(),
				"DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(
				this.inDegreeDistribution.getCdf(),
				"DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.outDegreeDistribution.getDistribution(),
				"DEGREE_DISTRIBUTION_OUT_DEGREE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(
				this.outDegreeDistribution.getCdf(),
				"DEGREE_DISTRIBUTION_OUT_DEGREE_DISTRIBUTION_CDF", folder);
		return success;
	}

	/**
	 * @return the degreeDistribution
	 */
	public Distribution getDegreeDistribution() {
		return this.degreeDistribution;
	}

	/**
	 * @return the inDegreeDistribution
	 */
	public Distribution getInDegreeDistribution() {
		return this.inDegreeDistribution;
	}

	/**
	 * @return the outDegreeDistribution
	 */
	public Distribution getOutDegreeDistribution() {
		return this.outDegreeDistribution;
	}

	/**
	 * @return the nodes
	 */
	public int getNodes() {
		return this.nodes;
	}

	/**
	 * @return the edges
	 */
	public int getEdges() {
		return this.edges;
	}

	/**
	 * @return the timer
	 */
	public Timer getRuntime() {
		return this.runtime;
	}
}

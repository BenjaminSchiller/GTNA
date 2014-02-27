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
 * 2014-02-03 : readData (Tim Grube)
 */
package gtna.metrics.basic;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;

import java.util.Arrays;
import java.util.HashMap;

public class DegreeDistribution extends Metric {
	private Distribution degreeDistribution;

	private Distribution inDegreeDistribution;

	private Distribution outDegreeDistribution;

	private int nodes;

	private int edges;

	public DegreeDistribution() {
		super("DEGREE_DISTRIBUTION");
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	public void computeData(Graph graph, Network nw, HashMap<String, Metric> m) {
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

		this.degreeDistribution = new Distribution("DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION", dd);
		this.inDegreeDistribution = new Distribution("DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION", ddi);
		this.outDegreeDistribution = new Distribution("DEGREE_DISTRIBUTION_OUT_DEGREE_DISTRIBUTION", ddo);

		this.nodes = graph.getNodes().length;
		this.edges = graph.generateEdges().length;
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

	public Single[] getSingles() {
		Single nodes = new Single("DEGREE_DISTRIBUTION_NODES", this.nodes);
		Single edges = new Single("DEGREE_DISTRIBUTION_EDGES", this.edges);

		Single degreeMin = new Single("DEGREE_DISTRIBUTION_DEGREE_MIN",
				this.degreeDistribution.getMin());
		Single degreeMed = new Single("DEGREE_DISTRIBUTION_DEGREE_MED",
				this.degreeDistribution.getMedian());
		Single degreeAvg = new Single("DEGREE_DISTRIBUTION_DEGREE_AVG",
				this.degreeDistribution.getAverage());
		Single degreeMax = new Single("DEGREE_DISTRIBUTION_DEGREE_MAX",
				this.degreeDistribution.getMax());

		Single inDegreeMin = new Single("DEGREE_DISTRIBUTION_IN_DEGREE_MIN",
				this.inDegreeDistribution.getMin());
		Single inDegreeMed = new Single("DEGREE_DISTRIBUTION_IN_DEGREE_MED",
				this.inDegreeDistribution.getMedian());
		Single inDegreeAvg = new Single("DEGREE_DISTRIBUTION_IN_DEGREE_AVG",
				this.inDegreeDistribution.getAverage());
		Single inDegreeMax = new Single("DEGREE_DISTRIBUTION_IN_DEGREE_MAX",
				this.inDegreeDistribution.getMax());

		Single outDegreeMin = new Single("DEGREE_DISTRIBUTION_OUT_DEGREE_MIN",
				this.outDegreeDistribution.getMin());
		Single outDegreeMed = new Single("DEGREE_DISTRIBUTION_OUT_DEGREE_MED",
				this.outDegreeDistribution.getMedian());
		Single outDegreeAvg = new Single("DEGREE_DISTRIBUTION_OUT_DEGREE_AVG",
				this.outDegreeDistribution.getAverage());
		Single outDegreeMax = new Single("DEGREE_DISTRIBUTION_OUT_DEGREE_MAX",
				this.outDegreeDistribution.getMax());

		return new Single[] { nodes, edges, degreeMin, degreeMed, degreeAvg,
				degreeMax, inDegreeMin, inDegreeMed, inDegreeAvg, inDegreeMax,
				outDegreeMin, outDegreeMed, outDegreeAvg, outDegreeMax };
	}
	
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] {degreeDistribution, inDegreeDistribution, outDegreeDistribution};
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[0];
	}

	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(
				this.degreeDistribution.getDistribution(),
				"DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(this.degreeDistribution.getCdf(),
				"DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithoutIndex(
				this.inDegreeDistribution.getDistribution(),
				"DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(
				this.inDegreeDistribution.getCdf(),
				"DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithoutIndex(
				this.outDegreeDistribution.getDistribution(),
				"DEGREE_DISTRIBUTION_OUT_DEGREE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(
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
	
	
	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#readData(java.lang.String)
	 */
	@Override
	public boolean readData(String folder) {

		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
				if("DEGREE_DISTRIBUTION_NODES".equals(single[0])){
					this.nodes = (int) Math.round(Double.valueOf(single[1]));
				} else if("DEGREE_DISTRIBUTION_EDGES".equals(single[0])){
					this.edges = (int) Math.round(Double.valueOf(single[1]));
				} 
			}
		}
		
		
		/* DISTRIBUTIONS */
		
		degreeDistribution = new Distribution("DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION", readDistribution(folder, "DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION"));
		inDegreeDistribution = new Distribution("DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION", readDistribution(folder, "DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION"));
		outDegreeDistribution = new Distribution("DEGREE_DISTRIBUTION_OUT_DEGREE_DISTRIBUTION", readDistribution(folder, "DEGREE_DISTRIBUTION_OUT_DEGREE_DISTRIBUTION"));
		
		
		
		
		return true;
	}


}

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
 * SamplingModularity.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-04: readData, getDistributions, getNodeValueLists (Tim Grube)
 */
package gtna.metrics.sampling;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.sampling.Sample;
import gtna.util.Distribution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class SamplingModularity extends Metric {

	private int sampleIndex;
	private double sampleModularity;
	private double sampleEdges;
	private int edges;
	private int sampleNodes;
	private int nodes;
	private NodeValueList samplemodularity;
	private double smMax;
	private double smMin;
	private double smMed;
	private double smAvg;

	/**
	 * @param key
	 */
	public SamplingModularity() {
		super("SAMPLING_MODULARITY");
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Sample[] samples = this.getSampleProperties(g);

		double[] sm = new double[samples.length];

		for (int i = 0; i < samples.length; i++) {
			Sample s = samples[i];
			double sumDegrees = 0;
			double withinEdgeSum = 0;

			Set<Integer> sample = s.getSampledIds();
			for (Integer nodeId : sample) {
				Node node = g.getNode(nodeId);

				sumDegrees += node.getOutDegree();
				// count incoming edges? (incoming into the sample-community)
				// TODO

				// count edges within the sample-community
				for (Integer outEdge : node.getOutgoingEdges()) {
					if (sample.contains(outEdge)) {
						withinEdgeSum++;
					}
				}
			}

			sampleModularity = withinEdgeSum / sumDegrees;
			sampleNodes = sample.size();

			sm[i] = sampleModularity;

		}

		this.samplemodularity = new NodeValueList("SAMPLING_MODULARITY_DISTRIBUTION", sm);
		this.smMax = getMax(sm);
		this.smMin = getMin(sm);
		this.smMed = getMed(sm);
		this.smAvg = getAvg(sm);
	}

	private double getMax(double[] dis) {
		double max = 0;

		for (double d : dis) {
			max = Math.max(max, d);
		}

		return max;
	}

	private double getMin(double[] dis) {
		double min = Double.MAX_VALUE;

		for (double d : dis) {
			min = Math.min(min, d);
		}

		return min;
	}

	private double getAvg(double[] dis) {
		double sum = 0;

		for (double d : dis) {
			sum += d;
		}

		return sum / dis.length;
	}

	private double getMed(double[] dis) {
		double[] s = dis;
		double median;
		Arrays.sort(s);

		if (s.length % 2 != 0) {
			// odd number of entries
			median = dis[(int) Math.floor(dis.length / 2)];
		} else {
			// even number of entries
			double umed = dis[(int) dis.length / 2 - 1];
			double omed = dis[(int) dis.length / 2];

			median = umed + (omed - umed) / 2;
		}

		return median;
	}

	/**
	 * @param g
	 * @return
	 */
	private Sample[] getSampleProperties(Graph g) {
		GraphProperty[] p = g.getProperties("SAMPLE");
		Sample[] s = new Sample[p.length];

		for (int i = 0; i < s.length; i++) {
			s[i] = (Sample) p[i];
		}

		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.samplemodularity.getValues(),
				"SAMPLING_MODULARITY_DISTRIBUTION", folder);
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single smMax = new Single("SAMPLING_MODULARITY_MAX",
				this.smMax);
		Single smMin = new Single("SAMPLING_MODULARITY_MIN",
				this.smMin);
		Single smAvg = new Single("SAMPLING_MODULARITY_AVG",
				this.smAvg);
		Single smMed = new Single("SAMPLING_MODULARITY_MED",
				this.smMed);

		return new Single[] { smMax, smMin, smAvg, smMed };
	}
	
	@Override
	public boolean readData(String folder){
		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
					// derived from NV-List biasd, delete if recovered differently
				if("SAMPLING_MODULARITY_MAX".equals(single[0])){
					this.smMax = Double.valueOf(single[1]);
				}  else if("SAMPLING_MODULARITY_MIN".equals(single[0])){
					this.smMin = Double.valueOf(single[1]);
				} else if("SAMPLING_MODULARITY_AVG".equals(single[0])){
					this.smAvg = Double.valueOf(single[1]);
				} else if("SAMPLING_MODULARITY_MED".equals(single[0])){
					this.smMed = Double.valueOf(single[1]);
				}
			}
		}
		
		
		/* NODE VALUE LIST */
		
		samplemodularity = new NodeValueList("SAMPLING_MODULARITY_DISTRIBUTION", readDistribution(folder, "SAMPLING_MODULARITY_DISTRIBUTION"));
		
		
		
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		if (g.getProperties("SAMPLE").length > 0) {
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getDistributions()
	 */
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[]{};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getNodeValueLists()
	 */
	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[]{samplemodularity};
	}

}

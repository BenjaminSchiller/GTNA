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
 * SamplingBias.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-05: readData, getDistributions, getNodeValueList (Tim Grube)
 */
package gtna.metrics.sampling;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
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
public class SamplingBias extends Metric {

	private Integer edges;
	private Integer nodes;
	private NodeValueList biasd;
	private double sbAvg;
	private double sbMed;
	private double sbMin;
	private double sbMax;

	/**
	 * @param key
	 */
	public SamplingBias() {
		super("SAMPLING_BIAS");
		// TODO Auto-generated constructor stub
	}


	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		
		double[] nodesampling = new double[g.getNodeCount()];
		Arrays.fill(nodesampling, 0.0);
		GraphProperty[] samples = g.getProperties("SAMPLE");
		
		for(GraphProperty s : samples){
			Sample sa = (Sample)s;
			
			Set<Integer> sampledIds = sa.getSampledIds();
			for(Integer i : sampledIds){
				nodesampling[i]++;
			}
		}
		
		double[] sb = new double[samples.length+1];
		for(double d : nodesampling){
			sb[(int)d]++;
		}
		
		
		Arrays.sort(nodesampling);
		biasd = new NodeValueList("SAMPLING_BIAS_DISTRIBUTION", nodesampling);
		nodes = g.getNodeCount();
		edges = g.getEdges().size();
		
		this.sbMax = getMax(sb);
		this.sbMin = getMin(sb);
		this.sbMed = getMed(sb);
		this.sbAvg = getAvg(sb);
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		
		success &= DataWriter.writeWithIndex(
				this.biasd.getValues(), 
				"SAMPLING_BIAS_DISTRIBUTION", 
				folder);

		
		return success;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single nodes = new Single("SAMPLING_BIAS_NODES", this.nodes);
		Single edges = new Single("SAMPLING_BIAS_EDGES", this.edges);
		
		Single sbMin = new Single("SAMPLING_BIAS_MIN", this.sbMin);
		Single sbMed = new Single("SAMPLING_BIAS_MED", this.sbMed);
		Single sbAvg = new Single("SAMPLING_BIAS_AVG", this.sbAvg);
		Single sbMax = new Single("SAMPLING_BIAS_MAX", this.sbMax);
		
		return new Single[] { nodes, edges, sbMin, sbMed, sbAvg, sbMax };
	}
	
	@Override
	public boolean readData(String folder){
		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
				if("SAMPLING_BIAS_NODES".equals(single[0])){
					this.nodes = (int) Math.round(Double.valueOf(single[1]));
				} else if("SAMPLING_BIAS_EDGES".equals(single[0])){
					this.edges = (int) Math.round(Double.valueOf(single[1]));
					
					// derived from NV-List biasd, delete if recovered differently
				} else if("SAMPLING_BIAS_MIN".equals(single[0])){
					this.sbMin = Double.valueOf(single[1]);
				}  else if("SAMPLING_BIAS_MED".equals(single[0])){
					this.sbMed = Double.valueOf(single[1]);
				} else if("SAMPLING_BIAS_AVG".equals(single[0])){
					this.sbAvg = Double.valueOf(single[1]);
				} else if("SAMPLING_BIAS_MAX".equals(single[0])){
					this.sbMax = Double.valueOf(single[1]);
				}
			}
		}
		
		
		/* NODE VALUE LIST */
		
		biasd = new NodeValueList("SAMPLING_BIAS_DISTRIBUTION", readDistribution(folder, "SAMPLING_BIAS_DISTRIBUTION"));
		
		
		
		
		return true;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		// at least 1 sample necessary
		if(g.getProperties("SAMPLE").length > 0) {
			return true;
		} else {
			return false;
		}
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
		return new NodeValueList[] {biasd};
	}

}

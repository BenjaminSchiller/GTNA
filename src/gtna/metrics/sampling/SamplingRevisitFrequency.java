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
 * SamplingRevisitFrequency.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *	2014-02-05: readData, getDistributions, getNodeValueLists (Tim Grube)
 */
package gtna.metrics.sampling;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.sampling.Sample;
import gtna.util.Distribution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tim
 *
 */
public class SamplingRevisitFrequency extends Metric {

	private int samplingIndex = 0;
	private NodeValueList revisitFrequency;
	private int numberOfRounds;
	private int edges;
	private int nodes;
	private NodeValueList samplingEfficiency; 
	private double rfMax;
	private double rfMin;
	private double rfAvg;
	private double rfMed;

	/**
	 * @param key
	 */
	public SamplingRevisitFrequency() {
		super("SAMPLING_REVISIT_FREQUENCY");
		this.samplingIndex = 0;
	}

	/**
	 * @param key
	 * @param parameters
	 */
	public SamplingRevisitFrequency(int i) {
		this();
		this.samplingIndex = i;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Sample s = (Sample)g.getProperty("SAMPLE_" + samplingIndex);
		
		double[] rounds = new double[s.getSample().getNumberOfRounds()];
		
		Map<Integer, List<Integer>> rf = s.getSample().getRevisitFrequency();
		
		for(int nindex : s.getSampledIds()){
			List<Integer> nrounds = rf.get(nindex);
			for(int r : nrounds){
				if(r < rounds.length)
					rounds[r] = nindex;
			}
		}
		
		
		Set<Double> seenIndices = new HashSet<Double>();
		double[] efficiency = new double[rounds.length];
		double ci;
		double currentSampleSize = 0;
		for(int i = 0; i < rounds.length; i++) {
		    ci = rounds[i];
		    if(!seenIndices.contains(ci)) {
			seenIndices.add(ci);
			currentSampleSize++;
			efficiency[i] = currentSampleSize;
		    }else {
			efficiency[i] = currentSampleSize;
		    }
		    
		}
		
		
		
		samplingEfficiency = new NodeValueList("SAMPLING_REVISIT_FREQUENCY_EFFICIENCY", efficiency);
		revisitFrequency = new NodeValueList("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION", rounds);
		nodes = g.getNodes().length;
		edges = g.getEdges().size();
		numberOfRounds = s.getSample().getNumberOfRounds();
		
		this.rfMax = getMax(rounds);
		this.rfMin = getMin(rounds);
		this.rfAvg = getAvg(rounds);
		this.rfMed = getMed(rounds);

	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.revisitFrequency.getValues(),
				"SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.samplingEfficiency.getValues(), 
			"SAMPLING_REVISIT_FREQUENCY_EFFICIENCY", folder);
		
		return success;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single nodes = new Single("SAMPLING_REVISIT_FREQUENCY_NODES", this.nodes);
		Single edges = new Single("SAMPLING_REVISIT_FREQUENCY_EDGES", this.edges);
		Single rounds = new Single("SAMPLING_REVISIT_FREQUENCY_ROUNDS", this.numberOfRounds);
		
		Single rfMin = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MIN", this.rfMin);
		Single rfMed = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MED", this.rfMed);
		Single rfAvg = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_AVG", this.rfAvg);
		Single rfMax = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MAX", this.rfMax);
		
		return new Single[] { 
				nodes, edges, rounds, 
				rfMin, rfMed, rfAvg, rfMax };
	}
	
	@Override
	public Distribution[] getDistributions(){
		return new Distribution[]{};
	}
	
	@Override
	public NodeValueList[] getNodeValueLists(){
		return new NodeValueList[]{revisitFrequency, samplingEfficiency};
	}
	
	@Override
	public boolean readData(String folder){
		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
				if("SAMPLING_REVISIT_FREQUENCY_NODES".equals(single[0])){
					this.nodes = (int) Math.round(Double.valueOf(single[1]));
				} else if("SAMPLING_REVISIT_FREQUENCY_EDGES".equals(single[0])){
					this.edges = (int) Math.round(Double.valueOf(single[1]));
				}else if("SAMPLING_REVISIT_FREQUENCY_ROUNDS".equals(single[0])){
						this.numberOfRounds = (int) Math.round(Double.valueOf(single[1]));
					
					// derived from NV-List biasd, delete if recovered differently
				} else if("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MIN".equals(single[0])){
					this.rfMin = Double.valueOf(single[1]);
				}  else if("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MED".equals(single[0])){
					this.rfMed = Double.valueOf(single[1]);
				} else if("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_AVG".equals(single[0])){
					this.rfAvg = Double.valueOf(single[1]);
				} else if("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MAX".equals(single[0])){
					this.rfMax = Double.valueOf(single[1]);
				}
			}
		}
		
		
		/* NODE VALUE LIST */
		
		revisitFrequency = new NodeValueList("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION", readDistribution(folder, "SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION"));
		samplingEfficiency = new NodeValueList("SAMPLING_REVISIT_FREQUENCY_EFFICIENCY", readDistribution(folder, "SAMPLING_REVISIT_FREQUENCY_EFFICIENCY"));	
		
		
		return true;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		if(g.hasProperty("SAMPLE_" + samplingIndex)){
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

}

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
 *
 */
package gtna.metrics.sampling;

import gtna.data.Single;
import gtna.graph.Graph;
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.io.DataWriter;
=======
>>>>>>> computation of the basic revisit frequency
=======
import gtna.io.DataWriter;
>>>>>>> SRF properties
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.sampling.Sample;
import gtna.util.Distribution;

<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
=======
=======
import java.util.Arrays;
>>>>>>> min/max/avg/med calculation
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
<<<<<<< HEAD
>>>>>>> computation of the basic revisit frequency
=======
import java.util.Set;
>>>>>>> Added distribution for showing the efficiency

/**
 * @author Tim
 *
 */
public class SamplingRevisitFrequency extends Metric {

	private int samplingIndex = 0;
	private Distribution revisitFrequency;
<<<<<<< HEAD
<<<<<<< HEAD
	private int numberOfRounds;
	private int edges;
	private int nodes;
	private Distribution samplingEfficiency;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> min/max/avg/med calculation
	private double rfMax;
	private double rfMin;
	private double rfAvg;
	private double rfMed;
<<<<<<< HEAD
=======
>>>>>>> computation of the basic revisit frequency
=======
	private int numberOfRounds;
	private int edges;
	private int nodes;
>>>>>>> SRF properties
=======
>>>>>>> Added distribution for showing the efficiency
=======
>>>>>>> min/max/avg/med calculation

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
		
		
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Added distribution for showing the efficiency
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
		
		
		
		samplingEfficiency = new Distribution(efficiency);
		revisitFrequency = new Distribution(rounds);
		nodes = g.getNodes().length;
		edges = g.getEdges().size();
		numberOfRounds = s.getSample().getNumberOfRounds();
		
		this.rfMax = getMax(rounds);
		this.rfMin = getMin(rounds);
		this.rfAvg = getAvg(rounds);
		this.rfMed = getMed(rounds);
<<<<<<< HEAD
=======
		
		
		revisitFrequency = new Distribution(rounds);
<<<<<<< HEAD
>>>>>>> computation of the basic revisit frequency
=======
		nodes = g.getNodes().length;
		edges = g.getEdges().size();
		numberOfRounds = s.getSample().getNumberOfRounds();
>>>>>>> SRF properties
=======
>>>>>>> min/max/avg/med calculation

	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
<<<<<<< HEAD
<<<<<<< HEAD
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.revisitFrequency.getDistribution(),
				"SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.samplingEfficiency.getDistribution(), 
			"SAMPLING_REVISIT_FREQUENCY_EFFICIENCY", folder);
		
		return success;
=======
		// TODO Auto-generated method stub
		return false;
>>>>>>> computation of the basic revisit frequency
=======
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.revisitFrequency.getDistribution(),
				"SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.samplingEfficiency.getDistribution(), 
			"SAMPLING_REVISIT_FREQUENCY_EFFICIENCY", folder);
		
		return success;
>>>>>>> SRF properties
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> SRF properties
		Single nodes = new Single("SAMPLING_REVISIT_FREQUENCY_NODES", this.nodes);
		Single edges = new Single("SAMPLING_REVISIT_FREQUENCY_EDGES", this.edges);
		Single rounds = new Single("SAMPLING_REVISIT_FREQUENCY_ROUNDS", this.numberOfRounds);
		
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> min/max/avg/med calculation
		Single rfMin = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MIN", this.rfMin);
		Single rfMed = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MED", this.rfMed);
		Single rfAvg = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_AVG", this.rfAvg);
		Single rfMax = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MAX", this.rfMax);
<<<<<<< HEAD
=======
		Single rfMin = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MIN", this.revisitFrequency.getMin());
		Single rfMed = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MED", this.revisitFrequency.getMedian());
		Single rfAvg = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_AVG", this.revisitFrequency.getAverage());
		Single rfMax = new Single("SAMPLING_REVISIT_FREQUENCY_DISTRIBUTION_MAX", this.revisitFrequency.getMax());
>>>>>>> SRF properties
=======
>>>>>>> min/max/avg/med calculation
		
		return new Single[] { 
				nodes, edges, rounds, 
				rfMin, rfMed, rfAvg, rfMax };
<<<<<<< HEAD
=======
		// TODO Auto-generated method stub
		return null;
>>>>>>> computation of the basic revisit frequency
=======
>>>>>>> SRF properties
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
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> min/max/avg/med calculation
	
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
<<<<<<< HEAD
=======
>>>>>>> computation of the basic revisit frequency
=======
>>>>>>> min/max/avg/med calculation

}

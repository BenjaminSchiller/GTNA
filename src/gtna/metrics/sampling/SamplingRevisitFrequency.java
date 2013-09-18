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
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.sampling.Sample;
import gtna.util.Distribution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tim
 *
 */
public class SamplingRevisitFrequency extends Metric {

	private int samplingIndex = 0;
	private Distribution revisitFrequency;

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
		
		
		
		
		revisitFrequency = new Distribution(rounds);

	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		// TODO Auto-generated method stub
		return null;
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

}

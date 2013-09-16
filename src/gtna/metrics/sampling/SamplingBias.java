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
 *
 */
package gtna.metrics.sampling;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
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
	private Distribution biasd;

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
		
		biasd = new Distribution(nodesampling);
		nodes = g.getNodeCount();
		edges = g.getEdges().size();
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
		// TODO Auto-generated method stub
		return false;
	}

}

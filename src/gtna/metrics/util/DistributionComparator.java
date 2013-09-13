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
 * DistributionComparator.java
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
package gtna.metrics.util;

import java.util.HashMap;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.metrics.Metric;
import gtna.networks.Network;

/**
 * @author Tim
 *
 */
public abstract class DistributionComparator extends Metric {
	
	/**
	 * @param key
	 */
	public DistributionComparator(String key, Metric refMetric) {
		super(key);
		

		referenceMetric = refMetric;
	}

<<<<<<< HEAD
	protected Metric referenceMetric = null;
	protected Metric comparedMetric = null;
=======
	private Metric referenceMetric = null;
	private Metric comparedMetric = null;
>>>>>>> Abstract DistributionComparator
	
	
	
	

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if(!applicable(g, n, m)){
			throw new IllegalArgumentException("No computation possible!");
		}
		
		
		comparedMetric = m.get(referenceMetric.getKey());
		computeRelative();
		computeAbsolute();
		
	}

	/**
	 * 
	 */
	public abstract void computeAbsolute();

	/**
	 * 
	 */
	public abstract void computeRelative();


	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		// true iff referenceMetric is set and the Map contains a metric with the same key
		if(referenceMetric != null && m.containsKey(referenceMetric.getKey()))
			return true;
		else
			return false;
	}


}

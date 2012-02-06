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
 * ScaleFreeRandomGraph.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.randomGraphs;

import gtna.graph.Graph;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

/**
 * @author stef
 *
 */
public class ScaleFreeRandomGraph extends NetworkImpl {
	double alpha;
	int min,max;

	/**
	 * 
	 * @param nodes
	 * @param alpha: exponent of degree
	 * @param minDegree: minimal degree
	 * @param maxDegree: maximal degree
	 * @param ra
	 * @param t
	 */
	public ScaleFreeRandomGraph(int nodes, double alpha, int minDegree, int maxDegree , RoutingAlgorithm ra, Transformation[] t) {
		super("SCALE_FREE_RANDOM", nodes, new String[]{"ALPHA", "MINDEG", "MAXDEG"}, new String[]{""+alpha,""+ minDegree, ""+maxDegree}, ra, t);
        this.alpha = alpha;
        this.max = maxDegree;
        this.min = minDegree;
	}

	/* (non-Javadoc)
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		double[] probs = new double[this.max+1];
		double norm = 0;
		for (int i = this.min; i <= this.max; i++){
			norm = norm + Math.pow(i, -alpha);
			probs[i] = norm;
		}
		for (int i = this.min; i < probs.length; i++){
			probs[i] = probs[i]/norm;
		}
		return (new ArbitraryDegreeDistribution(this.nodes(), this.name(), probs,null,null)).generate();
	}

}

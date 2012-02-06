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
 * AribitraryDegreeDistribution.java
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
import gtna.io.DataReader;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

import java.util.Random;

/**
 * @author stef
 * create an undirected graph with a arbitrary degree sequence
 */
public class ArbitraryDegreeDistribution extends NetworkImpl {
	double[] cdf;

	/**
	 * 
	 * @param nodes
	 * @param name
	 * @param distribution: P(degree=i)
	 * @param ra
	 * @param t
	 */
	public ArbitraryDegreeDistribution(int nodes, String name, double[] distribution, RoutingAlgorithm ra, Transformation[] t) {
		super("ARBITRARY_DEGREE_DISTRIBUTION", nodes, new String[]{"NAME"}, new String[] {"" + name}, ra, t);
		this.cdf = distribution;
		
	}
	
	public ArbitraryDegreeDistribution(int nodes, String name, String file, RoutingAlgorithm ra, Transformation[] t) {
		super("ARBITRARY_DEGREE_DISTRIBUTION", nodes, new String[]{"NAME"}, new String[] {"" + name}, ra, t);
		this.cdf = DataReader.readDouble(file);
	}
	
	

	/* (non-Javadoc)
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		int[] sequence = new int[this.nodes()];
		Random rand = new Random(System.currentTimeMillis());
		int sum = 0;
		int k = 0;
		for (int i = 0; i < sequence.length; i++){
			double bound = rand.nextDouble();
			k=0;
			while (this.cdf[k] < bound){
				k++;
			}
			sequence[i] = k;
			sum = sum + k;
		}
		while (sum % 2 == 1){
			sum = sum - k;
			double bound = rand.nextDouble();
			k=0;
			while (this.cdf[k] < bound){
				k++;
			}
			sequence[sequence.length-1] = k;
			sum = sum + k;
		}
		
		return (new ArbitraryDegreeSequence(this.nodes(), this.name(), sequence,null,null)).generate();
		
	}

}

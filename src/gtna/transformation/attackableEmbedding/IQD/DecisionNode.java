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
 * DecisionNode.java
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
package gtna.transformation.attackableEmbedding.IQD;

import gtna.graph.Graph;

import java.util.Random;

/**
 * @author stef various ways to select one ID based on its quality
 */
public abstract class DecisionNode extends IdentifierNode {
	public static double T = 1;
	public static double alpha = 0.99;
	private double t;

	/**
	 * @param index
	 * @param g
	 * @param id
	 * @param embedding
	 */
	public DecisionNode(int index, Graph g, IQDEmbedding embedding) {
		super(index, g, embedding);
		t = 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.attackableEmbedding.IQD.IQDNode#getDecision(java.
	 * util.Random, double[])
	 */
	@Override
	public int getDecision(Random rand, double[] metrics) {
		if (metrics.length == 1) {
			return 0;
		}
		// deal with max values
		for (int j = 0; j < metrics.length; j++) {
			if (metrics[j] == Double.MAX_VALUE) {
				return j;
			}
		}
		// take the best ID, preferring the new one
		if (this.embedding.deMethod == IQDEmbedding.DecisionMethod.BESTPREFERNEW) {
			int bestIndex = 0;
			double max = metrics[0];
			for (int i = 1; i < metrics.length; i++) {
				if (metrics[i] > max) {
					max = metrics[i];
					bestIndex = i;
				}
			}
			return bestIndex;
		}
		// take best ID, prefer old one
		if (this.embedding.deMethod == IQDEmbedding.DecisionMethod.BESTPREFEROLD) {
			int bestIndex = 0;
			double max = metrics[0];
			for (int i = 1; i < metrics.length; i++) {
				if (metrics[i] >= max) {
					max = metrics[i];
					bestIndex = i;
				}
			}
			return bestIndex;
		}
		// METROPOLIS-HASTINGS algorithm
		if (this.embedding.deMethod == IQDEmbedding.DecisionMethod.METROPOLIS) {
			if (metrics.length != 2) {
				throw new IllegalArgumentException(
						"METROPLOIS decision not possible with more than two options");
			}
			if (rand.nextDouble() < metrics[0] / metrics[1]) {
				return 0;
			} else {
				return 1;
			}
		}
		// a Simulated annealing method accepting with probability depending on
		// exponential of ratio
		if (this.embedding.deMethod == IQDEmbedding.DecisionMethod.SA) {
			if (metrics.length != 2) {
				throw new IllegalArgumentException(
						"SA decision not possible with more than two options");
			}
			if (metrics[0] > metrics[1]) {
				return 0;
			}
			if (rand.nextDouble() < Math.exp((metrics[0] - metrics[1]) / T)) {
				return 0;
			} else {
				return 1;
			}
		}
		// simulated annealing with temperature
		if (this.embedding.deMethod == IQDEmbedding.DecisionMethod.SATIMEDEPENDENT) {
			if (metrics.length != 2) {
				throw new IllegalArgumentException(
						"SA decision not possible with more than two options");
			}
			t = alpha * t;
			if (metrics[0] > metrics[1]) {
				return 0;
			}
			if (rand.nextDouble() < Math.exp((metrics[0] - metrics[1]) / t)) {
				return 0;
			} else {
				return 1;
			}
		}
		// choosing Ids proportional to their portion of sum of qualities
		if (this.embedding.deMethod == IQDEmbedding.DecisionMethod.PROPORTIONAL) {
			double sum = 0;
			for (int i = 0; i < metrics.length; i++) {
				sum = sum + metrics[i];
			}
			double p = rand.nextDouble() * sum;
			double s = 0;
			for (int j = 0; j < metrics.length; j++) {
				s = s + metrics[j];
				if (s > p) {
					return j;
				}
			}
		}

		return 0;
	}

}

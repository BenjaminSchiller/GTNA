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
 * EffectiveDiameter.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: truong;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics;

import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.HashMap;
import java.util.Random;

/**
 * Implement the Effective Diameter described by Christopher R. Palmer and
 * Phillip B. Gibbons in "Fast Approximation of the "Neighbourhood" Function for
 * Massive Graphs" (2001)
 * 
 * @author truong
 * 
 */
public class EffectiveDiameter extends Metric {

	private int effectiveDiameter;
	private int k;
	private int r;

	/**
	 * @param key
	 * @param parameters
	 */
	public EffectiveDiameter(int k, int r) {
		super("EFFECTIVE_DIAMETER", new Parameter[] { new IntParameter("K", k),
				new IntParameter("R", r) });
		this.k = k;
		this.r = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		// TODO: if graph is disconnected?
		int totalNumberOfPairs = g.getNodes().length
				* (g.getNodes().length - 1) / 2;
		double threshold = 0.9 * totalNumberOfPairs;
		int bitmaskLength = (int) (Math.log(g.getNodes().length) / Math.log(2) + r);
		// TODO: using diameter
		boolean[][][] M = new boolean[k * bitmaskLength][g.getNodes().length][g
				.getNodes().length];

		// FOR each node u DO
		// M(u,0) = concatenation of k bitmasks, each with 1 bit set (according
		// to an exponential distribution)
		for (Node u : g.getNodes()) {
			boolean[][] temp = new boolean[k][bitmaskLength];
			for (int i = 0; i < k; i++) {
				temp[i] = this.generateBitmask(bitmaskLength);
			}
			M[u.getIndex()][0] = this.concat(temp);
		}

		// FOR each distance it starting with 1 DO
		for (int it = 1; it < g.getNodes().length; it++) {
			// FOR each node u DO
			for (Node u : g.getNodes()) {
				M[u.getInDegree()][it] = M[u.getInDegree()][it - 1];
			}
			// FOR each edge (u,v) DO
			for (Edge e : g.getEdges().getEdges()) {
				int uIndex = e.getDst();
				int vIndex = e.getDst();
				if (uIndex > vIndex) {
					continue;
				}
				M[uIndex][it] = bitwiseOR(M[uIndex][it], M[vIndex][it - 1]);
			}

			// The estimate is: SUM(all u) (2^b)/(.7731*bias)
			int estimate = 0;
			for (Node u : g.getNodes()) {
				estimate += Math.pow(2,
						this.lowestOrderZeroBit(M[u.getIndex()]))
						/ (0.7731 * (1 + 0.31 / this.k));
			}
			if (estimate >= threshold) {
				this.effectiveDiameter = it;
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	private boolean[] generateBitmask(int length) {
		boolean[] result = new boolean[length];
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			if (rand.nextInt(2) == 0) {
				result[i] = true;
			} else {
				result[i] = false;
			}
		}
		return result;
	}

	private boolean[] concat(boolean[][] masks) {
		int totalLength = 0;
		for (boolean[] mask : masks) {
			totalLength += mask.length;
		}
		boolean[] result = new boolean[totalLength];
		int currentIndex = 0;
		for (boolean[] mask : masks) {
			for (boolean bool : mask) {
				result[currentIndex] = bool;
				currentIndex++;
			}
		}
		return result;
	}

	/**
	 * @param bs
	 * @param bs2
	 * @return
	 */
	private boolean[] bitwiseOR(boolean[] x, boolean[] y) {
		boolean[] result = new boolean[x.length];
		for (int i = 0; i < x.length; i++) {
			result[i] = x[i] || y[i];
		}
		return result;
	}

	private int lowestOrderZeroBit(boolean[][] masks) {
		// we calculate the sum of b1, b2,..., bk
		int sum = 0;
		for (int i = 0; i < masks.length; i++) {
			for (int j = masks[i].length - 1; j >= 0; j--) {
				if (!masks[i][j]) {
					sum += (masks[i].length - 1) - j;
				}
			}
		}
		return sum / masks.length;
	}
}

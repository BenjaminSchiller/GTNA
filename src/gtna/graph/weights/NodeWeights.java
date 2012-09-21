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
 * NodeWeights.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.graph.weights;

import gtna.graph.GraphProperty;
import gtna.util.ArrayUtils;

/**
 * Implements a graph property to hold node weights, i.e., a double value for
 * each node in a network.
 * 
 * @author benni
 * 
 */
public class NodeWeights extends GraphProperty {

	private double[] weights;

	public NodeWeights(int nodes) {
		this.weights = new double[nodes];
	}

	public NodeWeights(double[] weights) {
		this.weights = weights;
	}

	public NodeWeights(int nodes, double weight) {
		this.weights = ArrayUtils.initDoubleArray(nodes, weight);
	}

	public double[] getWeights() {
		return this.weights;
	}

	public double getWeight(int index) {
		return this.weights[index];
	}

	public void setWeight(int index, double weight) {
		this.weights[index] = weight;
	}

	@Override
	public boolean write(String filename, String key) {
		// TODO implement GraphProperty.write(...)
		return false;
	}

	@Override
	public String read(String filename) {
		// TODO implement GraphProperty.read(...)
		return null;
	}

}

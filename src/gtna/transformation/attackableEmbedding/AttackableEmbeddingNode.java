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
 * SortingNodeImpl.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    "Stefanie Roos";
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-14 : v1 (BS)
 *
 */
package gtna.transformation.attackableEmbedding;

import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * abstract Node type for round-based embeddings
 * 
 * @author stefanieroos
 * 
 */

public abstract class AttackableEmbeddingNode extends Node {

	//protected HashMap<Integer, Integer> position;

	public double[] knownIDs;

	public AttackableEmbeddingNode(int index, Graph g) {
		super(index, g);
	}

	/**
	 * must be called after creating the outgoing edges of this node
	 */
	public void initKnownIDs() {
		//this.position = new HashMap<Integer, Integer>(this.getOutDegree());
		this.knownIDs = new double[this.getOutDegree()];
//		for (int i = 0; i < this.getOutDegree(); i++) {
//			this.position.put(this.getOutgoingEdges()[i], i);
//		}
	}

	public abstract void updateNeighbors(Random rand);

	public abstract void turn(Random rand);

	/**
	 * return value with biggest distance to values
	 * 
	 * @param values
	 * @return
	 */
	public static double maxMiddle(double[] values) {
		Arrays.sort(values);
		double max = 1 - values[values.length - 1] + values[0];
		int index = values.length - 1;
		for (int i = 0; i < values.length - 1; i++) {
			double a = values[i];
			double b = values[i + 1];
			double dist = b - a;
			if (dist > max) {
				max = dist;
				index = i;
			}
		}
		double middle = (values[index] + max / 2) % 1.0;
		return middle;
	}

}

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
 * RandomRingID.java
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
package gtna.transformation.id;

import gtna.graph.Graph;
import gtna.id.RingID;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.Random;

/**
 * Assigns a randomly selected RingID to every node and stores it as a property
 * with key "ID" in the given graph.
 * 
 * @author benni
 * 
 */
public class RandomRingID extends TransformationImpl implements Transformation {
	public RandomRingID() {
		super("RANDOM_RING_ID", new String[] {}, new String[] {});
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		RingID[] ids = new RingID[graph.getNodes().length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = RingID.rand(rand);
		}
		graph.addNodeProperties("ID", ids);
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

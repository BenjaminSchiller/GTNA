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
 * RandomPlaneIDSpaceSimple.java
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
import gtna.id.plane.PlaneID;
import gtna.id.plane.PlaneIDSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RandomPlaneIDSpaceSimple extends TransformationImpl implements
		Transformation {
	private int realities;

	public RandomPlaneIDSpaceSimple() {
		super("RANDOM_PLANE_ID_SPACE_SIMPLE", new String[] {}, new String[] {});
		this.realities = 1;
	}

	public RandomPlaneIDSpaceSimple(int realities) {
		super("RANDOM_PLANE_ID_SPACE_SIMPLE", new String[] { "REALITIES" },
				new String[] { "" + realities });
		this.realities = realities;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			PlanePartitionSimple[] partitions = new PlanePartitionSimple[graph
					.getNodes().length];
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new PlanePartitionSimple(PlaneID.rand(rand));
			}
			PlaneIDSpaceSimple idSpace = new PlaneIDSpaceSimple(partitions);
			graph.addProperty("ID_SPACE_" + r, idSpace);
			if (r == 0) {
				graph.addProperty("ID_SPACE", idSpace);
			}
		}
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

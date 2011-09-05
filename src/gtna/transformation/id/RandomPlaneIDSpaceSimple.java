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

	private double modulusX;

	private double modulusY;

	private boolean wrapAround;

	public RandomPlaneIDSpaceSimple() {
		super("RANDOM_PLANE_ID_SPACE_SIMPLE", new String[] {}, new String[] {});
		this.realities = 1;
	}

	public RandomPlaneIDSpaceSimple(int realities, double modulusX,
			double modulusY, boolean wrapAround) {
		super("RANDOM_PLANE_ID_SPACE_SIMPLE", new String[] { "REALITIES",
				"MODULUS_X", "MODULUS_Y", "WRAP_AROUND" }, new String[] {
				"" + realities, "" + modulusX, "" + modulusY, "" + wrapAround });
		this.realities = realities;
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.wrapAround = wrapAround;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			PlanePartitionSimple[] partitions = new PlanePartitionSimple[graph
					.getNodes().length];
			PlaneIDSpaceSimple idSpace = new PlaneIDSpaceSimple(partitions,
					this.modulusX, this.modulusY, this.wrapAround);
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new PlanePartitionSimple(PlaneID.rand(rand,
						idSpace));
			}
			graph.addProperty("ID_SPACE_" + RandomIDSpace.nextIDSpace(graph),
					idSpace);
		}
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

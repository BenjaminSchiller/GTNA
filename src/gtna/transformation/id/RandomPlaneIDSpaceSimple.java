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
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RandomPlaneIDSpaceSimple extends Transformation {

	private double modulusX;

	private double modulusY;

	private boolean wrapAround;

	public RandomPlaneIDSpaceSimple() {
		super("RANDOM_PLANE_ID_SPACE_SIMPLE");
		this.modulusX = 1.0;
		this.modulusY = 1.0;
		this.wrapAround = false;
	}

	public RandomPlaneIDSpaceSimple(int realities, double modulusX,
			double modulusY, boolean wrapAround) {
		super("RANDOM_PLANE_ID_SPACE_SIMPLE", new Parameter[] {
				new DoubleParameter("MODULUS_X", modulusX),
				new DoubleParameter("MODULUS_Y", modulusY),
				new BooleanParameter("WRAP_AROUND", wrapAround) });
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.wrapAround = wrapAround;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		PlanePartitionSimple[] partitions = new PlanePartitionSimple[graph
				.getNodes().length];
		PlaneIdentifierSpaceSimple idSpace = new PlaneIdentifierSpaceSimple(
				partitions, this.modulusX, this.modulusY, this.wrapAround);
		for (int i = 0; i < partitions.length; i++) {
			partitions[i] = new PlanePartitionSimple(
					(PlaneIdentifier) idSpace.getRandomIdentifier(rand));
		}
		graph.addProperty(graph.getNextKey("ID_SPACE"), idSpace);
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

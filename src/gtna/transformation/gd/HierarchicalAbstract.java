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
 * HierarchicalAbstract.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.gd;

import gtna.graph.Graph;
import gtna.graph.spanningTree.SpanningTree;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.util.parameter.Parameter;

/**
 * @author Nico
 * 
 */
public abstract class HierarchicalAbstract extends GraphDrawingAbstract {

	protected PlaneIdentifierSpaceSimple idSpace;
	protected double[] nodePositionsX;
	protected double[] nodePositionsY;
	protected double modulusX;
	protected double modulusY;
	protected SpanningTree tree;

	public HierarchicalAbstract(String key, Parameter[] parameters) {
		super(key, parameters);
	}

	protected void initIDSpace(Graph g) {
		/*
		 * Nothing to do here
		 */
	}

	protected void writeIDSpace(Graph g) {
		/*
		 * As the current coordinates could exceed the given idSpace (or on the
		 * other hand use only a tiny pane), we need to calculate a scale factor
		 * for the coordinates
		 */
		double scaleX = 0, scaleY = 0;
		for (int i = 0; i < nodePositionsX.length; i++) {
			scaleX = Math.max(scaleX, Math.abs(nodePositionsX[i]));
			scaleY = Math.max(scaleY, Math.abs(nodePositionsY[i]));
		}
		/*
		 * The current scale factor would also use values on the borders of the
		 * idSpace (which will be cut to 0 by the modulus). So: scale it a tiny
		 * bit smaller
		 */
		scaleX = scaleX * 1.1;
		scaleY = scaleY * 1.1;

		PlaneIdentifier pos;
		PlanePartitionSimple[] partitions = new PlanePartitionSimple[g
				.getNodes().length];
		this.idSpace = new PlaneIdentifierSpaceSimple(partitions,
				this.modulusX, this.modulusY, false);
		for (int i = 0; i < nodePositionsX.length; i++) {
			pos = new PlaneIdentifier((nodePositionsX[i] / scaleX)
					* idSpace.getxModulus(), (nodePositionsY[i] / scaleY)
					* idSpace.getyModulus(), idSpace.getxModulus(),
					idSpace.getyModulus(), idSpace.isWrapAround());
			partitions[i] = new PlanePartitionSimple(pos);
		}
		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("SPANNINGTREE");
	}

}

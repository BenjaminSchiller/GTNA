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
 * ForceDrivenAbstract.java
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
import gtna.graph.Node;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDPartitionSimple;
import gtna.util.MDVector;
import gtna.util.parameter.Parameter;

/**
 * @author Nico
 * 
 */
public abstract class ForceDrivenAbstract extends GraphDrawingAbstract {

	protected MDIdentifierSpaceSimple idSpace;
	protected MDPartitionSimple[] partitions;
	protected int realities;
	protected double[] moduli;
	protected Boolean wrapAround;
	protected MDVector bias;

	public ForceDrivenAbstract(String key, Parameter[] parameters) {
		super(key, parameters);
	}

	protected void initIDSpace(Graph g) {
		for (int r = 0; r < this.realities; r++) {
			partitions = new MDPartitionSimple[g.getNodes().length];
			this.idSpace = new MDIdentifierSpaceSimple(partitions, this.moduli,
					this.wrapAround);
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new MDPartitionSimple(
						(MDIdentifier) idSpace.getRandomIdentifier(rand));
			}
		}
		generateBias();
	}

	protected void generateBias() {
		/*
		 * A bias is needed as the internal algorithm works on coordinates
		 * between (-modulus/2) and (+modulus/2) for each dimension
		 */
		bias = new MDVector(this.moduli.length);
		String moduliString = "";
		for (int i = 0; i < this.moduli.length; i++) {
			moduliString = moduliString + ", " + this.moduli[i];
			bias.setCoordinate(i, this.moduli[i] / 2);
		}
	}

	protected void writeIDSpace(Graph g) {
		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
	}

	protected MDVector setNormalized(MDVector v) {
		for (int i = 0; i < v.getDimension(); i++) {
			double coordinate = Math.min(idSpace.getModulus()[i] / 2,
					Math.max(idSpace.getModulus()[i] / -2, v.getCoordinate(i)));
			v.setCoordinate(i, coordinate);
		}
		return v;
	}

	protected MDVector getCoordinate(Node n) {
		return getCoordinate(n.getIndex());
	}

	protected MDVector getCoordinate(int i) {
		MDVector iV = new MDVector(
				(MDIdentifier) partitions[i].getRepresentativeIdentifier());
		// System.out.print("Retrieving " + iV);
		iV.subtract(bias);
		// System.out.println(" (biased: " + iV + ") for " +i);
		return iV;
	}

	protected void setCoordinate(Node v, MDVector newPos) {
		// System.out.print("Setting " + newPos);
		newPos.add(bias);
		// System.out.println(" (biased: " + newPos + ")");
		((MDIdentifier) partitions[v.getIndex()].getRepresentativeIdentifier())
				.setCoordinates(newPos.getCoordinates());
	}

}
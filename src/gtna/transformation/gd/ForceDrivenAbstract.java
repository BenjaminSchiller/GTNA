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

import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDPartitionSimple;
import gtna.plot.Gephi;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;
import gtna.util.MDVector;

/**
 * @author Nico
 *
 */
public abstract class ForceDrivenAbstract extends TransformationImpl {

	protected MDIdentifierSpaceSimple idSpace;
	protected MDPartitionSimple[] partitions;
	protected int realities;
	protected double[] moduli;
	protected Boolean wrapAround;	
	protected MDVector bias;
	protected Gephi gephi;	

	/**
	 * @param key
	 * @param configKeys
	 * @param configValues
	 */
	public ForceDrivenAbstract(String key, String[] configKeys,
			String[] configValues) {
		super(key, configKeys, configValues);
	}
	
	@Override
	public boolean applicable(Graph g) {
		return true;
	}
	
	protected void initIDSpace( Graph g ) {
		for (GraphProperty p : g.getProperties("ID_SPACE")) {
			if (p instanceof MDIdentifierSpaceSimple) {
				MDIdentifier id = (MDIdentifier) ((MDIdentifierSpaceSimple) p)
				.randomID( new Random() );
				if (!(id instanceof MDIdentifier)) {
					throw new RuntimeException("Okay, why do we have a MDIDSpace without a MDIdentifier?");
				}
					/*
					 * good question: how do we retrieve the number of realities from a given space?
					 */
				this.idSpace = (MDIdentifierSpaceSimple) p;
				
				this.partitions = (MDPartitionSimple[]) this.idSpace.getPartitions();
				this.moduli = idSpace.getModuli();
				this.wrapAround = idSpace.isWrapAround();				
			}
		}	
		
			/*
			 * A bias is needed as the internal algorithm works on coordinates
			 * between (-modulus/2) and (+modulus/2) for each dimension
			 */
		bias = new MDVector(this.moduli.length);
		String moduliString = "";
		for ( int i = 0; i < this.moduli.length; i++ ) {
			moduliString = moduliString + ", " + this.moduli[i];
			bias.setCoordinate(i, this.moduli[i] / 2);
		}		
	}

	protected MDVector setNormalized(MDVector v) {
		for ( int i = 0; i < v.getDimension(); i++ ) {
			double coordinate = Math.min(idSpace.getModulus(i)/2, Math.max(idSpace.getModulus(i)/-2, v.getCoordinate(i)));
			v.setCoordinate(i, coordinate);
		}
		return v;
	}

	protected MDVector getCoordinate(Node n) {
		return getCoordinate(n.getIndex());
	}

	protected MDVector getCoordinate(int i) {
			MDVector iV = ((MDIdentifier) partitions[i].getRepresentativeID()).toMDVector();
	//		System.out.print("Retrieving " + iV);
			iV.subtract(bias);
	//		System.out.println(" (biased: " + iV + ") for " +i);
			return iV;
		}

	protected void setCoordinate(Node v, MDVector newPos) {
	//		System.out.print("Setting " + newPos);
			newPos.add(bias);
	//		System.out.println(" (biased: " + newPos + ")");
			((MDIdentifier) partitions[v.getIndex()].getRepresentativeID()).setCoordinates(newPos.getCoordinates());
		}

}
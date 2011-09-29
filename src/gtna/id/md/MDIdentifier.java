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
 * MDIdentifier.java
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
package gtna.id.md;

import java.util.Random;

import gtna.id.DIdentifier;
import gtna.id.Identifier;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;

/**
 * @author Nico
 *
 */
public class MDIdentifier implements DIdentifier, Comparable<PlaneIdentifier> {
	private double[] coordinates;
	
	private MDIdentifierSpaceSimple idSpace;

	public MDIdentifier(double[] coordinates, MDIdentifierSpaceSimple idSpace) {
		this.coordinates = coordinates;
		for ( int i = 0; i < this.coordinates.length; i++ ) {
			this.coordinates[i] = this.coordinates[i] % idSpace.getModulus(i); 
		}
		this.idSpace = idSpace;
	}
	
	public MDIdentifier(String string, MDIdentifierSpaceSimple idSpace) {
		String[] temp = string.replace("(", "").replace(")", "").split("/");
		
		this.coordinates = new double[temp.length];
		for ( int i = 0; i < temp.length; i++ ) {
			this.coordinates[i] = Double.parseDouble(temp[i]) % idSpace.getModulus(i);
		}
		this.idSpace = idSpace;
	}
	
	public MDIdentifier(String string) {
		this(string, null);
	}

	public String toString() {
		StringBuilder temp = new StringBuilder("(");
		if ( coordinates.length >= 0 ) temp.append(coordinates[0]);
		for ( int i = 1; i < coordinates.length; i++ ) {
			temp.append("/" + coordinates[i]);
		}
		temp.append(")");
		return temp.toString();
	}
	
	@Override
	public Double distance(Identifier<Double> id) {
		MDIdentifier to = (MDIdentifier) id;
		if ( this.idSpace.getDimensions() != to.getIdSpace().getDimensions() ) {
			throw new RuntimeException("Cannot compute a distance between MDIntentifiers in spaces with unequal dimensions");
		}
		double squarredResult = 0;
		double temp;
		for ( int i = 0; i < this.idSpace.getDimensions(); i++ ) {
			if ( this.idSpace.isWrapAround() ) {
				 temp = Math.abs(this.coordinates[i] - to.getCoordinate(i))
					% (this.idSpace.getModulus(i) / 2.0);
			} else {
				 temp = Math.abs(this.coordinates[i] - to.getCoordinate(i));
			}
			squarredResult += Math.pow(temp, 2);
		}
		return Math.sqrt(squarredResult);
	}

	@Override
	public boolean equals(Identifier<Double> id) {
		return ( this.toString().equals( id.toString() ) );
	}
	
	public static MDIdentifier rand(Random rand, MDIdentifierSpaceSimple idSpace) {
		double[] newCoordinates = new double[idSpace.getDimensions()];
		for ( int i = 0; i < newCoordinates.length; i++ ) {
			newCoordinates[i] = rand.nextDouble() * idSpace.getModulus(i);
		}
		return new MDIdentifier(newCoordinates, idSpace);
	}
	
	public double getCoordinate ( int i ) {
		return this.coordinates[i];
	}

	public double[] getCoordinates() {
		return this.coordinates.clone();
	}
	
	public void setCoordinates ( double[] newPos ) {
		this.coordinates = newPos.clone();
	}
	
	public MDIdentifierSpaceSimple getIdSpace() {
		return this.idSpace;
	}

	@Override
	public int compareTo(PlaneIdentifier o) {
		// TODO Auto-generated method stub
		return 0;
	}	
}

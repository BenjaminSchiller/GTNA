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
 * AbstractGD.java
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

import java.util.Arrays;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDPartitionSimple;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;
import gtna.util.MDVector;

/**
 * @author Nico
 *
 */
public abstract class AbstractGD extends TransformationImpl implements Transformation {
		/*
		 * IDSpace and Partitions we care about
		 */
	protected MDIdentifierSpaceSimple idSpace;
	protected MDPartitionSimple[] partitions;
	
		/*
		 * Internal node positions
		 */
	protected MDVector[] nodePositions;
	
	
	public AbstractGD(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}
	
	protected void extractNodePositions ( Graph g ) {
		nodePositions = new MDVector[g.getNodes().length];
		for ( Node n: g.getNodes() ) {
			nodePositions[n.getIndex()] = new MDVector(idSpace.getDimensions(), ((MDIdentifier) partitions[n.getIndex()].getRepresentativeID()).getCoordinates());
		}
		double oldCoord = ((MDIdentifier) partitions[0].getRepresentativeID()).getCoordinate(0);
		
			// Now: transform the coordinates according to the moduli
		double[] moduli = idSpace.getModuli();
		MDVector moduliVector = new MDVector(moduli.length, moduli);
			// Divide by two to have two equal sized partitions on each side of the coordinate axis
		moduliVector.divideBy(2);
		for ( int i = 0; i < nodePositions.length; i++ ) {
			nodePositions[i].subtract ( moduliVector );
		}

		double newCoord = ((MDIdentifier) partitions[0].getRepresentativeID()).getCoordinate(0);
		if (oldCoord != newCoord ) {
			System.err.println("This cannot be true!");
			Throwable t = new Throwable();
			StackTraceElement[] es = t.getStackTrace();	
			StackTraceElement e = es[1];
			System.err.println( "at " + e.getClassName() + "."+ e.getMethodName()+ ", line " + e.getLineNumber() );
		}
	}
}

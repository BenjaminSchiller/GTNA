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
 * CircularAbstract.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDPartitionSimple;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace;
import gtna.id.ring.RingPartition;
import gtna.util.Util;

/**
 * @author Nico
 *
 */
public abstract class CircularAbstract extends GraphDrawingAbstract {

	protected RingIdentifierSpace idSpace;
	protected RingPartition[] partitions;
	protected int realities;
	protected double modulus;
	protected Boolean wrapAround;
	ArrayList<String> handledEdges;
	
	public CircularAbstract(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}

	protected void initIDSpace( Graph g ) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			RingPartition[] partitions = new RingPartition[g.getNodes().length];
			RingIdentifierSpace idSpace = new RingIdentifierSpace(partitions, this.modulus,
					this.wrapAround);
			RingIdentifier[] ids = new RingIdentifier[g.getNodes().length];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = RingIdentifier.rand(rand, idSpace);
			}
			Arrays.sort(ids);
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new RingPartition(ids[i], ids[(i + 1)
						% ids.length]);
			}
			Util.randomize(partitions, rand);
		}
	}

	protected void writeIDSpace ( Graph g ) {
		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
	}
	
	private int countAllCrossings(Graph g) {
		int numCross = 0;
		Edge[] edgeList = g.generateEdges();
		
		handledEdges = new ArrayList<String>();
		for ( Edge e: edgeList ) {
			numCross += countCrossings(e, edgeList);
		}
		System.out.println("Got " + numCross + " crossings");		
		return numCross;
	}

	private int countCrossings(Edge e, Edge[] list) {
		int numCross = 0;
		for ( Edge f: list ) {
			if ( hasCrossing(e, f) ) numCross++;
		}
		return numCross;
	}

	private Boolean hasCrossing(Edge x, Edge y) {
			/*
			 * There cannot be a crossing between only one edge
			 */
		if ( x.equals(y) ) return false;
		
		double xStart = Math.min ( getPosition( x.getSrc() ), getPosition( x.getDst() ) );
		double xEnd = Math.max ( getPosition( x.getSrc() ), getPosition( x.getDst() ) );
		String xString = xStart + " -> " +xEnd;
		double yStart = Math.min ( getPosition( y.getSrc() ), getPosition( y.getDst() ) );
		double yEnd = Math.max ( getPosition( y.getSrc() ), getPosition( y.getDst() ) );
		String yString = yStart + " -> " + yEnd;
		String edgeString;
		if ( xStart < yStart ) edgeString = xString + " and " + yString;
		else edgeString = yString + " and " + xString;
		
			/*
			 * Have we already handled this edge?
			 */
		if ( handledEdges.contains(edgeString) ) {
			return false;
		}
		handledEdges.add(edgeString);
		
		if ( ( xStart < yStart && xEnd > yEnd ) ||
			 ( yStart < xStart && yEnd > xEnd ) ||
			 ( yStart > xEnd || xStart > yEnd ) ||
			 ( yStart == xEnd || xStart == yEnd || xStart == yStart || xEnd == yEnd )
				) {
			System.out.println( "No crossing between " + edgeString );
			return false;
		}
		if ( ( xStart < yStart && xEnd < yEnd ) ||
				( xStart > yStart && xEnd > yEnd )
			)	{
			System.out.println("Got a crossing between " + edgeString);
			return true;
		}
		
		System.err.println( "Unknown case " + edgeString );
		return false;
	}

	private double getPosition(int i) {
		return partitions[i].getStart().getPosition();
	}
}

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.IdentifierSpace;
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
	HashSet<String> handledEdges;
	
	public CircularAbstract(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}

	protected void initIDSpace( Graph g ) {
		if ( !generateIDSpace ) return;
		
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			partitions = new RingPartition[g.getNodes().length];
			idSpace = new RingIdentifierSpace(partitions, this.modulus,
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
	
	public void setIDSpace(IdentifierSpace idSpace) {
		this.idSpace = (RingIdentifierSpace) idSpace.clone();
		this.partitions = (RingPartition[]) this.idSpace.getPartitions();
		this.modulus = this.idSpace.getModulus();
		this.generateIDSpace = false;
	}
	
	protected int countAllCrossings(Graph g) {
		int numCross = 0;
		Edge[] edgeList = g.generateEdges();
		
		handledEdges = new HashSet<String>();
		for ( Edge e: edgeList ) {
			numCross += countCrossings(e, edgeList);
		}
		return numCross;
	}
	
	protected int countCrossings (Graph g, Node n) {
		Edge[] nodeEdges = n.generateAllEdges();
		Edge[] graphEdges = g.generateEdges();
		handledEdges = new HashSet<String>();
		int numCross = 0;
		for ( Edge x: nodeEdges ) {
			for ( Edge y: graphEdges ) {
				if ( hasCrossing(x, y) ) numCross++;
			}
		}
		return numCross;
	}

	protected int countCrossings(Edge e, Edge[] list) {
		int numCross = 0;
		for ( Edge f: list ) {
			if ( hasCrossing(e, f) ) numCross++;
		}
		return numCross;
	}
	
	protected int countCrossings(Node n, Node m) {
		int numCross = 0;
		handledEdges = new HashSet<String>();
		Edge[] nEdges = n.generateAllEdges();
		Edge[] mEdges = m.generateAllEdges();
		for (Edge nEdge : nEdges) {
			for (Edge mEdge : mEdges) {
				if (hasCrossing(nEdge, mEdge))
					numCross++;
			}
		}
		return numCross;
	}

	private Boolean hasCrossing(Edge x, Edge y) {
			/*
			 * There cannot be a crossing between only one edge
			 */
		if ( x.equals(y) ) return false;
		if ( ( x.getSrc() == y.getSrc() ) || ( x.getSrc() == y.getDst() ) || ( x.getDst() == y.getSrc() ) || ( x.getDst() == y.getDst() ) ) return false;
		
		double xStart = Math.min ( getPosition( x.getSrc() ), getPosition( x.getDst() ) );
		double xEnd = Math.max ( getPosition( x.getSrc() ), getPosition( x.getDst() ) );
		double yStart = Math.min ( getPosition( y.getSrc() ), getPosition( y.getDst() ) );
		double yEnd = Math.max ( getPosition( y.getSrc() ), getPosition( y.getDst() ) );

		String xString = xStart + "-" +xEnd;
		String yString = yStart + "-" + yEnd;
		String edgeString;
		if ( xStart < yStart ) edgeString = xString + "|" + yString;
		else edgeString = yString + "|" + xString;
		
			/*
			 * Have we already handled this edge?
			 */
		if ( handledEdges.contains(edgeString) ) {
			return false;
		}
		handledEdges.add(edgeString);
		
		if ( ( xStart < yStart && xEnd > yEnd ) ||
			 ( yStart < xStart && yEnd > xEnd ) ||
			 ( yStart > xEnd || xStart > yEnd )	) {
//			System.out.println( "No crossing between " + edgeString );
			return false;
		}
		if ( ( xStart < yStart && xEnd < yEnd ) ||
				( xStart > yStart && xEnd > yEnd )
			)	{
//			System.out.println("Got a crossing between " + edgeString);
			return true;
		}
		
		System.err.println( "Unknown case " + edgeString );
		return false;
	}

	protected double getPosition(int i) {
		return partitions[i].getStart().getPosition();
	}
	
	protected int getPredecessor(int i) {
		double predEnd = partitions[i].getStart().getPosition();
		
		for (int j = 0; j < partitions.length; j++) {
			if ( partitions[j].getEnd().getPosition() == predEnd ) return j;
		}
		
		throw new RuntimeException("There's a hole in the RingIdentifierSpace!");
	}
	
	protected int getSuccessor(int i) {
		double succStart = partitions[i].getEnd().getPosition();
		
		for (int j = 0; j < partitions.length; j++) {
			if ( partitions[j].getStart().getPosition() == succStart ) return j;
		}
		
		throw new RuntimeException("There's a hole in the RingIdentifierSpace!");
	}
	
	protected void swapPositions(int i, int j) {
		RingPartition temp = partitions[i];
		partitions[i] = partitions[j];
		partitions[j] = temp;
	}
}

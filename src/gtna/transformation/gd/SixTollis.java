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
 * SixTollis.java
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
import java.util.Comparator;
import java.util.Random;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDPartitionSimple;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace;
import gtna.id.ring.RingPartition;
import gtna.networks.p2p.chord.ChordIdentifier;
import gtna.networks.p2p.chord.ChordPartition;
import gtna.plot.Gephi;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;
import gtna.transformation.id.RandomChordIDSpace;
import gtna.transformation.id.RandomMDIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpace;

/**
 * @author Nico
 *
 */
public class SixTollis extends TransformationImpl implements Transformation {
	/*
	 * IDSpace and Partitions we care about
	 */
	protected RingIdentifierSpace idSpace;
	protected RingPartition[] partitions;
	private int realities;
	private double modulus;
	private Boolean wrapAround;
	
	private Gephi gephi;
	private Transformation initialPositions;	
	
	public SixTollis() {
		this("GDA_SIX_TOLLIS", new String[]{}, new String[]{});
	}
	
	public SixTollis(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}
	
		/*
		 * Constructor for the case that we already have set the idspace
		 */
	public SixTollis(Gephi plotter) {
		this("GDA_SIX_TOLLIS", new String[]{}, new String[]{});
		this.gephi = plotter;
		this.initialPositions = null;
	}
	
	public SixTollis(int realities, double modulus, boolean wrapAround, Gephi plotter) {
		this("GDA_SIX_TOLLIS", new String[]{}, new String[]{});
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.gephi = plotter;
		initialPositions = new RandomRingIDSpace(realities, modulus, wrapAround);
	}	

	@Override
	public boolean applicable(Graph g) {
		return true;
	}
	
	@Override
	public Graph transform(Graph g) {
			if ( initialPositions != null ) {
				/*
				 * First step: create an idspace
				 */
			g = initialPositions.transform(g);
		}		
			
			/*
			 * idspace is now given, extract it
			 */
		for (GraphProperty p : g.getProperties("ID_SPACE")) {
			if (p instanceof RingIdentifierSpace) {
				RingIdentifier id = (RingIdentifier) ((RingIdentifierSpace) p)
				.randomID( new Random() );
				if (!(id instanceof RingIdentifier)) {
					throw new RuntimeException("Okay, why do we have a RingIDSpace without a RingIdentifier?");
				}
					/*
					 * good question: how do we retrieve the number of realities from a given space?
					 */
				this.modulus = ((RingIdentifierSpace) p).getModulus();
				this.wrapAround = ((RingIdentifierSpace) p).isWrapAround();
				this.idSpace = (RingIdentifierSpace) p;
				
				this.partitions = (RingPartition[]) this.idSpace.getPartitions();
			}
	}
		
		countAllCrossings(g);
		return g;
	}
	
	private int countAllCrossings( Graph g ) {
		// See diploma thesis by Michael Baur, page 43
		
		Node[] nodeList = g.getNodes();
		int[] openEdgesIdentifiers = new int[nodeList.length];

		Edge[] edgeList = g.generateEdges();
			// startNode[]: Array over all nodes; contains for each node a list of edges for which this node is the starting poin
		ArrayList<Integer>[] startNodeIdentifiers = new ArrayList[nodeList.length];
		ArrayList<Integer>[] targetNodeIdentifiers = new ArrayList[nodeList.length];
		for ( int i = 0; i < nodeList.length; i++ ) {
			startNodeIdentifiers[i] = new ArrayList<Integer>();
			for ( int temp: g.getNode(i).getOutgoingEdges() ) {
				startNodeIdentifiers[i].add(temp);
			}
			targetNodeIdentifiers[i] = new ArrayList<Integer>();
			for ( int temp: g.getNode(i).getIncomingEdges() ) {
				targetNodeIdentifiers[i].add(temp);
			}
		}
		
			/*
			 * Sort the nodeList - we want to walk the ring from twelve o'clock clockwise!
			 */
		Arrays.sort(nodeList, new NodeRingIDComparator());
		
		return -1;
	}

	private class NodeRingIDComparator implements Comparator<Node> {
		  @Override
		  public int compare(Node n1, Node n2) {
			  double n1ID = partitions[n1.getIndex()].getStart().getPosition();
			  double n2ID = partitions[n2.getIndex()].getStart().getPosition();
			  return Double.compare(n1ID, n2ID);
		  }
	}	
	
}

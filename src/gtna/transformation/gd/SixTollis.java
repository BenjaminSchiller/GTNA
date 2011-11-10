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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.plot.GraphPlotter;
import gtna.transformation.Transformation;

/**
 * @author Nico
 *
 */
public class SixTollis extends CircularAbstract {
	public SixTollis(int realities, double modulus, boolean wrapAround, GraphPlotter plotter) {
		super("GDA_SIX_TOLLIS", new String[] {"REALITIES", "MODULUS", "WRAPAROUND"}, new String[] {"" + realities, "" + modulus, "" + wrapAround});
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.graphPlotter = plotter;
	}	

	@Override
	public Graph transform(Graph g) {
		System.err.println("This is not working completely yet, so don't expect good results!");
		
		initIDSpace(g);
		
			/*
			 * Phase 1
			 */
		Node currentNode = null;
		List<Node> nodeList = Arrays.asList(g.getNodes());
		List<Edge> removalList = new ArrayList<Edge>();
		Collections.sort(nodeList, new NodeDegreeComparator());
		for ( int counter = 1; counter < ( nodeList.size() - 3 ); counter++ ) {
			Boolean selectedANodeForThisIteration = false;
			if ( currentNode != null ) {
				// try to get a wave front / wave center node
			}
			if ( !selectedANodeForThisIteration ) {
				// We got no node yet - choose one with lowest degree
				currentNode = nodeList.get(0);
			}
			List<Edge> establishedEdges = getPairEdges ( currentNode );
		}
		
		for ( Node n: nodeList ) System.out.println(n + " has degree " + n.getDegree());
		
//		countAllCrossings(g);
		writeIDSpace(g);
		return g;
	}
	
	private List<Edge> getPairEdges ( Node n ) {
		List<Edge> result = new ArrayList<Edge>();
		int[] incomingEdges = n.getIncomingEdges();
		for ( int eDst: incomingEdges ) {
			if ( isPairEdge( n.getIndex(), eDst ) ) result.add( new Edge(n.getIndex(), eDst));
		}
		return result;
	}
	
	private Boolean isPairEdge ( int src, int dst ) {
		// First test: Node src's endPosition is Node dst's start
		if ( partitions[src].getEnd().getPosition() == partitions[dst].getStart().getPosition() ) {
			return true;
		}
		// Second test: Node dst's endPosition is Node src's start		
		if ( partitions[dst].getEnd().getPosition() == partitions[src].getStart().getPosition() ) {
			return true;
		}
		return false;
	}

	private class NodeDegreeComparator implements Comparator<Node> {
		  @Override
		  public int compare(Node n1, Node n2) {
			  if ( n1.getDegree() == n2.getDegree() ) return 0;
			  else if ( n1.getDegree() > n2.getDegree() ) return 1;
			  else return -1;
		  }
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

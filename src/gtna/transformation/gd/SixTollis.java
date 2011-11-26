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
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.plot.GraphPlotter;

/**
 * @author Nico
 * 
 */
public class SixTollis extends CircularAbstract {
	private TreeSet<Node> waveCenterNodes, waveFrontNodes, removedNodes;
	private List<Node> nodeList;

	public SixTollis(int realities, double modulus, boolean wrapAround, GraphPlotter plotter) {
		super("GDA_SIX_TOLLIS", new String[] { "REALITIES", "MODULUS", "WRAPAROUND" }, new String[] { "" + realities,
				"" + modulus, "" + wrapAround });
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
		Node tempNode = null;
		Node currentNode = null;
		Node lastNode = null;
		List<Edge> removalList = new ArrayList<Edge>();
		removedNodes = new TreeSet<Node>();
		waveCenterNodes = new TreeSet<Node>();
		waveFrontNodes = new TreeSet<Node>();

		nodeList = Arrays.asList(g.getNodes());
		Collections.sort(nodeList, new NodeDegreeComparator());
		for (int counter = 1; counter < (nodeList.size() - 3); counter++) {
			currentNode = getNode(lastNode);
			HashSet<Edge> establishedEdges = getPairEdges(g, currentNode);

			/*
			 * Keep track of wave front and wave center nodes!
			 */

			waveFrontNodes = new TreeSet<Node>();
			for (Edge i : currentNode.generateAllEdges()) {
				int otherEnd;
				if (i.getDst() == currentNode.getIndex()) {
					otherEnd = i.getSrc();
				} else {
					otherEnd = i.getDst();
				}
				tempNode = g.getNode(otherEnd);
				if (removedNodes.contains(tempNode)) {
					continue;
				}
				waveFrontNodes.add(tempNode);
				waveCenterNodes.add(tempNode);
			}
			lastNode = currentNode;
		}

		for (Node n : nodeList)
			System.out.println(n + " has degree " + n.getDegree());

		// countAllCrossings(g);
		writeIDSpace(g);
		return g;
	}

	/**
	 * @return
	 */
	private Node getNode(Node lastNode) {
		/*
		 * Retrieve any wave front node...
		 */
		if (waveFrontNodes != null) {
			for (Node tempNode : waveFrontNodes) {
				if (!removedNodes.contains(tempNode)) {
					return tempNode;
				}
			}
		}
		/*
		 * ...or a wave center node...
		 */
		if (waveCenterNodes != null) {
			for (Node tempNode : waveCenterNodes) {
				if (!removedNodes.contains(tempNode)) {
					return tempNode;
				}
			}
		}
		/*
		 * ...or any lowest degree node
		 */
		for (Node tempNode : nodeList) {
			if (!removedNodes.contains(tempNode)) {
				return tempNode;
			}
		}
		return null;
	}

	private HashSet<Edge> getPairEdges(Graph g, Node n) {
		HashSet<Edge> result = new HashSet<Edge>();
		Node tempNode;
		int otherEnd;
		
		throw new RuntimeException("Computing pair edges is not implemented yet");
		Edge[] allEdges = n.generateAllEdges();
		for ( Edge tempEdge: allEdges ) {
			if (tempEdge.getDst() == n.getIndex()) {
				otherEnd = tempEdge.getSrc();
			} else {
				otherEnd = tempEdge.getDst();
			}
		}
		
		return result;
	}

	private class NodeDegreeComparator implements Comparator<Node> {
		@Override
		public int compare(Node n1, Node n2) {
			if (n1.getDegree() == n2.getDegree())
				return 0;
			else if (n1.getDegree() > n2.getDegree())
				return 1;
			else
				return -1;
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

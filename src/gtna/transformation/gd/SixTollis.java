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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
	private HashMap<String, Edge> removalList;
	private HashMap<String, Edge>[] additionalEdges;
	private TreeNode deepestNode;

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
		Node randDst1, randDst2;
		Edge tempEdge;
		String tempEdgeString;
		removalList = new HashMap<String, Edge>();
		HashMap<String, Edge> pairEdges = null;
		removedNodes = new TreeSet<Node>(new NodeComparator());
		waveCenterNodes = new TreeSet<Node>(new NodeComparator());
		waveFrontNodes = new TreeSet<Node>(new NodeComparator());

		additionalEdges = new HashMap[g.getNodes().length];
		for (int i = 0; i < g.getNodes().length; i++) {
			additionalEdges[i] = new HashMap<String, Edge>();
		}

		nodeList = Arrays.asList(g.getNodes().clone());
		Collections.sort(nodeList, new NodeDegreeComparator());
		for (int counter = 1; counter < (nodeList.size() - 3); counter++) {
			currentNode = getNode(lastNode);
			pairEdges = getPairEdges(g, currentNode);
			for (Edge singleEdge : pairEdges.values()) {
				removalList.put(getEdgeString(singleEdge), singleEdge);
			}

			int currentNodeDegree = getEdges(g, currentNode).size();
			int triangulationEdgesCount = (currentNodeDegree - 1) - pairEdges.size();
			int[] outgoingEdges = currentNode.getOutgoingEdges();
//			System.out.println(currentNode.getIndex() + " has a current degree of " + currentNodeDegree + ", "
//					+ pairEdges.size() + " pair edges");

			int loopCounter = 0;
			while (triangulationEdgesCount > 0) {
				if (currentNodeDegree == 2) {
					randDst1 = g.getNode(outgoingEdges[0]);
					randDst2 = g.getNode(outgoingEdges[1]);
				} else {
					randDst1 = g.getNode(outgoingEdges[rand.nextInt(outgoingEdges.length)]);
					randDst2 = g.getNode(outgoingEdges[rand.nextInt(outgoingEdges.length)]);
				}
				
//				System.out.println("rand1: " + randDst1.getIndex() + "; rand2: " + randDst2.getIndex());
				
				if (!randDst1.isConnectedTo(randDst2)) {
					tempEdge = new Edge(Math.min(randDst1.getIndex(), randDst2.getIndex()), Math.max(
							randDst1.getIndex(), randDst2.getIndex()));
					tempEdgeString = getEdgeString(tempEdge);
					if (randDst1.getIndex() != randDst2.getIndex()
							&& !additionalEdges[randDst1.getIndex()].containsKey(tempEdgeString)) {
//						System.out.println("Adding triangulation edge " + tempEdge);
						additionalEdges[randDst1.getIndex()].put(tempEdgeString, tempEdge);
						additionalEdges[randDst2.getIndex()].put(tempEdgeString, tempEdge);
						triangulationEdgesCount--;
						loopCounter = 0;
					} else {
//						System.out.println("Not adding triangulation edge " + tempEdge);
					}
				}

				if (loopCounter++ > 10)
					throw new RuntimeException("Could not find anymore pair edges");
			}

			/*
			 * Keep track of wave front and wave center nodes!
			 */

			for (Edge i : getEdges(g, currentNode).values()) {
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
			removedNodes.add(currentNode);
		}

		/*
		 * Do the DFS here
		 */
		LinkedList<Node> longestPath = longestPath(g);
		if ( longestPath == null ) throw new RuntimeException("No lP");

		for (Node n : nodeList)
			System.out.println(n + " has degree " + n.getDegree());

		// countAllCrossings(g);
		writeIDSpace(g);
		return g;
	}

	private ArrayList<Edge> getAllEdges(Graph g, Node n) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Edge e : n.generateAllEdges())
			edges.add(e);
		edges.addAll(additionalEdges[n.getIndex()].values());
		return edges;
	}

	private HashMap<String, Edge> getEdges(Graph g, Node n) {
		Node tempNode = null;
		HashMap<String, Edge> edges = new HashMap<String, Edge>();

		for (Edge i : getAllEdges(g, n)) {
			int otherEnd;
			if (i.getDst() == n.getIndex()) {
				otherEnd = i.getSrc();
			} else {
				otherEnd = i.getDst();
			}
			tempNode = g.getNode(otherEnd);
			if (removedNodes.contains(tempNode)) {
				continue;
			}
			edges.put(getEdgeString(i), i);
		}
		return edges;
	}

	private LinkedList<Node> longestPath(Graph g) {
		TreeNode root = new TreeNode(null, 0, 0);
		deepestNode = root;
		dfs(g, g.getNode(0), root, new ArrayList<Integer>());

		/*
		 * Create the path now!
		 */
		return null;
	}

	private void dfs(Graph g, Node n, TreeNode root, ArrayList<Integer> visited) {
		int otherEnd;

		if (visited.contains(n.getIndex())) {
			return;
		}

		visited.add(n.getIndex());
		TreeNode current = new TreeNode(root, n.getIndex(), root.depth + 1);
		root.children.add(current);
		if (current.depth > deepestNode.depth) {
			deepestNode = current;
		}

		for (Edge mEdge : getEdges(g, n).values()) {
			if (removalList.containsKey(mEdge.toString())) {
				/*
				 * Please, respect our removalList!
				 */
				continue;
			}
			if (mEdge.getDst() == n.getIndex()) {
				otherEnd = mEdge.getSrc();
			} else {
				otherEnd = mEdge.getDst();
			}
			Node mNode = g.getNode(otherEnd);
			dfs(g, mNode, current, visited);
		}
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
		throw new RuntimeException("No node left");
	}

	private HashMap<String, Edge> getPairEdges(Graph g, Node n) {
		HashMap<String, Edge> result = new HashMap<String, Edge>();
		Node tempInnerNode;
		Edge tempEdge;
		int otherOuterEnd, otherInnerEnd;

//		System.out.println("\n\nCalling getPairEdges for node " + n.getIndex());
		HashMap<String, Edge> allOuterEdges = getEdges(g, n);
		for (Edge tempOuterEdge : allOuterEdges.values()) {
//			System.out.println("\n");
			if (tempOuterEdge.getDst() == n.getIndex()) {
				otherOuterEnd = tempOuterEdge.getSrc();
			} else {
				otherOuterEnd = tempOuterEdge.getDst();
			}
//			System.out.println("For the edge " + tempOuterEdge + ", " + otherOuterEnd + " is the other node");
			HashMap<String, Edge> allInnerEdges = getEdges(g, g.getNode(otherOuterEnd));
			for (Edge tempInnerEdge : allInnerEdges.values()) {
//				System.out.println(tempInnerEdge + " is an edge for " + otherOuterEnd);
				if (tempInnerEdge.getDst() == otherOuterEnd) {
					otherInnerEnd = tempInnerEdge.getSrc();
				} else {
					otherInnerEnd = tempInnerEdge.getDst();
				}
				if (otherInnerEnd == n.getIndex()) {
					continue;
				}
				tempInnerNode = g.getNode(otherInnerEnd);
				if (tempInnerNode.isConnectedTo(n)) {
					tempEdge = new Edge(Math.min(otherInnerEnd, otherOuterEnd), Math.max(otherInnerEnd, otherOuterEnd));
//					System.out.println(getEdgeString(tempEdge) + " is a pair edge of " + otherOuterEnd + " and " + otherInnerEnd);
					result.put(getEdgeString(tempEdge), tempEdge);
				} else {
//					System.out.println("No pair edge between " + otherOuterEnd + " and " + otherInnerEnd);
				}
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

	private String getEdgeString(Edge e) {
		return Math.min(e.getSrc(), e.getDst()) + "->" + Math.max(e.getSrc(), e.getDst());
	}

	private class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node n1, Node n2) {
			if (n1 == null || n2 == null || n1.getIndex() == n2.getIndex())
				return 0;
			else if (n1.getIndex() > n2.getIndex())
				return 1;
			else
				return -1;
		}
	}

	private class TreeNode {
		public TreeNode root;
		public int index, depth;
		public ArrayList<TreeNode> children;

		public TreeNode(TreeNode root, int index, int depth) {
			this.root = root;
			this.index = index;
			this.depth = depth;
			this.children = new ArrayList<TreeNode>();
		}
	}
}

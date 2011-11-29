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
	private Boolean filterRemovalList = false;
	private Graph g;

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
		this.g = g;

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
			pairEdges = getPairEdges(currentNode);
			for (Edge singleEdge : pairEdges.values()) {
				removalList.put(getEdgeString(singleEdge), singleEdge);
			}

			HashMap<String, Edge> currentNodeConnections = getEdges(currentNode);
			int currentNodeDegree = currentNodeConnections.size();
			int triangulationEdgesCount = (currentNodeDegree - 1) - pairEdges.size();
			int[] outgoingEdges = filterOutgoingEdges(currentNode, currentNodeConnections);

//			System.out.print(currentNode.getIndex() + " has a current degree of " + currentNodeDegree + ", "
//					+ pairEdges.size() + " pair edges and a need for " + triangulationEdgesCount
//					+ " triangulation edges - existing edges:");
//			for (Edge sE : currentNodeConnections.values()) {
//				System.out.print(" " + sE);
//			}
//			System.out.println();

			int firstCounter = 0;
			int secondCounter = 1;
			while (triangulationEdgesCount > 0) {
				randDst1 = g.getNode(outgoingEdges[firstCounter]);
				randDst2 = g.getNode(outgoingEdges[secondCounter]);
				if (randDst1.equals(randDst2))
					continue;
				if (removedNodes.contains(randDst1) || removedNodes.contains(randDst2)) {
					continue;
				}

//				System.out.println("rand1: " + randDst1.getIndex() + "; rand2: " + randDst2.getIndex());
//				System.out.print("Outgoing edges for r1:");
//				for (int i : filterOutgoingEdges(randDst1, getEdges(randDst1))) {
//					System.out.print(" " + i);
//				}
//				System.out.println("");

				if (!connected(randDst1, randDst2)) {
					tempEdge = new Edge(Math.min(randDst1.getIndex(), randDst2.getIndex()), Math.max(
							randDst1.getIndex(), randDst2.getIndex()));
					tempEdgeString = getEdgeString(tempEdge);
					if (randDst1.getIndex() != randDst2.getIndex()
							&& !additionalEdges[randDst1.getIndex()].containsKey(tempEdgeString)) {
//						System.out.println("Adding triangulation edge " + tempEdge);
						additionalEdges[randDst1.getIndex()].put(tempEdgeString, tempEdge);
						additionalEdges[randDst2.getIndex()].put(tempEdgeString, tempEdge);
						triangulationEdgesCount--;
					}
				} else {
//					System.out.println("Node " + randDst1.getIndex() + " is already connected to "
//							+ randDst2.getIndex());
				}

				secondCounter++;
				if (secondCounter == (currentNodeDegree - 1)) {
					firstCounter++;
					secondCounter = firstCounter + 1;
				}

				if (firstCounter == (currentNodeDegree - 1))
					throw new RuntimeException("Could not find anymore pair edges for " + currentNode.getIndex());
			}

			/*
			 * Keep track of wave front and wave center nodes!
			 */

			waveFrontNodes = new TreeSet<Node>(new NodeComparator());
			for (Edge i : getEdges(currentNode).values()) {
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
//			System.out.println("Adding " + currentNode.getIndex() + " to removedNodes");
		}

		/*
		 * Do the DFS here
		 */
		LinkedList<Node> longestPath = longestPath();
		if (longestPath == null)
			throw new RuntimeException("No lP");

		for (Node n : nodeList)
			System.out.println(n + " has degree " + n.getDegree());

		// countAllCrossings(g);
		writeIDSpace(g);
		return g;
	}

	private ArrayList<Edge> getAllEdges(Node n) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Edge e : n.generateAllEdges())
			edges.add(e);
		edges.addAll(additionalEdges[n.getIndex()].values());
		return edges;
	}

	private HashMap<String, Edge> getEdges(Node n) {
		Node tempNode = null;
		HashMap<String, Edge> edges = new HashMap<String, Edge>();

		for (Edge i : getAllEdges(n)) {
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

	private int[] filterOutgoingEdges(Node n, HashMap<String, Edge> edges) {
		int[] result = new int[edges.size()];
		int edgeCounter = 0;
		for (Edge sE : edges.values()) {
			int otherEnd;
			if (sE.getDst() == n.getIndex()) {
				otherEnd = sE.getSrc();
			} else {
				otherEnd = sE.getDst();
			}
			result[edgeCounter++] = otherEnd;
		}
		return result;
	}

	private Boolean connected(Node n, Node m) {
		int[] edges = filterOutgoingEdges(n, getEdges(n));
		for (int sE : edges) {
			if (sE == m.getIndex())
				return true;
		}
		return false;
	}

	private LinkedList<Node> longestPath() {
		filterRemovalList = true;
		TreeNode root = new TreeNode(null, 0, 0);
		deepestNode = root;
		dfs(g.getNode(0), root, new ArrayList<Integer>());

		/*
		 * Create the path now!
		 */
		return null;
	}

	private void dfs(Node n, TreeNode root, ArrayList<Integer> visited) {
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

		for (Edge mEdge : getEdges(n).values()) {
			if (filterRemovalList && removalList.containsKey(mEdge.toString())) {
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
			dfs(mNode, current, visited);
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
					waveFrontNodes.remove(tempNode);
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
					waveCenterNodes.remove(tempNode);
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

	private HashMap<String, Edge> getPairEdges(Node n) {
		HashMap<String, Edge> result = new HashMap<String, Edge>();
		Node tempInnerNode;
		Edge tempEdge;
		int otherOuterEnd, otherInnerEnd;

		// System.out.println("\n\nCalling getPairEdges for node " +
		// n.getIndex());
		HashMap<String, Edge> allOuterEdges = getEdges(n);
		for (Edge tempOuterEdge : allOuterEdges.values()) {
			// System.out.println("\n");
			if (tempOuterEdge.getDst() == n.getIndex()) {
				otherOuterEnd = tempOuterEdge.getSrc();
			} else {
				otherOuterEnd = tempOuterEdge.getDst();
			}
			// System.out.println("For the edge " + tempOuterEdge + ", " +
			// otherOuterEnd + " is the other node");
			HashMap<String, Edge> allInnerEdges = getEdges(g.getNode(otherOuterEnd));
			for (Edge tempInnerEdge : allInnerEdges.values()) {
				// System.out.println(tempInnerEdge + " is an edge for " +
				// otherOuterEnd);
				if (tempInnerEdge.getDst() == otherOuterEnd) {
					otherInnerEnd = tempInnerEdge.getSrc();
				} else {
					otherInnerEnd = tempInnerEdge.getDst();
				}
				if (otherInnerEnd == n.getIndex()) {
					continue;
				}
				tempInnerNode = g.getNode(otherInnerEnd);
				if (connected(n, tempInnerNode)) {
					tempEdge = new Edge(Math.min(otherInnerEnd, otherOuterEnd), Math.max(otherInnerEnd, otherOuterEnd));
					// System.out.println(getEdgeString(tempEdge) +
					// " is a pair edge of " + otherOuterEnd + " and " +
					// otherInnerEnd);
					result.put(getEdgeString(tempEdge), tempEdge);
				} else {
					// System.out.println("No pair edge between " +
					// otherOuterEnd + " and " + otherInnerEnd);
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

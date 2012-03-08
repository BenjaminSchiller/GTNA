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
 * BFS.java
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
package gtna.transformation.spanningtree;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.spanningTree.ParentChild;
import gtna.graph.spanningTree.SpanningTree;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Nico
 * 
 */
public class BFS extends Transformation {
	String rootSelector;

	public BFS() {
		this("zero");
	}

	public BFS(String rootSelector) {
		super("SPANNINGTREE_BFS", new Parameter[] { new StringParameter(
				"ROOT_SELECTOR", rootSelector) });
		this.rootSelector = rootSelector;
	}

	@Override
	public Graph transform(Graph graph) {
		Node root = selectRoot(graph, rootSelector);

		Node tempNodeFromList;
		Integer[] edges;
		Node[] nodes = graph.getNodes();
		int depth;

		LinkedList<Node> todoList = new LinkedList<Node>();

		HashMap<Integer, ParentChild> parentChildMap = new HashMap<Integer, ParentChild>();

		todoList.add(root);
		parentChildMap.put(root.getIndex(), new ParentChild(-1,
				root.getIndex(), 0));
		while (!todoList.isEmpty()) {
			tempNodeFromList = todoList.pop();

			ParentChild parent = parentChildMap
					.get(tempNodeFromList.getIndex());
			if (parent == null) {
				depth = 1;
			} else {
				depth = parent.getDepth() + 1;
			}

			edges = tempNodeFromList.generateOutgoingEdgesByDegree();
			for (int e : edges) {
				if (!parentChildMap.containsKey(e)) {
					/*
					 * Node e has not been linked yet, so add it to the todoList
					 * (to handle it soon) and add the edge from the current
					 * node to e to the list of edges
					 */
					todoList.add(nodes[e]);

					parentChildMap.put(e,
							new ParentChild(tempNodeFromList.getIndex(), e,
									depth));
				}
			}
		}

		int graphNodeSize = graph.getNodes().length;
		int spanningTreeSize = parentChildMap.size();
		if ((spanningTreeSize + 1) < graphNodeSize) {
			for (Node sN : nodes) {
				if (!parentChildMap.containsKey(sN.getIndex())) {
					System.err.print(sN + " missing, connections to ");
					for (int e : sN.generateOutgoingEdgesByDegree()) {
						System.err.print(e + " ");
					}
					System.err.println();
				}
			}
			throw new RuntimeException("Error: graph contains " + graphNodeSize
					+ ", but spanning tree only contains " + spanningTreeSize
					+ " nodes - graph might be disconnected");
		}
		ArrayList<ParentChild> parentChildList = new ArrayList<ParentChild>();
		parentChildList.addAll(parentChildMap.values());
		SpanningTree result = new SpanningTree(graph, parentChildList);

		graph.addProperty("SPANNINGTREE", result);
		return graph;
	}

	private Node selectRoot(Graph graph, String rootSelector) {
		Random rand = new Random();
		Node result = null;
		Node[] nodeList = graph.getNodes();
		if (rootSelector == "zero") {
			return nodeList[0];
		} else if (rootSelector == "rand") {
			return nodeList[rand.nextInt(nodeList.length)];
		} else if (rootSelector == "hd") {
			/*
			 * Here, one of the nodes with highest degree will be chosen
			 */
			List<Node> sortedNodeList = Arrays.asList(nodeList.clone());
			Collections.sort(sortedNodeList);
			List<Node> highestDegreeNodes = sortedNodeList.subList(
					(int) (0.9 * sortedNodeList.size()), sortedNodeList.size());
			result = highestDegreeNodes.get(rand.nextInt(highestDegreeNodes
					.size()));
		} else {
			throw new RuntimeException("Unknown root selector " + rootSelector);
		}
		return result;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}
}

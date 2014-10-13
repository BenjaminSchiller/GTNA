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
 * Original Author: Dirk;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.trust;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import gtna.graph.Graph;

/**
 * @author Dirk
 * 
 */
public class BFS {
	private int k = 0;
	private Graph g;
	private EdgeValuator e;

	private int nodesCounter;
	private int edgesCounter;

	private List<Integer> visitedNodes;

	private int predA[];
	private int succB[];

	private int rendevouz;

	private List<Integer> closedNodes;

	public BFS(Graph g, int k) {
		this.k = k;
		this.g = g;
		this.e = new EdgeValuator() {

			@Override
			public double getEdgeValue(Graph g, int src, int dst) {
				return 1;
			}
		};

		closedNodes = new ArrayList<Integer>();
	}

	public BFS(Graph g, int k, EdgeValuator e) {
		this.k = k;
		this.g = g;
		this.e = e;
		
		closedNodes = new ArrayList<Integer>();
	}

	public void search(int srcNode) {
		unidirectonalSearch(srcNode, -1);
	}

	public void unidirectonalSearch(int srcNode, int dstNode) {
		boolean[] visited = new boolean[g.getNodeCount()];
		double[] distance = new double[g.getNodeCount()];
		Queue<Integer> queue = new LinkedList<Integer>();

		visitedNodes = new ArrayList<Integer>();

		visited[srcNode] = true;

		queue.add(srcNode);

		nodesCounter = 0;
		edgesCounter = 0;

		while (!queue.isEmpty()) {
			int currentNode = queue.poll();
			nodesCounter++;
			for (int neighbor : g.getNode(currentNode).getOutgoingEdges()) {
				edgesCounter++;
				if (!visited[neighbor] && !closedNodes.contains(neighbor)) {
					distance[neighbor] = distance[currentNode]
							+ e.getEdgeValue(g, currentNode, neighbor);
					if (distance[neighbor] <= k) {
						visited[neighbor] = true;
						visitedNodes.add(neighbor);
						queue.add(neighbor);
						if (neighbor == dstNode)
							queue.clear();
					}
				}
			}
		}

	}

	/*
	 * Performs a bidrectional BFS
	 */
	public boolean bidirectionalSearch(int srcNode, int dstNode) {
		boolean[] visitedA = new boolean[g.getNodeCount()];
		double[] distanceA = new double[g.getNodeCount()];
		Queue<Integer> queueA = new LinkedList<Integer>();

		boolean[] visitedB = new boolean[g.getNodeCount()];
		double[] distanceB = new double[g.getNodeCount()];
		Queue<Integer> queueB = new LinkedList<Integer>();

		predA = new int[g.getNodeCount()];
		succB = new int[g.getNodeCount()];
		for (int i = 0; i < g.getNodeCount(); i++) {
			predA[i] = -1;
			succB[i] = -1;
		}
		rendevouz = -1;

		visitedA[srcNode] = true;
		queueA.add(srcNode);

		visitedB[dstNode] = true;
		queueB.add(dstNode);

		int kA = k / 2;
		int kB = k / 2;

		if (k % 2 == 1)
			kA++;

		boolean found = false;

		while (!queueA.isEmpty() || !queueB.isEmpty()) {

			
			if (!queueA.isEmpty()) {
				int currentNodeA = queueA.poll();
			
				if (visitedB[currentNodeA]) {
					queueA.clear();
					queueB.clear();
					found = true;
					rendevouz = currentNodeA;
				} else {
					for (int neighbor : g.getNode(currentNodeA)
							.getOutgoingEdges()) {

						if (!visitedA[neighbor]
								&& !closedNodes.contains(neighbor)) {

							distanceA[neighbor] = distanceA[currentNodeA]
									+ e.getEdgeValue(g, currentNodeA, neighbor);
							if (distanceA[neighbor] <= kA) {
								visitedA[neighbor] = true;
								predA[neighbor] = currentNodeA;
								queueA.add(neighbor);
							} 
						}
					}
				}
			}

			if (!queueB.isEmpty()) {
				int currentNodeB = queueB.poll();

				if (visitedA[currentNodeB]) {
					queueA.clear();
					queueB.clear();
					found = true;
					rendevouz = currentNodeB;
				} else {
					for (int neighbor : g.getNode(currentNodeB)
							.getIncomingEdges()) {

						if (!visitedB[neighbor]
								&& !closedNodes.contains(neighbor)) {

							distanceB[neighbor] = distanceB[currentNodeB]
									+ e.getEdgeValue(g, currentNodeB, neighbor);
							if (distanceB[neighbor] <= kB) {

								visitedB[neighbor] = true;
								succB[neighbor] = currentNodeB;
								queueB.add(neighbor);
							}
						}
					}
				}
			}
		}
		return found;
	}

	public List<Integer> getPath(int src, int dst) {


		boolean exists = bidirectionalSearch(src, dst);


		if (exists) {
			int current = rendevouz;

			LinkedList<Integer> path = new LinkedList<Integer>();

			if (rendevouz != dst && rendevouz != src) {
				while (current != src) {
					path.addFirst(current);
					current = predA[current];
				}

				current = succB[rendevouz];

				while (current != dst) {
					path.addLast(current);
					current = succB[current];
				}
			}

			return path;
		}
		return null;
	}

	/**
	 * @return the nodesCounter
	 */
	public int getNodesCounter() {
		return this.nodesCounter;
	}

	/**
	 * @return the edgesCounter
	 */
	public int getEdgesCounter() {
		return this.edgesCounter;
	}

	/**
	 * @return the visitedNodes
	 */
	public List<Integer> getVisitedNodes() {
		return this.visitedNodes;
	}

	/**
	 * @return the closedNodes
	 */
	public List<Integer> getClosedNodes() {
		return closedNodes;
	}

	/**
	 * @param closedNodes
	 *            the closedNodes to set
	 */
	public void setClosedNodes(List<Integer> closedNodes) {
		this.closedNodes = closedNodes;
	}
}

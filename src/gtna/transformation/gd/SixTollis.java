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

import gtna.drawing.GraphPlotter;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.spanningTree.ParentChild;
import gtna.graph.spanningTree.SpanningTree;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingPartition;
import gtna.metrics.edges.EdgeCrossings;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Nico
 * 
 */
public class SixTollis extends CircularAbstract {
	private TreeSet<Node> waveCenterVertices, waveFrontVertices;
	private List<Node> vertexList, removedVertices;
	private HashMap<String, Edge> removedEdges;
	private HashMap<String, Edge>[] additionalEdges;
	private Edge[][] edges;
	private ParentChild deepestVertex;
	private Boolean useOriginalGraphWithoutRemovalList;
	private Graph g;

	public SixTollis(int realities, double modulus, boolean wrapAround,
			GraphPlotter plotter) {
		super("GDA_SIX_TOLLIS", new Parameter[] {
				new IntParameter("REALITIES", realities),
				new DoubleParameter("MODULUS", modulus),
				new BooleanParameter("WRAPAROUND", wrapAround) });
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.graphPlotter = plotter;
	}

	public GraphDrawingAbstract clone() {
		return new SixTollis(realities, modulus, wrapAround, graphPlotter);
	}

	@Override
	public Graph transform(Graph g) {
		useOriginalGraphWithoutRemovalList = false;

		initIDSpace(g);
		if (graphPlotter != null)
			graphPlotter.plotStartGraph(g, idSpace);

		EdgeCrossings ec = new EdgeCrossings();
		int countCrossings = -1;
		// countCrossings = ec.calculateCrossings(g.generateEdges(), idSpace,
		// true);
		// System.out.println("Crossings randomized: " + countCrossings);
		this.g = g;

		/*
		 * Phase 1
		 */
		edges = new Edge[g.getNodes().length][];
		Node tempVertex = null;
		Node currentVertex = null;
		Node randDst1, randDst2;
		Edge tempEdge;
		String tempEdgeString;
		ArrayList<Node> verticesToAdd;

		removedEdges = new HashMap<String, Edge>(g.computeNumberOfEdges());
		HashMap<String, Edge> pairEdges = null;
		removedVertices = new ArrayList<Node>();
		waveCenterVertices = new TreeSet<Node>();
		waveFrontVertices = new TreeSet<Node>();

		additionalEdges = new HashMap[g.getNodes().length];
		for (int i = 0; i < g.getNodes().length; i++) {
			additionalEdges[i] = new HashMap<String, Edge>();
		}

		vertexList = Arrays.asList(g.getNodes().clone());

		System.out
				.println("Done with all init stuff, should run following loop from 1 to "
						+ (vertexList.size() - 3));

		for (int counter = 1; counter < (vertexList.size() - 3); counter++) {
			currentVertex = getVertex();
			if (counter % (vertexList.size() / 10) == 0) {
				// System.out.println("Processing " + currentVertex +
				// " with a degree of "
				// + getEdges(currentVertex).size() + " (vertex " + counter +
				// " of " + (vertexList.size() - 3)
				// + ")");
			}
			pairEdges = getPairEdges(currentVertex);
			for (Edge singleEdge : pairEdges.values()) {
				removedEdges.put(getEdgeString(singleEdge), singleEdge);
			}

			HashMap<String, Edge> currentVertexConnections = getEdges(currentVertex);
			int currentVertexDegree = currentVertexConnections.size();
			int triangulationEdgesCount = (currentVertexDegree - 1)
					- pairEdges.size();
			int[] outgoingEdges = filterOutgoingEdges(currentVertex,
					currentVertexConnections);

			// System.out.print(currentVertex.getIndex() +
			// " has a current degree of " + currentVertexDegree + ", "
			// + pairEdges.size() + " pair edges and a need for " +
			// triangulationEdgesCount
			// + " triangulation edges - existing edges:");
			// for (Edge sE : currentVertexConnections.values()) {
			// System.out.print(" " + sE);
			// }
			// System.out.println();

			int firstCounter = 0;
			int secondCounter = 1;
			while (triangulationEdgesCount > 0) {
				randDst1 = g.getNode(outgoingEdges[firstCounter]);
				randDst2 = g.getNode(outgoingEdges[secondCounter]);
				if (!randDst1.equals(randDst2)
						&& !removedVertices.contains(randDst1)
						&& !removedVertices.contains(randDst2)) {

					// System.out.println("rand1: " + randDst1.getIndex() +
					// "; rand2: " + randDst2.getIndex());
					// System.out.print("Outgoing edges for r1:");
					// for (int i : filterOutgoingEdges(randDst1,
					// getEdges(randDst1))) {
					// System.out.print(" " + i);
					// }
					// System.out.println("");

					if (!connected(randDst1, randDst2)) {
						tempEdge = new Edge(Math.min(randDst1.getIndex(),
								randDst2.getIndex()), Math.max(
								randDst1.getIndex(), randDst2.getIndex()));
						tempEdgeString = getEdgeString(tempEdge);
						if (!additionalEdges[randDst1.getIndex()]
								.containsKey(tempEdgeString)
								&& !additionalEdges[randDst2.getIndex()]
										.containsKey(tempEdgeString)) {
							// System.out.println("Adding triangulation edge " +
							// tempEdge);
							additionalEdges[randDst1.getIndex()].put(
									tempEdgeString, tempEdge);
							additionalEdges[randDst2.getIndex()].put(
									tempEdgeString, tempEdge);
							triangulationEdgesCount--;
						}
					} else {
						// System.out.println("Vertex " + randDst1.getIndex() +
						// " is already connected to "
						// + randDst2.getIndex());
					}
				}

				secondCounter++;
				if (secondCounter == currentVertexDegree) {
					firstCounter++;
					secondCounter = firstCounter + 1;
				}

				if (firstCounter == (currentVertexDegree - 1)
						&& triangulationEdgesCount > 0)
					throw new GDTransformationException(
							"Could not find anymore pair edges for "
									+ currentVertex.getIndex());
			}

			/*
			 * Keep track of wave front and wave center vertices!
			 */

			verticesToAdd = new ArrayList<Node>();
			for (Edge i : getEdges(currentVertex).values()) {
				int otherEnd;
				if (i.getDst() == currentVertex.getIndex()) {
					otherEnd = i.getSrc();
				} else {
					otherEnd = i.getDst();
				}
				tempVertex = g.getNode(otherEnd);
				if (removedVertices.contains(tempVertex)) {
					continue;
				}
				verticesToAdd.add(tempVertex);
			}
			Collections.shuffle(verticesToAdd);
			waveFrontVertices = new TreeSet<Node>(verticesToAdd);
			Collections.shuffle(verticesToAdd);
			waveCenterVertices.addAll(verticesToAdd);

			removedVertices.add(currentVertex);
			// System.out.println("Adding " + currentVertex.getIndex() +
			// " to removedVertices, now containing "
			// + removedVertices.size() + " vertices");
		}

		LinkedList<Node> orderedVertices = orderVertices();
		placeVertices(orderedVertices);

		/*
		 * To avoid memory leaks: remove stuff that is not needed anymore
		 */
		waveCenterVertices = null;
		waveFrontVertices = null;
		removedEdges = null;
		removedVertices = null;

		// countCrossings = ec.calculateCrossings(g.generateEdges(), idSpace,
		// true);
		// System.out.println("Crossings after phase 1: " + countCrossings);
		if (graphPlotter != null)
			graphPlotter.plot(g, idSpace, graphPlotter.getBasename()
					+ "-afterPhase1");

		// System.out.println("Done with phase 1 of S/T");
		reduceCrossingsBySwapping(g);

		writeIDSpace(g);

		if (graphPlotter != null)
			graphPlotter.plotFinalGraph(g, idSpace);

		// countCrossings = ec.calculateCrossings(g.generateEdges(), idSpace,
		// true);
		// System.out.println("Final crossings: " + countCrossings);

		return g;
	}

	private void placeVertices(LinkedList<Node> orderedVertices) {
		Node lastVertex = null;
		double lastPos = 0;
		double posDiff = modulus / partitions.length;

		/*
		 * Create new RingIdentifiers in the order that was computed...
		 */
		RingIdentifier[] ids = new RingIdentifier[g.getNodes().length];
		for (Node n : orderedVertices) {
			ids[n.getIndex()] = new RingIdentifier(lastPos,
					idSpace.isWrapAround());
			// System.out.println("Place " + n.getIndex() + " at " + lastPos);
			lastPos += posDiff;
		}

		/*
		 * ...and assign these ids to the partitions
		 */
		lastVertex = orderedVertices.getLast();
		for (Node n : orderedVertices) {
			partitions[n.getIndex()] = new RingPartition(
					ids[lastVertex.getIndex()], ids[n.getIndex()]);
			lastVertex = n;
		}
	}

	private LinkedList<Node> orderVertices() {
		/*
		 * Compute the longest path in a spanning tree created by DFS
		 */
		// System.out.println("Starting computation of longest path");
		LinkedList<Node> longestPath = longestPath();
		// System.out.println("Longest path contains " + longestPath.size() +
		// " vertices, total number: "
		// + vertexList.size());
		// System.out.println("Characteristics for these vertices:");
		// int counter = 0;
		// int sum = 0;
		// int[] degrees = new int[longestPath.size()];
		//
		// for ( Node n: longestPath) {
		// degrees[counter++] = n.getOutDegree();
		// sum += n.getOutDegree();
		// }
		// System.out.println("Avg degree: " + ( (double)sum / counter) +
		// ", median degree: " + degrees[degrees.length/2]);

		/*
		 * Check which vertices still need to be placed, as they do not lie on
		 * the longestPath
		 */
		ArrayList<Node> todoList = new ArrayList<Node>();
		todoList.addAll(vertexList);
		todoList.removeAll(longestPath);

		Node neighbor, singleVertex;
		int errors = 0;
		int modCounter = 0;
		while (!todoList.isEmpty()) {
			int neighborPosition = -1;

			/*
			 * We will walk through the todoList with a counter: there might be
			 * vertices that can not be placed yet, as they do have only
			 * connections to other not yet connected vertices
			 */
			singleVertex = todoList.get(modCounter % todoList.size());

			int[] outgoingEdges = singleVertex.getOutgoingEdges();
			if (outgoingEdges.length == 0) {
				/*
				 * Current vertex is not connected, so place it anywhere
				 */
				neighborPosition = rand.nextInt(longestPath.size());
			} else if (outgoingEdges.length == 1
					&& todoList.contains(g.getNode(outgoingEdges[0]))) {
				/*
				 * Current vertex has only one connection, and the vertex on the
				 * other end is also in todoList, so also place this one
				 * anywhere to ensure that all vertices get placed - phase 2
				 * will do the rest
				 */
				neighborPosition = rand.nextInt(longestPath.size());
			} else {
				/*
				 * Current vertex has more than one connection (or one
				 * connection to a connected vertex), so let's check them
				 */
				for (int singleNeighbor : outgoingEdges) {
					neighbor = g.getNode(singleNeighbor);
					neighborPosition = longestPath.indexOf(neighbor);
					if (neighborPosition > -1) {
						/*
						 * We found a neighbor that is contained in the list of
						 * connected vertices - stop searching
						 */
						break;
					}
				}
			}
			if (neighborPosition == -1) {
				/*
				 * The current vertex does not yet have any connection to the
				 * longest path, so place it at a random position
				 */
				neighborPosition = rand.nextInt(longestPath.size());
			}

			/*
			 * As a possible position for this vertex is found: remove it from
			 * the todoList and place it in the longestPath. Following elements
			 * get shifted to the right
			 */
			todoList.remove(singleVertex);
			longestPath.add(neighborPosition, singleVertex);
		}
		return longestPath;
	}

	private ArrayList<Edge> getAllEdges(Node n) {
		ArrayList<Edge> vertexEdges = new ArrayList<Edge>();
		if (edges[n.getIndex()] == null) {
			edges[n.getIndex()] = n.generateAllEdges();
		}

		for (Edge e : edges[n.getIndex()])
			vertexEdges.add(e);
		if (!useOriginalGraphWithoutRemovalList) {
			vertexEdges.addAll(additionalEdges[n.getIndex()].values());
		}
		return vertexEdges;
	}

	private HashMap<String, Edge> getEdges(Node n) {
		HashMap<String, Edge> edges = new HashMap<String, Edge>(n.getDegree());

		for (Edge i : getAllEdges(n)) {
			if (!useOriginalGraphWithoutRemovalList
					&& (removedVertices.contains(g.getNode(i.getDst())) || removedVertices
							.contains(g.getNode(i.getSrc())))) {
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

	private LinkedList<Integer> findLongestPath(SpanningTree tree, int source,
			int comingFrom) {
		LinkedList<Integer> connections = new LinkedList<Integer>();
		for (int singleSrc : tree.getChildren(source)) {
			if (singleSrc == source)
				continue;
			connections.add(singleSrc);
		}
		connections.add(tree.getParent(source));
		connections.removeFirstOccurrence(source);
		connections.removeFirstOccurrence(comingFrom);

		if (connections.size() == 0) {
			connections.add(source);
			return connections;
		}
		LinkedList<Integer> longestPath, tempPath;
		longestPath = new LinkedList<Integer>();
		for (Integer singleConnection : connections) {
			if (singleConnection == null) {
				/*
				 * This is the roots parent!
				 */
				continue;
			}
			tempPath = findLongestPath(tree, singleConnection, source);
			if (tempPath.size() > longestPath.size()) {
				longestPath = tempPath;
			}
		}
		longestPath.add(source);
		return longestPath;
	}

	private LinkedList<Node> longestPath() {
		LinkedList<Node> result = new LinkedList<Node>();

		useOriginalGraphWithoutRemovalList = true;

		int startIndex = 0;
		Node start;
		do {
			start = removedVertices.get(startIndex++);
		} while (start.getOutDegree() == 0);

		deepestVertex = new ParentChild(-1, start.getIndex(), -1);
		// System.out.println("Starting DFS at " + start);

		HashMap<Integer, ParentChild> parentChildMap = new HashMap<Integer, ParentChild>(
				vertexList.size());
		dfs(start, deepestVertex, parentChildMap);
		ArrayList<ParentChild> parentChildList = new ArrayList<ParentChild>();
		parentChildList.addAll(parentChildMap.values());

		SpanningTree tree = new SpanningTree(g, parentChildList);

		LinkedList<Integer> resultInTree = findLongestPath(tree,
				deepestVertex.getChild(), -1);
		for (Integer tempVertex : resultInTree) {
			result.add(g.getNode(tempVertex));
		}
		return result;
	}

	private void dfs(Node n, ParentChild root,
			HashMap<Integer, ParentChild> visited) {
		int otherEnd;

		if (visited.containsKey(n.getIndex())) {
			return;
		}

		ParentChild current = new ParentChild(root.getChild(), n.getIndex(),
				root.getDepth() + 1);
		visited.put(n.getIndex(), current);
		if (current.getDepth() > deepestVertex.getDepth()) {
			deepestVertex = current;
		}

		for (Edge mEdge : getEdges(n).values()) {
			if (mEdge.getDst() == n.getIndex()) {
				otherEnd = mEdge.getSrc();
			} else {
				otherEnd = mEdge.getDst();
			}
			Node mVertex = g.getNode(otherEnd);
			dfs(mVertex, current, visited);
		}
	}

	/**
	 * @return
	 */
	private Node getVertex() {
		/*
		 * Retrieve any wave front vertex...
		 */
		int vDegree, tempVDegree;
		Node result = null;

		vDegree = Integer.MAX_VALUE;
		if (waveFrontVertices != null) {
			for (Node tempVertex : waveFrontVertices) {
				if (!removedVertices.contains(tempVertex)) {
					tempVDegree = getEdges(tempVertex).size();
					if (tempVDegree < vDegree) {
						result = tempVertex;
						vDegree = tempVDegree;
					}
				}
			}
			if (result != null) {
				waveFrontVertices.remove(result);
				return result;
			}
		}
		/*
		 * ...or a wave center vertex...
		 */
		vDegree = Integer.MAX_VALUE;
		if (waveCenterVertices != null) {
			for (Node tempVertex : waveCenterVertices) {
				if (!removedVertices.contains(tempVertex)) {
					tempVDegree = getEdges(tempVertex).size();
					if (tempVDegree < vDegree) {
						result = tempVertex;
						vDegree = tempVDegree;
					}
				}
			}
			if (result != null) {
				waveCenterVertices.remove(result);
				return result;
			}
		}
		/*
		 * ...or any lowest degree vertex
		 */
		resortVertexlist();
		for (Node tempVertex : vertexList) {
			if (!removedVertices.contains(tempVertex)) {
				return tempVertex;
			}
		}
		throw new GDTransformationException("No vertex left");
	}

	private void resortVertexlist() {
		if (removedVertices.isEmpty() && removedEdges.isEmpty()) {
			Collections.sort(vertexList);
			return;
		}
		// System.out.println("Resorting vertexList as already " +
		// removedVertices.size() + " vertices (of a total of "
		// + vertexList.size() + ") and " + removedEdges.size() +
		// " edges were removed");

		/*
		 * We may not delete vertices from the vertex list, as orderVertices
		 * needs this later. So: as we are only interested in the vertices with
		 * low degree, set the (internal) degree of removed vertices to the
		 * total number of vertices
		 */
		int vDegree;
		int highestDegree = vertexList.size();
		ArrayList<Node>[] tempVertexList = new ArrayList[highestDegree + 1];
		for (int i = 0; i <= highestDegree; i++) {
			tempVertexList[i] = new ArrayList<Node>();
		}

		for (Node v : vertexList) {
			if (removedVertices.contains(v)) {
				vDegree = highestDegree;
			} else {
				vDegree = getEdges(v).size();
			}
			tempVertexList[vDegree].add(v);
		}

		List<Node> newVertexList = new ArrayList<Node>(vertexList.size());
		for (int i = 0; i <= highestDegree; i++) {
			Collections.shuffle(tempVertexList[i]);
			newVertexList.addAll(tempVertexList[i]);
		}
		vertexList = newVertexList;
	}

	private HashMap<String, Edge> getPairEdges(Node n) {
		HashMap<String, Edge> result = new HashMap<String, Edge>();
		Node tempInnerVertex;
		Edge tempEdge;
		int otherOuterEnd, otherInnerEnd;

		// System.out.println("\n\nCalling getPairEdges for vertex " +
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
			// otherOuterEnd + " is the other vertex");
			HashMap<String, Edge> allInnerEdges = getEdges(g
					.getNode(otherOuterEnd));
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
				tempInnerVertex = g.getNode(otherInnerEnd);
				if (connected(n, tempInnerVertex)) {
					tempEdge = new Edge(Math.min(otherInnerEnd, otherOuterEnd),
							Math.max(otherInnerEnd, otherOuterEnd));
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

	private String getEdgeString(Edge e) {
		return Math.min(e.getSrc(), e.getDst()) + "->"
				+ Math.max(e.getSrc(), e.getDst());
	}
}
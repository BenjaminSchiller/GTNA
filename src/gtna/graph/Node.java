/*
 * ===========================================================
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
 * NodeImpl.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Node implements Comparable<Node> {
	private Graph graph;

	private int index;

	private int[] incomingEdges;

	private int[] outgoingEdges;

	public Node(int index, Graph graph) {
		this.index = index;
		this.graph = graph;
		this.incomingEdges = new int[] {};
		this.outgoingEdges = new int[] {};
	}

	public Node(int index, Graph graph, int incomingEdges, int outgoingEdges) {
		this.index = index;
		this.graph = graph;
		this.incomingEdges = new int[incomingEdges];
		this.outgoingEdges = new int[outgoingEdges];
	}

	public Node(int index, Graph graph, int[] incomingEdges, int[] outgoingEdges) {
		this.index = index;
		this.graph = graph;
		this.incomingEdges = incomingEdges;
		this.outgoingEdges = outgoingEdges;
	}

	public static Node[] init(int n, Graph graph) {
		Node[] nodes = new Node[n];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(i, graph);
		}
		return nodes;
	}

	public String toString() {
		return this.index + "  (" + this.incomingEdges.length + " / "
				+ this.outgoingEdges.length + ")";
	}

	public int getInDegree() {
		return this.incomingEdges.length;
	}

	public int getOutDegree() {
		return this.outgoingEdges.length;
	}

	public int getDegree() {
		return this.incomingEdges.length + this.outgoingEdges.length;
	}

	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return this.graph;
	}

	/**
	 * @param graph
	 *            the graph to set
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the incomingEdges
	 */
	public int[] getIncomingEdges() {
		return this.incomingEdges;
	}

	/**
	 * @param incomingEdges
	 *            the incomingEdges to set
	 */
	public void setIncomingEdges(int[] incomingEdges) {
		this.incomingEdges = incomingEdges;
	}

	/**
	 * @return the outgoingEdges
	 */
	public int[] getOutgoingEdges() {
		return this.outgoingEdges;
	}

	/**
	 * @param outgoingEdges
	 *            the outgoingEdges to set
	 */
	public void setOutgoingEdges(int[] outgoingEdges) {
		this.outgoingEdges = outgoingEdges;
	}

	public Edge[] generateAllEdges() {
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		for ( int dst: getOutgoingEdges() ) {
			edgeList.add( new Edge(this.index, dst) );
		}
		for ( int src: getIncomingEdges() ) {
			edgeList.add( new Edge(src, this.index) );
		}
		Edge[] arrayEdgeList = new Edge[edgeList.size()];
		for ( int i = 0; i < arrayEdgeList.length; i++ ) {
			arrayEdgeList[i] = edgeList.get(i);
		}
		return arrayEdgeList;
	}
	
	public boolean isConnectedTo(Node n) {
		for (int src: getIncomingEdges() ) {
			if ( src == n.getIndex() ) return true;
		}
		for (int dst: getOutgoingEdges() ) {
			if ( dst == n.getIndex() ) return true;
		}		
		return false;
	}

	/**
	 * @return
	 */
	public Integer[] generateOutgoingEdgesByDegree() {
		int[] edges = getOutgoingEdges();
		Integer[] integerEdges = new Integer[edges.length];
		for ( int i = 0; i < edges.length; i++) integerEdges[i] = edges[i];
		Arrays.sort(integerEdges, new DescendingDegreeComparator(graph));
		return integerEdges;
	}
	
	public int compareTo(Node n2) {
		if (this.getDegree() == n2.getDegree())
			return 0;
		else if (this.getDegree() > n2.getDegree())
			return 1;
		else
			return -1;
	}	
	
	private class DescendingDegreeComparator implements Comparator<Integer> {
		private Graph g;

		public DescendingDegreeComparator(Graph g) {
			this.g = g;
		}

		public int compare(Integer n, Integer m) {
			int nDegree = g.getNode(n).getDegree();
			int mDegree = g.getNode(m).getDegree();
			if (nDegree < mDegree)
				return 1;
			else if (nDegree == mDegree)
				return 0;
			else
				return -1;
		}

	}	
	
	// TODO hasOut, hasIn, addIn, addOut, removeIn, removeOut
}

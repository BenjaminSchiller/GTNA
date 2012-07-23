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
 * Edges.java
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
import java.util.HashMap;

public class Edges {
	private Node[] nodes;

	private ArrayList<Edge> edges;

	private HashMap<String, Edge> map;

	private int[] inDegree;

	private int[] outDegree;

	public Edges(Node[] nodes, int edges) {
		this.nodes = nodes;
		this.edges = new ArrayList<Edge>(edges);
		this.map = new HashMap<String, Edge>();
		this.inDegree = new int[this.nodes.length];
		this.outDegree = new int[this.nodes.length];
	}

	public Edges(Node[] nodes, Edge[] edges) {
		this(nodes, edges.length);
		for (Edge edge : edges) {
			this.add(edge);
		}
	}

	public boolean contains(int src, int dst) {
		return this.map.containsKey(Edge.toString(src, dst));
	}

	public int size() {
		return this.edges.size();
	}

	private boolean add(Edge edge) {
		if (this.map.containsKey(edge.toString())) {
			return false;
		}
		if (edge.getSrc() == edge.getDst()) {
			return false;
		}
		this.edges.add(edge);
		this.map.put(edge.toString(), edge);
		this.inDegree[edge.getDst()]++;
		this.outDegree[edge.getSrc()]++;
		return true;
	}

	public boolean add(int src, int dst) {
		if (this.map.containsKey(Edge.toString(src, dst))) {
			return false;
		}
		if (src == dst) {
			return false;
		}
		Edge edge = new Edge(src, dst);
		this.edges.add(edge);
		this.map.put(edge.toString(), edge);
		this.inDegree[edge.getDst()]++;
		this.outDegree[edge.getSrc()]++;
		return true;
	}

	public void fill() {
		for (int i = 0; i < this.nodes.length; i++) {
			this.nodes[i].setIncomingEdges(new int[this.inDegree[i]]);
			this.nodes[i].setOutgoingEdges(new int[this.outDegree[i]]);
		}
		int[] inIndex = new int[this.nodes.length];
		int[] outIndex = new int[this.nodes.length];
		for (Edge e : this.edges) {
			int srcIndex = e.getSrc();
			int dstIndex = e.getDst();
			Node src = this.nodes[srcIndex];
			Node dst = this.nodes[dstIndex];
			dst.getIncomingEdges()[inIndex[dstIndex]] = srcIndex;
			src.getOutgoingEdges()[outIndex[srcIndex]] = dstIndex;
			inIndex[dstIndex]++;
			outIndex[srcIndex]++;
		}
	}

	/**
	 * @return the edges
	 */
	public ArrayList<Edge> getEdges() {
		return this.edges;
	}
}

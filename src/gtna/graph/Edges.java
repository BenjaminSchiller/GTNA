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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
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

	private int[] in;

	private int[] out;

	public Edges(Node[] nodes, int edges) {
		this.nodes = nodes;
		this.edges = new ArrayList<Edge>(edges);
		this.map = new HashMap<String, Edge>();
		this.in = new int[this.nodes.length];
		this.out = new int[this.nodes.length];
	}
	
	public boolean contains(int index1, int index2){
		return this.map.containsKey(Edge.toString(index1, index2));
	}
	
	public boolean contains(Edge edge){
		return this.map.containsKey(edge.toString());
	}
	
	public int size(){
		return this.edges.size();
	}
	
	public boolean add(NodeImpl src, NodeImpl dst){
		return this.add(new Edge(src, dst));
	}

	public boolean add(Edge edge) {
		if (this.map.containsKey(edge.toString())) {
			return false;
		}
		this.edges.add(edge);
		this.map.put(edge.toString(), edge);
		this.in[edge.dst.index()]++;
		this.out[edge.src.index()]++;
		return true;
	}
	
	public boolean add(Edge[] edges){
		for(int i=0; i<edges.length; i++){
			this.add(edges[i]);
		}
		return true;
	}

	public void fill() {
		for (int i = 0; i < this.nodes.length; i++) {
			this.nodes[i].setIn(new NodeImpl[this.in[i]]);
			this.nodes[i].setOut(new NodeImpl[this.out[i]]);
		}
		int[] inIndex = new int[this.nodes.length];
		int[] outIndex = new int[this.nodes.length];
		for (int i = 0; i < this.edges.size(); i++) {
			Node src = this.edges.get(i).src;
			Node dst = this.edges.get(i).dst;
			src.out()[outIndex[src.index()]] = dst;
			dst.in()[inIndex[dst.index()]] = src;
			inIndex[dst.index()]++;
			outIndex[src.index()]++;
		}
	}
	
	public int outDegree(Node node){
		return this.out[node.index()];
	}
	
	public int inDegree(Node node){
		return this.in[node.index()];
	}
}

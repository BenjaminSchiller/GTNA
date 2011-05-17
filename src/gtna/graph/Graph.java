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
 * Graph.java
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

import gtna.data.Value;
import gtna.util.Timer;

import java.util.HashMap;

public class Graph {
	public String name;

	public NodeImpl[] nodes;

	public int edges;

	public double avgDegree;

	public int maxDegree;

	public int maxInDegree;

	public int maxOutDegree;

	public int minDegree;

	public int minInDegree;

	public int minOutDegree;

	public Timer timer;

	public Graph(String name, NodeImpl[] nodes, Timer timer) {
		this.name = name;
		this.nodes = nodes;
		this.computeDegrees();
		this.timer = timer;
	}

	public void computeDegrees() {
		this.edges = 0;
		this.minDegree = Integer.MAX_VALUE;
		this.minInDegree = Integer.MAX_VALUE;
		this.minOutDegree = Integer.MAX_VALUE;
		for (int i = 0; i < this.nodes.length; i++) {
			int in = nodes[i].in().length;
			int out = nodes[i].out().length;
			int degree = in + out;
			this.edges += out;
			this.avgDegree += degree;
			// DEGREE
			if (degree < this.minDegree) {
				this.minDegree = degree;
			}
			if (degree > this.maxDegree) {
				this.maxDegree = degree;
			}
			// IN-DEGREE
			if (in < this.minInDegree) {
				this.minInDegree = in;
			}
			if (in > this.maxInDegree) {
				this.maxInDegree = in;
			}
			// OUT-DEGREE
			if (out < this.minOutDegree) {
				this.minOutDegree = out;
			}
			if (out > this.maxOutDegree) {
				this.maxOutDegree = out;
			}
		}
		this.avgDegree /= this.nodes.length;
	}

	public Value[] values() {
		Value nodes = new Value("NODES", this.nodes.length);
		Value edges = new Value("EDGES", this.edges);
		Value davg = new Value("D_AVG", this.avgDegree);
		Value dmax = new Value("D_MAX", this.maxDegree);
		Value dmin = new Value("D_MIN", this.minDegree);
		Value dimax = new Value("DI_MAX", this.maxInDegree);
		Value dimin = new Value("DI_MIN", this.minInDegree);
		Value domax = new Value("DO_MAX", this.maxOutDegree);
		Value domin = new Value("DO_MIN", this.minOutDegree);
		return new Value[] { nodes, edges, davg, dmax, dmin, dimax, dimin,
				domax, domin };
	}

	private Edge[] Edges;

	public Edge[] edges() {
		if (this.Edges == null) {
			this.computeEdges();
		}
		return this.Edges;
	}

	public void computeEdges() {
		this.Edges = new Edge[this.edges];
		int index = 0;
		for (int i = 0; i < this.nodes.length; i++) {
			for (int j = 0; j < this.nodes[i].out().length; j++) {
				this.Edges[index++] = new Edge(this.nodes[i],
						this.nodes[i].out()[j]);
			}
		}
	}

	private HashMap<String, Edge> map;

	public HashMap<String, Edge> map() {
		if (this.map == null) {
			this.map = new HashMap<String, Edge>();
			for (int i = 0; i < this.nodes.length; i++) {
				for (int j = 0; j < this.nodes[i].out().length; j++) {
					Edge edge = new Edge(this.nodes[i], this.nodes[i].out()[j]);
					this.map.put(edge.toString(), edge);
				}
			}
		}
		return this.map;
	}

	public String toString() {
		return "|N| = " + this.nodes.length + ", |E| = " + this.edges;
	}
}

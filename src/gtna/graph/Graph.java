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

import gtna.util.Timer;

import java.util.HashMap;

public class Graph {
	private String name;

	private Node[] nodes;

	private Timer timer;

	private HashMap<String, GraphProperty> properties;

	/**
	 * initializes a graph with the given name an starts the timer
	 * 
	 * @param name
	 *            name of the graph
	 */
	public Graph(String name) {
		this.name = name;
		this.timer = new Timer();
		this.properties = new HashMap<String, GraphProperty>();
	}

	public String toString() {
		return this.name + " (" + this.nodes.length + ")";
	}

	public void addProperty(String key, GraphProperty property) {
		this.properties.put(key, property);
	}

	public boolean hasProperty(String key) {
		return this.properties.containsKey(key);
	}

	public GraphProperty getProperty(String key) {
		return this.properties.get(key);
	}

	public Edge[] generateEdges() {
		int E = 0;
		for (Node n : this.nodes) {
			E += n.getDegree();
		}
		Edge[] edges = new Edge[E];
		int index = 0;
		for (Node n : this.nodes) {
			for (int out : n.getOutgoingEdges()) {
				edges[index++] = new Edge(n.getIndex(), out);
			}
			for (int in : n.getIncomingEdges()) {
				edges[index++] = new Edge(in, n.getIndex());
			}
		}
		return edges;
	}

	public Edge[] generateIncomingEdges() {
		int E = 0;
		for (Node n : this.nodes) {
			E += n.getInDegree();
		}
		Edge[] edges = new Edge[E];
		int index = 0;
		for (Node n : this.nodes) {
			for (int in : n.getIncomingEdges()) {
				edges[index++] = new Edge(in, n.getIndex());
			}
		}
		return edges;
	}

	public Edge[] generateOutgoingEdges() {
		int E = 0;
		for (Node n : this.nodes) {
			E += n.getOutDegree();
		}
		Edge[] edges = new Edge[E];
		int index = 0;
		for (Node n : this.nodes) {
			for (int out : n.getOutgoingEdges()) {
				edges[index++] = new Edge(n.getIndex(), out);
			}
		}
		return edges;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the timer
	 */
	public Timer getTimer() {
		return this.timer;
	}

	/**
	 * @param timer
	 *            the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * @return the nodes
	 */
	public Node[] getNodes() {
		return this.nodes;
	}

	/**
	 * sets the nodes and ends the timer starte during graph initialization
	 * 
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
		this.timer.end();
	}
}

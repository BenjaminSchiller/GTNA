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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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

	public GraphProperty[] getProperties(String type) {
		ArrayList<GraphProperty> list = new ArrayList<GraphProperty>();
		int index = 0;
		while (this.hasProperty(type + "_" + index)) {
			list.add(this.getProperty(type + "_" + index));
			index++;
		}
		GraphProperty[] array = new GraphProperty[list.size()];
		for(int i=0; i<array.length; i++){
			array[i] = (GraphProperty) list.get(i);
		}
		return array;
	}

	public String getNextKey(String type) {
		int index = 0;
		while (this.hasProperty(type + "_" + index)) {
			index++;
		}
		return type + "_" + index;
	}

	public HashMap<String, GraphProperty> getProperties() {
		return this.properties;
	}

	public int computeNumberOfEdges() {
		int E = 0;
		for (Node n : this.nodes) {
			E += n.getOutDegree();
		}
		return E;
	}

	public Edge[] generateEdges() {
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

    /**
     * @param nodeIndex
     * @return the node with index nodeIndex
     */
    public Node getNode(int nodeIndex){
        return nodes[nodeIndex];
    }
    
    public Graph getSpanningTree( Node root ) {
    	Graph result = new Graph(this.name + "-ST");
    	Node[] resultNodes = new Node[this.nodes.length];
    	int[] edges;
    	Node tempNodeFromList, tempNewNode;
    	  	
    	LinkedList<Node> todoList = new LinkedList<Node>();
    	LinkedList<Integer> handledNodes = new LinkedList<Integer>();
    	LinkedList<Integer> linkedNodes = new LinkedList<Integer>();
    	LinkedList<Integer> newEdges;
    	
    	todoList.add(root);
    	linkedNodes.add(root.getIndex());
    	while ( !todoList.isEmpty() ) {
    		tempNodeFromList = todoList.pop();
    		if ( handledNodes.contains(tempNodeFromList.getIndex()) ) {
    				/*
    				 * Although the current node was fetched from the 
    				 * todoList, we will continue: this node was already
    				 * processed
    				 */
    			continue;
    		}
    		
    		edges = tempNodeFromList.getOutgoingEdges();
			newEdges = new LinkedList<Integer>();
    		for ( int e: edges ) {
    			if (  !linkedNodes.contains(e) ) {
    					/*
    					 * Node e has not been linked yet, so
    					 * add it to the todoList (to handle it soon)
    					 * and add the edge from the current node to e
    					 * to the list of edges 
    					 */
    				todoList.add( this.nodes[e] );
    				newEdges.add(e);
    				linkedNodes.add(e);
    			}
    		}
    		
    		tempNewNode = new Node(tempNodeFromList.getIndex(), result);
    		edges = new int[newEdges.size()];
    		for ( int i = 0; i < newEdges.size(); i++ ) {
    			edges[i] = newEdges.get(i);
    		}
    		tempNewNode.setOutgoingEdges(edges);
    		resultNodes[tempNodeFromList.getIndex()] = tempNewNode;
    		handledNodes.add(tempNodeFromList.getIndex());
    	}
    	result.setNodes(resultNodes);
    	return result;
    }
}

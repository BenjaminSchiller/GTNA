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
 * GeneralGraphMethods.java
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
package gtna.metrics.roles;

import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @deprecated
 */
public class GeneralGraphMethods {
	private static HashSet<Integer>[] oneHop = null;

	// Count the number of links from a node to a given group
	public static int NLinksToGroup(int node, Group group, Graph g, int[] nlist) {
		int counter = 0;
		Node[] in = g.nodes[node].in();
		for (int i = 0; i < in.length; i++) {
			if (nlist[in[i].index()] == group.getLabel()
					&& in[i].index() != node) {
				counter++;
			}
		}
		Node[] out = g.nodes[node].out();
		for (int i = 0; i < out.length; i++) {
			if (nlist[out[i].index()] == group.getLabel()
					&& out[i].index() != node) {
				counter++;
			}
		}
		return counter;
	}

	// returns a list of neighbors of the given node
	@SuppressWarnings("unchecked")
	public static HashSet<Integer> neighborsOfNode(int node, Graph g) {
		if(oneHop == null)
			oneHop = new HashSet[g.nodes.length];
		if (oneHop[node] == null) {
			HashSet<Integer> neighbors = new HashSet<Integer>();
			Node[] in = g.nodes[node].in();
			for (int i = 0; i < in.length; i++) {				
				neighbors.add(in[i].index());
			}
			Node[] out = g.nodes[node].out();
			for (int i = 0; i < out.length; i++) {
				neighbors.add(out[i].index());
			}
			oneHop[node] = neighbors;
		}
		return (HashSet<Integer>) oneHop[node].clone();
	}



	// calculate the degree of given node
	public static int degreeOfNode(int node, Graph g) {
		return g.nodes[node].in().length + g.nodes[node].out().length;
	}

	// calculate the in- and out-degree of given node within the subgraph
	public static int[] directedDegreeOfNodeWithinMotif(int node,
			ArrayList<Integer> nodeList, Graph g) {
		int inDegree = 0;
		int outDegree = 0;
		Node[] in = g.nodes[node].in();
		for (int i = 0; i < in.length; i++) {
			for (int j = 0; j < nodeList.size(); j++) {
				if (in[i].index() == nodeList.get(j)) {
					inDegree++;
				}
			}
		}
		Node[] out = g.nodes[node].out();
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < nodeList.size(); j++) {
				if (out[i].index() == nodeList.get(j)) {
					outDegree++;
				}
			}
		}
		int[] degree = new int[2];
		degree[0] = inDegree;
		degree[1] = outDegree;
		return degree;
	}

}

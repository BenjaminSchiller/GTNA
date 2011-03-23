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

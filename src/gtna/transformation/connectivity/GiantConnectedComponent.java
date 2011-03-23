package gtna.transformation.connectivity;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;
import gtna.util.Util;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Extracts the giant connected component of the given graph, i.e., the largest
 * subset of nodes that form a connected cluster.
 * 
 * @author benni
 * 
 */
public class GiantConnectedComponent extends TransformationImpl implements
		Transformation {
	public GiantConnectedComponent() {
		super("GIANT_CONNECTED_COMPONENT", new String[] {}, new String[] {});
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		NodeImpl[] lc = this.largestCluster(g);
		for (int i = 0; i < lc.length; i++) {
			lc[i].setIndex(i);
		}
		g.nodes = lc;
		g.computeDegrees();
		g.computeEdges();
		return g;
	}

	private ArrayList<ArrayList<NodeImpl>> clusters(Graph g) {
		ArrayList<ArrayList<NodeImpl>> clusters = new ArrayList<ArrayList<NodeImpl>>();
		boolean[] seen = new boolean[g.nodes.length];
		for (int i = 0; i < seen.length; i++) {
			if (!seen[i]) {
				ArrayList<NodeImpl> current = new ArrayList<NodeImpl>();
				clusters.add(current);
				Stack<NodeImpl> stack = new Stack<NodeImpl>();
				stack.push(g.nodes[i]);
				seen[i] = true;
				current.add(g.nodes[i]);
				while (!stack.empty()) {
					NodeImpl n = stack.pop();
					NodeImpl[] out = n.out();
					NodeImpl[] in = n.in();
					for (int j = 0; j < out.length; j++) {
						if (!seen[out[j].index()]) {
							seen[out[j].index()] = true;
							stack.push(out[j]);
							current.add(out[j]);
						}
					}
					for (int j = 0; j < in.length; j++) {
						if (!seen[in[j].index()]) {
							seen[in[j].index()] = true;
							stack.push(in[j]);
							current.add(in[j]);
						}
					}
				}
			}
		}
		return clusters;
	}

	private NodeImpl[] largestCluster(Graph g) {
		ArrayList<ArrayList<NodeImpl>> clusters = this.clusters(g);
		ArrayList<NodeImpl> largest = clusters.get(0);
		for (int i = 1; i < clusters.size(); i++) {
			if (clusters.get(i).size() > largest.size()) {
				largest = clusters.get(i);
			}
		}
		return Util.toArray(largest);
	}
}

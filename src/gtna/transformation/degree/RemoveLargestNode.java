package gtna.transformation.degree;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

/**
 * Transforms the given graph by removing the node with the highest degree.
 * 
 * @author benni
 * 
 */
public class RemoveLargestNode extends TransformationImpl implements
		Transformation {
	public RemoveLargestNode() {
		super("REMOVE_LARGEST_NODE", new String[] {}, new String[] {});
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		NodeImpl largest = g.nodes[0];
		int degree = largest.out().length + largest.in().length;
		for (int i = 1; i < g.nodes.length; i++) {
			if (g.nodes[i].out().length + g.nodes[i].in().length > degree) {
				degree = g.nodes[i].out().length + g.nodes[i].in().length;
				largest = g.nodes[i];
			}
		}
		for (int i = 0; i < largest.out().length; i++) {
			largest.out()[i].removeIn(largest);
		}
		for (int i = 0; i < largest.in().length; i++) {
			largest.in()[i].removeOut(largest);
		}
		NodeImpl[] nodes = new NodeImpl[g.nodes.length - 1];
		for (int i = 0; i < largest.index(); i++) {
			nodes[i] = g.nodes[i];
			nodes[i].setIndex(i);
		}
		for (int i = largest.index() + 1; i < g.nodes.length; i++) {
			nodes[i - 1] = g.nodes[i];
			nodes[i - 1].setIndex(i - 1);
		}
		g.nodes = nodes;
		g.computeDegrees();
		g.computeEdges();
		return g;
	}

}

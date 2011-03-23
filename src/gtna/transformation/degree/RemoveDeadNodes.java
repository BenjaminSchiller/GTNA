package gtna.transformation.degree;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.Hashtable;

/**
 * Transforms the given graph by removing all nodes with no incoming of outgoing
 * edges.
 * 
 * @author benni
 * 
 */
public class RemoveDeadNodes extends TransformationImpl implements
		Transformation {
	public RemoveDeadNodes() {
		super("REMOVE_DEAD_NODES", new String[] {}, new String[] {});
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		Hashtable<Integer, Integer> ids = new Hashtable<Integer, Integer>(
				g.nodes.length);
		int index = 0;
		for (int i = 0; i < g.nodes.length; i++) {
			if (g.nodes[i].out().length > 0 || g.nodes[i].in().length > 0) {
				ids.put(i, index++);
			}
		}
		NodeImpl[] nodes = new NodeImpl[index];
		for (int i = 0; i < g.nodes.length; i++) {
			if (ids.containsKey(i)) {
				nodes[ids.get(i)] = g.nodes[i];
				nodes[ids.get(i)].setIndex(ids.get(i));
			}
		}
		g.nodes = nodes;
		g.computeDegrees();
		g.computeEdges();
		return g;
	}

}

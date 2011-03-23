package gtna.transformation.edges;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.GraphProperties;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

/**
 * Transforms the given graph into a bidirectional version by adding every
 * reverse edge that is not already contained, i.e., for every edge (a,b) add
 * edge (b,a) if it does not exist.
 * 
 * @author benni
 * 
 */
public class Bidirectional extends TransformationImpl implements Transformation {
	public Bidirectional() {
		super("BIDIRECTIONAL", new String[] {}, new String[] {});
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		if (GraphProperties.bidirectional(g)) {
			return g;
		}
		Edges edgeSet = new Edges(g.nodes, g.edges * 2);
		Edge[] edges = g.edges();
		edgeSet.add(edges);
		for (int i = 0; i < edges.length; i++) {
			Edge back = new Edge(edges[i].dst, edges[i].src);
			if (!edgeSet.contains(back)) {
				edgeSet.add(back);
			}
		}
		edgeSet.fill();
		g.computeDegrees();
		g.computeEdges();
		return g;
	}

}

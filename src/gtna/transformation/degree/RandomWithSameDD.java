package gtna.transformation.degree;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.ArrayList;
import java.util.Random;

/**
 * Transforms the given graph by generating a random graph with the exact same
 * degree distribution. If the parameter bidirectional is true, only
 * bidirectional links are created, otherwise unidirectional links are
 * generated. Note that this transformation creates new node objects. Therefore,
 * all information except for the node adjacency is lost.
 * 
 * @author benni
 * 
 */
public class RandomWithSameDD extends TransformationImpl implements
		Transformation {
	private boolean bidirectional;

	public RandomWithSameDD(boolean bidirectional) {
		super("RANDOM_WITH_SAME_DD", new String[] { "BIDIRECTIONAL" },
				new String[] { "" + bidirectional });
		this.bidirectional = bidirectional;
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		NodeImpl[] nodes = NodeImpl.init(g.nodes.length);
		Edges edges = this.random(src(g), dst(g), nodes, g.nodes);
		while (edges == null) {
			edges = this.random(src(g), dst(g), nodes, g.nodes);
		}
		edges.fill();
		return new Graph(g.name, nodes, g.timer);
	}

	private Edges random(ArrayList<Integer> src, ArrayList<Integer> dst,
			NodeImpl[] nodes, NodeImpl[] orig) {
		Edges edges = new Edges(nodes, src.size());
		Random rand = new Random(System.currentTimeMillis());
		int counter = 0;
		while (src.size() > 0) {
			if (counter > 1000) {
				return null;
			}
			int si = rand.nextInt(src.size());
			int di = rand.nextInt(dst.size());
			int s = src.get(si);
			int d = dst.get(di);
			if (s == d || edges.contains(s, d)) {
				counter++;
				continue;
			}
			edges.add(nodes[s], nodes[d]);
			src.remove((Integer) s);
			dst.remove((Integer) d);
			if (this.bidirectional) {
				edges.add(nodes[d], nodes[s]);
				src.remove((Integer) d);
				dst.remove((Integer) s);
			}
		}
		return edges;
	}

	private ArrayList<Integer> src(Graph g) {
		ArrayList<Integer> src = new ArrayList<Integer>(g.edges);
		for (int i = 0; i < g.nodes.length; i++) {
			for (int j = 0; j < g.nodes[i].out().length; j++) {
				src.add(i);
			}
		}
		return src;
	}

	private ArrayList<Integer> dst(Graph g) {
		ArrayList<Integer> dst = new ArrayList<Integer>(g.edges);
		for (int i = 0; i < g.nodes.length; i++) {
			for (int j = 0; j < g.nodes[i].in().length; j++) {
				dst.add(i);
			}
		}
		return dst;
	}
}

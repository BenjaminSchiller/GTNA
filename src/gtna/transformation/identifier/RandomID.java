package gtna.transformation.identifier;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.routing.node.GridNodeEuclidean;
import gtna.routing.node.GridNodeManhattan;
import gtna.routing.node.RingNode;
import gtna.routing.node.RingNodeMultiR;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Transforms the given graph by assigning each node a random ID of the given
 * type. THe number of dimensions is another parameter used by some of these
 * types. Note that this transformation creates new node objects. Therefore, all
 * information except for the node adjacency is lost.
 * 
 * @author benni
 * 
 */
public class RandomID extends TransformationImpl implements Transformation {
	public static final String RING_NODE = "R";

	public static final String RING_NODE_MULTI_R = "RmR";

	public static String GRID_NODE_EUCLIDEAN = "GE";

	public static String GRID_NODE_MANHATTAN = "GM";

	private int parameter;

	private String type;

	public RandomID(String type, int parameter) {
		super("RANDOM_ID", new String[] { "TYPE", "PARAMETER" }, new String[] {
				type, "" + parameter });
		this.parameter = parameter;
		this.type = type;
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		NodeImpl[] nodes = new NodeImpl[g.nodes.length];
		Set<String> ids = new HashSet<String>();
		Random rand = new Random(System.currentTimeMillis());
		if (this.type.equals(RING_NODE)) {
			for (int i = 0; i < nodes.length; i++) {
				double id = this.uniqueNumber(ids, rand);
				nodes[i] = new RingNode(i, id);
			}
		} else if (this.type.equals(RING_NODE_MULTI_R)) {
			double[][] IDs = new double[nodes.length][this.parameter];
			for(int i=0; i<this.parameter; i++){
				ids = new HashSet<String>();
				for(int j=0; j<nodes.length; j++){
					IDs[j][i] = this.uniqueNumber(ids, rand);
				}
			}
			for(int i=0; i<nodes.length; i++){
				nodes[i] = new RingNodeMultiR(i, IDs[i]);
			}
		} else if (this.type.equals(GRID_NODE_EUCLIDEAN)) {
			for (int i = 0; i < nodes.length; i++) {
				double[] id = this.uniqueID(ids, rand);
				nodes[i] = new GridNodeEuclidean(i, id);
			}
		} else if (this.type.equals(GRID_NODE_MANHATTAN)) {
			for (int i = 0; i < nodes.length; i++) {
				double[] id = this.uniqueID(ids, rand);
				nodes[i] = new GridNodeManhattan(i, id);
			}
		}
		Edge[] original = g.edges();
		Edges edges = new Edges(nodes, original.length);
		for (int i = 0; i < original.length; i++) {
			NodeImpl src = nodes[original[i].src.index()];
			NodeImpl dst = nodes[original[i].dst.index()];
			edges.add(src, dst);
		}
		edges.fill();
		return new Graph(g.name, nodes, g.timer);
	}

	private double uniqueNumber(Set<String> ids, Random rand) {
		double n = rand.nextDouble();
		while (ids.contains(n + "")) {
			n = rand.nextDouble();
		}
		ids.add(n + "");
		return n;
	}

	private double[] uniqueID(Set<String> ids, Random rand) {
		double[] id = this.randomID(rand);
		while (ids.contains(this.idToString(id))) {
			id = this.randomID(rand);
		}
		ids.add(this.idToString(id));
		return id;
	}

	private double[] randomID(Random rand) {
		double[] id = new double[this.parameter];
		for (int i = 0; i < id.length; i++) {
			id[i] = rand.nextDouble();
		}
		return id;
	}

	private String idToString(double[] id) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < id.length; i++) {
			buff.append(";" + id[i]);
		}
		return buff.toString();
	}
}

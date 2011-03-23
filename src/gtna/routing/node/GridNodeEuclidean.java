package gtna.routing.node;

import gtna.graph.NodeImpl;
import gtna.routing.node.identifier.GridIDEuclidean;
import gtna.routing.node.identifier.Identifier;

import java.util.Random;

public class GridNodeEuclidean extends NodeImpl implements IDNode {
	private GridIDEuclidean id;

	public GridNodeEuclidean(int index, double[] pos) {
		super(index);
		this.id = new GridIDEuclidean(pos);
	}

	public boolean contains(Identifier id) {
		return this.id.equals(id);
	}

	public double dist(Identifier id) {
		return this.id.dist(id);
	}

	public Identifier randomID(Random rand, NodeImpl[] nodes) {
		GridIDEuclidean id = ((GridNodeEuclidean) nodes[rand
				.nextInt(nodes.length)]).id;
		while (this.contains(id)) {
			id = ((GridNodeEuclidean) nodes[rand.nextInt(nodes.length)]).id;
		}
		return id;
	}

	public double dist(IDNode node) {
		return ((GridNodeEuclidean) node).id.dist(this.id);
	}
}

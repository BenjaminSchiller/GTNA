package gtna.routing.node;

import gtna.graph.NodeImpl;
import gtna.routing.node.identifier.GridIDManhattan;
import gtna.routing.node.identifier.Identifier;

import java.util.Random;

public class GridNodeManhattan extends NodeImpl implements IDNode {
	private GridIDManhattan id;

	public GridNodeManhattan(int index, double[] pos) {
		super(index);
		this.id = new GridIDManhattan(pos);
	}

	public boolean contains(Identifier id) {
		return this.id.equals(id);
	}

	public double dist(Identifier id) {
		return this.id.dist(id);
	}

	public Identifier randomID(Random rand, NodeImpl[] nodes) {
		GridIDManhattan id = ((GridNodeManhattan) nodes[rand
				.nextInt(nodes.length)]).id;
		while (this.contains(id)) {
			id = ((GridNodeManhattan) nodes[rand.nextInt(nodes.length)]).id;
		}
		return id;
	}

	public double dist(IDNode node) {
		return ((GridNodeManhattan) node).id.dist(this.id);
	}
}

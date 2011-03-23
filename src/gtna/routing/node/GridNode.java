package gtna.routing.node;

import gtna.graph.NodeImpl;
import gtna.routing.node.identifier.GridID;
import gtna.routing.node.identifier.Identifier;

import java.util.Random;

/**
 * @deprecated
 */
public class GridNode extends NodeImpl implements IDNode {
	public GridID id;

	public GridNode(int index, GridID id) {
		super(index);
		this.id = id;
	}

	public boolean contains(Identifier id) {
		return this.id.equals((GridID) id);
	}

	public double dist(Identifier id) {
		return this.id.dist((GridID) id);
	}

	public Identifier randomID(Random rand, NodeImpl[] nodes) {
		return ((GridNode) nodes[rand.nextInt(nodes.length)]).id;
	}

	public double dist(IDNode node) {
		return ((GridNode) node).id.dist(this.id);
	}

	public String toString() {
		return this.id.toString();
	}
}

package gtna.routing.node;

import gtna.graph.NodeImpl;
import gtna.routing.node.identifier.Identifier;
import gtna.routing.node.identifier.RingID;

import java.util.HashSet;
import java.util.Random;


public class RingNode extends NodeImpl implements RegistrationNode {
	private RingID id;

	private HashSet<String> register;

	public RingNode(int index, double pos) {
		super(index);
		this.id = new RingID(pos);
		this.register = new HashSet<String>();
	}

	public void register(Identifier id) {
		if (!this.id.equals(id)) {
			this.register.add(((RingID) id).toString());
		}
	}

	public int registeredItems() {
		return this.register.size();
	}

	public boolean contains(Identifier id) {
		return this.id.equals(id)
				|| this.register.contains(((RingID) id).toString());
	}

	public double dist(Identifier id) {
		return this.id.dist(id);
	}

	public double dist(IDNode node) {
		return this.id.dist(((RingNode) node).id);
	}

	public Identifier randomID(Random rand, NodeImpl[] nodes) {
		int index = rand.nextInt(nodes.length);
		while (index == this.index()) {
			index = rand.nextInt(nodes.length);
		}
		return ((RingNode) nodes[index]).id;
	}

	public RingID getID() {
		return this.id;
	}

	public void setID(RingID id) {
		this.id = id;
	}

	public String toString() {
		return this.index() + " @ " + this.id.toString();
	}
}

package gtna.routing.node;

import gtna.graph.NodeImpl;
import gtna.routing.node.identifier.Identifier;
import gtna.routing.node.identifier.RingIDMultiR;

import java.util.HashSet;
import java.util.Random;

public class RingNodeMultiR extends NodeImpl implements RegistrationNode {
	private RingIDMultiR[] ids;

	private HashSet<String> register;

	public RingNodeMultiR(int index, double[] pos) {
		super(index);
		this.ids = new RingIDMultiR[pos.length];
		for (int i = 0; i < pos.length; i++) {
			this.ids[i] = new RingIDMultiR(pos[i], i);
		}
		this.register = new HashSet<String>();
	}

	public void register(Identifier id) {
		if (!this.ids[((RingIDMultiR) id).reality].equals(id)) {
			this.register.add(id.toString());
		}
	}

	public int registeredItems() {
		return this.register.size();
	}

	public boolean contains(Identifier id) {
		return this.ids[((RingIDMultiR) id).reality].equals(id)
				|| this.register.contains(id.toString());
	}

	public double dist(Identifier id) {
		return this.ids[((RingIDMultiR) id).reality].dist(id);
	}

	public double dist(IDNode node) {
		return this.ids[0].dist(((RingNodeMultiR) node).ids[0]);
	}

	public Identifier randomID(Random rand, NodeImpl[] nodes) {
		return this.randomID(rand, nodes, rand.nextInt(this.ids.length));
	}

	public Identifier randomID(Random rand, NodeImpl[] nodes, int reality) {
		int index = rand.nextInt(nodes.length);
		while (this.index() == index) {
			index = rand.nextInt(nodes.length);
		}
		return ((RingNodeMultiR) nodes[index]).ids[reality];
	}

	public RingIDMultiR[] getIDs() {
		return this.ids;
	}

	public void setID(RingIDMultiR id) {
		this.ids[id.reality] = id;
	}

	public String toString() {
		return this.index() + " @ " + this.ids[0].toString();
	}

}

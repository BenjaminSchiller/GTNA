package gtna.graph;

import gtna.util.Config;

public class NodeImpl implements Node {
	private NodeImpl[] out = null;

	private NodeImpl[] in = null;

	private int index = 0;

	public NodeImpl(int index) {
		this.index = index;
	}

	public int route(Node to) {
		return Config.getInt("ROUTING_LENGTH_DEFAULT");
	}

	public double[] routeProg(Node to) {
		return null;
	}

	public void init(NodeImpl[] in, NodeImpl[] out) {
		this.in = in;
		this.out = out;
	}

	public static NodeImpl[] init(int numberOfNodes) {
		NodeImpl[] nodes = new NodeImpl[numberOfNodes];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new NodeImpl(i);
            nodes[i].init(new NodeImpl[0], new NodeImpl[0]);
		}
		return nodes;
	}

	public NodeImpl[] in() {
		return this.in;
	}

	public NodeImpl[] out() {
		return this.out;
	}

	public int index() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setIn(NodeImpl[] in) {
		this.in = in;
	}

	public void setOut(NodeImpl[] out) {
		this.out = out;
	}

	public boolean hasIn(NodeImpl n) {
		for (int i = 0; i < this.in.length; i++) {
			if (this.in[i].index == n.index) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOut(NodeImpl n) {
		for (int i = 0; i < this.out.length; i++) {
			if (this.out[i].index == n.index) {
				return true;
			}
		}
		return false;
	}

	public void addIn(NodeImpl n) {
		NodeImpl[] temp = new NodeImpl[this.in.length + 1];
		for (int i = 0; i < this.in.length; i++) {
			temp[i] = this.in[i];
		}
		temp[this.in.length] = n;
		this.in = temp;
	}

	public void addOut(NodeImpl n) {
		NodeImpl[] temp = new NodeImpl[this.out.length + 1];
		for (int i = 0; i < this.out.length; i++) {
			temp[i] = this.out[i];
		}
		temp[this.out.length] = n;
		this.out = temp;
	}

	public void removeIn(NodeImpl n) {
		NodeImpl[] temp = new NodeImpl[this.in.length - 1];
		int index = 0;
		boolean second = false;
		for (int i = 0; i < this.in.length; i++) {
			if (this.in[i].index != n.index() || second) {
				temp[index++] = this.in[i];
			} else {
				second = true;
			}
		}
		this.in = temp;
	}

	// public void removeIn(NodeImpl n) {
	// NodeImpl[] temp = new NodeImpl[this.in.length - 1];
	// int index = 0;
	// for (int i = 0; i < this.in.length; i++) {
	// if (this.in[i].index != n.index) {
	// temp[index++] = this.in[i];
	// }
	// }
	// this.in = temp;
	// }

	public void removeOut(NodeImpl n) {
		NodeImpl[] temp = new NodeImpl[this.out.length - 1];
		int index = 0;
		boolean second = false;
		for (int i = 0; i < this.out.length; i++) {
			if (this.out[i].index != n.index || second) {
				temp[index++] = this.out[i];
			} else {
				second = true;
			}
		}
		this.out = temp;
	}

	// public void removeOut(NodeImpl n) {
	// NodeImpl[] temp = new NodeImpl[this.out.length - 1];
	// int index = 0;
	// for (int i = 0; i < this.out.length; i++) {
	// if (this.out[i].index != n.index) {
	// temp[index++] = this.out[i];
	// }
	// }
	// this.out = temp;
	// }

	public String toString() {
		return this.index + "  " + this.in.length + " / " + this.out.length;
	}
}

/*
 * ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 * 
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 * 
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 * 
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * NodeImpl.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.graph;

import gtna.util.Config;

public class Node {
	private Node[] out = null;

	private Node[] in = null;

	private int index = 0;

	public Node(int index) {
		this.index = index;
	}

	public int route(Node to) {
		return Config.getInt("ROUTING_LENGTH_DEFAULT");
	}

	public double[] routeProg(Node to) {
		return null;
	}

	public void init(Node[] in, Node[] out) {
		this.in = in;
		this.out = out;
	}

	public static Node[] init(int numberOfNodes) {
		Node[] nodes = new Node[numberOfNodes];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(i);
			nodes[i].init(new Node[0], new Node[0]);
		}
		return nodes;
	}

	public Node[] in() {
		return this.in;
	}

	public Node[] out() {
		return this.out;
	}

	public int index() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setIn(Node[] in) {
		this.in = in;
	}

	public void setOut(Node[] out) {
		this.out = out;
	}

	public boolean hasIn(Node n) {
		for (int i = 0; i < this.in.length; i++) {
			if (this.in[i].index == n.index) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOut(Node n) {
		for (int i = 0; i < this.out.length; i++) {
			if (this.out[i].index == n.index) {
				return true;
			}
		}
		return false;
	}

	public void addIn(Node n) {
		Node[] temp = new Node[this.in.length + 1];
		for (int i = 0; i < this.in.length; i++) {
			temp[i] = this.in[i];
		}
		temp[this.in.length] = n;
		this.in = temp;
	}

	public void addOut(Node n) {
		Node[] temp = new Node[this.out.length + 1];
		for (int i = 0; i < this.out.length; i++) {
			temp[i] = this.out[i];
		}
		temp[this.out.length] = n;
		this.out = temp;
	}

	public void removeIn(Node n) {
		Node[] temp = new Node[this.in.length - 1];
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

	public void removeOut(Node n) {
		Node[] temp = new Node[this.out.length - 1];
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

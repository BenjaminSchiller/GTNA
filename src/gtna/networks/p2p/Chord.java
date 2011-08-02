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
 * Chord.java
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
package gtna.networks.p2p;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routingOld.node.IDNode;
import gtna.routingOld.node.identifier.Identifier;
import gtna.transformation.Transformation;
import gtna.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Implements the network generator for the P2P network topology of Chord, a
 * P2P-based lookup protocol introduced by Stoica et al. in their paper
 * "Chord: A scalable peer-to-peer lookup service for internet applications"
 * from 2001.
 * 
 * Implemented parameters are the identifier space size, number of successors
 * each node maintains a connection to, and the distance parameter which
 * influences distance computation and thereby the number of finger in the
 * finger table of each node.
 * 
 * @author benni
 * 
 */
public class Chord extends NetworkImpl implements Network {
	private int BITS_PER_KEY = 32;

	private int SUCCESSOR_LIST_SIZE = 16;

	private int DISTANCE = 1;

	public Chord(int nodes, int BITS_PER_KEY, int SUCCESSOR_LIST_SIZE,
			int DISTANCE, RoutingAlgorithm ra, Transformation[] t) {
		super("CHORD", nodes, new String[] { "BITS_PER_KEY",
				"SUCCESSOR_LIST_SIZE", "DISTANCE" }, new String[] {
				"" + BITS_PER_KEY, "" + SUCCESSOR_LIST_SIZE, "" + DISTANCE },
				ra, t);
		this.BITS_PER_KEY = BITS_PER_KEY;
		this.SUCCESSOR_LIST_SIZE = SUCCESSOR_LIST_SIZE;
		this.DISTANCE = DISTANCE;
	}

	public static Chord[] get(int[] n, int m, int r, int d,
			RoutingAlgorithm ra, Transformation[] t) {
		Chord[] nw = new Chord[n.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new Chord(n[i], m, r, d, ra, t);
		}
		return nw;
	}

	public static Chord[] get(int n, int m, int[] r, int d,
			RoutingAlgorithm ra, Transformation[] t) {
		Chord[] nw = new Chord[r.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new Chord(n, m, r[i], d, ra, t);
		}
		return nw;
	}

	public static Chord[] get(int n, int m, int r, int[] d,
			RoutingAlgorithm ra, Transformation[] t) {
		Chord[] nw = new Chord[d.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new Chord(n, m, r, d[i], ra, t);
		}
		return nw;
	}

	public static Chord[][] get(int[] n, int m, int[] r, int d,
			RoutingAlgorithm ra, Transformation[] t) {
		Chord[][] nw = new Chord[r.length][n.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = Chord.get(n, m, r[i], d, ra, t);
		}
		return nw;
	}

	public static Chord[][] get(int[] n, int m, int r, int[] d,
			RoutingAlgorithm ra, Transformation[] t) {
		Chord[][] nw = new Chord[d.length][n.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = Chord.get(n, m, r, d[i], ra, t);
		}
		return nw;
	}

	public static Chord[][] getXY(int n, int m, int[] r, int[] d,
			RoutingAlgorithm ra, Transformation[] t) {
		Chord[][] nw = new Chord[d.length][r.length];
		for (int i = 0; i < d.length; i++) {
			nw[i] = Chord.get(n, m, r, d[i], ra, t);
		}
		return nw;
	}

	public static Chord[][] getYX(int n, int m, int[] r, int[] d,
			RoutingAlgorithm ra, Transformation[] t) {
		Chord[][] nw = new Chord[r.length][d.length];
		for (int i = 0; i < r.length; i++) {
			nw[i] = Chord.get(n, m, r[i], d, ra, t);
		}
		return nw;
	}

	public Graph generate() {
		Graph graph = new Graph(this.description());
		Random rand = new Random(System.currentTimeMillis());
		long mod = (long) Math.pow(2, this.BITS_PER_KEY);

		ChordNode[] nodes = new ChordNode[this.nodes()];
		long[] ids = this.getUniqueIDs(this.nodes(), rand, mod);
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new ChordNode(i, graph, ids[i], mod);
		}
		for (int i = 0; i < nodes.length; i++) {
			int indexPredecessor = (i + nodes.length - 1) % nodes.length;
			int indexSuccessor = (i + 1) % nodes.length;
			nodes[i].predecessor = nodes[indexPredecessor];
			nodes[i].successor = nodes[indexSuccessor];
		}
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].fillSuccessorList(this.SUCCESSOR_LIST_SIZE);
		}
		int fingers = this.computeNumberOfFingers(mod, this.DISTANCE);
		double base = 1.0 + 1.0 / (double) this.DISTANCE;
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].fillFingerTable(fingers, mod, base, nodes);
			nodes[i].fillFingers();
		}

		Edges edges = new Edges(nodes, nodes.length
				* (fingers + this.SUCCESSOR_LIST_SIZE + 1));
		for (int i = 0; i < nodes.length; i++) {
			edges.add(i, nodes[i].predecessor.getIndex());
			edges.add(i, nodes[i].successor.getIndex());
			for (int j = 0; j < nodes[i].successorList.length; j++) {
				edges.add(i, nodes[i].successorList[j].getIndex());
			}
			for (int j = 0; j < nodes[i].fingers.length; j++) {
				edges.add(i, nodes[i].fingers[j].getIndex());
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	private long[] getUniqueIDs(int number, Random rand, long mod) {
		ArrayList<Long> list = new ArrayList<Long>(number);
		while (list.size() < number) {
			long next = Math.abs(rand.nextLong() % mod);
			if (!list.contains(next)) {
				list.add(next);
			}
		}
		long[] array = Util.toLongArray(list);
		Arrays.sort(array);
		return array;
	}

	private int computeNumberOfFingers(long mod, int d) {
		int counter = 1;
		double base = 1.0 + 1.0 / (double) d;
		while (Math.pow(base, counter) < mod) {
			counter++;
		}
		counter--;
		return counter;
	}

	private static ChordNode findSuccessor(long id, ChordNode[] nodes, long mod) {
		int index = (int) ((double) nodes.length * (double) id / (double) mod);
		int inc = id > nodes[index].id.id ? 1 : -1;
		while (!nodes[index].isSuccessor(id)) {
			index = (index + inc + nodes.length) % nodes.length;
		}
		return nodes[index];
	}

	private class ChordNode extends Node implements IDNode {
		private ChordID id;

		private ChordNode predecessor;

		private ChordNode successor;

		private ChordNode[] successorList;

		private ChordNode[] fingerTable;

		private ChordNode[] fingers;

		private ChordNode(int index, Graph graph, long id, long mod) {
			super(index, graph);
			this.id = new ChordID(id, mod);
		}

		public int route(Node n2) {
			long id = ((ChordNode) n2).id.id;
			if (id == this.id.id) {
				return 0;
			}
			return this.route(id);
		}

		private int route(long id) {
			return this.find_successor(id);
		}

		// // ask node n to find id's successor
		// n.find_successor(id)
		// n' = find_predecessor(id);
		// return n'.successor;
		private int find_successor(long id) {
			return this.find_predecessor(id) + 1;
		}

		// // ask node n to find id's predecessor
		// n.find_predecessor(id)
		// n' = n;
		// while(id $\notin$ (n', n'.successor])
		// n' = n'.closest_preceding_finder(id);
		// return n';
		private int find_predecessor(long id) {
			int counter = 0;
			ChordNode n_ = this;
			while (!n_.isPredecessor(id)) {
				n_ = n_.closest_preceding_finger(id);
				if (n_.isSuccessor(id)) {
					break;
				}
				counter++;
				if (n_.predecessor.isSuccessor(id)) {
					break;
				}
				if (n_.successor.isPredecessor(id)) {
					break;
				}
				// if(n_.id == 13151402){
				// ChordNode n = n_;
				// System.out.println(n);
				// System.out.println(n.predecessor + " => X => " +
				// n.successor);
				// for(int i=0; i<n.fingers.length; i++){
				// System.out.println("   f[" + i + "] = " + n.fingers[i]);
				// }
				// for(int i=0; i<n.fingerTable.length; i++){
				// System.out.println("   ft[" + i + "] = " + n.fingerTable[i]);
				// }
				// break;
				// }
			}
			return counter;
		}

		// // return closest finger preceding id
		// n.closest_preceding_finger(id)
		// for i = m downto 1
		// if(finger[i].node in (n, id))
		// return finger[i].node;
		// return n;
		private ChordNode closest_preceding_finger(long id) {
			for (int i = 0; i < this.successorList.length; i++) {
				if (this.successorList[i].isPredecessor(id)) {
					return this.successorList[i];
				}
			}
			for (int i = this.fingers.length - 1; i >= 0; i--) {
				if (this.fingers[i].isSuccessor(id)) {
					return this.fingers[i];
				}
				if (this.id.id < this.fingers[i].id.id) {
					if (this.fingers[i].id.id < id || id < this.id.id) {
						return this.fingers[i];
					}
				} else {
					if (this.fingers[i].id.id < id && id < this.id.id) {
						return this.fingers[i];
					}
				}
			}
			return this;
		}

		private void fillSuccessorList(int successors) {
			this.successorList = new ChordNode[successors];
			if (successors == 0) {
				return;
			}
			this.successorList[0] = this.successor;
			for (int i = 1; i < this.successorList.length; i++) {
				this.successorList[i] = this.successorList[i - 1].successor;
			}
		}

		private void fillFingerTable(int fingers, long mod, double base,
				ChordNode[] nodes) {
			this.fingerTable = new ChordNode[fingers];
			for (int i = 0; i < this.fingerTable.length; i++) {
				double distance = Math.pow(base, i + 1);
				long fingerID = (this.id.id + (long) distance) % mod;
				ChordNode finger = Chord.findSuccessor(fingerID, nodes, mod);
				this.fingerTable[i] = finger;
			}
		}

		private void fillFingers() {
			ArrayList<ChordNode> list = new ArrayList<ChordNode>(
					this.fingerTable.length);
			for (int i = 0; i < this.fingerTable.length; i++) {
				if (!list.contains(this.fingerTable[i])
						&& this.fingerTable[i].id != this.id) {
					list.add(this.fingerTable[i]);
				}
			}
			this.fingers = new ChordNode[list.size()];
			for (int i = 0; i < list.size(); i++) {
				this.fingers[i] = list.get(i);
			}
		}

		private boolean isSuccessor(long id) {
			if (this.predecessor.id.id < this.id.id) {
				return this.predecessor.id.id < id && id <= this.id.id;
			} else {
				return this.predecessor.id.id < id || id <= this.id.id;
			}
		}

		private boolean isPredecessor(long id) {
			if (this.id.id < this.successor.id.id) {
				return this.id.id < id && id <= this.successor.id.id;
			} else {
				return this.id.id < id || id <= this.successor.id.id;
			}
		}

		public String toString() {
			return this.getIndex() + " / " + this.id;
		}

		public boolean contains(Identifier id) {
			return this.isSuccessor(((ChordID) id).id);
		}

		public double dist(Identifier id) {
			if (this.contains(id)) {
				return 0.0;
			}
			return this.id.dist((ChordID) id);
		}

		public Identifier randomID(Random rand, Node[] nodes) {
			return new ChordID(Math.abs(rand.nextLong() % this.id.mod),
					this.id.mod);
		}

		public double dist(IDNode node) {
			return ((ChordNode) node).id.dist(this.id);
		}
	}

	private class ChordID implements Identifier {
		private long id;

		private long mod;

		private ChordID(long id, long mod) {
			this.id = id;
			this.mod = mod;
		}

		public double dist(Identifier id) {
			if (((ChordID) id).id > this.id) {
				return ((ChordID) id).id - this.id;
			} else {
				return ((ChordID) id).id + this.mod - this.id;
			}
		}

		public boolean equals(Identifier id) {
			return ((ChordID) id).id == this.id;
		}

		public String toString() {
			return this.id + "";
		}
	}
}

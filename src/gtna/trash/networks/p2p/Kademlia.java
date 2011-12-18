///*
// * ===========================================================
// * GTNA : Graph-Theoretic Network Analyzer
// * ===========================================================
// * 
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors
// * 
// * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
// * 
// * GTNA is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * 
// * GTNA is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// * 
// * ---------------------------------------
// * Kademlia.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// * 
// * Original Author: Benjamin Schiller;
// * Contributors:    -;
// * 
// * Changes since 2011-05-17
// * ---------------------------------------
// */
//package gtna.trash.networks.p2p;
//
//import gtna.graph.Edges;
//import gtna.graph.Graph;
//import gtna.graph.Node;
//import gtna.networks.Network;
//import gtna.networks.NetworkImpl;
//import gtna.routing.RoutingAlgorithm;
//import gtna.transformation.Transformation;
//import gtna.util.Timer;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Random;
//
///**
// * Implements a network generator for the P2P network Kademlia introduced by
// * Maymounkov and Mazieres in their paper
// * "Kademlia: A peer-to-peer information system based on the xor metric" (2002).
// * 
// * The implemented parameters are identifier space size, bucket size, alpha, and
// * number of random lookups.
// * 
// * @author benni
// * 
// */
//public class Kademlia extends NetworkImpl implements Network {
//	private int BITS_PER_ID = 32;
//
//	private int BUCKET_SIZE = 3;
//
//	private int ALPHA = 3;
//
//	private int RANDOM_LOOKUPS = 50;
//
//	public static Kademlia[] get(int[] n, int b, int k, int a, int r,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Kademlia[] nw = new Kademlia[n.length];
//		for (int i = 0; i < n.length; i++) {
//			nw[i] = new Kademlia(n[i], b, k, a, r, ra, t);
//		}
//		return nw;
//	}
//
//	public static Kademlia[] get(int n, int[] b, int k, int a, int r,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Kademlia[] nw = new Kademlia[b.length];
//		for (int i = 0; i < b.length; i++) {
//			nw[i] = new Kademlia(n, b[i], k, a, r, ra, t);
//		}
//		return nw;
//	}
//
//	public static Kademlia[] get(int n, int b, int[] k, int a, int r,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Kademlia[] nw = new Kademlia[k.length];
//		for (int i = 0; i < k.length; i++) {
//			nw[i] = new Kademlia(n, b, k[i], a, r, ra, t);
//		}
//		return nw;
//	}
//
//	public static Kademlia[] get(int n, int b, int k, int[] a, int r,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Kademlia[] nw = new Kademlia[a.length];
//		for (int i = 0; i < a.length; i++) {
//			nw[i] = new Kademlia(n, b, k, a[i], r, ra, t);
//		}
//		return nw;
//	}
//
//	public static Kademlia[] get(int n, int b, int k, int a, int[] r,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Kademlia[] nw = new Kademlia[r.length];
//		for (int i = 0; i < r.length; i++) {
//			nw[i] = new Kademlia(n, b, k, a, r[i], ra, t);
//		}
//		return nw;
//	}
//
//	public Kademlia(int nodes, int BITS_PER_ID, int BUCKET_SIZE, int ALPHA,
//			int RANDOM_LOOKUPS, RoutingAlgorithm ra, Transformation[] t) {
//		super("KADEMLIA", nodes, new String[] { "BITS_PER_ID", "BUCKET_SIZE",
//				"ALPHA", "RANDOM_LOOKUPS" }, new String[] { "" + BITS_PER_ID,
//				"" + BUCKET_SIZE, "" + ALPHA, "" + RANDOM_LOOKUPS }, ra, t);
//		this.BITS_PER_ID = BITS_PER_ID;
//		this.BUCKET_SIZE = BUCKET_SIZE;
//		this.ALPHA = ALPHA;
//		this.RANDOM_LOOKUPS = RANDOM_LOOKUPS;
//	}
//
//	public Graph generate() {
//		Graph graph = new Graph(this.description());
//		Random rand = new Random(System.currentTimeMillis());
//
//		KademliaNode[] nodes = new KademliaNode[this.nodes()];
//		Timer t = new Timer("NODES");
//		for (int i = 0; i < nodes.length; i++) {
//			nodes[i] = new KademliaNode(ID.randomUnique(rand, this, nodes,
//					i - 1), i, graph, this);
//		}
//		t.end();
//		t = new Timer("JOIN");
//		for (int i = 1; i < nodes.length; i++) {
//			KademliaNode bootstrap = nodes[rand.nextInt(i)];
//			nodes[i].join(bootstrap, rand, this);
//		}
//		t.end();
//		t = new Timer("LOOKUP");
//		for (int j = 0; j < this.RANDOM_LOOKUPS; j++) {
//			for (int i = 0; i < nodes.length; i++) {
//				ID id = ID.random(rand, this);
//				nodes[i].lookup(id, this);
//			}
//		}
//		t.end();
//
//		Edges edges = new Edges(nodes, 100);
//		for (int i = 0; i < nodes.length; i++) {
//			for (int j = 0; j < nodes[i].buckets.length; j++) {
//				for (int k = 0; k < nodes[i].buckets[j].length; k++) {
//					if (nodes[i].buckets[j][k] != null) {
//						edges.add(i, nodes[i].buckets[j][k].getIndex());
//					}
//				}
//			}
//		}
//		edges.fill();
//		graph.setNodes(nodes);
//		return graph;
//	}
//
//	private class KademliaNode extends Node {
//		private ID id;
//
//		private KademliaNode[][] buckets;
//
//		private ArrayList<KademliaNode> list;
//
//		private KademliaNode(ID id, int index, Graph graph, Kademlia n) {
//			super(index, graph);
//			this.id = id;
//			this.buckets = new KademliaNode[n.BITS_PER_ID][n.BUCKET_SIZE];
//			this.list = new ArrayList<KademliaNode>();
//		}
//
//		private void join(KademliaNode bootstrap, Random rand, Kademlia n) {
//			// insert BOOTSTRAP into appropriate bucket
//			this.insertIntoBucket(bootstrap);
//			// lookup own ID
//			KademliaNode[] lookup = this.lookup(this.id, n);
//			this.insertIntoBucket(lookup);
//			// refresh all k-buckets further away than closest neighbor
//			for (int i = 0; i < this.buckets.length; i++) {
//				ID id = ID.randomWithMutualPrefix(rand, n, this.id, i);
//				KademliaNode[] refresh = this.lookup(id, n);
//				this.insertIntoBucket(refresh);
//			}
//		}
//
//		private KademliaNode[] lookup(ID id, Kademlia n) {
//			ArrayList<KademliaNode> queried = new ArrayList<KademliaNode>();
//			ArrayList<KademliaNode> seen = new ArrayList<KademliaNode>();
//			// get ALPHA closest nodes
//			KademliaNode[] initial = this.getClosestNodes(id, n.ALPHA);
//			// send FIND_NODE RPCs to them
//			for (int i = 0; i < initial.length; i++) {
//				KademliaNode[] found = initial[i].FIND_NODE(id, this, n);
//				if (!queried.contains(initial[i])) {
//					queried.add(initial[i]);
//				}
//				if (!seen.contains(initial[i])
//						&& !initial[i].id.equals(this.id)) {
//					seen.add(initial[i]);
//				}
//				for (int j = 0; j < found.length; j++) {
//					if (!seen.contains(found[j])
//							&& !found[j].id.equals(this.id)) {
//						seen.add(found[j]);
//					}
//				}
//			}
//			// repeat: send query to ALPHA unqueried out of k closest
//			while (true) {
//				Collections.sort(seen, new Distance(id));
//				ArrayList<KademliaNode> newQueries = new ArrayList<KademliaNode>();
//				for (int i = 0; i < Math.min(n.BUCKET_SIZE, seen.size()); i++) {
//					KademliaNode current = seen.get(i);
//					if (!queried.contains(current)
//							&& !current.id.equals(this.id)) {
//						newQueries.add(current);
//					}
//					if (newQueries.size() == n.ALPHA) {
//						break;
//					}
//				}
//				if (newQueries.size() == 0) {
//					break;
//				}
//				for (int i = 0; i < newQueries.size(); i++) {
//					KademliaNode current = newQueries.get(i);
//					KademliaNode[] found = current.FIND_NODE(id, this, n);
//					if (!queried.contains(current)
//							&& !current.id.equals(this.id)) {
//						queried.add(current);
//					}
//					for (int j = 0; j < found.length; j++) {
//						if (!seen.contains(found[j])
//								&& !found[j].id.equals(this.id)) {
//							seen.add(found[j]);
//						}
//					}
//				}
//			}
//			Collections.sort(seen, new Distance(id));
//			KademliaNode[] nodes = new KademliaNode[Math.min(seen.size(),
//					n.BUCKET_SIZE)];
//			for (int i = 0; i < nodes.length; i++) {
//				nodes[i] = seen.get(i);
//			}
//
//			for (int i = 0; i < seen.size(); i++) {
//				this.insertIntoBucket(seen.get(i));
//			}
//			return nodes;
//		}
//
//		private boolean insertIntoBucket(KademliaNode[] nodes) {
//			boolean ok = true;
//			for (int i = 0; i < nodes.length; i++) {
//				ok &= this.insertIntoBucket(nodes[i]);
//			}
//			return ok;
//		}
//
//		private boolean insertIntoBucket(KademliaNode node) {
//			if (this.list.contains(node)) {
//				return false;
//			}
//			this.list.add(node);
//
//			int prefix = this.id.prefix(node.id);
//			if (prefix < 0) {
//				return false;
//			}
//			for (int i = 0; i < this.buckets[prefix].length; i++) {
//				if (this.buckets[prefix][i] != null
//						&& this.buckets[prefix][i].id.equals(node.id)) {
//					break;
//				}
//				if (this.buckets[prefix][i] == null) {
//					this.buckets[prefix][i] = node;
//					return true;
//				}
//			}
//			return false;
//		}
//
//		private KademliaNode[] getClosestNodes(ID id, int numberOfNodes) {
//			KademliaNode[] nodes = new KademliaNode[numberOfNodes];
//			long distance = Long.MAX_VALUE / 2;
//			long lastDistance = -1;
//
//			for (int i = 0; i < nodes.length; i++) {
//				for (int a = 0; a < this.buckets.length; a++) {
//					for (int b = 0; b < this.buckets[a].length; b++) {
//						KademliaNode current = this.buckets[a][b];
//						if (current == null) {
//							break;
//						}
//						long currentDistance = current.id.distance(id);
//						if (currentDistance < distance
//								&& currentDistance > lastDistance) {
//							nodes[i] = current;
//							distance = currentDistance;
//						}
//					}
//				}
//				if (nodes[i] == null) {
//					break;
//				}
//				lastDistance = nodes[i].id.distance(id);
//				distance = Long.MAX_VALUE / 2;
//			}
//
//			if (nodes[nodes.length - 1] == null) {
//				int counter = 0;
//				for (int i = 0; i < nodes.length; i++) {
//					if (nodes[i] != null) {
//						counter++;
//					}
//				}
//				KademliaNode[] newNodes = new KademliaNode[counter];
//				for (int i = 0; i < counter; i++) {
//					if (nodes[i] != null) {
//						newNodes[i] = nodes[i];
//					}
//				}
//				return newNodes;
//			}
//
//			return nodes;
//		}
//
//		private KademliaNode[] FIND_NODE(ID id, KademliaNode caller, Kademlia n) {
//			this.insertIntoBucket(caller);
//			return this.getClosestNodes(id, n.BUCKET_SIZE);
//		}
//
//		public String toString() {
//			StringBuffer buff = new StringBuffer(this.id.toString());
//			for (int i = 0; i < this.buckets.length; i++) {
//				if (this.buckets[i][0] != null) {
//					buff.append("\n" + i + ": ");
//				}
//				for (int j = 0; j < this.buckets[i].length; j++) {
//					if (this.buckets[i][j] != null) {
//						if (j > 0) {
//							buff.append(", ");
//						}
//						buff.append(this.buckets[i][j].id.toString());
//					}
//				}
//			}
//			return buff.toString();
//		}
//
//		public int route(Node n2) {
//			return Short.MIN_VALUE;
//		}
//	}
//
//	private static class ID {
//		private boolean[] bits;
//
//		private long id;
//
//		private ID(boolean[] bits) {
//			this.bits = bits;
//			this.id = 0;
//			long pow = 1;
//			for (int i = bits.length - 1; i >= 0; i--) {
//				if (bits[i]) {
//					this.id += pow;
//				}
//				pow *= 2;
//			}
//		}
//
//		private int prefix(ID id) {
//			for (int i = 0; i < this.bits.length; i++) {
//				if (this.bits[i] != id.bits[i]) {
//					return i;
//				}
//			}
//			return -1;
//		}
//
//		private boolean equals(ID id) {
//			for (int i = 0; i < this.bits.length; i++) {
//				if (this.bits[i] != id.bits[i]) {
//					return false;
//				}
//			}
//			return true;
//		}
//
//		private long distance(ID id) {
//			return this.id ^ id.id;
//		}
//
//		private static ID randomWithMutualPrefix(Random rand, Kademlia n,
//				ID id, int prefixLength) {
//			boolean[] bits = new boolean[n.BITS_PER_ID];
//			for (int i = 0; i < prefixLength; i++) {
//				bits[i] = id.bits[i];
//			}
//			for (int i = prefixLength; i < bits.length; i++) {
//				bits[i] = rand.nextBoolean();
//			}
//			return new ID(bits);
//		}
//
//		private static ID randomUnique(Random rand, Kademlia n,
//				KademliaNode[] nodes, int index) {
//			ID id = random(rand, n);
//			for (int i = 0; i <= index; i++) {
//				if (nodes[i].id.equals(id)) {
//					return randomUnique(rand, n, nodes, index);
//				}
//			}
//			return id;
//		}
//
//		private static ID random(Random rand, Kademlia n) {
//			boolean[] bits = new boolean[n.BITS_PER_ID];
//			for (int i = 0; i < bits.length; i++) {
//				bits[i] = rand.nextBoolean();
//			}
//			return new ID(bits);
//		}
//
//		public String toString() {
//			StringBuffer buff = new StringBuffer();
//			for (int i = 0; i < this.bits.length; i++) {
//				buff.append(this.bits[i] ? 1 : 0);
//			}
//			return buff.toString();
//		}
//	}
//
//	public class Distance implements java.util.Comparator<KademliaNode> {
//		private ID id;
//
//		private Distance(ID id) {
//			this.id = id;
//		}
//
//		public int compare(KademliaNode a, KademliaNode b) {
//			long distA = this.id.distance(a.id);
//			long distB = this.id.distance(b.id);
//			if (distA < distB) {
//				return -1;
//			} else if (distA == distB) {
//				return 0;
//			} else {
//				return 1;
//			}
//		}
//	}
//}

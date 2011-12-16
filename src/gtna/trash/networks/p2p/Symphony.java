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
// * Symphony.java
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
//import gtna.trash.routing.node.IDNode;
//import gtna.trash.routing.node.identifier.Identifier;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//
///**
// * Implements a network generator for the P2P network Symphony proposed by Manku
// * et al. in their paper "Symphony: Distributed hashing in a small world"
// * (2003). it implements a ring-based lookup system using long distance links
// * drawn from a harmonic distribution.
// * 
// * The parameters are number of link distance links, number of successor links,
// * maximum number of long distance link retries, a flag for the bidirectionality
// * of links, and a maximum number of incoming links.
// * 
// * @author benni
// * 
// */
//public class Symphony extends NetworkImpl implements Network {
//	// public static String ROUTING_UNIDIRECTIONAL = "unidirectional";
//	//
//	// public static String ROUTING_BIDIRECTIONAL = "bidirectional";
//	//
//	// public static String ROUTING_LOOKAHEAD = "lookahead";
//	//
//	// public static String ROUTING_NONE = "X";
//
//	private int LONG_DISTANCE_LINKS;
//
//	private int SUCCESSOR_LINKS;
//
//	private int MAX_LONG_DISTANCE_RETRIES;
//
//	private boolean BIDIRECTIONAL;
//
//	private int MAX_INCOMMING;
//
//	public Symphony(int nodes, int LONG_DISTANCE_LINKS, int SUCCESSOR_LINKS,
//			int MAX_LONG_DISTANCE_RETRIES, boolean BIDIRECTIONAL,
//			RoutingAlgorithm ra, Transformation[] t) {
//		super("SYMPHONY", nodes,
//				new String[] { "LONG_DISTANCE_LINKS", "SUCCESSOR_LINKS",
//						"MAX_LONG_DISTANCE_RETRIES", "BIDIRECTIONAL" },
//				new String[] { "" + LONG_DISTANCE_LINKS, "" + SUCCESSOR_LINKS,
//						"" + MAX_LONG_DISTANCE_RETRIES, "" + BIDIRECTIONAL },
//				ra, t);
//		this.LONG_DISTANCE_LINKS = LONG_DISTANCE_LINKS;
//		this.SUCCESSOR_LINKS = SUCCESSOR_LINKS;
//		this.MAX_LONG_DISTANCE_RETRIES = MAX_LONG_DISTANCE_RETRIES;
//		this.MAX_INCOMMING = this.LONG_DISTANCE_LINKS * 2;
//		this.BIDIRECTIONAL = BIDIRECTIONAL;
//	}
//
//	public static Symphony[] get(int[] n, int l, int s, int r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[] nw = new Symphony[n.length];
//		for (int i = 0; i < n.length; i++) {
//			nw[i] = new Symphony(n[i], l, s, r, b, ra, t);
//		}
//		return nw;
//	}
//
//	public static Symphony[] get(int n, int[] l, int s, int r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[] nw = new Symphony[l.length];
//		for (int i = 0; i < l.length; i++) {
//			nw[i] = new Symphony(n, l[i], s, r, b, ra, t);
//		}
//		return nw;
//	}
//
//	public static Symphony[] get(int n, int l, int[] s, int r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[] nw = new Symphony[s.length];
//		for (int i = 0; i < s.length; i++) {
//			nw[i] = new Symphony(n, l, s[i], r, b, ra, t);
//		}
//		return nw;
//	}
//
//	public static Symphony[] get(int n, int l, int s, int[] r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[] nw = new Symphony[r.length];
//		for (int i = 0; i < r.length; i++) {
//			nw[i] = new Symphony(n, l, s, r[i], b, ra, t);
//		}
//		return nw;
//	}
//
//	public static Symphony[] get(int n, int l, int s, int r, boolean b,
//			RoutingAlgorithm[] ra, Transformation[] t) {
//		Symphony[] nw = new Symphony[ra.length];
//		for (int i = 0; i < ra.length; i++) {
//			nw[i] = new Symphony(n, l, s, r, b, ra[i], t);
//		}
//		return nw;
//	}
//
//	public static Symphony[][] get(int[] n, int[] l, int s, int r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[][] nw = new Symphony[l.length][n.length];
//		for (int i = 0; i < l.length; i++) {
//			for (int j = 0; j < n.length; j++) {
//				nw[i][j] = new Symphony(n[j], l[i], s, r, b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static Symphony[][] get(int[] n, int l, int[] s, int r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[][] nw = new Symphony[s.length][n.length];
//		for (int i = 0; i < s.length; i++) {
//			for (int j = 0; j < n.length; j++) {
//				nw[i][j] = new Symphony(n[j], l, s[i], r, b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static Symphony[][] get(int[] n, int l, int s, int[] r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[][] nw = new Symphony[r.length][n.length];
//		for (int i = 0; i < r.length; i++) {
//			for (int j = 0; j < n.length; j++) {
//				nw[i][j] = new Symphony(n[j], l, s, r[i], b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static Symphony[][] getXY(int n, int[] l, int[] s, int r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[][] nw = new Symphony[s.length][l.length];
//		for (int i = 0; i < s.length; i++) {
//			for (int j = 0; j < l.length; j++) {
//				nw[i][j] = new Symphony(n, l[j], s[i], r, b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static Symphony[][] getYX(int n, int[] l, int[] s, int r, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		Symphony[][] nw = new Symphony[l.length][s.length];
//		for (int i = 0; i < l.length; i++) {
//			for (int j = 0; j < s.length; j++) {
//				nw[i][j] = new Symphony(n, l[i], s[j], r, b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public Graph generate() {
//		Graph graph = new Graph(this.description());
//		double[] ids = new double[this.nodes()];
//		Random rand = new Random(System.currentTimeMillis());
//		for (int i = 0; i < this.nodes(); i++) {
//			ids[i] = rand.nextDouble();
//		}
//		Arrays.sort(ids);
//
//		SymphonyNode[] nodes = new SymphonyNode[this.nodes()];
//		Edges edges = new Edges(nodes, 100);
//		int[] incomming = new int[nodes.length];
//
//		// SUCC / PRED
//		double id = ids[0];
//		nodes[0] = new SymphonyNode(id, 0, graph);
//		for (int i = 1; i < nodes.length; i++) {
//			id = ids[i];
//			nodes[i] = new SymphonyNode(id, i, graph, nodes[i - 1]);
//			nodes[i - 1].succ = nodes[i];
//		}
//		nodes[0].pred = nodes[nodes.length - 1];
//		nodes[nodes.length - 1].succ = nodes[0];
//		for (int i = 0; i < nodes.length; i++) {
//			this.addLink(nodes[i], nodes[i].succ, edges);
//			this.addLink(nodes[i], nodes[i].pred, edges);
//		}
//		// SUCCESSOR LIST
//		for (int i = 0; i < nodes.length; i++) {
//			nodes[i].succs = new SymphonyNode[this.SUCCESSOR_LINKS];
//			if (this.SUCCESSOR_LINKS <= 0) {
//				break;
//			}
//			nodes[i].succs[0] = nodes[i].succ;
//			this.addLink(nodes[i], nodes[i].succs[0], edges);
//			for (int j = 1; j < this.SUCCESSOR_LINKS; j++) {
//				nodes[i].succs[j] = nodes[i].succs[j - 1].succ;
//				this.addLink(nodes[i], nodes[i].succs[j], edges);
//			}
//		}
//		// LONG DISTANCE LINKS
//		for (int i = 0; i < nodes.length; i++) {
//			ArrayList<SymphonyNode> links = new ArrayList<SymphonyNode>(
//					this.LONG_DISTANCE_LINKS);
//			int retries = 0;
//			for (int j = 0; j < this.LONG_DISTANCE_LINKS; j++) {
//				double key = -1;
//				SymphonyNode link = null;
//				while (true) {
//					if (retries > this.MAX_LONG_DISTANCE_RETRIES) {
//						break;
//					}
//					key = Symphony.longDistanceKey(nodes[i].id.id,
//							nodes.length, rand);
//					link = Symphony.getNode(key, nodes);
//					if (incomming[link.getIndex()] >= this.MAX_INCOMMING) {
//						retries++;
//						continue;
//					}
//					if (edges.contains(i, link.getIndex())) {
//						continue;
//					}
//					if (i == link.getIndex()) {
//						continue;
//					}
//					if (links.contains(link)) {
//						continue;
//					}
//					break;
//				}
//				if (link != null && retries <= this.MAX_LONG_DISTANCE_RETRIES
//						&& incomming[link.getIndex()] < this.MAX_INCOMMING) {
//					links.add(link);
//					incomming[link.getIndex()]++;
//				}
//			}
//			nodes[i].links = new SymphonyNode[links.size()];
//			for (int j = 0; j < links.size(); j++) {
//				nodes[i].links[j] = links.get(j);
//				this.addLink(nodes[i], nodes[i].links[j], edges);
//			}
//		}
//
//		edges.fill();
//
//		// for(int i=0; i<nodes.length; i++){
//		// SymphonyNode n = nodes[i];
//		// SymphonyNode p = n.pred;
//		// SymphonyNode s = n.succ;
//		// double middle = (p.id.id + (n.id.id - p.id.id) / 2) % 1;
//		// boolean ok = true;
//		// if(!nodes[i].contains(n.id())){
//		// System.out.println(i + ": own");
//		// ok = false;
//		// }
//		// if(nodes[i].contains(p.id())){
//		// System.out.println(i + ": pred");
//		// ok = false;
//		// }
//		// if(n.contains(s.id())){
//		// System.out.println(i + ": succ");
//		// ok = false;
//		// }
//		// if(!n.contains(new SymphonyID(middle))){
//		// System.out.println(i + ": middle");
//		// ok = false;
//		// }
//		// if(ok){
//		// System.out.println(i + ": OK");
//		// }
//		// }
//
//		graph.setNodes(nodes);
//		return graph;
//	}
//
//	private void addLink(SymphonyNode src, SymphonyNode dst, Edges edges) {
//		if (!edges.contains(src.getIndex(), dst.getIndex())) {
//			edges.add(src.getIndex(), dst.getIndex());
//		}
//		if (!this.BIDIRECTIONAL) {
//			return;
//		}
//		if (!edges.contains(dst.getIndex(), src.getIndex())) {
//			edges.add(dst.getIndex(), src.getIndex());
//		}
//	}
//
//	private static SymphonyNode getNode(double id, SymphonyNode[] nodes) {
//		int index = (int) ((double) nodes.length * id);
//		int inc = 1;
//		if (nodes[index].id.id - id > 0) {
//			inc = -1;
//		}
//		while (true) {
//			index = (index + inc + nodes.length) % nodes.length;
//			if (nodes[index].contains(id)) {
//				break;
//			}
//
//		}
//		return nodes[index];
//	}
//
//	private static double longDistanceKey(double currentID, int nodes,
//			Random rand) {
//		// SymphonyId symId = (SymphonyId) id;
//		// double x = symId.getDoubleValue() + Math.exp(Math.log((double) n) *
//		// (r.nextDouble() - 1.0));
//		// int i = (int) x;
//		// x -= i;
//		double x = rand.nextDouble();
//		if (x >= (1 / nodes)) {
//			double length = Math.exp(Math.log(nodes) * (x - 1.0));
//			// length = rand.nextDouble();
//			double key = (currentID + length) % 1.0;
//			return key;
//		} else {
//			return 0;
//		}
//	}
//
//	private static class SymphonyID implements Identifier {
//		private double id;
//
//		public SymphonyID(double id) {
//			this.id = id;
//		}
//
//		public double dist(Identifier id) {
//			double src = this.id;
//			double dest = ((SymphonyID) id).id;
//			double dist = src <= dest ? dest - src : dest + 1 - src;
//			return dist <= 0.5 ? dist : 1 - dist;
//		}
//
//		public String toString() {
//			return "" + this.id;
//		}
//
//		public boolean equals(Identifier id) {
//			return ((SymphonyID) id).id == this.id;
//		}
//	}
//
//	private static class SymphonyNode extends Node implements IDNode {
//		private SymphonyID id;
//
//		private SymphonyNode pred;
//
//		private SymphonyNode succ;
//
//		private SymphonyNode[] succs;
//
//		private SymphonyNode[] links;
//
//		private SymphonyNode(double id, int index, Graph graph) {
//			super(index, graph);
//			this.id = new SymphonyID(id);
//		}
//
//		private SymphonyNode(double id, int index, Graph graph,
//				SymphonyNode pred) {
//			super(index, graph);
//			this.id = new SymphonyID(id);
//			this.pred = pred;
//		}
//
//		private SymphonyNode(double id, int index, Graph graph,
//				SymphonyNode pred, SymphonyNode succ, String routing) {
//			super(index, graph);
//			this.id = new SymphonyID(id);
//			this.pred = pred;
//			this.succ = succ;
//		}
//
//		public boolean contains(double id) {
//			if (this.id.id > this.pred.id.id) {
//				return this.pred.id.id < id && id <= this.id.id;
//			}
//			return this.pred.id.id < id || id <= this.id.id;
//		}
//
//		public boolean contains(Identifier id) {
//			if (this.id.id > this.pred.id.id) {
//				return this.id.id >= ((SymphonyID) id).id
//						&& this.pred.id.id < ((SymphonyID) id).id;
//			} else {
//				return this.id.id >= ((SymphonyID) id).id
//						|| this.pred.id.id < ((SymphonyID) id).id;
//			}
//		}
//
//		public double dist(Identifier id) {
//			return this.id.dist(id);
//		}
//
//		public Identifier randomID(Random rand, Node[] nodes) {
//			return new SymphonyID(rand.nextDouble() % 1);
//		}
//
//		public double dist(IDNode node) {
//			return ((SymphonyNode) node).id.dist(this.id);
//		}
//	}
//}

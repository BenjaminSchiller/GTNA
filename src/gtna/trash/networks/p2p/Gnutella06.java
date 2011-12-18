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
// * Gnutella06.java
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
//
///**
// * Implements a network generator for the unstructured P2P network Gnutella
// * version 0.6 which uses a super-peer architecture.
// * 
// * The network model was derived from various sources including the paper
// * "Characterizing the two-tier Gnutella topology" by Stutzbach and Rejaie
// * (2005). Other sources were used as well as inspiration for the model.
// * 
// * @author benni
// * 
// */
//// TODO reimplement or remove Gnutella06
//public class Gnutella06 {
//	// public class Gnutella06 extends NetworkImpl implements Network {
//	// private int NETWORK_INIT_SIZE;
//	//
//	// private int BOOTSTRAP_LIST_SIZE;
//	//
//	// private int LEAF_LINKS_MIN;
//	//
//	// private int LEAF_LINKS_MAX;
//	//
//	// private int MAX_LEAVES;
//	//
//	// private int ULTRA_LINKS_MIN;
//	//
//	// private int ULTRA_LINKS_MAX;
//	//
//	// private int TRANSITION_THRESHOLD;
//	//
//	// private double TRANSITION_PROBABILITY;
//	//
//	// // ultra => ultra
//	// // signiﬁcant spike around 30 which is the default max-imum degree
//	//
//	// // ultra => leafes: 30, 45, (70)
//	// // signiﬁcant minority of ultrapeers are connected to less than 30 leaves
//	//
//	// // leaf => ultra: < 10
//	//
//	// public static Gnutella06[] get(int[] n, RoutingAlgorithm ra,
//	// Transformation[] t) {
//	// Gnutella06[] nw = new Gnutella06[n.length];
//	// for (int i = 0; i < n.length; i++) {
//	// nw[i] = new Gnutella06(n[i], ra, t);
//	// }
//	// return nw;
//	// }
//	//
//	// public Gnutella06(int nodes, RoutingAlgorithm ra, Transformation[] t) {
//	// super("GNUTELLA06", nodes, new String[] {}, new String[] {}, ra, t);
//	// this.NETWORK_INIT_SIZE = Config.getInt("GNUTELLA06_NETWORK_INIT_SIZE");
//	// this.BOOTSTRAP_LIST_SIZE = Config
//	// .getInt("GNUTELLA06_BOOTSTRAP_LIST_SIZE");
//	// this.LEAF_LINKS_MIN = Config.getInt("GNUTELLA06_LEAF_LINKS_MIN");
//	// this.LEAF_LINKS_MAX = Config.getInt("GNUTELLA06_LEAF_LINKS_MAX");
//	// this.MAX_LEAVES = Config.getInt("GNUTELLA06_MAX_LEAVES");
//	// this.ULTRA_LINKS_MIN = Config.getInt("GNUTELLA06_ULTRA_LINKS_MIN");
//	// this.ULTRA_LINKS_MAX = Config.getInt("GNUTELLA06_ULTRA_LINKS_MAX");
//	// this.TRANSITION_THRESHOLD = Config
//	// .getInt("GNUTELLA06_TRANSITION_THRESHOLD");
//	// this.TRANSITION_PROBABILITY = Config
//	// .getDouble("GNUTELLA06_TRANSITION_PROBABILITY");
//	// }
//	//
//	// public Graph generate() {
//	// Timer timer = new Timer();
//	// Random rand = new Random(System.currentTimeMillis());
//	//
//	// Gnutella06Node[] nodes = new Gnutella06Node[this.nodes()];
//	// ArrayList<Gnutella06Node> leafNodes = new ArrayList<Gnutella06Node>(
//	// nodes.length);
//	// ArrayList<Gnutella06Node> ultraNodes = new ArrayList<Gnutella06Node>(
//	// nodes.length);
//	// /**
//	// * init network with NETWORK_INIT_SIZE ultra nodes
//	// */
//	// for (int i = 0; i < Math.min(NETWORK_INIT_SIZE, nodes.length); i++) {
//	// nodes[i] = new Gnutella06Node(i, true);
//	// ultraNodes.add(nodes[i]);
//	// }
//	// /**
//	// * add nodes
//	// */
//	// for (int i = Math.min(NETWORK_INIT_SIZE, nodes.length); i < nodes.length;
//	// i++) {
//	// Gnutella06Node[] bootstrapList = bootstrapList(ultraNodes, rand);
//	// int averageNumberOfLeaves = averageNumberOfLeaves(bootstrapList);
//	// if (averageNumberOfLeaves < TRANSITION_THRESHOLD
//	// && rand.nextDouble() <= TRANSITION_PROBABILITY) {
//	// /**
//	// * add LEAF
//	// */
//	// nodes[i] = new Gnutella06Node(i, false);
//	// leafNodes.add(nodes[i]);
//	// Arrays.sort(bootstrapList, new LeavesComparator());
//	// int links = LEAF_LINKS_MIN
//	// + rand.nextInt(LEAF_LINKS_MAX - LEAF_LINKS_MIN + 1);
//	// int index = 0;
//	// int counter = 0;
//	// while (nodes[i].ultraNodes.size() < links) {
//	// if (bootstrapList[index].leafNodes.size() < MAX_LEAVES) {
//	// nodes[i].ultraNodes.add(bootstrapList[index]);
//	// bootstrapList[index].leafNodes.add(nodes[i]);
//	// counter = 0;
//	// }
//	// if (counter > 100) {
//	// break;
//	// }
//	// index++;
//	// counter++;
//	// }
//	// } else {
//	// /**
//	// * add ULTRA
//	// */
//	// nodes[i] = new Gnutella06Node(i, true);
//	// ultraNodes.add(nodes[i]);
//	// for (int j = 0; j < averageNumberOfLeaves; j++) {
//	// Gnutella06Node ultra = bootstrapList[bootstrapList.length
//	// - (j % bootstrapList.length) - 1];
//	// if (ultra.leafNodes.size() > 0) {
//	// Gnutella06Node leaf = ultra.leafNodes.get(rand
//	// .nextInt(ultra.leafNodes.size()));
//	// ultra.leafNodes.remove(leaf);
//	// nodes[i].leafNodes.add(leaf);
//	// leaf.ultraNodes.remove(ultra);
//	// leaf.ultraNodes.add(nodes[i]);
//	// }
//	// }
//	// }
//	// }
//	// /**
//	// * create ULTRA-ULTRA links
//	// */
//	// for (int i = 0; i < ultraNodes.size(); i++) {
//	// int links = ULTRA_LINKS_MIN
//	// + rand.nextInt(ULTRA_LINKS_MAX - ULTRA_LINKS_MIN);
//	// Gnutella06Node ultra = ultraNodes.get(i);
//	// int counter = 0;
//	// while (ultra.ultraNodes.size() < links) {
//	// Gnutella06Node link = ultraNodes.get(rand.nextInt(ultraNodes
//	// .size()));
//	// if (link.index() != ultra.index()
//	// && link.ultraNodes.size() < ULTRA_LINKS_MAX
//	// && !ultra.ultraNodes.contains(link)) {
//	// ultra.ultraNodes.add(link);
//	// link.ultraNodes.add(ultra);
//	// counter = 0;
//	// }
//	// if (counter > 100) {
//	// break;
//	// }
//	// counter++;
//	// }
//	// }
//	// /**
//	// * CHECK
//	// */
//	// // for (int i = 0; i < nodes.length; i++) {
//	// // if (nodes[i].ultraNode) {
//	// // if (nodes[i].ultraNodes.size() > ULTRA_LINKS_MAX) {
//	// // System.out.println("ULTRA-ULTRA");
//	// // }
//	// // if (nodes[i].leafNodes.size() > MAX_LEAVES) {
//	// // System.out.println("ULTRA-LEAVES");
//	// // }
//	// // } else {
//	// // if (nodes[i].ultraNodes.size() > LEAF_LINKS_MAX) {
//	// // System.out.println("LEAF-ULTRA");
//	// // }
//	// // if (nodes[i].leafNodes.size() > 0) {
//	// // System.out.println("LEAF-LEAVES");
//	// // }
//	// // }
//	// // }
//	// /**
//	// * fill nodes
//	// */
//	// for (int i = 0; i < nodes.length; i++) {
//	// Gnutella06Node[] in = new Gnutella06Node[nodes[i].leafNodes.size()
//	// + nodes[i].ultraNodes.size()];
//	// for (int j = 0; j < nodes[i].leafNodes.size(); j++) {
//	// in[j] = nodes[i].leafNodes.get(j);
//	// }
//	// for (int j = 0; j < nodes[i].ultraNodes.size(); j++) {
//	// in[j + nodes[i].leafNodes.size()] = nodes[i].ultraNodes.get(j);
//	// }
//	// nodes[i].init(in, in);
//	// }
//	// timer.end();
//	// Graph g = new Graph(this.description(), nodes, timer);
//	// return g;
//	// }
//	//
//	// private Gnutella06Node[] bootstrapList(ArrayList<Gnutella06Node> ultras,
//	// Random rand) {
//	// int size = Math.min(BOOTSTRAP_LIST_SIZE, ultras.size());
//	// ArrayList<Gnutella06Node> list = new ArrayList<Gnutella06Node>(size);
//	// while (list.size() < size) {
//	// Gnutella06Node ultra = ultras.get(rand.nextInt(ultras.size()));
//	// if (!list.contains(ultra)) {
//	// list.add(ultra);
//	// }
//	// }
//	// Gnutella06Node[] array = new Gnutella06Node[list.size()];
//	// for (int i = 0; i < list.size(); i++) {
//	// array[i] = list.get(i);
//	// }
//	// return array;
//	// }
//	//
//	// private static int averageNumberOfLeaves(Gnutella06Node[] nodes) {
//	// int sum = 0;
//	// for (int i = 0; i < nodes.length; i++) {
//	// sum += nodes[i].leafNodes.size();
//	// }
//	// return sum / nodes.length;
//	// }
//	//
//	// private class Gnutella06Node extends Node {
//	// private ArrayList<Gnutella06Node> leafNodes;
//	//
//	// private ArrayList<Gnutella06Node> ultraNodes;
//	//
//	// private boolean ultraNode;
//	//
//	// private Gnutella06Node(int index, boolean ultraNode) {
//	// super(index);
//	// this.ultraNode = ultraNode;
//	// this.leafNodes = new ArrayList<Gnutella06Node>();
//	// this.ultraNodes = new ArrayList<Gnutella06Node>();
//	// }
//	//
//	// public String toString() {
//	// StringBuffer buff = new StringBuffer();
//	// if (this.ultraNode) {
//	// buff.append("ULTRA - " + this.index() + " - "
//	// + this.ultraNodes.size() + " / "
//	// + this.leafNodes.size());
//	// } else {
//	// buff.append("LEAF  - " + this.index() + " - "
//	// + this.ultraNodes.size());
//	// }
//	// return buff.toString();
//	// }
//	// }
//	//
//	// private class LeavesComparator implements Comparator<Gnutella06Node> {
//	// public int compare(Gnutella06Node a, Gnutella06Node b) {
//	// if (a.leafNodes.size() == b.leafNodes.size()) {
//	// return 0;
//	// } else if (a.leafNodes.size() < b.leafNodes.size()) {
//	// return -1;
//	// } else {
//	// return 1;
//	// }
//	// }
//	// }
//}

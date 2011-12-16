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
// * Gnutella04.java
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
// * Implements a network generator for the network topology of Gnutella version
// * 0.4.
// * 
// * Note that this simply generates a topology with a specific degree
// * distribution. The distribution is described as the only property in the book
// * "Peer-to-peer systems and applications" by Steinmetz and Wehrle (2005). It is
// * therefore a rather limited implementation and should not be considered as a
// * good representation of the Gnutella network.
// * 
// * @author benni
// * 
// */
//// TODO reimplement or remove Gnutella04
//public class Gnutella04 {
//	// public class Gnutella04 extends NetworkImpl implements Network {
//	// public Gnutella04(int nodes, RoutingAlgorithm ra, Transformation[] t) {
//	// super("GNUTELLA04", nodes, new String[] {}, new String[] {}, ra, t);
//	// }
//	//
//	// public static Gnutella04[] get(int[] n, RoutingAlgorithm ra,
//	// Transformation[] t) {
//	// Gnutella04[] nw = new Gnutella04[n.length];
//	// for (int i = 0; i < n.length; i++) {
//	// nw[i] = new Gnutella04(n[i], ra, t);
//	// }
//	// return nw;
//	// }
//	//
//	// public Graph generate() {
//	// return this.generate(0);
//	// }
//	//
//	// private Graph generate(int restart) {
//	// Timer timer = new Timer();
//	// int[] nodesOfDegree = this.nodesOfDegree();
//	//
//	// Gnutella04Node[] nodes = new Gnutella04Node[this.nodes()];
//	// int[] index = new int[nodes.length];
//	// ArrayList<Integer> nodeList = new ArrayList<Integer>();
//	// int nodeIndex = 0;
//	// for (int i = 1; i < nodesOfDegree.length; i++) {
//	// for (int j = 0; j < nodesOfDegree[i]; j++) {
//	// for (int k = 0; k < i; k++) {
//	// nodeList.add(nodeIndex);
//	// }
//	// nodes[nodeIndex] = new Gnutella04Node(nodeIndex, i);
//	// nodeIndex++;
//	// }
//	// }
//	// while (nodeIndex < nodes.length) {
//	// nodes[nodeIndex] = new Gnutella04Node(nodeIndex, 2);
//	// nodeList.add(nodeIndex);
//	// nodeList.add(nodeIndex);
//	// nodeIndex++;
//	// }
//	//
//	// Random rand = new Random(System.currentTimeMillis() + restart);
//	// int counter = 0;
//	// while (nodeList.size() > 0) {
//	// int index1 = nodeList.size() - 1;
//	// int index2 = rand.nextInt(nodeList.size() / 2);
//	// int u = nodeList.get(index1);
//	// int v = nodeList.get(index2);
//	// boolean exists = false;
//	// int[] out = nodes[u].getOutgoingEdges();
//	// for (int i = 0; i < out.length; i++) {
//	// if (out[i] != null && out[i].index() == v) {
//	// exists = true;
//	// }
//	// }
//	// if (u != v && !exists) {
//	// nodes[u].in()[index[u]] = nodes[v];
//	// nodes[u].out()[index[u]] = nodes[v];
//	// nodes[v].in()[index[v]] = nodes[u];
//	// nodes[v].out()[index[v]] = nodes[u];
//	// index[u]++;
//	// index[v]++;
//	// nodeList.remove(index1);
//	// nodeList.remove(index2);
//	// counter = 0;
//	// } else {
//	// counter++;
//	// }
//	// if (counter == 100) {
//	// return generate(restart + 1);
//	// }
//	// }
//	//
//	// this.fix(nodes, rand);
//	// timer.end();
//	// Graph graph = new Graph(this.description(), nodes, timer);
//	// return graph;
//	// }
//	//
//	// private void fix(Gnutella04Node[] nodes, Random rand) {
//	// ArrayList<ArrayList<Integer>> clusters = this.clusters(nodes);
//	//
//	// int max = 0;
//	// for (int i = 1; i < clusters.size(); i++) {
//	// if (clusters.get(i).size() > clusters.get(max).size()) {
//	// max = i;
//	// }
//	// }
//	// for (int i = 0; i < clusters.size(); i++) {
//	// if (i != max) {
//	// int from = clusters.get(i).get(
//	// rand.nextInt(clusters.get(i).size()));
//	// int to = clusters.get(max).get(
//	// rand.nextInt(clusters.get(max).size()));
//	// this.add(nodes[from], nodes[to]);
//	// this.add(nodes[to], nodes[from]);
//	// }
//	// }
//	// }
//	//
//	// private void add(Gnutella04Node node, Gnutella04Node v) {
//	// Node[] in = node.in();
//	// Gnutella04Node[] newIn = new Gnutella04Node[in.length + 1];
//	// for (int i = 0; i < in.length; i++) {
//	// newIn[i] = (Gnutella04Node) in[i];
//	// }
//	// newIn[newIn.length - 1] = v;
//	// node.init(newIn, newIn);
//	// }
//	//
//	// private ArrayList<ArrayList<Integer>> clusters(Gnutella04Node[] nodes) {
//	// ArrayList<ArrayList<Integer>> clusters = new
//	// ArrayList<ArrayList<Integer>>(
//	// 20);
//	// boolean[] visited = new boolean[nodes.length];
//	// for (int start = 0; start < nodes.length; start++) {
//	// if (!visited[start]) {
//	// int size = 10;
//	// if (clusters.size() == 0
//	// || (clusters.size() == 1 && clusters.get(
//	// clusters.size() - 1).size() > nodes.length / 2)) {
//	// size = nodes.length;
//	// }
//	// ArrayList<Integer> cluster = new ArrayList<Integer>(size);
//	// clusters.add(cluster);
//	// Queue<Integer> q = new LinkedList<Integer>();
//	// visited[start] = true;
//	// cluster.add(start);
//	// q.add(start);
//	// while (!q.isEmpty()) {
//	// int current = q.poll();
//	// Node[] out = nodes[current].out();
//	// for (int i = 0; i < out.length; i++) {
//	// int index = ((Gnutella04Node) out[i]).index();
//	// if (!visited[index]) {
//	// visited[index] = true;
//	// cluster.add(index);
//	// q.add(index);
//	// }
//	// }
//	// }
//	// }
//	// }
//	// return clusters;
//	// }
//	//
//	// private int[] nodesOfDegree() {
//	// double[] p = new double[8];
//	// p[0] = 0;
//	// double c = 0;
//	// for (int i = 1; i < p.length; i++) {
//	// p[i] = Math.pow(i, -1.4);
//	// c += p[i];
//	// }
//	// for (int i = 1; i < p.length; i++) {
//	// p[i] = p[i] / c;
//	// }
//	//
//	// int[] nodesOfDegree = new int[p.length];
//	// for (int i = 1; i < p.length; i++) {
//	// nodesOfDegree[i] = (int) Math.round(this.nodes() * p[i]);
//	// }
//	// int edgesSum = 0;
//	// int nodesSum = 0;
//	// for (int i = 1; i < nodesOfDegree.length; i++) {
//	// edgesSum += i * nodesOfDegree[i];
//	// nodesSum += nodesOfDegree[i];
//	// }
//	// if (nodesSum > this.nodes()) {
//	// nodesOfDegree[1] -= nodesSum - this.nodes();
//	// edgesSum -= nodesSum - this.nodes();
//	// nodesSum -= nodesSum - this.nodes();
//	// }
//	// if (edgesSum % 2 == 1) {
//	// nodesOfDegree[1]--;
//	// nodesOfDegree[2]++;
//	// }
//	//
//	// return nodesOfDegree;
//	// }
//	//
//	// private class Gnutella04Node extends Node {
//	// private Gnutella04Node(int index, int inOutDegree) {
//	// super(index);
//	// this.init(new Gnutella04Node[inOutDegree],
//	// new Gnutella04Node[inOutDegree]);
//	// }
//	// }
//}

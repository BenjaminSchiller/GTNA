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
// * HybridKleinberg.java
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
//package gtna.trash.networks.model;
//
//
//// TODO reimplement HybridKleinberg
//public class HybridKleinberg {
//	// public class HybridKleinberg extends NetworkImpl implements Network {
//	// private Series series;
//	//
//	// private int dimensions;
//	//
//	// private double ratio;
//	//
//	// private double clusteringExponent;
//	//
//	// private boolean bidirectional;
//	//
//	// public HybridKleinberg(Series series, int dimensions, double ratio,
//	// double clusteringExponent, boolean bidirectional,
//	// RoutingAlgorithm ra, Transformation[] t) {
//	// super(
//	// "HYBRID_KLEINBERG",
//	// series.network().nodes(),
//	// new String[] { "NETWORK", "DIMENSIONS", "RATIO",
//	// "CLUSTERING_EXPONENT", "BIDIRECTIONAL" },
//	// new String[] {
//	// series.network()
//	// .folder()
//	// .replace(
//	// Config.get("FILESYSTEM_FOLDER_DELIMITER"),
//	// ""), dimensions + "", ratio + "",
//	// clusteringExponent + "", bidirectional + "" }, ra, t);
//	// // this.series = series;
//	// // this.dimensions = dimensions;
//	// // this.ratio = ratio;
//	// // this.clusteringExponent = clusteringExponent;
//	// // this.bidirectional = bidirectional;
//	// }
//	//
//	// public Graph generate() {
//	// return null;
//	// }
//	//
//	// public Graph generate() {
//	// Timer timer = new Timer();
//	// GridNode[] nodes = new GridNode[this.nodes()];
//	// int gridSize = (int) Math.ceil(Math.pow(this.nodes(),
//	// 1.0 / (double) this.dimensions));
//	// double[] x = new double[this.dimensions];
//	// for (int i = 0; i < nodes.length; i++) {
//	// GridIDManhattan id = new GridIDManhattan(x.clone());
//	// nodes[i] = new GridNode(i, id, nodes);
//	// x[0] = (x[0] + 1) % gridSize;
//	// for (int j = 1; j < x.length; j++) {
//	// if (x[j - 1] == 0) {
//	// x[j] = (x[j] + 1) % gridSize;
//	// } else {
//	// break;
//	// }
//	// }
//	// }
//	//
//	// Random rand = new Random(System.currentTimeMillis());
//	//
//	// DegreeDistribution dd = new DegreeDistribution();
//	// // int index = rand.nextInt(this.series.graphFolders().length);
//	// // dd.readXData(this.series, index);
//	// // dd.readXValues(this.series, index);
//	//
//	// int[] inDegree = new int[this.nodes()];
//	// for (int i = 0; i < dd.g.nodes.length; i++) {
//	// inDegree[i] = dd.g.nodes[i].in().length;
//	// }
//	// inDegree = Util.random(inDegree, rand);
//	//
//	// int[] outDegree = new int[this.nodes()];
//	// for (int i = 0; i < dd.g.nodes.length; i++) {
//	// outDegree[i] = dd.g.nodes[i].out().length;
//	// }
//	// outDegree = Util.random(outDegree, rand);
//	//
//	// Edges edges = new Edges(nodes, dd.g.edges);
//	//
//	// String b = this.bidirectional ? "_BIDIRECTIONAL" : "";
//	// int additionalFirstNeighbors = Config
//	// .getInt("HYBRID_KLEINBERG_ADDITIONAL_FIRST_NEIGHBORS" + b);
//	// int additionalNeighbors = Config
//	// .getInt("HYBRID_KLEINBERG_ADDITIONAL_NEIGHBORS" + b);
//	// int additionalLongLinks = Config
//	// .getInt("HYBRID_KLEINBERG_ADDITIONAL_LONG_LINKS" + b);
//	//
//	// for (int i = 0; i < nodes.length; i++) {
//	// GridNode[] neighbors = this.nodesAtDistance(nodes, nodes[i], 1);
//	// for (int j = 0; j < neighbors.length; j++) {
//	// this.addLink(nodes[i], neighbors[j], edges, outDegree,
//	// inDegree, additionalFirstNeighbors);
//	// }
//	// }
//	//
//	// for (int i = 0; i < nodes.length; i++) {
//	// double[] prob = this.computeProb(nodes[i], nodes);
//	// int none = 0;
//	// while (edges.outDegree(nodes[i]) < outDegree[i]) {
//	// none++;
//	// if (rand.nextDouble() <= this.ratio) {
//	// if (this.addShortLink(nodes[i], nodes, edges, outDegree,
//	// inDegree, gridSize, additionalNeighbors)) {
//	// none = 0;
//	// }
//	// } else {
//	// if (this.addLongLink(nodes[i], edges, nodes, prob, rand,
//	// outDegree, inDegree, additionalLongLinks)) {
//	// none = 0;
//	// }
//	// }
//	// if (none > 1000) {
//	// // System.out.println("none @ " + i + " @ "
//	// // + edges.outDegree(nodes[i]) + " / " + outDegree[i]);
//	// break;
//	// }
//	// }
//	// }
//	// // System.out.println("ratio: " + this.ratio);
//	// // System.out.println("   firstShort: " + firstShortLinks);
//	// // System.out.println("   short: " + shortLinks);
//	// // System.out.println("   long: " + longLinks);
//	// // System.out.println("   ratio: " + (double) shortLinks
//	// // / (double) (shortLinks + longLinks));
//	// edges.fill();
//	// timer.end();
//	// return new Graph(this.name(), nodes, timer);
//	// }
//	//
//	// private boolean addShortLink(GridNode node, GridNode[] nodes, Edges
//	// edges,
//	// int[] outDegree, int[] inDegree, int gridSize, int additionalLinks) {
//	// if (!this
//	// .outPossible(node, edges, outDegree, inDegree, additionalLinks)) {
//	// return false;
//	// }
//	// int dist = 0;
//	// while (true) {
//	// dist++;
//	// if (dist > gridSize) {
//	// return false;
//	// }
//	// for (int i = 0; i < nodes.length; i++) {
//	// if (nodes[i].id.dist(node.id) == dist) {
//	// GridNode dest = nodes[i];
//	// if (this.addLink(node, dest, edges, outDegree, inDegree,
//	// additionalLinks)) {
//	// return true;
//	// }
//	// }
//	// }
//	// }
//	// }
//	//
//	// private boolean addLongLink(GridNode node, Edges edges, GridNode[] nodes,
//	// double[] prob, Random rand, int[] outDegree, int[] inDegree,
//	// int additionalLinks) {
//	// if (!this
//	// .outPossible(node, edges, outDegree, inDegree, additionalLinks)) {
//	// return false;
//	// }
//	// double sum = 0;
//	// for (int i = 0; i < nodes.length; i++) {
//	// sum += node.id.dist(nodes[i].id);
//	// }
//	// while (true) {
//	// GridNode contact = nodes[rand.nextInt(nodes.length)];
//	// if (node.index() == contact.index()) {
//	// continue;
//	// }
//	// if (prob[contact.index()] >= rand.nextDouble()) {
//	// if (edges.contains(node.index(), contact.index())) {
//	// continue;
//	// }
//	// if (edges.outDegree(node) >= outDegree[node.index()]
//	// + additionalLinks
//	// || edges.outDegree(contact) >= outDegree[contact
//	// .index()]
//	// + additionalLinks) {
//	// return false;
//	// }
//	// edges.add(node, contact);
//	// if (this.bidirectional) {
//	// edges.add(contact, node);
//	// }
//	// return true;
//	// }
//	// }
//	// }
//	//
//	// private boolean addLink(GridNode node, GridNode dest, Edges edges,
//	// int[] outDegree, int[] inDegree, int additionalLinks) {
//	// if (this.outPossible(node, edges, outDegree, inDegree, additionalLinks)
//	// && this.inPossible(dest, edges, outDegree, inDegree,
//	// additionalLinks)
//	// && !edges.contains(node.index(), dest.index())) {
//	// edges.add(node, dest);
//	// if (this.bidirectional) {
//	// edges.add(dest, node);
//	// }
//	// return true;
//	// }
//	// return false;
//	// }
//	//
//	// private boolean inPossible(GridNode node, Edges edges, int[] outDegree,
//	// int[] inDegree, int additionalLinks) {
//	// if (this.bidirectional) {
//	// return edges.inDegree(node) < inDegree[node.index()]
//	// + additionalLinks
//	// && edges.outDegree(node) < outDegree[node.index()]
//	// + additionalLinks;
//	// } else {
//	// return edges.inDegree(node) < inDegree[node.index()]
//	// + additionalLinks;
//	// }
//	// }
//	//
//	// private boolean outPossible(GridNode node, Edges edges, int[] outDegree,
//	// int[] inDegree, int additionalLinks) {
//	// if (this.bidirectional) {
//	// return edges.inDegree(node) < inDegree[node.index()]
//	// + additionalLinks
//	// && edges.outDegree(node) < outDegree[node.index()]
//	// + additionalLinks;
//	// } else {
//	// return edges.outDegree(node) < outDegree[node.index()]
//	// + additionalLinks;
//	// }
//	// }
//	//
//	// private GridNode[] nodesAtDistance(GridNode[] nodes, GridNode node,
//	// int distance) {
//	// ArrayList<GridNode> list = new ArrayList<GridNode>();
//	// for (int i = 0; i < nodes.length; i++) {
//	// if (node.id.dist(nodes[i].id) == distance) {
//	// list.add(nodes[i]);
//	// }
//	// }
//	// GridNode[] array = new GridNode[list.size()];
//	// for (int i = 0; i < list.size(); i++) {
//	// array[i] = list.get(i);
//	// }
//	// return array;
//	// }
//	//
//	// private double[] computeProb(GridNode node, GridNode[] nodes) {
//	// double[] prob = new double[nodes.length];
//	// double sum = 0;
//	// for (int j = 0; j < nodes.length; j++) {
//	// if (node.index() != j) {
//	// sum += Math.pow(node.id.dist(nodes[j].id),
//	// -this.clusteringExponent);
//	// }
//	// }
//	// for (int j = 0; j < nodes.length; j++) {
//	// prob[j] = Math.pow(node.id.dist(nodes[j].id),
//	// -this.clusteringExponent)
//	// / sum;
//	// }
//	// return prob;
//	// }
//	//
//	// public static HybridKleinberg[] get(Series series, int[] dimensions,
//	// double ratio, double clusteringExponent, boolean bidirectional,
//	// RoutingAlgorithm ra, Transformation[] t) {
//	// HybridKleinberg[] nw = new HybridKleinberg[dimensions.length];
//	// for (int i = 0; i < nw.length; i++) {
//	// nw[i] = new HybridKleinberg(series, dimensions[i], ratio,
//	// clusteringExponent, bidirectional, ra, t);
//	// }
//	// return nw;
//	// }
//	//
//	// public static HybridKleinberg[] get(Series series, int dimensions,
//	// double[] ratio, double clusteringExponent, boolean bidirectional,
//	// RoutingAlgorithm ra, Transformation[] t) {
//	// HybridKleinberg[] nw = new HybridKleinberg[ratio.length];
//	// for (int i = 0; i < nw.length; i++) {
//	// nw[i] = new HybridKleinberg(series, dimensions, ratio[i],
//	// clusteringExponent, bidirectional, ra, t);
//	// }
//	// return nw;
//	// }
//	//
//	// public static HybridKleinberg[] get(Series series, int dimensions,
//	// double ratio, double[] clusteringExponent, boolean bidirectional,
//	// RoutingAlgorithm ra, Transformation[] t) {
//	// HybridKleinberg[] nw = new HybridKleinberg[clusteringExponent.length];
//	// for (int i = 0; i < nw.length; i++) {
//	// nw[i] = new HybridKleinberg(series, dimensions, ratio,
//	// clusteringExponent[i], bidirectional, ra, t);
//	// }
//	// return nw;
//	// }
//
//}

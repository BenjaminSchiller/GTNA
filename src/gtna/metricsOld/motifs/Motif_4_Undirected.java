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
 * Motif_4_Undirected.java
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
package gtna.metricsOld.motifs;


// TODO reimplement Motif_4_Undirected
@Deprecated
public class Motif_4_Undirected {
	// public class Motif_4_Undirected extends MetricImpl implements Metric {
	//
	// // private global variables
	// private int degreeOfMostNodes;
	// private long runtime;
	// private int[] motif = new int[6];
	// private HashSet<Integer>[] nodesInMotifList = new HashSet[6];
	//
	// ArrayList<Integer>[] motifsUndirected;
	//
	// // metric constructor
	// public Motif_4_Undirected() {
	// super("MOTIF4");
	// }
	//
	// @Override
	// //
	// public void computeData(Graph g, Network nw, Hashtable<String, Metric> m)
	// {
	// // create graph for local calculations
	// Graph graph = new ReadableFile("ROLE", Modules.filename, Modules.type,
	// null, null).generate();
	//
	// // initialize local variables
	// motifsUndirected = new ArrayList[graph.nodes.length];
	// for (int i = 0; i < motifsUndirected.length; i++) {
	// motifsUndirected[i] = new ArrayList<Integer>();
	// }
	// for (int i = 0; i < nodesInMotifList.length; i++) {
	// nodesInMotifList[i] = new HashSet<Integer>();
	// }
	// // start the timer
	// Timer timer = new Timer();
	//
	// // Delete loops and parallel edges
	// graph = new ClearGraph().clearGraph(graph);
	// // graph = new ClearGraph().downsizeGraph(graph, 500);
	//
	// // calculate undirected 4-Node-Motifs
	// for (int actualNode = 0; actualNode < graph.nodes.length; actualNode++) {
	// undirectedMotifCombinations(actualNode, graph);
	// }
	//
	// // debug console print
	// // MotifFunctions.outprintMotifs(motifsUndirected, motif);
	//
	// // stop the timer
	// this.runtime = timer.msec();
	// }
	//
	// // assign the motif type of a given subgraph
	// private void undirectedMotifCombinations(int node, Graph g) {
	// ArrayList<Integer> combi;
	// HashSet<Integer> done = new HashSet<Integer>();
	// done.add(node);
	// int[] degree;
	//
	// HashSet<Integer> level_one = GeneralGraphMethods.neighborsOfNode(node,
	// g);
	// Iterator<Integer> iter = level_one.iterator();
	// while (iter.hasNext()) {
	// if (iter.next() <= node) {
	// iter.remove();
	// }
	// }
	//
	// for (Integer level1 : level_one) {
	// HashSet<Integer> done2 = new HashSet<Integer>();
	// done.add(level1);
	// HashSet<Integer> level_two = GeneralGraphMethods.neighborsOfNode(
	// level1, g);
	// iter = level_two.iterator();
	// while (iter.hasNext()) {
	// Integer next = iter.next();
	// if (next <= node) {
	// iter.remove();
	// }
	// }
	// for (Integer l1 : level_one) {
	// done2.add(l1);
	// if (!done.contains(l1)) {
	// HashSet<Integer> level_two2 = GeneralGraphMethods
	// .neighborsOfNode(l1, g);
	// boolean l1_c = level_two.contains(l1);
	// for (Integer ll1 : level_one) {
	// if (!done2.contains(ll1) && !done.contains(ll1)) {
	// degree = new int[4];
	// degree[0] = 3;
	// degree[1] = 1;
	// degree[2] = 1;
	// degree[3] = 1;
	// if (l1_c) {
	// degree[1]++;
	// degree[2]++;
	// }
	// if (level_two.contains(ll1)) {
	// degree[1]++;
	// degree[3]++;
	// }
	// if (level_two2.contains(ll1)) {
	// degree[2]++;
	// degree[3]++;
	// }
	// combi = new ArrayList<Integer>();
	// combi.add(ll1);
	// combi.add(l1);
	// combi.add(level1);
	// combi.add(node);
	// Integer motif = getMotifUndirected(degree, combi);
	// for (int i : combi) {
	// motifsUndirected[i].add(motif);
	// }
	// }
	// }
	//
	// for (Integer l2 : level_two) {
	// if (!level_one.contains(l2)) {
	// degree = new int[4];
	// degree[0] = 2;
	// degree[1] = 2;
	// degree[2] = 1;
	// degree[3] = 1;
	// combi = new ArrayList<Integer>();
	// if (l1_c) {
	// degree[1]++;
	// degree[3]++;
	// }
	// if (level_two2.contains(l2)) {
	// degree[2]++;
	// degree[3]++;
	// }
	// combi.add(l2);
	// combi.add(l1);
	// combi.add(level1);
	// combi.add(node);
	// Integer motif = getMotifUndirected(degree, combi);
	// for (int i : combi) {
	// motifsUndirected[i].add(motif);
	// }
	// }
	// }
	//
	// for (Integer l2 : level_two2) {
	// if (l2 > node && !level_one.contains(l2)
	// && !level_two.contains(l2)) {
	// degree = new int[4];
	// degree[0] = 2;
	// degree[1] = 1;
	// degree[2] = 2;
	// degree[3] = 1;
	// if (l1_c) {
	// degree[2]++;
	// degree[1]++;
	// }
	// combi = new ArrayList<Integer>();
	// combi.add(l2);
	// combi.add(l1);
	// combi.add(level1);
	// combi.add(node);
	// Integer motif = getMotifUndirected(degree, combi);
	// for (int i : combi) {
	// motifsUndirected[i].add(motif);
	// }
	// }
	// }
	//
	// }
	// }
	// done2 = new HashSet<Integer>();
	// for (Integer l2 : level_two) {
	// if (!level_one.contains(l2)) {
	// HashSet<Integer> level_three = GeneralGraphMethods
	// .neighborsOfNode(l2, g);
	// done2.add(l2);
	// for (Integer ll2 : level_two) {
	// if (!done2.contains(ll2) && !level_one.contains(ll2)) {
	// degree = new int[4];
	// degree[0] = 1;
	// degree[1] = 3;
	// degree[2] = 1;
	// degree[3] = 1;
	// if (level_three.contains(ll2)) {
	// degree[2]++;
	// degree[3]++;
	// }
	// combi = new ArrayList<Integer>();
	// combi.add(l2);
	// combi.add(ll2);
	// combi.add(level1);
	// combi.add(node);
	// Integer motif = getMotifUndirected(degree, combi);
	// for (int i : combi) {
	// motifsUndirected[i].add(motif);
	// }
	// }
	// }
	//
	// for (Integer l3 : level_three) {
	// if (l3 > node && !level_one.contains(l3)
	// && !level_two.contains(l3)) {
	// degree = new int[4];
	// degree[0] = 2;
	// degree[1] = 2;
	// degree[2] = 1;
	// degree[3] = 1;
	// combi = new ArrayList<Integer>();
	// combi.add(l3);
	// combi.add(l2);
	// combi.add(level1);
	// combi.add(node);
	// Integer motif = getMotifUndirected(degree, combi);
	// for (int i : combi) {
	// motifsUndirected[i].add(motif);
	// }
	// }
	//
	// }
	// }
	// }
	//
	// }
	// }
	//
	// private Integer getMotifUndirected(int[] degree, ArrayList<Integer>
	// nodeList) {
	// int degree1 = 0;
	// int degree2 = 0;
	// int degree3 = 0;
	// for (int degreeOfNode : degree) {
	// switch (degreeOfNode) {
	// case 1:
	// degree1++;
	// break;
	// case 2:
	// degree2++;
	// break;
	// case 3:
	// degree3++;
	// break;
	// default:
	// System.out
	// .println("ERROR: Motif.MyMetric.getMotifUndirected invalid degreeOfNode: "
	// + degreeOfNode);
	// }
	// }
	// // if (degree1 + degree2 + degree3 != 4) {
	// // System.out
	// //
	// .println("ERROR: Motif.Motif_4_Undirected.getMotifUndirected invalid Motif: "
	// // + degree1 + "/" + degree2 + "/" + degree3);
	// // return null;
	// // }
	// //
	// // if (degree1 == 0) {
	// // if (degree2 == 0) {
	// // motif[5] += 4;
	// // addNodesToMotif(nodeList, 5);
	// // return 6;
	// // } else if (degree2 == 2) {
	// // motif[4] += 4;
	// // addNodesToMotif(nodeList, 4);
	// // return 5;
	// // } else if (degree2 == 4) {
	// // motif[3] += 4;
	// // addNodesToMotif(nodeList, 3);
	// // return 4;
	// // }
	// // } else if (degree1 == 1) {
	// // motif[2] += 4;
	// // addNodesToMotif(nodeList, 2);
	// // return 3;
	// // } else if (degree1 == 2) {
	// // motif[0] += 4;
	// // addNodesToMotif(nodeList, 0);
	// // return 1;
	// // } else if (degree1 == 3) {
	// // motif[1] += 4;
	// // addNodesToMotif(nodeList, 1);
	// // return 2;
	// // }
	//
	// // find the appropriate degree combination / motif
	// if (degree1 == 2 && degree2 == 2 && degree3 == 0) {
	// motif[0] += 4;
	// addNodesToMotif(nodeList, 0);
	// return 1;
	// }
	// if (degree1 == 3 && degree2 == 0 && degree3 == 1) {
	// motif[1] += 4;
	// addNodesToMotif(nodeList, 1);
	// return 2;
	// }
	// if (degree1 == 1 && degree2 == 2 && degree3 == 1) {
	// motif[2] += 4;
	// addNodesToMotif(nodeList, 2);
	// return 3;
	// }
	// if (degree1 == 0 && degree2 == 4 && degree3 == 0) {
	// motif[3] += 4;
	// addNodesToMotif(nodeList, 3);
	// return 4;
	// }
	// if (degree1 == 0 && degree2 == 2 && degree3 == 2) {
	// motif[4] += 4;
	// addNodesToMotif(nodeList, 4);
	// return 5;
	// }
	// if (degree1 == 0 && degree2 == 0 && degree3 == 4) {
	// motif[5] += 4;
	// addNodesToMotif(nodeList, 5);
	// return 6;
	// }
	//
	// // debug error message
	// System.out
	// .println("ERROR: Motif.Motif_4_Undirected.getMotifUndirected invalid Motif: "
	// + degree1 + "/" + degree2 + "/" + degree3);
	// return null;
	// }
	//
	// // add given node to given motif-list
	// private void addNodesToMotif(ArrayList<Integer> nodeList, int motif) {
	// nodesInMotifList[motif].addAll(nodeList);
	// }
	//
	// @Override
	// // GTNA output value
	// public Value[] getValues(Value[] values) {
	// Value domn = new Value("MOTIF4", this.degreeOfMostNodes);
	// Value runtime = new Value("MOTIF4", this.runtime);
	// return new Value[] { domn, runtime };
	// }
	//
	// @Override
	// // GTNA output data for gnuplot
	// public boolean writeData(String folder) {
	// try {
	// // initialize variables
	// int[] motif = new int[6];
	// double[] motif4Total = new double[motif.length];
	// Arrays.fill(motif, 0);
	// Arrays.fill(motif4Total, 0);
	// double[][] motif4Role = null;
	// double[][] motif4RoleM = new double[6][7];
	// for (int i = 0; i < motif4RoleM.length; i++) {
	// Arrays.fill(motif4RoleM[i], 0);
	// }
	//
	// // import the calculated roles
	// RoleFileReader RFR = new RoleFileReader();
	// ArrayList<Integer>[] nodesOfRole;
	//
	// nodesOfRole = RFR.readRoles(folder);
	//
	// ArrayList<Integer>[] numberOfMotifs = new ArrayList[7];
	// for (int i = 0; i < numberOfMotifs.length; i++) {
	// numberOfMotifs[i] = new ArrayList<Integer>();
	// }
	//
	// // count the 4 motifs
	// for (int i = 0; i < nodesOfRole.length; i++) { // for each role, do:
	// for (int j = 0; j < nodesOfRole[i].size(); j++) { // for each
	// // node j
	// // in role i,
	// // do:
	// for (int k = 0; k < motifsUndirected[nodesOfRole[i].get(j)]
	// .size(); k++) { // count each motif of node j in
	// // role i
	// if (motifsUndirected[nodesOfRole[i].get(j)].get(k) == 1) {
	// motif[0]++;
	// motif4Total[0]++;
	// } else {
	// if (motifsUndirected[nodesOfRole[i].get(j)].get(k) == 2) {
	// motif[1]++;
	// motif4Total[1]++;
	// } else {
	// if (motifsUndirected[nodesOfRole[i].get(j)]
	// .get(k) == 3) {
	// motif[2]++;
	// motif4Total[2]++;
	// } else {
	// if (motifsUndirected[nodesOfRole[i].get(j)]
	// .get(k) == 4) {
	// motif[3]++;
	// motif4Total[3]++;
	// } else {
	// if (motifsUndirected[nodesOfRole[i]
	// .get(j)].get(k) == 5) {
	// motif[4]++;
	// motif4Total[4]++;
	// } else {
	// if (motifsUndirected[nodesOfRole[i]
	// .get(j)].get(k) == 6) {
	// motif[5]++;
	// motif4Total[5]++;
	// } else {
	//
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// // calculate the total number of each motif for each role
	// for (int o = 0; o < 6; o++) {
	// numberOfMotifs[i].add(motif[o]);
	// }
	// Arrays.fill(motif, 0);
	// }
	//
	// // calculate the total number of motifs for each role
	// int counter[] = new int[7];
	// Arrays.fill(counter, 0);
	// for (int o = 0; o < 7; o++) {
	// for (int p = 0; p < numberOfMotifs[o].size(); p++) {
	// if (nodesOfRole[o].size() > 0) {
	// counter[o] += numberOfMotifs[o].get(p);
	// }
	// }
	// }
	// // calculate the number of motifs in percent for each role
	// motif4Role = new double[7][6];
	// for (int o = 0; o < 7; o++) {
	// for (int p = 0; p < numberOfMotifs[o].size(); p++) {
	// if (nodesOfRole[o].size() > 0) {
	// motif4Role[o][p] = (double) ((double) numberOfMotifs[o]
	// .get(p) / (double) ((double) counter[o] / (double) 100.0));
	// }
	// }
	// }
	//
	// // calculate the number of motifs in percent for each role
	// for (int i = 0; i < 6; i++) {
	// for (Integer j : nodesInMotifList[i]) {
	// for (int k = 0; k < nodesOfRole.length; k++) {
	// for (Integer l : nodesOfRole[k]) {
	// if (j == l) {
	// motif4RoleM[i][k]++;
	// }
	// }
	// }
	// }
	// }
	//
	// // significance profile
	// double[][] z = new double[7][motif4Total.length];
	// double[][] n = new double[7][motif4Total.length];
	// double[] zSum = new double[7];
	// Arrays.fill(zSum, 0);
	//
	// double numberOfTotalMotifs = 0;
	//
	// for (int i = 0; i < motif4Total.length; i++) {
	// numberOfTotalMotifs += motif4Total[i];
	// }
	//
	// for (int i = 0; i < motif4Total.length; i++) {
	// motif4Total[i] = (double) ((double) motif4Total[i] / ((double)
	// numberOfTotalMotifs / (double) 100));
	// }
	//
	// for (int i = 0; i < 7; i++) {
	// for (int j = 0; j < motif4Total.length; j++) {
	// z[i][j] = (double) ((double) motif4Role[i][j] - (double) motif4Total[j]);
	// zSum[i] += (z[i][j] * z[i][j]);
	// }
	// }
	// for (int i = 0; i < 7; i++) {
	// for (int j = 0; j < motif4Total.length; j++) {
	// if (nodesOfRole[i].size() > 0) {
	// n[i][j] = z[i][j]
	// / (double) Math.sqrt((double) zSum[i]);
	// } else {
	// n[i][j] = 0.0;
	// }
	// }
	// }
	//
	// for (int i = 0; i < n.length; i++) {
	// DataWriter.writeWithIndex(n[i], "SEQUENZ4_" + i, folder);
	// }
	// DataWriter.writeWithIndex(motif4RoleM[0], "MOTIF4_ROLE0M", folder);
	// DataWriter.writeWithIndex(motif4RoleM[1], "MOTIF4_ROLE1M", folder);
	// DataWriter.writeWithIndex(motif4RoleM[2], "MOTIF4_ROLE2M", folder);
	// DataWriter.writeWithIndex(motif4RoleM[3], "MOTIF4_ROLE3M", folder);
	// DataWriter.writeWithIndex(motif4RoleM[4], "MOTIF4_ROLE4M", folder);
	// DataWriter.writeWithIndex(motif4RoleM[5], "MOTIF4_ROLE5M", folder);
	// DataWriter.writeWithIndex(motif4Total, "MOTIF4_TOTAL", folder);
	// DataWriter.writeWithIndex(motif4Role[0], "MOTIF4_ROLE0", folder);
	// DataWriter.writeWithIndex(motif4Role[1], "MOTIF4_ROLE1", folder);
	// DataWriter.writeWithIndex(motif4Role[2], "MOTIF4_ROLE2", folder);
	// DataWriter.writeWithIndex(motif4Role[3], "MOTIF4_ROLE3", folder);
	// DataWriter.writeWithIndex(motif4Role[4], "MOTIF4_ROLE4", folder);
	// DataWriter.writeWithIndex(motif4Role[5], "MOTIF4_ROLE5", folder);
	// DataWriter.writeWithIndex(motif4Role[6], "MOTIF4_ROLE6", folder);
	// return false;
	// } catch (IOException e) {
	// return false;
	// }
	// }

}

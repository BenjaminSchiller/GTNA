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
// * Motif_3_Directed.java
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
//package gtna.trash.metrics.motifs;
//
//@Deprecated
//// TODO reimlement Motif_3_Directed
//public class Motif_3_Directed {
//	// public class Motif_3_Directed extends MetricImpl implements Metric {
//	//
//	// // private global variables
//	// private int degreeOfMostNodes;
//	// private long runtime;
//	// private HashSet<Integer>[] nodesInMotifList = new HashSet[13];
//	// private int[] motif = new int[13];
//	// private final int DIRECTEDMOTIF = 3;
//	//
//	// ArrayList<Integer>[] motifsDirected;
//	//
//	// // metric constructor
//	// public Motif_3_Directed() {
//	// super("MOTIF3");
//	// }
//	//
//	// @SuppressWarnings("unchecked")
//	// @Override
//	// //
//	// public void computeData(Graph g, Network nw, Hashtable<String, Metric> m)
//	// {
//	// // create graph for local calculations
//	// Graph graph = new ReadableFile("ROLE", Modules.filename,
//	// Modules.type, null, null).generate();
//	//
//	// // initialize local variables
//	// // GeneralGraphMethods GGM = new GeneralGraphMethods();
//	// motifsDirected = new ArrayList[graph.nodes.length];
//	// for (int i = 0; i < motifsDirected.length; i++) {
//	// motifsDirected[i] = new ArrayList<Integer>();
//	// }
//	// for (int i = 0; i < nodesInMotifList.length; i++) {
//	// nodesInMotifList[i] = new HashSet<Integer>();
//	// }
//	// ArrayList<ArrayList<Integer>> combinations;
//	//
//	// // start the timer
//	// Timer timer = new Timer();
//	//
//	// // Delete loops and parallel edges
//	// graph = new ClearGraph().graphDeleteLoops(graph);
//	// graph = new ClearGraph().graphDeleteDirectedParallelEdges(graph);
//	// // graph = new ClearGraph().downsizeGraph(graph, 500);
//	//
//	// // calculate directed 3-Node-Motifs
//	// for (int actualNode = 0; actualNode < graph.nodes.length; actualNode++) {
//	// // calculate node neighborhood
//	// // neighbors = GGM
//	// // .neighborhoodOfNode(actualNode, DIRECTEDMOTIF, graph);
//	// // calculate combinations of the neighborhood-list
//	// combinations = MotifFunctions.combinations(actualNode,
//	// DIRECTEDMOTIF, graph);
//	// // assign network motif and add it to the motif-list of the node
//	// for (int permutationNo = 0; permutationNo < combinations.size();
//	// permutationNo++) {
//	// ArrayList<Integer> currentCombination = combinations
//	// .get(permutationNo);
//	// Integer motif = getMotifDirected(currentCombination, graph);
//	// for(int i:currentCombination) {
//	// motifsDirected[i].add(motif);
//	// }
//	// // motifsDirected[].add(motif);
//	//
//	// }
//	// combinations = null;
//	// }
//	//
//	// // debug console print
//	// // MotifFunctions.outprintMotifs(motifsDirected, motif);
//	//
//	// // stop the timer
//	// this.runtime = timer.msec();
//	// }
//	//
//	// // assign the motif type of a given subgraph
//	// private Integer getMotifDirected(ArrayList<Integer> nodeList, Graph g) {
//	// // initialize variables
//	// int[] degreeOfNode;
//	// int[] nodeMotif = new int[9];
//	// int numberOfNodes = nodeList.size();
//	// Arrays.fill(nodeMotif, 0);
//	//
//	// for (int i = 0; i < nodeList.size(); i++) {
//	// // calculate the in- and out-degree within the subgraph for given
//	// // node
//	// degreeOfNode =
//	// GeneralGraphMethods.directedDegreeOfNodeWithinMotif(nodeList.get(i),
//	// nodeList, g);
//	//
//	// // look for the in- and out-degree pattern of given node
//	// if (degreeOfNode[0] == 0) {
//	// if (degreeOfNode[1] == 0) {
//	// nodeMotif[0]++;
//	// }
//	// if (degreeOfNode[1] == 1) {
//	// nodeMotif[1]++;
//	// }
//	// if (degreeOfNode[1] == 2) {
//	// nodeMotif[2]++;
//	// }
//	// } else {
//	// if (degreeOfNode[0] == 1) {
//	// if (degreeOfNode[1] == 0) {
//	// nodeMotif[3]++;
//	// }
//	// if (degreeOfNode[1] == 1) {
//	// nodeMotif[4]++;
//	// }
//	// if (degreeOfNode[1] == 2) {
//	// nodeMotif[5]++;
//	// }
//	// } else {
//	// if (degreeOfNode[0] == 2) {
//	// if (degreeOfNode[1] == 0) {
//	// nodeMotif[6]++;
//	// }
//	// if (degreeOfNode[1] == 1) {
//	// nodeMotif[7]++;
//	// }
//	// if (degreeOfNode[1] == 2) {
//	// nodeMotif[8]++;
//	// }
//	// } else {
//	//
//	// }
//	// }
//	// }
//	// }
//	//
//	// // find the appropriate degree combination / motif
//	// if (nodeMotif[3] == 2 && nodeMotif[2] == 1) {
//	// motif[0]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 0);
//	// return 1;
//	// }
//	// if (nodeMotif[3] == 1 && nodeMotif[4] == 1 && nodeMotif[1] == 1) {
//	// motif[1]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 1);
//	// return 2;
//	// }
//	// if (nodeMotif[3] == 1 && nodeMotif[5] == 1 && nodeMotif[4] == 1) {
//	// motif[2]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 2);
//	// return 3;
//	// }
//	// if (nodeMotif[1] == 2 && nodeMotif[6] == 1) {
//	// motif[3]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 3);
//	// return 4;
//	// }
//	// if (nodeMotif[4] == 1 && nodeMotif[6] == 1 && nodeMotif[2] == 1) {
//	// motif[4]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 4);
//	// return 5;
//	// }
//	// if (nodeMotif[5] == 2 && nodeMotif[6] == 1) {
//	// motif[5]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 5);
//	// return 6;
//	// }
//	// if (nodeMotif[4] == 1 && nodeMotif[1] == 1 && nodeMotif[7] == 1) {
//	// motif[6]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 6);
//	// return 7;
//	// }
//	// if (nodeMotif[4] == 2 && nodeMotif[8] == 1) {
//	// motif[7]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 7);
//	// return 8;
//	// }
//	// if (nodeMotif[4] == 3) {
//	// motif[8]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 8);
//	// return 9;
//	// }
//	// if (nodeMotif[5] == 1 && nodeMotif[4] == 1 && nodeMotif[7] == 1) {
//	// motif[9]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 9);
//	// return 10;
//	// }
//	// if (nodeMotif[7] == 2 && nodeMotif[2] == 1) {
//	// motif[10]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 10);
//	// return 11;
//	// }
//	// if (nodeMotif[7] == 1 && nodeMotif[5] == 1 && nodeMotif[8] == 1) {
//	// motif[11]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 11);
//	// return 12;
//	// }
//	// if (nodeMotif[8] == 3) {
//	// motif[12]+=numberOfNodes;
//	// addNodesToMotif(nodeList, 12);
//	// return 13;
//	// }
//	//
//	// // debug error message
//	// System.out
//	// .println("ERROR: Motif.Motif_3_Directed.getMotifDirected invalid Motif: "
//	// + nodeMotif[0]
//	// + "-"
//	// + nodeMotif[1]
//	// + "-"
//	// + nodeMotif[2]
//	// + "-"
//	// + nodeMotif[3]
//	// + "-"
//	// + nodeMotif[4]
//	// + "-"
//	// + nodeMotif[5]
//	// + "-"
//	// + nodeMotif[6]
//	// + "-"
//	// + nodeMotif[7]
//	// + "-"
//	// + nodeMotif[8]);
//	// return null;
//	// }
//	//
//	// // add given node to given motif-list
//	// private void addNodesToMotif(ArrayList<Integer> nodeList, int motif) {
//	// for (int i = 0; i < nodeList.size(); i++) {
//	// nodesInMotifList[motif].add(nodeList.get(i));
//	// }
//	// }
//	//
//	// @Override
//	// // GTNA output value
//	// public Value[] getValues(Value[] values) {
//	// Value domn = new Value("MOTIF3", this.degreeOfMostNodes);
//	// Value runtime = new Value("MOTIF3", this.runtime);
//	// return new Value[] { domn, runtime };
//	// }
//	//
//	// @Override
//	// // GTNA output data for gnuplot
//	// public boolean writeData(String folder) {
//	// try {
//	// // initialize variables
//	// int[] motif = new int[13];
//	// double[] motif3Total = new double[motif.length];
//	// Arrays.fill(motif, 0);
//	// Arrays.fill(motif3Total, 0);
//	// double[][] motif3Role = null;
//	// double[][] motif3RoleM = new double[13][7];
//	// for (int i = 0; i < motif3RoleM.length; i++) {
//	// Arrays.fill(motif3RoleM[i], 0);
//	// }
//	//
//	// // import the calculated roles
//	// RoleFileReader RFR = new RoleFileReader();
//	// ArrayList<Integer>[] nodesOfRole;
//	//
//	// nodesOfRole = RFR.readRoles(folder);
//	//
//	// ArrayList<Integer>[] numberOfMotifs = new ArrayList[7];
//	// for (int i = 0; i < numberOfMotifs.length; i++) {
//	// numberOfMotifs[i] = new ArrayList<Integer>();
//	// }
//	//
//	// // count the 3 motifs
//	// for (int i = 0; i < nodesOfRole.length; i++) { // for each role, do:
//	// for (int j = 0; j < nodesOfRole[i].size(); j++) { // for each node j
//	// // in role i,
//	// // do:
//	// for (int k = 0; k < motifsDirected[nodesOfRole[i].get(j)]
//	// .size(); k++) { // count each motif of node j in role i
//	// if (motifsDirected[nodesOfRole[i].get(j)].get(k) == 1) {
//	// motif[0]++;
//	// motif3Total[0]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i].get(j)].get(k) == 2) {
//	// motif[1]++;
//	// motif3Total[1]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i].get(j)].get(k) == 3) {
//	// motif[2]++;
//	// motif3Total[2]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i].get(j)]
//	// .get(k) == 4) {
//	// motif[3]++;
//	// motif3Total[3]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i].get(j)]
//	// .get(k) == 5) {
//	// motif[4]++;
//	// motif3Total[4]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i]
//	// .get(j)].get(k) == 6) {
//	// motif[5]++;
//	// motif3Total[5]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i]
//	// .get(j)].get(k) == 7) {
//	// motif[6]++;
//	// motif3Total[6]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i]
//	// .get(j)].get(k) == 8) {
//	// motif[7]++;
//	// motif3Total[7]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i]
//	// .get(j)].get(k) == 9) {
//	// motif[8]++;
//	// motif3Total[8]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i]
//	// .get(j)].get(k) == 10) {
//	// motif[9]++;
//	// motif3Total[9]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i]
//	// .get(j)]
//	// .get(k) == 11) {
//	// motif[10]++;
//	// motif3Total[10]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i]
//	// .get(j)]
//	// .get(k) == 12) {
//	// motif[11]++;
//	// motif3Total[11]++;
//	// } else {
//	// if (motifsDirected[nodesOfRole[i]
//	// .get(j)]
//	// .get(k) == 13) {
//	// motif[12]++;
//	// motif3Total[12]++;
//	// } else {
//	//
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//	// // calculate the total number of each motif for each role
//	// for (int o = 0; o < 13; o++) {
//	// numberOfMotifs[i].add(motif[o]);
//	// }
//	// Arrays.fill(motif, 0);
//	// }
//	//
//	// // calculate the total number of motifs for each role
//	// int counter[] = new int[7];
//	// Arrays.fill(counter, 0);
//	// for (int o = 0; o < 7; o++) {
//	// for (int p = 0; p < numberOfMotifs[o].size(); p++) {
//	// if ((double) nodesOfRole[o].size() > 0) {
//	// counter[o] += numberOfMotifs[o].get(p);
//	// }
//	// }
//	// }
//	//
//	// // calculate the number of motifs in percent for each role
//	// motif3Role = new double[7][13];
//	// for (int o = 0; o < 7; o++) {
//	// for (int p = 0; p < numberOfMotifs[o].size(); p++) {
//	// if ((double) nodesOfRole[o].size() > 0) {
//	// motif3Role[o][p] = (double) ((double) numberOfMotifs[o]
//	// .get(p) / (double) ((double) counter[o] / (double) 100.0));
//	// }
//	// }
//	// }
//	//
//	// // count number of roles for each motif
//	// for (int i = 0; i < 13; i++) {
//	// for (Integer j: nodesInMotifList[i]) {
//	// for (int k = 0; k < nodesOfRole.length; k++) {
//	// for (int l = 0; l < nodesOfRole[k].size(); l++) {
//	// if (j == nodesOfRole[k].get(l)) {
//	// motif3RoleM[i][k]++;
//	// }
//	// }
//	// }
//	// }
//	// }
//	//
//	// // significance profile
//	// double[][] z = new double[7][motif3Total.length];
//	// double[][] n = new double[7][motif3Total.length];
//	// double[] zSum = new double[7];
//	// Arrays.fill(zSum, 0);
//	//
//	// double numberOfTotalMotifs = 0;
//	//
//	// for (int i = 0; i < motif3Total.length; i++) {
//	// numberOfTotalMotifs += motif3Total[i];
//	// }
//	//
//	// for (int i = 0; i < motif3Total.length; i++) {
//	// motif3Total[i] = (double) ((double) motif3Total[i] / ((double)
//	// numberOfTotalMotifs / (double) 100));
//	// }
//	//
//	// for (int i = 0; i < 7; i++) {
//	// for (int j = 0; j < motif3Total.length; j++) {
//	// z[i][j] = (double) ((double) motif3Role[i][j] - (double) motif3Total[j]);
//	// zSum[i] += (z[i][j] * z[i][j]);
//	// }
//	// }
//	// for (int i = 0; i < 7; i++) {
//	// for (int j = 0; j < motif3Total.length; j++) {
//	// if (nodesOfRole[i].size() > 0) {
//	// n[i][j] = z[i][j] / (double) Math.sqrt((double) zSum[i]);
//	// } else {
//	// n[i][j] = 0.0;
//	// }
//	// }
//	// }
//	//
//	// // write data to given output folder
//	// for (int i = 0; i < n.length; i++) {
//	// DataWriter.writeWithIndex(n[i], "SEQUENZ3_" + i, folder);
//	// }
//	// DataWriter.writeWithIndex(motif3RoleM[0], "MOTIF3_ROLE0M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[1], "MOTIF3_ROLE1M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[2], "MOTIF3_ROLE2M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[3], "MOTIF3_ROLE3M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[4], "MOTIF3_ROLE4M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[5], "MOTIF3_ROLE5M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[6], "MOTIF3_ROLE6M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[7], "MOTIF3_ROLE7M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[8], "MOTIF3_ROLE8M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[9], "MOTIF3_ROLE9M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[10], "MOTIF3_ROLE10M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[11], "MOTIF3_ROLE11M", folder);
//	// DataWriter.writeWithIndex(motif3RoleM[12], "MOTIF3_ROLE12M", folder);
//	// DataWriter.writeWithIndex(motif3Total, "MOTIF3_TOTAL", folder);
//	// DataWriter.writeWithIndex(motif3Role[0], "MOTIF3_ROLE0", folder);
//	// DataWriter.writeWithIndex(motif3Role[1], "MOTIF3_ROLE1", folder);
//	// DataWriter.writeWithIndex(motif3Role[2], "MOTIF3_ROLE2", folder);
//	// DataWriter.writeWithIndex(motif3Role[3], "MOTIF3_ROLE3", folder);
//	// DataWriter.writeWithIndex(motif3Role[4], "MOTIF3_ROLE4", folder);
//	// DataWriter.writeWithIndex(motif3Role[5], "MOTIF3_ROLE5", folder);
//	// DataWriter.writeWithIndex(motif3Role[6], "MOTIF3_ROLE6", folder);
//	// return false;
//	// } catch (IOException e) {
//	// return false;
//	// }
//	// }
//
//}

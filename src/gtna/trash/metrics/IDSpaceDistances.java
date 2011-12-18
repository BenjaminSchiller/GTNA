///* ===========================================================
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
// * IDSpace.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// *
// * Original Author: "Benjamin Schiller";
// * Contributors:    -;
// *
// * Changes since 2011-05-17
// * ---------------------------------------
// * 2011-07-01 : v1 (BS)
// *
// */
//package gtna.trash.metrics;
//
///**
// * @author "Benjamin Schiller"
// * 
// */
//
//// TODO reimplement IDSpaceDistances
//public class IDSpaceDistances {
//	// public class IDSpaceDistances extends MetricImpl implements Metric {
//	// private double[][] neighbors;
//	//
//	// private double[][] neighborsEDF;
//	//
//	// private double[][] successor;
//	//
//	// private double[][] successorEDF;
//	//
//	// public IDSpaceDistances() {
//	// super("ID_SPACE_DISTANCES");
//	// }
//	//
//	// private void initEmpty() {
//	// this.neighbors = new double[][] { new double[] { 0.0, 0.0 } };
//	// this.neighborsEDF = new double[][] { new double[] { 0.0, 0.0 } };
//	// this.successor = new double[][] { new double[] { 0.0, 0.0 } };
//	// this.successorEDF = new double[][] { new double[] { 0.0, 0.0 } };
//	// }
//	//
//	// public void computeData(Graph g, Network n, Hashtable<String, Metric> m)
//	// {
//	// if (!(g.nodes[0] instanceof RingNode)) {
//	// this.initEmpty();
//	// }
//	// double[] dists = this.computeDists(g);
//	// double stepsNeighbors = Config
//	// .getDouble("ID_SPACE_DISTANCES_NEIGHBORS_DISTRIBUTION_STEPS");
//	// this.neighbors = Statistics.probabilityDistribution(dists, 0,
//	// stepsNeighbors);
//	// this.neighborsEDF = Statistics.empiricalDistributionFunction(dists, 0,
//	// stepsNeighbors);
//	// double[] successorDists = IDSpaceDistances.computeSuccessorDists(g);
//	// double stepsSuccessors = Config
//	// .getDouble("ID_SPACE_DISTANCES_SUCCESSOR_DISTRIBUTION_STEPS");
//	// this.successor = Statistics.probabilityDistribution(successorDists, 0,
//	// stepsSuccessors);
//	// this.successorEDF = Statistics.empiricalDistributionFunction(
//	// successorDists, 0, stepsSuccessors);
//	// }
//	//
//	// private double[] computeDists(Graph g) {
//	// double[] dist = new double[g.edges];
//	// Edge[] e = g.edges();
//	// for (int i = 0; i < e.length; i++) {
//	// dist[i] = ((RingNode) e[i].src).getID().dist(
//	// ((RingNode) e[i].dst).getID());
//	// }
//	// Arrays.sort(dist);
//	// return dist;
//	// }
//	//
//	// public static double[] computeSuccessorDists(Graph g) {
//	// RingNode[] nodes = new RingNode[g.nodes.length];
//	// for (int i = 0; i < g.nodes.length; i++) {
//	// nodes[i] = (RingNode) g.nodes[i];
//	// }
//	// Arrays.sort(nodes, new SuccessorSorting());
//	// double[] dist = new double[nodes.length];
//	// for (int i = 0; i < nodes.length; i++) {
//	// dist[i] = nodes[i].getID().dist(
//	// nodes[(i + 1) % nodes.length].getID());
//	// }
//	// return dist;
//	// }
//	//
//	// public Value[] getValues(Value[] values) {
//	// return new Value[] {};
//	// }
//	//
//	// public boolean writeData(String folder) {
//	// DataWriter.writeWithoutIndex(this.neighbors,
//	// "ID_SPACE_DISTANCES_NEIGHBORS", folder);
//	// DataWriter.writeWithoutIndex(this.neighborsEDF,
//	// "ID_SPACE_DISTANCES_NEIGHBORS_EDF", folder);
//	// DataWriter.writeWithoutIndex(this.successor,
//	// "ID_SPACE_DISTANCES_SUCCESSOR", folder);
//	// DataWriter.writeWithoutIndex(this.successorEDF,
//	// "ID_SPACE_DISTANCES_SUCCESSOR_EDF", folder);
//	// return true;
//	// }
//	//
//	// private static class SuccessorSorting implements Comparator<RingNode> {
//	// public int compare(RingNode a, RingNode b) {
//	// if (a.getID().pos == b.getID().pos) {
//	// return 0;
//	// } else if (a.getID().pos < b.getID().pos) {
//	// return -1;
//	// } else {
//	// return 1;
//	// }
//	// }
//	// }
//
//}
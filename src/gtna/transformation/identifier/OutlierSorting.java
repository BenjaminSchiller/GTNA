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
 * OutlierSorting.java
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
package gtna.transformation.identifier;


// TODO reimplement OutlierSorting
public class OutlierSorting {
	// public class OutlierSorting extends TransformationImpl implements
	// Transformation {
	// // allowed distance to best Position
	// // approx. 50 * 1 / n
	// private double k = 0.005;
	// // variance of random distance to node it adjusts to
	// // approx. 1 / n
	// private double delta = 0.0001;
	// // norm of best position estimation
	// // 1: 1-Norm (Summe)
	// // -1: by clustering (anpassung an den mit den meisten gemeinsamen
	// nachbarn)
	// private int norm;
	// // maximal number of iterations (in graph size)
	// // => iterations = iterations * n
	// private int iterations;
	// // nodes of graph to sort
	// // private NodeImpl[] curNodes;
	// // saving bestPositions
	// // zwischenspeicherung der besten IDs
	// // private double[] bestPosition;
	// private RingID[] bestPosition;
	//
	// // public OutlierSorting() {
	// // super("OUTLIER_SORTING", new String[] {}, new String[] {});
	// // this.k = -1;
	// // this.delta = -1;
	// // this.norm = 1;
	// // this.iterations = -1;
	// // }
	//
	// // public OutlierSorting(double n) {
	// // super("OUTLIER_SORTING", new String[] {}, new String[] {});
	// // this.k = 50.0 / n;
	// // this.delta = 1.0 / n;
	// // this.norm = 1;
	// // this.iterations = 1000;
	// // }
	//
	// public OutlierSorting(int norm) {
	// super("OUTLIER_SORTING", new String[] { "NORM" },
	// new String[] { norm == 1 ? "1" : "c" });
	// this.k = -1;
	// this.delta = -1;
	// this.norm = norm;
	// this.iterations = -1;
	// }
	//
	// public OutlierSorting(double k, double delta, int norm, int maxIter) {
	// super("OUTLIER_SORTING", new String[] { "K", "DELTA", "NORM",
	// "ITERATIONS" }, new String[] { "" + k, "" + delta, "" + norm,
	// "" + maxIter });
	// this.k = k;
	// this.delta = delta;
	// this.norm = norm;
	// this.iterations = maxIter;
	// }
	//
	// public boolean applicable(Graph g) {
	// return g.nodes[0] instanceof RingNode;
	// }
	//
	// public Graph transform(Graph g) {
	// if (this.k == -1) {
	// this.k = 50.0 / g.nodes.length;
	// this.delta = 1.0 / g.nodes.length;
	// this.iterations = 1000;
	// }
	// this.sort(g.nodes);
	// g.computeDegrees();
	// g.computeEdges();
	// return g;
	// }
	//
	// private void sort(Node[] nodes) {
	// int count = 0;
	// this.bestPosition = new RingID[nodes.length];
	// Vector<IDNode> outliers = this.collectOutliers(nodes);
	// IDNode curOutlier, curNeighbor;
	// Random rand = new Random();
	// while (count < this.iterations * nodes.length && outliers.size() > 0) {
	// // get a random outlier and correct its position
	// curOutlier = outliers.remove(rand.nextInt(outliers.size()));
	// if (this.isOutlier(curOutlier)) {
	// RingID cid = new RingID(bestPosition[curOutlier.index()].pos
	// + rand.nextDouble() * this.delta);
	// ((RingNode) curOutlier).setID(cid);
	// count++;
	// // collect new outlier created by change
	// for (int i = 0; i < curOutlier.out().length; i++) {
	// curNeighbor = (IDNode) curOutlier.out()[i];
	// if (curNeighbor.dist(bestPosition[curNeighbor.index()]) <= k
	// && this.isOutlier(curNeighbor)) {
	// outliers.add(curNeighbor);
	// }
	// }
	// }
	// }
	//
	// }
	//
	// /**
	// * best positon for cur in current constellation in the one norm
	// *
	// * @param cur
	// * @return
	// */
	// private double getBestPosition1(IDNode cur) {
	// // in one norm: best position is either a neighbor's location
	// double best = Double.MAX_VALUE;
	// IDNode curNeighbor;
	// double sum;
	// double bestLoc = ((RingNode) cur).getID().pos;
	// for (int j = 0; j < cur.out().length; j++) {
	// curNeighbor = (IDNode) cur.out()[j];
	// sum = 0;
	// for (int m = 0; m < cur.out().length; m++) {
	// sum += curNeighbor.dist(((RingNode) cur.out()[m]).getID());
	// }
	// if (sum < best) {
	// best = sum;
	// bestLoc = ((RingNode) curNeighbor).getID().pos;
	// }
	// }
	// this.bestPosition[cur.index()] = new RingID(bestLoc);
	// return bestLoc;
	// }
	//
	// /**
	// * best positon for cur in current constellation in the one norm
	// *
	// * @param cur
	// * @return
	// */
	// private double getBestPositionC(IDNode cur) {
	// // in one norm: best position is either a neighbor's location
	// double best = -1;
	// IDNode curNeighbor;
	// int sum;
	// double bestLoc = ((RingNode) cur).getID().pos;
	// for (int j = 0; j < cur.out().length; j++) {
	// curNeighbor = (IDNode) cur.out()[j];
	// sum = 0;
	// for (int m = 0; m < cur.out().length; m++) {
	// if (((Node) curNeighbor).hasOut((Node) cur.out()[m])) {
	// sum++;
	// }
	// }
	// if (sum > best) {
	// best = sum;
	// bestLoc = ((RingNode) curNeighbor).getID().pos;
	// }
	// }
	// this.bestPosition[cur.index()] = new RingID(bestLoc);
	// return bestLoc;
	// }
	//
	// private Vector<IDNode> collectOutliers(Node[] nodes) {
	// Vector<IDNode> outliers = new Vector<IDNode>();
	// IDNode cur;
	// for (int i = 0; i < nodes.length; i++) {
	// cur = (IDNode) nodes[i];
	// if (this.isOutlier(cur)) {
	// outliers.add(cur);
	// }
	// }
	// return outliers;
	// }
	//
	// private boolean isOutlier(IDNode cur) {
	// double bestPos;
	// switch (norm) {
	// case 1:
	// bestPos = this.getBestPosition1(cur);
	// break;
	// case -1:
	// bestPos = this.getBestPositionC(cur);
	// break;
	// default:
	// throw new IllegalArgumentException("Norm " + norm
	// + " not yet implemented");
	// }
	// if (cur.dist(new RingID(bestPos)) > k) {
	// return true;
	// }
	// return false;
	// }
}

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
// * Bubblecast.java
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
//package gtna.trash.routing.bubblecast;
//
//
//// TODO reimplement Bubblecast
//public class Bubblecast {
//	// public class Bubblecast extends RoutingAlgorithmImpl implements
//	// RoutingAlgorithm {
//	// private int DR;
//	//
//	// private int DS;
//	//
//	// private int DTTL;
//	//
//	// private int QR;
//	//
//	// private int QS;
//	//
//	// private int QTTL;
//	//
//	// private int retries;
//	//
//	// private Hashtable<Node, Node>[] rt;
//	//
//	// public Bubblecast(int DR, int DS, int DTTL, int QR, int QS, int QTTL,
//	// int retries) {
//	// super("BUBBLECAST", new String[] { "D_R", "D_S", "D_TTL", "Q_R", "Q_S",
//	// "Q_TTL", "RETRIES" }, new String[] { "" + DR, "" + DS,
//	// "" + DTTL, "" + QR, "" + QS, "" + QTTL, "" + retries });
//	// this.DR = DR;
//	// this.DS = DS;
//	// this.QR = QR;
//	// this.QS = QS;
//	// this.retries = retries;
//	// }
//	//
//	// public static Bubblecast[] get(int[] DR, int DS, int DTTL, int QR, int
//	// QS,
//	// int QTTL, int r) {
//	// Bubblecast[] ra = new Bubblecast[DR.length];
//	// for (int i = 0; i < ra.length; i++) {
//	// ra[i] = new Bubblecast(DR[i], DS, DTTL, QR, QS, QTTL, r);
//	// }
//	// return ra;
//	// }
//	//
//	// public static Bubblecast[] get(int DR, int[] DS, int DTTL, int QR, int
//	// QS,
//	// int QTTL, int r) {
//	// Bubblecast[] ra = new Bubblecast[DS.length];
//	// for (int i = 0; i < ra.length; i++) {
//	// ra[i] = new Bubblecast(DR, DS[i], DTTL, QR, QS, QTTL, r);
//	// }
//	// return ra;
//	// }
//	//
//	// public static Bubblecast[] get(int DR, int DS, int DTTL, int[] QR, int
//	// QS,
//	// int QTTL, int r) {
//	// Bubblecast[] ra = new Bubblecast[QR.length];
//	// for (int i = 0; i < ra.length; i++) {
//	// ra[i] = new Bubblecast(DR, DS, DTTL, QR[i], QS, QTTL, r);
//	// }
//	// return ra;
//	// }
//	//
//	// public static Bubblecast[] get(int DR, int DS, int DTTL, int QR, int[]
//	// QS,
//	// int QTTL, int r) {
//	// Bubblecast[] ra = new Bubblecast[QS.length];
//	// for (int i = 0; i < ra.length; i++) {
//	// ra[i] = new Bubblecast(DR, DS, DTTL, QR, QS[i], QTTL, r);
//	// }
//	// return ra;
//	// }
//	//
//	// public static Bubblecast[] get(int DR, int DS, int DTTL, int QR, int QS,
//	// int QTTL, int[] r) {
//	// Bubblecast[] ra = new Bubblecast[r.length];
//	// for (int i = 0; i < ra.length; i++) {
//	// ra[i] = new Bubblecast(DR, DS, DTTL, QR, QS, QTTL, r[i]);
//	// }
//	// return ra;
//	// }
//	//
//	// public static Bubblecast[][] getXY(int DR[], int DS, int DTTL, int QR,
//	// int QS, int QTTL, int[] r) {
//	// Bubblecast[][] ra = new Bubblecast[r.length][DR.length];
//	// for (int i = 0; i < r.length; i++) {
//	// for (int j = 0; j < DR.length; j++) {
//	// ra[i][j] = new Bubblecast(DR[j], DS, DTTL, QR, QS, QTTL, r[i]);
//	// }
//	// }
//	// return ra;
//	// }
//	//
//	// public static Bubblecast[][] getXY(int DR, int DS, int DTTL, int[] QR,
//	// int QS, int QTTL, int[] r) {
//	// Bubblecast[][] ra = new Bubblecast[r.length][QR.length];
//	// for (int i = 0; i < r.length; i++) {
//	// for (int j = 0; j < QR.length; j++) {
//	// ra[i][j] = new Bubblecast(DR, DS, DTTL, QR[j], QS, QTTL, r[i]);
//	// }
//	// }
//	// return ra;
//	// }
//	//
//	// public static Bubblecast[][] getYX(int[] DR, int DS, int DTTL, int[] QR,
//	// int QS, int QTTL, int r) {
//	// Bubblecast[][] ra = new Bubblecast[DR.length][QR.length];
//	// for (int i = 0; i < DR.length; i++) {
//	// for (int j = 0; j < QR.length; j++) {
//	// ra[i][j] = new Bubblecast(DR[i], DS, DTTL, QR[j], QS, QTTL, r);
//	// }
//	// }
//	// return ra;
//	// }
//	//
//	// public static Bubblecast[][] getXY(int[] DR, int DS, int DTTL, int[] QR,
//	// int QS, int QTTL, int r) {
//	// Bubblecast[][] ra = new Bubblecast[QR.length][DR.length];
//	// for (int i = 0; i < DR.length; i++) {
//	// for (int j = 0; j < QR.length; j++) {
//	// ra[j][i] = new Bubblecast(DR[i], DS, DTTL, QR[j], QS, QTTL, r);
//	// }
//	// }
//	// return ra;
//	// }
//	//
//	// public boolean applicable(Node[] nodes) {
//	// return true;
//	// }
//	//
//	// @SuppressWarnings("unchecked")
//	// public void init(Node[] nodes) {
//	// Random rand = new Random(System.currentTimeMillis());
//	// this.rt = new Hashtable[nodes.length];
//	// for (int i = 0; i < this.rt.length; i++) {
//	// this.rt[i] = new Hashtable<Node, Node>();
//	// }
//	// for (int i = 0; i < nodes.length; i++) {
//	// this.store(nodes[i], nodes[i], nodes[i], nodes[i], this.DR, rand,
//	// this.DTTL);
//	// }
//	// }
//	//
//	// private void store(Node node, Node from, Node DSt, Node over, int r,
//	// Random rand, int ttl) {
//	// if (node.index() != DSt.index()
//	// && !this.rt[node.index()].containsKey(DSt)) {
//	// this.rt[node.index()].put(DSt, over);
//	// r--;
//	// }
//	// // if (ttl == 0) {
//	// // System.out.println("TTL @ R");
//	// // return;
//	// // }
//	// int[] split = this.split(r, Math.min(this.DS, node.out().length));
//	// Node[] random = this.randomNeighbors(node, from, split.length, rand);
//	// for (int i = 0; i < random.length; i++) {
//	// this.store(random[i], node, DSt, node, split[i], rand, ttl - 1);
//	// }
//	// }
//	//
//	// public Route randomRoute(Node[] nodes, Node src, Random rand) {
//	// Node DSt = nodes[rand.nextInt(nodes.length)];
//	// while (DSt.index() == src.index()) {
//	// DSt = nodes[rand.nextInt(nodes.length)];
//	// }
//	// int length = Integer.MAX_VALUE;
//	// int r = -1;
//	// while (length == Integer.MAX_VALUE && r < this.retries) {
//	// length = this.query(src, src, DSt, this.QR, rand, this.QTTL);
//	// r++;
//	// }
//	// if (length == Integer.MAX_VALUE) {
//	// return null;
//	// }
//	// // return new double[length + 1];
//	// return null;
//	// }
//	//
//	// private int query(Node node, Node from, Node DSt, int r, Random rand,
//	// int ttl) {
//	// if (node.index() == DSt.index()) {
//	// return 0;
//	// }
//	// if (this.rt[node.index()].containsKey(DSt)) {
//	// return 1 + this.query(this.rt[node.index()].get(DSt), node, DSt,
//	// r - 1, rand, ttl - 1);
//	// }
//	// // if (ttl == 0) {
//	// // System.out.println("TTL @ Q");
//	// // return Integer.MAX_VALUE;
//	// // }
//	// int[] split = this.split(r - 1, Math.min(this.QS, node.out().length));
//	// Node[] random = this.randomNeighbors(node, from, split.length, rand);
//	// int min = Integer.MAX_VALUE;
//	// for (int i = 0; i < random.length; i++) {
//	// int route = this.query(random[i], node, DSt, split[i], rand,
//	// ttl - 1);
//	// if (route < min) {
//	// min = route;
//	// }
//	// }
//	// return min;
//	// }
//	//
//	// private Node[] randomNeighbors(Node node, Node from, int number, Random
//	// rand) {
//	// int Out = node.out().length;
//	// if (node.hasOut((Node) from)) {
//	// Out--;
//	// }
//	// number = Math.min(number, Out);
//	// HashSet<Node> set = new HashSet<Node>(number);
//	// Node[] out = node.out();
//	// while (set.size() < number) {
//	// Node random = out[rand.nextInt(out.length)];
//	// if (!set.contains(random) && random.index() != from.index()) {
//	// set.add(random);
//	// }
//	// }
//	// return Util.toNodeArray(set);
//	// }
//	//
//	// private int[] split(int r, int s) {
//	// if (r <= 0) {
//	// return new int[] {};
//	// }
//	// if (s <= 0) {
//	// return new int[] {};
//	// }
//	// int a = (int) Math.floor((double) r / (double) s);
//	// if (a == 0) {
//	// int[] split = new int[r];
//	// for (int i = 0; i < split.length; i++) {
//	// split[i] = 1;
//	// }
//	// return split;
//	// }
//	// int[] split = new int[s];
//	// for (int i = 0; i < split.length; i++) {
//	// split[i] = a;
//	// }
//	// try {
//	// for (int i = 0; i < (r % s); i++) {
//	// split[i] += 1;
//	// }
//	// } catch (ArithmeticException e) {
//	// System.out.println("s = " + s);
//	// System.out.println("r = " + r);
//	// System.out.println("split = " + split.length);
//	// e.printStackTrace();
//	// }
//	// return split;
//	// }
//}

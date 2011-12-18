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
// * SwappingSorting.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// * 
// * Original Author: Stefanie Roos;
// * Contributors:    Benjamin Schiller;
// * 
// * Changes since 2011-05-17
// * ---------------------------------------
// * 2011-06-05 : implemented Transformation interface correctly (BS)
// * 2011-06-05 : exchanged use of FreenetNode with RingNode (BS)
// * 
// */
//package gtna.trash.transformation.identifier;
//
//
//// TODO reimplement SwappingSorting
//public class SwappingSorting {
//	// public class SwappingSorting extends TransformationImpl implements
//	// Transformation {
//	//
//	// // three versions of swapping:
//	// // 0 = select partner randomly
//	// // 1 = select partner by random walk of length log_2(N) / 2
//	// // 2 = only neighbors
//	// private int version;
//	// // nr of iterations (per node)
//	// private int iterations;
//	//
//	// public SwappingSorting(int version, int iterations) {
//	// super("SWAPPING_SORTING", new String[] { "VERSION", "ITERATIONS" },
//	// new String[] { "" + version, "" + iterations });
//	// this.version = version;
//	// this.iterations = iterations;
//	// }
//	//
//	// public boolean applicable(Graph g) {
//	// return g.nodes[0] instanceof RingNode;
//	// }
//	//
//	// public Graph transform(Graph g) {
//	// Random rand = new Random(System.currentTimeMillis());
//	// RingNode initiator, partner;
//	// RingID placeHolder;
//	// // int steps = (int) Math
//	// // .floor(Math.log(g.nodes.length) / Math.log(2) / 2);
//	// for (int i = 0; i < iterations * g.nodes.length; i++) {
//	// initiator = (RingNode) g.nodes[rand.nextInt(g.nodes.length)];
//	// switch (version) {
//	// case 0:
//	// partner = getPartnerRandomly(g, rand);
//	// break;
//	// case 1:
//	// partner = getPartnerByRandomWalk(initiator, rand);
//	// break;
//	// case 2:
//	// partner = getPartnerNeighbor(initiator, rand);
//	// break;
//	// default:
//	// throw new IllegalArgumentException(
//	// "This version of choosing swapping partners is unknown!");
//	// }
//	//
//	// // compute switching coefficient
//	// Node[] friends = initiator.out();
//	// double before = 1;
//	// double after = 1;
//	// for (int j = 0; j < friends.length; j++) {
//	// before = before
//	// * initiator.dist(((RingNode) friends[j]).getID());
//	// if (!friends[j].equals(partner)) {
//	// after = after
//	// * partner.dist(((RingNode) friends[j]).getID());
//	// } else {
//	// after = after * initiator.dist(partner.getID());
//	// }
//	// }
//	// friends = partner.out();
//	// for (int j = 0; j < friends.length; j++) {
//	// before = before * partner.dist(((RingNode) friends[j]).getID());
//	// if (!friends[j].equals(partner)) {
//	// after = after
//	// * initiator.dist(((RingNode) friends[j]).getID());
//	// } else {
//	// after = after * initiator.dist(partner.getID());
//	// }
//	// }
//	//
//	// // decide if a switch is performed
//	// if (rand.nextDouble() < before / after) {
//	// placeHolder = initiator.getID();
//	// initiator.setID(partner.getID());
//	// partner.setID(placeHolder);
//	// }
//	//
//	// }
//	// return g;
//	// }
//	//
//	// private RingNode getPartnerRandomly(Graph g, Random rand) {
//	// return (RingNode) g.nodes[rand.nextInt(g.nodes.length)];
//	// }
//	//
//	// private RingNode getPartnerByRandomWalk(RingNode cur, Random rand) {
//	// for (int i = 1; i <= 6; i++) {
//	// cur = (RingNode) cur.out()[rand.nextInt(cur.out().length)];
//	// }
//	// return cur;
//	// }
//	//
//	// private RingNode getPartnerNeighbor(RingNode cur, Random rand) {
//	// return (RingNode) cur.out()[rand.nextInt(cur.out().length)];
//	// }
//	//
//	// public int getCount() {
//	// return iterations;
//	// }
//}

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
// * Greedy.java
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
//package gtna.trash.routing.greedy;
//
//
//// TODO reimplement Greedy
//public class Greedy {
//	// public class Greedy extends RoutingAlgorithmImpl implements
//	// RoutingAlgorithm {
//	// public Greedy() {
//	// super("GREEDY", new String[] {}, new String[] {});
//	// }
//	//
//	// public boolean applicable(Node[] nodes) {
//	// return nodes[0] instanceof IDNode;
//	// }
//	//
//	// public void init(Node[] nodes) {
//	// return;
//	// }
//	//
//	// public Route randomRoute(Node[] nodes, Node src, Random rand) {
//	// IDNode SRC = (IDNode) src;
//	// Identifier DEST = ((IDNode) nodes[rand.nextInt(nodes.length)])
//	// .randomID(rand, nodes);
//	// while (SRC.contains(DEST)) {
//	// DEST = ((IDNode) nodes[rand.nextInt(nodes.length)]).randomID(rand,
//	// nodes);
//	// }
//	// return route(SRC, SRC, DEST, new RouteImpl());
//	// }
//	//
//	// public static Route route(IDNode src, IDNode current, Identifier dest,
//	// Route route) {
//	// route.add((Node) current);
//	// if (current.contains(dest)) {
//	// route.setSuccess(true);
//	// return route;
//	// }
//	//
//	// Node[] out = current.out();
//	// double minDist = current.dist(dest);
//	// IDNode nextHop = null;
//	// for (int i = 0; i < out.length; i++) {
//	// IDNode Out = (IDNode) out[i];
//	// double dist = Out.dist(dest);
//	// if (dist < minDist) {
//	// minDist = dist;
//	// nextHop = Out;
//	// }
//	// }
//	// if (nextHop == null) {
//	// route.setSuccess(false);
//	// return route;
//	// }
//	// route.incMessages();
//	// return route(src, nextHop, dest, route);
//	// }
//}

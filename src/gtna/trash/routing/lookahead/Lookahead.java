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
 * Lookahead.java
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
package gtna.trash.routing.lookahead;


// TODO reimplement Lookahead

public class Lookahead {
	// public class Lookahead extends RoutingAlgorithmImpl implements
	// RoutingAlgorithm {
	// public Lookahead() {
	// super("LOOKAHEAD", new String[] {}, new String[] {});
	// }
	//
	// public boolean applicable(Node[] nodes) {
	// return nodes[0] instanceof IDNode;
	// }
	//
	// public void init(Node[] nodes) {
	//
	// }
	//
	// public Route randomRoute(Node[] nodes, Node src, Random rand) {
	// IDNode SRC = (IDNode) src;
	// Identifier DST = SRC.randomID(rand, nodes);
	// while (SRC.contains(DST)) {
	// DST = SRC.randomID(rand, nodes);
	// }
	// return route(SRC, SRC, DST, new RouteImpl(), new HashSet<IDNode>());
	// }
	//
	// public static Route route(IDNode src, IDNode current, Identifier dest,
	// Route route, Set<IDNode> seen) {
	// seen.add(current);
	// route.add((Node) current);
	// if (current.contains(dest)) {
	// route.setSuccess(true);
	// return route;
	// }
	//
	// Node[] out = current.out();
	// double minDist = current.dist(dest);
	// IDNode nextHop = null;
	// int outIndex = -1;
	// for (int i = 0; i < out.length; i++) {
	// IDNode Out = (IDNode) out[i];
	// if (Out.dist(dest) < minDist && !seen.contains(Out)) {
	// minDist = Out.dist(dest);
	// nextHop = Out;
	// outIndex = i;
	// }
	// Node[] lookahead = Out.out();
	// for (int j = 0; j < lookahead.length; j++) {
	// IDNode Lookahead = (IDNode) lookahead[j];
	// if (Lookahead.dist(dest) < minDist && !seen.contains(Out)) {
	// minDist = Lookahead.dist(dest);
	// nextHop = Out;
	// outIndex = i;
	// }
	// }
	// }
	// for (int i = 0; i < outIndex; i++) {
	// IDNode Out = (IDNode) out[i];
	// if (Out.dist(dest) <= minDist && seen.contains(Out)) {
	// route.incMessages(2);
	// break;
	// }
	// Node[] lookahead = Out.out();
	// for (int j = 0; j < lookahead.length; j++) {
	// IDNode Lookahead = (IDNode) lookahead[j];
	// if (Lookahead.dist(dest) <= minDist && seen.contains(Out)) {
	// route.incMessages(2);
	// break;
	// }
	// }
	// }
	// if (nextHop == null) {
	// route.setSuccess(false);
	// return route;
	// }
	// route.incMessages();
	// return route(src, nextHop, dest, route, seen);
	// }
}

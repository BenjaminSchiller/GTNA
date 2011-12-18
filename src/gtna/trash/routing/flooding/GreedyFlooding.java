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
// * GreedyFlooding.java
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
//package gtna.trash.routing.flooding;
//
//
//// TODO reimplement GreedyFlooding
//public class GreedyFlooding {
//	// public class GreedyFlooding extends RoutingAlgorithmImpl implements
//	// RoutingAlgorithm {
//	//
//	// public static final String FLOODING_FIRST = "FF";
//	//
//	// public static final String GREEDY_FIRST = "GF";
//	//
//	// private int ttl;
//	//
//	// private String mode;
//	//
//	// public GreedyFlooding(int ttl, String mode) {
//	// super("GREEDY_FLOODING", new String[] { "TTL", "MODE" }, new String[] {
//	// "" + ttl, mode });
//	// this.ttl = ttl;
//	// this.mode = mode;
//	// }
//	//
//	// public boolean applicable(Node[] nodes) {
//	// return nodes[0] instanceof IDNode;
//	// }
//	//
//	// public void init(Node[] nodes) {
//	//
//	// }
//	//
//	// public Route randomRoute(Node[] nodes, Node src, Random rand) {
//	// IDNode s = (IDNode) src;
//	// Identifier dest = s.randomID(rand, nodes);
//	// // return this.route(s, dest, nodes.length);
//	// return null;
//	// }
//	//
//	// private double[] route(IDNode start, Identifier dest, int nodes) {
//	// int messages = 0;
//	// boolean[] seen = new boolean[nodes];
//	// Hashtable<IDNode, ArrayList<IDNode>> paths = new Hashtable<IDNode,
//	// ArrayList<IDNode>>();
//	// ArrayList<IDNode> sp = new ArrayList<IDNode>();
//	// sp.add(start);
//	// paths.put(start, sp);
//	// double[] prog = null;
//	// LinkedList<IDNode> queue = new LinkedList<IDNode>();
//	// queue.add(start);
//	// seen[start.index()] = true;
//	// while (!queue.isEmpty()) {
//	// IDNode current = queue.pop();
//	// ArrayList<IDNode> currentPath = paths.get(current);
//	// if (current.contains(dest)) {
//	// prog = this.toProg(currentPath, dest);
//	// }
//	// int hop = currentPath.size() - 1;
//	// if (hop < this.ttl && GREEDY_FIRST.equals(this.mode)
//	// || hop >= this.ttl && FLOODING_FIRST.equals(this.mode)) {
//	// // GREEDY
//	// double min = current.dist(dest);
//	// IDNode nextHop = null;
//	// for (int i = 0; i < current.out().length; i++) {
//	// IDNode out = (IDNode) current.out()[i];
//	// if (out.dist(dest) < min) {
//	// min = out.dist(dest);
//	// nextHop = out;
//	// }
//	// }
//	// if (nextHop != null) {
//	// ArrayList<IDNode> p = new ArrayList<IDNode>();
//	// p.addAll(paths.get(current));
//	// p.add(nextHop);
//	// paths.put(nextHop, p);
//	// seen[nextHop.index()] = true;
//	// queue.push(nextHop);
//	// messages++;
//	// }
//	// } else if (hop >= this.ttl && GREEDY_FIRST.equals(this.mode)
//	// || hop < this.ttl && FLOODING_FIRST.equals(this.mode)) {
//	// // FLOODING
//	// for (int i = 0; i < current.out().length; i++) {
//	// IDNode out = (IDNode) current.out()[i];
//	// boolean closer = out.dist(dest) < current.dist(dest);
//	// if (closer && !seen[out.index()]) {
//	// ArrayList<IDNode> p = new ArrayList<IDNode>();
//	// p.addAll(paths.get(current));
//	// p.add(out);
//	// paths.put(out, p);
//	// seen[out.index()] = true;
//	// queue.push(out);
//	// messages++;
//	// } else if (closer) {
//	// messages++;
//	// }
//	// }
//	// }
//	// }
//	// return prog;
//	// }
//	//
//	// private double[] toProg(ArrayList<IDNode> path, Identifier dest) {
//	// double start = path.get(0).dist(dest);
//	// double[] prog = new double[path.size()];
//	// for (int i = 0; i < path.size() - 1; i++) {
//	// prog[i] = path.get(i).dist(dest) / start;
//	// }
//	// prog[prog.length - 1] = 0.0;
//	// return prog;
//	// }
//
//}

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
 * UnitDisc.java
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
package gtna.networks.model;


public class UnitDisc {
	// private int AREA_WIDTH;
	//
	// private int AREA_HEIGHT;
	//
	// private int RADIUS_MIN;
	//
	// private int RADIUS_MAX;
	//
	// public UnitDisc(int nodes, int AREA_WIDTH, int AREA_HEIGHT, int
	// RADIUS_MIN,
	// int RADIUS_MAX, RoutingAlgorithm ra, Transformation[] t) {
	// super("UNIT_DISC", nodes, new String[] { "AREA_WIDTH", "AREA_HEIGHT",
	// "RADIUS_MIN", "RADIUS_MAX" }, new String[] { "" + AREA_WIDTH,
	// "" + AREA_HEIGHT, "" + RADIUS_MIN, "" + RADIUS_MAX }, ra, t);
	// this.AREA_WIDTH = AREA_WIDTH;
	// this.AREA_HEIGHT = AREA_HEIGHT;
	// this.RADIUS_MIN = RADIUS_MIN;
	// this.RADIUS_MAX = RADIUS_MAX;
	// }
	//
	// public Graph generate() {
	// Timer timer = new Timer();
	// Random rand = new Random(System.currentTimeMillis());
	// UDNode[] nodes = new UDNode[this.nodes()];
	// for (int i = 0; i < nodes.length; i++) {
	// double x = rand.nextDouble() * this.AREA_WIDTH;
	// double y = rand.nextDouble() * this.AREA_HEIGHT;
	// double radius = this.RADIUS_MIN + rand.nextDouble()
	// * (this.RADIUS_MAX - this.RADIUS_MIN);
	// nodes[i] = new UDNode(i, x, y, radius, nodes);
	// }
	// Edges edges = new Edges(nodes, 100);
	// for (int i = 0; i < nodes.length; i++) {
	// for (int j = i + 1; j < nodes.length; j++) {
	// if (nodes[i].reaches(nodes[j])) {
	// edges.add(nodes[i], nodes[j]);
	// edges.add(nodes[j], nodes[i]);
	// }
	// }
	// }
	// edges.fill();
	// timer.end();
	// Graph graph = new Graph(this.description(), nodes, timer);
	// return graph;
	// }
	//
	// private class UDNode extends GridNode {
	// private double x;
	//
	// private double y;
	//
	// private double radius;
	//
	// private UDNode(int index, double x, double y, double radius,
	// UDNode[] nodes) {
	// super(index, new GridID(new double[] { x, y }));
	// this.x = x;
	// this.y = y;
	// this.radius = radius;
	// }
	//
	// private boolean reaches(UDNode node) {
	// double xd = this.x - node.x;
	// double yd = this.y - node.y;
	// double d = Math.sqrt(xd * xd + yd * yd);
	// return d <= this.radius && d <= node.radius;
	// }
	// }
}

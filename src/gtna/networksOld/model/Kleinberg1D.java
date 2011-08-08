/* ===========================================================
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
 * Kleinberg1D.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networksOld.model;


/**
 * @author "Benjamin Schiller"
 * 
 */
// TODO reimplement Kleinberg1D
public class Kleinberg1D {
	// public class Kleinberg1D extends NetworkImpl implements Network {
	// private int SHORT_RANGE_CONTACTS;
	//
	// private int LONG_RANGE_CONTACTS;
	//
	// private double CLUSTERING_EXPONENT;
	//
	// private boolean BIDIRECTIONAL;
	//
	// public Kleinberg1D(int nodes, int SHORT_RANGE_CONTACTS,
	// int LONG_RANGE_CONTACTS, double CLUSTERING_EXPONENT,
	// boolean BIDIRECTIONAL, RoutingAlgorithm ra, Transformation[] t) {
	// super("KLEINBERG_1D", nodes,
	// new String[] { "SHORT_RANGE_CONTACTS", "LONG_RANGE_CONTACTS",
	// "CLUSTERING_EXPONENT", "BIDIRECTIONAL" }, new String[] {
	// "" + SHORT_RANGE_CONTACTS, "" + LONG_RANGE_CONTACTS,
	// "" + CLUSTERING_EXPONENT, "" + BIDIRECTIONAL }, ra, t);
	// this.SHORT_RANGE_CONTACTS = SHORT_RANGE_CONTACTS;
	// this.LONG_RANGE_CONTACTS = LONG_RANGE_CONTACTS;
	// this.CLUSTERING_EXPONENT = CLUSTERING_EXPONENT;
	// this.BIDIRECTIONAL = BIDIRECTIONAL;
	// }
	//
	// public Graph generate() {
	// Timer timer = new Timer();
	// Random rand = new Random();
	// RingNode[] nodes = new RingNode[this.nodes()];
	// for (int i = 0; i < nodes.length; i++) {
	// // double id = (double) i / (double) this.nodes();
	// double id = rand.nextDouble();
	// nodes[i] = new RingNode(i, id);
	// }
	//
	// Edges edges = new Edges(nodes, this.nodes()
	// * (this.SHORT_RANGE_CONTACTS * 2 + this.LONG_RANGE_CONTACTS));
	// // short-distance links
	// for (int i = 0; i < nodes.length; i++) {
	// RingNode src = nodes[i];
	// for (int j = 1; j <= this.SHORT_RANGE_CONTACTS; j++) {
	// RingNode dst = nodes[(i + j) % nodes.length];
	// edges.add(src, dst);
	// edges.add(dst, src);
	// }
	// }
	// // long-distance links
	// for (int i = 0; i < nodes.length; i++) {
	// double[] prob = new double[nodes.length];
	// double sum = 0;
	// for (int j = 0; j < nodes.length; j++) {
	// if (i != j) {
	// sum += Math.pow(nodes[i].getID().dist(nodes[j].getID()),
	// -this.CLUSTERING_EXPONENT);
	// }
	// }
	// for (int j = 0; j < nodes.length; j++) {
	// prob[j] = Math.pow(nodes[i].getID().dist(nodes[j].getID()),
	// -this.CLUSTERING_EXPONENT)
	// / sum;
	// }
	// this.generateLongRangeContacts(nodes[i], nodes, edges, prob, rand);
	// }
	// edges.fill();
	//
	// timer.end();
	// return new Graph(this.description(), nodes, timer);
	// }
	//
	// private void generateLongRangeContacts(RingNode node, RingNode[] nodes,
	// Edges edges, double[] prob, Random rand) {
	// double sum = 0;
	// for (int i = 0; i < nodes.length; i++) {
	// sum += node.getID().dist(nodes[i].getID());
	// }
	// int found = 0;
	// while (found < this.LONG_RANGE_CONTACTS) {
	// RingNode contact = nodes[rand.nextInt(nodes.length)];
	// if (node.index() == contact.index()) {
	// continue;
	// }
	// if (prob[contact.index()] >= rand.nextDouble()) {
	// if (edges.contains(node.index(), contact.index())) {
	// continue;
	// }
	// edges.add(node, contact);
	// if (this.BIDIRECTIONAL) {
	// edges.add(contact, node);
	// }
	// found++;
	// }
	// }
	// }
}

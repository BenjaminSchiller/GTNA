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
// * RingNode.java
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
//package gtna.trash.routing.node;
//
//
//// TODO remove RingNode
//@Deprecated
//public class RingNode {
//	// public class RingNode extends Node implements RegistrationNode {
//	// private RingID id;
//	//
//	// private HashSet<String> register;
//	//
//	// public RingNode(int index, double pos) {
//	// super(index);
//	// this.id = new RingID(pos);
//	// this.register = new HashSet<String>();
//	// }
//	//
//	// public void register(Identifier id) {
//	// if (!this.id.equals(id)) {
//	// this.register.add(((RingID) id).toString());
//	// }
//	// }
//	//
//	// public int registeredItems() {
//	// return this.register.size();
//	// }
//	//
//	// public boolean contains(Identifier id) {
//	// return this.id.equals(id)
//	// || this.register.contains(((RingID) id).toString());
//	// }
//	//
//	// public double dist(Identifier id) {
//	// return this.id.dist(id);
//	// }
//	//
//	// public double dist(IDNode node) {
//	// return this.id.dist(((RingNode) node).id);
//	// }
//	//
//	// public Identifier randomID(Random rand, Node[] nodes) {
//	// int index = rand.nextInt(nodes.length);
//	// while (index == this.index()) {
//	// index = rand.nextInt(nodes.length);
//	// }
//	// return ((RingNode) nodes[index]).id;
//	// }
//	//
//	// public RingID getID() {
//	// return this.id;
//	// }
//	//
//	// public void setID(RingID id) {
//	// this.id = id;
//	// }
//	//
//	// public String toString() {
//	// return this.index() + "_" + this.id.toString();
//	// }
//	//
//	// public static RingNode parse(String str){
//	// String[] parts = str.split("_");
//	// int index = Integer.parseInt(parts[0]);
//	// RingID id = RingID.parse(parts[1]);
//	// return new RingNode(index, id.pos);
//	// }
//	//
//	// public static int parseIndex(String str){
//	// String[] parts = str.split("_");
//	// return Integer.parseInt(parts[0]);
//	// }
//}

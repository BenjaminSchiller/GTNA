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
 * RingNodeMultiR.java
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
package gtna.routing.node;


// TODO reimplement RingNodeMultiR
@Deprecated
public class RingNodeMultiR {
	// public class RingNodeMultiR extends Node implements RegistrationNode {
	// private RingIDMultiR[] ids;
	//
	// private HashSet<String> register;
	//
	// public RingNodeMultiR(int index, double[] pos) {
	// super(index);
	// this.ids = new RingIDMultiR[pos.length];
	// for (int i = 0; i < pos.length; i++) {
	// this.ids[i] = new RingIDMultiR(pos[i], i);
	// }
	// this.register = new HashSet<String>();
	// }
	//
	// public void register(Identifier id) {
	// if (!this.ids[((RingIDMultiR) id).reality].equals(id)) {
	// this.register.add(id.toString());
	// }
	// }
	//
	// public int registeredItems() {
	// return this.register.size();
	// }
	//
	// public boolean contains(Identifier id) {
	// return this.ids[((RingIDMultiR) id).reality].equals(id)
	// || this.register.contains(id.toString());
	// }
	//
	// public double dist(Identifier id) {
	// return this.ids[((RingIDMultiR) id).reality].dist(id);
	// }
	//
	// public double dist(IDNode node) {
	// return this.ids[0].dist(((RingNodeMultiR) node).ids[0]);
	// }
	//
	// public Identifier randomID(Random rand, Node[] nodes) {
	// return this.randomID(rand, nodes, rand.nextInt(this.ids.length));
	// }
	//
	// public Identifier randomID(Random rand, Node[] nodes, int reality) {
	// int index = rand.nextInt(nodes.length);
	// while (this.index() == index) {
	// index = rand.nextInt(nodes.length);
	// }
	// return ((RingNodeMultiR) nodes[index]).ids[reality];
	// }
	//
	// public RingIDMultiR[] getIDs() {
	// return this.ids;
	// }
	//
	// public void setID(RingIDMultiR id) {
	// this.ids[id.reality] = id;
	// }
	//
	// public String toString() {
	// return this.index() + " @ " + this.ids[0].toString();
	// }

}

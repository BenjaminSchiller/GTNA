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
 * MinDegree.java
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
package gtna.trash.transformation.degree;


/**
 * Transforms the given graph so that every node has the given minimum in- and
 * out-degree by adding random from and to nodes with a smaller in- or
 * out-degree. Note that these links are created at random so the degree and
 * connectivity of other nodes is affected as well.
 * 
 * @author benni
 * 
 */
// TODO reimplement MinDegree
public class MinDegree {
	// public class MinDegree extends TransformationImpl implements
	// Transformation {
	// private int minIn;
	//
	// private int minOut;
	//
	// private boolean bidirectional;
	//
	// public MinDegree(int minIn, int minOut, boolean bidirectional) {
	// super("MIN_DEGREE",
	// new String[] { "MIN_IN", "MIN_OUT", "BIDIRECTIONAL" },
	// new String[] { "" + minIn, "" + minOut, "" + bidirectional });
	// this.minIn = minIn;
	// this.minOut = minOut;
	// this.bidirectional = bidirectional;
	// }
	//
	// public boolean applicable(Graph g) {
	// return true;
	// }
	//
	// public Graph transform(Graph g) {
	// ArrayList<Node> mins = new ArrayList<Node>();
	// for (int i = 0; i < g.nodes.length; i++) {
	// if (g.nodes[i].in().length < this.minIn
	// || g.nodes[i].out().length < this.minOut) {
	// mins.add(g.nodes[i]);
	// }
	// }
	// Random rand = new Random(System.currentTimeMillis());
	// while (mins.size() > 0) {
	// int index = rand.nextInt(mins.size());
	// Node current = mins.get(index);
	// if (current.in().length >= this.minIn
	// && current.out().length >= this.minOut) {
	// mins.remove(index);
	// continue;
	// }
	// if (this.bidirectional) {
	// Node randLink = g.nodes[rand.nextInt(g.nodes.length)];
	// while (current.hasIn(randLink) || current.hasOut(randLink)
	// || current.index() == randLink.index()) {
	// randLink = g.nodes[rand.nextInt(g.nodes.length)];
	// }
	// current.addIn(randLink);
	// current.addOut(randLink);
	// randLink.addIn(current);
	// randLink.addOut(current);
	// } else if (current.in().length < this.minIn) {
	// Node randLink = g.nodes[rand.nextInt(g.nodes.length)];
	// while (current.hasIn(randLink)
	// || current.index() == randLink.index()) {
	// randLink = g.nodes[rand.nextInt(g.nodes.length)];
	// }
	// current.addIn(randLink);
	// randLink.addOut(current);
	// } else if (current.out().length < this.minOut) {
	// Node randLink = g.nodes[rand.nextInt(g.nodes.length)];
	// while (current.hasOut(randLink)
	// || current.index() == randLink.index()) {
	// randLink = g.nodes[rand.nextInt(g.nodes.length)];
	// }
	// current.addOut(randLink);
	// randLink.addIn(current);
	// }
	// }
	// return new Graph(g.name, g.nodes, g.timer);
	// }

}

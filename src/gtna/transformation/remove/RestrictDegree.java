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
 * RestrictDegree.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.remove;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * @author stef remove edges from nodes that exceed a maximal degree
 */
public class RestrictDegree extends RemoveEdges {
	int max;
	Type type;

	public static enum Type {
		IN, OUT, TOTALUNDIRECTED, TOTALDIRECTED
	}

	/**
	 * @param key
	 * @param parameters
	 */
	public RestrictDegree(int max, Type type) {
		super("RESTRICT_DEGREE", new Parameter[] {
				new IntParameter("MAX", max),
				new StringParameter("TYPE", type.toString()) });
		this.max = max;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.subGraphs.RemoveEdges#getEdgeSet(gtna.graph.Graph)
	 */
	@Override
	public HashMap<Integer, Vector<Integer>> getEdgeSet(Graph g) {
		Node[] nodes = g.getNodes();
		HashMap<Integer, Vector<Integer>> map = new HashMap<Integer, Vector<Integer>>(
				nodes.length);
		Vector<Integer> cur;
		Vector<Integer> neighs, vec;
		Random rand = new Random();
		for (int i = 0; i < nodes.length; i++) {
			if (this.type == Type.OUT) {
				if (nodes[i].getOutDegree() > this.max) {
					neighs = new Vector<Integer>(nodes[i].getOutDegree());
					for (int j = 0; j < nodes[i].getOutDegree(); j++) {
						neighs.add(nodes[i].getOutgoingEdges()[j]);
					}
					cur = map.get(i);
					if (cur == null) {
						cur = new Vector<Integer>();
						map.put(i, cur);
					}
					for (int j = 0; j < nodes[i].getOutDegree() - this.max; j++) {
						int removed = neighs
								.remove(rand.nextInt(neighs.size()));
						cur.add(removed);
					}
				}
			}
			if (this.type == Type.IN) {
				if (nodes[i].getInDegree() > this.max) {
					neighs = new Vector<Integer>(nodes[i].getInDegree());
					for (int j = 0; j < nodes[i].getInDegree(); j++) {
						neighs.add(nodes[i].getIncomingEdges()[j]);
					}
					for (int j = 0; j < nodes[i].getInDegree() - this.max; j++) {
						int removed = neighs
								.remove(rand.nextInt(neighs.size()));
						vec = map.get(removed);
						if (vec == null) {
							vec = new Vector<Integer>();
							map.put(removed, vec);
						}
						vec.add(i);
					}
				}
			}
			if (this.type == Type.TOTALUNDIRECTED) {
				if (nodes[i].getDegree() > this.max) {
					neighs = new Vector<Integer>(nodes[i].getInDegree());
					for (int j = 0; j < nodes[i].getInDegree(); j++) {
						neighs.add(nodes[i].getIncomingEdges()[j]);
					}
					cur = map.get(i);
					if (cur == null) {
						cur = new Vector<Integer>();
						map.put(i, cur);
					}
					for (int j = 0; j < (nodes[i].getDegree() - this.max) / 2; j++) {
						int removed = neighs
								.remove(rand.nextInt(neighs.size()));
						cur.add(removed);
						vec = map.get(removed);
						if (vec == null) {
							vec = new Vector<Integer>();
							map.put(removed, vec);
						}
						vec.add(i);
					}
				}
			}
			if (this.type == Type.TOTALDIRECTED) {
				if (nodes[i].getDegree() > this.max) {
					neighs = new Vector<Integer>(nodes[i].getDegree());
					for (int j = 0; j < nodes[i].getInDegree(); j++) {
						neighs.add(nodes[i].getIncomingEdges()[j]);
					}
					for (int j = 0; j < nodes[i].getOutDegree(); j++) {
						neighs.add(nodes[i].getOutgoingEdges()[j]);
					}
					cur = map.get(i);
					if (cur == null) {
						cur = new Vector<Integer>();
						map.put(i, cur);
					}
					int in = nodes[i].getInDegree();
					for (int j = 0; j < nodes[i].getDegree() - this.max; j++) {
						int removed = neighs
								.remove(rand.nextInt(neighs.size()));
						if (removed < in) {
							vec = map.get(removed);
							if (vec == null) {
								vec = new Vector<Integer>();
								map.put(removed, vec);
							}
							vec.add(i);
							in--;
						} else {
							cur.add(removed);
						}
					}
				}
			}
		}
		return map;
	};

}

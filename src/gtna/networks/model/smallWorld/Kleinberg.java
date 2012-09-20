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
 * Kleinberg.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.smallWorld;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDPartitionSimple;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.Random;

/**
 * implement the Kleinberg Small World Model as described in Kleinberg's 'The
 * Small-World Phenomenon: An Algorithmic Perspective'
 * 
 * @author stefanie
 * 
 */
public class Kleinberg extends Network {

	private int EDGELENGTH;
	private int DIMENSIONS;

	private int LOCAL_DISTANCE;

	private int LONG_RANGE_CONTACTS;

	private double CLUSTERING_EXPONENT;

	private boolean BIDIRECTIONAL;

	private boolean WRAPAROUND;

	/**
	 * 
	 * @param edgeLength
	 *            : lattice side length, graph size edgeLength^DIMENSIONS
	 * @param DIMENSIONS
	 * @param LOCAL_DISTANCE
	 * @param LONG_RANGE_CONTACTS
	 * @param CLUSTERING_EXPONENT
	 * @param BIDIRECTIONAL
	 * @param WRAPAROUND
	 *            : wraparound with modulus edgeLength?
	 * @param ra
	 * @param t
	 */

	public Kleinberg(int edgeLength, int DIMENSIONS, int LOCAL_DISTANCE,
			int LONG_RANGE_CONTACTS, double CLUSTERING_EXPONENT,
			boolean BIDIRECTIONAL, boolean WRAPAROUND, Transformation[] t) {
		super("KLEINBERG", (int) Math.pow(edgeLength, DIMENSIONS),
				new Parameter[] {
						new IntParameter("DIMENSIONS", DIMENSIONS),
						new IntParameter("LOCAL_DISTANCE", LOCAL_DISTANCE),
						new IntParameter("LONG_RANGE_CONTACTS",
								LONG_RANGE_CONTACTS),
						new DoubleParameter("CLUSTERING_EXPONENT",
								CLUSTERING_EXPONENT),
						new BooleanParameter("BIDIRECTIONAL", BIDIRECTIONAL),
						new BooleanParameter("WRAPAROUND", WRAPAROUND)

				}, t);
		this.EDGELENGTH = edgeLength;
		this.DIMENSIONS = DIMENSIONS;
		this.LOCAL_DISTANCE = LOCAL_DISTANCE;
		this.LONG_RANGE_CONTACTS = LONG_RANGE_CONTACTS;
		this.CLUSTERING_EXPONENT = CLUSTERING_EXPONENT;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
		this.WRAPAROUND = WRAPAROUND;
	}

	public Graph generate() {
		Graph g = new Graph(this.getDescription());
		Node[] nodes = new Node[this.getNodes()];

		double[] x = new double[this.DIMENSIONS];
		double[] modulo = new double[this.DIMENSIONS];
		for (int i = 0; i < modulo.length; i++) {
			modulo[i] = this.EDGELENGTH;
		}
		MDPartitionSimple[] parts = new MDPartitionSimple[this.getNodes()];
		MDIdentifierSpaceSimple idSpace = new MDIdentifierSpaceSimple(parts,
				modulo, this.WRAPAROUND);
		for (int i = 0; i < nodes.length; i++) {
			MDIdentifier id = new MDIdentifier(x.clone(), modulo,
					this.WRAPAROUND);
			parts[i] = new MDPartitionSimple(id);
			nodes[i] = new Node(i, g);
			x[0] = (x[0] + 1) % this.EDGELENGTH;
			for (int j = 1; j < x.length; j++) {
				if (x[j - 1] == 0) {
					x[j] = (x[j] + 1) % this.EDGELENGTH;
				} else {
					break;
				}
			}
		}

		Edges edges = new Edges(
				nodes,
				this.getNodes()
						* (2 * (this.LOCAL_DISTANCE + 1) * this.LOCAL_DISTANCE + this.LONG_RANGE_CONTACTS));
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < nodes.length; i++) {
			this.generateLocalContacts(i, edges, parts);
		}

		double sum = 0;
		if (this.WRAPAROUND) {
			for (int j = 1; j < nodes.length; j++) {

				sum += Math.pow(parts[0].distance(parts[j].getIdentifier()),
						-this.CLUSTERING_EXPONENT);

			}
		}
		for (int i = 0; i < nodes.length; i++) {
			if (!this.WRAPAROUND) {
				sum = 0;
				for (int j = 0; j < nodes.length; j++) {
					if (i != j) {
						sum += Math.pow(
								parts[i].distance(parts[j].getIdentifier()),
								-this.CLUSTERING_EXPONENT);
					}
				}
			}
			// for (int j = 0; j < nodes.length; j++) {
			// prob[j] = Math.pow(nodes[i].id.dist(nodes[j].id),
			// -this.CLUSTERING_EXPONENT)
			// / sum;
			// }
			this.generateLongRangeContacts(sum, parts, i, rand, edges);
		}
		edges.fill();
		g.setNodes(nodes);
		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		return g;
	}

	private void generateLocalContacts(int nr, Edges edges,
			MDPartitionSimple[] part) {
		for (int i = nr + 1; i < part.length; i++) {
			if (part[nr].getIdentifier().distance(part[i].getIdentifier()) <= this.LOCAL_DISTANCE) {
				edges.add(nr, i);
				edges.add(i, nr);
			}
		}
	}

	private void generateLongRangeContacts(double sum,
			MDPartitionSimple[] part, int nr, Random rand, Edges edges) {
		double[] rands = new double[this.LONG_RANGE_CONTACTS];
		for (int i = 0; i < rands.length; i++) {
			rands[i] = rand.nextDouble() * sum;
		}
		Arrays.sort(rands);

		double sum2 = 0;
		int current = 0;
		int found = 0;
		while (found < rands.length && current < part.length) {
			if (current == nr) {
				current++;
				continue;
			}
			sum2 = sum2
					+ Math.pow(
							part[nr].distance(part[current].getIdentifier()),
							-this.CLUSTERING_EXPONENT);
			if (sum2 >= rands[found]) {
				edges.add(nr, current);
				found++;
				if (this.BIDIRECTIONAL) {
					edges.add(current, nr);
				}
			}
			current++;
		}
		// double sum = 0;
		// for (int i = 0; i < nodes.length; i++) {
		// sum += node.id.dist(nodes[i].id);
		// }
		// int found = 0;
		// while (found < this.LONG_RANGE_CONTACTS) {
		// GridNode contact = nodes[rand.nextInt(nodes.length)];
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
	}

	public static Kleinberg[] get(int[] n, int d, int p, int q, double r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[] nw = new Kleinberg[n.length];
		for (int i = 0; i < n.length; i++) {
			nw[i] = new Kleinberg(n[i], d, p, q, r, b, w, t);
		}
		return nw;
	}

	public static Kleinberg[] get(int n, int d, int[] p, int q, double r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[] nw = new Kleinberg[p.length];
		for (int i = 0; i < p.length; i++) {
			nw[i] = new Kleinberg(n, d, p[i], q, r, b, w, t);
		}
		return nw;
	}

	public static Kleinberg[] get(int n, int d, int p, int[] q, double r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[] nw = new Kleinberg[q.length];
		for (int i = 0; i < q.length; i++) {
			nw[i] = new Kleinberg(n, d, p, q[i], r, b, w, t);
		}
		return nw;
	}

	public static Kleinberg[] get(int n, int d, int p, int q, double[] r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[] nw = new Kleinberg[r.length];
		for (int i = 0; i < r.length; i++) {
			nw[i] = new Kleinberg(n, d, p, q, r[i], b, w, t);
		}
		return nw;
	}

	public static Kleinberg[][] getXY(int n, int d, int[] p, int[] q, double r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[q.length][];
		for (int i = 0; i < q.length; i++) {
			nw[i] = Kleinberg.get(n, d, p, q[i], r, b, w, t);
		}
		return nw;
	}

	public static Kleinberg[][] getYX(int n, int d, int[] p, int[] q, double r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[p.length][];
		for (int i = 0; i < p.length; i++) {
			nw[i] = Kleinberg.get(n, d, p[i], q, r, b, w, t);
		}
		return nw;
	}

	public static Kleinberg[][] getXY(int n, int d, int[] p, int q, double[] r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[r.length][];
		for (int i = 0; i < r.length; i++) {
			nw[i] = Kleinberg.get(n, d, p, q, r[i], b, w, t);
		}
		return nw;
	}

	public static Kleinberg[][] getYX(int n, int d, int[] p, int q, double[] r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[p.length][];
		for (int i = 0; i < p.length; i++) {
			nw[i] = Kleinberg.get(n, d, p[i], q, r, b, w, t);
		}
		return nw;
	}

	public static Kleinberg[][] getXY(int n, int d, int p, int q[], double[] r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[r.length][];
		for (int i = 0; i < r.length; i++) {
			nw[i] = Kleinberg.get(n, d, p, q, r[i], b, w, t);
		}
		return nw;
	}

	public static Kleinberg[][] getYX(int n, int d, int p, int[] q, double[] r,
			boolean b, boolean w, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[q.length][];
		for (int i = 0; i < q.length; i++) {
			nw[i] = Kleinberg.get(n, d, p, q[i], r, b, w, t);
		}
		return nw;
	}

}

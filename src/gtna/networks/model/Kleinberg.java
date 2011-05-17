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
 * Kleinberg.java
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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.node.GridNode;
import gtna.routing.node.identifier.GridIDManhattan;
import gtna.transformation.Transformation;
import gtna.util.Timer;

import java.util.Random;

public class Kleinberg extends NetworkImpl implements Network {
	private int DIMENSIONS;

	private int LOCAL_DISTANCE;

	private int LONG_RANGE_CONTACTS;

	private double CLUSTERING_EXPONENT;

	private boolean BIDIRECTIONAL;

	public Kleinberg(int nodes, int DIMENSIONS, int LOCAL_DISTANCE,
			int LONG_RANGE_CONTACTS, double CLUSTERING_EXPONENT,
			boolean BIDIRECTIONAL, RoutingAlgorithm ra, Transformation[] t) {
		super("KLEINBERG", nodes, new String[] { "DIMENSIONS",
				"LOCAL_DISTANCE", "LONG_RANGE_CONTACTS", "CLUSTERING_EXPONENT",
				"BIDIRECTIONAL" }, new String[] { "" + DIMENSIONS,
				"" + LOCAL_DISTANCE, "" + LONG_RANGE_CONTACTS,
				"" + CLUSTERING_EXPONENT, "" + BIDIRECTIONAL }, ra, t);
		this.DIMENSIONS = DIMENSIONS;
		this.LOCAL_DISTANCE = LOCAL_DISTANCE;
		this.LONG_RANGE_CONTACTS = LONG_RANGE_CONTACTS;
		this.CLUSTERING_EXPONENT = CLUSTERING_EXPONENT;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
	}

	public Graph generate() {
		Timer timer = new Timer();
		GridNode[] nodes = new GridNode[this.nodes()];
		int gridSize = (int) Math.ceil(Math.pow(this.nodes(),
				1.0 / (double) this.DIMENSIONS));
		double[] x = new double[this.DIMENSIONS];
		for (int i = 0; i < nodes.length; i++) {
			GridIDManhattan id = new GridIDManhattan(x.clone());
			nodes[i] = new GridNode(i, id);
			x[0] = (x[0] + 1) % gridSize;
			for (int j = 1; j < x.length; j++) {
				if (x[j - 1] == 0) {
					x[j] = (x[j] + 1) % gridSize;
				} else {
					break;
				}
			}
		}

		Edges edges = new Edges(
				nodes,
				this.nodes()
						* (2 * (this.LOCAL_DISTANCE + 1) * this.LOCAL_DISTANCE + this.LONG_RANGE_CONTACTS));
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < nodes.length; i++) {
			this.generateLocalContacts(nodes[i], edges, nodes);
		}
		for (int i = 0; i < nodes.length; i++) {
			double[] prob = new double[nodes.length];
			double sum = 0;
			for (int j = 0; j < nodes.length; j++) {
				if (i != j) {
					sum += Math.pow(nodes[i].id.dist(nodes[j].id),
							-this.CLUSTERING_EXPONENT);
				}
			}
			for (int j = 0; j < nodes.length; j++) {
				prob[j] = Math.pow(nodes[i].id.dist(nodes[j].id),
						-this.CLUSTERING_EXPONENT)
						/ sum;
			}
			this.generateLongRangeContacts(nodes[i], nodes, edges, prob, rand);
		}
		edges.fill();

		timer.end();
		return new Graph(this.description(), nodes, timer);
	}

	private void generateLocalContacts(GridNode node, Edges edges,
			GridNode[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			if (node.id.dist(nodes[i].id) <= this.LOCAL_DISTANCE
					&& node.index() != nodes[i].index()) {
				edges.add(node, nodes[i]);
				if (this.BIDIRECTIONAL) {
					edges.add(nodes[i], node);
				}
			}
		}
	}

	private void generateLongRangeContacts(GridNode node, GridNode[] nodes,
			Edges edges, double[] prob, Random rand) {
		double sum = 0;
		for (int i = 0; i < nodes.length; i++) {
			sum += node.id.dist(nodes[i].id);
		}
		int found = 0;
		while (found < this.LONG_RANGE_CONTACTS) {
			GridNode contact = nodes[rand.nextInt(nodes.length)];
			if (node.index() == contact.index()) {
				continue;
			}
			if (prob[contact.index()] >= rand.nextDouble()) {
				if (edges.contains(node.index(), contact.index())) {
					continue;
				}
				edges.add(node, contact);
				if (this.BIDIRECTIONAL) {
					edges.add(contact, node);
				}
				found++;
			}
		}
	}

	public static Kleinberg[] get(int[] n, int d, int p, int q, double r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[] nw = new Kleinberg[n.length];
		for (int i = 0; i < n.length; i++) {
			nw[i] = new Kleinberg(n[i], d, p, q, r, b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[] get(int n, int d, int[] p, int q, double r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[] nw = new Kleinberg[p.length];
		for (int i = 0; i < p.length; i++) {
			nw[i] = new Kleinberg(n, d, p[i], q, r, b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[] get(int n, int d, int p, int[] q, double r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[] nw = new Kleinberg[q.length];
		for (int i = 0; i < q.length; i++) {
			nw[i] = new Kleinberg(n, d, p, q[i], r, b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[] get(int n, int d, int p, int q, double[] r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[] nw = new Kleinberg[r.length];
		for (int i = 0; i < r.length; i++) {
			nw[i] = new Kleinberg(n, d, p, q, r[i], b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[][] getXY(int n, int d, int[] p, int[] q, double r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[q.length][];
		for (int i = 0; i < q.length; i++) {
			nw[i] = Kleinberg.get(n, d, p, q[i], r, b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[][] getYX(int n, int d, int[] p, int[] q, double r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[p.length][];
		for (int i = 0; i < p.length; i++) {
			nw[i] = Kleinberg.get(n, d, p[i], q, r, b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[][] getXY(int n, int d, int[] p, int q, double[] r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[r.length][];
		for (int i = 0; i < r.length; i++) {
			nw[i] = Kleinberg.get(n, d, p, q, r[i], b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[][] getYX(int n, int d, int[] p, int q, double[] r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[p.length][];
		for (int i = 0; i < p.length; i++) {
			nw[i] = Kleinberg.get(n, d, p[i], q, r, b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[][] getXY(int n, int d, int p, int q[], double[] r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[r.length][];
		for (int i = 0; i < r.length; i++) {
			nw[i] = Kleinberg.get(n, d, p, q, r[i], b, ra, t);
		}
		return nw;
	}

	public static Kleinberg[][] getYX(int n, int d, int p, int[] q, double[] r,
			boolean b, RoutingAlgorithm ra, Transformation[] t) {
		Kleinberg[][] nw = new Kleinberg[q.length][];
		for (int i = 0; i < q.length; i++) {
			nw[i] = Kleinberg.get(n, d, p, q[i], r, b, ra, t);
		}
		return nw;
	}
}

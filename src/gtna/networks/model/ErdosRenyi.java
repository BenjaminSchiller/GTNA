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
 * ErdosRenyi.java
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

import java.util.Random;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

/**
 * Implements a network generator for the so-called Erdos-Renyi random graph
 * model G(n,M). It was first described by Erdos and Renyi in their Book
 * "On random graphs I" in 1959. The model generates a network topology G(n,M)
 * with a given number of nodes (n) and a specified number of edges (M). Until
 * the targeted number of edges in the system is reached, two nodes are selected
 * uniformly at random and an edge between them is created if it does not
 * already exist.
 * 
 * http://en.wikipedia.org/wiki/Erdos-Renyi_model
 * 
 * The parameters are the average node degree (= 2 * edges / nodes) as well as a
 * flag for the bidirectionality of edges.
 * 
 * Note that in this implementation, loops are not permitted, i.e., there is no
 * edge of the form (a,a).
 * 
 * @author benni
 * 
 */
public class ErdosRenyi extends NetworkImpl implements Network {
	private double AVERAGE_DEGREE;

	private boolean BIDIRECTIONAL;

	public static ErdosRenyi[] get(int[] n, double d, boolean b,
			RoutingAlgorithm ra, Transformation[] t) {
		ErdosRenyi[] nw = new ErdosRenyi[n.length];
		for (int i = 0; i < n.length; i++) {
			nw[i] = new ErdosRenyi(n[i], d, b, ra, t);
		}
		return nw;
	}

	public static ErdosRenyi[] get(int n, double d, boolean b,
			RoutingAlgorithm[] ra, Transformation[] t) {
		ErdosRenyi[] nw = new ErdosRenyi[ra.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new ErdosRenyi(n, d, b, ra[i], t);
		}
		return nw;
	}

	public static ErdosRenyi[] get(int n, double[] d, boolean b,
			RoutingAlgorithm ra, Transformation[] t) {
		ErdosRenyi[] nw = new ErdosRenyi[d.length];
		for (int i = 0; i < d.length; i++) {
			nw[i] = new ErdosRenyi(n, d[i], b, ra, t);
		}
		return nw;
	}

	public static ErdosRenyi[][] get(int[] n, double[] d, boolean b,
			RoutingAlgorithm ra, Transformation[] t) {
		ErdosRenyi[][] nw = new ErdosRenyi[d.length][n.length];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < n.length; j++) {
				nw[i][j] = new ErdosRenyi(n[j], d[i], b, ra, t);
			}
		}
		return nw;
	}

	public ErdosRenyi(int nodes, double AVERAGE_DEGREE, boolean BIDIRECTIONAL,
			RoutingAlgorithm ra, Transformation[] t) {
		super("ERDOS_RENYI", nodes, new String[] { "AVERAGE_DEGREE",
				"BIDIRECTIONAL" }, new String[] { "" + AVERAGE_DEGREE,
				"" + BIDIRECTIONAL }, ra, t);
		this.AVERAGE_DEGREE = AVERAGE_DEGREE;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
	}

	public Graph generate() {
		Timer timer = new Timer();
		Random rand = new Random(System.currentTimeMillis());
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		int toAdd = (int) (this.AVERAGE_DEGREE * this.nodes() / 2);
		Edges edges = new Edges(nodes, toAdd);
		while (edges.size() < toAdd) {
			NodeImpl u = nodes[rand.nextInt(nodes.length)];
			NodeImpl v = nodes[rand.nextInt(nodes.length)];
			if (u.index() == v.index()) {
				continue;
			}
			if (this.BIDIRECTIONAL) {
				Edge edge = new Edge(u, v);
				if (!edges.contains(edge)) {
					edges.add(edge);
					edges.add(v, u);
				}
			} else {
				Edge edge = new Edge(u, v);
				if (!edges.contains(edge)) {
					edges.add(edge);
				}
			}
		}
		edges.fill();
		timer.end();
		return new Graph(this.description(), nodes, timer);
	}
}

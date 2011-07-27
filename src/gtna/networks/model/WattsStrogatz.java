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
 * WattsStrogatz.java
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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

public class WattsStrogatz extends NetworkImpl implements Network {
	private int LINKS;

	private double BETA;

	public WattsStrogatz(int nodes, int SUCCESSORS, double BETA,
			RoutingAlgorithm ra, Transformation[] t) {
		super("WATTS_STROGATZ", nodes, new String[] { "SUCCESSORS", "BETA" },
				new String[] { "" + SUCCESSORS, "" + BETA }, ra, t);
		this.LINKS = SUCCESSORS;
		this.BETA = BETA;
	}

	public static WattsStrogatz[] get(int[] n, int s, double b,
			RoutingAlgorithm ra, Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[n.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n[i], s, b, ra, t);
		}
		return nw;
	}

	public static WattsStrogatz[] get(int n, int s[], double b,
			RoutingAlgorithm ra, Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[s.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n, s[i], b, ra, t);
		}
		return nw;
	}

	public static WattsStrogatz[] get(int n, int s, double b[],
			RoutingAlgorithm ra, Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[b.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n, s, b[i], ra, t);
		}
		return nw;
	}

	public Graph generate() {
		Timer timer = new Timer();
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.nodes());
		Edges edges = new Edges(nodes, this.LINKS * 2 * nodes.length);
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 1; j <= this.LINKS; j++) {
				if (rand.nextDouble() <= this.BETA) {
					Node dest = nodes[rand.nextInt(nodes.length)];
					while (edges.contains(i, dest.index())) {
						dest = nodes[rand.nextInt(nodes.length)];
					}
					edges.add(nodes[i], dest);
					edges.add(dest, nodes[i]);
				} else {
					Node succ = nodes[(i + j) % nodes.length];
					edges.add(nodes[i], succ);
					edges.add(succ, nodes[i]);
				}
			}
		}
		edges.fill();
		timer.end();
		Graph g = new Graph(this.description(), nodes, timer);
		return g;
	}
}

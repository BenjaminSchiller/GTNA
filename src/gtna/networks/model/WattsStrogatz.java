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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

public class WattsStrogatz extends Network {
	private int LINKS;

	private double BETA;

	public WattsStrogatz(int nodes, int SUCCESSORS, double BETA,
			Transformation[] t) {
		super("WATTS_STROGATZ", nodes, new Parameter[] {
				new IntParameter("SUCCESSORS", SUCCESSORS),
				new DoubleParameter("BETA", BETA) }, t);
		this.LINKS = SUCCESSORS;
		this.BETA = BETA;
	}

	public static WattsStrogatz[] get(int[] n, int s, double b,
			Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[n.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n[i], s, b, t);
		}
		return nw;
	}

	public static WattsStrogatz[] get(int n, int s[], double b,
			Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[s.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n, s[i], b, t);
		}
		return nw;
	}

	public static WattsStrogatz[] get(int n, int s, double b[],
			Transformation[] t) {
		WattsStrogatz[] nw = new WattsStrogatz[b.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(n, s, b[i], t);
		}
		return nw;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, this.LINKS * 2 * nodes.length);
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 1; j <= this.LINKS; j++) {
				if (rand.nextDouble() <= this.BETA) {
					int dest = rand.nextInt(nodes.length);
					while (edges.contains(i, dest)) {
						dest = rand.nextInt(nodes.length);
					}
					edges.add(i, dest);
					edges.add(dest, i);
				} else {
					int succ = (i + j) % nodes.length;
					edges.add(i, succ);
					edges.add(succ, i);
				}
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}

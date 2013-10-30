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
 * Regular.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.canonical;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class Regular extends Network {
	private int edges;

	private boolean undirected;

	public Regular(int nodes, int edges, boolean undirected, Transformation[] t) {
		super("REGULAR", nodes, new Parameter[] {
				new IntParameter("EDGES", edges),
				new BooleanParameter("UNDIRECTED", undirected) }, t);
		this.edges = edges;
		this.undirected = undirected;
	}

	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, this.getNodes() * this.edges * 2);
		ArrayList<Integer> from = new ArrayList<Integer>(this.nodes
				* this.edges / 2);
		ArrayList<Integer> to = new ArrayList<Integer>(this.nodes * this.edges
				/ 2);

		for (int i = 0; i < this.nodes; i++) {
			for (int j = 0; j < this.edges; j++) {
				from.add(i);
				to.add(i);
				if (!this.undirected) {
					from.add(i);
					to.add(i);
				}
			}
		}

		Random rand = new Random();

		int fails = 0;
		while (from.size() > 0) {
			int srcIndex = rand.nextInt(from.size());
			int dstIndex = rand.nextInt(to.size());
			int src = from.get(srcIndex);
			int dst = to.get(dstIndex);
			if (src == dst || edges.contains(src, dst)) {
				fails++;
				if (fails > 10) {
					return this.generate();
				}
				continue;
			}
			fails = 0;
			from.remove(srcIndex);
			to.remove(dstIndex);
			edges.add(src, dst);
			if (this.undirected) {
				edges.add(dst, src);
			}
			if ((from.size() % 10000) == 0) {
				System.out.println("f = " + from.size() / 10000 + " / e = " + edges.size() / 10000);
			}
		}

		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

}

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
 * ScaleFreeUndirectedSD.java
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
package gtna.networks.model.smallWorld;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class ScaleFreeUndirectedSD extends Transformation {
	private int C;

	public ScaleFreeUndirectedSD(int C) {
		super("SCALE_FREE_UNDIRECTED_SD", new Parameter[] { new IntParameter(
				"C", C) });
		this.C = C;
	}

	@Override
	public Graph transform(Graph g) {
		Node[] nodes = g.getNodes();
		Edges edges = g.getEdges();
		Random rand = new Random();

		// create local edges: randomly choose node within distance C
		for (int i = 0; i < nodes.length; i++) {
			int n1 = (i + rand.nextInt(this.C) + 1) % nodes.length;
			edges.add(i, n1);
			edges.add(n1, i);
			int n2 = (i - rand.nextInt(this.C) - 1 + nodes.length)
					% nodes.length;
			edges.add(i, n2);
			edges.add(n2, i);
		}

		edges.fill();
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

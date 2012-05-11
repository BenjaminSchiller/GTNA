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
 * CondonAndKarp.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
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

/**
 * @author Philipp Neubrand
 * 
 *         Brandes, U., M. Gaertler, and D. Wagner, 2003, in Proceed- ings of
 *         ESA (Springer-Verlag, Berlin, Germany), pp. 568{ 579.
 * 
 */
public class CondonAndKarp extends Network {

	private int groups;
	private double pin;
	private double pout;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public CondonAndKarp(int nodes, int groups, double pin, double pout,
			Transformation[] transformations) {
		super("CONDON_KARP", nodes, new Parameter[] {
				new IntParameter("GROUPS", groups),
				new DoubleParameter("PIN", pin),
				new DoubleParameter("POUT", pout) }, transformations);

		this.groups = groups;
		this.pin = pin;
		this.pout = pout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());

		Node[] nodes = Node.init(this.getNodes(), graph);
		int nodesPerCom = getNodes() / groups;
		Edges edges = new Edges(nodes, 1);

		for (int i = 0; i < getNodes(); i++) {
			for (int j = 0; j < getNodes(); j++) {
				if (i == j)
					continue;

				if (i / nodesPerCom == j / nodesPerCom) {
					if (Math.random() < pin)
						edges.add(i, j);
				} else if (Math.random() < pout)
					edges.add(i, j);
			}

		}

		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

}

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
 * WoTGrowthModel.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Dirk;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.wot;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.util.ReadableFile;
import gtna.transformation.Transformation;

/**
 * @author Dirk
 *
 */
public class WoTGrowthModel extends WoTModelSingleCommunity {

	/**
	 * @param nodes
	 * @param d
	 * @param b
	 * @param bAlpha
	 * @param alpha
	 * @param beta
	 * @param beta1
	 * @param beta2
	 * @param beta3
	 * @param t
	 */
	public WoTGrowthModel(int nodes, double d, double b, double bAlpha,
			double alpha, double beta, double beta1, double beta2,
			double beta3, Transformation[] t) {
		super(nodes, d, b, bAlpha, alpha, beta, beta1, beta2, beta3, t);
		
		this.key = "WOTGROWTHMODEL";
	}
	
	/*
	 * Overrides initSTartGRaphMethod to start with a smal real-world WoT Graph.
	 * 
	 * 
	 * @see gtna.networks.model.wot.WoTModelSingleCommunity#initStartGraph()
	 */
	@Override
	protected void initStartGraph() {
		ReadableFile wot = new ReadableFile("WoT Original", "wot-original",
				origfolder + NODES1 + ".gtna", null);
		Graph initGraph = wot.generate();

		nodeCounter = initGraph.getNodeCount();

		// Copy
		for (Node n : initGraph.getNodes()) {
			int out[] = n.getOutgoingEdges();

			for (int dst : out)
				addEdge(n.getIndex(), dst);

		}
	}

}

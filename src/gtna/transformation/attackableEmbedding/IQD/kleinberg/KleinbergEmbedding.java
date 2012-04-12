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
 * KleinbergEmbedding.java
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
package gtna.transformation.attackableEmbedding.IQD.kleinberg;

import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.DIdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding;
import gtna.transformation.attackableEmbedding.lmc.LMCAttackerContraction;
import gtna.transformation.attackableEmbedding.lmc.LMCAttackerConvergence;
import gtna.transformation.attackableEmbedding.lmc.LMCAttackerKleinberg;
import gtna.transformation.attackableEmbedding.lmc.LMCNode;
import gtna.util.parameter.Parameter;

/**
 * @author stef
 *
 */
public class KleinbergEmbedding extends IQDEmbedding {
   
	
	/**
	 * @param iterations
	 * @param key
	 * @param idMethod
	 * @param deMethod
	 * @param distance
	 * @param epsilon
	 * @param checkold
	 * @param parameters
	 */
	public KleinbergEmbedding(int iterations, 
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			Parameter[] parameters) {
		super(iterations, "KLEINBERG", idMethod, deMethod, distance, epsilon, checkold,
				parameters);
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbedding#generateNodes(gtna.graph.Graph, java.util.Random)
	 */
	@Override
	protected AttackableEmbeddingNode[] generateNodes(Graph g, Random rand) {
		AttackableEmbeddingNode[] nodes = new AttackableEmbeddingNode[g.getNodes().length];
		for (int i = 0; i < g.getNodes().length; i++) {
			nodes[i] = new KleinbergNode(i, g, this);
		}
		this.init(g, nodes);
		this.initIds(g);
		return nodes;
	}
	
	

	/* (non-Javadoc)
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbedding#generateSelectionSet(gtna.transformation.attackableEmbedding.AttackableEmbeddingNode[], java.util.Random)
	 */
	@Override
	protected AttackableEmbeddingNode[] generateSelectionSet(
			AttackableEmbeddingNode[] nodes, Random rand) {
		return nodes;
	}

	

}

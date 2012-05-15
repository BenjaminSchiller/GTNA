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
 * RegionCoverageEmbedding.java
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
package gtna.transformation.attackableEmbedding.IQD.RegionCoverage;

import gtna.graph.Graph;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding.AttackerSelection;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding.AttackerType;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.DecisionMethod;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.IdentifierMethod;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.HashSet;
import java.util.Random;

/**
 * @author stef embedding categories distances into buckets 2^{i-1} < b_i <= 2^i
 *         and count number of buckets
 */
public class RegionCoverageEmbedding extends AttackerIQDEmbedding {
	int max;

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
	public RegionCoverageEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustone, int max) {
		super(iterations, "REGION_COVERAGE", idMethod, deMethod, distance,
				epsilon, checkold, adjustone,
				new Parameter[] { new IntParameter("MAX_LOG", max) });
		this.max = max;
	}

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
	public RegionCoverageEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustone, int max, AttackerType type,
			AttackerSelection selection, int attackercount) {
		super(iterations, "REGION_COVERAGE", idMethod, deMethod, distance,
				epsilon, checkold, adjustone, type, selection, attackercount,
				new Parameter[] { new IntParameter("MAX_LOG", max) });
		this.max = max;
	}

	public int getMax() {
		return this.max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.attackableEmbedding.AttackableEmbedding#generateNodes
	 * (gtna.graph.Graph, java.util.Random)
	 */
	@Override
	protected AttackableEmbeddingNode[] generateNodes(Graph g, Random rand) {
		AttackableEmbeddingNode[] nodes = new AttackableEmbeddingNode[g
				.getNodes().length];
		HashSet<Integer> attackers = this.getAttackers(g, rand);
		for (int i = 0; i < g.getNodes().length; i++) {
			if (attackers.contains(i)) {
				nodes[i] = new RegionCoverageNode(i, g, this, true);
			} else {
				nodes[i] = new RegionCoverageNode(i, g, this, false);
			}
		}
		this.init(g, nodes);
		this.initIds();
		return nodes;
	}
	
	public RegionCoverageEmbedding(int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, int max,  boolean add) {
		super(iterations, "REGION_COVERAGE", idMethod, deMethod, distance,
				epsilon, checkold, adjustone, add?new Parameter[] {
						new IntParameter("MAX_LOG", max)}: new Parameter[]{new IntParameter("ITERATIONS", iterations)},add);
		this.max = max;
	}

	public RegionCoverageEmbedding(int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, int max,  AttackerType type,
			AttackerSelection selection, int attackercount, boolean add) {
		super(iterations, "REGION_COVERAGE", idMethod, deMethod, distance,
				epsilon, checkold, adjustone, type, selection, attackercount,
				add?new Parameter[] {
						new IntParameter("MAX_LOG", max)}: new Parameter[]{new IntParameter("ITERATIONS", iterations)},add);
		this.max = max;
		
	}

}

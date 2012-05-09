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
 * DistanceDiversityEmbedding.java
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
package gtna.transformation.attackableEmbedding.IQD.DistanceDiversity;

import gtna.graph.Graph;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.HashSet;
import java.util.Random;

/**
 * @author stef embedding categories distances into buckets 2^{i-1} < b_i <= 2^i
 *         and computes sum |b_i -b_j|^r for some r and all b_i,b_j
 */
public class DistanceDiversityEmbedding extends AttackerIQDEmbedding {

	private int max;
	private double exponent;

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
	public DistanceDiversityEmbedding(int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, int max, double exp) {
		super(iterations, "DISTANCE_DIVERSITY", idMethod, deMethod, distance,
				epsilon, checkold, adjustone, new Parameter[] {
						new IntParameter("MAX_LOG", max),
						new DoubleParameter("EXPONENT", exp) });
		this.max = max;
		this.exponent = exp;
	}

	public DistanceDiversityEmbedding(int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, int max, double exp, AttackerType type,
			AttackerSelection selection, int attackercount) {
		super(iterations, "DISTANCE_DIVERSITY", idMethod, deMethod, distance,
				epsilon, checkold, adjustone, type, selection, attackercount,
				new Parameter[] { new IntParameter("MAX_LOG", max),
						new DoubleParameter("EXPONENT", exp) });
		this.max = max;
		this.exponent = exp;
	}
	
	public DistanceDiversityEmbedding(int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, int max, double exp, boolean add) {
		super(iterations, "DISTANCE_DIVERSITY", idMethod, deMethod, distance,
				epsilon, checkold, adjustone, add?new Parameter[] {
						new IntParameter("MAX_LOG", max),
						new DoubleParameter("EXPONENT", exp)}: new Parameter[]{new IntParameter("ITERATIONS", iterations)},add);
		this.max = max;
		this.exponent = exp;
	}

	public DistanceDiversityEmbedding(int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, int max, double exp, AttackerType type,
			AttackerSelection selection, int attackercount, boolean add) {
		super(iterations, "DISTANCE_DIVERSITY", idMethod, deMethod, distance,
				epsilon, checkold, adjustone, type, selection, attackercount,
				add?new Parameter[] {
						new IntParameter("MAX_LOG", max),
						new DoubleParameter("EXPONENT", exp)}: new Parameter[]{new IntParameter("ITERATIONS", iterations)},add);
		this.max = max;
		this.exponent = exp;
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
				nodes[i] = new DistanceDiversityNode(i, g, this, true);
			} else {
				nodes[i] = new DistanceDiversityNode(i, g, this, false);
			}
		}
		this.init(g, nodes);
		this.initIds();
		return nodes;
	}

	public int getMax() {
		return this.max;
	}

	public double getExponent() {
		return this.exponent;
	}

}

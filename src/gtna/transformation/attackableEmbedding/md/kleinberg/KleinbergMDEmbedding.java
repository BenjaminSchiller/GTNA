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
package gtna.transformation.attackableEmbedding.md.kleinberg;

import gtna.graph.Graph;
import gtna.id.md.MDIdentifierSpaceSimple.DistanceMD;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.md.AttackerIQDMDEmbedding;
import gtna.transformation.attackableEmbedding.md.IQDMDEmbedding;
import gtna.util.parameter.Parameter;

import java.util.HashSet;
import java.util.Random;

/**
 * @author stef trying to minimize the product of edges' length
 * 
 */
public class KleinbergMDEmbedding extends AttackerIQDMDEmbedding {

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
	public KleinbergMDEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, DistanceMD distance, int dimension,
			double epsilon, boolean checkold, boolean adjustone,
			Parameter[] parameters) {
		super(iterations, "KLEINBERG_MD", idMethod, deMethod, distance,
				dimension, epsilon, checkold, adjustone, parameters);
	}

	public KleinbergMDEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, DistanceMD distance, int dimension,
			double epsilon, boolean checkold, boolean adjustone,
			AttackerType type, AttackerSelection selection, int attackercount,
			Parameter[] parameters) {
		super(iterations, "KLEINBERG_MD", idMethod, deMethod, distance,
				dimension, epsilon, checkold, adjustone, type, selection,
				attackercount, parameters);
	}

	public KleinbergMDEmbedding(String key, int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustone, Parameter[] parameters) {
		super(iterations, key, idMethod, deMethod, distance, dimension,
				epsilon, checkold, adjustone, parameters);
	}

	public KleinbergMDEmbedding(String key, int iterations,
			IQDMDEmbedding.IdentifierMethod onerandom,
			IQDMDEmbedding.DecisionMethod metropolis, DistanceMD distance,
			int dimension, double epsilon, boolean checkold, boolean adjustone,
			Parameter[] parameters, boolean add) {
		super(iterations, key, onerandom, metropolis, distance, dimension,
				epsilon, checkold, adjustone, parameters, add);
	}

	public KleinbergMDEmbedding(String key, int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustone, AttackerType type,
			AttackerSelection selection, int attackercount,
			Parameter[] parameters) {
		super(iterations, key, idMethod, deMethod, distance, dimension,
				epsilon, checkold, adjustone, type, selection, attackercount,
				parameters);
	}

	public KleinbergMDEmbedding(String key, int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustone, AttackerType type,
			AttackerSelection selection, int attackercount,
			Parameter[] parameters, boolean add) {
		super(iterations, key, idMethod, deMethod, distance, dimension,
				epsilon, checkold, adjustone, type, selection, attackercount,
				parameters, add);
	}

	/**
	 * @param i
	 * @param identifierMethod
	 * @param decisionMethod
	 * @param distance
	 * @param epsilon
	 * @param checkold
	 */
	public KleinbergMDEmbedding(int i, IdentifierMethod identifierMethod,
			DecisionMethod decisionMethod, DistanceMD distance, int dimension,
			double epsilon, boolean checkold, boolean adjustone) {
		this(i, identifierMethod, decisionMethod, distance, dimension, epsilon,
				checkold, adjustone, new Parameter[0]);
	}

	public KleinbergMDEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, DistanceMD distance, int dimension,
			double epsilon, boolean checkold, boolean adjustone,
			AttackerType type, AttackerSelection selection, int attackercount) {
		super(iterations, "KLEINBERG_MD", idMethod, deMethod, distance,
				dimension, epsilon, checkold, adjustone, type, selection,
				attackercount, new Parameter[0]);
	}

	public KleinbergMDEmbedding(String key, int i,
			IdentifierMethod identifierMethod, DecisionMethod decisionMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustone) {
		this(key, i, identifierMethod, decisionMethod, distance, dimension,
				epsilon, checkold, adjustone, new Parameter[0]);
	}

	public KleinbergMDEmbedding(String key, int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustone, AttackerType type,
			AttackerSelection selection, int attackercount) {
		super(iterations, key, idMethod, deMethod, distance, dimension,
				epsilon, checkold, adjustone, type, selection, attackercount,
				new Parameter[0]);
	}

	public KleinbergMDEmbedding(String key, int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustone, AttackerType type,
			AttackerSelection selection, int attackercount, boolean add) {
		super(iterations, key, idMethod, deMethod, distance, dimension,
				epsilon, checkold, adjustone, type, selection, attackercount,
				new Parameter[0], add);
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
		HashSet<Integer> map = this.getAttackers(g, rand);
		for (int i = 0; i < g.getNodes().length; i++) {
			if (map.contains(i)) {
				nodes[i] = new KleinbergMDNode(i, g, this, true);
			} else {
				nodes[i] = new KleinbergMDNode(i, g, this, false);
			}
		}
		this.init(g, nodes);
		this.initIds();
		return nodes;
	}

}

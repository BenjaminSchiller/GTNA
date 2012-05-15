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

import gtna.graph.Graph;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.DecisionMethod;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.IdentifierMethod;
import gtna.util.parameter.Parameter;

import java.util.HashSet;
import java.util.Random;

/**
 * @author stef embedding minimizing edge lengths
 */
public class KleinbergEmbedding extends AttackerIQDEmbedding {

	/**
	 * see super class
	 * @param iterations
	 * @param key
	 * @param idMethod
	 * @param deMethod
	 * @param distance
	 * @param epsilon
	 * @param checkold
	 * @param parameters
	 */
	public KleinbergEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustone, Parameter[] parameters) {
		super(iterations, "KLEINBERG", idMethod, deMethod, distance, epsilon,
				checkold, adjustone, parameters);
	}
	
	public KleinbergEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustone, Parameter[] parameters, boolean add) {
		super(iterations, "KLEINBERG", idMethod, deMethod, distance, epsilon,
				checkold, adjustone, parameters, add);
	}
	
	

	public KleinbergEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustone, AttackerType type,
			AttackerSelection selection, int attackercount,
			Parameter[] parameters) {
		super(iterations, "KLEINBERG", idMethod, deMethod, distance, epsilon,
				checkold, adjustone, type, selection, attackercount, parameters);
	}

	public KleinbergEmbedding(String key, int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, Parameter[] parameters) {
		super(iterations, key, idMethod, deMethod, distance, epsilon, checkold,
				adjustone, parameters, false);
	}

	public KleinbergEmbedding(String key, int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, AttackerType type, AttackerSelection selection,
			int attackercount, Parameter[] parameters) {
		super(iterations, key, idMethod, deMethod, distance, epsilon, checkold,
				adjustone, type, selection, attackercount, parameters, false);
	}
	
	

	/**
	 * @param i
	 * @param identifierMethod
	 * @param decisionMethod
	 * @param distance
	 * @param epsilon
	 * @param checkold
	 */
	public KleinbergEmbedding(int i, IdentifierMethod identifierMethod,
			DecisionMethod decisionMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustone) {
		this(i, identifierMethod, decisionMethod, distance, epsilon, checkold,
				adjustone, new Parameter[0]);
	}

	public KleinbergEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustone, AttackerType type,
			AttackerSelection selection, int attackercount) {
		super(iterations, "KLEINBERG", idMethod, deMethod, distance, epsilon,
				checkold, adjustone, type, selection, attackercount,
				new Parameter[0]);
	}

	public KleinbergEmbedding(String key, int i,
			IdentifierMethod identifierMethod, DecisionMethod decisionMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone) {
		super( i, key, identifierMethod, decisionMethod, distance, epsilon,
				checkold, adjustone, new Parameter[0], false);
	}

	public KleinbergEmbedding(String key, int iterations,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, AttackerType type, AttackerSelection selection,
			int attackercount) {
		super(iterations, key, idMethod, deMethod, distance, epsilon, checkold,
				adjustone, type, selection, attackercount, new Parameter[0], false);
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
				nodes[i] = new KleinbergNode(i, g, this, true);
			} else {
				nodes[i] = new KleinbergNode(i, g, this, false);
			}
		}
		this.init(g, nodes);
		this.initIds();
		return nodes;
	}

}

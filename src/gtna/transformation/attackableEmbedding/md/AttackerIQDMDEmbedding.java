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
 * AttackerIQDMDEmbedding.java
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
package gtna.transformation.attackableEmbedding.md;

import gtna.graph.Graph;
import gtna.id.md.MDIdentifierSpaceSimple.DistanceMD;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashSet;
import java.util.Random;

/**
 * @author stef
 * 
 */
public abstract class AttackerIQDMDEmbedding extends IQDMDEmbedding {

	public static int MEDIAN_SET_SIZE = 500;

	public static enum AttackerType {
		NONE, CONTRACTION, DIVERGENCE, REJECTION
	}

	public static enum AttackerSelection {
		RANDOM, MEDIAN, MIN, MAX
	}

	private AttackerType attackertype;
	private AttackerSelection attackerselection;
	private int attackercount;

	public AttackerIQDMDEmbedding(int iterations, String key,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustOne, AttackerType attackertype,
			AttackerSelection attackerselection, int attackercount,
			Parameter[] parameters, boolean add) {
		super(iterations, key, idMethod, deMethod, distance, dimension,
				epsilon, checkold, adjustOne, addAttackerParams(attackertype,
						attackerselection, attackercount, parameters), add);
		this.attackertype = attackertype;
		this.attackerselection = attackerselection;
		this.attackercount = attackercount;

	}

	/**
	 * @param iterations
	 * @param key
	 * @param idMethod
	 * @param deMethod
	 * @param distance
	 * @param epsilon
	 * @param checkold
	 * @param adjustOne
	 * @param parameters
	 */
	public AttackerIQDMDEmbedding(int iterations, String key,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustOne, AttackerType attackertype,
			AttackerSelection attackerselection, int attackercount,
			Parameter[] parameters) {
		super(iterations, key, idMethod, deMethod, distance, dimension,
				epsilon, checkold, adjustOne, addAttackerParams(attackertype,
						attackerselection, attackercount, parameters));
		this.attackertype = attackertype;
		this.attackerselection = attackerselection;
		this.attackercount = attackercount;

	}

	private static Parameter[] addAttackerParams(AttackerType type,
			AttackerSelection selection, int count, Parameter[] param) {
		Parameter[] parameter = new Parameter[param.length + 3];
		parameter[0] = new StringParameter("ATTACKER_TYPE", type.toString());
		parameter[1] = new StringParameter("ATTACKER_SELECTION",
				selection.toString());
		parameter[2] = new IntParameter("ATTACKER_COUNT", count);
		for (int i = 0; i < param.length; i++) {
			parameter[i + 3] = param[i];
		}
		return parameter;
	}

	/**
	 * @param iterations
	 * @param key
	 * @param idMethod
	 * @param deMethod
	 * @param distance
	 * @param epsilon
	 * @param checkold
	 * @param adjustOne
	 * @param parameters
	 */
	public AttackerIQDMDEmbedding(int iterations, String key,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustOne, Parameter[] parameters) {
		this(iterations, key, idMethod, deMethod, distance, dimension, epsilon,
				checkold, adjustOne,  parameters, true);
	}
	
	public AttackerIQDMDEmbedding(int iterations, String key,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustOne, Parameter[] parameters, boolean add) {
		super(iterations, key, idMethod, deMethod, distance, dimension, epsilon,
				checkold, adjustOne,  parameters, true);
		this.attackertype = AttackerType.NONE;
		this.attackerselection = AttackerSelection.RANDOM;
		this.attackercount = 0;
	}

	/**
	 * indices of attackers
	 * 
	 * @param g
	 * @param rand
	 * @return
	 */
	protected HashSet<Integer> getAttackers(Graph g, Random rand) {
		HashSet<Integer> attackers = new HashSet<Integer>();
		if (this.attackertype != AttackerType.NONE) {
			if (this.attackerselection == AttackerSelection.MAX) {
				attackers = this.selectNodesByDegreeDesc(g.getNodes(),
						this.attackercount, rand);
			} else if (this.attackerselection == AttackerSelection.MIN) {
				attackers = this.selectNodesByDegreeAsc(g.getNodes(),
						this.attackercount, rand);
			} else if (this.attackerselection == AttackerSelection.MEDIAN) {
				attackers = this.selectNodesAroundMedian(g.getNodes(),
						this.attackercount, rand, MEDIAN_SET_SIZE);
			} else if (this.attackerselection == AttackerSelection.RANDOM) {
				attackers = this.selectNodesRandomly(g.getNodes(),
						this.attackercount, rand);
			} else {
				throw new IllegalArgumentException(this.attackerselection
						+ " is an unknown attacker selection in Swapping");
			}
		}
		return attackers;
	}

	public AttackerType getAttackertype() {
		return this.attackertype;
	}

	public AttackerSelection getAttackerselection() {
		return this.attackerselection;
	}

	public int getAttackercount() {
		return this.attackercount;
	}

}

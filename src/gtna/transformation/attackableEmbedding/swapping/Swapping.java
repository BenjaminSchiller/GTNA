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
 * Swapping.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.attackableEmbedding.swapping;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.DIdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.transformation.attackableEmbedding.AttackableEmbedding;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashSet;
import java.util.Random;

/**
 * an embedding achieved by swapping IDs as described in Sandberg:
 * "Searching in a Small World "
 * 
 * @author stefanieroos
 * 
 */

public class Swapping extends AttackableEmbedding {

	public static final String ATTACK_CONVERGENCE = "CONVERGENCE";

	public static final String ATTACK_KLEINBERG = "KLEINBERG";

	public static final String ATTACK_CONTRACTION = "CONTRACTION";

	public static final String ATTACK_NONE = "NONE";

	public static final String ATTACKER_SELECTION_LARGEST = "LARGEST";

	public static final String ATTACKER_SELECTION_SMALLEST = "SMALLEST";

	public static final String ATTACKER_SELECTION_MEDIAN = "MEDIAN";

	public static final String ATTACKER_SELECTION_RANDOM = "RANDOM";

	public static final String ATTACKER_SELECTION_NONE = "NONE";

	protected int interations;

	protected double delta;

	protected String attack;

	protected String attackerSelection;

	protected int attackers;

	public static int MEDIAN_SET_SIZE = 500;

	private RingIdentifier[] ids;

	public Swapping(int iterations) {
		this(iterations, 0, ATTACK_NONE, ATTACKER_SELECTION_NONE, 0);
	}

	public Swapping(int iterations, double delta, String attack,
			String attackerSelection, int attackers) {
		super(iterations, "SWAPPING", new Parameter[] {
				new IntParameter("ITERATIONS", iterations),
				new DoubleParameter("DELTA", delta),
				new StringParameter("ATTACK", attack),
				new StringParameter("ATTACKERSELECTION", attackerSelection),
				new IntParameter("ATTACKERS", attackers) });
		this.iterations = iterations;
		this.delta = delta;
		this.attack = attack;
		this.attackerSelection = attackerSelection;
		this.attackers = attackers;
	}

	protected AttackableEmbeddingNode[] generateNodes(Graph g, Random rand) {
		HashSet<Integer> attackers = new HashSet<Integer>();
		if (!ATTACK_NONE.equals(this.attack)
				&& !ATTACKER_SELECTION_NONE.equals(this.attackerSelection)) {
			if (ATTACKER_SELECTION_LARGEST.equals(this.attackerSelection)) {
				attackers = this.selectNodesByDegreeDesc(g.getNodes(),
						this.attackers, rand);
			} else if (ATTACKER_SELECTION_SMALLEST
					.equals(this.attackerSelection)) {
				attackers = this.selectNodesByDegreeAsc(g.getNodes(),
						this.attackers, rand);
			} else if (ATTACKER_SELECTION_MEDIAN.equals(this.attackerSelection)) {
				attackers = this.selectNodesAroundMedian(g.getNodes(),
						this.attackers, rand, MEDIAN_SET_SIZE);
			} else if (ATTACKER_SELECTION_RANDOM.equals(this.attackerSelection)) {
				attackers = this.selectNodesRandomly(g.getNodes(),
						this.attackers, rand);
			} else {
				throw new IllegalArgumentException(this.attackerSelection
						+ " is an unknown attacker selection in Swapping");
			}
		}
		AttackableEmbeddingNode[] nodes = new AttackableEmbeddingNode[g
				.getNodes().length];
		for (int i = 0; i < g.getNodes().length; i++) {
			// double pos = ((RingNode) g.getNodes()[i]).getID().pos;
			if (attackers.contains(i)) {
				if (ATTACK_CONTRACTION.equals(this.attack)) {
					nodes[i] = new SwappingAttackerContraction(i, g, this);
				} else if (ATTACK_CONVERGENCE.equals(this.attack)) {
					nodes[i] = new SwappingAttackerConvergence(i, g, this);
				} else if (ATTACK_KLEINBERG.equals(this.attack)) {
					nodes[i] = new SwappingAttackerKleinberg(i, g, this);
				} else {
					throw new IllegalArgumentException(this.attack
							+ " is an unknown attack in LMC");
				}
			} else {
				nodes[i] = new SwappingNode(i, g, this);
			}
		}
		this.init(g, nodes);
		this.initIds(g);
		return nodes;
	}

	protected AttackableEmbeddingNode[] generateSelectionSet(
			AttackableEmbeddingNode[] nodes, Random rand) {
		return nodes.clone();
	}

	/**
	 * init IdSpace from a graph g
	 * 
	 * @param g
	 */
	public void initIds(Graph g) {
		GraphProperty[] gp = g.getProperties("ID_SPACE");
		GraphProperty p = gp[gp.length - 1];
		DIdentifierSpace idSpaceD = (DIdentifierSpace) p;
		this.ids = new RingIdentifier[g.getNodes().length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = (RingIdentifier) idSpaceD.getPartitions()[i]
					.getRepresentativeID();
		}
	}

	/**
	 * @return the ids
	 */
	public RingIdentifier[] getIds() {
		return this.ids;
	}

	public SwappingNode getRandomNeighbor(Graph g, int[] neighs, Random rand) {
		return (SwappingNode) g.getNode(neighs[rand.nextInt(neighs.length)]);
	}

}

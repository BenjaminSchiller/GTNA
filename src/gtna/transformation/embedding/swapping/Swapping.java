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
package gtna.transformation.embedding.swapping;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.routing.node.RingNode;
import gtna.transformation.embedding.Embedding;
import gtna.transformation.embedding.EmbeddingNode;

import java.util.HashSet;
import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class Swapping extends Embedding {

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

	public Swapping(int iterations) {
		this(iterations, 0, ATTACK_NONE, ATTACKER_SELECTION_NONE, 0);
	}

	public Swapping(int iterations, double delta, String attack,
			String attackerSelection, int attackers) {
		super(iterations, "SWAPPING", new String[] { "ITERATIONS", "DELTA",
				"ATTACK", "ATTACKER_SELECTION", "ATTACKERS" }, new String[] {
				"" + iterations, "" + delta, attack, attackerSelection,
				"" + attackers });
		this.iterations = iterations;
		this.delta = delta;
		this.attack = attack;
		this.attackerSelection = attackerSelection;
		this.attackers = attackers;
	}

	protected EmbeddingNode[] generateNodes(Graph g, Random rand) {
		HashSet<NodeImpl> attackers = new HashSet<NodeImpl>();
		if (!ATTACK_NONE.equals(this.attack)
				&& !ATTACKER_SELECTION_NONE.equals(this.attackerSelection)) {
			if (ATTACKER_SELECTION_LARGEST.equals(this.attackerSelection)) {
				attackers = this.selectNodesByDegreeDesc(g.nodes,
						this.attackers, rand);
			} else if (ATTACKER_SELECTION_SMALLEST
					.equals(this.attackerSelection)) {
				attackers = this.selectNodesByDegreeAsc(g.nodes,
						this.attackers, rand);
			} else if (ATTACKER_SELECTION_MEDIAN.equals(this.attackerSelection)) {
				attackers = this.selectNodesAroundMedian(g.nodes,
						this.attackers, rand, MEDIAN_SET_SIZE);
			} else if (ATTACKER_SELECTION_RANDOM.equals(this.attackerSelection)) {
				attackers = this.selectNodesRandomly(g.nodes, this.attackers,
						rand);
			} else {
				throw new IllegalArgumentException(this.attackerSelection
						+ " is an unknown attacker selection in Swapping");
			}
		}
		EmbeddingNode[] nodes = new EmbeddingNode[g.nodes.length];
		for (int i = 0; i < g.nodes.length; i++) {
			double pos = ((RingNode) g.nodes[i]).getID().pos;
			if (attackers.contains(g.nodes[i])) {
				if (ATTACK_CONTRACTION.equals(this.attack)) {
					nodes[i] = new SwappingAttackerContraction(i, pos, this);
				} else if (ATTACK_CONVERGENCE.equals(this.attack)) {
					nodes[i] = new SwappingAttackerConvergence(i, pos, this);
				} else if (ATTACK_KLEINBERG.equals(this.attack)) {
					nodes[i] = new SwappingAttackerKleinberg(i, pos, this);
				} else {
					throw new IllegalArgumentException(this.attack
							+ " is an unknown attack in LMC");
				}
			} else {
				nodes[i] = new SwappingNode(i, pos, this);
			}
		}
		this.init(g, nodes);
		return nodes;
	}

	protected EmbeddingNode[] generateSelectionSet(EmbeddingNode[] nodes,
			Random rand) {
		return nodes.clone();
	}

}

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
 * LMC.java
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
package gtna.transformation.attackableEmbedding.lmc;

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
 * execute LMC embedding on a graph with node IDs in [0,1) as described in
 * Schiller et al: "Attack Resistant Network Embeddings for Darknets"
 * 
 * @author stefanieroos
 * 
 */

public class LMC extends AttackableEmbedding {

	public static final String MODE_UNRESTRICTED = "UNRESTRICTED";

	public static final String MODE_RESTRICTED = "RESTRICTED";

	public static final String DELTA_1_N = "1_N";

	public static final String DELTA_1_N2 = "1_N_2";

	public static final String ATTACK_CONVERGENCE = "CONVERGENCE";

	public static final String ATTACK_KLEINBERG = "KLEINBERG";

	public static final String ATTACK_CONTRACTION = "CONTRACTION";

	public static final String ATTACK_NONE = "NONE";

	public static final String ATTACKER_SELECTION_LARGEST = "LARGEST";

	public static final String ATTACKER_SELECTION_SMALLEST = "SMALLEST";

	public static final String ATTACKER_SELECTION_MEDIAN = "MEDIAN";

	public static final String ATTACKER_SELECTION_RANDOM = "RANDOM";

	public static final String ATTACKER_SELECTION_NONE = "NONE";

	protected String mode;

	protected double P;

	protected String deltaMode;

	protected double delta;

	protected int C;

	protected String attack;

	protected String attackerSelection;

	protected int attackers;

	public static int MEDIAN_SET_SIZE = 500;

	private RingIdentifier[] ids;

	public LMC(int iterations, String mode, double P, String deltaMode, int C) {
		this(iterations, mode, P, deltaMode, C, ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0);
	}

	public LMC(int iterations, String mode, double P, String deltaMode, int C,
			String attack, String attackerSelection, int attackers) {
		super(iterations, "LMC", new Parameter[] {
				new IntParameter("ITERATIONS", iterations),
				new StringParameter("MODE", mode), new DoubleParameter("P", P),
				new StringParameter("DELTA", deltaMode),
				new IntParameter("C", C),
				new StringParameter("ATTACK", attack),
				new StringParameter("ATTACKERSELECTION", attackerSelection),
				new IntParameter("ATTACKERS", attackers) });
		this.mode = mode;
		this.P = P;
		this.deltaMode = deltaMode;
		this.delta = 0;
		this.C = C;
		this.attack = attack;
		this.attackerSelection = attackerSelection;
		this.attackers = attackers;
	}

	protected AttackableEmbeddingNode[] generateNodes(Graph g, Random rand) {
		this.setDelta(g);
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
						+ " is an unknown attacker selection in LMC");
			}
		}
		AttackableEmbeddingNode[] nodes = new AttackableEmbeddingNode[g
				.getNodes().length];

		for (int i = 0; i < g.getNodes().length; i++) {
			// double pos = ((RingNode) g.getNodes()[i]).getID().pos;
			if (attackers.contains(i)) {
				// System.out.println("adding attacker @ " + i + " (D="
				// + g.getNodes()[i].out().length * 2 + ")");
				if (ATTACK_CONTRACTION.equals(this.attack)) {
					nodes[i] = new LMCAttackerContraction(i, g, this);
				} else if (ATTACK_CONVERGENCE.equals(this.attack)) {
					nodes[i] = new LMCAttackerConvergence(i, g, this);
				} else if (ATTACK_KLEINBERG.equals(this.attack)) {
					nodes[i] = new LMCAttackerKleinberg(i, g, this);
				} else {
					throw new IllegalArgumentException(this.attack
							+ " is an unknown attack in LMC");
				}
			} else {
				nodes[i] = new LMCNode(i, g, this);
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
	 * initializes delta depending on the configuration parameter deltaMode and
	 * possibly the graph g
	 * 
	 * @param g
	 *            graph on which the selection of delta might depend
	 */
	protected void setDelta(Graph g) {
		if (DELTA_1_N.equals(this.deltaMode)) {
			this.delta = 1.0 / (double) g.getNodes().length;
		} else if (DELTA_1_N2.equals(this.deltaMode)) {
			this.delta = 1.0 / (double) (g.getNodes().length * g.getNodes().length);
		} else {
			this.delta = Double.parseDouble(this.deltaMode);
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbedding#getIDs()
	 */
	@Override
	public RingIdentifier[] getIds() {
		// TODO Auto-generated method stub
		return this.ids;
	}

}

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


/**
 * @author "Benjamin Schiller"
 * 
 */
// TODO reimplement LMC
public class LMC {
	// public class LMC extends Embedding {
	//
	// public static final String MODE_UNRESTRICTED = "UNRESTRICTED";
	//
	// public static final String MODE_RESTRICTED = "RESTRICTED";
	//
	// public static final String DELTA_1_N = "1_N";
	//
	// public static final String DELTA_1_N2 = "1_N_2";
	//
	// public static final String ATTACK_CONVERGENCE = "CONVERGENCE";
	//
	// public static final String ATTACK_KLEINBERG = "KLEINBERG";
	//
	// public static final String ATTACK_CONTRACTION = "CONTRACTION";
	//
	// public static final String ATTACK_NONE = "NONE";
	//
	// public static final String ATTACKER_SELECTION_LARGEST = "LARGEST";
	//
	// public static final String ATTACKER_SELECTION_SMALLEST = "SMALLEST";
	//
	// public static final String ATTACKER_SELECTION_MEDIAN = "MEDIAN";
	//
	// public static final String ATTACKER_SELECTION_RANDOM = "RANDOM";
	//
	// public static final String ATTACKER_SELECTION_NONE = "NONE";
	//
	// protected String mode;
	//
	// protected double P;
	//
	// protected String deltaMode;
	//
	// protected double delta;
	//
	// protected int C;
	//
	// protected String attack;
	//
	// protected String attackerSelection;
	//
	// protected int attackers;
	//
	// public static int MEDIAN_SET_SIZE = 500;
	//
	// public LMC(int iterations, String mode, double P, String deltaMode, int
	// C) {
	// this(iterations, mode, P, deltaMode, C, ATTACK_NONE,
	// ATTACKER_SELECTION_NONE, 0);
	// }
	//
	// public LMC(int iterations, String mode, double P, String deltaMode, int
	// C,
	// String attack, String attackerSelection, int attackers) {
	// super(iterations, "LMC", new String[] { "ITERATIONS", "MODE", "P",
	// "DELTA", "C", "ATTACK", "ATTACKER_SELECTION", "ATTACKERS" },
	// new String[] { "" + iterations, mode, "" + P, deltaMode,
	// "" + C, attack, attackerSelection, "" + attackers });
	// this.mode = mode;
	// this.P = P;
	// this.deltaMode = deltaMode;
	// this.delta = 0;
	// this.C = C;
	// this.attack = attack;
	// this.attackerSelection = attackerSelection;
	// this.attackers = attackers;
	// }
	//
	// protected EmbeddingNode[] generateNodes(Graph g, Random rand) {
	// this.setDelta(g);
	// HashSet<Node> attackers = new HashSet<Node>();
	// if (!ATTACK_NONE.equals(this.attack)
	// && !ATTACKER_SELECTION_NONE.equals(this.attackerSelection)) {
	// if (ATTACKER_SELECTION_LARGEST.equals(this.attackerSelection)) {
	// attackers = this.selectNodesByDegreeDesc(g.nodes,
	// this.attackers, rand);
	// } else if (ATTACKER_SELECTION_SMALLEST
	// .equals(this.attackerSelection)) {
	// attackers = this.selectNodesByDegreeAsc(g.nodes,
	// this.attackers, rand);
	// } else if (ATTACKER_SELECTION_MEDIAN.equals(this.attackerSelection)) {
	// attackers = this.selectNodesAroundMedian(g.nodes,
	// this.attackers, rand, MEDIAN_SET_SIZE);
	// } else if (ATTACKER_SELECTION_RANDOM.equals(this.attackerSelection)) {
	// attackers = this.selectNodesRandomly(g.nodes, this.attackers,
	// rand);
	// } else {
	// throw new IllegalArgumentException(this.attackerSelection
	// + " is an unknown attacker selection in LMC");
	// }
	// }
	// EmbeddingNode[] nodes = new EmbeddingNode[g.nodes.length];
	// for (int i = 0; i < g.nodes.length; i++) {
	// double pos = ((RingNode) g.nodes[i]).getID().pos;
	// if (attackers.contains(g.nodes[i])) {
	// // System.out.println("adding attacker @ " + i + " (D="
	// // + g.nodes[i].out().length * 2 + ")");
	// if (ATTACK_CONTRACTION.equals(this.attack)) {
	// nodes[i] = new LMCAttackerContraction(i, pos, this);
	// } else if (ATTACK_CONVERGENCE.equals(this.attack)) {
	// nodes[i] = new LMCAttackerConvergence(i, pos, this);
	// } else if (ATTACK_KLEINBERG.equals(this.attack)) {
	// nodes[i] = new LMCAttackerKleinberg(i, pos, this);
	// } else {
	// throw new IllegalArgumentException(this.attack
	// + " is an unknown attack in LMC");
	// }
	// } else {
	// nodes[i] = new LMCNode(i, pos, this);
	// }
	// }
	// this.init(g, nodes);
	// return nodes;
	// }
	//
	// protected EmbeddingNode[] generateSelectionSet(EmbeddingNode[] nodes,
	// Random rand) {
	// return nodes.clone();
	// }
	//
	// /**
	// * initializes delta depending on the configuration parameter deltaMode
	// and
	// * possibly the graph g
	// *
	// * @param g
	// * graph on which the selection of delta might depend
	// */
	// protected void setDelta(Graph g) {
	// if (DELTA_1_N.equals(this.deltaMode)) {
	// this.delta = 1.0 / (double) g.nodes.length;
	// } else if (DELTA_1_N2.equals(this.deltaMode)) {
	// this.delta = 1.0 / (double) (g.nodes.length * g.nodes.length);
	// } else {
	// this.delta = Double.parseDouble(this.deltaMode);
	// }
	// }

}

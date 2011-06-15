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
package gtna.transformation.sorting.lmc;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.routing.node.RingNode;
import gtna.transformation.sorting.Sorting;
import gtna.transformation.sorting.SortingNode;

import java.util.HashSet;
import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class LMC extends Sorting {
	public static final String MODE_X = "X";

	public static final String MODE_Y = "Y";

	public static final String MODE_Z = "Z";

	public static final String DELTA_1_N = "1_N";

	public static final String DELTA_1_N2 = "1_N_2";

	public static final String ATTACK_CONVERGENCE = "CONVERGENCE";

	public static final String ATTACK_KLEINBERG = "KLEINBERG";

	public static final String ATTACK_CONTRACTION = "CONTRACTION";

	public static final String ATTACK_CONVERGENCE_WC = "CONVERGENCE_WC";

	public static final String ATTACK_KLEINBERG_WC = "KLEINBERG_WC";

	public static final String ATTACK_CONTRACTION_WC = "CONTRACTION_WC";

	protected String mode;

	protected double P;

	protected String deltaMode;

	protected double delta;

	protected int C;

	protected boolean includeDegree1;

	protected String attack;

	protected int attackers;

	public LMC(int iterations, String mode, double P, String deltaMode, int C,
			boolean includeDegree1, String attack, int attackers) {
		super(iterations, "LMC", new String[] { "ITERATIONS", "MODE", "P",
				"DELTA", "C", "INCLUDE_DEGREE_1", "ATTACK", "ATTACKERS" },
				new String[] { "" + iterations, mode, "" + P, deltaMode,
						"" + C, "" + includeDegree1, attack, "" + attackers });
		this.mode = mode;
		this.P = P;
		this.deltaMode = deltaMode;
		this.delta = 0;
		this.C = C;
		this.includeDegree1 = includeDegree1;
		this.attack = attack;
		this.attackers = attackers;
	}

	protected SortingNode[] generateNodes(Graph g, Random rand) {
		this.setDelta(g);
		HashSet<NodeImpl> attackers = this.selectNodesRandomly(g.nodes,
				this.attackers, rand);
		SortingNode[] nodes = new SortingNode[g.nodes.length];
		for (int i = 0; i < g.nodes.length; i++) {
			double pos = ((RingNode) g.nodes[i]).getID().pos;
			if (attackers.contains(g.nodes[i])) {
				System.out.println("adding attacker @ " + i);
				if (ATTACK_CONTRACTION.equals(this.attack)) {
					nodes[i] = new LMCAttackerContraction(i, pos, this);
				} else if (ATTACK_CONVERGENCE.equals(this.attack)) {
					nodes[i] = new LMCAttackerConvergence(i, pos, this);
				} else if (ATTACK_KLEINBERG.equals(this.attack)) {
					nodes[i] = new LMCAttackerKleinberg(i, pos, this);
				} else if (ATTACK_CONTRACTION_WC.equals(this.attack)) {
					nodes[i] = new LMCWCAttackerContraction(i, pos, this);
				} else if (ATTACK_CONVERGENCE_WC.equals(this.attack)) {
					nodes[i] = new LMCWCAttackerConvergence(i, pos, this);
				} else if (ATTACK_KLEINBERG_WC.equals(this.attack)) {
					nodes[i] = new LMCWCAttackerKleinberg(i, pos, this);
				} else {
					throw new IllegalArgumentException(this.attack
							+ " is an unknown attack in LMC");
				}
			} else {
				if (ATTACK_CONTRACTION.equals(this.attack)
						|| ATTACK_CONVERGENCE.equals(this.attack)
						|| ATTACK_KLEINBERG.equals(this.attack)) {
					nodes[i] = new LMCNode(i, pos, this);
				} else if (ATTACK_CONTRACTION_WC.equals(this.attack)
						|| ATTACK_CONVERGENCE_WC.equals(this.attack)
						|| ATTACK_KLEINBERG_WC.equals(this.attack)) {
					nodes[i] = new LMCWCNode(i, pos, this);
				}
			}
		}
		this.init(g, nodes);
		return nodes;
	}

	protected SortingNode[] generateSelectionSet(SortingNode[] nodes,
			Random rand) {
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
			this.delta = 1.0 / (double) g.nodes.length;
		} else if (DELTA_1_N2.equals(this.deltaMode)) {
			this.delta = 1.0 / (double) (g.nodes.length * g.nodes.length);
		} else {
			this.delta = Double.parseDouble(this.deltaMode);
		}
	}

}

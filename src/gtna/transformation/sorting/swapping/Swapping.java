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
package gtna.transformation.sorting.swapping;

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
public class Swapping extends Sorting {
	public static final String MODE_X = "X";

	public static final String MODE_Y = "Y";

	public static final String MODE_Z = "Z";

	public static final String ATTACK_CONVERGENCE = "CONVERGENCE";

	public static final String ATTACK_KLEINBERG = "KLEINBERG";

	public static final String ATTACK_CONTRACTION = "CONTRACTION";

	public static final String ATTACK_CONVERGENCE_WC = "CONVERGENCE_WC";

	public static final String ATTACK_KLEINBERG_WC = "KLEINBERG_WC";

	public static final String ATTACK_CONTRACTION_WC = "CONTRACTION_WC";

	public static final String ATTACK_NONE = "NONE";

	protected int interations;

	protected String mode;

	protected String attack;

	protected int attackers;
	
	public Swapping(int iterations, String mode){
		this(iterations, mode, ATTACK_NONE, 0);
	}

	public Swapping(int iterations, String mode, String attack, int attackers) {
		super(iterations, "Swapping", new String[] { "ITERATIONS", "MODE",
				"ATTACK", "ATTACKERS" }, new String[] { "" + iterations, mode,
				attack, "" + attackers });
		this.iterations = iterations;
		this.mode = mode;
		this.attack = attack;
		this.attackers = attackers;
	}

	protected SortingNode[] generateNodes(Graph g, Random rand) {
		HashSet<NodeImpl> attackers = ATTACK_NONE.equals(this.attack) ? new HashSet<NodeImpl>()
				: this.selectNodesRandomly(g.nodes, this.attackers, rand);
		SortingNode[] nodes = new SortingNode[g.nodes.length];
		for (int i = 0; i < g.nodes.length; i++) {
			double pos = ((RingNode) g.nodes[i]).getID().pos;
			if (attackers.contains(g.nodes[i])) {
				System.out.println("adding attacker @ " + i);
				if (ATTACK_CONTRACTION.equals(this.attack)) {
					nodes[i] = new SwappingAttackerContraction(i, pos, this);
				} else if (ATTACK_CONVERGENCE.equals(this.attack)) {
					nodes[i] = new SwappingAttackerConvergence(i, pos, this);
				} else if (ATTACK_KLEINBERG.equals(this.attack)) {
					nodes[i] = new SwappingAttackerKleinberg(i, pos, this);
				} else if (ATTACK_CONTRACTION_WC.equals(this.attack)) {
					nodes[i] = new SwappingWCAttackerContraction(i, pos, this);
				} else if (ATTACK_CONVERGENCE_WC.equals(this.attack)) {
					nodes[i] = new SwappingWCAttackerConvergence(i, pos, this);
				} else if (ATTACK_KLEINBERG_WC.equals(this.attack)) {
					nodes[i] = new SwappingWCAttackerKleinberg(i, pos, this);
				} else {
					throw new IllegalArgumentException(this.attack
							+ " is an unknown attack in LMC");
				}
			} else {
				if (ATTACK_NONE.equals(this.attack)
						|| ATTACK_CONTRACTION.equals(this.attack)
						|| ATTACK_CONVERGENCE.equals(this.attack)
						|| ATTACK_KLEINBERG.equals(this.attack)) {
					nodes[i] = new SwappingNode(i, pos, this);
				} else if (ATTACK_CONTRACTION_WC.equals(this.attack)
						|| ATTACK_CONVERGENCE_WC.equals(this.attack)
						|| ATTACK_KLEINBERG_WC.equals(this.attack)) {
					nodes[i] = new SwappingWCNode(i, pos, this);
				}
			}
		}
		this.init(g, null);
		return nodes;
	}

	protected SortingNode[] generateSelectionSet(SortingNode[] nodes,
			Random rand) {
		return nodes.clone();
	}

}

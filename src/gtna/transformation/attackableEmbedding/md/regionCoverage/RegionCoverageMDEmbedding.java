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
 * RegionCoverageMDEmbedding.java
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
package gtna.transformation.attackableEmbedding.md.regionCoverage;

import gtna.graph.Graph;
import gtna.id.md.MDIdentifierSpaceSimple.DistanceMD;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.md.AttackerIQDMDEmbedding;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashSet;
import java.util.Random;

/**
 * @author stef
 * 
 */
public class RegionCoverageMDEmbedding extends AttackerIQDMDEmbedding {

	int max;

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
	public RegionCoverageMDEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, DistanceMD distance, int dim,
			double epsilon, boolean checkold, boolean adjustone, int max) {
		super(iterations, "REGION_COVERAGE_MD", idMethod, deMethod, distance,
				dim, epsilon, checkold, adjustone,
				new Parameter[] { new IntParameter("MAX_LOG", max) });
		this.max = max;
	}

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
	public RegionCoverageMDEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, DistanceMD distance, int dim,
			double epsilon, boolean checkold, boolean adjustone, int max,
			AttackerType type, AttackerSelection selection, int attackercount) {
		super(iterations, "REGION_COVERAGE_MD", idMethod, deMethod, distance,
				dim, epsilon, checkold, adjustone, type, selection,
				attackercount, new Parameter[] { new IntParameter("MAX_LOG",
						max) });
		this.max = max;
	}

	public int getMax() {
		return this.max;
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
				nodes[i] = new RegionCoverageMDNode(i, g, this, true);
			} else {
				nodes[i] = new RegionCoverageMDNode(i, g, this, false);
			}
		}
		this.init(g, nodes);
		this.initIds();
		return nodes;
	}

	public RegionCoverageMDEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, DistanceMD distance, int dimension,
			double epsilon, boolean checkold, boolean adjustone, int max,
			boolean add) {
		super(iterations, "REGION_COVERAGE_MD", idMethod, deMethod, distance,
				dimension, epsilon, checkold, adjustone,
				add ? new Parameter[] { new IntParameter("MAX_LOG", max) }
						: new Parameter[] {
								new IntParameter("ITERATIONS", iterations),
								new IntParameter("DIMENSION", dimension),
								new StringParameter("DISTANCE",
										distance.toString()) }, add);
		this.max = max;
	}

	public RegionCoverageMDEmbedding(int iterations, IdentifierMethod idMethod,
			DecisionMethod deMethod, DistanceMD distance, int dimension,
			double epsilon, boolean checkold, boolean adjustone, int max,
			AttackerType type, AttackerSelection selection, int attackercount,
			boolean add) {
		super(iterations, "REGION_COVERAGE_MD", idMethod, deMethod, distance,
				dimension, epsilon, checkold, adjustone, type, selection,
				attackercount, add ? new Parameter[] { new IntParameter(
						"MAX_LOG", max) } : new Parameter[] {
						new IntParameter("ITERATIONS", iterations),
						new IntParameter("DIMENSION", dimension),
						new StringParameter("DISTANCE", distance.toString()) },
				add);
		this.max = max;

	}

}

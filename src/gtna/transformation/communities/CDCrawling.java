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
 * CDCrawling.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import java.util.ArrayList;
import java.util.HashMap;

import gtna.communities.CommunityList;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.Util;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * Encapsulates the Community Detection Algorithm based on Crawling, originally
 * introduced by Nortbert Blenn et al. in "Crawling and Detecting Community
 * Structure in Online Social Networks using Local Information". The main idea
 * is that instead of randomly moving through the graph, the algorithm always
 * selects the next node with the highest connection ratio into the already
 * crawled graph as the next step. This is done until the change in this ratio
 * drops below a certain threshold, which then finishes a community.
 * 
 * The algorithm can be used to crawl the graph one community after another,
 * this behaviour is implemented as the "ORIGINAL" modus and activated by
 * passing a <code>NodePicker</code> as well as <code>CDCrawling.ORIGINAL</code>
 * into the constructor. As this will depend a lot on the starting node, two
 * other operation modes are implemented as well:
 * 
 * SINGLE_PASS: Activated by passing a <code>NodePicker</code> object in the
 * constructor as well as <code>CDCrawling.SINGLE_PASS</code>, this mode will
 * cause the algorithm to touch every node exactly one time. This is done by
 * starting with a node determined by the <code>NodePicker</code> and then
 * ignoring all the nodes that are already in a community. This is repeated
 * until every node has a community.
 * 
 * MULTI_PASS: Activated by passing a <code>SimilarityMeasureContainer</code>
 * object in the constructor, this will cause the algorithm to run with every
 * node in the graph as the starting node. The communities detected around the
 * nodes are then compared using the supplied SimilarityMeasure within the
 * SimilarityMeasureContainer, and the two most similar nodes are assumed to be
 * in the same community.
 * 
 * @author Philipp Neubrand
 * 
 */
public class CDCrawling extends Transformation {

	public static final int MULTI_PASS = 2;
	public static final int SINGLE_PASS = 1;
	public static final int ORIGINAL = 0;
	private int mode;
	private NodePicker np;
	private SimilarityMeasureContainer smc;
	private double minDelta;

	/**
	 * Standard constructor for either ORIGINAL or SINGLE_PASS mode.
	 * 
	 * @param minDelta
	 *            The threshold.
	 * @param np
	 *            The NodePicker that is to be used.
	 * @param mode
	 *            Either ORIGINAL or SINGLE_PASS.
	 */
	public CDCrawling(double minDelta, NodePicker np, int mode) {
		super("CD_CRAWLING", Util.mergeArrays(new Parameter[] {
				new DoubleParameter("MINDELTA", minDelta),
				new StringParameter("MODE", getModeString(mode)) },
				np.getParameterArray()));
		if (mode != CDCrawling.ORIGINAL && mode != CDCrawling.SINGLE_PASS)
			throw new IllegalArgumentException("Invalid mode");

		this.mode = mode;
		this.np = np;
		this.minDelta = minDelta;

	}

	/**
	 * Standard constructor for MULTI_PASS mode.
	 * 
	 * @param minDelta
	 *            The threshold.
	 * @param smc
	 *            The similarity measure container.
	 */
	public CDCrawling(double minDelta, SimilarityMeasureContainer smc) {
		super("CD_CRAWLING", Util.mergeArrays(new Parameter[] {
				new DoubleParameter("MINDELTA", minDelta),
				new StringParameter("MODE", "MULTI_PASS") },
				smc.getParameterArray()));
		this.mode = CDCrawling.MULTI_PASS;
		this.smc = smc;
		this.minDelta = minDelta;
	}

	@Override
	public Graph transform(Graph g) {
		if (!applicable(g))
			return g;

		CommunityList cl = null;
		HashMap<Integer, Integer> temp = null;
		if (mode == CDCrawling.ORIGINAL) {
			np.addAll(g.getNodes());
			temp = getCommunityAroundNode(np.pop(), g, null, false);
			cl = new CommunityList(temp);

		} else if (mode == CDCrawling.SINGLE_PASS) {
			np.addAll(g.getNodes());

			Node n;
			HashMap<Integer, Boolean> ignore = new HashMap<Integer, Boolean>();
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

			while (!np.empty()) {
				n = np.pop();

				temp = this.getCommunityAroundNode(n, g, ignore, true);

				for (int akt : temp.keySet()) {
					map.put(akt, temp.get(akt));
					np.remove(akt);
					ignore.put(akt, true);
				}
			}

			cl = new CommunityList(map);

		} else if (mode == CDCrawling.MULTI_PASS) {
			smc.setSize(g.getNodes().length);
			for (Node akt : g.getNodes()) {
				temp = this.getCommunityAroundNode(akt, g, null, true);
				smc.addCommunityOfNode(akt, temp);
			}

			cl = smc.getCommunityList();
		}

		g.addProperty(g.getNextKey("COMMUNITIES"), cl);
		return g;
	}

	private static String getModeString(int mode) {
		if (mode == CDCrawling.ORIGINAL)
			return "ORIGINAL";
		else if (mode == CDCrawling.SINGLE_PASS)
			return "SINGLE_PASS";
		else
			return "MULTI_PASS";
	}

	private HashMap<Integer, Integer> getCommunityAroundNode(Node n, Graph g,
			HashMap<Integer, Boolean> ignore, boolean breakAfterFirstCommunity) {
		double score;
		double maxScore;
		double minScore;
		double diff;
		double average;

		Node nextNode;
		Node neighbor;
		HashMap<Node, Integer> refs = new HashMap<Node, Integer>();
		HashMap<Integer, Integer> ret = new HashMap<Integer, Integer>();
		ArrayList<Node> visited = new ArrayList<Node>();
		refs.put(n, 0);

		int comIndex = 0;

		while (!refs.isEmpty()) {
			nextNode = null;
			maxScore = 0;
			double oldScore = 0;
			minScore = Double.MAX_VALUE;
			for (Node akt : refs.keySet()) {
				if ((ignore != null && ignore.containsKey(akt))
						|| visited.contains(akt))
					continue;

				score = ((double) refs.get(akt)) / akt.getDegree();
				if (score >= maxScore) {
					maxScore = score;
					nextNode = akt;
				}
				if (score < minScore) {
					minScore = score;
				}
			}

			refs.remove(nextNode);
			visited.add(nextNode);

			ret.put(nextNode.getIndex(), comIndex);

			for (Edge akt : nextNode.getEdges()) {
				if (nextNode.getIndex() == akt.getDst()) {
					neighbor = g.getNode(akt.getSrc());
				} else {
					neighbor = g.getNode(akt.getDst());
				}
				if ((ignore != null && ignore.containsKey(neighbor))
						|| visited.contains(neighbor))
					continue;

				if (refs.containsKey(neighbor))
					refs.put(neighbor, refs.get(neighbor) + 1);
				else
					refs.put(neighbor, 1);
			}

			diff = maxScore - oldScore;
			average = (maxScore - minScore) / 2;

			if (diff > minDelta && Math.abs(diff) > average) {
				if (breakAfterFirstCommunity) {
					refs.clear();
				} else {
					comIndex++;
				}
			}

			oldScore = maxScore;
		}

		return ret;

	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

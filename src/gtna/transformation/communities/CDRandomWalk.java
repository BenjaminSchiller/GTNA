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
 * CDRandomWalk.java
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

import gtna.communities.Community;
import gtna.communities.CommunityList;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.Util;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * Encapsulates the Community Detection algorithm based on random walks,
 * introduced in "Detection of local community structures in complex dynamic
 * networks with random walks" by G.S. Thakur et al.
 * http://www.cise.ufl.edu/~gsthakur/docs/detection_local.pdf
 * 
 * The basic concept to find the community around a node n is as follows:
 * <ul>
 * <li>perform a couple of random walks starting from node n, each walk is
 * stored as a community</li>
 * <li>exchange nodes among the random walk communities to minimize the
 * potential gain from moving a node in a different community</li>
 * </ul>
 * 
 * There are a couple of issues with the description of the algorithm in the
 * paper:
 * <ul>
 * <li>The formula to calculate the potential gain is described as
 * "y_ji = ((2 * d_ij) / (k_ji * (k_ji -1)))", which is a divison by 0 for k_ij
 * = 0 or k_ij = 1. We fix this by just returning 0 in these cases. In addition,
 * we propose a similar but simpler formula "y_ji = d_ji - k_ji" which should be
 * similar.</li>
 * <li>There is a procedure used in the paper that is not explained,
 * "do_crossover(...)"</li>
 * <li>The term "non-picked vertices" is not defined and could be interpreted as
 * "non-picked neighbors" or "non-picked vertices", which in the second case
 * would mean that the algorithm is not local</li>
 * </ul>
 * Unfortunatelly the original authors did not react to an email regarding these
 * problems.
 * 
 * As the algorithm only detects a community for one starting node but we are
 * interested in detecting the community structure of the whole graph, the
 * algorithm had to be extended to do this. There are two different operation
 * modes: SINGLE_PASS and MULTI_PASS that are activated depending on the
 * constructor that is used.
 * 
 * SINGLE_PASS: Activated by passing a <code>NodePicker</code> object in the
 * constructor, this mode will cause the algorithm to touch every node exactly
 * one time. This is done by starting with a node determined by the
 * <code>NodePicker</code> and then ignoring all the nodes that are already in a
 * community. This is repeated until every node has a community.
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
public class CDRandomWalk extends Transformation {

	private static final int MULTI_PASS = 1;
	private static final int SINGLE_PASS = 2;
	private int n;
	private int mode;
	private NodePicker picker;
	private SimilarityMeasureContainer matrix;

	private boolean useNewFormula;

	/**
	 * Creates a new CDRandomWalk object in SINGLE_PASS mode with the supplied
	 * NodePicker.
	 * 
	 * @param maxRandomWalks
	 *            The maximum number of random walks.
	 * @param useNewFormula
	 *            Whether or not to use the alternate formula.
	 * @param picker
	 *            The NodePicker to pick the nodes.
	 */
	public CDRandomWalk(int maxRandomWalks, boolean useNewFormula,
			NodePicker picker) {
		super("CD_RANDOMWALK", Util.mergeArrays(new Parameter[] { new IntParameter("N",
				maxRandomWalks), new BooleanParameter("NEW_FORMULA", useNewFormula)}, picker.getParameterArray()));
		this.useNewFormula = useNewFormula;
		this.n = maxRandomWalks;
		mode = CDRandomWalk.SINGLE_PASS;
		this.picker = picker;
	}

	/**
	 * Creates a new CDRandomWalk obect in MULTI_PASS mode with the supplied
	 * SimilarityMeasureContainer.
	 * 
	 * @param maxRandomWalks
	 *            The maximum number of random walks.
	 * @param useNewFormula
	 *            Whether or not to use the alternate formula.
	 * @param smc
	 *            The similarity measure to compare the nodes.
	 */
	public CDRandomWalk(int maxRandomWalks, boolean useNewFormula,
			SimilarityMeasureContainer smc) {
		super("CD_RANDOMWALK", Util.mergeArrays(new Parameter[] { new IntParameter("N",
				maxRandomWalks),  new BooleanParameter("NEW_FORMULA", useNewFormula) }, smc.getParameterArray()));
		this.useNewFormula = useNewFormula;
		this.matrix = smc;
		this.n = maxRandomWalks;
		mode = CDRandomWalk.MULTI_PASS;
	}

	private RWCommunity getCommunityAroundNode(Node s, Graph g,
			HashMap<Integer, Boolean> ignore) {

		RWCommunityList<RWCommunity> cl = randomWalk(s, g, ignore);

		addUnpicked(cl, s, g, ignore);

		for (RWCommunity akt : cl.getCommunities()) {
			for (int aktNode : akt.getNodes()) {
				akt.getNodeRank(aktNode);
			}

			akt.sortNodes();
			akt.getCommunityRank();
		}

		cl.sortCommunities();

		boolean changed = true;
		while (changed) {
			changed = false;

			for (int i = 0; i < cl.getCommunities().length - 1; i++) {
				if (doExchange(cl.getCommunities()[i],
						cl.getCommunities()[i + 1])) {
					changed = true;
				}
			}

			cl.removeEmptyCommunities();

			cl.sortCommunities();
		}

		return cl.getCommunities()[0];

	}

	private void addUnpicked(RWCommunityList<RWCommunity> cl, Node s, Graph g,
			HashMap<Integer, Boolean> ignore) {

		RWCommunity nw = new RWCommunity(
				cl.getCommunities()[cl.getCommunities().length - 1].getIndex() + 1,
				g, useNewFormula);
		for (Node a : g.getNodes()) {
			if (!cl.containsNode(a.getIndex())
					&& (ignore == null || !ignore.containsKey(a)))
				nw.addNode(a.getIndex());
		}

		cl.addCommunity(nw);
	}

	private boolean doExchange(RWCommunity c1, RWCommunity c2) {

		boolean changed = false;
		double temp;
		int[] n1 = c1.getNodes();
		int[] n2 = c2.getNodes();
		for (int akt : n1) {
			if (c2.contains(akt))
				continue;

			for (int akt2 : n2) {
				if (c1.contains(akt2))
					continue;

				if (c1.getNodeRank(akt) >= c2.getNodeRank(akt2)) {
					temp = c1.getNodeRank(akt);
					if (temp > c2.getNodeRank(akt) && !c2.contains(akt)) {
						// System.out.println(akt + "=>" + c2.hashCode() + ":"
						// + "old:" + temp + "new:"
						// + c2.calculateGamma(akt));
						c2.addNode(akt);
						c1.removeNode(akt);
						c1.getCommunityRank();
						c2.getCommunityRank();
						changed = true;
						continue;
					}
				} else {
					temp = c2.getNodeRank(akt2);
					if (temp > c1.getNodeRank(akt2) && !c1.contains(akt2)) {
						// System.out.println(akt2 + "=>" + c1.hashCode() + ":"
						// + "old:" + temp + "new:"
						// + c1.calculateGamma(akt2));
						c1.addNode(akt2);
						c2.removeNode(akt2);
						c1.getCommunityRank();
						c2.getCommunityRank();
						changed = true;
						continue;
					}
				}
			}
		}

		return changed;
	}

	private RWCommunityList<RWCommunity> randomWalk(Node s, Graph g,
			HashMap<Integer, Boolean> ignore) {
		RWCommunityList<RWCommunity> ret = new RWCommunityList<RWCommunity>();
		boolean finished = false;

		Node aktNode;
		Node next;
		RWCommunity aktCom;
		int count = 0;
		int id = 0;
		while (!finished) {
			aktNode = s;
			aktCom = new RWCommunity(id, g, useNewFormula);
			id++;
			while (true) {
				if (aktCom.contains(aktNode.getIndex()))
					break;

				aktCom.addNode(aktNode.getIndex());
				if (!hasPickableNeighbors(aktNode, g, ignore))
					break;

				do {
					next = g.getNode(aktNode.getOutgoingEdges()[(int) Math
							.floor(Math.random() * aktNode.getOutDegree())]);
				} while (!(ignore == null) && ignore.containsKey(next));
				aktNode = next;
			}
			count++;
			finished = ret.contains(aktCom) || count > n;

			ret.addCommunity(aktCom);
		}
		return ret;
	}

	private boolean hasPickableNeighbors(Node aktNode, Graph g,
			HashMap<Integer, Boolean> ignore) {
		boolean ret = false;
		for (int akt : aktNode.getOutgoingEdges()) {
			if (ignore == null || !ignore.containsKey(g.getNode(akt)))
				ret = true;
		}

		return ret;
	}

	@Override
	public Graph transform(Graph g) {
		if (!applicable(g))
			return g;

		CommunityList cl = null;

		if (mode == CDRandomWalk.SINGLE_PASS) {
			picker.addAll(g.getNodes());

			ArrayList<Community> t = new ArrayList<Community>();
			RWCommunity c;

			Node n;
			HashMap<Integer, Boolean> ignore = new HashMap<Integer, Boolean>();

			while (!picker.empty()) {
				n = picker.pop();

				c = this.getCommunityAroundNode(n, g, ignore);

				for (int akt : c.getNodes()) {
					picker.remove(akt);
					ignore.put(akt, true);
				}

				t.add(c);
			}

			cl = new CommunityList(t);
		} else if (mode == CDRandomWalk.MULTI_PASS) {
			RWCommunity c;
			for (Node akt : g.getNodes()) {
				c = this.getCommunityAroundNode(akt, g, null);
				matrix.addCommunityOfNode(akt, c);
			}

			cl = matrix.getCommunityList();
		}
		g.addProperty(g.getNextKey("COMMUNITIES"), cl);
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

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
 * CDExpandingShells.java
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
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * Encapsulates the community detection algorithm based on Expanding Spheres,
 * introduced by James P. Bagrow and Erik M. Bollt in
 * "A Local Method for Detecting Communities" (2008,
 * http://arxiv.org/pdf/cond-mat/0412482.pdf).
 * 
 * The basic idea is that starting with one node the neighbours of the community
 * are added to the community in every step, like an expanding sphere. This is
 * done until the "newExternalEdges / OldExternalEdges < alpha", where alpha is
 * a configuration value for the algorithm.
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
public class CDExpandingSpheres extends Transformation {

	private static final int MULTI_PASS = 1;
	private static final int SINGLE_PASS = 2;
	private double alpha;
	private NodePicker np;
	private SimilarityMeasureContainer smc;
	private int mode;

	/**
	 * Creates a new CDExpandingSpheres algorithm in SINGLE_PASS mode, using the
	 * supplied node picker to determine which node to start from in each step.
	 * 
	 * @param alpha
	 *            The ratio of newExternalEdges / oldExternalEdges below which
	 *            to stop the expansion.
	 * @param np
	 *            The nodepicker that determines the next starting node.
	 */
	public CDExpandingSpheres(double alpha, NodePicker np) {
		super("CD_EXPSPHERES", Util
				.mergeArrays(new Parameter[] {
						new DoubleParameter("ALPHA", alpha),
						new StringParameter("MODE", "SINGLE") },
						np.getParameterArray()));
		this.alpha = alpha;
		this.np = np;
		this.mode = CDExpandingSpheres.SINGLE_PASS;
	}

	/**
	 * Creates a new CDExpandingSpheres algorithm in MULTI_PASS mode, using the
	 * supplied SimilarityMeasureContainer to compare the nodes after the
	 * algorithm was run for all of them.
	 * 
	 * @param alpha
	 *            The ratio of newExternalEdges / oldExternalEdges below which
	 *            to stop the expansion.
	 * @param sm
	 *            The SimilarityMeasureContainer that is used to compare the
	 *            nodes.
	 */
	public CDExpandingSpheres(double alpha, SimilarityMeasureContainer sm) {
		super("CD_EXPSPHERES", Util.mergeArrays(new Parameter[] {
				new DoubleParameter("ALPHA", alpha),
				new StringParameter("MODE", "MULTI") }, sm.getParameterArray()));
		this.alpha = alpha;
		this.mode = CDExpandingSpheres.MULTI_PASS;
		this.smc = sm;
	}

	@Override
	public Graph transform(Graph g) {
		if (!applicable(g))
			return g;

		CommunityList cl = null;

		if (mode == CDExpandingSpheres.SINGLE_PASS) {
			np.addAll(g.getNodes());

			Node akt;
			Community c;
			ArrayList<Community> ct = new ArrayList<Community>();
			int index = 0;
			HashMap<Integer, Boolean> ignore = new HashMap<Integer, Boolean>();
			while (!np.empty()) {
				akt = np.pop();
				c = getCommunityAroundNode(akt, g, ignore, index);
				for (int aktN : c.getNodes()) {
					ignore.put(aktN, true);
					np.remove(aktN);
				}

				ct.add(c);
				index++;

			}

			cl = new CommunityList(ct);

		} else if (mode == CDExpandingSpheres.MULTI_PASS) {
			smc.setSize(g.getNodes().length);
			for (Node akt : g.getNodes()) {
				smc.addCommunityOfNode(akt,
						getCommunityAroundNode(akt, g, null, 0));
			}

			cl = smc.getCommunityList();

		}

		g.addProperty(g.getNextKey("COMMUNITIES"), cl);

		return g;
	}

	private Community getCommunityAroundNode(Node akt, Graph g,
			HashMap<Integer, Boolean> ignore, int id) {

		ArrayList<Integer> sphere = new ArrayList<Integer>();
		sphere.add(akt.getIndex());
		double curr = akt.getDegree()/2;
		double last;
		do {
			last = curr;

			sphere = expandSphere(sphere, g, ignore);

			curr = calcEmergingDegree(sphere, g);
		} while (curr / last >= alpha && !containsAll(sphere, ignore, g));
		return new Community(id, sphere);

	}

	/**
	 * @param sphere
	 * @param ignore
	 * @param g
	 * @return
	 */
	private boolean containsAll(ArrayList<Integer> sphere,
			HashMap<Integer, Boolean> ignore, Graph g) {
		boolean ret = true;
		for(Node akt : g.getNodes()){
			if(!(sphere.contains(akt.getIndex()) || (ignore != null && ignore.containsKey(akt.getIndex()))))
				ret = false;
		}
		
		return ret;
	}

	private double calcEmergingDegree(ArrayList<Integer> sphere, Graph g) {
		int count = 0;
		for (int akt : sphere) {
			for (int akt2 : g.getNode(akt).getOutgoingEdges()) {
				if (!contains(sphere, akt2))
					count++;
			}
		}

		return count;
	}

	private boolean contains(ArrayList<Integer> sphere, int akt2) {
		boolean ret = false;
		for (int akt : sphere)
			if (akt == akt2)
				ret = true;

		return ret;
	}

	private ArrayList<Integer> expandSphere(ArrayList<Integer> sphere, Graph g,
			HashMap<Integer, Boolean> ignore) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (int akt : sphere) {
			if ((!contains(ret, akt))
					&& (ignore == null || !ignore.containsKey(akt)))
				ret.add(akt);

			for (int akt2 : g.getNode(akt).getOutgoingEdges())
				if ((!contains(ret, akt2))
						&& (ignore == null || !ignore.containsKey(akt2)))
					ret.add(akt2);
		}

		return ret;

	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

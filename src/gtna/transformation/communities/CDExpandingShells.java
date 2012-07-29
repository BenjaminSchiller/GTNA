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
 * @author Flipp
 * 
 */
public class CDExpandingShells extends Transformation {

	private static final int MULTI_PASS = 1;
	private static final int SINGLE_PASS = 2;
	private double alpha;
	private NodePicker np;
	private SimilarityMeasure sm;
	private SimilarityMeasureContainer smc;
	private int mode;

	public CDExpandingShells(double alpha, NodePicker np) {
		super("CD_EXPSHELLS", Util.mergeArrays(new Parameter[] { new DoubleParameter("ALPHA",
				alpha), new StringParameter("MODE", "SINGLE")}, np.getParameterArray()));
		this.alpha = alpha;
		this.np = np;
		this.mode = CDExpandingShells.SINGLE_PASS;
	}

	public CDExpandingShells(double alpha, SimilarityMeasure sm) {
		super("CD_EXPSHELLS", Util.mergeArrays(new Parameter[] { new DoubleParameter("ALPHA",
				alpha), new StringParameter("MODE", "MULTI")}, sm.getParameterArray()));
		this.alpha = alpha;
		this.mode = CDExpandingShells.MULTI_PASS;
		this.sm = sm;
	}

	@Override
	public Graph transform(Graph g) {
		if (!applicable(g))
			return g;

		this.smc = new SimilarityMeasureContainer(sm, g.getNodes().length);
		CommunityList cl = null;
		
		if (mode == CDExpandingShells.SINGLE_PASS) {
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
			
		} else if (mode == CDExpandingShells.MULTI_PASS) {
			for(Node akt : g.getNodes()){
				smc.addCommunityAroundNode(akt, getCommunityAroundNode(akt, g, null, 0));
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
		double curr = akt.getDegree();
		double last;
		do {
			last = curr;

			sphere = expandSphere(sphere, g, ignore);

			curr = calcEmergingDegree(sphere, g);

		} while (curr / last > alpha);

		System.out.println("Found com with " + sphere.size());
		return new Community(id, sphere);

	}

	/**
	 * @param sphere
	 * @param g
	 * @return
	 */
	private double calcEmergingDegree(ArrayList<Integer> sphere, Graph g) {
		int count = 0;
		for (int akt : sphere) {
			for (int akt2 : g.getNode(akt).getOutgoingEdges()) {
				if (contains(sphere, akt2))
					count++;
			}
		}

		return count;
	}

	/**
	 * @param sphere
	 * @param akt2
	 * @return
	 */
	private boolean contains(ArrayList<Integer> sphere, int akt2) {
		boolean ret = false;
		for (int akt : sphere)
			if (akt == akt2)
				ret = true;

		return ret;
	}

	/**
	 * @param sphere
	 * @param g
	 * @param ignore
	 * @return
	 */
	private ArrayList<Integer> expandSphere(ArrayList<Integer> sphere, Graph g,
			HashMap<Integer, Boolean> ignore) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (int akt : sphere) {
			if ((!contains(ret, akt)) && (ignore == null || !ignore.containsKey(akt)))
				ret.add(akt);

			for (int akt2 : g.getNode(akt).getOutgoingEdges())
				if ((!contains(ret, akt2)) && (ignore == null || !ignore.containsKey(akt)))
					ret.add(akt2);
		}

		return ret;

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

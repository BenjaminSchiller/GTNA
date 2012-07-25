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
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Flipp
 * 
 * http://www.cise.ufl.edu/~gsthakur/docs/detection_local.pdf
 * 
 */
public class CDRandomWalk extends Transformation {

	private static final int MULTI_PASS = 1;
	private static final int SINGLE_PASS = 2;
	private int n;
	private int mode;
	private NodePicker picker;
	private SimilarityMeasureContainer matrix;
	private SimilarityMeasure t;

	public CDRandomWalk(int n, NodePicker picker) {
		super("CD_RANDOMWALK", new Parameter[] { new IntParameter("N", n) });
		this.n = n;
		mode = CDRandomWalk.SINGLE_PASS;
		this.picker = picker;
	}

	public CDRandomWalk(int n, SimilarityMeasure t) {
		super("CD_RANDOMWALK", new Parameter[] { new IntParameter("N", n) });
		this.t = t;
		this.n = n;
		mode = CDRandomWalk.MULTI_PASS;
	}

	private RWCommunity getCommunityAroundNode(Node s, Graph g,
			HashMap<Integer, Boolean> ignore) {
		
		RWCommunityList cl = randomWalk(s, g, ignore);
		
		addUnpicked(cl, s, g, ignore);
		
		for (RWCommunity akt : cl.getCommunities()) {
			for (int aktNode : akt.getNodes()) {
				akt.calculateGamma(aktNode);
			}

			akt.sortVertices();
			akt.computeGamma();
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

	/**
	 * @param cl
	 * @param s
	 * @param g
	 * @param ignore
	 */
	private void addUnpicked(RWCommunityList cl, Node s, Graph g,
			HashMap<Integer, Boolean> ignore) {
		
		RWCommunity nw = new RWCommunity(
				cl.getCommunities()[cl.getCommunities().length-1].getIndex() + 1,
				g);
		for (Node a : g.getNodes()) {
			if (!cl.containsNode(a.getIndex()) && (ignore == null || !ignore.containsKey(a)))
				nw.add(a.getIndex());
		}

		cl.add(nw);
	}

	/**
	 * @param community
	 * @param community2
	 * @return
	 */
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

				if (c1.getGamma(akt) >= c2.getGamma(akt2)) {
					temp = c1.getGamma(akt);
					if (temp > c2.calculateGamma(akt) && !c2.contains(akt)) {
//						System.out.println(akt + "=>" + c2.hashCode() + ":"
//								+ "old:" + temp + "new:"
//								+ c2.calculateGamma(akt));
						c2.add(akt);
						c1.remove(akt);
						c1.computeGamma();
						c2.computeGamma();
						changed = true;
						continue;
					}
				} else {
					temp = c2.getGamma(akt2);
					if (temp > c1.calculateGamma(akt2) && !c1.contains(akt2)) {
//						System.out.println(akt2 + "=>" + c1.hashCode() + ":"
//								+ "old:" + temp + "new:"
//								+ c1.calculateGamma(akt2));
						c1.add(akt2);
						c2.remove(akt2);
						c1.computeGamma();
						c2.computeGamma();
						changed = true;
						continue;
					}
				}
			}
		}
		
		return changed;
	}

	/**
	 * @param s
	 * @param g
	 * @return
	 */
	private RWCommunityList randomWalk(Node s, Graph g,
			HashMap<Integer, Boolean> ignore) {
		RWCommunityList ret = new RWCommunityList();
		boolean finished = false;

		Node aktNode;
		Node next;
		RWCommunity aktCom;
		int count = 0;
		int id = 0;
		while (!finished) {
			aktNode = s;
			aktCom = new RWCommunity(id, g);
			id++;
			while (true) {
				if (aktCom.contains(aktNode.getIndex()))
					break;

				aktCom.add(aktNode.getIndex());
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

			ret.add(aktCom);
		}
		return ret;
	}

	/**
	 * @param aktNode
	 * @param ignore
	 * @param g
	 * @return
	 */
	private boolean hasPickableNeighbors(Node aktNode, Graph g,
			HashMap<Integer, Boolean> ignore) {
		boolean ret = false;
		for (int akt : aktNode.getOutgoingEdges()) {
			if (ignore == null || !ignore.containsKey(g.getNode(akt)))
				ret = true;
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		if (!applicable(g))
			return g;

		matrix = new SimilarityMeasureContainer(t, g.getNodes().length);
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
				matrix.addCommunityAroundNode(akt, c);
			}

			cl = matrix.getCommunityList();
		}
		g.addProperty(g.getNextKey("COMMUNITIES"), cl);
		return g;
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

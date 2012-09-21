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
 * CommunityDetectionFastUnfolding.java
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

import java.util.HashMap;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.weights.EdgeWeights;
import gtna.graph.weights.NodeWeights;
import gtna.transformation.Transformation;

/**
 * @author Flipp
 * 
 */
public class CDFastUnfolding extends Transformation{

	/**
	 * @param key
	 */
	public CDFastUnfolding() {
		super("CD_FAST_UNFOLDING");
	}

	private double edges;
	private int l;

	public Graph transform(Graph g) {
		l = 0;
		edges = g.getEdges().getEdges().size();
		int[] mastercoms = new int[g.getNodes().length];
		for (int i = 0; i < mastercoms.length; i++)
			mastercoms[i] = i;

		mastercoms = doWork(g, mastercoms);
		
		HashMap<Integer, Integer> c = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < mastercoms.length; i++){
			
			c.put(i, mastercoms[i]);
		}
		
		g.addProperty(g.getNextKey("COMMUNITIES"), new gtna.communities.CommunityList(c));

		
		return g;
	}

	private int[] doWork(Graph g, int[] mastercoms) {
		l++;
		CommunityList coms = new CommunityList();
		EdgeWeights ews = (EdgeWeights) g.getProperty("ew");
		NodeWeights nws = (NodeWeights) g.getProperty("nw");
		Community temp;
		Node[] nodes = g.getNodes();
		for (Node akt : nodes) {
			temp = new Community(akt.getIndex(), g, coms, nws, ews);
			temp.setNode(akt);
			coms.add(temp);

		}

		boolean finished = false;
		boolean changed = false;
		

		MoveValue mv;
		Community tempC;
		// find communities
		while (!finished) {

			finished = true;
			for (Node akt : g.getNodes()) {
				mv = getBestMove(akt, coms, g, ews);
				if (mv.getModDelta() > 0) {

					tempC = coms.getCommunityByNode(akt);
					tempC.removeNode(akt);
					if(tempC.getNodes().length == 0)
						coms.removeCom(tempC);
					
					System.out.println("Moving #"+akt.getIndex()+":" + tempC.getIndex()+"->" +mv.getNewCom()+"("+mv.getModDelta()+")");
					coms.getCommunityByID(mv.getNewCom()).addNode(akt);
					
					
					changed = true;
					finished = false;
				}
			}
		}
		

		if (!changed)
			return mastercoms;

		Graph n = new Graph("temp");
		int distinct = coms.getCommunities().size();
		System.out.println(l + ":" + g.getNodes().length+"->"+distinct);
		Node[] n2 = new Node[distinct];
		NodeWeights nw = new NodeWeights(distinct);
		EdgeWeights ew = new EdgeWeights(distinct);
		Edges edges = new Edges(n2, distinct);
		Edge e;
		
		coms.normalizeIDs();

		
		int[][] edgetemp = new int[distinct][distinct];
		for (Edge akt : g.getEdges().getEdges()) {
			edgetemp[coms.getCommunityByNode(g.getNode(akt.getSrc())).getIndex()][coms
					.getCommunityByNode(g.getNode(akt.getDst())).getIndex()] += (ew == null) ? 1 : ew
					.getWeight(akt);
		}
		
		int[] mcnew = new int[mastercoms.length];
	
		for (Community akt : coms.getCommunities()) {
			
			n2[akt.getIndex()] = new Node(akt.getIndex(), n);
			
			nw.setWeight(akt.getIndex(), akt.getInternalEdges());
			for (Community akt2 : coms.getCommunities()) {
				if (akt.equals(akt2))
					continue;

				if (edgetemp[akt.getIndex()][akt2.getIndex()] > 0) {
					e = new Edge(akt.getIndex(), akt2.getIndex());
					edges.add(akt.getIndex(), akt2.getIndex());
					ew.setWeight(e,edgetemp[akt.getIndex()][akt2.getIndex()]);
				}
			}
			for (Node k : akt.getNodes()) {
				for (int j = 0; j < mastercoms.length; j++) {
					if (mastercoms[j] == k.getIndex())
						mcnew[j] = akt.getIndex();
				}
			}
			
		}
		
		edges.fill();
		n.setNodes(n2);

		n.addProperty("ew", ew);
		n.addProperty("nw", nw);

		return doWork(n, mcnew);

		// merge/create new graph

	}

	/**
	 * @param aktNode
	 * @param coms
	 * @param g
	 * @param nws
	 * @return
	 */
	private MoveValue getBestMove(Node aktNode, CommunityList coms, Graph g,
			EdgeWeights ew) {
		Community ownc = coms.getCommunityByNode(aktNode);
		Community c;
		double v1;
		int index = aktNode.getIndex();
		MoveValue ret = new MoveValue();
		ret.setOldCom(ownc.getIndex());
		ret.setModDelta(-Double.MAX_VALUE);

		for (Edge akt : aktNode.getEdges()) {
			if (akt.getSrc() == index)
				c = coms.getCommunityByNode(g.getNode(akt.getDst()));
			else
				c = coms.getCommunityByNode(g.getNode(akt.getSrc()));
			if(c.getIndex() == ownc.getIndex())
				continue;
			
			v1 = calcDeltaAddc(aktNode, c, coms, g, ew);
			if(!(ownc.getNodes().length == 1))
				v1 -= calcDeltaRemove(aktNode, c, coms, g, ew);
			
			

			if (v1 > ret.getModDelta()) {
				ret.setModDelta(v1);
				ret.setNewCom(c.getIndex());
			}

		}

		return ret;
	}

	/**
	 * @param i
	 * @param ownc
	 * @param coms
	 * @param g
	 * @return
	 */
	private double calcDeltaRemove(Node i, Community c, CommunityList coms,
			Graph g, EdgeWeights ew) {
		double nodeToCom = getSumWeightsNodeToCom(i, c, coms, ew, g);
		double sumIn = c.getInternalEdges() - nodeToCom;
		double sumNode = getSumWeight(i, ew);
		double sumOut = c.getExternalEdges() - (sumNode - nodeToCom) + sumIn;
		return (((sumIn + getSumWeightsNodeToCom(i, c, coms, ew, g)) / (2 * edges)) - Math
				.pow(((sumOut + sumNode) / (2 * edges)), 2))
				- ((sumIn / (2 * edges)) - Math.pow(sumOut / (2 * edges), 2) - Math
						.pow(sumNode / ( 2 * edges), 2));

	}

	/**
	 * @param i
	 * @param c
	 * @param coms
	 * @param g
	 * @param nws
	 * @return
	 */
	private double calcDeltaAddc(Node i, Community c, CommunityList coms,
			Graph g, EdgeWeights ew) {
		double sumIn = c.getInternalEdges();
		double sumOut = c.getExternalEdges() + sumIn;
		double sumNode = getSumWeight(i, ew);
		
		return (((sumIn + getSumWeightsNodeToCom(i, c, coms, ew, g)) / (2 * edges)) - Math
				.pow(((sumOut + sumNode) / (2 * edges)), 2))
				- ((sumIn / (2 * edges)) - Math.pow(sumOut / (2 * edges), 2) - Math
						.pow(sumNode / ( 2 * edges), 2));

	}

	/**
	 * @param i
	 * @param c
	 * @return
	 */
	private double getSumWeightsNodeToCom(Node i, Community c,
			CommunityList coms, EdgeWeights ew, Graph g) {
		int temp;
		double ret = 0;
		for (Edge akt : i.getEdges()) {
			if (akt.getSrc() == i.getIndex())
				temp = coms.getCommunityByNode(g.getNode(akt.getDst())).getIndex();
			else
				temp = coms.getCommunityByNode(g.getNode(akt.getSrc())).getIndex();

			if (temp == c.getIndex())
				ret += (ew == null) ? 1 : ew.getWeight(akt);
		}
		return ret;
	}

	/**
	 * @param i
	 * @return
	 */
	private double getSumWeight(Node i, EdgeWeights ews) {

		if (ews == null)
			return i.getEdges().length;

		double ret = 0;
		for (Edge akt : i.getEdges())
			ret += ews.getWeight(akt);

		return ret;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

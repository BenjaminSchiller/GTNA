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
import gtna.graph.EdgeWeights;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.NodeWeights;
import gtna.transformation.Transformation;
import gtna.transformation.communities.fastunfolding.FastUnfoldingHelperCommunity;
import gtna.transformation.communities.fastunfolding.FastUnfoldingHelperCommunityList;
import gtna.transformation.communities.fastunfolding.MoveValue;

/**
 * Encapsulates the community detection algorithm called "Fast Unfolding" by the
 * original authors in their paper "Fast unfolding of communities in large
 * networks" (Vincent D. Blondel, Jean-Loup Guillaume, Renaud Lambiotte, Etienne
 * Lefebvre, "Fast unfolding of communities in large networks", March 2008). The
 * algorithm roughly works as follows:
 * 
 * <ul>
 * <li>Starting with every node in their own community, check if moving a node
 * to a community of one of the neighbours will improve modularity, keep moving
 * nodes until no improvement is possible anymore.</li>
 * <li>Create a new graph with the communities as the nodes and the edges as the
 * combination of all the edges between the nodes of the communities.</li>
 * <li>Repeat these two steps until nothing changes anymore.</li>
 * </ul>
 * 
 * As the number of nodes decreases drastically with every iteration, the
 * algorithm is rather fast. The authors claim that they analyzed a graph with
 * 118 million nodes in 152 minutes. This implementation is only minimally
 * optimized, so it will probably perform a little worse.
 * 
 * @author Philipp Neubrand
 * 
 */
public class CDFastUnfolding extends Transformation {

	/**
	 * Standard constructor, no arguments are passed as there are no parameters
	 * for the algorithm.
	 */
	public CDFastUnfolding() {
		super("CD_FAST_UNFOLDING");
	}

	private double edgeCount;

	@Override
	public Graph transform(Graph g) {
		if (!applicable(g))
			return g;

		edgeCount = g.getEdges().getEdges().size();
		int[] mastercoms = new int[g.getNodes().length];
		for (int i = 0; i < mastercoms.length; i++)
			mastercoms[i] = i;

		mastercoms = doWork(g, mastercoms, null, null);

		HashMap<Integer, Integer> c = new HashMap<Integer, Integer>();

		for (int i = 0; i < mastercoms.length; i++) {

			c.put(i, mastercoms[i]);
		}

		g.addProperty(g.getNextKey("COMMUNITIES"),
				new gtna.communities.CommunityList(c));

		return g;
	}

	/**
	 * Performs both steps of the algorithm and then recurses if necessary.
	 * Returns an array containing a mapping for the node indices to their
	 * community indices.
	 * 
	 * @param g
	 *            The graph for which the steps are to be performed.
	 * @param mastercoms
	 *            The current mapping for the original nodes to their community
	 *            indices.
	 * @param ews
	 *            The edgeweights for the calculation.
	 * @param nws
	 *            The nodeweights for the calculation.
	 * @return An array containing the mapping for the node indices to their
	 *         community indices.
	 */
	private int[] doWork(Graph g, int[] mastercoms, EdgeWeights ews,
			NodeWeights nws) {

		// Initialize the communitylist and the communities
		FastUnfoldingHelperCommunityList coms = new FastUnfoldingHelperCommunityList();
		FastUnfoldingHelperCommunity temp;
		Node[] nodes = g.getNodes();
		for (Node akt : nodes) {
			temp = new FastUnfoldingHelperCommunity(akt.getIndex(), g, coms,
					nws, ews);
			temp.setNode(akt);
			coms.add(temp);

		}

		boolean finished = false;
		// helper flag if something changed, if still false after the
		// calculations will return the unchanged community mappings
		boolean changed = false;

		MoveValue mv;
		// move nodes around till no improvement is possible
		while (!finished) {

			finished = true;
			for (Node akt : g.getNodes()) {
				// get best possible move for the current node
				mv = getBestMove(akt, coms, g, ews);
				if (mv.getModDelta() > 0) {
					// if best possible move is an improvement then do it

					temp = coms.getCommunityByNode(akt);
					temp.removeNode(akt);
					// if the old community is empty, remove it
					if (temp.getNodes().length == 0)
						coms.removeCom(temp);

					// add the node to the new community
					coms.getCommunityByID(mv.getNewCom()).addNode(akt);

					// something changed and we are not finished
					changed = true;
					finished = false;
				}
			}
		}

		// if nothing changed at all we are finished and return the unchanged
		// community mappings
		if (!changed)
			return mastercoms;

		// something changed and so we have to recurse, preparing the needed
		// objects
		Graph n = new Graph("temp");
		int distinct = coms.getCommunities().size();
		Node[] n2 = new Node[distinct];
		NodeWeights newNW = new NodeWeights(distinct);
		EdgeWeights newEW = new EdgeWeights(distinct);
		Edges newEdges = new Edges(n2, distinct);
		Edge tempEdge;

		// normalize the community IDs so they are between 1 and n
		coms.normalizeIDs();

		// calculate the summed edgeweights between the communities; this could
		// be done "live" while moving nodes around, but it seems to be a lot of
		// unneeded calculations to update the edgecounts while moving nodes
		// around, therefore they are calculated once after the communities are
		// fixed
		int[][] edgetemp = new int[distinct][distinct];
		for (Edge akt : g.getEdges().getEdges()) {
			edgetemp[coms.getCommunityByNode(g.getNode(akt.getSrc()))
					.getIndex()][coms.getCommunityByNode(
					g.getNode(akt.getDst())).getIndex()] += (newEW == null) ? 1
					: newEW.getWeight(akt);
		}

		int[] mcnew = new int[mastercoms.length];

		// combine the nodes to communities and update the master community
		// mapping
		for (FastUnfoldingHelperCommunity akt : coms.getCommunities()) {

			n2[akt.getIndex()] = new Node(akt.getIndex(), n);

			newNW.setWeight(akt.getIndex(), akt.getInternalEdges());
			for (FastUnfoldingHelperCommunity akt2 : coms.getCommunities()) {
				if (akt.equals(akt2))
					continue;

				if (edgetemp[akt.getIndex()][akt2.getIndex()] > 0) {
					tempEdge = new Edge(akt.getIndex(), akt2.getIndex());
					newEdges.add(akt.getIndex(), akt2.getIndex());
					newEW.setWeight(tempEdge,
							edgetemp[akt.getIndex()][akt2.getIndex()]);
				}
			}
			for (Node k : akt.getNodes()) {
				for (int j = 0; j < mastercoms.length; j++) {
					if (mastercoms[j] == k.getIndex())
						mcnew[j] = akt.getIndex();
				}
			}

		}

		newEdges.fill();
		n.setNodes(n2);

		return doWork(n, mcnew, newEW, newNW);

		// merge/create new graph

	}

	private MoveValue getBestMove(Node aktNode,
			FastUnfoldingHelperCommunityList coms, Graph g, EdgeWeights ew) {
		FastUnfoldingHelperCommunity ownc = coms.getCommunityByNode(aktNode);
		FastUnfoldingHelperCommunity c;
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
			if (c.getIndex() == ownc.getIndex())
				continue;

			v1 = calcDeltaAddc(aktNode, c, coms, g, ew);
			if (!(ownc.getNodes().length == 1))
				v1 -= calcDeltaRemove(aktNode, c, coms, g, ew);

			if (v1 > ret.getModDelta()) {
				ret.setModDelta(v1);
				ret.setNewCom(c.getIndex());
			}

		}

		return ret;
	}

	private double calcDeltaRemove(Node i, FastUnfoldingHelperCommunity c,
			FastUnfoldingHelperCommunityList coms, Graph g, EdgeWeights ew) {
		double nodeToCom = getSumWeightsNodeToCom(i, c, coms, ew, g);
		double sumIn = c.getInternalEdges() - nodeToCom;
		double sumNode = getSumWeight(i, ew);
		double sumOut = c.getExternalEdges() - (sumNode - nodeToCom) + sumIn;

		return calcDelta(sumIn, sumNode, sumOut,
				getSumWeightsNodeToCom(i, c, coms, ew, g));

	}

	private double calcDeltaAddc(Node i, FastUnfoldingHelperCommunity c,
			FastUnfoldingHelperCommunityList coms, Graph g, EdgeWeights ew) {
		double sumIn = c.getInternalEdges();
		double sumOut = c.getExternalEdges() + sumIn;
		double sumNode = getSumWeight(i, ew);

		return calcDelta(sumIn, sumNode, sumOut,
				getSumWeightsNodeToCom(i, c, coms, ew, g));
	}

	public double calcDelta(double sumIn, double sumNode, double sumOut,
			double sumNodeToCom) {
		return (((sumIn + sumNodeToCom) / (2 * edgeCount)) - Math.pow(
				((sumOut + sumNode) / (2 * edgeCount)), 2))
				- ((sumIn / (2 * edgeCount))
						- Math.pow(sumOut / (2 * edgeCount), 2) - Math.pow(
						sumNode / (2 * edgeCount), 2));
	}

	private double getSumWeightsNodeToCom(Node i,
			FastUnfoldingHelperCommunity c,
			FastUnfoldingHelperCommunityList coms, EdgeWeights ew, Graph g) {
		int temp;
		double ret = 0;
		for (Edge akt : i.getEdges()) {
			if (akt.getSrc() == i.getIndex())
				temp = coms.getCommunityByNode(g.getNode(akt.getDst()))
						.getIndex();
			else
				temp = coms.getCommunityByNode(g.getNode(akt.getSrc()))
						.getIndex();

			if (temp == c.getIndex())
				ret += (ew == null) ? 1 : ew.getWeight(akt);
		}
		return ret;
	}

	private double getSumWeight(Node i, EdgeWeights ews) {

		if (ews == null)
			return i.getEdges().length;

		double ret = 0;
		for (Edge akt : i.getEdges())
			ret += ews.getWeight(akt);

		return ret;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

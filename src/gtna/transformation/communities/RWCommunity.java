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
 * Community.java
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
import gtna.communities.CBufferingCommunity;
import gtna.graph.Graph;
import gtna.graph.Node;

/**
 * An <code>RWCommunity</code> is a <code>CBufferingCommunity</code> that
 * contains additional data fields for the nodeRanks and the communityRank as
 * well as the logic to calculate those ranks as defined by the
 * <code>CDRandomWalk</code> community detection algorithm. More information can
 * be found in there.
 * 
 * @author Philipp Neubrand
 * 
 */
public class RWCommunity extends CBufferingCommunity {
	private HashMap<Integer, Double> nodeRanks = new HashMap<Integer, Double>();
	private double communityRank;
	private Graph g;
	private boolean recalculateRanks = true;
	private boolean useNewFormula;;

	/**
	 * Standard Constructor for a <code>CBufferingCommunity</code>, supplied are
	 * the ID of the community, the graph for which this community is used and
	 * whether or not the alternative formula to calculate the node rankings is
	 * to be used.
	 * 
	 * @param index
	 *            The ID of this community.
	 * @param g
	 *            The <code>Graph</code> of this community.
	 * @param useAlternativeFormula
	 *            True if the alternative formula should be used, false
	 *            otherwise.
	 */
	public RWCommunity(int index, Graph g, boolean useAlternativeFormula) {
		super(index);
		this.g = g;
		this.useNewFormula = useAlternativeFormula;
	}

	/**
	 * Sorts the nodes according to their node rank.
	 */
	public void sortNodes() {
		Integer temp;
		for (int i = nodes.size(); i > 1; i--) {
			for (int j = 0; j < i - 1; j++) {
				if (getNodeRank(nodes.get(j)) < getNodeRank(nodes.get(j + 1))) {
					temp = nodes.get(j);
					nodes.set(j, nodes.get(j + 1));
					nodes.set(j + 1, temp);
				}
			}
		}
	}

	public void addNode(int node) {
		super.addNode(node);
		recalculateRanks = true;
	}

	public void removeNode(int node) {
		super.removeNode(node);
		recalculateRanks = true;
	}

	/**
	 * Getter for the rank of this community, which is the average node rank.
	 * The rank is buffered and only recalculated if the community has changed.
	 * 
	 * @return The rank of this community.
	 */
	public double getCommunityRank() {
		if (!recalculateRanks)
			return communityRank;

		double t = 0;
		for (int akt : nodes) {
			t += getNodeRank(akt);
		}

		communityRank = t / ((double) nodes.size());

		recalculateRanks = false;

		return communityRank;
	}

	/**
	 * Getter for the rank of the supplied node. Node ranks are buffered and
	 * will only be recalculated if something has changed since the last
	 * calculation. The formula used to calculate the node rank depends on the
	 * constructor parameter <code>useAlternativeFormula</code>. If false, node
	 * rank is calculated as "degree / in²", if true as "degree - in" (where
	 * "in" is the number of neighbors in the same community).
	 * 
	 * @param node
	 *            The node for which the rank should be calculated.
	 * @return The rank of the supplied node.
	 */
	public double getNodeRank(int node) {
		if (!recalculateRanks)
			return nodeRanks.get(node);

		double nr = 0;

		double in = 0;
		Node aktNode = g.getNode(node);
		for (int akt : aktNode.getOutgoingEdges()) {
			if (contains(akt))
				in++;
		}

		for (int akt : aktNode.getIncomingEdges()) {
			if (contains(akt))
				in++;
		}
		if (useNewFormula) {
			nr = aktNode.getDegree() - in;
		} else {
			if (in == 0)
				nr = aktNode.getDegree();
			else
				nr = (aktNode.getDegree()) / (in * in);
		}

		nodeRanks.put(node, nr);

		return nr;
	}

	/**
	 * Overwritten equals method, two RWCommunities are assumed to be equal if
	 * and only if they contain the same nodes.
	 */
	public boolean equals(Object c) {
		if (!(c instanceof RWCommunity))
			return false;

		RWCommunity t = (RWCommunity) c;
		boolean eq = true;
		for (int akt : nodes) {
			if (!t.contains(akt))
				eq = false;
		}

		return eq;
	}

}

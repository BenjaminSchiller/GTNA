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
package gtna.communities;

import java.util.ArrayList;

import gtna.graph.Edge;
import gtna.graph.EdgeWeights;
import gtna.graph.Graph;
import gtna.graph.NodeWeights;

/**
 * Helper class for the FastUnfolding community detection algorithm, is pretty
 * similar to gtna.communities.Community but provides additional functionality
 * by maintaining the number of internal and external edges at every point in
 * the process of creating the community. This means additional calculations in
 * the removeNode and addNode methods.
 * 
 * @author Philipp Neubrand
 * 
 */
public class EdgeCountingCommunity extends ChangeableCommunity {
	private Graph g;
	private double internalEdges = 0;
	private ChangeableCommunityList<EdgeCountingCommunity> coms;
	private double externalEdges = 0;
	private NodeWeights nw;
	private EdgeWeights ew;

	public EdgeCountingCommunity(int id, Graph g,
			FastUnfoldingHelperCommunityList<EdgeCountingCommunity> coms, NodeWeights nw,
			EdgeWeights ew) {
		super(id);
		this.nw = nw;
		this.ew = ew;
		this.g = g;
		this.coms = coms;
		nodes = new ArrayList<Integer>();
	}
	
	public double getInternalEdges() {
		return internalEdges;
	}

	public void removeNode(int node) {
		super.removeNode(node);
		int t;
		if (nw != null)
			internalEdges -= nw.getWeight(node);

		for (Edge akt : g.getNode(node).getEdges()) {
			if (akt.getSrc() == node)
				t = akt.getDst();
			else
				t = akt.getSrc();

			if (coms.getCommunityOfNode(t).getIndex() == this
					.getIndex()) {
				internalEdges -= (ew != null) ? ew.getWeight(akt) : 1;
			} else
				externalEdges -= (ew != null) ? ew.getWeight(akt) : 1;
		}
	}

	public void addNode(int node) {
		int t;
		super.addNode(node);
		if (nw != null)
			internalEdges += nw.getWeight(node);

		coms.setCommunity(node, this);

		for (Edge akt : g.getNode(node).getEdges()) {
			if (akt.getSrc() == node)
				t = akt.getDst();
			else
				t = akt.getSrc();

			if (coms.getCommunityOfNode(t).getIndex() == this
					.getIndex()) {
				internalEdges += (ew != null) ? ew.getWeight(akt) : 1;
			} else
				externalEdges += (ew != null) ? ew.getWeight(akt) : 1;
		}

	}

	public double getExternalEdges() {
		return externalEdges;
	}
}

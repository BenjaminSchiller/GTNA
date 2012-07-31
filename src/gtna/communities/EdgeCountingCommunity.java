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
 * An <code>EdgeCountingCommunity</code> is a <code>ChangeableCommunity</code>
 * that keeps track of the internal and external edges of each
 * <code>Community</code>.
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

	/**
	 * 
	 * @param id
	 * @param g
	 * @param coms
	 * @param nw
	 * @param ew
	 */
	public EdgeCountingCommunity(int id, Graph g,
			ChangeableCommunityList<EdgeCountingCommunity> coms,
			NodeWeights nw, EdgeWeights ew) {
		super(id);
		this.nw = nw;
		this.ew = ew;
		this.g = g;
		this.coms = coms;
		nodes = new ArrayList<Integer>();
	}

	/**
	 * Getter for the number of internal edges.
	 * 
	 * @return The number of internal edges of this Community.
	 */
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

			if (coms.getCommunityOfNode(t).getIndex() == this.getIndex()) {
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
			
			if (coms.getCommunityOfNode(t) != null && coms.getCommunityOfNode(t).getIndex() == this.getIndex()) {
				internalEdges += (ew != null) ? ew.getWeight(akt) : 1;
			} else
				externalEdges += (ew != null) ? ew.getWeight(akt) : 1;
		}

	}

	/**
	 * Getter for the number of external edges.
	 * 
	 * @return The number of external edges of this Community.
	 */
	public double getExternalEdges() {
		return externalEdges;
	}
}

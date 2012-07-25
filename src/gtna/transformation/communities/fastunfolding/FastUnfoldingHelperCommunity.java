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
package gtna.transformation.communities.fastunfolding;

import java.util.ArrayList;

import gtna.communities.Community;
import gtna.graph.Edge;
import gtna.graph.EdgeWeights;
import gtna.graph.Graph;
import gtna.graph.Node;
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
public class FastUnfoldingHelperCommunity extends Community {
	private Graph g;
	private double internalEdges = 0;
	private ArrayList<Integer> nodes;
	private FastUnfoldingHelperCommunityList coms;
	private double externalEdges = 0;
	private NodeWeights nw;
	private EdgeWeights ew;

	public FastUnfoldingHelperCommunity(int id, Graph g,
			FastUnfoldingHelperCommunityList coms, NodeWeights nw,
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

	public int[] getNodes() {
		int[] ret = new int[nodes.size()];
		for(int i = 0; i < ret.length; i++)
			ret[i] = nodes.get(i);
		
		return ret;
	}

	public void removeNode(Node akt2) {
		int t;
		if (nw != null)
			internalEdges -= nw.getWeight(akt2.getIndex());

		for (Edge akt : akt2.getEdges()) {
			if (akt.getSrc() == akt2.getIndex())
				t = akt.getDst();
			else
				t = akt.getSrc();

			if (coms.getCommunityOfNode(t).getIndex() == this
					.getIndex()) {
				internalEdges -= (ew != null) ? ew.getWeight(akt) : 1;
			} else
				externalEdges -= (ew != null) ? ew.getWeight(akt) : 1;
		}

		nodes.remove(akt2.getIndex());

	}

	public void addNode(int node) {
		int t;
		if (nw != null)
			internalEdges += nw.getWeight(node);

		nodes.add(node);
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

	public void setID(int i) {
		index = i;
	}
}

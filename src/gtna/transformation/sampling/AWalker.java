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
 * AWalker.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.sampling;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public abstract class AWalker extends Parameter {

	private AWalkerController controller;
	private Collection<Node> currents;

	public AWalker(String walker) {
		super("WALKER", walker);
		currents = new ArrayList<Node>();
	}
	
	public void setWalkerController(AWalkerController awc) {
	    controller = awc;
	}

	/**
	 * Returns the current neighbors of current nodes of the walker
	 * 
	 * @return Map: key: current node value: neighbors of the current node
	 */
	private Map<Node, Collection<Node>> getCurrentCandidates(Graph g) {
		Map<Node, Collection<Node>> cn = new HashMap<Node, Collection<Node>>();

		for (Node n : currents) {
			Collection<Node> nn = resolveCandidates(g, n);
			cn.put(n, nn);
		}
		return cn;
	}

	/**
	 * This default implementation returns the list of neighbors as candidates
	 * 
	 * @param g
	 *            Graph
	 * @param n
	 *            Current node
	 * @return List of candidates
	 */
	private Collection<Node> resolveCandidates(Graph g, Node n) {
		int[] nids = n.getOutgoingEdges();
		ArrayList<Node> nn = new ArrayList<Node>();
		for (int i : nids) {
			nn.add(g.getNode(i));
		}
		return nn;
	}

	/**
	 * @param candidates
	 * @return
	 */
	protected abstract Node selectNextNode(Collection<Node> candidates);

	/**
	 * Move walker by one step
	 */
	public void takeAStep(Graph g, NetworkSample ns) {
		Map<Node, Collection<Node>> cc = this.getCurrentCandidates(g);
		Set<Node> c = cc.keySet();
		for (Node n : c) {
			Collection<Node> candidates = controller
					.filterCandidates(cc.get(n), ns);
			Node next = this.selectNextNode(candidates);
			currents.remove(n);
			currents.add(next);
		}
	}

	/**
	 * @param node
	 */
	public void setStartNode(Node node) {
	    if(currents.size() == 0)
		currents.add(node);	    
	}

}
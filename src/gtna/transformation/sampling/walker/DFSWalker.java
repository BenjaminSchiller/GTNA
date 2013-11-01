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
 * RandomWalkWalker.java
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
package gtna.transformation.sampling.walker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.transformation.sampling.sample.NetworkSample;
=======
import gtna.transformation.sampling.NetworkSample;
>>>>>>> added DFSWalker
=======
import gtna.transformation.sampling.sample.NetworkSample;
>>>>>>> refactoring to allow multiple types of sample

/**
 * @author Tim
 * 
 * This class provides the base functionality of a bfs/dfs walker using a deque datastructure
 * 
 */
public class DFSWalker extends AWalker {

	Deque<Node> nextQ;
	private int restartcounter = 0;

	/**
	 * @param walker
	 */
	public DFSWalker() {
		super("DFS_WALKER");
		nextQ = new LinkedList<Node>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.sampling.AWalker#selectNextNode(java.util.Collection)
	 */
	@Override
	protected Node selectNextNode(Collection<Node> candidates) {
		Node n = null;
		List<Node> c = new ArrayList<Node>();
		Collection<Node> cc = new ArrayList<Node>();
		while (n == null) {
			if (nextQ.size() > 0) {
				c.add(nextQ.removeFirst());
				cc = this.filterCandidates(c);
				if (cc.size() > 0) {
					n = cc.toArray(new Node[0])[0];
				}
			} else {

<<<<<<< HEAD
//				System.err.println("NextQ empty, need a restart! ("
//						+ restartcounter + ")");
=======
				System.err.println("NextQ empty, need a restart! ("
						+ restartcounter + ")");
>>>>>>> added DFSWalker
				restartcounter += 1;
				cc = super.getRestartNodes();
				n = cc.toArray(new Node[0])[0];

			}
		}

		return n;
	}

	@Override
	public void takeAStep(Graph g, NetworkSample ns) {
		Map<Node, Collection<Node>> cc = this.getCurrentCandidates();
		Collection<Node> c = new ArrayList<Node>();

		// add new neighbors to the q
		if (cc.size() > 0) {
			c = cc.keySet();
		}

		Collection<Collection<Node>> toQ = cc.values();
		for (Collection<Node> cn : toQ) {
			for(Node cni : cn){
				nextQ.addFirst(cni);
			}
		}

		Node next = this.selectNextNode(new ArrayList<Node>());

		super.currents.remove(cc.keySet().toArray(new Node[0])[0]);
		super.currents.add(next);

	}

	

	/**
	 * returns the list of neighbors as candidates
	 * 
	 * @param g
	 *            Graph
	 * @param n
	 *            Current node
	 * @return List of candidates
	 */
	@Override
	public Collection<Node> resolveCandidates(Graph g, Node n) {
		int[] nids = n.getOutgoingEdges();
		ArrayList<Node> nn = new ArrayList<Node>();
		for (int i : nids) {
			nn.add(g.getNode(i));
		}
		return nn;
	}

}

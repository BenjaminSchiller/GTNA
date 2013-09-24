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
>>>>>>> added SnowballWalker
=======
import gtna.transformation.sampling.sample.NetworkSample;
>>>>>>> refactoring to allow multiple types of sample

/**
 * @author Tim
 * 
 */
<<<<<<< HEAD
<<<<<<< HEAD
public class SnowballWalker extends BFSBaseWalker {

	int amountOfAddedNodesPerStep = 1;
	
<<<<<<< HEAD
=======
public class SnowballWalker extends AWalker {
=======
public class SnowballWalker extends BFSBaseWalker {
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small

	int amountOfAddedNodesPerStep = 1;
	List<Node> nextQ;
	private int restartcounter = 0;
>>>>>>> added SnowballWalker
=======
>>>>>>> removed unnecessary fields in child classes

	/**
	 * @param walker
	 */
	public SnowballWalker() {
		super("SNOWBALL_WALKER");
<<<<<<< HEAD
<<<<<<< HEAD
	
=======
		nextQ = new LinkedList<Node>();
>>>>>>> added SnowballWalker
=======
	
>>>>>>> removed unnecessary fields in child classes
	}

	/**
	 * @param i
	 */
	public SnowballWalker(int i) {
		this();
		amountOfAddedNodesPerStep = i;
	}

<<<<<<< HEAD
<<<<<<< HEAD
=======
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
				c.add(nextQ.get(0));
				nextQ.remove(0);
				cc = this.filterCandidates(c);
				if (cc.size() > 0) {
					n = cc.toArray(new Node[0])[0];
				}
			} else {

				System.err.println("NextQ empty, need a restart! ("
						+ restartcounter + ")");
				restartcounter += 1;
				cc = this.getRestartNodes();
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
			nextQ.addAll(chooseNodesToAddToQ(cn));
		}

		Node next = this.selectNextNode(new ArrayList<Node>());

		this.currents.remove(cc.keySet().toArray(new Node[0])[0]);
		this.currents.add(next);

	}
>>>>>>> added SnowballWalker
=======
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small

	/**
	 * @param cn
	 * @return
	 */
<<<<<<< HEAD
<<<<<<< HEAD
	@Override
	protected Collection<Node> chooseNodesToAddToQ(Collection<Node> cn) {
		Collection<Node> q = new ArrayList<Node>();
		ArrayList<Node> temp = new ArrayList<Node>();
		Collection<Node> temp1 = new ArrayList<Node>();

		temp.addAll(cn);
		temp1 = this.filterCandidates(temp);
		if (temp1.size() <= amountOfAddedNodesPerStep) {
			q.addAll(temp1);

		} else {
			temp.clear();
			temp.addAll(temp1);
			
<<<<<<< HEAD
			int m = Math.min(amountOfAddedNodesPerStep, temp.size());
			for (int i = 0; i < m; i++) {
				q.add(temp.get(i));
			}
		}
		return q;
	}

=======
	private Collection<Node> chooseNodesToAddToQ(Collection<Node> cn) {
=======
	@Override
	protected Collection<Node> chooseNodesToAddToQ(Collection<Node> cn) {
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small
		Collection<Node> q = new ArrayList<Node>();
		ArrayList<Node> temp = new ArrayList<Node>();
		Collection<Node> temp1 = new ArrayList<Node>();

		temp.addAll(cn);
		temp1 = this.filterCandidates(temp);
		if (temp1.size() <= amountOfAddedNodesPerStep) {
			q.addAll(temp1);

		} else {
			temp.clear();
			temp.addAll(temp1);
			Random r = this.getRNG();

=======
>>>>>>> removed unnecessary fields in child classes
			int m = Math.min(amountOfAddedNodesPerStep, temp.size());
			for (int i = 0; i < m; i++) {
				q.add(temp.get(i));
			}
		}
		return q;
	}

<<<<<<< HEAD
	/**
	 * @param node
	 * @return
	 */
	private boolean alreadyContained(Node node) {
		List<Node> nf = new ArrayList<Node>();
		nf.add(node);
		Collection<Node> f = this.filterCandidates(nf);

		if (f.size() == 0)
			return true;

		return false;
	}

<<<<<<< HEAD
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

>>>>>>> added SnowballWalker
=======
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small
=======
>>>>>>> removed unnecessary fields in child classes
}

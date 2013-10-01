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
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;

/**
 * @author Tim
<<<<<<< HEAD
<<<<<<< HEAD
 * 
=======
 *
>>>>>>> Metropolized Random Walk Walker + Entry for SamplingAlgorithmFactory
=======
 * 
>>>>>>> trying to fix the walking problem of the metropolized random walk
 */
public class MetropolizedRandomWalkWalker extends AWalker {

	/**
	 * @param walker
	 */
	public MetropolizedRandomWalkWalker() {
		super("METROPLIZED_RANDOM_WALK_WALKER");
	}

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> trying to fix the walking problem of the metropolized random walk
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.sampling.AWalker#selectNextNode(java.util.Collection)
<<<<<<< HEAD
	 */
	@Override
	protected Node selectNextNode(Collection<Node> candidates) {
		Random r = this.getRNG();
		Collection<Node> c = this.getCurrentNodes();
		if (c.size() > 0) {
			Node current = c.toArray(new Node[0])[0];

			int next = r.nextInt(candidates.size());
			next = next % candidates.size();

			Node nextStepCandidate = candidates.toArray(new Node[0])[next];

			int nscDegree = nextStepCandidate.getDegree();
			int cDegree = current.getDegree();

			double d = (double) cDegree / (double) nscDegree;

			d = Math.min(d, 1);

			double p = r.nextDouble();

			if (p < d) {
				return nextStepCandidate; // move the walker to the next node
			} else {
//				System.err.println("Stay, no moving: deg(old)/deg(candidate) - " + cDegree + "/" + nscDegree);
				return current; // stay and don't move the walker!
			}
		} else {
			c = this.getRestartNodes();

			return c.toArray(new Node[0])[0];
		}

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
=======
	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.AWalker#selectNextNode(java.util.Collection)
=======
>>>>>>> trying to fix the walking problem of the metropolized random walk
	 */
	@Override
	protected Node selectNextNode(Collection<Node> candidates) {
		Random r = this.getRNG();
		Collection<Node> c = this.getCurrentNodes();
		if (c.size() > 0) {
			Node current = c.toArray(new Node[0])[0];

			int next = r.nextInt(candidates.size());
			next = next % candidates.size();

			Node nextStepCandidate = candidates.toArray(new Node[0])[next];

			int nscDegree = nextStepCandidate.getDegree();
			int cDegree = current.getDegree();

			double d = (double) cDegree / (double) nscDegree;

			d = Math.min(d, 1);

			double p = r.nextDouble();

			if (p < d) {
				return nextStepCandidate; // move the walker to the next node
			} else {
//				System.err.println("Stay, no moving: deg(old)/deg(candidate) - " + cDegree + "/" + nscDegree);
				return current; // stay and don't move the walker!
			}
		} else {
			c = this.getRestartNodes();

			return c.toArray(new Node[0])[0];
		}

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
<<<<<<< HEAD
    public Collection<Node> resolveCandidates(Graph g, Node n) {
    	int[] nids = n.getOutgoingEdges();
    	ArrayList<Node> nn = new ArrayList<Node>();
    	for (int i : nids) {
    		nn.add(g.getNode(i));
    	}
    	return nn;
    }
>>>>>>> Metropolized Random Walk Walker + Entry for SamplingAlgorithmFactory
=======
	public Collection<Node> resolveCandidates(Graph g, Node n) {
		int[] nids = n.getOutgoingEdges();
		ArrayList<Node> nn = new ArrayList<Node>();
		for (int i : nids) {
			nn.add(g.getNode(i));
		}
		return nn;
	}
>>>>>>> trying to fix the walking problem of the metropolized random walk

}

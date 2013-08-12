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
<<<<<<< HEAD
import java.util.Map;
=======
>>>>>>> started BFS implementation. known bug:  java.lang.OutOfMemoryError: Java heap space @ resolveCandidates
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;
<<<<<<< HEAD
import gtna.transformation.sampling.sample.NetworkSample;
=======
>>>>>>> started BFS implementation. known bug:  java.lang.OutOfMemoryError: Java heap space @ resolveCandidates

/**
 * @author Tim
 * 
 */
<<<<<<< HEAD
public class BFSWalker extends BFSBaseWalker {

    
=======
public class BFSWalker extends AWalker {

    List<Node> nextQ;

>>>>>>> started BFS implementation. known bug:  java.lang.OutOfMemoryError: Java heap space @ resolveCandidates
    /**
     * @param walker
     */
    public BFSWalker() {
<<<<<<< HEAD
	super("BFS_WALKER");
    }

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.walker.BFSBaseWalker#chooseNodesToAddToQ(java.util.Collection)
	 */
	@Override
	protected Collection<Node> chooseNodesToAddToQ(Collection<Node> toFilter) {
		return toFilter; // all nodes are added.
	}

    
=======
	super("RANDOM_WALK_WALKER");
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
	Iterator<Node> ci = candidates.iterator();
	
	if(ci.hasNext())
	    return ci.next();

	return null;
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
	nextQ.addAll(nn);
	
	return nextQ;
    }

>>>>>>> started BFS implementation. known bug:  java.lang.OutOfMemoryError: Java heap space @ resolveCandidates
}

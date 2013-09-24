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
>>>>>>> added ForestFireWalker
=======
import gtna.transformation.sampling.sample.NetworkSample;
>>>>>>> refactoring to allow multiple types of sample

/**
 * @author Tim
 * 
 */
<<<<<<< HEAD
<<<<<<< HEAD
public class FFWalker extends BFSBaseWalker {

	
    private double probability = 0.7;
=======
public class FFWalker extends AWalker {
=======
public class FFWalker extends BFSBaseWalker {
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small

	
    private double probability = 1.0;
>>>>>>> added ForestFireWalker

    /**
     * @param walker
     */
    public FFWalker() {
	super("FF_WALKER");
	nextQ = new LinkedList<Node>();
    }
    
    /**
     * Returns an instance of a ForestFire Walker, adding nodes with the given probability to the Q
     * @param probability	probability of adding the neighbor-nodes to the Q, if 1.0 this is a BFSWalker	
     */
    public FFWalker(double probability){
    	this();
    	this.probability = probability;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small
	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.walker.BFSBaseWalker#chooseNodesToAddToQ(java.util.Collection)
	 */
	@Override
	protected Collection<Node> chooseNodesToAddToQ(Collection<Node> toFilter) {
		Random r = this.getRNG();
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
		
		System.err.println("NextQ empty, need a restart! (" + restartcounter  
			+ ")");
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
	    for(Collection<Node> cn : toQ) {
		nextQ.addAll(filterByProbability(cn));
	    }
	    
	    Node next = this.selectNextNode(new ArrayList<Node>());
	    
	    
	    super.currents.remove(cc.keySet().toArray(new Node[0])[0]);
	    super.currents.add(next);

	

    }
    
    private Collection<Node> filterByProbability(Collection<Node> toFilter){
    	Random r = this.getRNG();
>>>>>>> added ForestFireWalker
=======
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small
    	Collection<Node> filtered = new ArrayList<Node>();
    	double pn = 0.0;
    	for(Node n : toFilter){
    		pn = r.nextDouble();
    		if(pn <= probability){
    			filtered.add(n);
    		}
    	}
    	
    	return filtered;
<<<<<<< HEAD
<<<<<<< HEAD
	}

=======
    	
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
>>>>>>> added ForestFireWalker
=======
	}

>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small

}

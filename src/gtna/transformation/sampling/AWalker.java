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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public abstract class AWalker extends Parameter {

    private AWalkerController controller;
    private Collection<Node> currents;
  
    /**
     * create a walker instance
     * 
     * @param walker
     */
    public AWalker(String walker) {
	super("WALKER", walker);
	currents = new ArrayList<Node>();
    }

    /**
     * set the appendant walker controller
     * 
     * @param awc
     */
    public void setWalkerController(AWalkerController awc) {
	controller = awc;
    }

    /**
     * Returns the current neighbors of current nodes of the walker
     * 
     * @return Map: key: current node value: neighbors of the current node
     */
    public Map<Node, Collection<Node>> getCurrentCandidates() {
	Map<Node, Collection<Node>> cn = new HashMap<Node, Collection<Node>>();

	for (Node n : currents) {
	    Collection<Node> nn = resolveCandidates(this.getGraph(), n);
	    cn.put(n, nn);
	}
	return cn;
    }

   public abstract Collection<Node> resolveCandidates(Graph g, Node n);

    /**
     * @param candidates
     * @return
     */
    protected abstract Node selectNextNode(Collection<Node> candidates, Node current);

    /**
     * Move walker one step
     */
    public void takeAStep(Graph g, NetworkSample ns) {
	Map<Node, Collection<Node>> cc = this.getCurrentCandidates();
	Collection<Node> c;

	// TODO
	if (cc.size() > 0) {
	    c = cc.keySet();
	} else {
		System.err.println("Candidate Set is empty! catching restart nodes");
	    c = getRestartNodes();
	}
	for (Node n : c) {
	    Collection<Node> candidates = new ArrayList<Node>();
	    do {
		// TODO
		if (cc.size() > 0) {
		    candidates = controller.filterCandidates(cc.get(n));
		} else {
		    candidates = controller.filterCandidates(c);
		}
		if (candidates.size() == 0) {
			System.err.println("Candidate Set is empty! catching restart nodes");
		    cc.clear();
		    c = getRestartNodes();
		}

	    } while (candidates.size() == 0);

	    Node next = this.selectNextNode(candidates, n);

	    currents.remove(n);
	    currents.add(next);
	}
    }

    /**
     * Get new nodes for restarting the walk if the walker blocks
     * 
     * @return collection of new start nodes
     */
    private Collection<Node> getRestartNodes() {
	return this.controller.getRestartNodes();
    }

    /**
     * Set start node of this walker
     * 
     * @param node
     */
    public void setStartNode(Node node) {
	if (currents.size() == 0)
	    currents.add(node);
    }
    
    /**
     * returns the graph hold by the sampling controller
     * @return
     */
    public Graph getGraph() {
	return controller.getGraph();
    }
    
    public Collection<Node> getCurrentNodes(){
    	return currents;
    }

    /**
     * @param nodesToFilter
     * @return
     */
    public Collection<Node> filterCandidates(List<Node> nodesToFilter) {
	return controller.filterCandidates(nodesToFilter);
    }

    /**
     * @return
     */
    public Random getRNG() {
	return controller.getRNG();
    }

}
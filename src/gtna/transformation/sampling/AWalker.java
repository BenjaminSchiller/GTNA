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
import gtna.transformation.sampling.sample.INetworkSample;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Tim
 * 
 */
public abstract class AWalker extends Parameter {

    private AWalkerController controller;
    protected Collection<Node> currents;
  
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
     * @return Map: 	<br><b>key</b>: current node 
     * 			<br><b>value</b>: neighbors of the current node
     */
    public Map<Node, List<Node>> getCurrentCandidates() {
	Map<Node, List<Node>> cn = new HashMap<Node, List<Node>>();

	for (Node n : currents) {
	    List<Node> nn = resolveCandidates(this.getGraph(), n);
	    cn.put(n, nn);
	}
	return cn;
    }

   public abstract List<Node> resolveCandidates(Graph g, Node n);

    /**
     * @param candidates
     * @return
     */
    protected abstract Node selectNextNode(Collection<Node> candidates);

    /**
     * Move walker one step
     */
    public void takeAStep(Graph g, INetworkSample ns) {
	Map<Node, List<Node>> cc = this.getCurrentCandidates();
	
	List<Node> c = new ArrayList<Node>();

	// TODO
	if (cc.size() > 0) {
	    c.addAll(cc.keySet());
	} else {
//		System.err.println("Candidate Set is empty! catching restart nodes");
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
//			System.err.println("Candidate Set is empty! catching restart nodes");
		    cc.clear();
		    c = getRestartNodes();
		}

	    } while (candidates.size() == 0);

	    Node next = this.selectNextNode(candidates);

	    currents.remove(n);
	    currents.add(next);
	}
    }

    /**
     * Get new nodes for restarting the walk if the walker blocks
     * 
     * @return collection of new start nodes
     */
    protected List<Node> getRestartNodes() {
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
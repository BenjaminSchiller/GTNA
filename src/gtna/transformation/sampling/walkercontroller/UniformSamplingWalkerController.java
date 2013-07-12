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
 * UniformSamplingWalkerController.java
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
package gtna.transformation.sampling.walkercontroller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;
import gtna.transformation.sampling.AWalkerController;
import gtna.transformation.sampling.CandidateFilter;
import gtna.transformation.sampling.NetworkSample;
import gtna.transformation.sampling.StartNodeSelector;

/**
 * @author Tim
 *
 */
public class UniformSamplingWalkerController extends AWalkerController {

    
    CandidateFilter cf;
    Collection<AWalker> walkers;
    Graph graph;
    /**
     * @param key
     * @param value
     * @param w
     * @param cf
     */
    public UniformSamplingWalkerController(Collection<AWalker> w, CandidateFilter cf, StartNodeSelector sns) {
	super(w.size() + "x_" + w.toArray(new AWalker[0])[0].getValue()
		, w, cf, sns);
	this.walkers = w;
	this.cf = cf;
    }

    /* (non-Javadoc)
     * @see gtna.transformation.sampling.AWalkerController#initialize(gtna.graph.Graph, gtna.graph.Node[])
     */
    @Override
    public void initialize(Graph g, Node[] startNodes) {
	graph = g;
	AWalker[] wa = walkers.toArray(new AWalker[0]);
	for(int i = 0; i < walkers.size(); i++) {
	    // if #walkers > #startNodes assign startnodes with wraparound
	    int snid = i % startNodes.length; 
	    
	    wa[i].setStartNode(startNodes[snid]);
	    
	}

    }

    /* (non-Javadoc)
     * @see gtna.transformation.sampling.AWalkerController#getActiveWalkers()
     */
    @Override
    protected Collection<AWalker> getActiveWalkers() {
	return walkers;
    }

    /* (non-Javadoc)
     * @see gtna.transformation.sampling.AWalkerController#filterCandidates(java.util.Map)
     */
    @Override
    public Map<Node, Collection<Node>> filterCandidates(
	    Map<Node, Collection<Node>> candidates,
	    NetworkSample ns) {
	Map<Node, Collection<Node>> filtered = new HashMap<Node, Collection<Node>>();
	Collection<Node> f;
	for(Node n : candidates.keySet()) {
	    f = this.filterCandidates(candidates.get(n), ns);
	    filtered.put(n, f);
	}
	
	
	return filtered;
    }

    /* (non-Javadoc)
     * @see gtna.transformation.sampling.AWalkerController#filterCandidates(java.util.Collection)
     */
    @Override
    public Collection<Node> filterCandidates(Collection<Node> candidates, 
	    NetworkSample ns) {
	return cf.filterCandidates(candidates, ns);
    }

}

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
 * RandomWalkWalkerController.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;
import gtna.transformation.sampling.AWalkerController;
import gtna.transformation.sampling.CandidateFilter;

/**
 * @author Tim
 * 
 */
public class FrontierSamplingWalkerController extends AWalkerController {

    public CandidateFilter cf;
    Collection<AWalker> walkers;

	public FrontierSamplingWalkerController(Collection<AWalker> w,
	    CandidateFilter cf) {
	super(w.size() + "x_" + w.toArray(new AWalker[0])[0].getValue(), w, cf);

	this.walkers = w;
	this.cf = cf;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gtna.transformation.sampling.AWalkerController#initialize(gtna.graph.
     * Node[])
     */
    @Override
    public void initialize(Node[] startNodes) {
	AWalker[] wa = walkers.toArray(new AWalker[0]);
	for (int i = 0; i < walkers.size(); i++) {
	    // if #walkers > #startNodes assign startnodes with wraparound
	    int snid = i % startNodes.length;
	    wa[i].setStartNode(startNodes[snid]);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.sampling.AWalkerController#getActiveWalkers()
     */
    @Override
    protected Collection<AWalker> getActiveWalkers() {
	// return the walker with the highest node-degree
	Random r = super.getRNG();

	double p = r.nextDouble();
	Map<Node, List<Node>> c;
	Map<AWalker, Integer> wp = new HashMap<AWalker, Integer>();

	for (AWalker w : walkers) {
	    c = w.getCurrentCandidates();
	    int maxD = 0;
	    for (Node n : c.keySet()) {
		if (n.getDegree() > maxD) {
		    maxD = n.getDegree();
		}
	    }
	    wp.put(w, maxD);
	}
	
	AWalker active = null;
	
	double sumP = 0;
	for(Integer i : wp.values()){
		sumP += i;
	}
	
	double currentP = 0.0;
	for(Iterator<Entry<AWalker, Integer>> iter = wp.entrySet().iterator(); iter.hasNext();){
		Entry<AWalker, Integer> w = iter.next();
		currentP += w.getValue()/sumP;
		if(currentP > p){
			active = w.getKey();
			break;
		}
	}
	
	// should never happen
	if(active==null){
		int cd = -1;
		for(AWalker w : walkers){
			active = (cd > w.getCurrentNodes().toArray(new Node[0])[0].getDegree()) ? active : w;
		}
	}
	
	Collection<AWalker> activeWalker = new ArrayList<AWalker>();
	activeWalker.add(active);

	return activeWalker;

    }

}

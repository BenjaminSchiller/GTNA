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
 * AWalkerController.java
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.Parameter;

/**
 * @author Tim
 * 
 */
public abstract class AWalkerController extends Parameter {

	Collection<AWalker> walkers;
	CandidateFilter candidateFilter;
	private StartNodeSelector startNodeSelector;
	private Graph graph;
	private NetworkSample currentSample;

	/**
	 * @param key
	 * @param value
	 */
	public AWalkerController(String walkercontroller) {
		super("WALKER_CONTROLLER", walkercontroller);

	}

	public AWalkerController(String walkercontroller, Collection<AWalker> w, CandidateFilter cf, StartNodeSelector sns) {
		super("WALKER_CONTROLLER", walkercontroller);
		this.walkers = w;
		this.candidateFilter = cf;
		this.startNodeSelector = sns;
	}

	
	public void setGraph(Graph g) {
	    this.graph = g;
	}
	
	public void setCurrentSample(NetworkSample ns) {
	    this.currentSample = ns;
	}
	/**
	 * @param g
	 * @param startNodes
	 */
	public abstract void initialize(Graph g, Node[] startNodes);

	public void initialize(Graph g, Node[] startNodes, Collection<AWalker> w, CandidateFilter cf) {
		walkers = w;
		candidateFilter = cf;
		this.initialize(g, startNodes);
	}

	public boolean isInitialized() {
		if (walkers == null || walkers.size() == 0 || candidateFilter == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Perform one step of walking with all active walker. The number of active walkers depends on the used walking-strategy
	 */
	public void walkOneStep(Graph g, NetworkSample ns) {
	    	this.setCurrentSample(ns);
		if (!isInitialized()) {
			throw new IllegalStateException(
					"You have to initialize the WalkerController first.");
		}
		
		Collection<AWalker> activeWalkers = this.getActiveWalkers();
		for(AWalker w : activeWalkers){
			w.takeAStep(g, ns);
		}
	}

	/**
	 *  evaluates the collection of walkers to return a subset of the walkers which have to take a step
	 */
	protected abstract Collection<AWalker> getActiveWalkers();
	
	/**
	 * Filters the list of candidates for real candidates e.g. without already sampled nodes
	 * @param candidates	possible nodes
	 * @param sample	current networksamples
	 * @return				subset of candidates
	 */
	public abstract Map<Node, Collection<Node>> filterCandidates(Map<Node, Collection<Node>> candidates, NetworkSample sample);
	
	/**
	 * Filters the list of candidates for real candidates e.g. without already sampled nodes
	 * @param candidates	possible nodes
	 * @param sample	current networksamples
	 * @return				subset of candidates
	 */
	public abstract Collection<Node> filterCandidates(Collection<Node> candidates, NetworkSample sample);

	/**
	 * @return
	 */
	public Collection<Node> getRestartNodes() {
	   Node[] rn;
	   Collection<Node> frn, c;
	   do {
	       rn = startNodeSelector.selectStartNodes(graph, 1);
	       c = Arrays.asList(rn);
	       frn = filterCandidates(c, currentSample);
	   }while(frn.size()==0);
	   
	   return frn;
	}

}

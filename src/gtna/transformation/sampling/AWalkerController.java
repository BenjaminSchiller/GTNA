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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.sample.INetworkSample;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;

/**
 * @author Tim
 * 
 */
public abstract class AWalkerController extends Parameter {

	CandidateFilter candidateFilter;
	private SamplingController samplingController;
	Collection<AWalker> walkers;
	private double runtime;

	/**
	 * Instantiate the walker controller
	 * 
	 * @param value
	 */
	public AWalkerController(String walkercontroller) {
		super("WALKER_CONTROLLER", walkercontroller);

	}

	/**
	 * Instantiate the walker controller and set necessary components
	 * 
	 * @param walkercontroller
	 * @param w
	 *            Collection of walkers
	 * @param cf
	 *            candidate filter
	 * @param sns
	 *            start node selector
	 */
	public AWalkerController(String walkercontroller, Collection<AWalker> w,
			CandidateFilter cf) {
		super("WALKER_CONTROLLER", walkercontroller);
		this.walkers = w;
		this.candidateFilter = cf;
	}

	/**
	 * Filters the list of candidates for real candidates e.g. without already
	 * sampled nodes
	 * 
	 * @param candidates
	 *            possible nodes
	 * @param sample
	 *            current networksamples
	 * @return subset of candidates
	 */
	public Map<Node, List<Node>> filterCandidates(
			Map<Node, List<Node>> candidates) {
		Map<Node, List<Node>> filtered = new HashMap<Node, List<Node>>();
		List<Node> f;
		for (Node n : candidates.keySet()) {
			f = this.filterCandidates(candidates.get(n));
			filtered.put(n, f);
		}

		return filtered;
	}

	/**
	 * evaluates the collection of walkers to return a subset of the walkers
	 * which have to take a step
	 */
	protected abstract Collection<AWalker> getActiveWalkers();

	/**
	 * @return
	 */
	public Graph getGraph() {
		return samplingController.getGraph();
	}

	/**
	 * returns the current network sample hold by the sampling controller
	 * 
	 * @return
	 */
	public INetworkSample getNetworkSample() {
		return getSamplingController().getNetworkSample();
	}

	/**
	 * get new start nodes for restarting a walker instance
	 * 
	 * @return collection of new start nodes
	 */
	public List<Node> getRestartNodes() {
		Node[] rn;
		List<Node> c;
		List<Node> frn;
		do {
			rn = samplingController.getStartNodeSelector().selectStartNodes(
					this.getGraph(), 1, samplingController.getRng());
			c = Arrays.asList(rn);
			frn = filterCandidates(c);
		} while (frn.size() == 0);

		return frn;
	}

	/**
	 * @return the samplingController
	 */
	public SamplingController getSamplingController() {
		return samplingController;
	}

	/**
	 * @param g
	 * @param startNodes
	 */
	public abstract void initialize(Node[] startNodes);

	/**
	 * initialize the walker controller
	 * 
	 * @param g
	 *            graph
	 * @param startNodes
	 *            start nodes for the walker instances
	 * @param w
	 *            collection of walkers
	 * @param cf
	 *            candidate filter
	 */
	public void initialize(Node[] startNodes, Collection<AWalker> w,
			CandidateFilter cf) {
		walkers = w;
		candidateFilter = cf;
		this.initialize(startNodes);
	}

	/**
	 * Checks the initialization of the walker controller
	 * 
	 * @return true if ok, else false
	 */
	public boolean isInitialized() {
		if (walkers == null || walkers.size() == 0 || candidateFilter == null
				|| samplingController == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @param samplingController
	 *            the samplingController to set
	 */
	public void setSamplingController(SamplingController samplingController) {
		this.samplingController = samplingController;
	}

	/**
	 * Perform one step of walking with all active walker. The number of active
	 * walkers depends on the used walking-strategy
	 */
	public void walkOneStep() {
		if (!isInitialized()) {
			throw new IllegalStateException(
					"You have to initialize the WalkerController first.");
		}
		
		Timer t = new Timer(); // TODO
		Collection<AWalker> activeWalkers = this.getActiveWalkers();
		for (AWalker w : activeWalkers) {
			w.takeAStep(this.getGraph(), this.getNetworkSample());
		}
		
		// TODO
		t.end();
		runtime += t.getMsec();
	}

	public List<Node> filterCandidates(List<Node> candidates) {
		return candidateFilter.filterCandidates(candidates, this.getNetworkSample());
	}

	/**
	 * @return
	 */
	public Random getRNG() {
	    return samplingController.getRng();
	}

	/**
	 * 
	 */
	public void printRuntimes() {
		System.out.println("Filtering took: " + candidateFilter.getRuntime() + " ms");
		
		System.out.println("Walking took: " + runtime + " ms");
		
	}

}

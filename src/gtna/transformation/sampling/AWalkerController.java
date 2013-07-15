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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> testing & bugfixing (2)
import java.util.Arrays;
import java.util.Collection;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> code format
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
=======
import java.util.Map;
>>>>>>> ASampler default implementation

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.sample.NetworkSample;
import gtna.util.parameter.Parameter;

/**
 * @author Tim
 * 
 */
public abstract class AWalkerController extends Parameter {

	CandidateFilter candidateFilter;
	private SamplingController samplingController;
	Collection<AWalker> walkers;

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
	public Map<Node, Collection<Node>> filterCandidates(
			Map<Node, Collection<Node>> candidates) {
		Map<Node, Collection<Node>> filtered = new HashMap<Node, Collection<Node>>();
		Collection<Node> f;
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
	public NetworkSample getNetworkSample() {
		return getSamplingController().getNetworkSample();
	}

	/**
	 * get new start nodes for restarting a walker instance
	 * 
	 * @return collection of new start nodes
	 */
	public Collection<Node> getRestartNodes() {
		Node[] rn;
		Collection<Node> frn, c;
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

		Collection<AWalker> activeWalkers = this.getActiveWalkers();
		for (AWalker w : activeWalkers) {
			w.takeAStep(this.getGraph(), this.getNetworkSample());
		}
	}

	public Collection<Node> filterCandidates(Collection<Node> candidates) {
		return candidateFilter.filterCandidates(candidates, this.getNetworkSample());
	}

	/**
	 * @return
	 */
	public Random getRNG() {
	    return samplingController.getRng();
	}
=======
=======
=======
=======
import java.util.Collection;

>>>>>>> AWalkerController structure and default implementations
import gtna.graph.Graph;
import gtna.graph.Node;
>>>>>>> Implementing the SamplingController - coarse structure
import gtna.util.parameter.Parameter;

>>>>>>> SamplingController is a Transformation
/**
 * @author Tim
 * 
 */
<<<<<<< HEAD
<<<<<<< HEAD
public abstract class AWalkerController {
>>>>>>> Class Structure
=======
public abstract class AWalkerController extends Parameter{
>>>>>>> SamplingController is a Transformation
=======
public abstract class AWalkerController extends Parameter {

<<<<<<< HEAD
    Collection<AWalker> walkers;
    CandidateFilter candidateFilter;
    private SamplingController samplingController;

    /**
     * Instantiate the walker controller
     * 
     * @param value
     */
    public AWalkerController(String walkercontroller) {
	super("WALKER_CONTROLLER", walkercontroller);

<<<<<<< HEAD
	public AWalkerController(String walkercontroller, Collection<AWalker> w, CandidateFilter cf, StartNodeSelector sns) {
		super("WALKER_CONTROLLER", walkercontroller);
		this.walkers = w;
		this.candidateFilter = cf;
		this.startNodeSelector = sns;
	}
>>>>>>> AWalkerController structure and default implementations
=======
    }
>>>>>>> refactoring and cleanup after debugging (1)

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
	if (walkers == null || walkers.size() == 0 || candidateFilter == null || samplingController == null) {
	    return false;
	} else {
	    return true;
=======
	CandidateFilter candidateFilter;
	private SamplingController samplingController;
	Collection<AWalker> walkers;

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
	public abstract Collection<Node> filterCandidates(
			Collection<Node> candidates);

	
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
	public Map<Node, Collection<Node>> filterCandidates(
			Map<Node, Collection<Node>> candidates) {
		Map<Node, Collection<Node>> filtered = new HashMap<Node, Collection<Node>>();
		Collection<Node> f;
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
	public NetworkSample getNetworkSample() {
		return getSamplingController().getNetworkSample();
>>>>>>> code format
	}

	/**
	 * get new start nodes for restarting a walker instance
	 * 
	 * @return collection of new start nodes
	 */
	public Collection<Node> getRestartNodes() {
		Node[] rn;
		Collection<Node> frn, c;
		do {
			rn = samplingController.getStartNodeSelector().selectStartNodes(
					this.getGraph(), 1);
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

		Collection<AWalker> activeWalkers = this.getActiveWalkers();
		for (AWalker w : activeWalkers) {
			w.takeAStep(this.getGraph(), this.getNetworkSample());
		}
	}

}

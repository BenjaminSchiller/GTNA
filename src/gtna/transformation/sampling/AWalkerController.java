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

	/**
	 * @param key
	 * @param value
	 */
	public AWalkerController(String key, String value) {
		super(key, value);

	}

	public AWalkerController(String key, String value, Collection<AWalker> w, CandidateFilter cf) {
		super(key, value);
		walkers = w;
		candidateFilter = cf;
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
	public void walkOneStep() {
		if (!isInitialized()) {
			throw new IllegalStateException(
					"You have to initialize the WalkerController with a Collection of Walker-instances and a CandidateFilter first.\n"
							+ "Please use the initialize(Graph, Node[], Collection<AWalker>, CandidateFilter) method or "
							+ "the AWalkerController(String, String, Collection<AWalker>, CandidateFilter) constructor.");
		}
		
		Collection<AWalker> activeWalkers = this.getActiveWalkers();
		for(AWalker w : activeWalkers){
			w.takeAStep();
		}
	}

	/**
	 *  evaluates the collection of walkers to return a subset of the walkers which have to take a step
	 */
	protected abstract Collection<AWalker> getActiveWalkers();
	
	/**
	 * Filters the list of candidates for real candidates e.g. without already sampled nodes
	 * @param candidates	possible nodes
	 * @return				subset of candidates
	 */
	public abstract Map<Node, Collection<Node>> filterCandidates(Map<Node, Collection<Node>> candidates);

}

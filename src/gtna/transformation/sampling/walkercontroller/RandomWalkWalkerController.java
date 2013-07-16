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

import java.util.Collection;

import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;
import gtna.transformation.sampling.AWalkerController;
import gtna.transformation.sampling.CandidateFilter;

/**
 * @author Tim
 * 
 */
public class RandomWalkWalkerController extends AWalkerController {

	public CandidateFilter cf;
	Collection<AWalker> walkers;

	public RandomWalkWalkerController(Collection<AWalker> w, CandidateFilter cf) {
		super(w.size() + "x_" + w.toArray(new AWalker[0])[0].getValue(), w, cf);

		if (w.size() != 1) {
			throw new IllegalArgumentException(
					"This Walker Controller is defined for single dimensional usage.");
		}
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
		return walkers; // by definition only one walker
	}

}

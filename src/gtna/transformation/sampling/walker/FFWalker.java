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

import gtna.graph.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Tim
 * 
 */
public class FFWalker extends BFSBaseWalker {

	private double probability = 0.7;

	/**
	 * @param walker
	 */
	public FFWalker() {
		super("FF_WALKER");
		nextQ = new LinkedList<Node>();
	}

	/**
	 * Returns an instance of a ForestFire Walker, adding nodes with the given
	 * probability to the Q
	 * 
	 * @param probability
	 *            probability of adding the neighbor-nodes to the Q, if 1.0 this
	 *            is a BFSWalker
	 */
	public FFWalker(double probability) {
		this();
		this.probability = probability;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.sampling.walker.BFSBaseWalker#chooseNodesToAddToQ
	 * (java.util.Collection)
	 */
	@Override
	protected Collection<Node> chooseNodesToAddToQ(Collection<Node> toFilter) {
		Random r = this.getRNG();
		Collection<Node> filtered = new ArrayList<Node>();
		double pn = 0.0;
		for (Node n : toFilter) {
			pn = r.nextDouble();
			if (pn <= probability) {
				filtered.add(n);
			}
		}

		return filtered;
	}

}

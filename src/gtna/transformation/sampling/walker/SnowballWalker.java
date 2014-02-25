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
import java.util.Collections;
import java.util.List;

/**
 * @author Tim
 * 
 */
public class SnowballWalker extends BFSBaseWalker {

	int amountOfAddedNodesPerStep = 1;
	

	/**
	 * @param walker
	 */
	public SnowballWalker() {
		super("SNOWBALL_WALKER");
	
	}

	/**
	 * @param i
	 */
	public SnowballWalker(int i) {
		this();
		amountOfAddedNodesPerStep = i;
	}


	/**
	 * @param cn
	 * @return
	 */
	@Override
	protected Collection<Node> chooseNodesToAddToQ(Collection<Node> cn) {
		List<Node> q = new ArrayList<Node>();
		ArrayList<Node> temp = new ArrayList<Node>();
		Collection<Node> temp1 = new ArrayList<Node>();

		temp.addAll(cn);
		Collections.shuffle(temp); // shuffle because the neighbor lists are not sorted in real life networks
		temp1 = this.filterCandidates(temp);
		if (temp1.size() <= amountOfAddedNodesPerStep) {
			q.addAll(temp1);

		} else {
			temp.clear();
			temp.addAll(temp1);
			
			int m = Math.min(amountOfAddedNodesPerStep, temp.size());
			for (int i = 0; i < m; i++) {
				q.add(temp.get(i));
			}
		}
		return q;
	}

}

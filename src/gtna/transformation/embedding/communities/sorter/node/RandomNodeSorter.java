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
 * RandomNodeSorter.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.embedding.communities.sorter.node;

import gtna.communities.Community;
import gtna.graph.Graph;
import gtna.util.ArrayUtils;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RandomNodeSorter extends NodeSorter {

	public RandomNodeSorter() {
		super("NODE_SORTER_RANDOM");
	}

	@Override
	public int[] sort(Graph g, Community[] communities, Community community) {
		int[] index = ArrayUtils.initIntArray(community.getNodes().length);
		ArrayUtils.shuffle(index, new Random());

		int[] sorted = new int[index.length];
		for (int i = 0; i < index.length; i++) {
			sorted[i] = community.getNodes()[index[i]];
		}

		return sorted;
	}

}

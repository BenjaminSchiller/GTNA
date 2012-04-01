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
 * SimplePartitioner.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.placementmodels.partitioners;

import gtna.networks.model.placementmodels.PartitionerImpl;

/**
 * Distributes the nodes evenly among the hotspots. Every hotspot will contain
 * <code>Math.floor(nodes / hotspots)</code> nodes, with the remaining nodes
 * evenly distributed among the hotspots (the first (nodes % hotspots) hotspots
 * will contain an additional node).
 * 
 * @author Philipp Neubrand
 * 
 */
public class SimplePartitioner extends PartitionerImpl {

	/**
	 * Standard constructor, no configuration values are needed.
	 */
	public SimplePartitioner() {
		setKey("SIMPLE");
	}

	/**
	 * Distributes the nodes among the hotspots, (nodes % hotspots) hotspots
	 * will contain (floor(nodes / hotspots) + 1) nodes, the rest will contain
	 * (nodes / hotspots) nodes.
	 */
	@Override
	public int[] partition(int nodes, int hotspots) {
		int t = nodes % hotspots;
		int[] ret = new int[hotspots];
		for (int i = 0; i < hotspots; i++) {
			ret[i] = nodes / hotspots;
			if (i < t)
				ret[i]++;

		}
		return ret;
	}
}

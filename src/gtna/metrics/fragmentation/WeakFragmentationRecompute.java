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
 * WeakFragmentationRecompute.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.fragmentation;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorterUpdate;
import gtna.metrics.fragmentation.FragmentationRecompute.Resolution;
import gtna.transformation.partition.WeakConnectivityPartition;

/**
 * @author stef
 *
 */
public class WeakFragmentationRecompute extends FragmentationRecompute {
	
	public WeakFragmentationRecompute(NodeSorterUpdate sorter,
			Resolution resolution,boolean bi) {
		super(FragmentationRecompute.Type.WEAK, sorter, resolution,bi);
	}

	@Override
	protected Partition partition(Graph g, Node[] sorted, boolean[] exclude) {
		return WeakConnectivityPartition.getWeakPartition(g, exclude);
	}

}

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
 * EdgeComparator.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.edges;

import gtna.graph.Edge;
import gtna.id.ring.RingPartition;

import java.util.Comparator;

/**
 * @author Nico
 * 
 */
public class EdgeComparator implements Comparator<Edge> {

	private RingPartition[] partitions;

	public EdgeComparator(RingPartition[] partitions) {
		this.partitions = partitions;
	}

	@Override
	public int compare(Edge x, Edge y) {
		double xStart = Math.min(getPositionRing(x.getSrc()),
				getPositionRing(x.getDst()));
		double xEnd = Math.max(getPositionRing(x.getSrc()),
				getPositionRing(x.getDst()));
		double yStart = Math.min(getPositionRing(y.getSrc()),
				getPositionRing(y.getDst()));
		double yEnd = Math.max(getPositionRing(y.getSrc()),
				getPositionRing(y.getDst()));
		if (xStart > yStart) {
			return 1;
		}
		if (xStart == yStart) {
			if (xEnd > yEnd) {
				return 1;
			}
		}
		return -1;
	}

	protected double getPositionRing(int i) {
		return partitions[i].getStart().getPosition();
	}

}

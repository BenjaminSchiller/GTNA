/*
 * ===========================================================
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
 * RouteImpl.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.routing;

import java.util.List;

/**
 * Representation of a route as generated using a RoutingAlgorithm and used by
 * the Routing metric.
 * 
 * @author benni
 * 
 */
public class Route {
	private int[] route;

	private boolean successful;

	public Route(int[] route, boolean successful) {
		this.route = route;
		this.successful = successful;
	}

	public Route(List<Integer> route, boolean successful) {
		this.route = new int[route.size()];
		int index = 0;
		for (int r : route) {
			this.route[index++] = r;
		}
		this.successful = successful;
	}

	public int[] getRoute() {
		return this.route;
	}

	public int getHops() {
		return this.route.length - 1;
	}

	public boolean isSuccessful() {
		return this.successful;
	}

	public int getFirstNode() {
		return this.route[0];
	}

	public int getLastNode() {
		return this.route[this.route.length - 1];
	}
}

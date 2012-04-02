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
 * IPartitioner.java
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
package gtna.networks.model.placementmodels;

import gtna.util.parameter.Parameter;

/**
 * An <code>IPartitioner</code> distributes a number of nodes to a number of
 * hotspots. In the most simple case, the nodes are distributed even among the
 * hotspots, but more complex algorithms are possible.
 * 
 * @author Philipp Neubrand
 * 
 */
public interface Partitioner {

	/**
	 * The main method of an <code>IPartitioner</code>, its implementation
	 * defines how the nodes are distributed among the hotspots.
	 * 
	 * @param nodes
	 *            The number of nodes to be distributed.
	 * @param hotspots
	 *            The number of hotspots the nodes are to be distributed among.
	 * @return An array of size <code>hotspots</code> containing the number of
	 *         nodes for each hotspot.
	 */
	public int[] partition(int nodes, int hotspots);

	/**
	 * Getter for the configuration Parameters.
	 * 
	 * @return An array containing all the configuration parameters.
	 */
	public Parameter[] getConfigParameters();

}
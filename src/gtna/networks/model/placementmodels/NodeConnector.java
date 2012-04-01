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
 * INodeConnector.java
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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.util.parameter.Parameter;

/**
 * An <code>INodeConnector</code> connects the nodes in a given graph based on
 * their coordinates.
 * 
 * @author Philipp Neubrand
 * 
 */
public interface NodeConnector {

	/**
	 * Connects the supplied nodes based on their coordinates. Can be as simple
	 * as an UDG or something much more complex.
	 * 
	 * @param nodes
	 *            An array of nodes in the graph.
	 * @param coordinates
	 *            The coordinates of the nodes in the graph.
	 * @param g
	 * @return An Edges object containing the connections between the nodes.
	 */
	public Edges connect(Node[] nodes, PlaneIdentifierSpaceSimple coordinates,
			Graph g);

	/**
	 * Getter for the configuration Parameters.
	 * 
	 * @return An array containing all the configuration parameters.
	 */
	public Parameter[] getConfigParameters();

}
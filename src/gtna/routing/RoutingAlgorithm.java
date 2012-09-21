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
 * RoutingAlgorithm.java
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

import gtna.graph.Graph;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.id.data.DataStoreList;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;

import java.util.Random;

/**
 * Interface that must be implemented by all routing algorithms. All methods are
 * implemented by RoutingAlgorithmImpl except for the actual routing, check for
 * applicability, and the initialization.
 * 
 * @author benni
 * 
 */
public abstract class RoutingAlgorithm extends ParameterList {

	protected IdentifierSpace identifierSpace;

	protected DataStoreList dataStorageList;

	public RoutingAlgorithm(String key) {
		super(key);
	}

	public RoutingAlgorithm(String key, Parameter[] parameters) {
		super(key, parameters);
	}

	/**
	 * The given source attempts to route towards the destination specified by
	 * the target identifier.
	 * 
	 * @param graph
	 *            network graph
	 * @param start
	 *            node from which the routing should start
	 * @param target
	 *            target identifier to be routed towards
	 * @return Path object containing information about the routing attempt
	 */
	public abstract Route routeToTarget(Graph graph, int start,
			Identifier target, Random rand);

	/**
	 * Checks if this routing algorithm can be applied to the given network
	 * represented by the set of nodes.
	 * 
	 * @param nodes
	 *            list of all nodes in the network
	 * @return true if this routing algorithm can be applied to the network
	 *         represented by the given set of nodes, false otherwise
	 */
	public abstract boolean applicable(Graph graph);

	/**
	 * Induces a pre-processing of the routing algorithm if required to, e.g.,
	 * distribute replicas or init routing tables.
	 * 
	 * @param nodes
	 *            list of all nodes contained in the network
	 */
	public void preprocess(Graph graph) {
		if (graph.hasProperty("ID_SPACE_0", IdentifierSpace.class)) {
			this.identifierSpace = (IdentifierSpace) graph
					.getProperty("ID_SPACE_0");
		}
		if (graph.hasProperty("DATA_STORAGE_0", DataStoreList.class)) {
			this.dataStorageList = (DataStoreList) graph
					.getProperty("DATA_STORAGE_0");
		}
	}

	public boolean hasDataItem(int node, Identifier id) {
		if (this.dataStorageList == null) {
			return false;
		}
		return this.dataStorageList.getStorageForNode(node).contains(id);
	}

	public boolean isResponsibleForIdentifier(int node, Identifier id) {
		if (this.identifierSpace == null) {
			return false;
		}
		return this.identifierSpace.getPartitions()[node].contains(id);
	}

	public boolean isEndPoint(int node, Identifier id) {
		if (this.dataStorageList != null) {
			return this.hasDataItem(node, id);
		}
		if (this.identifierSpace != null) {
			return this.isResponsibleForIdentifier(node, id);
		}
		return false;
	}

	public static String toString(RoutingAlgorithm[] phases) {
		StringBuffer buff = new StringBuffer();
		for (RoutingAlgorithm phase : phases) {
			if (buff.length() > 0) {
				buff.append("--");
			}
			buff.append(phase.getFolderName());
		}
		return buff.toString();
	}

}

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
 * NetworkSample1.java
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
package gtna.transformation.sampling.sample;

import gtna.graph.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tim
 *
 */
public interface INetworkSample {

	/**
	 * Add the given node in the given round to the sample
	 * 
	 * @param n
	 *            Nodes, added to the sample
	 * @param round
	 *            Round in which the node is sampled
	 * @return true if added
	 */
	public abstract boolean addNodeToSample(Collection<Node> nodes, int round);

	/**
	 * Checks if a node is already sampled
	 * 
	 * @param n
	 *            node
	 * @return true if contained, else false
	 */
	public abstract boolean contains(Node n);

	/**
	 * Return the RevisitFrequency-Map for the whole sampling Key = nodeID (from
	 * the node in the original graph) Value = List of the rounds in which the
	 * node was visited
	 * 
	 * 
	 * @return
	 */
	public abstract HashMap<Integer, List<Integer>> getRevisitFrequency();
	public abstract void setRevisitFrequency(HashMap<Integer, List<Integer>> rf);

	/**
	 * Return the revisitFrequency for the given nodeId
	 * 
	 * @param nodeId
	 * @return List of rounds in which the given node is visited.
	 */
	public abstract List<Integer> getRevisitFrequency(int nodeId);

	/**
	 * Return the sample as Map of: Key = nodeID in the original graph Value =
	 * nodeID in the subgraph induced by the sampling
	 * 
	 * @return
	 */
	public abstract HashMap<Integer, Integer> getSampleNodeMapping();
	public abstract void setSampleNodeMapping(HashMap<Integer, Integer> snm);

	/**
	 * returns the current size of the sample
	 * 
	 * @return
	 */
	public abstract int getSampleSize();

	/**
	 * Returns the new Id of the sampled node
	 * 
	 * @param n
	 *            node in the original graph
	 * @return nodeId in the sampled graph
	 */
	public abstract int getNewIndexOfSampledNode(Node n);

	/**
	 * Returns the new Id of the sampled node
	 * 
	 * @param n
	 *            nodeId in the original graph
	 * @return nodeId in the sampled graph
	 */
	public abstract int getNewIndexOfSampledNode(int n);

	/**
	 * returns a string representing the sampled mapping of nodes.
	 */
	public abstract String toString();

	/**
	 * @return the algorithm
	 */
	public abstract String getAlgorithm();

	/**
	 * @param algorithm
	 *            the algorithm to set
	 */
	public abstract void setAlgorithm(String algorithm);

	/**
	 * @return the scaledown
	 */
	public abstract double getScaledown();

	/**
	 * @param scaledown
	 *            the scaledown to set
	 */
	public abstract void setScaledown(double scaledown);

	/**
	 * @return the dimension
	 */
	public abstract int getDimension();

	/**
	 * @param dimension
	 *            the dimension to set
	 */
	public abstract void setDimension(int dimension);

	/**
	 * @return the revisiting
	 */
	public abstract boolean isRevisiting();

	/**
	 * @param revisiting
	 *            the revisiting to set
	 */
	public abstract void setRevisiting(boolean revisiting);

	/**
	 * @param oldId
	 * @param newId
	 * @param rf
	 */
	public abstract void addNodeEntry(int oldId, int newId, List<Integer> rf);

	/**
	 * @return the numberOfRounds
	 */
	public abstract int getNumberOfRounds();

	/**
	 * @return
	 */
	public abstract INetworkSample cleanInstance();
	
	/**
	 * 
	 */
	public abstract List<Node> filterContainedNodes(List<Node> toFilter);

	/**
	 * @param targetSize
	 */
	public abstract void initialize(int targetSize);
}
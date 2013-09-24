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
 * NetworkSample.java
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
import gtna.util.parameter.Parameter;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tim
 * 
 */
public class NetworkSample extends Parameter {

	Map<Integer, List<Integer>> revisitFrequency;
	Map<Integer, Integer> sampleNodeMapping;
	String algorithm;
	double scaledown;
	int dimension;
	boolean revisiting;
	int numberOfRounds = 0;
	String type;

	/**
	 * @param key
	 * @param value
	 */
	public NetworkSample() {
		this("standard");
	}

	/**
	 * @param algorithm
	 * @param scaledown
	 * @param dimension
	 * @param revisiting
	 */
	public NetworkSample(String algorithm, double scaledown, int dimension,
			boolean revisiting) {
		this("standard", algorithm, scaledown, dimension, revisiting);
	}
	
	/**
	 * @param algorithm
	 * @param scaledown
	 * @param dimension
	 * @param revisiting
	 */
	public NetworkSample(String type, String algorithm, double scaledown, int dimension,
			boolean revisiting) {
		super("Network_SAMPLE", type);
		sampleNodeMapping = new HashMap<Integer, Integer>();
		revisitFrequency = new HashMap<Integer, List<Integer>>();
		this.type = type;
		this.algorithm = algorithm;
		this.scaledown = scaledown;
		this.dimension = dimension;
		this.revisiting = revisiting;
	}
	
	

	/**
	 * @param type
	 */
	public NetworkSample(String type) {
		super("NETWORK_SAMPLE", type);
		this.type = type;
		sampleNodeMapping = new HashMap<Integer, Integer>();
		revisitFrequency = new HashMap<Integer, List<Integer>>();

	}

	/**
	 * Add the given node in the given round to the sample
	 * 
	 * @param n
	 *            Nodes, added to the sample
	 * @param round
	 *            Round in which the node is sampled
	 * @return true if added
	 */
	public boolean addNodeToSample(Collection<Node> nodes, int round) {
		if (!initialized()) {
			throw new IllegalStateException("NetworkSample is not initialized!");
		}
		if (!uniqueRound(round)) {
			throw new IllegalArgumentException(
					"round has to be unique but round = " + round
							+ " was already seen");
		}

		for (Node n : nodes) {
			if (!sampleNodeMapping.containsKey(n.getIndex())) {
				// add node to the sample and initialize the RF for this node
				int newId = sampleNodeMapping.size();
				sampleNodeMapping.put(n.getIndex(), newId);

				List<Integer> rF = new LinkedList<Integer>();
				rF.add(round);
				revisitFrequency.put(n.getIndex(), rF);
			} else {
				// no need to add the node to the sample again, just add it to
				// the RF
				List<Integer> rF = revisitFrequency.get(n.getIndex());
				rF.add(round);
				revisitFrequency.put(n.getIndex(), rF);
			}
		}

		numberOfRounds++;

		return true;
	}

	/**
	 * Checks if a node is already sampled
	 * 
	 * @param n
	 *            node
	 * @return true if contained, else false
	 */
	public boolean contains(Node n) {
		if (sampleNodeMapping.containsKey(n.getIndex())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return the RevisitFrequency-Map for the whole sampling Key = nodeID (from
	 * the node in the original graph) Value = List of the rounds in which the
	 * node was visited
	 * 
	 * 
	 * @return
	 */
	public Map<Integer, List<Integer>> getRevisitFrequency() {
		return revisitFrequency;
	}

	/**
	 * Return the revisitFrequency for the given nodeId
	 * 
	 * @param nodeId
	 * @return List of rounds in which the given node is visited.
	 */
	public List<Integer> getRevisitFrequency(int nodeId) {
		if (revisitFrequency.containsKey(nodeId)) {
			return revisitFrequency.get(nodeId);
		}

		return new LinkedList<Integer>();
	}

	/**
	 * Return the sample as Map of: Key = nodeID in the original graph Value =
	 * nodeID in the subgraph induced by the sampling
	 * 
	 * @return
	 */
	public Map<Integer, Integer> getSampleNodeMapping() {
		return sampleNodeMapping;
	}

	/**
	 * returns the current size of the sample
	 * 
	 * @return
	 */
	public int getSampleSize() {
		return sampleNodeMapping.size();
	}

	/**
	 * Checks if the necessary maps are initialized
	 * 
	 * @return
	 */
	private boolean initialized() {
		if (sampleNodeMapping != null && revisitFrequency != null) {
			return true;
		}

		return false;
	}

	/**
	 * checks if the given round is already known known = any node is visited in
	 * this round
	 * 
	 * @param round
	 * @return
	 */
	private boolean uniqueRound(int round) {
		for (List<Integer> l : revisitFrequency.values()) {
			if (l.contains(round)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the new Id of the sampled node
	 * 
	 * @param n
	 *            node in the original graph
	 * @return nodeId in the sampled graph
	 */
	public int getNewIndexOfSampledNode(Node n) {
		return getNewIndexOfSampledNode(n.getIndex());
	}

	/**
	 * Returns the new Id of the sampled node
	 * 
	 * @param n
	 *            nodeId in the original graph
	 * @return nodeId in the sampled graph
	 */
	public int getNewIndexOfSampledNode(int n) {
		return sampleNodeMapping.get(n);
	}

	/**
	 * returns a string representing the sampled mapping of nodes.
	 */
	public String toString() {
		return printMapping();
	}

	private String printMapping() {
		StringBuilder sb = new StringBuilder();
		for (int n : sampleNodeMapping.keySet()) {
			Integer newId = sampleNodeMapping.get(n);
			String rf = getRevisitFrequencyString(revisitFrequency.get(n));

			sb.append(n + ";" + newId + ";" + rf + "\n");
		}

		return sb.toString();
	}

	/**
	 * @param list
	 * @return
	 */
	private String getRevisitFrequencyString(List<Integer> list) {
		String rf = "";

		for (Integer i : list) {
			if (rf.equals("")) {
				rf = i.toString();
			} else {
				rf = rf + "," + i.toString();
			}

		}

		return rf;
	}

	/**
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return this.algorithm;
	}

	/**
	 * @param algorithm
	 *            the algorithm to set
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @return the scaledown
	 */
	public double getScaledown() {
		return this.scaledown;
	}

	/**
	 * @param scaledown
	 *            the scaledown to set
	 */
	public void setScaledown(double scaledown) {
		this.scaledown = scaledown;
	}

	/**
	 * @return the dimension
	 */
	public int getDimension() {
		return this.dimension;
	}

	/**
	 * @param dimension
	 *            the dimension to set
	 */
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	/**
	 * @return the revisiting
	 */
	public boolean isRevisiting() {
		return this.revisiting;
	}

	/**
	 * @param revisiting
	 *            the revisiting to set
	 */
	public void setRevisiting(boolean revisiting) {
		this.revisiting = revisiting;
	}

	/**
	 * @param oldId
	 * @param newId
	 * @param rf
	 */
	public void addNodeEntry(int oldId, int newId, List<Integer> rf) {
		sampleNodeMapping.put(oldId, newId);
		revisitFrequency.put(oldId, rf);

	}

	/**
	 * @return the numberOfRounds
	 */
	public int getNumberOfRounds() {
		
		int nor = 0;
		
		for(Entry<Integer, List<Integer>> e : revisitFrequency.entrySet()){
			for(Integer r : e.getValue()){
				nor = Math.max(nor, r);
			}
		}
		
		
		
		
		return nor;
	}


	/**
	 * @return
	 */
	public NetworkSample cleanInstance() {
		return new NetworkSample(type, algorithm, scaledown, dimension, revisiting);
		
	}

	/**
	 * 
	 */
	private void clear() {

		sampleNodeMapping = new HashMap<Integer, Integer>();
		revisitFrequency = new HashMap<Integer, List<Integer>>();
		numberOfRounds = 0;
		
	}
	

}

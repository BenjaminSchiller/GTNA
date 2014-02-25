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

import gnu.trove.THashMap;
import gtna.graph.Node;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tim
 * 
 */
public class NetworkSampleFast extends Parameter implements INetworkSample {

	THashMap<Integer, Integer> sampleNodeMapping;
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
	public NetworkSampleFast() {
		this("fast");
	}

	/**
	 * @param algorithm
	 * @param scaledown
	 * @param dimension
	 * @param revisiting
	 */
	public NetworkSampleFast(String algorithm, double scaledown, int dimension,
			boolean revisiting) {
		this("fast", algorithm, scaledown, dimension, revisiting);
	}
	
	
	/**
	 * @param algorithm
	 * @param scaledown
	 * @param dimension
	 * @param revisiting
	 */
	public NetworkSampleFast(String type, String algorithm, double scaledown, int dimension,
			boolean revisiting) {
		super("NETWORK_SAMPLE", type);
		sampleNodeMapping = new THashMap<Integer, Integer>();

		this.type = type;
		this.algorithm = algorithm;
		this.scaledown = scaledown;
		this.dimension = dimension;
		this.revisiting = revisiting;
	}
	
	

	/**
	 * @param type
	 */
	public NetworkSampleFast(String type) {
		super("NETWORK_SAMPLE", type);
		this.type = type;
		sampleNodeMapping = new THashMap<Integer, Integer>();


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
	public HashMap<Integer, List<Integer>> getRevisitFrequency() {
		return new HashMap<Integer, List<Integer>>();
	}

	/**
	 * Return the revisitFrequency for the given nodeId
	 * 
	 * @param nodeId
	 * @return List of rounds in which the given node is visited.
	 */
	public List<Integer> getRevisitFrequency(int nodeId) {
		return new LinkedList<Integer>();
	}

	/**
	 * Return the sample as Map of: Key = nodeID in the original graph Value =
	 * nodeID in the subgraph induced by the sampling
	 * 
	 * @return
	 */
	public HashMap<Integer, Integer> getSampleNodeMapping() {
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		hm.putAll(sampleNodeMapping);
		return hm;
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
			if (sampleNodeMapping != null) {
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
		return true; // TODO!
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
			
			sb.append(n + ";" + newId + ";\n");
		}

		return sb.toString();
	}

	

	/**
	 * @param oldId
	 * @param newId
	 * @param rf
	 */
	public void addNodeEntry(int oldId, int newId, List<Integer> rf) {
		sampleNodeMapping.put(oldId, newId);
	}

	/**
	 * @return the numberOfRounds
	 */
	public int getNumberOfRounds() {
		return numberOfRounds;
	}


	/**
	 * @return
	 */
	public INetworkSample cleanInstance() {
		return new NetworkSampleFull(type, algorithm, scaledown, dimension, revisiting);
		
	}

	/**
	 * 
	 */
	private void clear() {

		sampleNodeMapping = new THashMap<Integer, Integer>(); 
		
		numberOfRounds = 0;
		
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getAlgorithm()
	 */
	@Override
	public String getAlgorithm() {
		return this.algorithm;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#setAlgorithm(java.lang.String)
	 */
	@Override
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getScaledown()
	 */
	@Override
	public double getScaledown() {
		return this.scaledown;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#setScaledown(double)
	 */
	@Override
	public void setScaledown(double scaledown) {
		this.scaledown = scaledown;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getDimension()
	 */
	@Override
	public int getDimension() {
		return this.dimension;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#setDimension(int)
	 */
	@Override
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#isRevisiting()
	 */
	@Override
	public boolean isRevisiting() {
		return this.revisiting;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#setRevisiting(boolean)
	 */
	@Override
	public void setRevisiting(boolean revisiting) {
		this.revisiting = revisiting;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	Deque<Integer> sampleNodeMapping1;
//	String algorithm;
//	double scaledown;
//	int dimension;
//	boolean revisiting;
//	int numberOfRounds = 0;
//	String type;
//
//	/**
//	 * @param key
//	 * @param value
//	 */
//	public NetworkSampleFast() {
//		this("fast");
//	}
//
//	/**
//	 * @param algorithm
//	 * @param scaledown
//	 * @param dimension
//	 * @param revisiting
//	 */
//	public NetworkSampleFast(String algorithm, double scaledown, int dimension,
//			boolean revisiting) {
//		this("fast", algorithm, scaledown, dimension, revisiting);
//	}
//	
//	/**
//	 * @param algorithm
//	 * @param scaledown
//	 * @param dimension
//	 * @param revisiting
//	 */
//	public NetworkSampleFast(String type, String algorithm, double scaledown, int dimension,
//			boolean revisiting) {
//		super(type);
//		sampleNodeMapping1 = new ArrayDeque<Integer>(25000);
//		this.type = type;
//		this.algorithm = algorithm;
//		this.scaledown = scaledown;
//		this.dimension = dimension;
//		this.revisiting = revisiting;
//	}
//	
//	
//
//	/**
//	 * @param type
//	 */
//	public NetworkSampleFast(String type) {
//		super(type);
//		this.type = type;
//		sampleNodeMapping = new HashMap<Integer, Integer>();
//		sampleNodeMapping1 = new ArrayDeque<Integer>(25000);
//
//	}
//
//	/**
//	 * Add the given node in the given round to the sample
//	 * 
//	 * @param n
//	 *            Nodes, added to the sample
//	 * @param round
//	 *            Round in which the node is sampled
//	 * @return true if added
//	 */
//	public boolean addNodeToSample(Collection<Node> nodes, int round) {
//
//		if (!initialized()) {
//			throw new IllegalStateException("NetworkSample is not initialized!");
//		}
//		if (!uniqueRound(round)) {
//			throw new IllegalArgumentException(
//					"round has to be unique but round = " + round
//							+ " was already seen");
//		}
//
//
//
//		for (Node n : nodes) {
//			if (!sampleNodeMapping1.contains(n.getIndex())) {
//				sampleNodeMapping1.add(n.getIndex()); // add old index, the new index is the index within the list
//			}
//		}
//		numberOfRounds++;
//		return true;
//	}
//
//	/**
//	 * Checks if a node is already sampled
//	 * 
//	 * @param n
//	 *            node
//	 * @return true if contained, else false
//	 */
//	public boolean contains(Node n) {
//		if (sampleNodeMapping1.contains(n.getIndex())) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * Return the RevisitFrequency-Map for the whole sampling Key = nodeID (from
//	 * the node in the original graph) Value = List of the rounds in which the
//	 * node was visited
//	 * 
//	 * 
//	 * @return
//	 */
//	public Map<Integer, List<Integer>> getRevisitFrequency() {
//		return new HashMap<Integer, List<Integer>>();
//	}
//
//	/**
//	 * Return the revisitFrequency for the given nodeId
//	 * 
//	 * @param nodeId
//	 * @return List of rounds in which the given node is visited.
//	 */
//	public List<Integer> getRevisitFrequency(int nodeId) {
//		return new LinkedList<Integer>();
//	}
//
//	/**
//	 * Return the sample as Map of: Key = nodeID in the original graph Value =
//	 * nodeID in the subgraph induced by the sampling
//	 * 
//	 * @return
//	 */
//	public Map<Integer, Integer> getSampleNodeMapping() {
//		
//		Map<Integer, Integer> map = new HashMap<Integer, Integer>(sampleNodeMapping1.size());
//		
//		Iterator<Integer> li = sampleNodeMapping1.iterator();
//		int i = 0;
//		while(li.hasNext()){
//			int newIndex = i;
//			int oldIndex = li.next();
//			
//			map.put(oldIndex, newIndex);
//			i++;
//		}
//		
//		
//		return map;
//	}
//
//	/**
//	 * returns the current size of the sample
//	 * 
//	 * @return
//	 */
//	public int getSampleSize() {
//		return sampleNodeMapping1.size();
//	}
//
//	/**
//	 * Checks if the necessary maps are initialized
//	 * 
//	 * @return
//	 */
//	private boolean initialized() {
//		if (sampleNodeMapping1 != null && revisitFrequency != null) {
//			return true;
//		}
//
//		return false;
//	}
//
//	/**
//	 * checks if the given round is already known known = any node is visited in
//	 * this round
//	 * 
//	 * @param round
//	 * @return
//	 */
//	private boolean uniqueRound(int round) {
//		for (List<Integer> l : revisitFrequency.values()) {
//			if (l.contains(round)) {
//				return false;
//			}
//		}
//
//		return true;
//	}
//
//	/**
//	 * Returns the new Id of the sampled node
//	 * 
//	 * @param n
//	 *            node in the original graph
//	 * @return nodeId in the sampled graph
//	 */
//	public int getNewIndexOfSampledNode(Node n) {
//		return getNewIndexOfSampledNode(n.getIndex());
//	}
//
//	/**
//	 * Returns the new Id of the sampled node
//	 * 
//	 * @param n
//	 *            nodeId in the original graph
//	 * @return nodeId in the sampled graph
//	 */
//	public int getNewIndexOfSampledNode(int n) {
//		
//		Iterator<Integer> li = sampleNodeMapping1.iterator();
//		int i = 0;
//		while(li.hasNext()){
//			if(li.next() == n){
//				return i;
//			}
//			i++;
//		}
//		
//		return -1;
//	}
//
//	/**
//	 * returns a string representing the sampled mapping of nodes.
//	 */
//	public String toString() {
//		return printMapping();
//	}
//
//	private String printMapping() {
//		StringBuilder sb = new StringBuilder();
//		
//		Iterator<Integer> li = sampleNodeMapping1.iterator();
//		int i = 0;
//		while(li.hasNext()){
//			int newId = i;
//			int oldId = li.next();
//			
//			sb.append(oldId + ";" + newId + ";\n");
//			i++;
//		}
//
//		return sb.toString();
//	}
//
//	/**
//	 * @param oldId
//	 * @param newId
//	 * @param rf
//	 */
//	public void addNodeEntry(int oldId, int newId, List<Integer> rf) {
//		sampleNodeMapping1.add(oldId);
//	}
//
//	/**
//	 * @return the numberOfRounds
//	 */
//	public int getNumberOfRounds() {		
//		return numberOfRounds;
//	}
//
//
//	/**
//	 * @return
//	 */
//	public NetworkSampleFast cleanInstance() {
//		return new NetworkSampleFast(type, algorithm, scaledown, dimension, revisiting);		
//	}
//
//	/**
//	 * 
//	 */
//	private void clear() {
//		sampleNodeMapping1 = new LinkedList<Integer>();
//		numberOfRounds = 0;		
//	}
	

}

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
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tim
 * 
 */
public class NetworkSampleFull extends Parameter implements INetworkSample {

	THashMap<Integer, List<Integer>> revisitFrequency;
	THashMap<Integer, Integer> sampleNodeMapping;
	String algorithm;
	double scaledown;
	int dimension;
	boolean revisiting;
	int numberOfRounds = 0;
	String type;
	private boolean[] contained;

	/**
	 * @param key
	 * @param value
	 */
	public NetworkSampleFull() {
		this("standard");
	}

	/**
	 * @param algorithm
	 * @param scaledown
	 * @param dimension
	 * @param revisiting
	 */
	public NetworkSampleFull(String algorithm, double scaledown, int dimension,
			boolean revisiting) {
		this("standard", algorithm, scaledown, dimension, revisiting);
	}
	
	/**
	 * @param algorithm
	 * @param scaledown
	 * @param dimension
	 * @param revisiting
	 */
	public NetworkSampleFull(String type, String algorithm, double scaledown, int dimension,
			boolean revisiting) {
		super("NETWORK_SAMPLE", type);
		sampleNodeMapping = new THashMap<Integer, Integer>();
		revisitFrequency = new THashMap<Integer, List<Integer>>();
		this.type = type;
		this.algorithm = algorithm;
		this.scaledown = scaledown;
		this.dimension = dimension;
		this.revisiting = revisiting;
	}
	
	

	/**
	 * @param type
	 */
	public NetworkSampleFull(String type) {
		super("NETWORK_SAMPLE", type);
		this.type = type;
		sampleNodeMapping = new THashMap<Integer, Integer>();
		revisitFrequency = new THashMap<Integer, List<Integer>>();

	}
	
	public void initialize(int targetsize){
		contained = new boolean[targetsize];
		Arrays.fill(contained, false);
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#addNodeToSample(java.util.Collection, int)
	 */
	@Override
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
			if (!contains(n)) {
				// add node to the sample and initialize the RF for this node
				int newId = sampleNodeMapping.size();
				sampleNodeMapping.put(n.getIndex(), newId);

				List<Integer> rF = new LinkedList<Integer>();
				rF.add(round);
				revisitFrequency.put(n.getIndex(), rF);
				
				contained[n.getIndex()]=true;
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

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#contains(gtna.graph.Node)
	 */
	@Override
	public boolean contains(Node n) {
		return contained[n.getIndex()];
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getRevisitFrequency()
	 */
	@Override
	public HashMap<Integer, List<Integer>> getRevisitFrequency() {
		HashMap<Integer, List<Integer>> hm = new HashMap<Integer, List<Integer>>();
		hm.putAll(revisitFrequency);
		return hm;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getRevisitFrequency(int)
	 */
	@Override
	public List<Integer> getRevisitFrequency(int nodeId) {
		if (revisitFrequency.containsKey(nodeId)) {
			return revisitFrequency.get(nodeId);
		}

		return new LinkedList<Integer>();
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getSampleNodeMapping()
	 */
	@Override
	public HashMap<Integer, Integer> getSampleNodeMapping() {
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		hm.putAll(sampleNodeMapping);
		return hm;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getSampleSize()
	 */
	@Override
	public int getSampleSize() {
		return sampleNodeMapping.size();
	}

	/**
	 * Checks if the necessary maps are initialized
	 * 
	 * @return
	 */
	private boolean initialized() {
			if (sampleNodeMapping != null && revisitFrequency != null && contained != null) {
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

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getNewIndexOfSampledNode(gtna.graph.Node)
	 */
	@Override
	public int getNewIndexOfSampledNode(Node n) {
		return getNewIndexOfSampledNode(n.getIndex());
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getNewIndexOfSampledNode(int)
	 */
	@Override
	public int getNewIndexOfSampledNode(int n) {
		return sampleNodeMapping.get(n); 
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#toString()
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#addNodeEntry(int, int, java.util.List)
	 */
	@Override
	public void addNodeEntry(int oldId, int newId, List<Integer> rf) {
		sampleNodeMapping.put(oldId, newId);
		revisitFrequency.put(oldId, rf);
		contained[oldId]=true;

	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#getNumberOfRounds()
	 */
	@Override
	public int getNumberOfRounds() {
		
		int nor = 0;
		
		for(Entry<Integer, List<Integer>> e : revisitFrequency.entrySet()){
			for(Integer r : e.getValue()){
				nor = Math.max(nor, r);
			}
		}
		
		
		
		
		return nor;
	}


	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.NetworkSample1#cleanInstance()
	 */
	@Override
	public INetworkSample cleanInstance() {
		return new NetworkSampleFull(type, algorithm, scaledown, dimension, revisiting);
		
	}

	/**
	 * 
	 */
	private void clear() {

		sampleNodeMapping = new THashMap<Integer, Integer>(); 
		revisitFrequency = new THashMap<Integer, List<Integer>>();
		numberOfRounds = 0;
		Arrays.fill(contained, false);
		
	}
	
	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.INetworkSample#filterContainedNodes(java.util.List)
	 */
	@Override
	public List<Node> filterContainedNodes(List<Node> toFilter) {
		
		LinkedList<Node> filtered = new LinkedList<Node>();
		
		ListIterator<Node> lit = toFilter.listIterator();
		while(lit.hasNext()){
			Node n = lit.next();
			if(!contained[n.getIndex()]){
				filtered.add(n);
			}
		}
		
		return filtered;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.INetworkSample#setRevisitFrequency()
	 */
	@Override
	public void setRevisitFrequency(HashMap<Integer, List<Integer>> rf) {
		this.revisitFrequency = new THashMap<Integer, List<Integer>>();
		this.revisitFrequency.putAll(rf);
		
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.sample.INetworkSample#setSampleNodeMapping()
	 */
	@Override
	public void setSampleNodeMapping(HashMap<Integer, Integer> snm) {
		this.sampleNodeMapping = new THashMap<Integer, Integer>();
		this.sampleNodeMapping.putAll(snm);
		
	}
	

}

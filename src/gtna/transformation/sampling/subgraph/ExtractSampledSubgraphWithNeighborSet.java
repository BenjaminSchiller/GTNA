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
 * SubgraphTransformation.java
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
package gtna.transformation.sampling.subgraph;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.Sample;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class ExtractSampledSubgraphWithNeighborSet extends Transformation {

    /**
     * @param key
     */
    public ExtractSampledSubgraphWithNeighborSet() {
	super("SUBGRAPH", new Parameter[] {
		new StringParameter("SUBGPRAPHFUNCTION", "extract_with_neighborset")
	});
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
     */
    @Override
    public Graph transform(Graph g) {
	Sample sample = (Sample) g.getProperty("SAMPLE_0");

	Set<Integer> oldIds = sample.getSampledIds();
	Edges edges = g.getEdges();

	List<Node> sampledNodes = new ArrayList<Node>();
	Map<Node, List<Integer>> neighborSet = new HashMap<Node, List<Integer>>();

	// get Sampled nodes
	for (Integer i : oldIds) {
		Node ni = g.getNode(i);
	    sampledNodes.add(ni);
	    
	    for(int oi : ni.getOutgoingEdges()){
	    	if(!oldIds.contains(oi)){ // if a neighbor is not contained in the sample, its contained in the neighborset
	    		neighborSet = addNodeToNeighborSet(neighborSet, g.getNode(oi), ni);
	    	}
	    }
	    
	}

	for (Node n : sampledNodes) {
	    setNewOutgoingEdges(g, sample, oldIds, n);
	    setNewIncomingEdges(g, sample, oldIds, n);
	    n.setIndex(sample.getNewNodeId(n.getIndex()));
	}
	
	for(Node n : neighborSet.keySet()){
		int newIndexN = sampledNodes.size();
		List<Integer> neighborsN = neighborSet.get(n);
		List<Integer> inE = new ArrayList<Integer>();
		int[] inEdges;
		int[] outEdges = new int[0]; // we haven't visited the neighborhoodset nodes, we don't know anything
		
		for(Integer i : neighborsN){
			inE.add(sample.getNewNodeId(i));
		}
		
		inEdges = new int[inE.size()];
		for(int i = 0; i < inEdges.length; i++){
			inEdges[i] = inE.get(i);
		}
		
		
		n.setIndex(newIndexN);
		n.setIncomingEdges(inEdges);
		n.setOutgoingEdges(outEdges);
		sampledNodes.add(n);
	}
	
	
	// set new Nodearray
	Node[] newNodes = new Node[sampledNodes.size()];
	for(Node n : sampledNodes) {
	    newNodes[n.getIndex()] = n;
	}
	g.setNodes(newNodes);

	g.setName(g.getName() + " (SAMPLED)");
	
	
	return g;
    }

    /**
	 * @param neighborSet
	 * @param oi
	 * @param ni
	 * @return
	 */
	private Map<Node, List<Integer>> addNodeToNeighborSet(
			Map<Node, List<Integer>> neighborSet, Node oi, Node ni) {
		
		if(neighborSet.containsKey(oi)){
			List<Integer> oin = neighborSet.get(oi);
			if(!oin.contains(ni)){
				oin.add(ni.getIndex());
			}
			
			neighborSet.put(oi, oin);
		} else {
			List<Integer> oin = new ArrayList<Integer>();
			oin.add(ni.getIndex());
			neighborSet.put(oi, oin);
		}
		
		return neighborSet;
	}

	/**
	 * @param sampledNodes
	 * @param g
	 * @return
	 */
	private List<Node> addNeighborSet(List<Node> sampledNodes, Graph g) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * @param g
     * @param sample
     * @param oldIds
     * @param n
     */
    private void setNewIncomingEdges(Graph g, Sample sample,
	    Set<Integer> oldIds, Node n) {
	int[] nIn = n.getIncomingEdges();
	List<Integer> newIn = new ArrayList<Integer>();
	
	// collect sampled edges and calculate new ids
	for(int i : nIn) {
	    if(oldIds.contains(i)) {
		newIn.add(sample.getNewNodeId(i));
	    }
	}
	
	// set new incoming edges
	nIn = new int[newIn.size()];
	for(int i = 0; i < newIn.size(); i++) {
	    nIn[i] = newIn.get(i);
	}
	
	n.setIncomingEdges(nIn);

    }

    /**
     * @param g
     * @param sample
     * @param oldIds
     * @param n
     */
    private void setNewOutgoingEdges(Graph g, Sample sample,
	    Set<Integer> oldIds, Node n) {
	int[] nOut = n.getOutgoingEdges();
	List<Integer> newOut = new ArrayList<Integer>();
	
	// collect sampled edges and calculate new ids
	for (int i : nOut) {
	    if (oldIds.contains(i)){
		newOut.add(sample.getNewNodeId(i));
	    }
	}

	// set new outgoing edges
	nOut = new int[newOut.size()];
	for (int i = 0; i < newOut.size(); i++) {
	    nOut[i] = newOut.get(i);
	}
	n.setOutgoingEdges(nOut);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
     */
    @Override
    public boolean applicable(Graph g) {
	return g.hasProperty("SAMPLE_0");
    }

}

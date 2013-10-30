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
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.Sample;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class ExtractSampledSubgraph extends Transformation {

    private int index = 0;

    /**
     * @param key
     */
    public ExtractSampledSubgraph() {
	super("SUBGRAPH", new Parameter[] {
		new StringParameter("SUBGPRAPHFUNCTION", "extract")
	});
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
     */
    @Override
    public Graph transform(Graph g) {
	Graph gi = new Graph(g.getName() + " (SAMPLED)");

	Sample sample = (Sample) g.getProperty("SAMPLE_" + this.index);
	
	Node[] ni = Node.init(sample.getSampledIds().size(), gi);
	Edges e = new Edges(ni, g.computeNumberOfEdges());
	
	gi.setNodes(ni);
	
	
	
	Set<Integer> oldIds = sample.getSampledIds();
	

	List<Node> sampledNodes = new ArrayList<Node>();

	// get Sampled nodes
	for (Integer i : oldIds) {
	    sampledNodes.add(g.getNode(i));
	}

	for (Node n : sampledNodes) {
	    Node giN = gi.getNode(sample.getNewNodeId(n.getIndex()));
	    setNewOutgoingEdges(gi, sample, oldIds, n, giN);
	    setNewIncomingEdges(gi, sample, oldIds, n, giN);
//	    n.setIndex(sample.getNewNodeId(n.getIndex()));
	}
	
	// set new Nodearray
//	Node[] newNodes = new Node[sampledNodes.size()];
//	for(Node n : sampledNodes) {
//	    newNodes[n.getIndex()] = n;
//	}
//	gi.setNodes(newNodes);
	
	for(Node nin : ni) {
	    for(int i : nin.getOutgoingEdges()) {
		e.add(nin.getIndex(), i);
	    }
	}
	
	gi.setNodes(ni);
	e.fill();
	gi.setName(g.getName() + " (SAMPLED)");
	
	
	return gi;
    }

    /**
     * @param g
     * @param sample
     * @param oldIds
     * @param n
     */
    private void setNewIncomingEdges(Graph g, Sample sample,
	    Set<Integer> oldIds, Node n, Node newN) {
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
	
	newN.setIncomingEdges(nIn);

    }

    /**
     * @param g
     * @param sample
     * @param oldIds
     * @param n
     */
    private void setNewOutgoingEdges(Graph g, Sample sample,
	    Set<Integer> oldIds, Node n, Node newN) {
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
	newN.setOutgoingEdges(nOut);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
     */
    @Override
    public boolean applicable(Graph g) {
	return g.hasProperty("SAMPLE_" + this.index);
    }
    
    public void setIndex(int i) {
	this.index = i;
    }

}

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
import java.util.List;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class ExtractSampledSubgraph extends Transformation {

    /**
     * @param key
     */
    public ExtractSampledSubgraph() {
	super("GENERATE_SAMPLED_GRAPH", new Parameter[] {
		new StringParameter("SUBGRAPH", "extract")
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

	// get Sampled nodes
	for (Integer i : oldIds) {
	    sampledNodes.add(g.getNode(i));
	}

	for (Node n : sampledNodes) {
	    setNewOutgoingEdges(g, sample, oldIds, n);
	    setNewIncomingEdges(g, sample, oldIds, n);
	    n.setIndex(sample.getNewNodeId(n.getIndex()));
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

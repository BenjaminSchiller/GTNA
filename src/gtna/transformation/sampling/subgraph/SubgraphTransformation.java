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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import apple.awt.ClientPropertyApplicator.Property;
import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.Sample;

/**
 * @author Tim
 *
 */
public class SubgraphTransformation extends Transformation {

    /* (non-Javadoc)
     * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
     */
    @Override
    public Graph transform(Graph g) {
	Sample sample = (Sample)g.getProperty("SAMPLE_0");
	
	Set<Integer> oldIds = sample.getSampledIds();
	Edges edges = g.getEdges();
	
	List<Node> sampledNodes = new ArrayList<Node>();
	
	// get Sampled nodes
	for(Integer i : oldIds) {
	    sampledNodes.add(g.getNode(i));
	}
	
	for(Node n : sampledNodes) {
	    Edge[] ne = n.getEdges();
	    List<Edge> sampledEdges = new ArrayList<Edge>();
	    for(Edge e : ne) {
		// save edge only iff src and dst nodes are sampled
		if(oldIds.contains(e.getSrc()) &&
			oldIds.contains(e.getDst())) {
		    sampledEdges.add(e);
		    
		}
	    }
	    
	}
	
	
	return g;
    }

    /* (non-Javadoc)
     * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
     */
    @Override
    public boolean applicable(Graph g) {
	return g.hasProperty("SAMPLE_0");
    }

}

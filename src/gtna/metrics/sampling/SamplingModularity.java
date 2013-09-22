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
 * SamplingModularity.java
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
package gtna.metrics.sampling;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.sampling.Sample;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class SamplingModularity extends Metric {

    private int sampleIndex;
    private double sampleModularity;
    private double sampleEdges;
    private int edges;
    private int sampleNodes;
    private int nodes;

    /**
     * @param key
     */
    public SamplingModularity() {
	super("SAMPLING_MODULARITY");
	this.sampleIndex = 0;
    }

    /**
     * @param key
     * @param parameters
     */
    public SamplingModularity(int i) {
	this();
	this.sampleIndex = i;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
     * gtna.networks.Network, java.util.HashMap)
     */
    @Override
    public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
	Sample s = this.getSampleProperty(g);
	
	double sumDegrees = 0;
	double withinEdgeSum = 0;
	
	Set<Integer> sample = s.getSampledIds();
	for(Integer nodeId : sample) {
	    Node node = g.getNode(nodeId);
	    
	    sumDegrees += node.getOutDegree();
	    // count incoming edges? (incoming into the sample-community) TODO
	    
	    // count edges within the sample-community
	    for(Integer outEdge : node.getOutgoingEdges()) {
		if(sample.contains(outEdge)) {
		    withinEdgeSum++;
		}
	    }
	}
	
//	nodes = g.getNodeCount();
//	sampleNodes = sample.size();
//	edges = g.getEdges().size();
//	sampleEdges = Math.abs(sumDegrees);
	
	sampleModularity = withinEdgeSum / sumDegrees;
	
	

    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.metrics.Metric#writeData(java.lang.String)
     */
    @Override
    public boolean writeData(String folder) {
	boolean success = true;
	return success;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.metrics.Metric#getSingles()
     */
    @Override
    public Single[] getSingles() {
	Single smSampleModularity = new Single("SAMPLING_MODULARITY_SAMPLING_MODULARITY", sampleModularity);
	
	return new Single[] {smSampleModularity};
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
     * gtna.networks.Network, java.util.HashMap)
     */
    @Override
    public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
	if (g.hasProperty("SAMPLE_" + sampleIndex)) {
	    return true;
	} else {
	    return false;
	}
    }
    
    
    private Sample getSampleProperty(Graph g) {
	return (Sample)g.getProperty("SAMPLE_" + sampleIndex);
    }

}

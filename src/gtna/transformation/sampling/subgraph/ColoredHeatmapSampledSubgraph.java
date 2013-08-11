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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import apple.awt.ClientPropertyApplicator.Property;
import gtna.drawing.NodeColors;
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
public class ColoredHeatmapSampledSubgraph extends Transformation {

    /**
     * @param key
     */
    public ColoredHeatmapSampledSubgraph() {
	super("HEATMAP_SAMPLED_GRAPH");
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
     */
    @Override
    public Graph transform(Graph g) {
	Sample sample = (Sample) g.getProperty("SAMPLE_0");
	
	Sample[] samples = (Sample[]) g.getProperties("SAMPLE");
	Map<Integer, Integer> occurences = calculateSampleOccurences(samples);
	
	Color[] C = this.getColors(2, 0);
	Color[] colors = new Color[g.getNodes().length];
	Node[] nodes = g.getNodes();
	for (int i = 0; i < nodes.length; i++) {
	    if(sampledNodes.contains(nodes[i].getIndex()))
		colors[i] = C[0];
	    else
		colors[i] = C[1];
	}
	NodeColors cc = new NodeColors(colors);
	g.addProperty(g.getNextKey("NODE_COLORS"), cc);
	
	

	g.setName(g.getName() + " (SAMPLED)");
	
	
	return g;
    }
    
    /**
     * @param samples
     * @return
     */
    private Map<Integer, Integer> calculateSampleOccurences(Sample[] samples) {
	Map<Integer, Integer> hm = new HashMap<Integer, Integer>();
	
	for(Sample s : samples) {
	    Set<Integer> ids = s.getSampledIds();
	    for(Integer i : ids) {
		Integer o = hm.get(i);
		if(o != null) {
		    hm.put(i, o+1);
		}else {
		    hm.put(i, 1);
		}
	    }
	}
	
	return hm;
    }


    private Color[] getColors(int number, int start) {
	Color[] init = new Color[] { Color.green, Color.red, Color.blue,
			Color.cyan, Color.black, Color.orange, Color.yellow,
			Color.MAGENTA, Color.pink, Color.darkGray, Color.gray };
	Color[] c = new Color[number];
	for (int i = start; i-start < c.length; i++) {
		c[i-start] = init[i % init.length];
	}
	return c;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
     */
    @Override
    public boolean applicable(Graph g) {
	return g.hasProperty(sampleKey);
    }

}

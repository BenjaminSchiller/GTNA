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

import gtna.drawing.NodeColors;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.Sample;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class ColoredHeatmapSampledSubgraph extends Transformation {

    /**
     * @param key
     */
    public ColoredHeatmapSampledSubgraph() {
	super("SUBGRAPH", new Parameter[] {
		new StringParameter("SUBGPRAPHFUNCTION", "heatmap") 
	});
    }

    private Color base = Color.YELLOW;

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
     */
    @Override
    public Graph transform(Graph g) {
	GraphProperty[] p = g.getProperties("SAMPLE");
	Collection<Sample> samples = new ArrayList<Sample>();
	for(GraphProperty pi : p) {
	    if(pi instanceof Sample) {
		samples.add((Sample) pi);
	    }
	}
	Map<Integer, Integer> occurences = calculateSampleOccurences(samples.toArray(new Sample[0]));
	Color[] C = calculateColors(occurences);
	Color[] colors = new Color[g.getNodes().length];
	Node[] nodes = g.getNodes();
	for (int i = 0; i < nodes.length; i++) {
	    colors[i] = assignColorToNode(C, nodes[i], occurences);
	}
	NodeColors cc = new NodeColors(colors);
	g.addProperty(g.getNextKey("NODE_COLORS"), cc);

	g.setName(g.getName() + " (SAMPLED)");

	return g;
    }

    /**
     * @param occurences
     * @return
     */
    private Color[] calculateColors(Map<Integer, Integer> occurences) {
	int max = 0;
	for(Integer oi : occurences.values()) {
	    if(max < oi) {
		max = oi;
	    }
	}
	Color[] C = this.getColors(1+max);
	return C;
    }

    /**
     * @param c
     * @param node
     * @param occurences
     * @return
     */
    private Color assignColorToNode(Color[] c, Node node,
	    Map<Integer, Integer> occurences) {
	Integer o = occurences.get(node.getIndex());
	if (o == null) {
	    return c[0];
	} else {
	    return c[o];
	}
    }

    /**
     * @param samples
     * @return
     */
    private Map<Integer, Integer> calculateSampleOccurences(Sample[] samples) {
	Map<Integer, Integer> hm = new HashMap<Integer, Integer>();

	for (Sample s : samples) {
	    Set<Integer> ids = s.getSampledIds();
	    for (Integer i : ids) {
		Integer o = hm.get(i);
		if (o != null) {
		    hm.put(i, o + 1);
		} else {
		    hm.put(i, 1);
		}
	    }
	}

	return hm;
    }

    /**
     * returns an array of <b>number</b> colors
     * @param number	number of colors to be calculated
     * @return		array of colors
     */
    private Color[] getColors(int number) {
	Color[] c = new Color[number];
	
	c[0] = Color.LIGHT_GRAY;
	
	int parts = Math.max(number-2, 1);
	for(int i = 0; i < number-1; i++) {
	    int r = base.getRed();
	    int g = base.getGreen();	
	    int b = base.getBlue();
	    
	    g = g - (255/parts)*i;
	    
	    c[i+1] = new Color(r, g, b);
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
	return g.hasProperty("SAMPLE_0");
    }

}

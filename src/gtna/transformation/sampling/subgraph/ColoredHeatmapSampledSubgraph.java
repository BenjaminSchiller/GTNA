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

<<<<<<< HEAD
<<<<<<< HEAD
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

=======
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import apple.awt.ClientPropertyApplicator.Property;
=======
>>>>>>> debugging the WFSampling app...
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

>>>>>>> count node occurrences in samples
/**
 * @author Tim
 * 
 */
public class ColoredHeatmapSampledSubgraph extends Transformation {

    /**
     * @param key
     */
    public ColoredHeatmapSampledSubgraph() {
<<<<<<< HEAD
<<<<<<< HEAD
	super("SUBGRAPH", new Parameter[] {
		new StringParameter("SUBGPRAPHFUNCTION", "heatmap") 
	});
    }

    private Color base = Color.YELLOW;

=======
	super("HEATMAP_SAMPLED_GRAPH");
=======
	super("HEATMAP_SAMPLED_GRAPH", new Parameter[] {
		new StringParameter("SUBGPRAPHFUNCTION", "heatmap") 
	});
>>>>>>> debugging the WFSampling app...
    }
<<<<<<< HEAD
    
    
>>>>>>> count node occurrences in samples
=======

    private Color base = Color.YELLOW;

>>>>>>> Calculate colors. Here: yellow to red. Try to calculate the colors automatically?
    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
     */
    @Override
    public Graph transform(Graph g) {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Calculate colors. Here: yellow to red. Try to calculate the colors automatically?
	GraphProperty[] p = g.getProperties("SAMPLE");
	Collection<Sample> samples = new ArrayList<Sample>();
	for(GraphProperty pi : p) {
	    if(pi instanceof Sample) {
		samples.add((Sample) pi);
	    }
	}
	Map<Integer, Integer> occurences = calculateSampleOccurences(samples.toArray(new Sample[0]));
<<<<<<< HEAD
<<<<<<< HEAD
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

=======
	Sample sample = (Sample) g.getProperty("SAMPLE_0");
	
	Sample[] samples = (Sample[]) g.getProperties("SAMPLE");
	Map<Integer, Integer> occurences = calculateSampleOccurences(samples);
	
	Color[] C = this.getColors(2, 0);
=======

	Color[] C = this.getColors(10);
>>>>>>> Calculate colors. Here: yellow to red. Try to calculate the colors automatically?
=======
	Color[] C = calculateColors(occurences);
>>>>>>> Heatmap yellow -> red is calculated automatically for all SAMPLE properties of a graph
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
<<<<<<< HEAD
    
>>>>>>> count node occurrences in samples
=======

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

>>>>>>> Calculate colors. Here: yellow to red. Try to calculate the colors automatically?
    /**
     * @param samples
     * @return
     */
    private Map<Integer, Integer> calculateSampleOccurences(Sample[] samples) {
	Map<Integer, Integer> hm = new HashMap<Integer, Integer>();
<<<<<<< HEAD
<<<<<<< HEAD

	for (Sample s : samples) {
	    Set<Integer> ids = s.getSampledIds();
	    for (Integer i : ids) {
		Integer o = hm.get(i);
		if (o != null) {
		    hm.put(i, o + 1);
		} else {
=======
	
	for(Sample s : samples) {
=======

	for (Sample s : samples) {
>>>>>>> Calculate colors. Here: yellow to red. Try to calculate the colors automatically?
	    Set<Integer> ids = s.getSampledIds();
	    for (Integer i : ids) {
		Integer o = hm.get(i);
<<<<<<< HEAD
		if(o != null) {
		    hm.put(i, o+1);
		}else {
>>>>>>> count node occurrences in samples
=======
		if (o != null) {
		    hm.put(i, o + 1);
		} else {
>>>>>>> Calculate colors. Here: yellow to red. Try to calculate the colors automatically?
		    hm.put(i, 1);
		}
	    }
	}
<<<<<<< HEAD
<<<<<<< HEAD

	return hm;
    }

    /**
     * returns an array of <b>number</b> colors
     * @param number	number of colors to be calculated
     * @return		array of colors
     */
<<<<<<< HEAD
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

=======
	
=======

>>>>>>> Calculate colors. Here: yellow to red. Try to calculate the colors automatically?
	return hm;
    }

=======
>>>>>>> correct calculation of colors of sampled nodes. not sampled nodes are colored gray to highlight the sampling
    private Color[] getColors(int number) {
	Color[] c = new Color[number];
	
	c[0] = Color.LIGHT_GRAY;
	
	int parts = Math.max(number-2, 1);
	for(int i = 0; i < number-1; i++) {
	    int r = base.getRed();
	    int g = base.getGreen();	
	    int b = base.getBlue();
	    
<<<<<<< HEAD
<<<<<<< HEAD
	    g = g - (255/number)*i;
	    c[i] = new Color(r, g, b);
	}
>>>>>>> count node occurrences in samples
=======
	    g = g - (255/(number-2))*i;
=======
	    g = g - (255/parts)*i;
>>>>>>> allow heat map coloring for a single sample -> division by 0 prevented
	    
	    c[i+1] = new Color(r, g, b);
	}	

>>>>>>> correct calculation of colors of sampled nodes. not sampled nodes are colored gray to highlight the sampling
	return c;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
     */
    @Override
    public boolean applicable(Graph g) {
<<<<<<< HEAD
<<<<<<< HEAD
	return g.hasProperty("SAMPLE_0");
=======
	return g.hasProperty(sampleKey);
>>>>>>> count node occurrences in samples
=======
	return g.hasProperty("SAMPLE_0");
>>>>>>> Calculate colors. Here: yellow to red. Try to calculate the colors automatically?
    }

}

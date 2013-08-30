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
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.Sample;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.awt.Color;
import java.util.Set;
=======
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import apple.awt.ClientPropertyApplicator.Property;
=======
>>>>>>> debugging the WFSampling app...
import gtna.drawing.NodeColors;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.Sample;
<<<<<<< HEAD
>>>>>>> First implementation of coloring the sampled subgraph
=======
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.awt.Color;
import java.util.Set;
>>>>>>> debugging the WFSampling app...

/**
 * @author Tim
 * 
 */
public class ColorSampledSubgraph extends Transformation {

<<<<<<< HEAD
<<<<<<< HEAD
    String sampleKey = "SAMPLE_0"; 
    int key = 0;
=======
>>>>>>> First implementation of coloring the sampled subgraph
=======
    String sampleKey = "SAMPLE_0"; 
    int key = 0;
>>>>>>> Color different sample properties in different colors. (if a node is sampled multiple times - the last set color will win!)
    /**
     * @param key
     */
    public ColorSampledSubgraph() {
<<<<<<< HEAD
<<<<<<< HEAD
	super("SUBGRAPH", new Parameter[] {
		new StringParameter("SUBGPRAPHFUNCTION", "colored")
	});
    }
    
    public String getSamplingKeyToColoredSample() {
	return sampleKey;
    }
    
    public void setSamplingKeyToColoredSample(int key) {
	this.sampleKey = "SAMPLE_" + key;
	this.key = key;
=======
	super("COLOR_SAMPLED_GRAPH");
>>>>>>> First implementation of coloring the sampled subgraph
=======
	super("COLOR_SAMPLED_GRAPH", new Parameter[] {
		new StringParameter("SUBGRAPH", "colored")
	});
>>>>>>> debugging the WFSampling app...
    }
    
    public String getSamplingKeyToColoredSample() {
	return sampleKey;
    }
    
    public void setSamplingKeyToColoredSample(int key) {
	this.sampleKey = "SAMPLE_" + key;
	this.key = key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
     */
    @Override
    public Graph transform(Graph g) {
<<<<<<< HEAD
<<<<<<< HEAD
	Sample sample = (Sample) g.getProperty(sampleKey);

	Set<Integer> sampledNodes = sample.getSampledIds();
	
	Color[] C = this.getColors(2, key*2);
=======
	Sample sample = (Sample) g.getProperty("SAMPLE_0");

	Set<Integer> sampledNodes = sample.getSampledIds();
	
	Color[] C = this.getColors(2);
>>>>>>> First implementation of coloring the sampled subgraph
=======
	Sample sample = (Sample) g.getProperty(sampleKey);

	Set<Integer> sampledNodes = sample.getSampledIds();
	
	Color[] C = this.getColors(2, key*2);
>>>>>>> Color different sample properties in different colors. (if a node is sampled multiple times - the last set color will win!)
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
    
<<<<<<< HEAD
<<<<<<< HEAD
    private Color[] getColors(int number, int start) {
=======
    private Color[] getColors(int number) {
>>>>>>> First implementation of coloring the sampled subgraph
=======
    private Color[] getColors(int number, int start) {
>>>>>>> Color different sample properties in different colors. (if a node is sampled multiple times - the last set color will win!)
	Color[] init = new Color[] { Color.green, Color.red, Color.blue,
			Color.cyan, Color.black, Color.orange, Color.yellow,
			Color.MAGENTA, Color.pink, Color.darkGray, Color.gray };
	Color[] c = new Color[number];
<<<<<<< HEAD
<<<<<<< HEAD
	for (int i = start; i-start < c.length; i++) {
		c[i-start] = init[i % init.length];
=======
	for (int i = 0; i < c.length; i++) {
		c[i] = init[i % init.length];
>>>>>>> First implementation of coloring the sampled subgraph
=======
	for (int i = start; i-start < c.length; i++) {
		c[i-start] = init[i % init.length];
>>>>>>> Color different sample properties in different colors. (if a node is sampled multiple times - the last set color will win!)
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
<<<<<<< HEAD
<<<<<<< HEAD
	return g.hasProperty(sampleKey);
=======
	return g.hasProperty("SAMPLE_0");
>>>>>>> First implementation of coloring the sampled subgraph
=======
	return g.hasProperty(sampleKey);
>>>>>>> Color different sample properties in different colors. (if a node is sampled multiple times - the last set color will win!)
    }

}

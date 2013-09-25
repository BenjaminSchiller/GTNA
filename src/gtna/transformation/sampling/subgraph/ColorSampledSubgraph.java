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
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.Sample;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.awt.Color;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class ColorSampledSubgraph extends Transformation {

    String sampleKey = "SAMPLE_0"; 
    int key = 0;
    /**
     * @param key
     */
    public ColorSampledSubgraph() {
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
     */
    @Override
    public Graph transform(Graph g) {
	Sample sample = (Sample) g.getProperty(sampleKey);

	Set<Integer> sampledNodes = sample.getSampledIds();
	
	Color[] C = this.getColors(2, key*2);
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

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
 * SamplingController.java
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
package gtna.transformation.sampling;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Tim
 *
 */
public class SamplingController extends Transformation {

	AWalkerController walkerController;
	ASampler sampler;
	AStartNodeSelector startNodeSelector;
	NetworkSample networkSample;
	
	int dimension;
	double scaledown;
	boolean revisiting;
	
	public SamplingController(String algorithm, AWalkerController awc, ASampler as, AStartNodeSelector asns, double scaledown, int dimension, boolean revisiting){
		super("SAMPLING_"+algorithm+"_"+awc.getKey() + "_" + as.getKey(), new Parameter[]{
				awc, 
				as, 
				asns, 
				new DoubleParameter("SCALEDOWN", scaledown),
				new IntParameter("DIMENSIONS", dimension),
				new BooleanParameter("REVISITING", revisiting)
		});
		
		// we must have at least 1 walker
		if(dimension < 1){
			throw new IllegalArgumentException("Dimension has to be at least 1 but is: " + dimension);
		}
		
		// a scaledown has to be a float between 0.0 and 1.0 representing the percentage of the original size
		if(scaledown < 0.0 || scaledown > 1.0){
			throw new IllegalArgumentException("Scaledown has to be in [0,1] but is: " + scaledown);
		}
		
		
		this.scaledown = scaledown;
		this.dimension = dimension;
		this.revisiting = revisiting;
		
		
	}
	
	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		// do not sample twice with the same algorithm
		if(g.hasProperty(this.key))
			return false;
		else
			return true;
	}
	
	public int calculateTargetedSampleSize(Graph g, double scaledown){
		
		int originalSize = g.getNodeCount();
				
		return (int) Math.ceil(originalSize * scaledown);
	}
	
	public boolean sampleGraph(Graph g, int targetSampleSize){
		Node[] startNodes = startNodeSelector.selectStartNodes(g, dimension); 
		
		walkerController.initialize(g, startNodes); // place walker(s) on start node(s)
		sampler.initialize(walkerController, targetSampleSize); // initialize Sampler, eventually sampling the start nodes
		
		// walk -> sample loop as long new nodes are sampled
		do{
			walkerController.walkOneStep();
		}while(sampler.sampleOneStepNodes());
		
		return true;
	}

}

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
 * ASampler.java
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.Parameter;

/**
 * @author Tim
 *
 */
public abstract class ASampler extends Parameter {

	
	AWalkerController walkerController;
	
	
	
	/**
	 * 
	 * @param sampler
	 */
	public ASampler(String sampler) {
		super("SAMPLER", sampler);
	}	
	
	/**
	 * Initializes the ASampler implementation
	 * 
	 * This default implementation calls sampleOneStepNodes to sample
	 * the startnode(s)!
	 * @param targetSampleSize  max nodes sampled in this round 
	 * @param walkerController 
	 * 
	 */
	public Collection<Node> initialize(Graph g, NetworkSample ns, int maxNodes){
		return sampleOneStepNodes(g, ns, maxNodes);
	}
	
	public void setWalkerController(AWalkerController awc) {
	    this.walkerController = awc;
	}

	public boolean isInitialized(){
		if(walkerController == null){
			return false;
		}
		
		return true;
	}
	/**
	 * @return
	 */
	public Collection<Node> sampleOneStepNodes(Graph g, NetworkSample ns, int maxNodes){
		Collection<AWalker> walkers = walkerController.getActiveWalkers();
		Collection<Node> sampled = new LinkedList<Node>();
		
		for(AWalker w : walkers){
			Map<Node, Collection<Node>> wcc = w.getCurrentCandidates(g);
			Map<Node, Collection<Node>> fc = walkerController.filterCandidates(wcc, ns);
			sampled.addAll(sampleNodes(fc));
		}		
		return sampled;
		
	}
	
	
	protected abstract Collection<Node> sampleNodes(Map<Node, Collection<Node>> filteredCandidates);

}

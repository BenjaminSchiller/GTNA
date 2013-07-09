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
 * SamplingAlgorithmFactory.java
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

import java.util.ArrayList;
import java.util.Collection;

import gtna.transformation.sampling.sampler.VisitedNodeSampler;
import gtna.transformation.sampling.walker.UniformRandomWalker;
import gtna.transformation.sampling.walkercontroller.UniformSamplingWalkerController;
import gtna.transformation.sampling.*;

/**
 * @author Tim
 *
 */
public class SamplingAlgorithmFactory {

    /**
     * Enumeration of by default supported sampling algorithms
     * @author Tim
     *
     */
    public enum SamplingAlgorithm {
	UNIFORMSAMPLING,
	RANDOMWALK,
	RANDOMWALK_METROPOLIZED,
	RANDOMWALK_DEGREECORRECTION,
	RANDOMWALK_MULTIPLE,
	FRONTIERSAMPLING,
	RANDOMSTROLL,
	RANDOMSTROLL_DEGREECORRECTION,
	RANDOMJUMP,
	BFS,
	SNOWBALLSAMPLING,
	RESPONDENTDRIVENSAMPLING,
	FORESTFIRE,
	DFS
    }
    
    /**
     * build an instance of a default sampling transformation
     * 
     * @param sg
     * @param revisiting
     * @param dimension IGNORED BY SINGLEDIMENSIONAL SAMPLING ALGORITHMS!
     * @return
     */
    public static SamplingController getInstanceOf(SamplingAlgorithm sg, double scaledown, boolean revisiting, int dimension) {
	SamplingController sc;
	ASampler as;
	AWalker aw;
	AWalkerController awc;
	CandidateFilter cf;
	NetworkSample ns;
	StartNodeSelector sns;
	String algorithm;
	
	switch(sg) {
	case UNIFORMSAMPLING:
	    as = new VisitedNodeSampler();
	    aw = new UniformRandomWalker();
	    cf = new CandidateFilter(revisiting);
	    Collection<AWalker> cw = new ArrayList<AWalker>();
	    cw.add(aw);
	    awc = new UniformSamplingWalkerController(cw, cf);
	    aw.setWalkerController(awc);
	    sns = new StartNodeSelector("RANDOM");
	    algorithm="UNIFORM_SAMPLING";
	    break;
	default:
	    throw new IllegalArgumentException("Not supported algorithm");
	}	
	sc = new SamplingController(algorithm, awc, as, sns, scaledown, dimension, revisiting);
	return sc;
    }
}

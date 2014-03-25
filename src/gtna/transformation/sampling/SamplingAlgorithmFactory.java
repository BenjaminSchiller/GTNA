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

import gtna.transformation.sampling.sample.NetworkSampleFull;
import gtna.transformation.sampling.sample.INetworkSample;
import gtna.transformation.sampling.sample.NetworkSampleFast;
import gtna.transformation.sampling.sampler.RoundBasedVisitedNodeSampler;
import gtna.transformation.sampling.sampler.VisitedNodeSampler;
import gtna.transformation.sampling.walker.BFSWalker;
import gtna.transformation.sampling.walker.DFSWalker;
import gtna.transformation.sampling.walker.FFWalker;
import gtna.transformation.sampling.walker.MetropolizedRandomWalkWalker;
import gtna.transformation.sampling.walker.RDSWalker;
import gtna.transformation.sampling.walker.RandomJumpWalker;
import gtna.transformation.sampling.walker.RandomWalkDegreeCorrectionWalker;
import gtna.transformation.sampling.walker.RandomWalkWalker;
import gtna.transformation.sampling.walker.SnowballWalker;
import gtna.transformation.sampling.walker.UniformRandomWalker;
import gtna.transformation.sampling.walkercontroller.FrontierSamplingWalkerController;
import gtna.transformation.sampling.walkercontroller.MultipleRandomWalkWalkerController;
import gtna.transformation.sampling.walkercontroller.RandomWalkWalkerController;
import gtna.transformation.sampling.walkercontroller.UniformSamplingWalkerController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Tim
 * 
 */
public class SamplingAlgorithmFactory {

    /**
     * Enumeration of by default supported sampling algorithms
     * 
     * @author Tim
     * 
     */
    @SuppressWarnings("javadoc")
    public enum SamplingAlgorithm {
    	UNIFORMSAMPLING, 
    	RANDOMWALK, RANDOMWALK_METROPOLIZED, RANDOMWALK_DEGREECORRECTION, RANDOMWALK_MULTIPLE, FRONTIERSAMPLING, 
    	RANDOMSTROLL, RANDOMSTROLL_DEGREECORRECTION, 
    	RANDOMJUMP, 
    	BFS, SNOWBALLSAMPLING, RESPONDENTDRIVENSAMPLING, FORESTFIRE, 
    	DFS
    }


    /**
     * build an instance of a default sampling transformation
     * 
     * @param sg
     * @param revisiting
     * @param dimension
     *            IGNORED BY SINGLEDIMENSIONAL SAMPLING ALGORITHMS!
     * @param randomSeed - can be <b>null</b> to use a standard RNG
     * @return
     */
    public static SamplingController getInstanceOf(SamplingAlgorithm sg,
	    double[] scaledown, boolean revisiting, int dimension, Long randomSeed) {
    	
    	return getInstanceOf(sg, scaledown, revisiting, dimension, randomSeed, false);
    }
    
    
    /**
     * build an instance of a default sampling transformation
     * 
     * @param sg
     * @param revisiting
     * @param dimension
     *            IGNORED BY SINGLEDIMENSIONAL SAMPLING ALGORITHMS!
     * @param randomSeed - can be <b>null</b> to use a standard RNG
     * @param fast - fast or completely logged sampling
     * @return
     */
    public static SamplingController getInstanceOf(SamplingAlgorithm sg,
	    double[] scaledown, boolean revisiting, int dimension, Long randomSeed, boolean fast) {
	SamplingController sc;
	ASampler as;
	AWalker aw;
	Collection<AWalker> cw;
	AWalkerController awc;
	CandidateFilter cf;
	StartNodeSelector sns;
	String algorithm;
	@SuppressWarnings("unused")
	INetworkSample ns;

	switch (sg) {
		case UNIFORMSAMPLING:
			as = new VisitedNodeSampler();
			aw = new UniformRandomWalker();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new UniformSamplingWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);

			algorithm = "UNIFORM_SAMPLING";
			break;
		case RANDOMWALK:
			as = new VisitedNodeSampler();
			aw = new RandomWalkWalker();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "RANDOM_WALK";
			break;
		case RANDOMWALK_METROPOLIZED:
			as = new VisitedNodeSampler();
			aw = new MetropolizedRandomWalkWalker();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "METROPOLIZED_RANDOM_WALK";
			break;
		case RANDOMWALK_DEGREECORRECTION:
			as = new VisitedNodeSampler();
			aw = new RandomWalkDegreeCorrectionWalker();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "RANDOM_WALK_WITH_DEGREE_CORRECTION";
			break;
		case RANDOMWALK_MULTIPLE:
			as = new VisitedNodeSampler();
			
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			for(int i = 0; i < dimension; i++){
				cw.add(new RandomWalkWalker());
			}
			awc = new MultipleRandomWalkWalkerController(cw, cf);
			for(AWalker mrw : cw){
				mrw.setWalkerController(awc);
			}
			as.setWalkerController(awc);
		
			algorithm = "MULTIPLE_RANDOM_WALK";
			break;
		case FRONTIERSAMPLING:
			as = new VisitedNodeSampler();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			for(int i = 0; i < dimension; i++){
				cw.add(new RandomWalkWalker());
			}
			awc = new FrontierSamplingWalkerController(cw, cf);
			for(AWalker mrw : cw){
				mrw.setWalkerController(awc);
			}
			as.setWalkerController(awc);
			
			algorithm = "FRONTIER_SAMPLING";
			break;	
		case RANDOMSTROLL:
			as = new RoundBasedVisitedNodeSampler(5);
			aw = new RandomWalkWalker();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "RANDOM_STROLL";
			break;
		case RANDOMSTROLL_DEGREECORRECTION:
			as = new RoundBasedVisitedNodeSampler(5);
			aw = new RandomWalkDegreeCorrectionWalker();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "RANDOM_STROLL_WITH_DEGREE_CORRECTION";
			break;
		case RANDOMJUMP:
			as = new VisitedNodeSampler();
			aw = new RandomJumpWalker(0.15);
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "RANDOM_JUMP";
			break;
		case BFS:
			as = new VisitedNodeSampler();
			aw = new BFSWalker();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "BFS";
			break;
		case SNOWBALLSAMPLING:
			as = new VisitedNodeSampler();
			aw = new SnowballWalker(3);
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "SNOWBALL_SAMPLING";
			break;
		case RESPONDENTDRIVENSAMPLING:
			as = new VisitedNodeSampler();
			aw = new RDSWalker(8);
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "RESPONDENT_DRIVEN_SAMPLING";
			break;
		case FORESTFIRE:
			as = new VisitedNodeSampler();
			aw = new FFWalker(0.3);
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "FOREST_FIRE_SAMPLING";
			break;
		case DFS:
			as = new VisitedNodeSampler();
			aw = new DFSWalker();
			cf = new CandidateFilter(revisiting);
			sns = new StartNodeSelector("RANDOM");
			cw = new ArrayList<AWalker>();
			cw.add(aw);
			awc = new RandomWalkWalkerController(cw, cf);
			aw.setWalkerController(awc);
			as.setWalkerController(awc);
			
			algorithm = "DFS_SAMPLING";
			break;
		default:
			throw new IllegalArgumentException("Not supported algorithm");
	}
	
	Arrays.sort(scaledown);
	
	if(fast){
		ns = new NetworkSampleFast(algorithm, scaledown[scaledown.length-1], dimension,
			revisiting);
	}else{
		ns = new NetworkSampleFull(algorithm, scaledown[scaledown.length-1], dimension,
			revisiting);
	}
	sc = new SamplingController(algorithm, awc, as, sns, ns, scaledown,
		dimension, revisiting, randomSeed);
	
	awc.setSamplingController(sc);
	as.setSamplingController(sc);
	return sc;
    }
    
    
}

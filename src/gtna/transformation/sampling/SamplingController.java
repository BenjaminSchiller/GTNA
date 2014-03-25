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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import sun.security.util.ManifestDigester.Entry;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.sample.INetworkSample;
import gtna.transformation.sampling.sample.NetworkSampleFast;
import gtna.transformation.sampling.sample.NetworkSampleFull;
import gtna.util.DeterministicRandom;
import gtna.util.Timer;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author Tim
 * 
 */
public class SamplingController extends Transformation {

	private AWalkerController walkerController;
	private ASampler sampler;
	private StartNodeSelector startNodeSelector;
	private INetworkSample networkSample;
	private Graph graph;

	private int dimension;
	private double scaledown;
	@SuppressWarnings({ "unused" })
	private boolean revisiting;
	private Node[] startNodes;
	private Random rng;
	private double deriveSamplingRuntime;
	private double runSamplingRuntime;
	private double runtimeSOS;
	private double runtimeWOS;
	private double runtimeRB;
	private int round;
	private double runtimeAddingNode;
	private TreeSet<Double> intermediateSizes;
	private String algorithm;

	public SamplingController(String algorithm, AWalkerController awc,
			ASampler as, StartNodeSelector asns, INetworkSample ns, double[] scaledown,
			int dimension, boolean revisiting, Long randomSeed) {
		this(algorithm, awc, as, asns, ns, scaledown, dimension, revisiting);
		if (randomSeed != null)
			this.setRng(new DeterministicRandom(randomSeed));

	}

	/**
	 * @param algorithm
	 *            name of the algorithm implemented with this instance
	 * @param awc
	 *            instance of an abstract walker controller
	 * @param as
	 *            instance of an abstract sampler
	 * @param asns
	 *            instance of a start node selector
	 * @param scaledown
	 *            scaledown factor
	 * @param dimension
	 *            number of walkers
	 * @param revisiting
	 *            revisiting algorithm
	 */
	public SamplingController(String algorithm, AWalkerController awc,
			ASampler as, StartNodeSelector asns, INetworkSample ns, double[] scaledown,
			int dimension, boolean revisiting) {
		super("SAMPLING", new Parameter[] {
				new StringParameter("ALGORITHM", algorithm), awc, as, asns,
				new StringParameter("SCALEDOWN", Arrays.toString(scaledown)),
				new IntParameter("DIMENSIONS", dimension),
				new BooleanParameter("REVISITING", revisiting) });

		// we must have at least 1 walker
		if (dimension < 1) {
			throw new IllegalArgumentException(
					"Dimension has to be at least 1 but is: " + dimension);
		}

		// a scaledown has to be a float between 0.0 and 1.0 representing the
		// percentage of the original size
//		if (scaledown < 0.0 || scaledown > 1.0) {
//			throw new IllegalArgumentException(
//					"Scaledown has to be in [0,1] but is: " + scaledown);
//		}
		
		this.intermediateSizes = new TreeSet<Double>(); // TODO
		this.intermediateSizes = checkAndAddScaledowns(scaledown);
		this.scaledown = this.intermediateSizes.pollLast(); // returns the largest/highest scaledown
		
		this.algorithm = algorithm;
		this.setRng(new Random());
//		this.scaledown = scaledown;
		this.dimension = dimension;
		this.revisiting = revisiting;
		this.sampler = as;
		this.walkerController = awc;
		this.startNodeSelector = asns;
		this.networkSample = ns;

	}

	/**
	 * @param scaledown2
	 * @return
	 */
	private TreeSet<Double> checkAndAddScaledowns(double[] scaledown2) {
		TreeSet<Double> sizes = new TreeSet<Double>();
		
		for(double d : scaledown2){
			if(d > 0d && d <= 1d)
				sizes.add(d);
		}
		
		if(sizes.size() > 0)
			return sizes;
		
		throw new IllegalArgumentException("At least one scaledown has to be in [0,1] but all are: " + Arrays.toString(scaledown2));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		if (!initialized()) {
			return g;
		} else {
			this.setGraph(g);
			
			networkSample.initialize(g.getNodeCount());

			// run Sampling Loop
			sampleGraph(g);

			addSampleGraphProperty(g, networkSample);

			networkSample = networkSample.cleanInstance();
			this.setGraph(null);
		
			return g;
		}	
	}

	private void addSampleGraphProperty(Graph g, INetworkSample ns) {
		int[] sn = collectStartNodeIndices();
		Sample s = new Sample(ns, sn, rng);
		g.addProperty(g.getNextKey("SAMPLE"), s);
	}

	

	/**
	 * @return
	 */
	private int[] collectStartNodeIndices() {
		int[] sn = new int[startNodes.length];
		for (int i = 0; i < sn.length; i++) {
			sn[i] = startNodes[i].getIndex();
		}
		return sn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		if (!initialized()) {
			System.out.println("Sampling is not initialized:");
			if (dimension <= 0)
				System.out.println("> Dimension is < 0");
			if (networkSample == null)
				System.out.println("> Network Sample is not initialized");
			if (walkerController == null || !walkerController.isInitialized())
				System.out.println("> Walker Controller is not initilized");
			if (sampler == null || !sampler.isInitialized())
				System.out.println("> Sampler is not initialized");
			if (startNodeSelector == null)
				System.out.println("> Startnode selector is not initialized");

			return false;
		}
		return true;
	}

	/**
	 * checks the initialization of the sampling controller
	 * 
	 * @return true if ok, else false
	 */
	private boolean initialized() {
		if (dimension <= 0 || networkSample == null || walkerController == null
				|| !walkerController.isInitialized() || sampler == null
				|| !sampler.isInitialized() || startNodeSelector == null
				|| intermediateSizes == null || intermediateSizes.size()==0) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * Calculates the sample size
	 * 
	 * @param g
	 *            Graph
	 * 
	 * @return number of nodes in the sampled graph
	 */
	public int calculateTargetedSampleSize(Graph g) {

		int originalSize = g.getNodeCount();

		return (int) Math.ceil(originalSize * scaledown);
	}

	/**
	 * Start the sampling algorithm on the given graph
	 * 
	 * @param g
	 *            original graph
	 * @return true if ok
	 */
	public boolean sampleGraph(Graph g) {
		startNodes = startNodeSelector.selectStartNodes(g, dimension, rng);

		int targetSampleSize = (int) Math.ceil(g.getNodeCount() * scaledown);
		int maxNodesInThisRound = calculateResidualBudget(targetSampleSize);
		round = 0; // TODO make local variable again
		walkerController.initialize(startNodes); // place walker(s) on start
		// node(s)
		Collection<Node> s = sampler.initialize(g, targetSampleSize, round); // initialize
																				// Sampler


		boolean running = true;
		// walk -> sample loop as long new nodes are sampled
		do {
			sampleOneStep(g, maxNodesInThisRound, round);
			// walk
			walkerController.walkOneStep();

			// calculate residual budget
			maxNodesInThisRound = calculateResidualBudget(targetSampleSize);

			// TODO
			if (intermediateSizes.size() > 0 && intermediateSize()) 
				createIntermediateSample(g);
			
			
			// next round necessary?
			running = (maxNodesInThisRound > 0) ? true : false;
			round++;
		} while (running);
		
		return true;
	}

	

	/**
	 * @param graph2 
	 * 
	 */
	private void createIntermediateSample(Graph graph2) {
		double intermediateScaleDown = getSmallesSamplingSize();
		
		int intermediateSampleNodeCount = (int) Math.floor(graph2.getNodeCount()*intermediateScaleDown);
		
		INetworkSample ins;
		if(networkSample instanceof NetworkSampleFast)
			ins = new NetworkSampleFast(this.algorithm, intermediateScaleDown, this.dimension, this.revisiting);
		else
			ins = new NetworkSampleFull(this.algorithm, intermediateScaleDown, this.dimension, this.revisiting);
		
		HashMap<Integer, Integer> snm = networkSample.getSampleNodeMapping();
		HashMap<Integer, List<Integer>> rf = networkSample.getRevisitFrequency();
		
		// only possible with multidimensional sampling algorithms
		// extremely expensive
		while(snm.size() > intermediateSampleNodeCount){
			removeLastSampledNode(snm, rf);
		}
		
		// TODO
		ins.setRevisitFrequency(rf);
		ins.setSampleNodeMapping(snm);
		
		addSampleGraphProperty(graph2, ins);
		
		intermediateSizes.remove(intermediateScaleDown);
		
		return;
	}

	/**
	 * @param snm
	 * @param rf
	 */
	private void removeLastSampledNode(HashMap<Integer, Integer> snm,
			HashMap<Integer, List<Integer>> rf) {
		
		// TODO efficient implementation?
		int last = 0;
		int oldIndex = 0;
		for(java.util.Map.Entry<Integer, Integer> e : snm.entrySet()){
			if(e.getValue() > last){
				last = e.getValue();
				oldIndex = e.getKey();
			}		
		}
		
		snm.remove(oldIndex);
		rf.remove(oldIndex);		
	}

	/**
	 * @return
	 */
	private boolean intermediateSize() {
		if(networkSample.getSampleSize() >= getSmallesSamplingSize()*this.graph.getNodeCount())
			return true;
		
		return false;
	}

	/**
	 * @return
	 */
	private Double getSmallesSamplingSize() {
		
		if(intermediateSizes.size() > 0)
			return intermediateSizes.first();
		
		return 0d;
	}

	/**
	 * Sample eventually nodes in the specified round
	 * 
	 * @param maxNodesInThisRound
	 *            number of maximal added nodes
	 * @param round
	 *            current round
	 * @return true if at least one node is sampled, else false
	 */
	private boolean sampleOneStep(Graph g, int maxNodesInThisRound, int round) {
		
		Collection<Node> chosenNodes = sampler.sampleOneStep(
				maxNodesInThisRound, round);
		
		
		if (chosenNodes.size() > 0){
			Timer tAddToSample = new Timer();
			boolean added = networkSample.addNodeToSample(chosenNodes, round);
			tAddToSample.end();
			runtimeAddingNode += tAddToSample.getMsec();
			return added;
		}
		
		return false;
	}

	/**
	 * Calculates the residual sampling budget based on the targeted size and
	 * the current size of the sample
	 * 
	 * @param targetSampleSize
	 *            target size
	 * @return residual Budget
	 */
	private int calculateResidualBudget(int targetSampleSize) {
		int currentSampleSize = networkSample.getSampleSize();
		int residualBudget = targetSampleSize - currentSampleSize;
		if (residualBudget > 0) {
			return residualBudget;
		} else {
			return 0;
		}
	}

	/**
	 * @return the walkerController
	 */
	public AWalkerController getWalkerController() {
		return this.walkerController;
	}

	/**
	 * @param walkerController
	 *            the walkerController to set
	 */
	public void setWalkerController(AWalkerController walkerController) {
		this.walkerController = walkerController;
	}

	/**
	 * @return the sampler
	 */
	public ASampler getSampler() {
		return this.sampler;
	}

	/**
	 * @param sampler
	 *            the sampler to set
	 */
	public void setSampler(ASampler sampler) {
		this.sampler = sampler;
	}

	/**
	 * @return the startNodeSelector
	 */
	public StartNodeSelector getStartNodeSelector() {
		return this.startNodeSelector;
	}

	/**
	 * @param startNodeSelector
	 *            the startNodeSelector to set
	 */
	public void setStartNodeSelector(StartNodeSelector startNodeSelector) {
		this.startNodeSelector = startNodeSelector;
	}

	/**
	 * @return the networkSample
	 */
	public INetworkSample getNetworkSample() {
		return this.networkSample;
	}

	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @param graph
	 *            the graph to set
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * @param networkSample
	 *            the networkSample to set
	 */
	public void setNetworkSample(INetworkSample networkSample) {
		this.networkSample = networkSample;
	}

	/**
	 * @return the rng
	 */
	public Random getRng() {
		return rng;
	}

	/**
	 * @param rng
	 *            the rng to set
	 */
	public void setRng(Random rng) {
		this.rng = rng;
	}

}

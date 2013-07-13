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

import java.util.Collection;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
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
    private NetworkSample networkSample;

    private int dimension;
    private double scaledown;
    @SuppressWarnings({ "unused" })
    private boolean revisiting;

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
	    ASampler as, StartNodeSelector asns, double scaledown,
	    int dimension, boolean revisiting) {
	super("SAMPLING", new Parameter[] {
		new StringParameter("ALGORITHM", algorithm), awc, as, asns,
		new DoubleParameter("SCALEDOWN", scaledown),
		new IntParameter("DIMENSIONS", dimension),
		new BooleanParameter("REVISITING", revisiting) });

	// we must have at least 1 walker
	if (dimension < 1) {
	    throw new IllegalArgumentException(
		    "Dimension has to be at least 1 but is: " + dimension);
	}

	// a scaledown has to be a float between 0.0 and 1.0 representing the
	// percentage of the original size
	if (scaledown < 0.0 || scaledown > 1.0) {
	    throw new IllegalArgumentException(
		    "Scaledown has to be in [0,1] but is: " + scaledown);
	}

	this.scaledown = scaledown;
	this.dimension = dimension;
	this.revisiting = revisiting;
	this.sampler = as;
	this.walkerController = awc;
	this.startNodeSelector = asns;
	this.networkSample = new NetworkSample();

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
	    sampleGraph(g);
	    // TEST
	    System.out.println("\n> Sampled " + networkSample.getSampleSize()
		    + " out of " + g.getNodeCount() + " nodes.");
	    return g;
	}
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
		|| !sampler.isInitialized() || startNodeSelector == null) {
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
	Node[] startNodes = startNodeSelector.selectStartNodes(g, dimension);

	int targetSampleSize = (int) Math.ceil(g.getNodeCount() * scaledown);
	int maxNodesInThisRound = calculateResidualBudget(targetSampleSize);
	int round = 0;
	walkerController.initialize(g, startNodes); // place walker(s) on start
						    // node(s)
	sampler.initialize(g, networkSample, targetSampleSize); // initialize
								// Sampler
	walkerController.setGraph(g);

	// eventually sample startnodes
	sampleOneStep(g, maxNodesInThisRound, round);

	boolean running = true;
	// walk -> sample loop as long new nodes are sampled
	do {
	    round++;
	    // walk
	    walkerController.walkOneStep(g, networkSample);
	    // eventually sample
	    sampleOneStep(g, maxNodesInThisRound, round);

	    // calculate residual budget
	    maxNodesInThisRound = calculateResidualBudget(targetSampleSize);
	    running = (maxNodesInThisRound > 0) ? true : false;
	} while (running);

	return true;
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
	Collection<Node> chosenNodes = sampler.sampleOneStepNodes(g,
		networkSample, maxNodesInThisRound);
	if (chosenNodes.size() > 0)
	    return networkSample.addNodeToSample(chosenNodes, round);

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
    public NetworkSample getNetworkSample() {
	return this.networkSample;
    }

    /**
     * @param networkSample
     *            the networkSample to set
     */
    public void setNetworkSample(NetworkSample networkSample) {
	this.networkSample = networkSample;
    }

}

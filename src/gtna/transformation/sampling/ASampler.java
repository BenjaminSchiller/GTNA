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
import java.util.List;
import java.util.Map;
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;

/**
 * @author Tim
 * 
 */
public abstract class ASampler extends Parameter {

	private AWalkerController walkerController;
	private Graph graph;
	private SamplingController samplingController;
	private double runtime = 0; // TODO REMOVE


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
	 * This default implementation calls sampleOneStepNodes to sample the
	 * startnode(s)!
	 * 
	 * This default implementation calls setGraph, you don't have to take care
	 * about setting the graph after calling this method.
	 * 
	 * @param targetSampleSize
	 *            max nodes sampled in this round
	 * @param walkerController
	 * 
	 */
	public Collection<Node> initialize(Graph g, int maxNodes, int round) {
		this.setGraph(g);
		return sampleOneStep(maxNodes, round);
	}

	/**
	 * Set the used walker controller instance
	 * 
	 * @param awc
	 */
	public void setWalkerController(AWalkerController awc) {
		this.walkerController = awc;
	}

	/**
	 * Checks the initialization of the sampler instance
	 * 
	 * @return true if ok, else false
	 */
	public boolean isInitialized() {
		if (walkerController == null) {
			return false;
		}

		return true;
	}

	/**
	 * Sample nodes of the current step
	 * 
	 * @param g
	 *            graph
	 * @param ns
	 *            current network sample
	 * @param maxNodes
	 *            sample max maxNodes nodes in this step
	 * @return
	 */
	public Collection<Node> sampleOneStep(int maxNodes, int round) {

		Timer t = new Timer(); // TODO

		Collection<AWalker> walkers = walkerController.getActiveWalkers();
		LinkedList<Node> sampled = new LinkedList<Node>();

		for (AWalker w : walkers) {
			Map<Node, List<Node>> wcc = w.getCurrentCandidates();
			Map<Node, List<Node>> fc = walkerController
					.filterCandidates(wcc);
			sampled.addAll(sampleNodes(fc, round));
		}

		if (sampled.size() > maxNodes) {
			Random r = samplingController.getRng();
			while (sampled.size() > maxNodes) {
				int i = r.nextInt(sampled.size() - 1);
				sampled.remove(i);
			}
		}
		// TODO
		t.end();
		runtime += t.getMsec();

		// System.out.println("Sampling took " + runtime + " ms (" + t.getMsec()
		// + ") in " + calls + " calls");

		return sampled;

	}

	/**
	 * Select the sampled node out of the filtered candidates
	 * 
	 * @param filteredCandidates
	 *            candidate nodes
	 * @return collection of selected nodes
	 */
	protected abstract List<Node> sampleNodes(
			Map<Node, List<Node>> filteredCandidates, int round);

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
	 * @param sc
	 */
	public void setSamplingController(SamplingController sc) {
		this.samplingController = sc;

	}

	/**
	 * 
	 */
	public void printRuntimes() {
		System.out.println("Collecting nodes took: " + runtime + "ms");

	}

}

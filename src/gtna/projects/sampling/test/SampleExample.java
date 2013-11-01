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
 * SampleExample.java
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
package gtna.projects.sampling.test;

import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.CondonAndKarp;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.Regular;
import gtna.networks.model.WattsStrogatz;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ColorSampledSubgraph;
import gtna.transformation.sampling.subgraph.ColoredHeatmapSampledSubgraph;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;
import gtna.util.Config;

/**
 * @author Tim
 * 
 */
public class SampleExample {

	/**
	 * 
	 */
	public SampleExample() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		boolean get = false; // get or generate
		int times = 1; // how many generations?

		// Sampling parameter
		double scaledown = 0.5;
		int dimension = 1;
		boolean revisiting = false;
		Long rngSeed = null;

		String folder = "./plots/network-plot/";

		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new ShortestPaths(), new ClusteringCoefficient() };

		Transformation[] sampling = instantiateSamplingTransformation(
				scaledown, dimension, revisiting, rngSeed);

		Network[] networks = instantiateNetworkModels();

		SamplingWorkflow
				.runWorkflow(times, metrics, sampling, networks, folder);

	}

	private static Transformation[] instantiateSamplingTransformation(
			double scaledown, int dimension, boolean revisiting, Long randomSeed) {
		Transformation ss = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.SNOWBALLSAMPLING, scaledown, revisiting,
				dimension, randomSeed);
		Transformation rs = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMSTROLL, scaledown, revisiting,
				dimension, randomSeed);
		Transformation rj = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMJUMP, scaledown, revisiting, dimension,
				randomSeed);
		Transformation mrw = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMWALK_MULTIPLE, scaledown, revisiting,
				dimension, randomSeed);
		Transformation rsdc = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMSTROLL_DEGREECORRECTION, scaledown,
				revisiting, dimension, randomSeed);
		Transformation us = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.UNIFORMSAMPLING, scaledown, revisiting,
				dimension, randomSeed);
		Transformation bfs = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.BFS, scaledown, revisiting, dimension,
				randomSeed);
		Transformation rds = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RESPONDENTDRIVENSAMPLING, scaledown,
				revisiting, dimension, randomSeed);
		Transformation ff = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.FORESTFIRE, scaledown, revisiting, dimension,
				randomSeed);
		Transformation dfs = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.DFS, scaledown, revisiting, dimension,
				randomSeed);
		Transformation rwm = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMWALK_METROPOLIZED, scaledown,
				revisiting, dimension, randomSeed);
		Transformation rw = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMWALK, scaledown, revisiting, dimension,
				randomSeed);

		// Transformation subgraphing = new ExtractSampledSubgraph();
		// Transformation subgraphing = new ColorSampledSubgraph();
		Transformation subgraphing = new ColoredHeatmapSampledSubgraph();

		Transformation[] t1 = new Transformation[] { rw, subgraphing };
		return t1;
	}

	public static Network[] instantiateNetworkModels() {
		Network nw1 = new ErdosRenyi(100, 3, false, null);
		Network nw2 = new BarabasiAlbert(500, 2, null);
		Network nw3 = new WattsStrogatz(500, 6, 0.2, null);
		Network nw4 = new CondonAndKarp(500, 3, 0.05, 0.0005, null);
		Network nw5 = new Regular(100, 2, true, false, null);

		// Network[] n = new Network[] { nw1, nw2, nw3, nw4, nw5 };

		// Network[] n = new Network[] { nw2, nw3, nw4, nw5 };

		Network[] n = new Network[] { nw5 };
		return n;
	}

}

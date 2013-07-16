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
 * SamplingDriver.java
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
package gtna;

import java.util.ArrayList;
import java.util.Collection;

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.CondonAndKarp;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.WattsStrogatz;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.util.Config;

/**
 * @author Tim
 * 
 */
public class SamplingDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		boolean get = false; // get or generate
		int times = 1; // how many generations?

		// Sampling parameter
<<<<<<< HEAD
<<<<<<< HEAD
		double scaledown = 0.1;
		int dimension = 1;
=======
		double scaledown = 0.25;
<<<<<<< HEAD
		int dimension = 5;
>>>>>>> MultipleRandomWalk Walker Controller and added entry for the samplingalgorithmfactory
=======
		int dimension = 1;
<<<<<<< HEAD
>>>>>>> Implementation of sample property -> not persisted?!
		boolean revisiting = false;
=======
		boolean revisiting = true;
<<<<<<< HEAD
>>>>>>> - write property ;-seperated like the other properties use ; too

=======
		Long seed = new Long(0);
		
>>>>>>> usage, persisting, loading of the deterministic-rng possible
		String folder = "./plots/network-plot/";

		Transformation[] t1 = instantiateSamplingTransformation(scaledown,
				dimension, revisiting, seed);
=======
		double scaledown = 0.25;
		int dimension = 1;
		boolean revisiting = false;

		String folder = "./plots/network-plot/";

		Transformation[] t1 = instantiateSamplingTransformation(scaledown,
				dimension, revisiting);
>>>>>>> Fixing for BUG: writing a network to hdd before calculating the series

		Network[] n = instantiateNetworkModels();

		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new ShortestPaths(), new ClusteringCoefficient() };

<<<<<<< HEAD
		Collection<String> networks = persistNetworks(times, n, folder, t1[0]);

		Collection<Network> nets = loadNetworks(folder, t1, networks);
		
=======
		Collection<String> networks = persistNetworks(times, n, folder);

		Collection<Network> nets = loadNetworks(folder, t1, networks);

>>>>>>> Fixing for BUG: writing a network to hdd before calculating the series
		plotNetworkMetrics(get, times, metrics, nets);

	}

	private static Collection<Network> loadNetworks(String folder,
			Transformation[] t1, Collection<String> networks) {
		Collection<Network> nets = new ArrayList<Network>();

		for (String i : networks) {
			Network ni = new ReadableFile(i, folder, i, t1);
			nets.add(ni);
		}
		return nets;
	}

	private static void plotNetworkMetrics(boolean get, int times,
			Metric[] metrics, Collection<Network> nets) {
		Series[] s = get ? Series.get(nets.toArray(new Network[0]), metrics)
				: Series.generate(nets.toArray(new Network[0]), metrics, times);

		Plotting.single(s, metrics, "example-s/");
		Plotting.multi(s, metrics, "example-m/");
	}

	private static Collection<String> persistNetworks(int times, Network[] n,
<<<<<<< HEAD
			String folder, Transformation sample) {
=======
			String folder) {
>>>>>>> Fixing for BUG: writing a network to hdd before calculating the series
		Collection<String> networks = new ArrayList<String>();
		for (Network i : n) {
			String p = folder + "n-" + i.getKey() + "-" + i.getNodes();
			System.out.println("Plotting network - " + i.getKey() + " @ "
					+ i.getNodes() + " nodes");
<<<<<<< HEAD
			plot(i.generate(), p, times, sample);
=======
			plot(i.generate(), p, times);
>>>>>>> Fixing for BUG: writing a network to hdd before calculating the series
			networks.add(p);
		}
		return networks;
	}

	private static Network[] instantiateNetworkModels() {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		Network nw1 = new ErdosRenyi(100, 12, false, null); 
		Network nw2 = new BarabasiAlbert(1000, 10, null);
		Network nw3 = new WattsStrogatz(1000, 6, 0.2, null);
		Network nw4 = new CondonAndKarp(500, 4, 0.4, 0.05, null);
=======
		Network nw1 = new ErdosRenyi(10000, 12, false, null); 
=======
		Network nw1 = new ErdosRenyi(100, 3, false, null); 
>>>>>>> Implementation of sample property -> not persisted?!
=======
		Network nw1 = new ErdosRenyi(10000, 3, false, null); 
>>>>>>> - write property ;-seperated like the other properties use ; too
		Network nw2 = new BarabasiAlbert(2500, 10, null);
		Network nw3 = new WattsStrogatz(5000, 6, 0.2, null);
		Network nw4 = new CondonAndKarp(750, 4, 0.4, 0.05, null);
>>>>>>> MultipleRandomWalk Walker Controller and added entry for the samplingalgorithmfactory

//		Network[] n = new Network[] { nw3/*nw1, nw2, nw3, nw4*/ };
		
<<<<<<< HEAD
		Network[] n = new Network[] { nw2 };
=======
		Network[] n = new Network[] { nw1 };
>>>>>>> added entry for random jump algorithm to the sampling algorithm factory
=======
		Network nw1 = new ErdosRenyi(1000, 12, false, null); 
		Network nw2 = new BarabasiAlbert(1000, 10, null);
		Network nw3 = new WattsStrogatz(1000, 6, 0.2, null);
		Network nw4 = new CondonAndKarp(500, 4, 0.4, 0.05, null);

//		Network[] n = new Network[] { nw3/*nw1, nw2, nw3, nw4*/ };
		
		Network[] n = new Network[] { nw4 };
>>>>>>> Fixing for BUG: writing a network to hdd before calculating the series
		return n;
	}

	private static Transformation[] instantiateSamplingTransformation(
<<<<<<< HEAD
<<<<<<< HEAD
			double scaledown, int dimension, boolean revisiting) {
<<<<<<< HEAD
		Transformation uniformSampling1 = SamplingAlgorithmFactory
				.getInstanceOf(SamplingAlgorithm.RANDOMWALK_METROPOLIZED, scaledown,
=======
=======
			double scaledown, int dimension, boolean revisiting, Long randomSeed) {
>>>>>>> usage, persisting, loading of the deterministic-rng possible
		Transformation sampling = SamplingAlgorithmFactory
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
				.getInstanceOf(SamplingAlgorithm.RANDOMWALK_MULTIPLE, scaledown,
>>>>>>> MultipleRandomWalk Walker Controller and added entry for the samplingalgorithmfactory
=======
				.getInstanceOf(SamplingAlgorithm.RANDOMSTROLL, scaledown,
>>>>>>> testing random stroll
=======
				.getInstanceOf(SamplingAlgorithm.RANDOMSTROLL_DEGREECORRECTION, scaledown,
>>>>>>> build wiring of components for RANDOM_STROLL_WITH_DEGREE_CORRECTION
=======
				.getInstanceOf(SamplingAlgorithm.RANDOMJUMP, scaledown,
>>>>>>> added entry for random jump algorithm to the sampling algorithm factory
=======
				.getInstanceOf(SamplingAlgorithm.RANDOMWALK, scaledown,
<<<<<<< HEAD
>>>>>>> Implementation of sample property -> not persisted?!
						revisiting, dimension);
=======
=======
				.getInstanceOf(SamplingAlgorithm.RANDOMSTROLL, scaledown,
>>>>>>> Usage of the deterministic-rng
						revisiting, dimension, randomSeed);
>>>>>>> usage, persisting, loading of the deterministic-rng possible

		Transformation[] t1 = new Transformation[] { sampling };
=======
			double scaledown, int dimension, boolean revisiting) {
		Transformation uniformSampling1 = SamplingAlgorithmFactory
				.getInstanceOf(SamplingAlgorithm.RANDOMWALK, scaledown,
						revisiting, dimension);

		Transformation[] t1 = new Transformation[] { uniformSampling1 };
>>>>>>> Fixing for BUG: writing a network to hdd before calculating the series
		return t1;
	}

	@SuppressWarnings("javadoc")
<<<<<<< HEAD
	public static void plot(Graph g, String filename, int times, Transformation sample) {
=======
	public static void plot(Graph g, String filename, int times) {
>>>>>>> Fixing for BUG: writing a network to hdd before calculating the series
		Transformation tCRIdS = new ConsecutiveRingIDSpace(true);

		for (int i = 0; i < times; i++) {
			Gephi gephi = new Gephi();
			Config.overwrite("GEPHI_RING_RADIUS", "1");
			Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.01");
			Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
			Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "true");
			Config.overwrite("GEPHI_NODE_SIZE", "0.1");

			String graphFilename = filename;

			g = tCRIdS.transform(g);
<<<<<<< HEAD
			g = sample.transform(g);
=======
>>>>>>> Fixing for BUG: writing a network to hdd before calculating the series

			IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");

			new GtnaGraphWriter().writeWithProperties(g, graphFilename);

			gephi.plot(g, ids, graphFilename + ".pdf");

			System.out.println(filename);
		}
	}

}
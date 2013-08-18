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
 * SamplingWorkflow.java
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

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
=======
=======
import gtna.id.plane.PlaneIdentifierSpaceSimple;
>>>>>>> fixed bfs
import gtna.io.graphReader.GraphReader;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;
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
>>>>>>> Workflow definition and implementation
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.util.Config;

import java.util.ArrayList;
=======
=======
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
>>>>>>> fixed bfs
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ColorSampledSubgraph;
import gtna.transformation.sampling.subgraph.ColoredHeatmapSampledSubgraph;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;
import gtna.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
>>>>>>> Workflow definition and implementation
import java.util.Collection;

/**
 * @author Tim
 * 
 */
public class SamplingWorkflow {

<<<<<<< HEAD
<<<<<<< HEAD
	

	/**
	 * @param times
	 * @param scaledown
	 * @param dimension
	 * @param revisiting
	 * @param rngSeed
	 * @param metrics
	 * @param folder
	 */
	public static void runWorkflow(int times, Metric[] metrics,
			Transformation[] t, Network[] networks, String folder) {

		String[] networkPaths = persistNetworks(times, networks, folder);

		Graph[] g = loadNetworksAndApplyTransformations(folder, t, networkPaths);

		Collection<String> graphPaths = new ArrayList<String>();
		for (Graph gi : g) {
			String pg = persistGraphs(gi, "", gi.getName());
			graphPaths.add(pg);
		}

		loadNetworksAndCalculateMetrics(folder, graphPaths, metrics);
	}

	public static void runFastWorkflow(int times, Metric[] metrics,
			Transformation[] t, Network[] networks, String folder) {

		Graph[] g = applyTransformations(networks, t);

		Collection<String> graphPaths = new ArrayList<String>();
		for (Graph gi : g) {
			String pg = persistGraphs(gi, "", gi.getName());
			graphPaths.add(pg);
		}

		loadNetworksAndCalculateMetrics(folder, graphPaths, metrics);
	}

	/**
	 * @param folder
	 * @param t1
	 * @param networkPaths
	 * @return
	 */
	private static Graph[] loadNetworksAndApplyTransformations(String folder,
			Transformation[] t1, String[] networkPaths) {
		Network[] nets = loadNetworks(folder, networkPaths);

		Graph[] g = applyTransformations(nets, t1);

		return g;
	}

	/**
	 * @param times
	 * @param folder
	 * @param graphPaths
	 * @param metrics
	 */
	private static void loadNetworksAndCalculateMetrics(String folder,
			Collection<String> graphPaths, Metric[] metrics) {
		Network[] sampledNets = loadNetworks(folder,
				graphPaths.toArray(new String[0]));

		// get = false as the network.generate method is reading only the
		// persisted graph
		calculateNetworkMetricsAndWriteToFile(false, 1, metrics, sampledNets,
				"example");
	}

	public static Graph[] applyTransformations(Network[] networks,
			Transformation[] samplingTransformation) {
		Collection<Graph> sampledNetworks = new ArrayList<Graph>();

		for (Network n : networks) {
			Graph g = n.generate();

			for (Transformation t : samplingTransformation) {
				if (t.applicable(g)) {
					g = t.transform(g);
				} else {
					System.err.println("Transformation not applicable: "
							+ t.getDescription() + " is not applicable to "
							+ g.getName());
				}
			}

			sampledNetworks.add(g);
		}

		return sampledNetworks.toArray(new Graph[0]);
	}

	

	public static Network[] loadNetworks(String folder, String[] networkPaths) {
		Collection<Network> nets = new ArrayList<Network>();

		for (String i : networkPaths) {
			Network ni = new ReadableFile(i, folder, i, null);
			nets.add(ni);
		}
		return nets.toArray(new Network[0]);
	}

	public static String[] persistNetworks(int times, Network[] n, String folder) {
		Collection<String> networks = new ArrayList<String>();
		for (Network i : n) {
			String filename = i.getKey() + "-" + i.getNodes();
			String p = persistGraphs(i.generate(), folder, filename);
			networks.add(p);
		}
		return networks.toArray(new String[0]);
	}

	/**
	 * @param g
	 *            - graph to be persisted
	 * @param folder
	 *            - basepath for persisting the graphs
	 * @param filename
	 *            - name for the graphfile
	 * @return paths to the persisted graph
	 */
	private static String persistGraphs(Graph g, String folder, String filename) {
		return writePlotAndGraphToFile(g, folder + filename);

	}

	@SuppressWarnings("javadoc")
	public static String writePlotAndGraphToFile(Graph g, String filename) {

		Collection<String> graphs = new ArrayList<String>();

		Gephi gephi = new Gephi();
		setGephiConfiguration();

		String graphFilename = filename;

		g = setIdSpace(g);

		IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");

		new GtnaGraphWriter().writeWithProperties(g, graphFilename);

		gephi.plot(g, ids, graphFilename + ".pdf");

		return graphFilename;

	}

	/**
	 * @param g
	 * @return
	 */
	private static Graph setIdSpace(Graph g) {
		if (!g.hasProperty("ID_SPACE_0")) {
			Transformation idS = new ConsecutiveRingIDSpace(true);
//			 Transformation idS = new RandomPlaneIDSpaceSimple(2, 5, 5, false);
			g = idS.transform(g);
		}
		return g;
	}

	

	public static void calculateNetworkMetricsAndWriteToFile(
			boolean getOrGenerate, int times, Metric[] metrics, Network[] nets,
			String path) {
		Series[] s = getOrGenerate ? Series.get(nets, metrics) : Series
				.generate(nets, metrics, times);

		Plotting.single(s, metrics, path + "-s/");
		Plotting.multi(s, metrics, path + "-m/");
	}

	/**
     * 
     */
	private static void setGephiConfiguration() {
		Config.overwrite("GEPHI_RING_RADIUS", "1");
		Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.001");
		Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
		Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "true");
		Config.overwrite("GEPHI_NODE_SIZE", "0.01");
	}
=======
    /**
     * @param args
     */
    public static void main(String[] args) {
	Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

	boolean get = false; // get or generate
	int times = 1; // how many generations?

	// Sampling parameter
	double scaledown = 0.25;
	int dimension = 1;
	boolean revisiting = false;
	Long rngSeed = null;

	String folder = "./plots/network-plot/";
	
	Metric[] metrics = new Metric[] { new DegreeDistribution(),
		new ShortestPaths(), new ClusteringCoefficient() };
	
	Transformation[] sampling = instantiateSamplingTransformation(scaledown,
		dimension, revisiting, rngSeed);
	
	Network[] networks = instantiateNetworkModels();

	runWorkflow(times, metrics, sampling, networks, folder);

    }

    /**
     * @param times
     * @param scaledown
     * @param dimension
     * @param revisiting
     * @param rngSeed
     * @param metrics
     * @param folder
     */
    public static void runWorkflow(int times, Metric[] metrics, Transformation[] t, Network[] networks, String folder) {
	
	String[] networkPaths = persistNetworks(times, networks, folder);

	Graph[] g = loadNetworksAndApplyTransformations(
		folder, t, networkPaths);
	
	Collection<String> graphPaths = new ArrayList<String>();
	for (Graph gi : g) {
	    String pg = persistGraphs(gi, "", gi.getName());
	    graphPaths.add(pg);
=======
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		boolean get = false; // get or generate
		int times = 1; // how many generations?

		// Sampling parameter
		double scaledown = 0.25;
		int dimension = 2;
		boolean revisiting = false;
		Long rngSeed = new Long(0);

		String folder = "./plots/network-plot/";

		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new ShortestPaths(), new ClusteringCoefficient() };

		Transformation[] sampling = instantiateSamplingTransformation(
				scaledown, dimension, revisiting, rngSeed);

		Network[] networks = instantiateNetworkModels();

		runWorkflow(times, metrics, sampling, networks, folder);

>>>>>>> added SnowballSampling to SamplingAlgorithmFactory
	}

	/**
	 * @param times
	 * @param scaledown
	 * @param dimension
	 * @param revisiting
	 * @param rngSeed
	 * @param metrics
	 * @param folder
	 */
	public static void runWorkflow(int times, Metric[] metrics,
			Transformation[] t, Network[] networks, String folder) {

		String[] networkPaths = persistNetworks(times, networks, folder);

		Graph[] g = loadNetworksAndApplyTransformations(folder, t, networkPaths);

		Collection<String> graphPaths = new ArrayList<String>();
		for (Graph gi : g) {
			String pg = persistGraphs(gi, "", gi.getName());
			graphPaths.add(pg);
		}

		loadNetworksAndCalculateMetrics(folder, graphPaths, metrics);
	}

	public static void runFastWorkflow(int times, Metric[] metrics,
			Transformation[] t, Network[] networks, String folder) {

		Graph[] g = applyTransformations(networks, t);

		Collection<String> graphPaths = new ArrayList<String>();
		for (Graph gi : g) {
			String pg = persistGraphs(gi, "", gi.getName());
			graphPaths.add(pg);
		}

		loadNetworksAndCalculateMetrics(folder, graphPaths, metrics);
	}

	/**
	 * @param folder
	 * @param t1
	 * @param networkPaths
	 * @return
	 */
	private static Graph[] loadNetworksAndApplyTransformations(String folder,
			Transformation[] t1, String[] networkPaths) {
		Network[] nets = loadNetworks(folder, networkPaths);

		Graph[] g = applyTransformations(nets, t1);

		return g;
	}

	/**
	 * @param times
	 * @param folder
	 * @param graphPaths
	 * @param metrics
	 */
	private static void loadNetworksAndCalculateMetrics(String folder,
			Collection<String> graphPaths, Metric[] metrics) {
		Network[] sampledNets = loadNetworks(folder,
				graphPaths.toArray(new String[0]));

		// get = false as the network.generate method is reading only the
		// persisted graph
		calculateNetworkMetricsAndWriteToFile(false, 1, metrics, sampledNets,
				"example");
	}

	public static Graph[] applyTransformations(Network[] networks,
			Transformation[] samplingTransformation) {
		Collection<Graph> sampledNetworks = new ArrayList<Graph>();

		for (Network n : networks) {
			Graph g = n.generate();

			for (Transformation t : samplingTransformation) {
				if (t.applicable(g)) {
					g = t.transform(g);
				} else {
					System.err.println("Transformation not applicable: "
							+ t.getDescription() + " is not applicable to "
							+ g.getName());
				}
			}

			sampledNetworks.add(g);
		}

		return sampledNetworks.toArray(new Graph[0]);
	}

	private static Transformation[] instantiateSamplingTransformation(
			double scaledown, int dimension, boolean revisiting, Long randomSeed) {
		Transformation sampling = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.SNOWBALLSAMPLING, scaledown, revisiting,
				dimension, randomSeed);
		Transformation sampling2 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMSTROLL, scaledown, revisiting,
				dimension, randomSeed);
		Transformation sampling3 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMJUMP, scaledown, revisiting, dimension,
				randomSeed);
		Transformation sampling4 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMWALK_MULTIPLE, scaledown, revisiting, dimension,
				randomSeed);
		Transformation sampling5 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMSTROLL_DEGREECORRECTION, scaledown,
				revisiting, dimension, randomSeed);
		Transformation sampling6 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.UNIFORMSAMPLING, scaledown, revisiting,
				dimension, randomSeed);
		Transformation sampling7 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.BFS, scaledown, revisiting, dimension,
				randomSeed);
		Transformation sampling8 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RESPONDENTDRIVENSAMPLING, scaledown, revisiting, dimension,
				randomSeed);
		Transformation sampling9 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.FORESTFIRE, scaledown, revisiting, dimension,
				randomSeed);
		Transformation sampling10 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.DFS, scaledown, revisiting, dimension,
				randomSeed);
		Transformation sampling11 = SamplingAlgorithmFactory.getInstanceOf(
				SamplingAlgorithm.RANDOMWALK_METROPOLIZED, scaledown, revisiting, dimension,
				randomSeed);

//		 Transformation subgraphing = new ExtractSampledSubgraph();
		 Transformation subgraphing = new ColorSampledSubgraph();
//		Transformation subgraphing = new ColoredHeatmapSampledSubgraph();

		Transformation[] t1 = new Transformation[] { sampling4, subgraphing };
		return t1;
	}

	public static Network[] loadNetworks(String folder, String[] networkPaths) {
		Collection<Network> nets = new ArrayList<Network>();

		for (String i : networkPaths) {
			Network ni = new ReadableFile(i, folder, i, null);
			nets.add(ni);
		}
		return nets.toArray(new Network[0]);
	}

	public static String[] persistNetworks(int times, Network[] n, String folder) {
		Collection<String> networks = new ArrayList<String>();
		for (Network i : n) {
			String filename = i.getKey() + "-" + i.getNodes();
			String p = persistGraphs(i.generate(), folder, filename);
			networks.add(p);
		}
		return networks.toArray(new String[0]);
	}

	/**
	 * @param g
	 *            - graph to be persisted
	 * @param folder
	 *            - basepath for persisting the graphs
	 * @param filename
	 *            - name for the graphfile
	 * @return paths to the persisted graph
	 */
	private static String persistGraphs(Graph g, String folder, String filename) {
		return writePlotAndGraphToFile(g, folder + filename);

	}

	@SuppressWarnings("javadoc")
	public static String writePlotAndGraphToFile(Graph g, String filename) {

		Collection<String> graphs = new ArrayList<String>();

		Gephi gephi = new Gephi();
		setGephiConfiguration();

		String graphFilename = filename;

		g = setIdSpace(g);

		IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");

		new GtnaGraphWriter().writeWithProperties(g, graphFilename);

		gephi.plot(g, ids, graphFilename + ".pdf");

		return graphFilename;

	}

	/**
	 * @param g
	 * @return
	 */
	private static Graph setIdSpace(Graph g) {
		if (!g.hasProperty("ID_SPACE_0")) {
			Transformation idS = new ConsecutiveRingIDSpace(true);
//			 Transformation idS = new RandomPlaneIDSpaceSimple(2, 5, 5, false);
			g = idS.transform(g);
		}
		return g;
	}

	public static Network[] instantiateNetworkModels() {
		Network nw1 = new ErdosRenyi(100, 3, false, null);
		Network nw2 = new BarabasiAlbert(500, 2, null);
		Network nw3 = new WattsStrogatz(500, 6, 0.2, null);
		Network nw4 = new CondonAndKarp(500, 3, 0.05, 0.0005, null);
		Network nw5 = new Regular(100, 1, true, false, null);

//		 Network[] n = new Network[] { nw1, nw2, nw3, nw4, nw5 };
//		Network[] n = new Network[] { nw2, nw3, nw4, nw5 };

		Network[] n = new Network[] { nw5 };
		return n;
	}

	public static void calculateNetworkMetricsAndWriteToFile(
			boolean getOrGenerate, int times, Metric[] metrics, Network[] nets,
			String path) {
		Series[] s = getOrGenerate ? Series.get(nets, metrics) : Series
				.generate(nets, metrics, times);

		Plotting.single(s, metrics, path + "-s/");
		Plotting.multi(s, metrics, path + "-m/");
	}

	/**
     * 
     */
<<<<<<< HEAD
    private static void setGephiConfiguration() {
	Config.overwrite("GEPHI_RING_RADIUS", "1");
	Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.001");
	Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
	Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "true");
	Config.overwrite("GEPHI_NODE_SIZE", "0.01");
    }
>>>>>>> Workflow definition and implementation
=======
	private static void setGephiConfiguration() {
		Config.overwrite("GEPHI_RING_RADIUS", "1");
		Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.001");
		Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
		Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "true");
		Config.overwrite("GEPHI_NODE_SIZE", "0.01");
	}
>>>>>>> added SnowballSampling to SamplingAlgorithmFactory

}

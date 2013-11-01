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
package gtna.projects.sampling.test;

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.util.Config;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Tim
 * 
 */
public class SamplingWorkflow {

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
			// Transformation idS = new RandomPlaneIDSpaceSimple(2, 5, 5,
			// false);
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

}

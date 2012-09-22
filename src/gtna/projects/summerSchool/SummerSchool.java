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
 * SummerSchool.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.summerSchool;

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.id.IdentifierSpace;
import gtna.io.graphReader.EdgeListGraphReader;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.connectivity.RichClubConnectivity;
import gtna.metrics.connectivity.StrongConnectivity;
import gtna.metrics.connectivity.WeakConnectivity;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data.Type;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.gd.MelanconHerman;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.transformation.spanningtree.BFS;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class SummerSchool {

	static Metric dd = new DegreeDistribution();
	static Metric cc = new ClusteringCoefficient();
	static Metric sp = new ShortestPaths();
	static Metric wc = new WeakConnectivity();
	static Metric sc = new StrongConnectivity();
	static Metric rcc = new RichClubConnectivity();
	static Metric wf_r = new WeakFragmentation(new RandomNodeSorter(),
			Fragmentation.Resolution.PERCENT);
	static Metric wf_d = new WeakFragmentation(new DegreeNodeSorter(
			NodeSorter.NodeSorterMode.DESC), Fragmentation.Resolution.PERCENT);

	static Metric[] metrics = new Metric[] { dd, cc, sp, wc, sc, rcc, wf_r,
			wf_d };

	static String src_g1 = "/Users/benni/TUD/SummerSchool/DC+DA/graphs/g1.txt";
	static String src_g2 = "/Users/benni/TUD/SummerSchool/DC+DA/graphs/g2.txt";
	static String src_g3 = "/Users/benni/TUD/SummerSchool/DC+DA/graphs/g3.txt";

	static String dst_g1 = "/Users/benni/TUD/SummerSchool/DC+DA/graphs/g1.gtna";
	static String dst_g2 = "/Users/benni/TUD/SummerSchool/DC+DA/graphs/g2.gtna";
	static String dst_g3 = "/Users/benni/TUD/SummerSchool/DC+DA/graphs/g3.gtna";

	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/summerSchool/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/summerSchool/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// socialGraphGeneration();
		// socialGraphAnalysis();
		// socialGraphDrawing();
		// er();
		// spi();
		example();
	}

	public static void socialGraphDrawing() {
		Network g1 = new ReadableFile("sssg-1", "Summer School Social Graph 1",
				dst_g1, null);
		Network g2 = new ReadableFile("sssg-2", "Summer School Social Graph 2",
				dst_g2, null);
		Network g3 = new DescriptionWrapper(new ReadableFile("sssg-3",
				"Summer School Social Graph 3", dst_g3,
				new Transformation[] { new Bidirectional() }),
				"Summer School Social Graph 3");

		Transformation t0 = new BFS("rand");
		Transformation t = new gtna.transformation.gd.FruchtermanReingold(2,
				new double[] { 1.0, 1.0 }, false, 1000, null);
		t = new MelanconHerman(1.0, 1.0, null);
		Graph g1_ = t.transform(g1.generate());
		Graph g2_ = t.transform(g2.generate());
		Graph g3_ = t.transform(t0.transform(g3.generate()));
		System.out.println(g3_.getProperties());
		Gephi gephi = new Gephi();
		// gephi.plot(g1_, (IdentifierSpace) g1_.getProperty("ID_SPACE_0"),
		// "./plots/1.pdf");
		// gephi.plot(g2_, (IdentifierSpace) g2_.getProperty("ID_SPACE_0"),
		// "./plots/2.pdf");
		gephi.plot(g3_, (IdentifierSpace) g3_.getProperty("ID_SPACE_0"),
				"./plots/3.pdf");
	}

	public static void socialGraphAnalysis() {
		Config.overwrite("GNUPLOT_LW", "5");
		Config.overwrite("GNUPLOT_CONFIG_1", "set style fill solid 1.0");
		Config.overwrite("GNUPLOT_OFFSET_Y", "0.1");

		boolean get = true;

		Network g1 = new ReadableFile("sssg-1", "Summer School Social Graph 1",
				dst_g1, null);
		Network g2 = new ReadableFile("sssg-2", "Summer School Social Graph 2",
				dst_g2, null);
		Network g3 = new ReadableFile("sssg-3", "Summer School Social Graph 3",
				dst_g3, null);
		Network g2_d = new ReadableFile("sssg-2",
				"Summer School Social Graph 2", dst_g2,
				new Transformation[] { new Bidirectional() });
		Network g3_d = new DescriptionWrapper(new ReadableFile("sssg-3",
				"Summer School Social Graph 3", dst_g3,
				new Transformation[] { new Bidirectional() }),
				"Summer School Social Graph 3");

		// Network[] nw = new Network[] { g1, g2, g3, g2_d, g3_d };
		Network[] nw = new Network[] { g1, g2, g3_d };

		Series[] s = get ? Series.get(nw, metrics) : Series.generate(nw,
				metrics, 1);
		Series s1 = Series.get(g1, metrics);
		Series s2 = Series.get(g2, metrics);
		Series s3 = Series.get(g3_d, metrics);

		Plotting.multi(s, metrics, "social-graph/");
		// Plotting.multi(s1, metrics, "social-graph-1/");
		// Plotting.multi(s2, metrics, "social-graph-2/");
		// Plotting.multi(s3, metrics, "social-graph-3/");
		// Plotting.multi(s, metrics, "social-graph-boxes/", Type.average,
		// Style.boxes);
		// Plotting.multi(s1, metrics, "social-graph-boxes-1/", Type.average,
		// Style.boxes);
		// Plotting.multi(s2, metrics, "social-graph-boxes-2/", Type.average,
		// Style.boxes);
		// Plotting.multi(s3, metrics, "social-graph-boxes-3/", Type.average,
		// Style.boxes);
	}

	public static void socialGraphGeneration() {
		Graph g1 = new EdgeListGraphReader(" ").read(src_g1);
		Graph g2 = new EdgeListGraphReader(" ").read(src_g2);
		Graph g3 = new EdgeListGraphReader(" ").read(src_g3);

		GraphWriter w = new GtnaGraphWriter();

		w.write(g1, dst_g1);
		w.write(g2, dst_g2);
		w.write(g3, dst_g3);
	}

	public static void er() {
		Config.overwrite("GNUPLOT_LW", "2");
		int times = 1;
		Network[] nw = ErdosRenyi.get(10000, new double[] { 5, 10, 15, 20 },
				true, null);
		Series[] s = Series.generate(nw, metrics, times);
		Plotting.multi(s, metrics, "er-multi/");
		Plotting.single(s, metrics, "er-single/");
	}

	public static void spi() {
		Config.overwrite("GNUPLOT_LW", "5");
		// Config.overwrite("GNUPLOT_CONFIG_1", "set logscale x");
		int times = 1;
		Network nw = new ReadableFile("spi", "SPI 2010",
				"resources/spi/_RLN/0_analyze_buddy_2010.csv.gtna", null);
		Series s = Series.get(nw, metrics);
		Plotting.multi(s, metrics, "spi-multi/");
		Plotting.single(s, metrics, "spi-single/");
	}

	public static void example() {
		Config.overwrite("GNUPLOT_LW", "3");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/quick_example/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/quick_example/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "true");

		Metric dd = new DegreeDistribution();
		Metric sp = new ShortestPaths();
		Metric wf_d = new WeakFragmentation(new DegreeNodeSorter(
				NodeSorterMode.DESC), Resolution.PERCENT);
		Metric wf_r = new WeakFragmentation(new RandomNodeSorter(),
				Resolution.PERCENT);
		Metric[] metrics = new Metric[] { dd, sp, wf_d, wf_r };

		int times = 10;
		boolean generate = false;
		double[] d = new double[] { 10, 15, 20, 25, 30, 35, 40, 45, 50 };
		Transformation[] t = new Transformation[] { new LargestWeaklyConnectedComponent() };

		Network[] nw1 = ErdosRenyi.get(1000, d, true, t);
		Network[] nw2 = ErdosRenyi.get(2000, d, true, t);
		Network[] nw3 = ErdosRenyi.get(3000, d, true, t);
		Network[] nw4 = ErdosRenyi.get(4000, d, true, t);
		Network[][] nw = new Network[][] { nw1, nw2, nw3, nw4 };

		Series[][] s = generate ? Series.generate(nw, metrics, times) : Series
				.get(nw, metrics);
		Series[] s1 = Series.get(nw1, metrics);

		Plotting.single(s, metrics, "single/", Type.confidence1,
				Style.candlesticks);
		Plotting.singleBy(s, metrics, "single-edges/", dd, "EDGES");
		Plotting.multi(s1, metrics, "multi/", Type.average, Style.linespoint);
	}
}

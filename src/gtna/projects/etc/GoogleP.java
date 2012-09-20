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
 * GooglePlus.java
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
package gtna.projects.etc;

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.io.Filereader;
import gtna.io.Output;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphWriter.CsvGraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.io.networks.googlePlus.Crawl;
import gtna.io.networks.googlePlus.CrawlDuration;
import gtna.io.networks.googlePlus.FileIndexComparator;
import gtna.io.networks.googlePlus.FileNameFilter;
import gtna.io.networks.googlePlus.GooglePlus;
import gtna.io.networks.googlePlus.GooglePlusReader;
import gtna.io.networks.googlePlus.GraphSizeComparator;
import gtna.io.networks.googlePlus.IdList;
import gtna.io.networks.googlePlus.Mapping;
import gtna.io.networks.googlePlus.Node;
import gtna.io.networks.googlePlus.Statistics;
import gtna.io.networks.googlePlus.Task;
import gtna.io.networks.googlePlus.User;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.partition.StrongConnectivityPartition;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Config;
import gtna.util.Stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author benni
 * 
 */
public class GoogleP {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {
		/**
		 * SETTINGS
		 */
		Stats stats = new Stats();
		String main = "/home/benni/g+/";
		if (!(new File(main)).exists()) {
			main = "/Users/benni/TUD/g+/";
			Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		}
		String data = main + "data/";
		String graphs = main + "graphs/";
		String intersectionCrawled = main + "intersection-crawled/";
		String intersectionSeen = main + "intersection-seen/";
		String intersectionCrawledFull = main + "intersection-crawled-full/";
		String intersectionSeenFull = main + "intersection-seen-full/";
		String consecutiveCrawled = main + "intersection-crawled.txt";
		String consecutiveSeen = main + "intersection-seen.txt";

		boolean generate = true;
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		int mod = 1;
		int offset = 0;
		if (args.length == 2) {
			mod = Integer.parseInt(args[0]);
			offset = Integer.parseInt(args[1]);
		}

		Transformation[] connT = new Transformation[] {
				new WeakConnectivityPartition(),
				new StrongConnectivityPartition() };

		/**
		 * external computation of XX
		 */
		// if (args.length != 3) {
		// System.err.println("expecting 3 parameters...");
		// return;
		// }
		// String graphFolder = args[0];
		// String dataFolder = args[1];
		// String metrics = args[2];
		//
		// // String graphFolder =
		// // "/Users/benni/TUD/GTNA/deploy4/temp/graphs/";
		// // String dataFolder =
		// // "/Users/benni/TUD/GTNA/deploy4/temp/data/g+/dd/";
		// // String metrics = "DD";
		//
		// Config.overwrite("MAIN_DATA_FOLDER", dataFolder);
		// Config.overwrite("METRICS", metrics);
		//
		// while (true) {
		// File[] files = (new File(graphFolder))
		// .listFiles(new FileNameFilter("", "-graph.txt"));
		// boolean foundGraph = false;
		// System.out.println("looking for new graph...");
		// for (File f : files) {
		// String filename = f.getAbsolutePath();
		// int cid = Integer.parseInt(f.getName().split("-")[0]);
		// Network nw = new GooglePlus(filename, cid, null, null);
		// File test = new File(dataFolder + nw.nodes() + "/"
		// + nw.folder());
		// if (!test.exists()) {
		// System.out.println("NEW : " + f.getAbsolutePath());
		// foundGraph = true;
		// Series.generate(nw, 1);
		// break;
		// } else {
		// System.out.println("OLD : " + f.getAbsolutePath());
		// }
		// }
		// if (!foundGraph) {
		// System.out.println("no new graph found, quitting...");
		// return;
		// }
		// }

		/**
		 * external plot of XX
		 */
		// if (args.length != 4) {
		// System.err.println("expecting 4 parameters...");
		// return;
		// }
		// String graphFolder = args[0];
		// String dataFolder = args[1];
		// String plotFolder = args[2];
		// String metrics = args[3];
		//
		// // String graphFolder =
		// // "/Users/benni/TUD/GTNA/deploy4/temp/graphs/";
		// // String dataFolder =
		// // "/Users/benni/TUD/GTNA/deploy4/temp/data/g+/dd/";
		// // String metrics = "DD";
		//
		// Config.overwrite("MAIN_DATA_FOLDER", dataFolder);
		// Config.overwrite("MAIN_PLOT_FOLDER", plotFolder);
		// Config.overwrite("METRICS", metrics);
		//
		// File[] files = (new File(graphFolder)).listFiles(new
		// FileNameFilter("",
		// "-graph.txt"));
		// Arrays.sort(files, new FileIndexComparator("-", 0));
		// ArrayList<Series> list = new ArrayList<Series>();
		// for (File f : files) {
		// String filename = f.getAbsolutePath();
		// int cid = Integer.parseInt(f.getName().split("-")[0]);
		// Network nw = new GooglePlus(filename, cid, null, null);
		// Series s = Series.get(nw);
		// if (s != null && s.graphFolders() != null
		// && s.graphFolders().length != 0) {
		// list.add(s);
		// }
		// }
		// Series[] s = new Series[list.size()];
		// for (int i = 0; i < list.size(); i++) {
		// s[i] = list.get(i);
		// }
		// // Plot.multiAvg(s, "multi/");
		// Plot.singlesAvg(s, "single/");

		// if (true) {
		// double[] ds = new double[] { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50 };
		// ds = new double[] { 1 };
		// for (double d : ds) {
		// Network nw = new Ring(10, null, null);
		// nw = new ErdosRenyi(100, d, false, null, null);
		// // nw = new ReadableFile("G+", "g+",
		// // "/Users/benni/TUD/g+/graphs/1-graph.txt", null, null);
		// Transformation t1 = new RandomRingIDSpaceSimple(1, 1.0, false);
		// Graph g = nw.generate();
		// g = t1.transform(g);
		// RingIdentifierSpaceSimple ids = (RingIdentifierSpaceSimple) g
		// .getProperty("ID_SPACE_0");
		//
		// int counter = 0;
		// for (Partition<Double> p : ids.getPartitions()) {
		// ((RingPartitionSimple) p).getId().setPosition(
		// ((double) counter / ids.getPartitions().length)
		// * ids.getModulus());
		// counter++;
		// }
		//
		// Gephi gephi = new Gephi();
		// Config.overwrite("MAIN_PLOT_FOLDER", "./temp/visualization/");
		// gephi.Plot(g, ids, "plot-" + d + ".svg");
		// }
		// return;
		// }

		// GooglePlus.generateGraphsETC(data, graphs, mod, offset);

		// GooglePlus.computations(data, graphs, mod, offset, generate, "DD",
		// "/home/benni/g+/graphs-data/dd/",
		// "/home/benni/g+/graphs-plots/dd/", null);
		// GooglePlus.computations(data, graphs, mod, offset, generate, "CC",
		// "/home/benni/g+/graphs-data/cc/",
		// "/home/benni/g+/graphs-plots/cc/", null);
		// GooglePlus.computations(data, graphs, mod, offset, generate, "SP",
		// "/home/benni/g+/graphs-data/sp/",
		// "/home/benni/g+/graphs-plots/sp/", null);

		// TODO compute
		// GoogleP.computations(data, graphs, mod, offset, generate,
		// "WEAK_CONNECTIVITY, STRONG_CONNECTIVITY",
		// "/home/benni/g+/graphs-data/conn/",
		// "/home/benni/g+/graphs-plots/conn/", connT);

		// GooglePlus.intersetions(data, graphs, mod, offset,
		// intersectionCrawled,
		// "-crawled.txt", 0);
		// GooglePlus.intersetions(data, graphs, mod, offset,
		// intersectionCrawledFull, "-crawled.txt", 5);
		// GooglePlus.intersetions(data, graphs, mod, offset, intersectionSeen,
		// "-seen.txt", 0);
		// GooglePlus.intersetions(data, graphs, mod, offset,
		// intersectionSeenFull, "-seen.txt", 5);

		// GooglePlus.consecutiveIntersections(graphs, mod, offset,
		// "-crawled.txt", consecutiveCrawled);
		// GooglePlus.consecutiveIntersections(graphs, mod, offset, "-seen.txt",
		// consecutiveSeen);

		/**
		 * Combined plots for all crawls
		 */
		// String d = main + "graphs-data/";
		// String p = main + "graphs-plots/";
		// GoogleP.plotCombined(graphs, "DD", d + "dd/", p + "dd/", null);
		// GoogleP.plotCombined(graphs, "CC", d + "cc/", p + "cc/", null);
		// GoogleP.plotCombined(graphs, "SP", d + "sp/", p + "sp/", null);
		// GoogleP.plotCombined(graphs,
		// "WEAK_CONNECTIVITY, STRONG_CONNECTIVITY",
		// d + "conn/", p + "conn/", connT);

		// int maxOffset = 7;
		// String suffix1 = "-crawled.txt";
		// String folder1 = "/home/benni/g+/statistics-crawled/";
		// String name1 = "crawled";
		// String suffix2 = "-seen.txt";
		// String folder2 = "/home/benni/g+/statistics-seen/";
		// String name2 = "seen";
		// GooglePlus.statistics(graphs, maxOffset, suffix1, folder1, name1);
		// GooglePlus.statistics(graphs, maxOffset, suffix2, folder2, name2);x

		// GoogleP.readDates(main + "crawl-table.txt", "	");

		// IdList idl1 = IdList.read(main + "intersection-crawled/0k.txt", 0);
		// IdList idl2 = IdList.read(main + "intersection-crawled-full/0k.txt",
		// 0);
		// Mapping mappingIntersection = new Mapping(idl1, 0);
		// Mapping mappingIntersectionFull = new Mapping(idl2, 0);
		// GoogleP.generateGraphsForMapping(data, main + "graphs_intersection/",
		// mappingIntersection);
		// GoogleP.generateGraphsForMapping(data, main
		// + "graphs_intersection-full/", mappingIntersectionFull);

		// GoogleP.plotGraphs("/Users/benni/TUD/g+/graphs_intersection/",
		// "/Users/benni/TUD/g+/graphs_intersection-plots/");
		// GoogleP.plotGraphs("/Users/benni/TUD/g+/graphs_intersection-full/",
		// "/Users/benni/TUD/g+/graphs_intersection-full-plots/");

		/**
		 * 1-hop neighborhood of starters
		 */
		// String data1hop = main + "1-hop-data/";
		// String idList1hop = main + "1-hop-ids.txt";
		// String mapping1hop = main + "1-hop-mapping.txt";
		// String graphs1hop = main + "1-hop-graphs/";
		// String graphs1hopCsv = main + "1-hop-graphs-csv/";
		// String plots1hop = main + "1-hop-plots/";
		// String plots1hop2 = main + "1-hop-plots-2/";
		// String plots1hop3 = main + "1-hop-plots-3/";
		// String plots1hop4 = main + "1-hop-plots-4/";
		// String basis1hop = data + "81/33605/";
		// // IdList idl = GoogleP.generateIDList1Hop(basis1hop);
		// // idl.write(idList1hop);
		// // Mapping mapping = new Mapping(idl, 0);
		// // mapping.writeMapping(mapping1hop);
		// // IdList idl = IdList.read(idList1hop, 0);
		// Mapping mapping = Mapping.readMapping(mapping1hop);
		// // GoogleP.generate1HopData(mapping, data, data1hop);
		// // GoogleP.generateGraphsForMapping(data1hop, graphs1hop, mapping);

		/**
		 * 1-hop GDA plots
		 */
		// String b = data + "81/33605/";
		// String graphs1hop = main + "1-hop-graphs/";
		// String p = main + "1-hop-plots/";
		// HashSet<Integer> plot = new HashSet<Integer>();
		// plot.add(1);
		// plot.add(5);
		// plot.add(81);
		// Mapping mapping = Mapping.readMapping(main + "1-hop-mapping.txt");
		// Network nw = new ErdosRenyi(690, 10, false, null, null);
		// Graph g = nw.generate();
		// GoogleP.plotGraphs(graphs1hop, p + "1/",
		// GoogleP.generateIdSpace1(b, mapping, g), plot);
		// GoogleP.plotGraphs(graphs1hop, p + "2/",
		// GoogleP.generateIdSpace2(b, mapping, g), plot);
		// GoogleP.plotGraphs(graphs1hop, p + "3/",
		// GoogleP.generateIdSpace3(b, mapping, g), plot);
		// GoogleP.plotGraphs(graphs1hop, p + "4-0/",
		// GoogleP.generateIdSpace4(b, mapping, g, 0), plot);
		// GoogleP.plotGraphs(graphs1hop, p + "4-1/",
		// GoogleP.generateIdSpace4(b, mapping, g, 1), plot);
		// GoogleP.plotGraphs(graphs1hop, p + "4-2/",
		// GoogleP.generateIdSpace4(b, mapping, g, 2), plot);
		// GoogleP.plotGraphs(graphs1hop, p + "4-3/",
		// GoogleP.generateIdSpace4(b, mapping, g, 3), plot);

		/**
		 * 1-hop GEXF graph
		 */
		// // GraphWriter.writeGEXF(graphs1hop, main + "graph.gexf");
		// // GoogleP.writeCSV(graphs1hop, graphs1hopCsv);

		/**
		 * Metrics for 1-hop neighborhoods
		 */
		// File[] files = (new File(main + "1-hop-graphs/"))
		// .listFiles(new FileNameFilter("", "-graph.txt"));
		// Arrays.sort(files, new FileIndexComparator("-", 0));
		// Network[] nw = new Network[files.length];
		// for (int i = 0; i < files.length; i++) {
		// String filename = files[i].getAbsolutePath();
		// int cid = Integer.parseInt(files[i].getName().split("-")[0]);
		// Transformation[] t = new Transformation[] {
		// new WeakConnectivityPartition(),
		// new GiantConnectedComponent() };
		// nw[i] = new GooglePlus(filename, cid, null, t);
		// }
		// Config.overwrite("MAIN_DATA_FOLDER", main + "1-hop-metrics-data/");
		// Config.overwrite("MAIN_PLOT_FOLDER", main + "1-hop-metrics-plots/");
		// Config.overwrite("METRICS",
		// "DD, CC, SP, WEAK_CONNECTIVITY, STRONG_CONNECTIVITY");
		// Series[] s = Series.generate(nw, 1);
		// Plot.multiAvg(s, "multi/");
		// Plot.singlesAvg(s, "single/");

		/**
		 * INC - graph generation [inc-data => inc-graphs]
		 */
		// String incData = main + "inc-data/";
		// String incGraphs = main + "inc-graphs/";
		// String incMetricsData = main + "inc-metrics-data/";
		// String incMetricsPlots = main + "inc-metrics-plots/";
		// ArrayList<Crawl> crawls = Crawl.getCrawls(data, mod, offset, 0, 0,
		// 100);
		// GoogleP.generateGraphsETC(incData, incGraphs, crawls);

		/**
		 * INC - graph generation [inc-data => inc-graphs] simple version
		 */
		// String dataSrc = main + "data/";
		// String graphsDst = main + "graphs/";
		// ArrayList<Crawl> crawls = new ArrayList<Crawl>();
		// int[] cids = new int[] { 82, 83, 84, 85, 86 };
		// for (int i = 0; i < cids.length; i++) {
		// if ((i % mod) == offset) {
		// crawls.add(new Crawl(new File(dataSrc + cids[i])));
		// }
		// }
		// GoogleP.generateGraphsETC(dataSrc, graphsDst, crawls);

		/**
		 * INC - metric computation
		 */
		// Network[] nwInc = new Network[8];
		// for (int i = 0; i < nwInc.length; i++) {
		// nwInc[i] = new GooglePlus(main + "temp/inc-graphs/" + (i + 1)
		// + "-graph.txt", i + 1, null, null);
		// }
		// Config.overwrite("METRICS", "CC");
		// Config.overwrite("MAIN_DATA_FOLDER", main +
		// "temp/inc-metrics-data/cc/");
		// Config.overwrite("MAIN_PLOT_FOLDER", main +
		// "temp/inc-metrics-plots/cc/");
		// Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);
		// Series[] s = Series.get(nwInc);
		// // Plot.multiAvg(s, "multi/");
		// Plot.singlesAvg(s, "single/");

		/**
		 * stats from WC
		 */
		double[] seen = GoogleP.statsFromWC(main + "stats/seenUsers.log");
		double[] crawled = GoogleP.statsFromWC(main + "stats/crawledUsers.log");
		double[] edges = GoogleP.statsFromWC(main + "stats/edges.log");
		double[] seenInc = GoogleP.statsFromWC(main + "stats/seenUsersInc.log");
		double[] crawledInc = GoogleP.statsFromWC(main
				+ "stats/crawledUsersInc.log");
		double[] edgesInc = GoogleP.statsFromWC(main + "stats/edgesInc.log");
		Config.overwrite("MAIN_PLOT_FOLDER", main + "stats/plots/");

		// PlotOld.fast(new double[][] { crawled, seen }, new String[] {
		// "Crawled Users", "Seen Users" }, "Users in each crawl", "CID",
		// "# of Users", "crawled-seen.pdf");
		// PlotOld.fast(new double[][] { crawled }, new String[] {
		// "Crawled Users" },
		// "Users in each crawl", "CID", "# of Users", "crawled.pdf");
		// PlotOld.fast(new double[][] { seen }, new String[] { "Seen Users" },
		// "Users in each crawl", "CID", "# of Users", "seen.pdf");
		//
		// PlotOld.fast(new double[][] { crawledInc, seenInc }, new String[] {
		// "Crawled Users (INC)", "Seen Users (INC)" },
		// "Users (INC) in each crawl", "CID", "# of Users (INC)",
		// "inc-crawled-seen.pdf");
		// PlotOld.fast(new double[][] { crawledInc },
		// new String[] { "Crawled Users (INC)" },
		// "Users (INC) in each crawl", "CID", "# of Users (INC)",
		// "inc-crawled.pdf");
		// PlotOld.fast(new double[][] { seenInc },
		// new String[] { "Seen Users (INC)" },
		// "Users (INC) in each crawl", "CID", "# of Users (INC)",
		// "inc-seen.pdf");
		//
		// PlotOld.fast(new double[][] { edges }, new String[] { "Edges" },
		// "Edges in each crawl", "CID", "# of Edges", "edges.pdf");
		// PlotOld.fast(new double[][] { edgesInc }, new String[] {
		// "Edges (INC)" },
		// "Edges (INC) in each crawl", "CID", "# of Edges (INC)",
		// "inc-edges.pdf");

		stats.end();
	}

	private static double[] statsFromWC(String filename) {
		Filereader fr = new Filereader(filename);
		String line = null;
		ArrayList<int[]> list = new ArrayList<int[]>();
		int maxIndex = 0;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.trim().split(" ");
			String[] temp2 = temp[1].split("/");
			String[] temp3 = temp2[temp2.length - 1].split("-");
			try {
				int value = Integer.parseInt(temp[0]);
				int index = Integer.parseInt(temp3[0]);
				list.add(new int[] { index, value });
				if (index > maxIndex) {
					maxIndex = index;
				}
			} catch (NumberFormatException e) {

			}
		}
		fr.close();
		double[] stats = new double[maxIndex + 1];
		for (int[] entry : list) {
			stats[entry[0]] = entry[1];
		}
		return stats;
	}

	private static void generateGraphsETC(String data, String graphs,
			ArrayList<Crawl> crawls) throws IOException {
		for (Crawl crawl : crawls) {
			System.out.println("\nCG - PROCESSING " + crawl);

			String crawled = graphs + crawl.getCid() + "-crawled.txt";
			String seen = graphs + crawl.getCid() + "-seen.txt";
			String mapping = graphs + crawl.getCid() + "-mapping.txt";
			String graph = graphs + crawl.getCid() + "-graph.txt";

			if ((new File(crawled)).exists() && (new File(seen)).exists()
					&& (new File(mapping)).exists()
					&& (new File(graph)).exists()) {
				System.out.println("nothing to do...");
				continue;
			}

			IdList crawledList = null;

			if (!(new File(crawled)).exists()) {
				crawledList = IdList.generateCrawledIdList(crawl);
				crawledList.write(crawled);
				System.out.println("crawledList => " + crawled);
			}

			if (!(new File(seen)).exists()) {
				IdList seenList = IdList.generateSeenIdList(crawl);
				seenList.write(seen);
				System.out.println("seenList => " + seen);
			}

			Mapping crawledMapping = null;

			if (!(new File(mapping)).exists()) {
				if (crawledList == null) {
					crawledList = IdList.read(crawled);
				}
				crawledMapping = new Mapping(crawledList, crawl.getCid());
				crawledMapping.writeMapping(mapping);
				System.out.println("crawledMapping => " + mapping);
			}

			if (!(new File(graph)).exists()) {
				if (crawledMapping == null) {
					crawledMapping = Mapping.readMapping(mapping);
				}
				Graph crawledGraph = GooglePlusReader.generateGraph(crawl,
						crawledMapping);
				new GtnaGraphWriter().write(crawledGraph, graph);
				System.out.println("crawledGraph => " + graph);
			}
		}
	}

	private static void writeCSV(String src, String dst) {
		File[] files = (new File(src)).listFiles();
		for (File f : files) {
			Graph g = new GtnaGraphReader().read(f.getAbsolutePath());
			new CsvGraphWriter().write(g,
					dst + f.getName().replace(".txt", ".csv"));
		}
	}

	private static void plotGraphs(String graphs, String dst,
			IdentifierSpace idspace, HashSet<Integer> plot) {
		boolean overwrite = true;
		File[] files = (new File(graphs)).listFiles(new FileNameFilter("",
				"-graph.txt"));
		Arrays.sort(files, new FileIndexComparator("-", 0));
		int index = 0;
		for (File f : files) {
			index++;
			int cid = Integer.parseInt(f.getName().split("-")[0]);
			String out = dst + cid + "-plot.pdf";
			if (!plot.contains(cid)) {
				continue;
			}
			if (((new File(out)).exists() && !overwrite)) {
				continue;
			}
			System.out.println(out);
			Graph graph = new GtnaGraphReader().read(f.getAbsolutePath());
			Gephi gephi = new Gephi();
			gephi.plot(graph, idspace, out);
			gephi = null;
		}
	}

	private static PlaneIdentifierSpaceSimple generateIdSpace1(
			String basis1hop, Mapping mapping, Graph g) {
		Transformation t = new RandomPlaneIDSpaceSimple(1, 16000, 16000, false);
		g = t.transform(g);
		PlaneIdentifierSpaceSimple idspace = (PlaneIdentifierSpaceSimple) g
				.getProperty("ID_SPACE_0");
		Partition[] p = idspace.getPartitions();

		ArrayList<ArrayList<String>> idss = GoogleP
				.generateNeighborhood1Hop(basis1hop);

		for (int i = 0; i < idss.size(); i++) {
			ArrayList<String> ids = idss.get(i);
			int starter = mapping.getMap().get(ids.get(0));
			PlaneIdentifier starterID = ((PlaneIdentifier) p[starter]
					.getRepresentativeIdentifier());
			double a = -1;
			double b = -1;
			double radius = 700;
			double offset = 800;
			double dist = 1600;
			if (i == 0) {
				a = 0;
				b = 0;
			} else if (i == 1) {
				a = 1;
				b = 0;
			} else if (i == 2) {
				a = 0;
				b = 1;
			} else if (i == 3) {
				a = 1;
				b = 1;
			}
			starterID.setX(a * dist + offset);
			starterID.setY(b * dist + offset);

			for (int j = 1; j < ids.size(); j++) {
				int index = mapping.getMap().get(ids.get(j));
				PlaneIdentifier nodeID = ((PlaneIdentifier) p[index]
						.getRepresentativeIdentifier());
				double pos = (double) (j - 1) / (double) (ids.size() - 1);
				double angle = pos * 360;
				double x = Math.sin(Math.toRadians(angle)) * radius;
				double y = Math.cos(Math.toRadians(angle)) * radius;
				nodeID.setX(x + starterID.getX());
				nodeID.setY(y + starterID.getY());
			}
		}

		return idspace;
	}

	private static PlaneIdentifierSpaceSimple generateIdSpace2(
			String basis1hop, Mapping mapping, Graph g) {
		Transformation t = new RandomPlaneIDSpaceSimple(1, 20000, 20000, false);
		g = t.transform(g);
		PlaneIdentifierSpaceSimple idspace = (PlaneIdentifierSpaceSimple) g
				.getProperty("ID_SPACE_0");
		Partition[] p = idspace.getPartitions();

		ArrayList<ArrayList<String>> idss = GoogleP
				.generateNeighborhood1Hop(basis1hop);

		int counter = 0;

		for (int i = 0; i < idss.size(); i++) {
			ArrayList<String> ids = idss.get(i);
			double a = -1;
			double b = -1;
			double radius = 10000;
			double offset = 10000;
			for (String id : ids) {
				int index = mapping.getMap().get(id);
				PlaneIdentifier nodeID = ((PlaneIdentifier) p[index]
						.getRepresentativeIdentifier());
				double pos = (double) counter / (double) (g.getNodes().length);
				double angle = pos * 360;
				double x = Math.sin(Math.toRadians(angle)) * radius;
				double y = Math.cos(Math.toRadians(angle)) * radius;
				nodeID.setX(x + offset);
				nodeID.setY(y + offset);
				counter++;
			}
		}

		return idspace;
	}

	private static PlaneIdentifierSpaceSimple generateIdSpace3(
			String basis1hop, Mapping mapping, Graph g) {
		Transformation t = new RandomPlaneIDSpaceSimple(1, 16000, 16000, false);
		g = t.transform(g);
		PlaneIdentifierSpaceSimple idspace = (PlaneIdentifierSpaceSimple) g
				.getProperty("ID_SPACE_0");
		Partition[] p = idspace.getPartitions();

		ArrayList<ArrayList<String>> idss = GoogleP
				.generateNeighborhood1Hop(basis1hop);

		for (int i = 0; i < idss.size(); i++) {
			ArrayList<String> ids = idss.get(i);
			int starter = mapping.getMap().get(ids.get(0));
			PlaneIdentifier starterID = ((PlaneIdentifier) p[starter]
					.getRepresentativeIdentifier());
			double a = -1;
			double b = -1;
			double radius = 700;
			double offset = 800;
			double dist = 1600;
			if (i == 0) {
				a = 1;
				b = 0;
			} else if (i == 1) {
				a = 0;
				b = 0.5;
				radius = 1000;
			} else if (i == 2) {
				a = 0;
				b = 0.5;
				radius = 1000;
			} else if (i == 3) {
				a = 1;
				b = 1;
			}
			starterID.setX(a * dist + offset);
			starterID.setY(b * dist + offset);

			for (int j = 1; j < ids.size(); j++) {
				int index = mapping.getMap().get(ids.get(j));
				PlaneIdentifier nodeID = ((PlaneIdentifier) p[index]
						.getRepresentativeIdentifier());
				double pos = (double) (j - 1) / (double) (ids.size() - 1);
				if (i == 1) {
					pos = (double) (j - 1)
							/ (double) (idss.get(1).size() + idss.get(2).size() - 2);
				} else if (i == 2) {
					pos = (double) (idss.get(1).size() + j - 2)
							/ (double) (idss.get(1).size() + idss.get(2).size() - 2);
				}
				double angle = pos * 360;
				double x = Math.sin(Math.toRadians(angle)) * radius;
				double y = Math.cos(Math.toRadians(angle)) * radius;
				nodeID.setX(x + starterID.getX());
				nodeID.setY(y + starterID.getY());
			}
		}

		return idspace;
	}

	private static PlaneIdentifierSpaceSimple generateIdSpace4(
			String basis1hop, Mapping mapping, Graph g, int single) {
		Transformation t = new RandomPlaneIDSpaceSimple(1, 16000, 16000, false);
		g = t.transform(g);
		PlaneIdentifierSpaceSimple idspace = (PlaneIdentifierSpaceSimple) g
				.getProperty("ID_SPACE_0");
		Partition[] p = idspace.getPartitions();

		ArrayList<ArrayList<String>> idss = GoogleP
				.generateNeighborhood1Hop(basis1hop);

		for (int i = 0; i < idss.size(); i++) {
			ArrayList<String> ids = idss.get(i);
			int starter = mapping.getMap().get(ids.get(0));
			PlaneIdentifier starterID = ((PlaneIdentifier) p[starter]
					.getRepresentativeIdentifier());
			double a = 0;
			double b = 0;
			double radius = 700;
			double offset = 800;
			double dist = 1600;
			starterID.setX(a * dist + offset);
			starterID.setY(b * dist + offset);

			for (int j = 1; j < ids.size(); j++) {
				int index = mapping.getMap().get(ids.get(j));
				PlaneIdentifier nodeID = ((PlaneIdentifier) p[index]
						.getRepresentativeIdentifier());
				double pos = (double) (j - 1) / (double) (ids.size() - 1);
				double angle = pos * 360;
				double x = Math.sin(Math.toRadians(angle)) * radius;
				double y = Math.cos(Math.toRadians(angle)) * radius;
				if (i != single) {
					x = starterID.getX();
					y = starterID.getY();
				}
				nodeID.setX(x + starterID.getX());
				nodeID.setY(y + starterID.getY());
			}
		}

		return idspace;
	}

	private static void generate1HopData(Mapping mapping, String data,
			String data1hop) throws InterruptedException, IOException {
		File[] files = (new File(data)).listFiles();
		int[] cids = new int[files.length];
		for (int i = 0; i < files.length; i++) {
			cids[i] = Integer.parseInt(files[i].getName());
		}
		Arrays.sort(cids);
		for (int i = 0; i < cids.length; i++) {
			int cid = cids[i];
			Crawl crawl = new Crawl(new File(data + cid));
			System.out.println("\n\nPROCESSING " + crawl);
			if (i > 0) {
				String src = data1hop + cids[i - 1] + "/1/";
				String dst = data1hop + cid + "/1/";
				GoogleP.cp(src, dst);
			}
			int counter = 0;
			for (File n : crawl.getNodeList()) {
				String uid = n.getName().split("-")[1];
				if (mapping.getMap().containsKey(uid)) {
					String src = n.getAbsolutePath();
					String dst = data1hop + cid + "/1/0-" + uid;
					GoogleP.ln(src, dst);
					counter++;
				}
			}
			System.out.println("ln'ed " + counter + " files => "
					+ (new File(data1hop + cid + "/1/")).listFiles().length);
		}
	}

	private static void cp(String src, String dst) {
		File[] files = (new File(src)).listFiles();
		for (File f : files) {
			String cmd = "cp -R " + f.getAbsolutePath() + " " + dst
					+ f.getName();
			GoogleP.execute(cmd);
			// System.out.println(cmd);
		}
		System.out.println("cp'ed " + files.length + " files => "
				+ (new File(dst)).listFiles().length);
	}

	private static void ln(String src, String dst) {
		String cmd = "ln -sf " + src + " " + dst;
		// System.out.println("  " + cmd);
		GoogleP.execute(cmd);
	}

	private static boolean execute(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);

			InputStream stderr = p.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				Output.writeln(line);
			}

			p.waitFor();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static IdList generateIDList1Hop(String basis1hop)
			throws IOException {
		ArrayList<ArrayList<String>> list1 = GoogleP
				.generateNeighborhood1Hop(basis1hop);
		ArrayList<String> list = new ArrayList<String>();
		for (ArrayList<String> l : list1) {
			for (String e : l) {
				list.add(e);
			}
		}
		IdList idl = new IdList(list, 0);
		return idl;
	}

	private static ArrayList<ArrayList<String>> generateNeighborhood1Hop(
			String basis1hop) {
		HashSet<String> seen = new HashSet<String>();
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		File[] nodes = (new File(basis1hop)).listFiles();
		for (File n : nodes) {
			ArrayList<String> neighbors = new ArrayList<String>();
			String[] temp = n.getName().split("-");
			String u_id = temp[1];
			seen.add(u_id);
		}
		for (File n : nodes) {
			ArrayList<String> neighbors = new ArrayList<String>();
			String[] temp = n.getName().split("-");
			int tid = Integer.parseInt(temp[0]);
			int tlid = 0;
			String u_id = temp[1];
			Task task = new Task(tid, 0, tlid, u_id, 0, 0);
			Node node = Node.read(n.getAbsolutePath(), task);
			neighbors.add(u_id);
			for (User out : node.getOut()) {
				if (!seen.contains(out.getId())) {
					neighbors.add(out.getId());
					seen.add(out.getId());
				}
			}
			for (User in : node.getIn()) {
				if (!seen.contains(in.getId())) {
					neighbors.add(in.getId());
					seen.add(in.getId());
				}
			}
			list.add(neighbors);
		}
		return list;
	}

	private static void generateGraphsForMapping(String data, String dst,
			Mapping mapping) {
		ArrayList<Crawl> crawls = Crawl.getCrawls(data, 1, 0);
		for (Crawl crawl : crawls) {
			String graph = dst + crawl.getCid() + "-graph.txt";
			if (!(new File(graph)).exists()) {
				System.out.print("generating graph for " + crawl);
				Graph g = GooglePlusReader.generateGraph(crawl, mapping);
				new GtnaGraphWriter().write(g, graph);
				System.out.println(" ===> " + graph);
			}
		}
	}

	private static void intersetions(String data, String graphs, int mod,
			int offset, String dst, String suffix, int minCid)
			throws IOException {
		int[] threshold = new int[] { 0, 1, 5, 10, 50, 100, 150, 200 };
		for (int minNodes : threshold) {
			GoogleP.intersections(data, graphs, mod, offset, suffix, minNodes,
					minCid, dst + minNodes + "k.txt");
		}
	}

	private static void intersections(String data, String graphs, int mod,
			int offset, String suffix, int minNodes, int minCid, String dst)
			throws IOException {
		ArrayList<Crawl> crawls = Crawl.getCrawls(data, mod, offset,
				minNodes * 1000, minCid, Integer.MAX_VALUE);
		IdList intersection = IdList.read(graphs + crawls.get(0).getCid()
				+ suffix, crawls.get(0).getCid());

		for (Crawl c : crawls) {
			String filename = graphs + c.getCid() + suffix;
			System.out.println("Intersection(" + minNodes + "k / " + minCid
					+ " / " + suffix.replace(".txt", "").replace("-", "")
					+ ") - PROCESSING " + c.getCid() + " @ "
					+ c.getNodeList().size());
			IdList list = IdList.read(filename, c.getCid());
			intersection = IdList.intersect(intersection, list, -1);
			System.out.println("  => " + intersection.getList().size());
		}
		System.out.println("===> " + intersection.getList().size()
				+ " elements");
		intersection.write(dst);

	}

	private static void consecutiveIntersections(String graphs, int mod,
			int offset, String suffix, String dst) throws IOException {
		File[] files = (new File(graphs)).listFiles(new FileNameFilter("",
				suffix));
		Arrays.sort(files, new FileIndexComparator("-", 0));
		double[] fractions = new double[files.length - 1];
		for (int i = 1; i < files.length; i++) {
			IdList l1 = IdList.read(files[i - 1].getAbsolutePath());
			IdList l2 = IdList.read(files[i].getAbsolutePath());
			IdList l3 = IdList.intersect(l1, l2, 0);
			int s1 = l1.getList().size();
			int s2 = l2.getList().size();
			int s3 = l3.getList().size();
			double fraction = (double) s3 / (double) Math.min(s1, s2);
			fractions[i - 1] = fraction;
			System.out.println(l1.getCid() + " /\\ " + l2.getCid());
			System.out.println(s1 + " / " + s2 + " => " + s3 + " (" + fraction
					+ ")");
			System.out.println("");
		}
		Statistics.write(fractions, dst);
	}

	private static void computations(String data, String graph, int mod,
			int offset, boolean generate, Metric[] metrics, String dataDst,
			String plotDst, Transformation[] transformations) {
		// Config.overwrite("METRICS", metrics);
		Config.overwrite("MAIN_DATA_FOLDER", dataDst);
		Config.overwrite("MAIN_PLOT_FOLDER", plotDst);
		File folder = new File(graph);
		File[] files = folder.listFiles(new FileNameFilter("", "-graph.txt"));
		Arrays.sort(files, new GraphSizeComparator(new FileIndexComparator("-",
				0)));
		int counter = 0;
		for (File f : files) {
			if (!f.getName().contains("graph")) {
				continue;
			}
			if ((counter % mod) == offset) {
				int cid = Integer.parseInt(f.getName().split("-")[0]);
				System.out.println("PROCESSING " + f.getAbsolutePath());

				// Network nw = new ReadableFile("G+ " + cid, "g+" + cid,
				// f.getAbsolutePath(), null, transformations);
				Network nw = new GooglePlus(f.getAbsolutePath(), cid,
						transformations);
				Series s = generate ? Series.generate(nw, metrics, 1) : Series
						.get(nw, metrics);

				if (s == null || s.getRunFolders() == null
						|| s.getRunFolders().length == 0) {
					System.out.println("SKIPPING " + f.getAbsolutePath());
				} else {
					// PlotOld.multiAvg(s, "multi-" + cid + "/", metrics);
				}
			}
			counter++;
		}
	}

	private static void plotCombined(String graphs, Metric[] metrics,
			String dataDst, String plotDst, Transformation[] transformations) {
		System.out.println("PLOTTING FOR " + metrics + " (from " + dataDst
				+ ")");
		// Config.overwrite("METRICS", metrics);
		Config.overwrite("MAIN_DATA_FOLDER", dataDst);
		Config.overwrite("MAIN_PLOT_FOLDER", plotDst);
		File folder = new File(graphs);
		File[] files = folder.listFiles();
		Arrays.sort(files);
		ArrayList<Series> list = new ArrayList<Series>();
		for (File f : files) {
			if (!f.getName().contains("graph")) {
				continue;
			}
			int cid = Integer.parseInt(f.getName().split("-")[0]);

			Network nw = new GooglePlus(f.getAbsolutePath(), cid,
					transformations);
			Series s = Series.get(nw, metrics);

			if (s != null && s.getRunFolders() != null
					&& s.getRunFolders().length != 0) {
				list.add(s);
			}
		}
		Series[] s = new Series[list.size()];
		for (int i = 0; i < list.size(); i++) {
			s[i] = list.get(i);
		}
		// Plot.multiAvg(s, "all-multi/");
		Config.overwrite("SINGLES_PLOT_LINE_WIDTH", "0");
		// PlotOld.singlesAvg(s, "all-single/", metrics);
	}

	private static void statistics(String graphs, int maxOffset, String suffix,
			String dst, String name) throws IOException {
		Config.overwrite("MAIN_PLOT_FOLDER", dst);
		double[] crawledUsers = Statistics.users(graphs, suffix, dst, name);

		double[][] recrawledUsers = Statistics.reUsers(graphs, maxOffset,
				suffix, dst, name, false);
		double[][] recrawledUsersF = Statistics.reUsers(graphs, maxOffset,
				suffix, dst, name, true);
		String[] names = new String[recrawledUsers.length];
		String[] namesF = new String[recrawledUsers.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = "Re-" + name + " users " + i;
			namesF[i] = "Re-" + name + " users " + i;
		}

		String[] pre = new String[] { "line/", "points/" };
		// int[] type = new int[] { PlotData.LINE, PlotData.POINTS };
		for (int j = 0; j < pre.length; j++) {
			// PlotOld.fast(new double[][] { crawledUsers },
			// new String[] { "users" }, "# of users", "crawl", "#",
			// pre[j] + "Users.pdf", "right", type[j], 1);
			//
			// PlotOld.fast(recrawledUsers, names, "# of Re users", "crawl",
			// "#",
			// pre[j] + "reUsers.pdf", "right", type[j], 1);
			// PlotOld.fast(recrawledUsersF, namesF, "Fraction of Re users",
			// "crawl",
			// "Fraction", pre[j] + "reUsersF.pdf", "right", type[j], 1);
			//
			// for (int i = 1; i < recrawledUsers.length; i++) {
			// PlotOld.fast(new double[][] { recrawledUsers[i] },
			// new String[] { names[i] }, "# of Re Users", "crawl",
			// "#", pre[j] + "reUsers-" + i + ".pdf", "right",
			// type[j], 1);
			// PlotOld.fast(
			// new double[][] { recrawledUsers[0], recrawledUsers[i] },
			// new String[] { names[0], names[i] }, "# of Re Users",
			// "crawl", "#", pre[j] + "reUsers_" + i + ".pdf",
			// "right", type[j], 1);
			// PlotOld.fast(new double[][] { recrawledUsersF[i] },
			// new String[] { namesF[i] }, "Fraction of Re Users",
			// "crawl", "Fraction", pre[j] + "reUsersF-" + i + ".pdf",
			// "right", type[j], 1);
			// PlotOld.fast(new double[][] { recrawledUsersF[0],
			// recrawledUsersF[i] }, new String[] { namesF[0],
			// namesF[i] }, "Fraction of Re Users", "crawl",
			// "Fraction", pre[j] + "reUsersF_" + i + ".pdf", "right",
			// type[j], 1);
			// }
		}
	}

	private static void readDates(String filename, String sep) {
		ArrayList<CrawlDuration> crawls = CrawlDuration.read(filename, sep);
		for (int i = 0; i < crawls.size(); i++) {
			CrawlDuration c = crawls.get(i);
			System.out.println(c.toString());
			if (i > 0) {
				CrawlDuration c2 = crawls.get(i - 1);
				double t = c.timeBetweenH(c2);
				// System.out.println(t);
				// if (t > 12) {
				// System.out.println("    " + (i - 1) + " : " + c2);
				// System.out.println("    " + i + " : " + c);
				// System.out.println("    ==> " + (t / 24) + " days");
				// }
			}
		}
	}
}

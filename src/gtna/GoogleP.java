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
package gtna;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.io.Filereader;
import gtna.io.GraphWriter;
import gtna.io.networks.googlePlus.Crawl;
import gtna.io.networks.googlePlus.CrawlDuration;
import gtna.io.networks.googlePlus.FileIndexComparator;
import gtna.io.networks.googlePlus.FileNameFilter;
import gtna.io.networks.googlePlus.GooglePlus;
import gtna.io.networks.googlePlus.GooglePlusReader;
import gtna.io.networks.googlePlus.GraphSizeComparator;
import gtna.io.networks.googlePlus.IdList;
import gtna.io.networks.googlePlus.Mapping;
import gtna.io.networks.googlePlus.Statistics;
import gtna.networks.Network;
import gtna.plot.Plot;
import gtna.plot.PlotData;
import gtna.transformation.Transformation;
import gtna.transformation.partition.StrongConnectivityPartition;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Config;
import gtna.util.Stats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author benni
 * 
 */
public class GoogleP {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// if (true) {
		//
		// Config.overwrite("METRICS",
		// "WEAK_CONNECTIVITY, STRONG_CONNECTIVITY");
		// Config.overwrite("MAIN_DATA_FOLDER", "./data/test/");
		// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/test/");
		// Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		// Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + false);
		//
		// Transformation t1 = new WeakConnectivityPartition();
		// Transformation t2 = new StrongConnectivityPartition();
		// Network[] nw = ErdosRenyi.get(100, new double[] { 3, 4, 5, 6 },
		// false, null, new Transformation[] { t1, t2 });
		// Series[] s = Series.generate(nw, 1);
		// Plot.multiAvg(s, "multi/");
		// Plot.singlesAvg(s, "single/");
		// return;
		// }

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

		// TODO perform
		// String d = main + "graphs-data/";
		// String p = main + "graphs-plots/";
		// GoogleP.plotting(graphs, "DD", d + "dd/", p + "dd/", null);
		// GoogleP.plotting(graphs, "CC", d + "cc/", p + "cc/", null);
		// TODO move data folders and replot!!!!
		// GoogleP.plotting(graphs, "SP", d + "sp/", p + "sp/", null);
		// GoogleP.plotting(graphs, "WEAK_CONNECTIVITY, STRONG_CONNECTIVITY", d
		// + "conn/", p + "conn/", connT);

		// int maxOffset = 7;
		// String suffix1 = "-crawled.txt";
		// String folder1 = "/home/benni/g+/statistics-crawled/";
		// String name1 = "crawled";
		// String suffix2 = "-seen.txt";
		// String folder2 = "/home/benni/g+/statistics-seen/";
		// String name2 = "seen";
		// GooglePlus.statistics(graphs, maxOffset, suffix1, folder1, name1);
		// GooglePlus.statistics(graphs, maxOffset, suffix2, folder2, name2);x

		GoogleP.readDates(main + "crawl-table.txt", "	");

		stats.end();
	}

	private static void generateGraphsETC(String data, String graphs, int mod,
			int offset) throws IOException {
		ArrayList<Crawl> crawls = Crawl.getCrawls(data, mod, offset);
		for (Crawl crawl : crawls) {
			String crawled = graphs + crawl.getCid() + "-crawled.txt";
			String seen = graphs + crawl.getCid() + "-seen.txt";
			String mapping = graphs + crawl.getCid() + "-mapping.txt";
			String graph = graphs + crawl.getCid() + "-graph.txt";

			if ((new File(crawled)).exists() && (new File(seen)).exists()
					&& (new File(mapping)).exists()
					&& (new File(graph)).exists()) {
				continue;
			}

			System.out.println("\nCG - PROCESSING " + crawl);

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
				GraphWriter.write(crawledGraph, graph);
				System.out.println("crawledGraph => " + graph);
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
				minNodes * 1000, minCid);
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
			int offset, boolean generate, String metrics, String dataDst,
			String plotDst, Transformation[] transformations) {
		Config.overwrite("METRICS", metrics);
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
				Network nw = new GooglePlus(f.getAbsolutePath(), cid, null,
						transformations);
				Series s = generate ? Series.generate(nw, 1) : Series.get(nw);

				if (s == null || s.dataFolders() == null
						|| s.dataFolders().length == 0) {
					System.out.println("SKIPPING " + f.getAbsolutePath());
				} else {
					Plot.multiAvg(s, "multi-" + cid + "/");
				}
			}
			counter++;
		}
	}

	private static void plotting(String graphs, String metrics, String dataDst,
			String plotDst, Transformation[] transformations) {
		System.out.println("PLOTTING FOR " + metrics + " (from " + dataDst
				+ ")");
		Config.overwrite("METRICS", metrics);
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

			Network nw = new GooglePlus(f.getAbsolutePath(), cid, null,
					transformations);
			// Network nw = new ReadableFile("G+ " + cid, "g+" + cid,
			// f.getAbsolutePath(), null, transformations);
			Series s = Series.get(nw);

			if (s != null && s.dataFolders() != null
					&& s.dataFolders().length != 0) {
				list.add(s);
			}
		}
		Series[] s = new Series[list.size()];
		for (int i = 0; i < list.size(); i++) {
			s[i] = list.get(i);
		}
		// Plot.multiAvg(s, "all-multi/");
		Config.overwrite("SINGLES_PLOT_LINE_WIDTH", "0");
		Plot.singlesAvg(s, "all-single/");
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
		int[] type = new int[] { PlotData.LINE, PlotData.POINTS };
		for (int j = 0; j < pre.length; j++) {
			Plot.fast(new double[][] { crawledUsers },
					new String[] { "users" }, "# of users", "crawl", "#",
					pre[j] + "Users.pdf", "right", type[j], 1);

			Plot.fast(recrawledUsers, names, "# of Re users", "crawl", "#",
					pre[j] + "reUsers.pdf", "right", type[j], 1);
			Plot.fast(recrawledUsersF, namesF, "Fraction of Re users", "crawl",
					"Fraction", pre[j] + "reUsersF.pdf", "right", type[j], 1);

			for (int i = 1; i < recrawledUsers.length; i++) {
				Plot.fast(new double[][] { recrawledUsers[i] },
						new String[] { names[i] }, "# of Re Users", "crawl",
						"#", pre[j] + "reUsers-" + i + ".pdf", "right",
						type[j], 1);
				Plot.fast(
						new double[][] { recrawledUsers[0], recrawledUsers[i] },
						new String[] { names[0], names[i] }, "# of Re Users",
						"crawl", "#", pre[j] + "reUsers_" + i + ".pdf",
						"right", type[j], 1);
				Plot.fast(new double[][] { recrawledUsersF[i] },
						new String[] { namesF[i] }, "Fraction of Re Users",
						"crawl", "Fraction", pre[j] + "reUsersF-" + i + ".pdf",
						"right", type[j], 1);
				Plot.fast(new double[][] { recrawledUsersF[0],
						recrawledUsersF[i] }, new String[] { namesF[0],
						namesF[i] }, "Fraction of Re Users", "crawl",
						"Fraction", pre[j] + "reUsersF_" + i + ".pdf", "right",
						type[j], 1);
			}
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

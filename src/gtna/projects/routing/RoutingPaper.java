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
 * RoutingPaper.java
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
package gtna.projects.routing;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.id.data.LruDataStore;
import gtna.io.graphReader.CaidaGraphReader;
import gtna.io.graphReader.GraphReader;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricDescriptionWrapper;
import gtna.metrics.routing.DataStorageMetric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.routing.routingTable.CcnRouting;
import gtna.routing.routingTable.RoutingTableRouting;
import gtna.routing.selection.source.ConsecutiveSourceSelection;
import gtna.routing.selection.target.DataStorageRandomTargetSelection;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.id.node.NodeIds;
import gtna.transformation.id.node.NodeIdsDataStorage;
import gtna.transformation.id.node.NodeIdsRoutingTable;
import gtna.util.Config;
import gtna.util.parameter.DateParameter;
import gtna.util.parameter.Parameter;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.print.resources.serviceui;

/**
 * @author benni
 * 
 */
public class RoutingPaper {

	static String[] dates_all = new String[] { "20070913", "20080102",
			"20080702", "20090103", "20090702", "20100101", "20100701",
			"20110102", "20110701", "20120102", "20120703", "20130102" };

	static String[] dates = new String[] { "20070913", "20080102", "20080702",
			"20090103", "20090702", "20100101", "20100701", "20110102",
			"20110701", "20120102", "20120703", "20130102" };

	static String pathFrom = "../resources/_temp/CAIDA/original/";
	static String pathTo = "../resources/_temp/CAIDA/gtna/";

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) {
		long l = (long) Double.parseDouble("1.1896344E9");
		System.out.println(l);
		System.out.println(new Date(l));
		System.out.println(new Date(l * 1000));
		// graphs();
		// print();

		int startRun = 0;
		int endRun = 69;
		if (args.length == 2) {
			startRun = Integer.parseInt(args[0]);
			endRun = Integer.parseInt(args[1]);
		}

		eval(startRun, endRun);
	}

	public static void print() {
		String[] filenames = getFilenames();
		GraphReader reader = new GtnaGraphReader();
		for (int i = 0; i < filenames.length; i++) {
			Graph g = reader.read(filenames[i]);
			// System.out.println(filenames[i]);
			// System.out.println(dates[i]);
			// System.out.println("  name: " + g.getName());
			// System.out.println("  nodes: " + g.getNodes().length);
			// System.out.println("  edges: " + g.getEdges().size());
			int n = g.getNodes().length;
			int e = g.getEdges().size();
			String d = dates[i];
			System.out.println(d.substring(2, 4) + "-" + d.substring(4, 6)
					+ " & " + ((n - (n % 1000)) / 1000) + "," + (n % 1000)
					+ " & " + ((e - (e % 1000)) / 1000) + "," + (e % 1000)
					+ " & ms & ms \\\\");
		}
	}

	public static void graphs() {
		GraphReader reader = new CaidaGraphReader();
		GraphWriter writer = new GtnaGraphWriter();
		File[] originals = (new File(pathFrom)).listFiles();
		for (File original : originals) {
			if (".".equals(original.getName())
					|| "..".equals(original.getName()))
				continue;
			System.out.println("reading " + original.getAbsolutePath());
			writer.write(reader.read(original.getAbsolutePath()), pathTo
					+ original.getName() + ".gtna");
		}
	}

	public static Network[] getNetworks(Transformation[] t) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String[] filenames = getFilenames();
		Network[] nw = new Network[filenames.length];
		for (int i = 0; i < filenames.length; i++) {
			try {
				Date d = df.parse(dates[i]);
				nw[i] = new DescriptionWrapper(new ReadableFile(
						"AS Topology (CAIDA)", "caida", filenames[i],
						new Parameter[] { new DateParameter("Date", d) }, t),
						"AS Topology");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return nw;
	}

	public static String[] getFilenames() {
		String[] filenames = new String[dates.length];
		for (int i = 0; i < dates.length; i++) {
			filenames[i] = filename(dates[i]);
		}
		return filenames;
	}

	public static String filename(String date) {
		return pathTo + date + ".txt.gtna";
	}

	public static void eval(int startRun, int endRun) {
		System.out.println("generating run(s) " + startRun + ":" + endRun);

		boolean generate = true;
		int routes = 5;

		Config.overwrite("MAIN_DATA_FOLDER", "data/routing-" + routes + "/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/routing-" + routes + "/");
		Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
				+ "_scripts/");

		Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		Config.overwrite("ROUTING_ROUTES_PER_NODE", "" + routes);

		Config.overwrite("GNUPLOT_TERMINAL", "png");
		Config.overwrite("PLOT_EXTENSION", ".png");

		Transformation t0, t1, t2, t3;
		t0 = new Bidirectional();
		t1 = new NodeIds();
		t2 = new NodeIdsRoutingTable();
		t3 = new NodeIdsDataStorage(new LruDataStore(0, 30), 1);

		Transformation[] t = new Transformation[] { t0, t1, t2, t3 };

		Network[] nw = getNetworks(t);

		Routing r1_ = new Routing(new RoutingTableRouting(),
				new ConsecutiveSourceSelection(),
				new DataStorageRandomTargetSelection());
		Routing r2_ = new Routing(new CcnRouting(),
				new ConsecutiveSourceSelection(),
				new DataStorageRandomTargetSelection());
		DataStorageMetric ds1_ = new DataStorageMetric();
		DataStorageMetric ds2_ = new DataStorageMetric();

		Metric r1 = new MetricDescriptionWrapper(r1_, "IP");
		Metric ds1 = new MetricDescriptionWrapper(ds1_, "IP", 1);
		Metric r2 = new MetricDescriptionWrapper(r2_, "NDN");
		Metric ds2 = new MetricDescriptionWrapper(ds2_, "NDN", 2);

		Metric[] metrics = new Metric[] { r1, ds1, r2, ds2 };

		if (generate) {
			for (Network network : nw) {
				if (startRun == 0) {
					Series.generate(network, metrics, endRun + 1);
				} else {
					Series.generate(network, metrics, startRun, endRun);
				}
			}
		}
		Series[] s = Series.get(nw, metrics);

		Plotting.multi(s, metrics, "multi/", Type.average, Style.linespoint);
		Plotting.single(s, metrics, "single/", Type.median, Style.linespoint);

		Plotting.multi(s[s.length - 1], metrics, "multi-1/", Type.average,
				Style.linespoint);
	}
}

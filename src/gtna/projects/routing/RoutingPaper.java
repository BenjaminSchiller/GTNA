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
import gtna.id.data.LruDataStore;
import gtna.io.graphReader.CaidaGraphReader;
import gtna.io.graphReader.GraphReader;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricDescriptionWrapper;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.routing.DataStorageMetric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
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

/**
 * @author benni
 * 
 */
public class RoutingPaper {

	static String[] dates_all = new String[] { "20070913", "20080102",
			"20080702", "20090103", "20090702", "20100101", "20100701",
			"20110102", "20110701", "20120102", "20120703" };

	static String[] dates = new String[] { "20070913", "20080102", "20080702",
			"20090103", "20090702", "20100101", "20100701", "20110102",
			"20110701", "20120102", "20120703" };

	static String pathFrom = "resources/_temp/CAIDA/original/";
	static String pathTo = "resources/_temp/CAIDA/gtna/";

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
		eval();
		// evaluation();
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

	public static void eval() {
		Config.overwrite("MAIN_DATA_FOLDER", "data/gtna2-routing-3/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/gtna2-routing-3/");

		Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		Config.overwrite("ROUTING_ROUTES_PER_NODE", "5");

		Config.overwrite("GNUPLOT_TERMINAL", "png");
		Config.overwrite("PLOT_EXTENSION", ".png");

		int times = 10;

		boolean generate = false;

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

		Metric dd = new DegreeDistribution();
		Metric sp = new ShortestPaths();
		Metric r1 = new MetricDescriptionWrapper(r1_, "IP");
		Metric ds1 = new MetricDescriptionWrapper(ds1_, "IP", 1);
		Metric r2 = new MetricDescriptionWrapper(r2_, "NDN");
		Metric ds2 = new MetricDescriptionWrapper(ds2_, "NDN", 2);

		Metric[] metrics = new Metric[] { r1, ds1, r2, ds2 };

		Series[] s = generate ? Series.generate(nw, metrics, times) : Series
				.get(nw, metrics);

		Plotting.multi(s, metrics, "multi/");
		Plotting.single(s, metrics, "single/");

		Plotting.multi(s[s.length - 1], metrics, "multi-1/");
	}
}

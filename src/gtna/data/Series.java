/*
 * ===========================================================
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
 * Series.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.data;

import gtna.graph.Graph;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Timer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Series {
	private Network network;

	private Metric[] metrics;

	private Series(Network network, Metric[] metrics) {
		this.network = network;
		this.metrics = metrics;
	}

	public Network getNetwork() {
		return this.network;
	}

	public Metric[] getMetrics() {
		return this.metrics;
	}

	public SingleList getSingleList(Metric m) {
		return SingleList.read(m, this.getSinglesFilename(m));
	}

	public Single getSingle(Metric m, String key) {
		SingleList sl = this.getSingleList(m);
		if (sl == null) {
			return null;
		}
		return sl.get(key);
	}

	public String getFolder() {
		return Config.get("MAIN_DATA_FOLDER") + this.network.getFolder();
	}

	public String getFolder(Metric m) {
		return this.getFolder() + m.getFolder();
	}

	public String getSeriesFolderRun(int run) {
		return this.getFolder() + run
				+ Config.get("FILESYSTEM_FOLDER_DELIMITER");
	}

	public String getMetricFolder(int run, Metric m) {
		return this.getSeriesFolderRun(run) + m.getFolder();
	}

	public String getSinglesFilename(Metric m) {
		return this.getFolder(m) + Config.get("SERIES_SINGLES_FILENAME");
	}

	public String getSinglesFilenameRun(int run, Metric m) {
		return this.getMetricFolder(run, m)
				+ Config.get("SERIES_SINGLES_FILENAME");
	}

	public String getMultiFilename(Metric m, String key) {
		return this.getFolder(m) + Config.get(key + "_DATA_FILENAME")
				+ Config.get("DATA_EXTENSION");
	}

	public String getFilenameRun(int run, Metric m, String key) {
		return this.getMetricFolder(run, m)
				+ Config.get(key + "_DATA_FILENAME")
				+ Config.get("DATA_EXTENSION");
	}

	public String getGraphFilename(int run) {
		return this.getSeriesFolderRun(run)
				+ Config.get("SERIES_GRAPH_FILENAME");
	}

	public String getRuntimesFilename() {
		return this.getFolder() + Config.get("SERIES_RUNTIME_FILENAME");
	}

	public String getRuntimesFilenameRun(int run) {
		return this.getSeriesFolderRun(run)
				+ Config.get("SERIES_RUNTIME_FILENAME");
	}

	public String[] getRunFolders() {
		int run = 0;
		while (run < 10000000) {
			File folder = new File(this.getSeriesFolderRun(run));
			if (folder.exists()) {
				run++;
			} else {
				run--;
				break;
			}
		}
		String[] folders = new String[run + 1];
		for (int i = 0; i <= run; i++) {
			folders[i] = this.getSeriesFolderRun(i);
		}
		return folders;
	}

	public static Series get(Network nw, Metric[] metrics) {
		Series s = new Series(nw, metrics);
		File folder = new File(s.getFolder());
		if (!folder.exists()) {
			System.err.println("no series data found for network "
					+ nw.getDescriptionShort());
		}
		for (Metric m : metrics) {
			folder = new File(s.getFolder(m));
			if (!folder.exists()) {
				System.err.println("no data for metric "
						+ m.getDescriptionShort() + " found for network "
						+ nw.getDescriptionShort());
				return null;
			}
		}
		return s;
	}

	public static Series generate(Network nw, Metric[] metrics, int times) {
		System.out.println("series (" + times + ") for "
				+ nw.getDescriptionShort());
		Series s = new Series(nw, metrics);
		File folder = new File(s.getFolder());
		if (!folder.exists()) {
			folder.mkdirs();
		}
		for (Metric m : metrics) {
			folder = new File(s.getFolder(m));
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}
		for (int run = 0; run < times; run++) {
			if (!Series.generateRun(s, run)) {
				System.err.println("error in run " + run);
				return null;
			}
		}
		Timer timerAggregation = new Timer("\n===> " + s.getFolder());
		boolean success = Aggregation.aggregate(s);
		timerAggregation.end();
		System.out.println("\n");
		if (success) {
			return s;
		}
		System.err.println("problems ocurred writing to " + s.getFolder());
		return null;
	}

	private static boolean generateRun(Series s, int run) {
		System.out.println("\n" + run + ":");
		ArrayList<Single> runtimes = new ArrayList<Single>();
		File folder = new File(s.getSeriesFolderRun(run));
		if (folder.exists() && Config.getBoolean("SKIP_EXISTING_DATA_FOLDERS")) {
			System.out.println("skipping");
			return true;
		}
		Timer timer = new Timer("G: " + s.getNetwork().getDescriptionShort());
		Graph g = s.getNetwork().generate();
		timer.end();
		runtimes.add(new Single("G", timer.getRuntime()));
		if (s.getNetwork().getTransformations() != null) {
			for (Transformation t : s.getNetwork().getTransformations()) {
				if (t.applicable(g)) {
					timer = new Timer("T: " + t.getDescriptionShort());
					for (int i = 0; i < t.getTimes(); i++) {
						g = t.transform(g);
					}
					timer.end();
					runtimes.add(new Single(t.getFolderName(), timer
							.getRuntime()));
				} else {
					System.out.println("T: " + t.getDescriptionShort()
							+ " not applicable");
				}
			}
		}
		if (Config.getBoolean("SERIES_GRAPH_WRITE")) {
			new GtnaGraphWriter().writeWithProperties(g,
					s.getGraphFilename(run));
		}
		StringBuffer p = new StringBuffer();
		ArrayList<String> properties = new ArrayList<String>(g.getProperties()
				.size());
		for (String gp : g.getProperties().keySet()) {
			properties.add(gp);
		}
		Collections.sort(properties);
		for (String gp : properties) {
			if (p.length() == 0) {
				p.append(gp);
			} else {
				p.append(", " + gp);
			}
		}
		System.out.println("P: " + p.toString());
		HashMap<String, Metric> metrics = new HashMap<String, Metric>();
		for (Metric m : s.getMetrics()) {
			folder = new File(s.getMetricFolder(run, m));
			if (!m.applicable(g, s.getNetwork(), metrics)) {
				System.out.println("M: " + m.getDescriptionShort()
						+ " not applicable");
				continue;
			}
			if (!folder.exists()) {
				folder.mkdirs();
			}
			timer = new Timer("M: " + m.getDescriptionShort());
			m.computeData(g, s.getNetwork(), metrics);
			timer.end();
			runtimes.add(new Single(m.getFolderName(), timer.getRuntime()));
			m.writeData(s.getMetricFolder(run, m));
			SingleList singleList = new SingleList(m, m.getSingles());
			singleList.write(s.getSinglesFilenameRun(run, m));
			metrics.put(m.getKey(), m);
			metrics.put(m.getFolder(), m);
		}
		SingleList rt = new SingleList(null, runtimes);
		rt.write(s.getRuntimesFilenameRun(run));
		return true;
	}

	public static Series[] get(Network[] nw, Metric[] metrics) {
		Series[] s = new Series[nw.length];
		for (int i = 0; i < nw.length; i++) {
			s[i] = Series.get(nw[i], metrics);
		}
		return s;
	}

	public static Series[][] get(Network[][] nw, Metric[] metrics) {
		Series[][] s = new Series[nw.length][];
		for (int i = 0; i < nw.length; i++) {
			s[i] = Series.get(nw[i], metrics);
		}
		return s;
	}

	public static Series[] generate(Network[] nw, Metric[] metrics, int times) {
		Series[] s = new Series[nw.length];
		for (int i = 0; i < nw.length; i++) {
			s[i] = Series.generate(nw[i], metrics, times);
		}
		return s;
	}

	public static Series[][] generate(Network[][] nw, Metric[] metrics,
			int times) {
		Series[][] s = new Series[nw.length][];
		for (int i = 0; i < nw.length; i++) {
			s[i] = Series.generate(nw[i], metrics, times);
		}
		return s;
	}
}

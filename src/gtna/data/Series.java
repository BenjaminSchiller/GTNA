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
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Timer;

import java.io.File;
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

	public String getFolder() {
		return Config.get("MAIN_DATA_FOLDER") + this.network.getFolder();
	}

	private String getFolder(Metric m) {
		return this.getFolder() + m.getFolder();
	}

	private String getFolder(int run) {
		return this.getFolder() + run
				+ Config.get("FILESYSTEM_FOLDER_DELIMITER");
	}

	private String getFolder(int run, Metric m) {
		return this.getFolder(run) + m.getFolder();
	}

	public String[] getRunFolders() {
		int run = 0;
		while (run < 10000000) {
			File folder = new File(this.getFolder(run));
			if (folder.exists()) {
				run++;
			} else {
				run--;
				break;
			}
		}
		String[] folders = new String[run + 1];
		for (int i = 0; i <= run; i++) {
			folders[i] = this.getFolder(i);
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
		// TODO implement
		return null;
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
				break;
			}
		}
		// TODO generate average data, etc.
		return null;
	}

	private static boolean generateRun(Series s, int run) {
		System.out.println("\n" + run + ":");
		File folder = new File(s.getFolder(run));
		if (folder.exists() && Config.getBoolean("SKIP_EXISTING_DATA_FOLDERS")) {
			System.out.println("skipping");
			return true;
		}
		Timer graphTimer = new Timer("G: "
				+ s.getNetwork().getDescriptionShort());
		Graph g = s.getNetwork().generate();
		graphTimer.end();
		if (s.getNetwork().getTransformations() != null) {
			for (Transformation t : s.getNetwork().getTransformations()) {
				if (t.applicable(g)) {
					Timer transformationTimer = new Timer("T: "
							+ t.getDescriptionShort());
					g = t.transform(g);
					transformationTimer.end();
				}
			}
		}
		HashMap<String, Metric> metrics = new HashMap<String, Metric>();
		for (Metric m : s.getMetrics()) {
			folder = new File(s.getFolder(run, m));
			if (!m.applicable(g, s.getNetwork(), metrics)) {
				continue;
			}
			if (!folder.exists()) {
				folder.mkdirs();
			}
			Timer metricTimer = new Timer("M: " + m.getDescriptionShort());
			m.computeData(g, s.getNetwork(), metrics);
			metricTimer.end();
			m.writeData(s.getFolder(run, m));
			metrics.put(m.getKey(), m);
			metrics.put(m.getFolder(), m);
		}
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

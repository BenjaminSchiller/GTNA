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
 * ResourcesGeneration.java
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
package gtna.projects.resources;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.io.graphReader.CaidaGraphReader;
import gtna.io.graphReader.CoauthorGraphReader;
import gtna.io.graphReader.GraphReader;
import gtna.io.graphReader.SpiGraphReader;
import gtna.io.graphReader.WotGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;
import gtna.util.filenameFilter.ExcludeHiddenFilenameFilter;
import gtna.util.filenameFilter.PostfixFilenameFilter;

import java.io.File;

/**
 * @author benni
 * 
 */
public class ResourcesGeneration {

	public static void main(String[] args) throws Exception {
		ResourcesGeneration.read(new CaidaGraphReader(), "caida");
		ResourcesGeneration.read(new WotGraphReader(), "wot");
		ResourcesGeneration.read(new SpiGraphReader(), "spi");
		ResourcesGeneration.read(new CoauthorGraphReader(), "coauthor");
	}

	public static void read(GraphReader graphReader, String type) {
		String srcFolder = "./resources/" + type + "/source/";
		String dstFolder = "./resources/" + type + "/original/";

		if (!(new File(srcFolder)).exists()) {
			return;
		}

		if (!(new File(dstFolder)).exists()) {
			(new File(dstFolder)).mkdirs();
		}

		File[] files = new File(srcFolder)
				.listFiles(new ExcludeHiddenFilenameFilter());
		for (File f : files) {
			String dest = dstFolder + f.getName() + ".gtna";
			if ((new File(dest)).exists()) {
				continue;
			}
			System.out.println(graphReader.getKey() + ": " + f.getName());
			Graph g = graphReader.read(f.getAbsolutePath());
			new GtnaGraphWriter().write(g, dest);
		}

		ResourcesGeneration.evaluate(type, dstFolder);
	}

	public static void evaluate(String type, String folder) {
		Config.overwrite("MAIN_DATA_FOLDER", folder + "data/");
		Config.overwrite("MAIN_PLOT_FOLDER", folder + "plots/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		File[] graphs = new File(folder).listFiles(new PostfixFilenameFilter(
				".gtna"));
		Network[] nw = new Network[graphs.length];
		for (int i = 0; i < graphs.length; i++) {
			nw[i] = new ReadableFile(type + " - " + graphs[i].getName(),
					graphs[i].getName(), graphs[i].getAbsolutePath(), null);
		}

		Metric dd = new DegreeDistribution();
		Metric[] metrics = new Metric[] { dd };

		Series[] s = Series.generate(nw, metrics, 1);

		Plotting.multi(s, metrics, "multi/");
	}

}

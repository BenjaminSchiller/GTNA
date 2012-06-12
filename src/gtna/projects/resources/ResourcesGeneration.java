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
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphReader.SpiGraphReader;
import gtna.io.graphReader.WotGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.partition.LargestStronglyConnectedComponent;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.remove.RemoveLargestNode;
import gtna.util.Config;
import gtna.util.filenameFilter.ExcludeHiddenFilenameFilter;
import gtna.util.filenameFilter.SuffixFilenameFilter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.io.File;

/**
 * @author benni
 * 
 */
public class ResourcesGeneration {

	public static void main(String[] args) throws Exception {
		Metric dd = new DegreeDistribution();
		Metric[] m = new Metric[] { dd };

		Transformation[][] t_caida = new Transformation[][] {
				new Transformation[] { new LargestStronglyConnectedComponent() },
				new Transformation[] { new LargestWeaklyConnectedComponent() },
				new Transformation[] { new LargestWeaklyConnectedComponent(),
						new Bidirectional() } };

		Transformation[][] t_coauthor = new Transformation[][] {
				new Transformation[] { new LargestStronglyConnectedComponent() },
				new Transformation[] { new LargestWeaklyConnectedComponent() },
				new Transformation[] { new LargestWeaklyConnectedComponent(),
						new Bidirectional() } };

		Transformation[][] t_spi = new Transformation[][] {
				new Transformation[] { new RemoveLargestNode() },
				new Transformation[] { new RemoveLargestNode(),
						new LargestStronglyConnectedComponent() },
				new Transformation[] { new RemoveLargestNode(),
						new LargestWeaklyConnectedComponent() },
				new Transformation[] { new RemoveLargestNode(),
						new LargestWeaklyConnectedComponent(),
						new Bidirectional() } };

		Transformation[][] t_wot = new Transformation[][] {
				new Transformation[] { new LargestStronglyConnectedComponent() },
				new Transformation[] { new LargestWeaklyConnectedComponent() },
				new Transformation[] { new LargestWeaklyConnectedComponent(),
						new Bidirectional() } };

		ResourcesGeneration.generate(new CaidaGraphReader(), "caida", t_caida,
				m);
		ResourcesGeneration.generate(new CoauthorGraphReader(), "coauthor",
				t_coauthor, m);
		ResourcesGeneration.generate(new SpiGraphReader(), "spi", t_spi, m);
		ResourcesGeneration.generate(new WotGraphReader(), "wot", t_wot, m);
	}

	public static void generate(GraphReader graphReader, String type,
			Transformation[][] T, Metric[] metrics) {
		String folder = "resources/" + type + "/";

		String srcFolder = folder + "source/";
		if (!(new File(srcFolder)).exists()) {
			return;
		}

		String originalFolder = folder + "original/";
		if (!(new File(originalFolder)).exists()) {
			(new File(originalFolder)).mkdirs();
		}

		generateOriginal(graphReader, type, srcFolder, originalFolder);
		generateTransformed(type, folder, originalFolder, T);
		evaluation(type, folder, originalFolder, T, metrics);
	}

	public static void evaluation(String type, String folder,
			String originalFolder, Transformation[][] T, Metric[] metrics) {
		Config.overwrite("MAIN_DATA_FOLDER", folder + "evaluation-data/");
		Config.overwrite("MAIN_PLOT_FOLDER", folder + "evaluation-plots/");

		File[] originalGraphs = new File(originalFolder)
				.listFiles(new SuffixFilenameFilter(".gtna"));
		Network[][] nw = new Network[T.length + 1][originalGraphs.length];
		for (int j = 0; j < originalGraphs.length; j++) {
			nw[0][j] = new ReadableFile(type, type,
					originalGraphs[j].getAbsolutePath(),
					new Parameter[] { new IntParameter("INDEX", j) }, null);
		}
		for (int i = 0; i < T.length; i++) {
			for (int j = 0; j < originalGraphs.length; j++) {
				nw[i + 1][j] = new ReadableFile(type + toString(T[i]), type
						+ toString(T[i]), originalGraphs[j].getAbsolutePath(),
						new Parameter[] { new IntParameter("INDEX", j) }, T[i]);
			}
		}

		Series[][] s = Series.generate(nw, metrics, 1);

		Plotting.single(s, metrics, "all/");
		Plotting.multi(s[0], metrics, "original/");
		for (int i = 0; i < T.length; i++) {
			Plotting.multi(s[i + 1], metrics, toString(T[i]) + "/");
		}
	}

	public static void generateOriginal(GraphReader graphReader, String type,
			String srcFolder, String originalFolder) {
		File[] srcGraphs = new File(srcFolder)
				.listFiles(new ExcludeHiddenFilenameFilter());
		for (File srcGraph : srcGraphs) {
			String original = originalFolder + srcGraph.getName() + ".gtna";
			if ((new File(original)).exists()) {
				continue;
			}
			System.out.println(type + ": original " + srcGraph.getName());
			Graph g = graphReader.read(srcGraph.getAbsolutePath());
			new GtnaGraphWriter().writeWithProperties(g, original);
		}
	}

	public static void generateTransformed(String type, String folder,
			String originalFolder, Transformation[][] T) {
		for (Transformation[] t : T) {
			String transformedFolder = folder + toString(t) + "/";
			generateTransformed(type, originalFolder, transformedFolder, t);
		}
	}

	public static void generateTransformed(String type, String originalFolder,
			String transformedFolder, Transformation[] t) {
		File[] originalGraphs = new File(originalFolder)
				.listFiles(new SuffixFilenameFilter(".gtna"));
		for (File originalGraph : originalGraphs) {
			String transformed = transformedFolder + originalGraph.getName();
			if ((new File(transformed)).exists()) {
				continue;
			}
			System.out.println(type + ": transformation "
					+ originalGraph.getName() + " @ " + toString(t));
			Graph g = new GtnaGraphReader().readWithProperties(originalGraph
					.getAbsolutePath());
			for (Transformation t1 : t) {
				if (!t1.applicable(g)) {
					continue;
				}
				g = t1.transform(g);
			}
			new GtnaGraphWriter().writeWithProperties(g, transformed);
		}
	}

	public static String toString(Transformation[] t) {
		if (t == null) {
			return "";
		}
		String string = "";
		for (Transformation t1 : t) {
			string += "_" + t1.getDescriptionShort();
		}
		return string;
	}

}

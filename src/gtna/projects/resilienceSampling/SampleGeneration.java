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
 * SampleGeneration.java
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
package gtna.projects.resilienceSampling;

import gtna.graph.Graph;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.ReadableFile;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;

import java.io.File;

/**
 * @author benni
 * 
 */
public class SampleGeneration {

	public static final String graphFolder = "/Users/benni/Downloads/criticalPoints/";

	public static final String[] topologies = new String[] { "spi", "wot", "er" };

	public static final Transformation[] samplings = new Transformation[] { new Bidirectional() };

	public static final int[] percents = new int[] { 10, 20 };

	public static void main(String[] args) {
		// generateSamples("wot");
		// Network[][] nwss = getSamples("wot");
		// for (Network[] nws : nwss) {
		// for (Network nw : nws) {
		// System.out.println(nw.getDescription());
		// }
		// }
	}

	public static void er() {
		Network er = new ErdosRenyi(100, 5, false, null);
		Graph g = er.generate();
		GraphWriter writer = new GtnaGraphWriter();
		writer.write(g, graphFolder + "er.gtna");
	}

	public static void generateSamples(String name) {
		System.out.println("generating samples for " + name);
		GraphWriter writer = new GtnaGraphWriter();
		Network nw = getNetwork(name);
		for (Transformation sampling : samplings) {
			for (int percent : percents) {
				String filename = getSampleFilename(name, sampling, percent);
				if ((new File(filename)).exists()) {
					System.out.println("  skipping " + filename);
					continue;
				}
				Graph g = nw.generate();
				g = sampling.transform(g);
				writer.write(g, filename);
				System.out.println("  => " + filename);
			}
		}
	}

	public static String getFilename(String name) {
		return graphFolder + name + ".gtna";
	}

	public static String getSampleFilename(String name,
			Transformation sampling, int percent) {
		return graphFolder + getSampleName(name, sampling, percent) + ".gtna";
	}

	public static String getSampleName(String name, Transformation sampling,
			int percent) {
		return name + "--" + sampling.getKey() + "--" + percent;
	}

	public static Network getNetwork(String name) {
		return new ReadableFile(name, name, getFilename(name), null);
	}

	public static Network[][] getSamples(String name) {
		Network[][] nw = new Network[samplings.length][percents.length];
		for (int i = 0; i < samplings.length; i++) {
			for (int j = 0; j < percents.length; j++) {
				String filename = getSampleFilename(name, samplings[i],
						percents[j]);
				String name_ = getSampleName(name, samplings[i], percents[j]);
				nw[i][j] = new ReadableFile(name_, name_, filename, null);
			}
		}
		return nw;
	}
}

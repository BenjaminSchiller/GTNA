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
package gtna.projects.sampling.test;

import gtna.graph.Graph;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableFolder;
import gtna.transformation.Transformation;
import gtna.transformation.partition.LargestStronglyConnectedComponent;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;
import gtna.util.Timer;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.io.File;

/**
 * @author benni
 * 
 */
public class SampleGeneration {

	public static final String graphFolder = "/Users/Tim/Documents/Projekte/gtna/source/graphs/";

	public static final String[] topologies = new String[] { "spi", "wot", "er" };

	public static final SamplingAlgorithm[] algorithms = new SamplingAlgorithm[] {
			SamplingAlgorithm.RANDOMWALK };

	public static final int[] percents = new int[] { 10, 20, 30, 40, 50, 60,
			70, 80, 90 };
	
//	public static final int[] percents = new int[] { 90 };

	public static final int times = 1;

	public static void main(String[] args) {
		generateSamples("wot", times);
	}

	public static void er() {
		Network er = new ErdosRenyi(100, 5, false, null);
		Graph g = er.generate();
		GraphWriter writer = new GtnaGraphWriter();
		writer.write(g, graphFolder + "er.gtna");
	}

	public static void generateSamples(String name, int times) {
		System.out.println("generating samples for " + name);
		GraphWriter writer = new GtnaGraphWriter();
		Transformation subgraph = new ExtractSampledSubgraph();
		Network nw = getNetwork(name);
		for (SamplingAlgorithm algorithm : algorithms) {
			for (int percent : percents) {
				for (int t = 0; t < times; t++) {
					System.out.println(name + " / " + algorithm + " @ "
							+ percent + "%");
					String filename = getSampleFilename(name, algorithm,
							percent, t);
					if ((new File(filename)).exists()) {
						System.out.println("  skipping " + filename);
						continue;
					}
					Graph g = nw.generate();
					// System.out.println("   " + g);
					Transformation sampling = SamplingAlgorithmFactory
							.getInstanceOf(algorithm, (double) percent / 100.0,
									false, 1, System.currentTimeMillis());
					Transformation weak = new LargestWeaklyConnectedComponent();
					Transformation strong = new LargestStronglyConnectedComponent();

					Timer t1 = new Timer();
//					g = strong.transform(g);
					t1.end();
					Timer t2 = new Timer();
					g = sampling.transform(g);
					t2.end();
					Timer t3 = new Timer();
					g = subgraph.transform(g);
					t3.end();
					
//					writer.writeWithProperties(g, filename); // TODO

					System.out.println("=> " + filename + "\tSTRONG: " + t1.getSec() + "s / SAMPLING: " + t2.getSec() + "s / SUBGRAPH: " + t3.getSec() + "s");
				}
			}
		}
	}

	public static String getFilename(String name) {
		return graphFolder + name + ".gtna";
	}

	public static String getSampleFolder(String name,
			SamplingAlgorithm algorithm, int percent) {
		return graphFolder + name + "/" + algorithm + "-" + percent + "/";
	}

	public static String getSampleFilename(String name,
			SamplingAlgorithm algorithm, int percent, int number) {
		return getSampleFolder(name, algorithm, percent) + number + ".gtna";
	}

	public static Network getNetwork(String name) {
		return new ReadableFile(name, name, getFilename(name), null);
	}

	public static Network[][] getSamples(String name) {
		Network[][] nw = new Network[algorithms.length][percents.length];
		for (int i = 0; i < algorithms.length; i++) {
			for (int j = 0; j < percents.length; j++) {
				String folder = getSampleFolder(name, algorithms[i],
						percents[j]);
				nw[i][j] = new DescriptionWrapper(new ReadableFolder(name,
						name, folder, ".gtna", new Parameter[] {
								new StringParameter("ALGORITHM",
										algorithms[i].toString()),
								new IntParameter("PERCENT", percents[j]) },
						null), name + "-" + algorithms[i] + "@" + percents[j],
						new Parameter[] {
								new StringParameter("ALGORITHM", algorithms[i]
										.toString()),
								new IntParameter("PERCENT", percents[j]) });
			}
		}
		return nw;
	}
}

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
 * SamplingThesis.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.sampling;

import gtna.graph.Graph;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphReader.SnapGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;
import gtna.transformation.util.RemoveGraphProperty;
import gtna.transformation.util.RemoveGraphProperty.RemoveType;
import gtna.util.Timer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Tim
 * 
 */
public class SampleNetworks {

	private static String folder;
	private static String fileending;
	private static int scaledown;
	private static algorithms samplingClasses;
	private static LinkedList<SamplingAlgorithm> samplingAlgorithms = new LinkedList<SamplingAlgorithm>();
	private static int times;

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {

		if (args.length == 0
				|| (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			System.out
					.println("Usage: \n ./java -jar <yourJARname>.jar folder=<foldercontainingSNAPfile> fileending=<SNAPfileending>");
			System.exit(0);
		}

		for (String s : args) {
			matchArgument(s);
		}

		if (samplingClasses == algorithms.SPECIFIED
				&& samplingAlgorithms.size() == 0) {
			System.out
					.println("You have to specify sampling algorithm(s): algorithm=choice1,choice2,choice3");
			System.exit(0);
		}

		File f = new File(folder);
		if (f.isDirectory()) {
			File[] files = f.listFiles();

			for (File snaps : files) {
				if (snaps.isFile() && snaps.getName().endsWith(fileending)) {
					if (!(new File(folder
							+ snaps.getName().replace(fileending, "") + "/"
							+ String.valueOf(scaledown)).exists())
							&& (new File(folder
									+ snaps.getName().replace(fileending, "")
									+ "/" + String.valueOf(scaledown)).mkdirs())) {
						System.out.println("----------------------------\n\tsampling " + snaps.getName());
						String name = snaps.getName().replace(fileending, "");
						createSample(folder, name, scaledown);

					} else {
						System.out.println("skipping: " + snaps.getName());
					}
				}
			}

		}

	}

	/**
	 * @param p
	 * @param name
	 * @param scaledown2
	 */
	private static void createSample(String p, String name, int sd) {
		String network = p + name + fileending;

		Graph g = new GtnaGraphReader().read(network);

		if (samplingClasses != algorithms.SPECIFIED) {
			samplingAlgorithms.clear();
			if (samplingClasses == algorithms.ALL) {
				samplingAlgorithms.addAll(Arrays.asList(SamplingAlgorithm
						.values()));
			} else if (samplingClasses == algorithms.US) {
				samplingAlgorithms.offer(SamplingAlgorithm.UNIFORMSAMPLING);
			} else if (samplingClasses == algorithms.BFS) {
				samplingAlgorithms.offer(SamplingAlgorithm.BFS);
				samplingAlgorithms.offer(SamplingAlgorithm.SNOWBALLSAMPLING);
				samplingAlgorithms
						.offer(SamplingAlgorithm.RESPONDENTDRIVENSAMPLING);
				samplingAlgorithms.offer(SamplingAlgorithm.FORESTFIRE);
			} else if (samplingClasses == algorithms.RW) {
				samplingAlgorithms.add(SamplingAlgorithm.RANDOMWALK);
				samplingAlgorithms
						.add(SamplingAlgorithm.RANDOMWALK_DEGREECORRECTION);
				samplingAlgorithms.add(SamplingAlgorithm.RANDOMWALK_MULTIPLE);
				samplingAlgorithms.add(SamplingAlgorithm.FRONTIERSAMPLING);
				samplingAlgorithms.add(SamplingAlgorithm.RANDOMSTROLL);
				samplingAlgorithms
						.add(SamplingAlgorithm.RANDOMSTROLL_DEGREECORRECTION);
				samplingAlgorithms.add(SamplingAlgorithm.RANDOMJUMP);
				samplingAlgorithms.add(SamplingAlgorithm.DFS);
			} else if (samplingClasses == algorithms.SELECTED) {
				samplingAlgorithms.add(SamplingAlgorithm.RANDOMWALK);
				samplingAlgorithms.add(SamplingAlgorithm.RANDOMSTROLL);
				samplingAlgorithms.add(SamplingAlgorithm.BFS);
				samplingAlgorithms.add(SamplingAlgorithm.UNIFORMSAMPLING);
			} else if (samplingClasses == algorithms.TEST) {
				samplingAlgorithms.add(SamplingAlgorithm.RANDOMWALK);
				samplingAlgorithms.add(SamplingAlgorithm.BFS);
			}
		}

		for (SamplingAlgorithm a : samplingAlgorithms) {
			for (int i = 0; i < times; i++) {
				int run = i;
				String sp = p + name + "/" + String.valueOf(sd) + "/"
						+ a.toString().toLowerCase() + "/" + name + "-" + run;

				if ((new File(sp + ".gtna")).exists()) {
					System.out.println("Skipping " + a.toString().toLowerCase()
							+ ". File exists.");
					break;
				}

				Timer t = new Timer();
				Transformation sampling = SamplingAlgorithmFactory
						.getInstanceOf(a, (double) sd / 100.0, false, 1,
								System.currentTimeMillis(), true);
				Transformation subgraph = new ExtractSampledSubgraph();

				Graph sampled = sampling.transform(g);
				sampled = subgraph.transform(sampled);

				writeGraphToFile(sampled, sp + ".gtna");
				writeGraphToFile(g, p + name + "/" + String.valueOf(sd) + "/"
						+ a.toString().toLowerCase() + "/"
						+ "original-with-properties/" + name);

				// for dummy file creation
				// File f = new File(sp + ".gtna");
				// try {
				// f.createNewFile();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				t.end();
				System.out.println("Sampled " + name + " using "
						+ a.toString().toLowerCase() + " @ " + sd + "% in "
						+ t.getMsec() + "ms (" + (run+1) + " of " + times + ")");
			}

			g = (new RemoveGraphProperty(RemoveType.ALL_OF_TYPE, "SAMPLE"))
					.transform(g);
		}

	}

	private static String writeGraphToFile(Graph g, String filename) {
		new GtnaGraphWriter().writeWithProperties(g, filename);
		
		if(!filename.endsWith(".gtna")){
			File f = new File(filename);
			f.renameTo(new File(filename + ".gtna"));
		}
		return filename;
	}

	public enum algorithms {
		ALL, RW, BFS, US, SELECTED, SPECIFIED, TEST
	}

	/**
	 * @param s
	 * @throws ParseException
	 */
	private static void matchArgument(String s) throws ParseException {
		if (s.startsWith("fileending=")) {
			fileending = s.substring(11);
		} else if (s.startsWith("scaledown=")) {
			scaledown = Integer.parseInt(s.substring(10));
		} else if (s.startsWith("times=")) {
			times = Integer.parseInt(s.substring(6));
		} else if (s.startsWith("class=")) {
			String c = s.substring(6);
			for (algorithms a : algorithms.values()) {
				if (a.toString().equalsIgnoreCase(c))
					samplingClasses = a;
			}
		} else if (s.startsWith("algorithms=")) {
			String c = s.substring(11);
			String[] specified = c.split(",");
			for (String sa : specified) {
				for (SamplingAlgorithm a : SamplingAlgorithm.values()) {
					if (a.toString().equalsIgnoreCase(sa)) {
						samplingAlgorithms.offer(a);
						break;
					}
				}
			}
		} else if (s.startsWith("folder=")) {
			folder = s.substring(7);
			File f = new File(folder);
			if (!f.isDirectory()) {
				throw new IllegalArgumentException("Folder not existing.");
			}
		}
	}
}

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
 * GDAEvaluation_RHDD.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.GD;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableList;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedy.GreedyBacktracking;
import gtna.routing.greedyVariations.DepthFirstEdgeGreedy;
import gtna.routing.lookahead.LookaheadSimple;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.id.RandomMDIDSpaceSimple;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpace;
import gtna.transformation.partition.GiantConnectedComponent;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Config;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Nico
 * 
 */
public class GDAEvaluation_RHDD {
	public static Network getNetwork(int size, String gda, int count, RoutingAlgorithm ra) {
		String[] files = new String[count];

		String folder = "./resources/evaluation/" + size + gda;
		for (int i = 0; i < count; i++) {
			files[i] = folder + "/" + i + ".txt";
		}
		String[] tempSplit = folder.split("/");
		String outputFolder = tempSplit[tempSplit.length - 1];
		return new ReadableList("", outputFolder, files, ra, null);
	}

	public static void main(String[] args) {
		int lengthOfSeries = 10;
		RoutingAlgorithm[] ra = new RoutingAlgorithm[] { new Greedy(100), new GreedyBacktracking(100),
				new LookaheadSimple(50) };
		String folderName;
		String rootFolder = "./resources/evaluation/";
		ArrayList<Network> networks = new ArrayList<Network>();

		String[] base = new String[] {
				// "1000/barabasiAlbert-10-b-wcp-gcc-",
				// "1000/erdosRenyi-10.0-true-b-wcp-gcc-",
				// "5000/barabasiAlbert-10-b-wcp-gcc-",
				// "5000/erdosRenyi-10.0-true-b-wcp-gcc-",
				"10000/barabasiAlbert-10-b-wcp-gcc-", "10000/erdosRenyi-10.0-true-b-wcp-gcc-",
		// "11407/SPI-b-wcp-gcc-",
		// "20057/CAIDA-b-wcp-gcc-",
		// "25487/WOT-b-wcp-gcc-"
		};
		String[] gdaFolders = new String[] { "fr-100-[100.0, 100.0]-false-100/",

				// "CCC-1-100.0-true/",
				"st-1-100.0-true/",

				// "stBFS-hd-bt-100.0-100.0/", "stBFS-hd-kBST-100.0-100.0/",
				"stBFS-hd-mh-100.0-100.0/",
		// "stBFS-hd-ws-100.0-100.0/",

		};

		int missingFiles;
		String[] tempSplit;
		Network[] nwArray;
		String[] files;
		File tempFile;
		Series[] s;
		Network temp;
		String outputFolder;
		Transformation[] stArray;
		Transformation[] randomIDSpace = new Transformation[] {
				new RandomMDIDSpaceSimple(1, new double[] { 100, 100 }, false),
				// new RandomPlaneIDSpaceSimple(1, 100, 100, false),
				new RandomRingIDSpace(1, 100, false), };

		for (RoutingAlgorithm singleRA : ra) {
			for (Transformation sT : randomIDSpace) {
				stArray = new Transformation[] { new Bidirectional(), new WeakConnectivityPartition(),
						new GiantConnectedComponent(), sT };
				// networks.add(new ReadableFile("caida", "CAIDA",
				// "./data/cycle-aslinks.l7.t1.c001749.20111206.txt.gtna",
				// singleRA, stArray));
				// networks.add(new ReadableFile("wot", "WOT",
				// "./data/graph-wot.txt", singleRA, stArray));
				// networks.add(new ReadableFile("spi", "SPI",
				// "./data/graph-spi.txt", singleRA, stArray));
				// networks.add(new BarabasiAlbert(1000, 10, singleRA,
				// stArray));
				// networks.add(new BarabasiAlbert(5000, 10, singleRA,
				// stArray));
				// networks.add(new BarabasiAlbert(10000, 10, singleRA,
				// stArray));
				// networks.add(new ErdosRenyi(1000, 10, true, singleRA,
				// stArray));
				// networks.add(new ErdosRenyi(5000, 10, true, singleRA,
				// stArray));
				// networks.add(new ErdosRenyi(10000, 10, true, singleRA,
				// stArray));
			}

			for (String singleBase : base) {
				for (String singleGDA : gdaFolders) {
					files = new String[lengthOfSeries];
					folderName = singleBase + singleGDA;
					missingFiles = 0;
					for (int i = 0; i < lengthOfSeries; i++) {
						tempFile = new File(rootFolder + folderName + "/" + i + ".txt_ID_SPACE_0");
						if (!tempFile.exists()) {
							missingFiles++;
						}
					}
					if (missingFiles > 0) {
						System.out.println("Skipping non-complete " + folderName + " (" + missingFiles
								+ " simulations missing)");
						continue;
					}

					tempSplit = folderName.split("/");
					outputFolder = tempSplit[tempSplit.length - 1];

					for (int i = 0; i < lengthOfSeries; i++) {
						files[i] = rootFolder + folderName + i + ".txt";
					}
					String nwName;
					if (singleBase.contains("baraba")) {
						nwName = "BA ";
					} else if (singleBase.contains("erdosRenyi")) {
						nwName = "ER ";
					} else {
						throw new RuntimeException("No name could be determined");
					}
					nwName += tempSplit[0] + " ";
					if (singleGDA.contains("fr-100")) {
						nwName += "FR";
					} else if (singleGDA.contains("CCC-")) {
						nwName += "CCC";
					} else if (singleGDA.contains("st-")) {
						nwName += "S/T";
					} else if (singleGDA.contains("-hd-mh-")) {
						nwName += "M/H";
					} else if (singleGDA.contains("-hd-ws")) {
						nwName += "W/S";
					} else {
						throw new RuntimeException("No name could be determined");
					}

					temp = new ReadableList(nwName, outputFolder, files, singleRA, null);
					networks.add(temp);
				}
			}
		}

		Config.overwrite("MAIN_DATA_FOLDER", "./data/GDAevaluation_RHDD/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/testsNico/");
		Config.overwrite("METRICS", "ROUTING, ROUTING_HOPDEGREEDISTRIBUTION");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);
		Config.overwrite("ROUTING_HOPDEGREEDISTRIBUTION_DISTRIBUTION_PLOT_LOGSCALE_X", "" + true);
		Config.overwrite("ROUTING_HOPDEGREEDISTRIBUTION_DISTRIBUTION_CDF_PLOT_LOGSCALE_X", "" + true);
		Config.overwrite("ROUTING_HOPDEGREEDISTRIBUTION_HOPS", "5");
		Config.overwrite("AVERAGE_PLOT_LINE_WIDTH", "4");
		Config.overwrite("AVERAGE_PLOT_POINT_WIDTH", "2");

		nwArray = new Network[networks.size()];
		networks.toArray(nwArray);
		 s = Series.generate(nwArray, lengthOfSeries);

//		Config.overwrite("GNUPLOT_PATH", "C:\\Cygwin\\bin\\gnuplot.exe");
//		nwArray = new Network[] {
//				new DescriptionWrapper(getNetwork(10000, "/erdosRenyi-10.0-true-b-wcp-gcc-stBFS-hd-mh-100.0-100.0", 60,
//						new Greedy(100)), "ER (n = 10000), GDA: S/T, Greedy routing"),
//				new DescriptionWrapper(getNetwork(10000, "/barabasiAlbert-10-b-wcp-gcc-stBFS-hd-mh-100.0-100.0", 60,
//						new Greedy(100)), "BA (n = 10000), GDA: S/T, Greedy routing"),
//				new DescriptionWrapper(getNetwork(10000, "/erdosRenyi-10.0-true-b-wcp-gcc-stBFS-hd-mh-100.0-100.0", 60,
//						new GreedyBacktracking(100)), "ER (n = 10000), GDA: S/T, Greedy backtracking routing"),
//				new DescriptionWrapper(getNetwork(10000, "/barabasiAlbert-10-b-wcp-gcc-stBFS-hd-mh-100.0-100.0", 60,
//						new GreedyBacktracking(100)), "BA (n = 10000), GDA: S/T, Greedy backtracking routing"),
//
//		};
//		s = Series.get(nwArray);

		Plot.allMulti(s, "GDAEva-RHDD/");
	}
}

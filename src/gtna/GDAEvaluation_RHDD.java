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
package gtna;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
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
import java.util.Iterator;

/**
 * @author Nico
 *
 */
public class GDAEvaluation_RHDD {
	public static void main(String[] args) {
		int lengthOfSeries = 10;
		RoutingAlgorithm[] ra = new RoutingAlgorithm[] { new Greedy(100),
		};
		String folderName;
		String rootFolder = "./resources/evaluation/";
		ArrayList<Network> networks = new ArrayList<Network>();

		String[] base = new String[] { "1000/barabasiAlbert-10-b-wcp-gcc-",
//				"1000/erdosRenyi-10.0-true-b-wcp-gcc-",
//				"5000/barabasiAlbert-10-b-wcp-gcc-", "5000/erdosRenyi-10.0-true-b-wcp-gcc-",
//				"10000/barabasiAlbert-10-b-wcp-gcc-", "10000/erdosRenyi-10.0-true-b-wcp-gcc-", "11407/SPI-b-wcp-gcc-",
//				"20057/CAIDA-b-wcp-gcc-", "25487/WOT-b-wcp-gcc-" 
				};
		String[] gdaFolders = new String[] {"fr-100-[100.0, 100.0]-false-100/",
				
				"CCC-1-100.0-true/", "st-1-100.0-true/",
				
//				"stBFS-hd-bt-100.0-100.0/", "stBFS-hd-kBST-100.0-100.0/",
//				"stBFS-hd-mh-100.0-100.0/", "stBFS-hd-ws-100.0-100.0/",
				
		};

		int missingFiles;
		String[] tempSplit;
		String[] files;
		File tempFile;
		Network temp;
		String outputFolder;
		Transformation[] stArray;
		Transformation[] randomIDSpace = new Transformation[] {
				new RandomMDIDSpaceSimple(1, new double[] { 100, 100 }, false),
//				new RandomPlaneIDSpaceSimple(1, 100, 100, false), 
				new RandomRingIDSpace(1, 100, false),
				};

		for (RoutingAlgorithm singleRA : ra) {
			for (Transformation sT : randomIDSpace) {
				stArray = new Transformation[] { new Bidirectional(), new WeakConnectivityPartition(),
						new GiantConnectedComponent(), sT };
//				networks.add(new ReadableFile("caida", "CAIDA", "./data/cycle-aslinks.l7.t1.c001749.20111206.txt.gtna",
//						singleRA, stArray));
//				networks.add(new ReadableFile("wot", "WOT", "./data/graph-wot.txt", singleRA, stArray));
//				networks.add(new ReadableFile("spi", "SPI", "./data/graph-spi.txt", singleRA, stArray));
				networks.add(new BarabasiAlbert(1000, 10, singleRA, stArray));
//				networks.add(new BarabasiAlbert(5000, 10, singleRA, stArray));
//				networks.add(new BarabasiAlbert(10000, 10, singleRA, stArray));
//				networks.add(new ErdosRenyi(1000, 10, true, singleRA, stArray));
//				networks.add(new ErdosRenyi(5000, 10, true, singleRA, stArray));
//				networks.add(new ErdosRenyi(10000, 10, true, singleRA, stArray));
			}

			for (String singleBase : base) {
				for (String singleGDA : gdaFolders) {
					files = new String[lengthOfSeries];
					folderName = rootFolder + singleBase + singleGDA;
					missingFiles = 0;
					for (int i = 0; i < lengthOfSeries; i++) {
						tempFile = new File(folderName + "/" + i + ".txt_ID_SPACE_0");
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
						files[i] = folderName + i + ".txt";
					}
					temp = new ReadableList("", outputFolder, files, singleRA, null);
					networks.add(temp);
				}
			}
		}

		for (Iterator<Network> it = networks.iterator(); it.hasNext();) {
			Network nw = it.next();
			if (nw.folder().contains("araba") && nw.routingAlgorithm() instanceof LookaheadSimple)
				it.remove();
			else if (nw.nodes() > 10000 && (nw.routingAlgorithm() instanceof DepthFirstEdgeGreedy))
				it.remove();
		}

		Network[] nwArray = new Network[networks.size()];
		networks.toArray(nwArray);
		System.out.println("Generating plots for " + nwArray.length + " networks");

		Config.overwrite("GNUPLOT_PATH", "C:\\Cygwin\\bin\\gnuplot.exe");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/GDAevaluation_RHDD/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/testsNico/");
		Config.overwrite("METRICS", "ROUTING, ROUTING_HOPDEGREEDISTRIBUTION");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);
		Config.overwrite("ROUTING_HOPDEGREEDISTRIBUTION_HOPS", "2");
		Config.overwrite("ROUTING_HOPDEGREEDISTRIBUTION_DISTRIBUTION_PLOT_LOGSCALE_X", ""+true);
		Config.overwrite("ROUTING_HOPDEGREEDISTRIBUTION_DISTRIBUTION_CDF_PLOT_LOGSCALE_X", ""+true);

		Series[] s = Series.generate(nwArray, lengthOfSeries);
		Plot.allMulti(s, "GDAEva-RHDD/");
	}
}

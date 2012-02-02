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
 * GDAEvaluation_Routing.java
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

import java.util.ArrayList;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.util.ReadableFolder;
import gtna.networks.util.ReadableList;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedy.GreedyBacktracking;
import gtna.routing.lookahead.Lookahead;
import gtna.routing.lookahead.LookaheadSequential;
import gtna.transformation.Transformation;
import gtna.transformation.lookahead.NeighborsFirstLookaheadList;
import gtna.util.Config;

/**
 * @author Nico
 * 
 */
public class GDAEvaluation_Routing {
	public static void main(String[] args) {
		int lengthOfSeries = 60;
		RoutingAlgorithm[] ra = new RoutingAlgorithm[] { new GreedyBacktracking(10), new Greedy(10), new LookaheadSequential(25) };
		String folderName;
		String rootFolder = "./resources/evaluation/";
		ArrayList<Network> networks = new ArrayList<Network>();

		String[] base = new String[] { "1000/barabasiAlbert-10-b-wcp-gcc-", "1000/erdosRenyi-10.0-true-b-wcp-gcc-",
		// "5000/barabasiAlbert-10-b-wcp-gcc-",
		// "5000/erdosRenyi-10.0-true-b-wcp-gcc-",
		// "10000/barabasiAlbert-10-b-wcp-gcc-",
		// "10000/erdosRenyi-10.0-true-b-wcp-gcc-",
		// "11407/SPI-b-wcp-gcc-",
		// "20057/CAIDA-b-wcp-gcc-",
		// "25487/WOT-b-wcp-gcc-"
		};
		String[] gdaFolders = new String[] { "CCC-1-100.0-true/", "fr-100-[100.0, 100.0]-false-100/",
				"st-1-100.0-true/", "stBFS-hd-bt-100.0-100.0/", "stBFS-hd-kBST-100.0-100.0/",
				"stBFS-hd-mh-100.0-100.0/", "stBFS-hd-ws-100.0-100.0/", "stBFS-rand-bt-100.0-100.0/",
				"stBFS-rand-kBST-100.0-100.0/", "stBFS-rand-mh-100.0-100.0/", "stBFS-rand-ws-100.0-100.0/", };

		String[] tempSplit;
		String[] files;
		Network temp;
		String outputFolder;
		Transformation[] nll = new Transformation[] { new NeighborsFirstLookaheadList(true) };

		for (RoutingAlgorithm singleRA : ra) {
			for (String singleBase : base) {
				for (String singleGDA : gdaFolders) {
					files = new String[lengthOfSeries];
					folderName = rootFolder + singleBase + singleGDA;
					tempSplit = folderName.split("/");
					outputFolder = tempSplit[tempSplit.length - 1];

					for (int i = 0; i < lengthOfSeries; i++) {
						files[i] = folderName + i + ".txt";
					}
					if (singleRA instanceof Lookahead) {
						temp = new ReadableList("", outputFolder, files, singleRA, nll);
					} else {
						temp = new ReadableList("", outputFolder, files, singleRA, null);
					}
					networks.add(temp);
				}
			}
		}
		Network[] nwArray = new Network[networks.size()];
		networks.toArray(nwArray);
		System.out.println("Generating plots for " + nwArray.length + " networks");

		// folderName =
		// "./resources/evaluation/5000/erdosRenyi-10.0-true-b-wcp-gcc-CCC-1-100.0-true/";
		// String[] files3 = new String[lengthOfSeries];
		// for (int i = 0; i < lengthOfSeries; i++) {
		// files2[i] = folderName + i + ".txt";
		// }
		// Network nw3 = new ReadableList("ER-5000-CCC", "evaluation", files2,
		// ra, null);

		Config.overwrite("MAIN_DATA_FOLDER", "./data/GDAevaluation/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/testsNico/");
		// Config.overwrite("GNUPLOT_PATH", "C:\\Cygwin\\bin\\gnuplot.exe");
		Config.overwrite("METRICS", "ROUTING");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);
		Config.overwrite("PARALLEL_ROUTINGS", "" + 4);

		Series[] s = Series.generate(nwArray, lengthOfSeries);
		Plot.allSingle(s, "GDAEva/");
	}
}

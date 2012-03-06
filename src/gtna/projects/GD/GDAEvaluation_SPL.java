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
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.plot.Plot;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.partition.GiantConnectedComponent;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Config;

/**
 * @author Nico
 * 
 */
public class GDAEvaluation_SPL {
	public static void main(String[] args) {
		int lengthOfSeries = 100;
		ArrayList<Network> networks = new ArrayList<Network>();

		Transformation[] stArray;
		stArray = new Transformation[] { new Bidirectional(), new WeakConnectivityPartition(),
				new GiantConnectedComponent() };
//		 networks.add(new ReadableFile("caida", "CAIDA",
//		 "./data/cycle-aslinks.l7.t1.c001749.20111206.txt.gtna",
//		 null, stArray));
//		 networks.add(new ReadableFile("wot", "WOT", "./data/graph-wot.txt",
//		 null, stArray));
//		 networks.add(new ReadableFile("spi", "SPI", "./data/graph-spi.txt",
//		 null, stArray));
		networks.add(new BarabasiAlbert(1000, 10, null, stArray));
		networks.add(new BarabasiAlbert(5000, 10, null, stArray));
		networks.add(new BarabasiAlbert(10000, 10, null, stArray));
		networks.add(new ErdosRenyi(1000, 10, true, null, stArray));
		networks.add(new ErdosRenyi(5000, 10, true, null, stArray));
		networks.add(new ErdosRenyi(10000, 10, true, null, stArray));
		
		Network[] nwArray = new Network[networks.size()];
		networks.toArray(nwArray);
		System.out.println("Generating plots for " + nwArray.length + " networks");		

		Config.overwrite("MAIN_DATA_FOLDER", "./data/GDAevaluation_SPL/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/testsNico_SPL/");
		// Config.overwrite("GNUPLOT_PATH", "C:\\Cygwin\\bin\\gnuplot.exe");
		Config.overwrite("METRICS", "SHORTEST_PATHS");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		Series[] s = Series.generate(nwArray, lengthOfSeries);
		Plot.allSingle(s, "GDAEva/");
	}
}

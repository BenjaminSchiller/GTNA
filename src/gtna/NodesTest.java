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
 * NodesTest.java
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
package gtna;

import gtna.data.Series;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableFolder;
import gtna.networks.util.ReadableList;
import gtna.plot.Plot;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class NodesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Config.overwrite("METRICS", "CC, DD, SP");
		// OLD VERSION:
		// CC, DD, RCC, SPL, NF_B_D, NF_B_DI, NF_B_DO, NF_B_R, NF_U_D, NF_U_DI,
		// NF_U_DO, NF_U_R
		Config.overwrite(
				"METRICS",
				"CC, DD, RCC, SPL, NF_B_D, NF_B_DI, NF_B_DO, NF_B_R, NF_U_D, NF_U_DI, NF_U_DO, NF_U_R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/malcolm/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/malcolm/");
		// Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + false);

		Network nw1 = new ReadableFile("Kademlia", "kademlia", "filename.txt",
				null, null);
		Network nw2 = new ReadableFolder("Kademlia", "kademlia", "fodlerName/",
				null, null);
		Network nw3 = new ReadableList("Kademlia", "kademlia", new String[] {
				"file1.txt", "file2.txt" }, null, null);

		// should only use times = 1, because we only have a single instance of
		// a graph input file
		Series s1 = Series.generate(nw1, 1);

		// times should equals the number of input graph files in the folder
		Series s2 = Series.generate(nw2, 10);

		// times should equal the inputFiles.length
		Series s3 = Series.generate(nw3, 17);

		Plot.multiAvg(s1, "series-1-kademlia-output-plot-folder/");
		
		
		
		Network er = new ErdosRenyi(100, 10, false, null, null);
		GraphWriter.write(er.generate(), "./data/malcom/er.txt");
		

		// Network nw1 = new ReadableFile("Name", "folder",
		// "./temp/kai/spi.txt",
		// null, null);
		// Network[] nw = new Network[] { nw1, nw2 };
		// Series s = Series.generate(nw1, 1);
		// Plot.allMulti(s, "multi-spi/");
		// Plot.allSingle(s, "singles-spi/");

		// Graph g1 = GraphReader.readOld("./temp/kai/2005-02-25.wot-bd.txt");
		// Graph g2 = GraphReader.readOld("./temp/kai/2005-02-25.wot-ud.txt");
		// Graph g3 = GraphReader.readOld("./temp/kai/2010-08.spi.txt");
		//
		// GraphWriter.write(g1, "./temp/kai/_2005-02-25.wot-bd.txt");
		// GraphWriter.write(g2, "./temp/kai/_2005-02-25.wot-ud.txt");
		// GraphWriter.write(g3, "./temp/kai/_2010-08.spi.txt");

		// Stats stats = new Stats();
		//
		// Network nw = new ErdosRenyi(3, 2, false, null, null);
		// Graph g = nw.generate();
		//
		// Transformation t1 = new RandomRingIDSpaceSimple(1, 1.0, true);
		// // Transformation t2 = new RandomLookaheadList();
		// // Transformation t3 = new RandomObfuscatedLookaheadList(0.001,
		// 0.002);
		//
		// System.out.println(t1.name());
		// // System.out.println(t2.name());
		// // System.out.println(t3.name());
		//
		// g = t1.transform(g);
		// // g = t2.transform(g);
		// // g = t3.transform(g);
		//
		// GraphWriter.writeWithProperties(g,
		// "./temp/test/randomObfuscated.txt");
		//
		// stats.end();
	}

}

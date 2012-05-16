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
 * CdaTest.java
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
package gtna.projects.communities;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.communities.Communities;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDFastUnfolding;
import gtna.transformation.communities.CDLPA;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class CdaTest {
	public static void main(String[] args) {
		Stats stats = new Stats();

		Config.overwrite("MAIN_DATA_FOLDER", "data/cda-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/cda-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		Metric c = new Communities();
		Metric[] metrics = new Metric[] { c };

		Transformation[] t1 = new Transformation[] { new CDLPA(50) };
		Transformation[] t2 = new Transformation[] { new CDFastUnfolding() };

		Transformation[][] t = new Transformation[][] { t1, t2 };

		int[] nodes = new int[] { 500, 600, 700, 800 };

		Network[][] nw = new Network[t.length * 2][nodes.length];
		int index = 0;
		for (Transformation[] T : t) {
			for (int i = 0; i < nodes.length; i++) {
				nw[index][i] = new ErdosRenyi(nodes[i], 5, true, T);
			}
			index++;
			for (int i = 0; i < nodes.length; i++) {
				nw[index][i] = new PowerLawRandomGraph(nodes[i], 2.3, 1,
						Integer.MAX_VALUE, false, T);
			}
			index++;
		}

		nw = new Network[][] { new Network[] {
				new ReadableFile("SPI", "spi", "resources/spi/_RLN_LWCC_BI/"
						+ "0_analyze_buddy_2010.csv.gtna", t1),
				new ReadableFile("SPI", "spi", "resources/spi/_RLN_LWCC_BI/"
						+ "0_analyze_buddy_2010.csv.gtna", t2) } };

		Series[][] s = Series.generate(nw, metrics, 1);

		Plotting.multi(s, metrics, "spi-multi/");
		Plotting.single(s, metrics, "spi-single/");

		stats.end();
	}
}

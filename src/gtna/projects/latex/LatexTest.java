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
 * Test.java
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
package gtna.projects.latex;

import gtna.data.Series;
import gtna.io.LaTex;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;

/**
 * @author benni
 * 
 */
public class LatexTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int nodes[] = new int[] { 100, 200, 300, 400 };
		Network[] nw1 = ErdosRenyi.get(nodes, 10, true, null);
		Network[] nw2 = ErdosRenyi.get(nodes, 20, true, null);
		Network[] nw3 = new Network[] { new BarabasiAlbert(nodes[0], 30, null),
				new BarabasiAlbert(nodes[1], 30, null) };
		Network[][] nw = new Network[][] { nw1, nw2, nw3 };

		Metric dd = new DegreeDistribution();
		Metric[] metrics = new Metric[] { dd };

		Series[][] s = Series.generate(nw, metrics, 2);

		LaTex.writeTables(s, metrics, "./data/", "blafasel");
	}
}

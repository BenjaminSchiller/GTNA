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
 * PETComputation.java
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
package gtna.projects;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;

/**
 * @author benni
 * 
 */
public class PETComputation {
	public static void diff(int[] Nodes, double[] Alpha, int[] C,
			RoutingAlgorithm[] R, PET.cutoffType type, String singleKey) {
		if (R.length != 2) {
			System.out.println("need two RAs");
			return;
		}
		RoutingAlgorithm r1 = R[0];
		RoutingAlgorithm r2 = R[1];
		System.out.println("\n\n\n\ndiff for " + singleKey);
		System.out.println("alpha / c : " + r1.nameShort() + " / "
				+ r2.nameShort());
		for (int c : C) {
			for (double alpha : Alpha) {
				System.out.println();
				for (int nodes : Nodes) {
					Network nw1 = PET.getSDR(nodes, alpha, type, c, r1);
					Network nw2 = PET.getSDR(nodes, alpha, type, c, r2);
					Series s1 = Series.get(nw1);
					Series s2 = Series.get(nw2);
					double v1 = s1.avgSingles().getValue(singleKey);
					double v2 = s2.avgSingles().getValue(singleKey);
					double v3 = (v2 - v1) / v2;
					System.out.println(nodes + "/" + alpha + "/" + c + " :	=> "
							+ v3);
				}
			}
		}
	}
}

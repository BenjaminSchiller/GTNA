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
package gtna.projects.pets;

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
		System.out.println("alpha / c : " + r1.getNameShort() + " / "
				+ r2.getNameShort());
		System.out.println("\\begin{tabular}{p{2cm} *{" + Nodes.length
				+ "}{l}}");
		for (int nodes : Nodes) {
			System.out.print(" & " + nodes);
		}
		System.out.println("\\\\");
		for (int c : C) {
			for (double alpha : Alpha) {
				System.out.print(alpha + "/" + c);
				for (int nodes : Nodes) {
					// Network nw1 = PET.getSDR(nodes, alpha, type, c, r1);
					// Network nw2 = PET.getSDR(nodes, alpha, type, c, r2);
					// Series s1 = Series.get(nw1);
					// Series s2 = Series.get(nw2);
					// double v1 = s1.avgSingles().getValue(singleKey);
					// double v2 = s2.avgSingles().getValue(singleKey);
					// double v3 = (v2 - v1) / v2;
					// System.out.println(nodes + "/" + alpha + "/" + c +
					// " :	=> "
					// + v3);
					// System.out.print(" & " + PETComputation.sub(v3));
				}
				System.out.println("\\\\");
			}
		}
		System.out.println("\n\\end{tabular}");
	}

	private static String sub(double v) {
		// return (-1.0 * v) + "";
		v = -1.0 * v;
		return String.format("%.5g", v);
		// v = Math.round(v * -100000.0) / 100000.0;
		// String bla = v + "";
		// bla = bla.replace("0.", ".");
		// bla = bla.substring(0, Math.min(bla.length() - 1, 5));
		// while (bla.length() < 6) {
		// bla = bla + "0";
		// }
		// return bla;
	}
}

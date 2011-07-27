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

import gtna.graph.Graph;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class NodesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] N = new int[] { 10000, 20000, 30000, 40000, 50000 };
		double[] D = new double[] { 10, 100, 1000 };
		for (double d : D) {
			System.out.println("\n");
			for (int n : N) {
				NodesTest.testER(n, d);
			}
		}
	}

	private static void testER(int n, double d) {
		Stats stats = new Stats();
		Network er = new ErdosRenyi(n, d, true, null, null);
		System.out.println("\n" + er.description());
		Graph g = er.generate();
		stats.end();
		er = null;
		g = null;
		for (int i = 0; i < 10; i++) {
			System.gc();
		}
	}

}

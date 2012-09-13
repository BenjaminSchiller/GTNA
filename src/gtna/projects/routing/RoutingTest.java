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
 * RoutingTest.java
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
package gtna.projects.routing;

import gtna.networks.Network;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;
import gtna.networks.util.DescriptionWrapper;
import gtna.transformation.Transformation;

import java.util.Map;

/**
 * @author benni
 * 
 */
public class RoutingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Config.overwrite("MAIN_DATA_FOLDER", "data/routing-test/");
		// Config.overwrite("MAIN_PLOT_FOLDER", "plots/routing-test/");
		// Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		//
		// int ttl = 50;
		// int rw = 6;
		// int[] R_STORAGE = new int[] { 1, 5, 10, 20, 30, 40, 50 };
		// R_STORAGE = new int[] { 1, 50 };
		// int rStorage = 10;
		// int rRouting = 1;
		//
		// int times = 1;
		// int nodes = 500;
		// boolean get = true;
		//
		// Transformation rrids = new RandomRingIDSpaceSimple();
		// Transformation lmc = new LMC(1000, LMC.MODE_UNRESTRICTED, 0,
		// LMC.DELTA_1_N, 0);
		// Transformation sw = new Swapping(1000);
		//
		// Map<Transformation[], String> names = new HashMap<Transformation[],
		// String>();
		//
		// Transformation[][] t1 = new Transformation[R_STORAGE.length][];
		// for (int i = 0; i < R_STORAGE.length; i++) {
		// Transformation mpds = new MultiPhaseDataStorage(R_STORAGE[i],
		// new RoutingAlgorithm[] { new RandomWalk(rw),
		// new HighestDegreeNeighbor(), new Greedy() });
		// t1[i] = new Transformation[] { rrids, lmc, mpds };
		// names.put(t1[i], "MultiPhase-R-D-G " + R_STORAGE[i]);
		// }
		//
		// Transformation[][] t2 = new Transformation[R_STORAGE.length][];
		// for (int i = 0; i < R_STORAGE.length; i++) {
		// Transformation mpds = new MultiPhaseDataStorage(R_STORAGE[i],
		// new RoutingAlgorithm[] { new RandomWalk(rw),
		// new HighestDegreeNeighbor(),
		// new LookaheadSimple(ttl) });
		// t2[i] = new Transformation[] { rrids, lmc, mpds };
		// names.put(t2[i], "MultiPhase-R-D-L " + R_STORAGE[i]);
		// }
		//
		// Transformation[][][] t = new Transformation[][][] { t1, t2 };
		//
		// Metric dd = new DegreeDistribution();
		// Metric dsm = new DataStorageMetric();
		// Routing r1 = new Routing(new Greedy(ttl));
		// Routing r2 = new Routing(new MultiPhaseRouting(rRouting,
		// new RoutingAlgorithm[] { new RandomWalk(rw),
		// new HighestDegreeNeighbor(), new Greedy(ttl) }));
		// Routing r3 = new Routing(
		// new MultiPhaseRouting(rRouting, new RoutingAlgorithm[] {
		// new RandomWalk(rw), new HighestDegreeNeighbor(),
		// new LookaheadSimple(ttl) }));
		// Routing r4 = new Routing(new RandomWalk(ttl));
		// Metric[] metrics = new Metric[] { dd, dsm, r1, r2, r3, r4 };
		//
		// Network[][] nw = RoutingTest.getNW(nodes, t, names);
		//
		// Series[][] s = get ? Series.get(nw, metrics) : Series.generate(nw,
		// metrics, times);
		//
		// Plotting.multi(s, metrics, "multi/");
		// Plotting.single(s, metrics, "single/");
	}

	public static Network getNW(int nodes, Transformation[] t,
			Map<Transformation[], String> names) {
		// String spi = "resources/spi/_RLN_LWCC_BI/"
		// + "0_analyze_buddy_2010.csv.gtna";
		// nw[i] = new DescriptionWrapper(new ReadableFile("SPI", "spi",
		// spi, t[i]), names.get(t[i]));
		return new DescriptionWrapper(new PowerLawRandomGraph(nodes, 2.3, 3,
				Integer.MAX_VALUE, true, t), names.get(t));
	}

	public static Network[] getNW(int nodes, Transformation[][] t,
			Map<Transformation[], String> names) {
		Network[] nw = new Network[t.length];
		for (int i = 0; i < t.length; i++) {
			nw[i] = RoutingTest.getNW(nodes, t[i], names);
		}
		return nw;
	}

	public static Network[][] getNW(int nodes, Transformation[][][] t,
			Map<Transformation[], String> names) {
		Network[][] nw = new Network[t.length][];
		for (int i = 0; i < t.length; i++) {
			nw[i] = RoutingTest.getNW(nodes, t[i], names);
		}
		return nw;
	}
}

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

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.routing.DataStorageMetric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.lookahead.LookaheadSimple;
import gtna.routing.multiPhase.MultiPhaseRouting;
import gtna.routing.random.RandomWalk;
import gtna.routing.util.HighestDegreeNeighbor;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.attackableEmbedding.swapping.Swapping;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.id.data.MultiPhaseDataStorage;
import gtna.transformation.id.data.RandomDataStorage;
import gtna.util.Config;

import java.util.HashMap;
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

		Config.overwrite("MAIN_DATA_FOLDER", "data/routing-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/routing-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		int ttl = 100;
		int rw = 5;
		int rStorage = 10;
		int rRouting = 10;

		int times = 1;
		int nodes = 2000;

		Metric dd = new DegreeDistribution();
		Metric dsm = new DataStorageMetric();
		Routing r1 = new Routing(new Greedy(ttl));
		Routing r2 = new Routing(new MultiPhaseRouting(rRouting,
				new RoutingAlgorithm[] { new RandomWalk(rw),
						new HighestDegreeNeighbor(), new Greedy(ttl) }));
		Routing r3 = new Routing(
				new MultiPhaseRouting(rRouting, new RoutingAlgorithm[] {
						new RandomWalk(rw), new HighestDegreeNeighbor(),
						new LookaheadSimple(ttl) }));
		Routing r4 = new Routing(new RandomWalk(ttl));
		Metric[] metrics = new Metric[] { dd, dsm, r1, r2, r3, r4 };

		Transformation rrids = new RandomRingIDSpaceSimple();
		Transformation lmc = new LMC(1000, LMC.MODE_UNRESTRICTED, 0,
				LMC.DELTA_1_N, 0);
		Transformation sw = new Swapping(1000);

		Transformation mpds_rdg = new MultiPhaseDataStorage(rStorage,
				new RoutingAlgorithm[] { new RandomWalk(rw),
						new HighestDegreeNeighbor(), new Greedy() });
		Transformation mpds_rdl = new MultiPhaseDataStorage(rStorage,
				new RoutingAlgorithm[] { new RandomWalk(rw),
						new HighestDegreeNeighbor(), new LookaheadSimple(ttl) });

		Transformation[] t2 = new Transformation[] { rrids, lmc, mpds_rdg };
		Transformation[] t3 = new Transformation[] { rrids, lmc, mpds_rdl };

		Transformation[][] t = new Transformation[][] { t2, t3 };

		Map<Transformation[], String> names = new HashMap<Transformation[], String>();
		names.put(t2, "MultiPhase-R-D-G");
		names.put(t3, "MultiPhase-R-D-L");

		String spi = "resources/spi/_RLN_LWCC_BI/"
				+ "0_analyze_buddy_2010.csv.gtna";
		Network[] nw = new Network[t.length];
		for (int i = 0; i < t.length; i++) {
			// nw[i] = new DescriptionWrapper(new ReadableFile("SPI", "spi",
			// spi,
			// t[i]), names.get(t[i]));
			nw[i] = new DescriptionWrapper(new PowerLawRandomGraph(nodes, 2.3,
					3, Integer.MAX_VALUE, true, t[i]), names.get(t[i]));
		}

		Series[] s = Series.generate(nw, metrics, times);

		Plotting.multi(s, metrics, "multi/");
	}
}

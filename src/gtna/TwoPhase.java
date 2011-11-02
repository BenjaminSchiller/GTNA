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
 * TwoPhase.java
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
import gtna.graph.Graph;
import gtna.graph.spanningTree.ParentChild;
import gtna.graph.spanningTree.SpanningTree;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plot;
import gtna.routing.greedy.Greedy;
import gtna.routing.lookahead.LookaheadMinVia;
import gtna.routing.twoPhase.TwoPhaseGreedy;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CommunityGeneration;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpaceSimpleCommunities;
import gtna.transformation.lookahead.NeighborsFirstLookaheadList;
import gtna.transformation.storage.TwoPhaseStorage;
import gtna.util.Config;
import gtna.util.Stats;

import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class TwoPhase {
	public static void main(String args[]) {
		// TwoPhase.routing();
		TwoPhase.testSpanningTree();
	}

	public static void testSpanningTree() {
		Network nw = new ErdosRenyi(10, 5, true, null, null);
		Graph g = nw.generate();
		ArrayList<ParentChild> pcs = new ArrayList<ParentChild>();
		for (int i = 0; i < g.getNodes().length - 1; i++) {
			pcs.add(new ParentChild(i, i + 1));
		}
		SpanningTree st = new SpanningTree(g, pcs);
		st.write("./temp/test/spanningTree.txt", "key");
		SpanningTree st2 = new SpanningTree();
		st2.read("./temp/test/spanningTree.txt", g);
		st2.write("./temp/test/spanningTree2.txt", "key");
	}

	public static void routing() {
		Stats stats = new Stats();

		Config.overwrite("METRICS", "R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/twoPhaseGreedy/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/twoPhaseGreedy/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + false);

		boolean uniform = false;
		boolean bidirectional = true;
		String spi = "./temp/kai/spi.txt";
		Transformation ids = new RandomRingIDSpaceSimple();
		ids = new RandomRingIDSpaceSimpleCommunities();
		Transformation c = new CommunityGeneration();

		Network nw1 = new ReadableFile("SPI", "spi", spi, new Greedy(),
				new Transformation[] { c, ids });
		Network nw2 = new ReadableFile("SPI", "spi", spi, new TwoPhaseGreedy(),
				new Transformation[] { c, ids, new TwoPhaseStorage(false) });
		Network nw3 = new ReadableFile("SPI", "spi", spi, new TwoPhaseGreedy(),
				new Transformation[] { c, ids, new TwoPhaseStorage(true) });
		Network nw4 = new ReadableFile("SPI", "spi", spi,
				new LookaheadMinVia(), new Transformation[] { c, ids,
						new NeighborsFirstLookaheadList(true) });
		// Network nw1 = new ErdosRenyi(1000, 20, bidirectional, new Greedy(),
		// new Transformation[] { new RandomRingIDSpaceSimple() });
		// Network nw2 = new ErdosRenyi(1000, 20, bidirectional,
		// new TwoPhaseGreedy(), new Transformation[] {
		// new RandomRingIDSpaceSimple(),
		// new TwoPhaseStorage(false) });
		// Network nw3 = new ErdosRenyi(1000, 20, bidirectional,
		// new TwoPhaseGreedy(), new Transformation[] {
		// new RandomRingIDSpaceSimple(),
		// new TwoPhaseStorage(true) });
		// Network nw1 = new Chord(1000, 20, uniform, new Greedy(), null);
		// Network nw2 = new Chord(1000, 20, uniform, new TwoPhaseGreedy(),
		// new Transformation[] { new TwoPhaseStorage(false) });
		// Network nw3 = new Chord(1000, 20, uniform, new TwoPhaseGreedy(),
		// new Transformation[] { new TwoPhaseStorage(true) });

		Series[] s = Series.generate(new Network[] { nw1, nw2, nw3, nw4 }, 1);
		Plot.multiAvg(s, "plane/");

		stats.end();
	}
}

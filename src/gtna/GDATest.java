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
 * GDATest.java
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
package gtna;

import java.util.ArrayList;

import gtna.data.*;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.spanningTree.ParentChild;
import gtna.graph.spanningTree.SpanningTree;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDPartitionSimple;
import gtna.id.ring.RingIdentifierSpace;
import gtna.io.GraphReader;
import gtna.io.GraphWriter;
import gtna.metrics.EdgeCrossings;
import gtna.networks.*;
import gtna.networks.model.*;
import gtna.plot.*;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.*;
import gtna.routing.lookahead.*;
import gtna.transformation.*;
import gtna.transformation.gd.*;
import gtna.transformation.id.*;
import gtna.transformation.lookahead.*;
import gtna.transformation.spanningtree.BFS;
import gtna.util.*;

/**
 * @author Nico
 * 
 */
public class GDATest {

	public static void main(String[] args) {
		Stats stats = new Stats();

		boolean generate = false;
		int times = 1;
		boolean skipExistingFolders = false;

		Config.overwrite("METRICS", "R");
		// R for routing
		Config.overwrite("MAIN_DATA_FOLDER", "./data/testsNico/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/testsNico/");
		Config.overwrite("GNUPLOT_PATH", "C:\\Cygwin\\bin\\gnuplot.exe");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skipExistingFolders);

		// Lookahead.testLookahead(generate, times);
		// GDATest.testRandomize();
		// GDATest.testMD();
//		 GDATest.randomFRTest();
		// GDATest.randomFRTest_multidimensional();
		// GDATest.randomFRTestWithSeperateIDSpaceTransformation();
		// GDATest.randomFrickTest();
//		 GDATest.testWS();
		// GDATest.testMH();
		// GDATest.canonicalCircularCrossingWithSeperateIDSpace();
		// GDATest.testMHRandom();
		// GDATest.canonicalCircularCrossing();
		// GDATest.testSpanningTree();
		// GDATest.testSpanningTree_Benni();
		// GDATest.testSpanningTree_transform();
		// GDATest.routingTest();
//		 GDATest.testKnuth();
//		 GDATest.routingKnuthAgainstWS();
		// GDATest.routingWS_BFSChange();
		 GDATest.testBT();
		// GDATest.routingBT_BFSChange();
		// GDATest.routingTestCCCAgainstRandom();
//		GDATest.randomSTTest();
//		GDATest.timeMeasurements();
//		 GDATest.compareKnuthWS();

		stats.end();
	}

	public static void routingTest() {
		Config.overwrite("METRICS", "DD, SP, R, EC");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		Transformation[] t1 = new Transformation[] { new RandomMDIDSpaceSimple(1, new double[] { 100, 100 }, true) };
		RoutingAlgorithm r1 = new GreedyBacktracking();
		Transformation[] t2 = new Transformation[] { new FruchtermanReingold(1, new double[] { 100, 100 }, true, 70,
				null) };
		Network nw1 = new ErdosRenyi(100, 7.0, true, r1, t1);
		Network nw2 = new ErdosRenyi(100, 7.0, true, r1, t2);
		Series[] s = Series.generate(new Network[] { nw1, nw2 }, 5);
		// Series s = Series.generate(nw1, 10);
		// Series s = Series.get(nw);

		Plot.allMulti(s, "multi/");
	}

	public static void routingTestCCCAgainstRandom() {
		Config.overwrite("METRICS", "SP, R, EC");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		Transformation[] t1 = new Transformation[] { new RandomRingIDSpace(1, 100, true) };
		RoutingAlgorithm r1 = new GreedyBacktracking();
		Transformation[] t2 = new Transformation[] { new CanonicalCircularCrossing(1, 100, true, null) };
		RoutingAlgorithm r2 = new GreedyBacktracking();
		Network nw1 = new ErdosRenyi(70, 25, true, r1, t1);
		Network nw2 = new ErdosRenyi(70, 25, true, r2, t2);
		Series[] s = Series.generate(new Network[] { nw1, nw2 }, 25);
		// Series s = Series.generate(nw1, 3);
		// Series s = Series.get(nw);

		Plot.allMulti(s, "multi/");
		// Plot.allSingle(s, "single/");
	}

	public static void routingKnuthAgainstWS() {
		Config.overwrite("METRICS", "SP, R");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		RoutingAlgorithm r1 = new GreedyBacktracking(24);
		Transformation[] t1 = new Transformation[] { new BFS("hd"), new Knuth(500, 500, null) };
		Transformation[] t2 = new Transformation[] { new BFS("hd"), new WetherellShannon(500, 500, null) };
		Network nw1 = new ErdosRenyi(5000, 20, true, r1, t1);
		Network nw2 = new ErdosRenyi(5000, 20, true, r1, t2);
		Series[] s = Series.generate(new Network[] { nw1, nw2 }, 50);
		// Series s = Series.generate(nw1, 3);
		// Series s = Series.get(nw);

		Plot.allMulti(s, "multi/");
		// Plot.allSingle(s, "single/");
	}

	public static void routingWS_BFSChange() {
		Config.overwrite("METRICS", "SP, R, EC");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		Transformation[] t1 = new Transformation[] { new BFS("hd"), new WetherellShannon(500, 500, null) };
		Transformation[] t2 = new Transformation[] { new BFS("zero"), new WetherellShannon(500, 500, null) };
		Transformation[] t3 = new Transformation[] { new BFS("rand"), new WetherellShannon(500, 500, null) };
		RoutingAlgorithm r1 = new GreedyBacktracking(20);
		int nodes = 100;
		int avgDegree = 12;
		Network nw1 = new ErdosRenyi(nodes, avgDegree, true, r1, t1);
		Network nw2 = new ErdosRenyi(nodes, avgDegree, true, r1, t2);
		Network nw3 = new ErdosRenyi(nodes, avgDegree, true, r1, t3);
		Series[] s = Series.generate(new Network[] { nw1, nw2, nw3 }, 15);
		// Series s = Series.generate(nw1, 3);
		// Series s = Series.get(nw);

		Plot.allMulti(s, "multi/");
		// Plot.allSingle(s, "single/");
	}

	public static void routingBT_BFSChange() {
		Config.overwrite("METRICS", "SP, R");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		Transformation[] t1 = new Transformation[] { new BFS("hd"), new BubbleTree(500, 500, null) };
		Transformation[] t2 = new Transformation[] { new BFS("zero"), new BubbleTree(500, 500, null) };
		Transformation[] t3 = new Transformation[] { new BFS("rand"), new BubbleTree(500, 500, null) };
		Transformation[] t4 = new Transformation[] { new BFS("hd"), new MelanconHerman(500, 500, null) };
		Transformation[] t5 = new Transformation[] { new BFS("zero"), new MelanconHerman(500, 500, null) };
		Transformation[] t6 = new Transformation[] { new BFS("rand"), new MelanconHerman(500, 500, null) };
		RoutingAlgorithm r1 = new GreedyBacktracking(13);

		int graphSize = 700;
		int avgDegree = 19;

		Network nw1 = new ErdosRenyi(graphSize, avgDegree, true, r1, t1);
		Network nw2 = new ErdosRenyi(graphSize, avgDegree, true, r1, t2);
		Network nw3 = new ErdosRenyi(graphSize, avgDegree, true, r1, t3);
		Network nw4 = new ErdosRenyi(graphSize, avgDegree, true, r1, t4);
		Network nw5 = new ErdosRenyi(graphSize, avgDegree, true, r1, t5);
		Network nw6 = new ErdosRenyi(graphSize, avgDegree, true, r1, t6);
		Series[] s = Series.generate(new Network[] { nw1, nw3, nw4, nw6 }, 50);
		// Series s = Series.generate(nw1, 3);
		// Series s = Series.get(nw);

		Plot.allMulti(s, "multi/");
		// Plot.allSingle(s, "single/");
	}

	/**
	 * 
	 */
	private static void canonicalCircularCrossing() {
		Network nw1 = new ErdosRenyi(8, 3, true, new Greedy(), null);
		Graph g = nw1.generate();
		// g = GraphReader.readWithProperties("./data/testsNico/testCCC.txt");

		Transformation ccc = new CanonicalCircularCrossing(1, 250, false, new GraphPlotter("testCCC", "svg"));

		if (!ccc.applicable(g))
			throw new RuntimeException("Oh no, can not use CCC on g");
		g = ccc.transform(g);
		// GraphWriter.writeWithProperties(g, "./data/testsNico/testCCC.txt");
	}

	private static void canonicalCircularCrossingWithSeperateIDSpace() {
		Network nw1 = new ErdosRenyi(100, 15, true, new Greedy(), null);
		Graph g = nw1.generate();
		Transformation ringIDSpace = new RandomRingIDSpace(1, 250, false);
		g = ringIDSpace.transform(g);
		RingIdentifierSpace idSpace = (RingIdentifierSpace) g.getProperty("ID_SPACE_0");

		// g = GraphReader.readWithProperties("./data/testsNico/testCCC.txt");

		CanonicalCircularCrossing ccc = new CanonicalCircularCrossing(1, 250, false, new GraphPlotter("testCCC", "svg"));
		ccc.setIDSpace(idSpace);

		if (!ccc.applicable(g))
			throw new RuntimeException("Oh no, can not use CCC on g");
		g = ccc.transform(g);
		GraphWriter.writeWithProperties(g, "./data/testsNico/testCCC_seperateIDSpace.txt");
	}

	public static void testMH() {
		Network nw1 = new ErdosRenyi(10, 7, true, new Greedy(), null);
		Graph g = nw1.generate();
		g = GraphReader.readWithProperties("./data/testsNico/testMH.txt");

		Transformation bfs = new BFS("hd");
		g = bfs.transform(g);
		Transformation ws = new MelanconHerman(122, 122, new GraphPlotter("testMH", "svg"));
		if (!ws.applicable(g))
			throw new RuntimeException("Oh no, can not use MH on g");
		g = ws.transform(g);
		GraphWriter.writeWithProperties(g, "./data/testsNico/testMH.txt");
	}

	public static void testBT() {
		Network nw1 = new ErdosRenyi(20, 7, true, new Greedy(), null);
		Graph g = nw1.generate();

		Transformation bfs = new BFS("hd");
		g = bfs.transform(g);
		Transformation ws = new BubbleTree(122, 122, new GraphPlotter("testBT", "svg"));
		if (!ws.applicable(g))
			throw new RuntimeException("Oh no, can not use BT on g");
		g = ws.transform(g);
	}

	public static void testMHRandom() {
		Network nw1 = new ErdosRenyi(120, 22, true, new Greedy(), null);
		Graph g = nw1.generate();
		Transformation bfs = new BFS("hd");
		g = bfs.transform(g);
		Transformation ws = new MelanconHerman(122, 122, new GraphPlotter("testMH-random", "svg"));
		if (!ws.applicable(g))
			throw new RuntimeException("Oh no, can not use WS on g");
		g = ws.transform(g);
		GraphWriter.writeWithProperties(g, "./data/testsNico/testMH.txt");
	}

	public static void testWS() {
		Network nw1 = new ErdosRenyi(120, 10, true, new Greedy(), null);
		Graph g = nw1.generate();
		// GraphWriter.writeWithProperties(g, "./data/testsNico/testWS.txt");
		// g = GraphReader.readWithProperties("./data/testsNico/testWS.txt");

		Transformation bfs = new BFS("hd");
		g = bfs.transform(g);
		Transformation ws = new WetherellShannon(200, 200, new GraphPlotter("testWS", "svg"));
		if (!ws.applicable(g))
			throw new RuntimeException("Oh no, can not use WS on g");
		g = ws.transform(g);
	}

	public static void testKnuth() {
		Network nw1 = new ErdosRenyi(15, 18, true, new Greedy(), null);
		Graph g = nw1.generate();
		// GraphWriter.writeWithProperties(g, "./data/testsNico/testWS.txt");
		// g = GraphReader.readWithProperties("./data/testsNico/testWS.txt");

		Transformation bfs = new BFS("hd");
		g = bfs.transform(g);
		Transformation knuth = new Knuth(250, 250, new GraphPlotter("testKN", "svg"));
		if (!knuth.applicable(g))
			throw new RuntimeException("Oh no, can not use KN on g");
		g = knuth.transform(g);
	}

	public static void compareKnuthWS() {
		Network nw1 = new ErdosRenyi(15, 6, true, new Greedy(), null);
		Graph g = nw1.generate();
		Transformation bfs = new BFS("hd");
		g = bfs.transform(g);
		Transformation knuth = new Knuth(50, 50, new GraphPlotter("compareKnuthWS-KN", "pdf"));
		g = knuth.transform(g);
		Transformation ws = new WetherellShannon(50, 50, new GraphPlotter("compareKnuthWS-WS", "pdf"));
		g = ws.transform(g);
	}
	
	public static void testSpanningTree() {
		GraphPlotter plotter = new GraphPlotter("testSpanningTree-FR", "svg", 60);

		Network nw1 = new ErdosRenyi(50, 10, true, new Greedy(), null);

		Graph g = nw1.generate();
		System.out.println(g);

		for (Node i : g.getNodes()) {
			if (i == null) {
				System.out.println("Missing node");
				continue;
			}
			System.out.print("Node " + i.getIndex() + " has edges to: ");
			for (int j : i.getOutgoingEdges())
				System.out.print(j + " ");
			System.out.println();
		}

		Transformation fr = new FruchtermanReingold(1, new double[] { 60, 60 }, false, 300, plotter);
		g = fr.transform(g);
	}

	public static void randomFRTest() {
		GraphPlotter plotter = new GraphPlotter("randomFR", "svg", 500);
		plotter = null;

		Network nw1 = new ErdosRenyi(20, 7, true, new Greedy(), new Transformation[] { new FruchtermanReingold(1,
				new double[] { 100, 100 }, false, 10, plotter) });

		Graph g = nw1.generate();
		Transformation[] t = nw1.transformations();
		for (int j = 0; j < t.length; j++) {
			if (t[j].applicable(g)) {
				System.out.println("Apply >>" + t[j].getClass() + "<<");
				g = t[j].transform(g);
			} else
				System.out.println("Cannot apply " + t[j].getClass());
		}
		EdgeCrossings ec = new EdgeCrossings();
		ec.computeData(g, nw1, null);
		ec.writeData("./data/testsNico/testFRTest-ecDist/");
		GraphWriter.writeWithProperties(g, "./data/testsNico/testFRTest.txt");
	}

	private static void printGraph(Graph g) {
		MDPartitionSimple partI, partJ;
		ArrayList<Integer> edgesList;
		MDIdentifierSpaceSimple idSpace = (MDIdentifierSpaceSimple) g.getProperty("ID_SPACE_0");
		MDPartitionSimple[] p = (MDPartitionSimple[]) idSpace.getPartitions();

		for (Node i : g.getNodes()) {
			partI = p[i.getIndex()];
			System.out.println("\nDoing " + i.getIndex() + " " + partI + " now");

			edgesList = new ArrayList<Integer>();
			for (int e : i.getIncomingEdges())
				edgesList.add(e);

			for (Node j : g.getNodes()) {
				if (j.getIndex() <= i.getIndex())
					continue;
				partJ = p[j.getIndex()];
				double distance = partI.distance(partJ.getId());
				System.out.print("Distance to ");
				if (edgesList.contains(j.getIndex()))
					System.out.print("   ");
				else
					System.out.print("non");
				System.out.println("adjacent " + partJ + " [" + j.getIndex() + "]: " + distance);
			}
		}
	}

	public static void randomFRTest_multidimensional() {
		GraphPlotter plotter = null;
		// plotter = new GraphPlotter("randomFR-md", "svg", 20);

		Network nw1 = new ErdosRenyi(100, 16, true, new Greedy(), null);

		Graph g = nw1.generate();
		System.out.println("Initial graph\n------");
		// printGraph(g);

		Transformation fr = new FruchtermanReingold(1, new double[] { 30, 40, 50, 60 }, false, 100, plotter);
		g = fr.transform(g);
		printGraph(g);
	}

	public static void randomFrickTest() {
		GraphPlotter plotter = new GraphPlotter("randomFrick", "svg", 20);

		Network nw1 = new ErdosRenyi(20, 6, true, new Greedy(), null);

		Graph g = nw1.generate();
		GraphWriter.writeWithProperties(g, "./data/testsNico/randomFrick.txt");
		g = GraphReader.readWithProperties("./data/testsNico/randomFrick.txt");
		Transformation frick = new Frick(1, new double[] { 200, 200 }, false, plotter);
		g = frick.transform(g);
	}

	public static void timeMeasurements() {
		Transformation[] t = new Transformation[]{
//				new Knuth(100, 100, null),
//				new WetherellShannon(100, 100, null),
//				new MelanconHerman(100, 100, null),
//				new BubbleTree(100, 100, null),
//				new CanonicalCircularCrossing(1, 500, true, null),
//				new SixTollis(1, 500, true, null),
				new FruchtermanReingold(1, new double[]{100,100}, false, 100, null)
		};
		Network nw;
		Graph g;
		BFS bfs = new BFS("hd");

		for (Transformation sT : t) {
			System.out.println("Starting " + sT.name());
			double runtimeComplete = 0;
			int iterations = 10;
			for (int i = 0; i < iterations; i++) {
				double thisIterationStart = System.currentTimeMillis();
				nw = new ErdosRenyi(2000, 17, true, null, null);
				g = nw.generate();
				if ( sT instanceof HierarchicalAbstract ) {
					g = bfs.transform(g);
				}
				g = sT.transform(g);
				double thisIterationDuration = System.currentTimeMillis() - thisIterationStart;
				System.out.println("Did loop " + (i + 1) + " of " + iterations + " in " + thisIterationDuration);
				runtimeComplete += (thisIterationDuration);
			}
			System.out.println("Complete runtime: " + runtimeComplete + " msec");
			System.out.println("Per graph / transformation: " + (runtimeComplete / iterations) + " msec\n");
		}
	}

	public static void randomSTTest() {
		Config.overwrite("GEPHI_RING_RADIUS", "100");
		Config.overwrite("GEPHI_EDGE_SCALE", "0.4");
		GraphPlotter plotter = new GraphPlotter("randomST", "pdf");
//		plotter = null;
		Network nw1 = new ErdosRenyi(20, 5, true, null, null);

		Graph g = nw1.generate();
		// System.out.println("# Edges: " + g.generateEdges().length);
		//
		// for ( Node n: g.getNodes() ) {
		// System.out.print("\nNode " + n.getIndex() + " has edges to");
		// for (int m: n.getOutgoingEdges()) {
		// System.out.print(" " + m);
		// }
		// }

		SixTollis st = new SixTollis(1, 50, false, plotter);
		g = st.transform(g);
	}

	public static void testMD() {
		GraphPlotter plotter = new GraphPlotter("testMD", "svg", 60);

		// Network nw1 = new ErdosRenyi(8, 5, true,
		// new Greedy(), new Transformation[] {
		// new RandomMDIDSpaceSimple(1, new double[]{40,40},false) });
		//
		// Network nw2 = new ErdosRenyi(2000, 10, true,
		// new Greedy(), new Transformation[] {
		// new RandomMDIDSpaceSimple(1, new double[]{10,10,10,10},false), new
		// RandomLookaheadList() });
		//
		// Graph g = nw1.generate();
		// Transformation[] t = nw1.transformations();
		// for (int j = 0; j < t.length; j++) {
		// if (t[j].applicable(g)) {
		// System.out.println("Apply >>" + t[j].getClass() + "<<");
		// if (
		// t[j].getClass().toString().equals("class gtna.transformation.gd.FruchtermanReingold"))
		// plotter.Plot ( g, "testVorher.svg" );
		// g = t[j].transform(g);
		// } else System.out.println("Cannot apply " + t[j].getClass());
		// }
		//
		// GraphWriter.writeWithProperties(g, "./data/testsNico/test.txt");
		Graph g = GraphReader.readWithProperties("./data/testsNico/test.txt");
		System.out.println("Wrote it, we're save");

		Transformation fr = new FruchtermanReingold(1, new double[] { 40, 40 }, false, 100, plotter);
		g = fr.transform(g);

		// Series s1 = Series.generate(nw1, 2);
		// Series s2 = Series.generate(nw2, 2);
		// Plot.multiAvg(new Series[] { s1, s2 }, "testRandom/");
		// Plot.singlesAvg(new Series[][] { new Series[] { s1 },
		// new Series[] { s2 } }, "testRandom/");

		// GraphWriter.writeWithProperties(g, "./data/testsNico/test.txt");
		// Graph secondG =
		// GraphReader.readWithProperties("./data/testsNico/test.txt");
		Node first = g.getNode(0);

		// plotter.Plot ( secondG, "testNachher.svg" );

		// MDIdentifierSpaceSimple idSpace = (MDIdentifierSpaceSimple)
		// g.getProperty("ID_SPACE_0");
		// MDPartitionSimple[] p = (MDPartitionSimple[])
		// idSpace.getPartitions();
		//
		// for ( Node n: g.getNodes() ) {
		// MDPartitionSimple firstP = p[first.getIndex()];
		// MDPartitionSimple nP = p[n.getIndex()];
		// System.out.println("Distance between " + firstP + " and " + nP + ": "
		// + firstP.distance(nP.getRepresentativeID()));
		// }

		// Gephi plotter = new Gephi();
		// plotter.Plot ( g, "testMD.svg" );

	}

	public static void testBroken() {
		// Network nw1 = new ErdosRenyi(200, 10, true,
		// new Greedy(), new Transformation[] {
		// new RandomMDIDSpaceSimple(1, new double[]{50,50},false), new
		// RandomLookaheadList() });
		//
		// Graph g = nw1.generate();
		// Transformation[] t = nw1.transformations();
		// for (int j = 0; j < t.length; j++) {
		// if (t[j].applicable(g)) {
		// System.out.println("Apply " + t[j].getClass());
		// g = t[j].transform(g);
		// } else System.out.println("Cannot apply " + t[j].getClass());
		// }
		// /*
		// * This won't work until we have added support for MD*
		// * into ObfuscatedLookaheadList!
		// */
		// GraphWriter.writeWithProperties(g, "./data/testsNico/test.txt");
	}

	public static void testRandomize() {
		Network nw1 = new ErdosRenyi(100, 10, true, new Greedy(), new Transformation[] {
				new RandomPlaneIDSpaceSimple(1, 100, 100, false), new RandomLookaheadList() });

		Network nw2 = new ErdosRenyi(50, 4, true, new LookaheadSequential(50), new Transformation[] {
				new RandomRingIDSpace(), new RandomLookaheadList() });

		Graph g = nw2.generate();
		Transformation[] t = nw2.transformations();
		for (int j = 0; j < t.length; j++) {
			if (t[j].applicable(g)) {
				System.out.println("Apply " + t[j].getClass());
				g = t[j].transform(g);
			} else
				System.out.println("Cannot apply " + t[j].getClass());
		}
		GraphPlotter plotter = new GraphPlotter("testRandom", "svg");
		plotter.plot(g, (RingIdentifierSpace) g.getProperty("ID_SPACE"), "test");

		Series s1 = Series.generate(nw1, 2);
		Series s2 = Series.generate(nw2, 2);
		Plot.multiAvg(new Series[] { s1, s2 }, "testRandom/");
		Plot.singlesAvg(new Series[][] { new Series[] { s1 }, new Series[] { s2 } }, "testRandom/");

	}

	public static void testSpanningTree_transform() {
		Network nw = new ErdosRenyi(15, 8, true, null, null);
		Graph g = nw.generate();
		BFS trans = new BFS("hd");
		g = trans.transform(g);
		GraphWriter.writeWithProperties(g, "./data/testsNico/testSpanningTreeTransform.txt");
	}

	public static void testSpanningTree_Benni() {
		Network nw = new ErdosRenyi(15, 5, true, null, null);
		Graph g = nw.generate();
		GraphWriter.write(g, "./temp/test/spanningTree-graph.txt");
		ArrayList<ParentChild> pcs = new ArrayList<ParentChild>();
		for (int i = 0; i < g.getNodes().length - 1; i++) {
			pcs.add(new ParentChild(i, i + 1, 0));
		}
		SpanningTree st = new SpanningTree(g, pcs);
		st.write("./temp/test/spanningTree.txt", "key");
		SpanningTree st2 = new SpanningTree();
		st2.read("./temp/test/spanningTree.txt", g);
		st2.write("./temp/test/spanningTree2.txt", "key");
	}
}
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
 * Evaluation.java
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import gtna.graph.Graph;
import gtna.io.Filewriter;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.ReadableFile;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedy.GreedyBacktracking;
import gtna.routing.lookahead.LookaheadSequential;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.gd.*;
import gtna.transformation.partition.GiantConnectedComponent;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.transformation.spanningtree.BFS;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author Nico
 * 
 */
public class GDAEvaluation {
	public static void main(String[] args) {

		int times = 60;
		int threads = 10;
		int sizeFactor = 1000;
		int degree = 10;

		GDAEvaluation gdae = new GDAEvaluation(times, threads, sizeFactor, degree);
	}

	public GDAEvaluation(int times, int threads, int sizeFactor, int degree) {
		Stats stats = new Stats();
		ProgressMonitor pm = new ProgressMonitor();

		Config.overwrite("METRICS", "ROUTING");
		// R for routing
		Config.overwrite("MAIN_DATA_FOLDER", "./data/GDAevaluation/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/GDAevaluation/");
		Config.overwrite("GNUPLOT_PATH", "C:\\Cygwin\\bin\\gnuplot.exe");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		String bfsRoot;
		Transformation[] sTArray;
		Network nw;
		HashMap<String, Integer> lastCounter = new HashMap<String, Integer>();

		ArrayList<NetworkTask> todoList = new ArrayList<NetworkTask>();
		RoutingAlgorithm[] rA = new RoutingAlgorithm[] { new Greedy(25), new GreedyBacktracking(25),
				new LookaheadSequential(25) };
		GraphDrawingAbstract[] t = new GraphDrawingAbstract[] { new CanonicalCircularCrossing(1, 100, true, null),
				new SixTollis(1, 100, true, null), new WetherellShannon(100, 100, null), new Knuth(100, 100, null),
				new MelanconHerman(100, 100, null), new BubbleTree(100, 100, null),
				new FruchtermanReingold(1, new double[] { 100, 100 }, false, 100, null) };
		for (GraphDrawingAbstract originalT : t) {
			if (originalT instanceof HierarchicalAbstract) {
				times = times * 2;
			}
			bfsRoot = "hd";
			for (int j = 0; j < times; j++) {
				if (originalT instanceof HierarchicalAbstract && j == times / 2) {
					bfsRoot = "rand";
				}

				sTArray = getTransformations(originalT, bfsRoot);
				Network caida = new ReadableFile("caida", "CAIDA",
						"./data/cycle-aslinks.l7.t1.c001749.20111206.txt.gtna", null, sTArray);
				checkAndAdd(caida, lastCounter, todoList);

				sTArray = getTransformations(originalT, bfsRoot);
				Network wot = new ReadableFile("wot", "WOT", "./data/graph-wot.txt", null, sTArray);
				checkAndAdd(wot, lastCounter, todoList);

				sTArray = getTransformations(originalT, bfsRoot);
				Network spi = new ReadableFile("spi", "SPI", "./data/graph-spi.txt", null, sTArray);
				checkAndAdd(spi, lastCounter, todoList);

				for (int i : new int[] { 1, 5, 10 }) {
					sTArray = getTransformations(originalT, bfsRoot);
					nw = new ErdosRenyi(i * sizeFactor, degree, true, null, sTArray);
					checkAndAdd(nw, lastCounter, todoList);

					sTArray = getTransformations(originalT, bfsRoot);
					nw = new BarabasiAlbert(i * sizeFactor, degree, null, sTArray);
					checkAndAdd(nw, lastCounter, todoList);
				}
			}
			if (originalT instanceof HierarchicalAbstract) {
				times = times / 2;
			}
		}

		NetworkThread[] nwThreads = new NetworkThread[threads];
		for (int i = 0; i < threads; i++) {
			nwThreads[i] = new NetworkThread(i, pm);
		}

		Collections.shuffle(todoList);

		int counter = 0;
		for (NetworkTask n : todoList) {
			// System.out.println(n.nodes() + "/" + n.folder());
			nwThreads[counter].add(n);
			counter = (counter + 1) % threads;
		}

		pm.output("Starting to generate " + todoList.size() + " networks");
		pm.setMaxNetworks(todoList.size());

		for (int i = 0; i < threads; i++) {
			nwThreads[i].start();
		}
		for (int i = 0; i < threads; i++) {
			try {
				nwThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		stats.end();
	}

	private Transformation[] getTransformations(GraphDrawingAbstract gda, String bfsRoot) {
		BFS bfs = new BFS(bfsRoot);

		Transformation[] t;
		if (gda instanceof HierarchicalAbstract) {
			t = new Transformation[] { new Bidirectional(), new WeakConnectivityPartition(),
					new GiantConnectedComponent(), bfs, gda.clone() };
		} else {
			t = new Transformation[] { new Bidirectional(), new WeakConnectivityPartition(),
					new GiantConnectedComponent(), gda.clone() };
		}
		return t;
	}

	public void checkAndAdd(Network nw, HashMap<String, Integer> lastCounter, ArrayList<NetworkTask> todoList) {
		Integer i = lastCounter.get(nw.nodes() + "/" + nw.folder());
		if (i == null)
			i = 0;
		lastCounter.put(nw.nodes() + "/" + nw.folder(), (i + 1));
		if (!isCompleteDataset(nw, i)) {
			NetworkTask nwt = new NetworkTask(nw, i);
			todoList.add(nwt);
		}
	}

	public boolean isCompleteDataset(Network nw, int counter) {
		String prefix = "./resources/evaluation/" + nw.nodes() + "/" + nw.folder() + counter + ".txt";
		File temp;
		temp = new File(prefix + "_LOOKAHEAD_LIST_0");
		if (!temp.exists()) {
			// return false;
		}
		temp = new File(prefix + ".RUNTIME");
		if (!temp.exists()) {
			return false;
		}
		temp = new File(prefix + "_ID_SPACE_0");
		if (!temp.exists()) {
			return false;
		}
		return true;
	}

	private class ProgressMonitor {
		private Date start;
		private int generatedNetworks = 0;
		private int maxNetworks;
		Random rand;

		public ProgressMonitor() {
			start = new Date();
			output("Started ProgressMonitor");
			rand = new Random();
		}

		public void setMaxNetworks(int mNW) {
			this.maxNetworks = mNW;
		}

		public void tickNW() {
			generatedNetworks++;
			double runtime = new Date().getTime() - start.getTime();
			output("Generated network " + generatedNetworks + " of " + maxNetworks + " after running for "
					+ (runtime / 1000) + " seconds");
			if (rand.nextBoolean() && rand.nextBoolean()) {
				System.gc();
			}
		}

		public void output(String text) {
			SimpleDateFormat ts = new SimpleDateFormat("MMM d HH:mm:ss");
			System.out.println(ts.format(new Date()) + " -- " + text);
		}
	}

	private class NetworkTask {
		public Network nw;
		public int id;

		public NetworkTask(Network nw, int id) {
			this.nw = nw;
			this.id = id;
		}

	}

	private class NetworkThread extends Thread {
		private ArrayList<NetworkTask> nws;
		private ProgressMonitor pm;
		private int id;

		public NetworkThread(int id, ProgressMonitor pm) {
			this.nws = new ArrayList<NetworkTask>();
			this.pm = pm;
			this.id = id;
		}

		public void add(NetworkTask nw) {
			this.nws.add(nw);
		}

		public void run() {
			for (NetworkTask nwTask : nws) {
				Network nw = nwTask.nw;
				Integer i = nwTask.id;

				Graph g = nw.generate();
				int graphSize = g.getNodes().length;

				String folderName = "./resources/evaluation/" + graphSize + "/" + nw.folder();
				String fileName = folderName + i + ".txt";
				pm.output("Thread " + id + " starts generation of " + fileName);

				double startTime = System.currentTimeMillis();
				double completeTransformationTime = System.currentTimeMillis();
				Filewriter runtimeLogger = new Filewriter(fileName + ".RUNTIME");
				for (Transformation t : nw.transformations()) {
					try {
						g = t.transform(g);
					} catch (GDTransformationException e) {
						/*
						 * Accept an exception *once*, but not twice for the
						 * same transformation
						 */
						pm.output("Thread " + id + " restarts generation of " + fileName
								+ " after an exception was caught");
						g = t.transform(g);
					}
					runtimeLogger.writeln(t.key() + ":" + (System.currentTimeMillis() - startTime));
					startTime = System.currentTimeMillis();
				}
				runtimeLogger.close();

				GraphWriter.writeWithProperties(g, fileName);
				pm.output("Thread " + id + " wrote " + fileName + " after "
						+ (System.currentTimeMillis() - completeTransformationTime) / 1000 + " seconds");
				pm.tickNW();
			}
		}
	}
}
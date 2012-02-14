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
 * PET.java
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
import gtna.graph.Graph;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.routing.greedyVariations.OneWorseGreedy;
import gtna.util.Config;
import gtna.util.Stats;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class PET {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/PET/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/PET/");
		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, ROUTING");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		int[] Nodes = new int[] { 1000, 5000, 10000, 50000, 100000 };
		Nodes = new int[] { 1000, 5000, 10000, 50000 };
		double[] Alpha = new double[] { 2.0, 2.2, 2.4, 2.6, 2.8, 3.0 };
		int[] C = new int[] { 1, 2, 5, 10, 20, 50 };
		HashMap<Integer, Integer> times = new HashMap<Integer, Integer>();
		times.put(1000, 100);
		times.put(5000, 100);
		times.put(10000, 100);
		times.put(50000, 50);
		times.put(100000, 50);
		RoutingAlgorithm owg = new OneWorseGreedy();
		RoutingAlgorithm dfg = new DepthFirstGreedy();
		RoutingAlgorithm[] R = new RoutingAlgorithm[] { owg, dfg };

		if (args.length == 2) {
			int threads = Integer.parseInt(args[0]);
			int offset = Integer.parseInt(args[1]);
			Stats stats = new Stats();
			PET.generateGraphs(times, Nodes, Alpha, C, threads, offset);
			// PET.generateData(times, Nodes, Alpha, C, threads, offset);
			stats.end();
		} else {
			// PET.generatePlotsMulti(Nodes, Alpha, C, R);
			// PET.generatePlotsSingle(Nodes, Alpha, C, R);
		}
	}

	private static void generatePlotsMulti(int[] Nodes, double[] Alpha,
			int[] C, RoutingAlgorithm[] R) {
		for (int nodes : Nodes) {
			for (double alpha : Alpha) {
				for (int c : C) {
					int cut = nodes;
					Network[] nw = new Network[R.length];
					for (int i = 0; i < R.length; i++) {
						nw[i] = new ScaleFreeUndirected(nodes, alpha, c, cut,
								R[i], null);
					}
					String folder = nodes
							+ "/"
							+ (new ScaleFreeUndirected(nodes, alpha, c, cut,
									null, null)).folder();
					Series[] s = Series.get(nw);
					Plot.multiAvg(s, folder);
				}
			}
		}
	}

	private static void generatePlotsSingle(int[] Nodes, double[] Alpha,
			int[] C, RoutingAlgorithm[] R) {
		for (int nodes : Nodes) {
			for (RoutingAlgorithm r : R) {
				Network[][] nw1 = new Network[Alpha.length][C.length];
				Network[][] nw2 = new Network[C.length][Alpha.length];
				for (int i = 0; i < Alpha.length; i++) {
					for (int j = 0; j < C.length; j++) {
						int cut = nodes;
						nw1[i][j] = new ScaleFreeUndirected(nodes, Alpha[i],
								C[j], cut, r, null);
						nw2[j][i] = nw1[i][j];
					}
				}
				String folder1 = nodes + "/_alpha-c-" + r.folder() + "/";
				String folder2 = nodes + "/_c-alpha-" + r.folder() + "/";
				Series[][] s1 = Series.get(nw1);
				Series[][] s2 = Series.get(nw2);
				Plot.singlesAvg(s1, folder1);
				Plot.singlesAvg(s2, folder2);
			}
		}
	}

	private static void generateData(HashMap<Integer, Integer> times,
			int[] Nodes, double[] Alpha, int[] C, int threads, int offset) {
		for (int nodes : Nodes) {
			ArrayList<Network> nw = new ArrayList<Network>();
			for (double alpha : Alpha) {
				for (int c : C) {
					RoutingAlgorithm owg = new OneWorseGreedy();
					RoutingAlgorithm dfg = new DepthFirstGreedy();
					RoutingAlgorithm[] R = new RoutingAlgorithm[] { owg, dfg };
					for (RoutingAlgorithm r : R) {
						int cut = nodes;
						Network NW = new ScaleFreeUndirected(nodes, alpha, c,
								cut, null, null);
						Network readable = new ReadableFolder(NW.description(),
								NW.folder().replace("/", ""),
								PET.graphFolder(NW), ".txt", r,
								NW.transformations());
						nw.add(readable);
					}
				}
			}
			DataGenerator g = new DataGenerator(nw, threads, offset, times);
			g.start();
			try {
				g.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generateGraphs(HashMap<Integer, Integer> times,
			int[] Nodes, double[] Alpha, int[] C, int threads, int offset) {
		for (int nodes : Nodes) {
			ArrayList<Network> nw = new ArrayList<Network>();
			for (double alpha : Alpha) {
				for (int c : C) {
					int cut = nodes;
					nw.add(new ScaleFreeUndirected(nodes, alpha, c, cut, null,
							null));
				}
			}
			GraphGenerator g = new GraphGenerator(nw, threads, offset, times);
			g.start();
			try {
				g.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static String graphFolder(Network nw) {
		return "./data/graphs/" + nw.nodes() + "/" + nw.folder();
	}

	public static String graphFilename(Network nw, int i) {
		return PET.graphFolder(nw) + i + ".txt";
	}

	private static abstract class Generator extends Thread {
		protected ArrayList<Network> nw;

		protected int threads;

		protected int offset;

		protected HashMap<Integer, Integer> times;

		public Generator(ArrayList<Network> nw, int threads, int offset,
				HashMap<Integer, Integer> times) {
			this.nw = nw;
			this.threads = threads;
			this.offset = offset;
			this.times = times;
		}

		protected abstract void process(Network nw);

		public void run() {
			int index = this.offset;
			while (index < this.nw.size()) {
				Network NW = this.nw.get(index);
				this.process(NW);
				index += this.threads;
			}
		}
	}

	private static class GraphGenerator extends Generator {

		public GraphGenerator(ArrayList<Network> nw, int threads, int offset,
				HashMap<Integer, Integer> times) {
			super(nw, threads, offset, times);
		}

		@Override
		protected void process(Network nw) {
			for (int i = 0; i < this.times.get(nw.nodes()); i++) {
				String folder = PET.graphFolder(nw);
				String filename = PET.graphFilename(nw, i);
				File f = new File(folder);
				if ((new File(filename)).exists()) {
					System.out.println(this.offset + ":  skipping" + filename);
				} else {
					f.mkdirs();
					Graph g = nw.generate();
					GraphWriter.writeWithProperties(g, filename);
					System.out.println(this.offset + ":  " + filename);
				}
			}
		}
	}

	private static class DataGenerator extends Generator {
		public DataGenerator(ArrayList<Network> nw, int threads, int offset,
				HashMap<Integer, Integer> times) {
			super(nw, threads, offset, times);
		}

		@Override
		protected void process(Network nw) {
			System.out.println("@@@ " + offset + "/" + this.threads
					+ ": generating " + nw.description());
			Series s = Series.generate(nw, this.times.get(nw.nodes()));
		}
	}

}

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
 * PETData.java
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
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.networks.util.ReadableFolder;
import gtna.projects.PET.cutoffType;
import gtna.routing.RoutingAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author benni
 *
 */
public class PETData {
	public static void generateData(HashMap<Integer, Integer> times,
			int[] Nodes, double[] Alpha, int[] C, cutoffType type, int threads,
			int offset) {
		ArrayList<Network> nw = new ArrayList<Network>();
		for (int nodes : Nodes) {
			for (double alpha : Alpha) {
				for (int c : C) {
					RoutingAlgorithm[] R = PET.getRoutingAlgorithms();
					for (RoutingAlgorithm r : R) {
						int cut = PET.cutoff(nodes, type);
						Network NW = new ScaleFreeUndirected(nodes, alpha, c,
								cut, null, null);
						Network readable = new ReadableFolder(NW.description(),
								NW.folder().replace("/", ""),
								PET.graphFolder(NW), ".txt",
								new String[] { PET.idSpaceFilename(NW) }, r,
								NW.transformations());
						nw.add(readable);
					}
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

	private static class DataGenerator extends PETGenerator {
		public DataGenerator(ArrayList<Network> nw, int threads, int offset,
				HashMap<Integer, Integer> times) {
			super(nw, threads, offset, times);
		}

		@Override
		protected boolean process(Network nw) {
			System.out.println("@@@ " + offset + "/" + this.threads
					+ ": generating " + nw.description());
			Series.generate(nw, this.times.get(nw.nodes()));
			return true;
		}
	}
}

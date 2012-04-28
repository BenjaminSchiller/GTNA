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
 * PETGraphs.java
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

import gtna.graph.Graph;
import gtna.io.Filewriter;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.Network;
import gtna.projects.pets.PET.cutoffType;
import gtna.transformation.Transformation;
import gtna.util.Timer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class PETGraphs {
	public static void generateGraphs(HashMap<Integer, Integer> times,
			int[] Nodes, double[] Alpha, int[] C, cutoffType type, int threads,
			int offset) {
		ArrayList<Network> nw = new ArrayList<Network>();
		for (int nodes : Nodes) {
			for (double alpha : Alpha) {
				for (int c : C) {
					nw.add(PET.getSD(nodes, alpha, type, c));
				}
			}
		}
		GraphGenerator g = new GraphGenerator(nw, threads, offset, times, type);
		g.run();
	}

	private static class GraphGenerator extends PETGenerator {
		private cutoffType type;

		public GraphGenerator(ArrayList<Network> nw, int threads, int offset,
				HashMap<Integer, Integer> times, cutoffType type) {
			super(nw, threads, offset, times);
			this.type = type;
		}

		@Override
		protected boolean process(Network nw) {
			boolean success = true;
			double alpha = PET.getAlpha(nw);
			for (int i = 0; i < this.times.get(nw.getNodes()); i++) {
				Network nwLD = PET.getLD(nw.getNodes(), alpha, this.type);
				String folderLD = PET.graphLDFolder(nwLD);
				String filenameLD = PET.graphLDFilename(nwLD, i);
				String idSpaceFilenameLD = PET.idSpaceLDFilename(nwLD);
				File fLD = new File(folderLD);
				Graph gLD = null;
				if (!(new File(filenameLD)).exists()) {
					fLD.mkdirs();
					Filewriter fw = new Filewriter(filenameLD);
					fw.writeln("LOCK");
					fw.close();
					Timer timer = new Timer(this.offset + ": generateLD "
							+ filenameLD);
					gLD = nwLD.generate();
					timer.end();
					new GtnaGraphWriter().write(gLD, filenameLD);
					if (!(new File(idSpaceFilenameLD)).exists()) {
						gLD.getProperty("ID_SPACE_0").write(idSpaceFilenameLD,
								"ID_SPACE_0");
					}
				} else {
					String[] p = new String[] { idSpaceFilenameLD };
					try {
						gLD = new GtnaGraphReader().readWithProperties(
								filenameLD, p);
					} catch (Exception e) {
						gLD = null;
					}
					if (gLD == null) {
						System.out.println(this.offset + ": can't read "
								+ filenameLD);
						success = false;
					}
				}
				if (gLD == null) {
					success = false;
					continue;
				}
				String folder = PET.graphFolder(nw);
				String filename = PET.graphFilename(nw, i);
				String idSpaceFilename = PET.idSpaceFilename(nw);
				File f = new File(folder);
				if ((new File(filename)).exists()) {
					System.out.println(this.offset + ":     skipping"
							+ filename);
					continue;
				}
				Transformation t = nw.getTransformations()[0];
				Timer timer = new Timer(this.offset + ": generateSD "
						+ filename);
				Graph g = t.transform(gLD);
				timer.end();
				f.mkdirs();
				new GtnaGraphWriter().write(g, filename);
				if (!(new File(idSpaceFilename)).exists()) {
					g.getProperty("ID_SPACE_0").write(idSpaceFilename,
							"ID_SPACE_0");
				}
			}
			return success;
		}
	}
}

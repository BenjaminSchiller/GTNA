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
 * WiFi.java
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
package gtna.projects.placement;

import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.util.Config;
import gtna.util.Execute;

import java.io.File;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class WiFi {

	/**
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Config.overwrite("GEPHI_RING_RADIUS", "100");
		Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
		Config.overwrite("GEPHI_EDGE_SCALE", "0.1");
		Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.1");
		Config.overwrite("GEPHI_NODE_SIZE", "2");

		String[] cities = new String[] { "berlin", "chicago", "darmstadt_all",
				"darmstadt_open", "google", "manhattan", "melbourne", "praha",
				"san_francisco" };

		HashMap<String, double[]> radii = new HashMap<String, double[]>();
		radii.put("berlin", new double[] {});
		radii.put("chicago", new double[] {});
		radii.put("darmstadt_all", new double[] {});
		radii.put("darmstadt_open", new double[] {});
		radii.put("google", new double[] {});
		radii.put("manhattan", new double[] {});
		radii.put("melbourne", new double[] {});
		radii.put("praha", new double[] {});
		radii.put("san_francisco", new double[] {});

		HashMap<String, double[]> area = new HashMap<String, double[]>();
		area.put("berlin", new double[] {});
		area.put("chicago", new double[] {});
		area.put("darmstadt_all", new double[] {});
		area.put("darmstadt_open", new double[] {});
		area.put("google", new double[] {});
		area.put("manhattan", new double[] {});
		area.put("melbourne", new double[] {});
		area.put("praha", new double[] {});
		area.put("san_francisco", new double[] {});

		double maxWidth = 1000;
		double maxHeight = 1000;

		WiFiNetwork wn1 = new WiFiNetwork("berlin", 50, 459, 1000,
				new double[] {});
		WiFiNetwork wn2 = new WiFiNetwork("chicago", 50, 960, 1000,
				new double[] {});
		WiFiNetwork wn3 = new WiFiNetwork("darmstadt_all", 1971, 503, 1000,
				new double[] {});
		WiFiNetwork wn4 = new WiFiNetwork("darmstadt_open", 212, 456, 1000,
				new double[] {});
		WiFiNetwork wn5 = new WiFiNetwork("google", 488, 1000, 927,
				new double[] {});
		WiFiNetwork wn6 = new WiFiNetwork("manhattan", 166, 1000, 616,
				new double[] {});
		WiFiNetwork wn7 = new WiFiNetwork("melbourne", 115, 1000, 783,
				new double[] {});
		WiFiNetwork wn8 = new WiFiNetwork("praha", 124, 744, 1000,
				new double[] {});
		WiFiNetwork wn9 = new WiFiNetwork("san_francisco", 170, 1000, 566,
				new double[] {});

		WiFiNetwork[] wns = new WiFiNetwork[] { wn1, wn2, wn3, wn4, wn5, wn6,
				wn7, wn8, wn9 };

		for (WiFiNetwork wn : wns) {
			for (Network nw : wn.getNetworks(null)) {
				String p1 = wn.getPlot1(nw);
				String p2 = wn.getPlot2(nw);
				if ((new File(p2)).exists()) {
					continue;
				}
				Graph g = nw.generate();
				IdentifierSpace idSpace = (IdentifierSpace) g
						.getProperty("ID_SPACE_0");
				Gephi gephi = new Gephi();
				gephi.plot(g, idSpace, p1);
				gephi = null;
				String[] env = new String[] { "PATH=/opt/local/bin/:$PATH" };
				String cmd = "/usr/local/bin/convert " + p1 + " " + p2;
				Execute.exec(cmd, false, env);
				Execute.exec("rm " + p1, true);
			}
		}
	}

//	private static Network getRandom(WiFiNetwork wfn, Network nw) {
//		PlacementModel p1 = new RandomPlacementModel(xy, xy, true);
//		PlacementModel p2 = new RandomPlacementModel(xy, xy, true);
//		return new PlacementModelContainer(nodes, 1, xy, xy, p1, p2,
//				partitioner, connector, null);
//	}
}

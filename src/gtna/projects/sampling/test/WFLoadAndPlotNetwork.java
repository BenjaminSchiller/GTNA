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
 * WFLoadAndPlotNetwork.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.sampling.test;

import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.CsvGraphWriter;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.util.Config;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tim
 * 
 */
public class WFLoadAndPlotNetwork {

	static Collection<String> networks = new ArrayList<String>();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		for (String s : args) {
			matchArgument(s);
		}

		Map<String, Network> mp = new HashMap<String, Network>();

		for (String s : networks) {
			ReadableFile net = new ReadableFile(new File(s).getName(),
					new File(s).getCanonicalPath(), s, null);

			mp.put(s, net);
		}

		plotNetworks(mp);
	}

	/**
	 * @param mp
	 */
	private static void plotNetworks(Map<String, Network> mp) {

		for (Entry<String, Network> n : mp.entrySet()) {
			File p = new File(n.getKey());
			String dir = p.getParent();

			File d = new File(dir);
			if (!d.exists() || !d.isDirectory()) {
				d.mkdirs();
			}

			Graph g = n.getValue().generate();

			String filename = p.getName();

			Transformation t_rpid = new RandomPlaneIDSpaceSimple(3,
					g.getNodeCount(), 1.5 * g.getNodeCount(), true);
			Transformation t_rrid = new RandomRingIDSpaceSimple(true);
			Transformation t_crid = new ConsecutiveRingIDSpace(true);

			Transformation t_nid = t_rpid;

			Gephi gephi = new Gephi();
			if (t_nid == t_rpid) {
				Config.overwrite("GEPHI_RING_RADIUS", "1");
				Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.001");
				Config.overwrite("GEPHI_EDGE_SCALE", "0.0001");
				Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
				Config.overwrite("GEPHI_NODE_SIZE", "0.01");
			} else if (t_nid == t_rrid || t_nid == t_crid) {
				Config.overwrite("GEPHI_RING_RADIUS", "50");
				Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.01");
				Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
				Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
				Config.overwrite("GEPHI_NODE_SIZE", "0.001");
			}

			g = t_nid.transform(g);

			IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");

			// new GtnaGraphWriter().writeWithProperties(g,
			// graphFilename+".txt");
			// // writing is already done

			CsvGraphWriter cgw = new CsvGraphWriter();
			cgw.write(g, p.getParent() + "/" + filename + ".csv");

			gephi.plot(g, ids, p.getParent() + "/" + filename + ".pdf");
		}
	}

	/**
	 * @param s
	 * @throws ParseException
	 */
	private static void matchArgument(String s) {
		if (s.startsWith("path=")) {
			String dir = s.substring(5);
			File f = new File(dir);
			if (f.isFile()) {
				networks.add(dir);
			}
		}
	}

}

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
 * Networks.java
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
package gtna.projects.cc;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.canonical.Complete;
import gtna.networks.canonical.Regular;
import gtna.networks.canonical.Ring;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.WattsStrogatz;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class Networks {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HashMap<Network, String> name = new HashMap<Network, String>();

		int k = 1000;

		Network regular1 = new Regular(1 * k, 10, false, null);
		name.put(regular1, "regular-1k");
		Network regular2 = new Regular(10 * k, 50, false, null);
		name.put(regular2, "regular-10k");

		Network complete1 = new Complete(1 * k, null);
		name.put(complete1, "complete-1k");

		Network ring1 = new Ring(1 * k, null);
		name.put(ring1, "ring-1k");
		Network ring2 = new Ring(10 * k, null);
		name.put(ring2, "ring-10k");
		Network ring3 = new Ring(100 * k, null);
		name.put(ring3, "ring-100k");

		Network ba1 = new BarabasiAlbert(1 * k, 10, null);
		name.put(ba1, "scale-free-1k");
		Network ba2 = new BarabasiAlbert(10 * k, 50, null);
		name.put(ba2, "scale-free-10k");
		Network ba3 = new BarabasiAlbert(100 * k, 100, null);
		name.put(ba3, "scale-free-100k");

		Network er1 = new ErdosRenyi(1 * k, 10, false, null);
		name.put(er1, "random-1k");
		Network er2 = new ErdosRenyi(10 * k, 50, false, null);
		name.put(er2, "random-10k");
		Network er3 = new ErdosRenyi(100 * k, 100, false, null);
		name.put(er3, "random-100k");

		Network ws1 = new WattsStrogatz(1 * k, 10, 0.5, null);
		name.put(ws1, "small-world-1k");
		Network ws2 = new WattsStrogatz(10 * k, 50, 0.5, null);
		name.put(ws2, "small-world-10k");
		Network ws3 = new WattsStrogatz(100 * k, 100, 0.5, null);
		name.put(ws3, "small-world-100k");

		GraphWriter writer = new GtnaGraphWriter();
		Network[] nws = new Network[] { regular1, regular2,
				complete1, ring1, ring2, ring3, ba1, ba2, ba3, er1, er2, er3,
				ws1, ws2, ws3 };
		for (Network nw : nws) {
			System.out.print("generating " + nw.getDescription());
			Graph g = nw.generate();
			System.out.println("    DONE");
			String filename = "res/" + name.get(nw) + ".gtna";
			System.out.print("writing to " + filename);
			writer.write(g, filename);
			System.out.println("    DONE");
		}

		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new ShortestPaths() };
		for (Network nw : nws) {
			Network nw_ = new ReadableFile(name.get(nw), name.get(nw), "res/"
					+ name.get(nw) + ".gtna", null);
			Series s = Series.generate(nw_, metrics, 1);
			Plotting.multi(s, metrics, name.get(nw) + "/");
		}
	}
}

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
 * GexfGraphWriter.java
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
package gtna.io.graphWriter;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Filewriter;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.networks.googlePlus.FileIndexComparator;
import gtna.io.networks.googlePlus.FileNameFilter;
import gtna.util.Util;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * GraphWriter for the GEXF graph file format as specified here:
 * http://gexf.net/format/basic.html
 * 
 * @author benni
 * 
 */
public class GexfGraphWriter extends GraphWriter {

	public GexfGraphWriter() {
		super("GEXF");
	}

	@Override
	public boolean write(Graph g, String filename) {
		Filewriter fw = new Filewriter(filename);
		fw.writeln("<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\">");
		fw.writeln("<graph mode=\"dynamic\" defaultedgetype=\"directed\">");
		fw.writeln("  <nodes>");
		for (Node n : g.getNodes()) {
			fw.writeln("    <node id=\"" + n.getIndex()
					+ "\" label=\"\" start=\"" + n.getIndex()
					+ "\" end=\"1000\" />");
		}
		fw.writeln("  </nodes>");
		fw.writeln("  <edges>");
		Date date = new Date(System.currentTimeMillis());
		for (Node n : g.getNodes()) {
			for (int out : n.getOutgoingEdges()) {
				// SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat(
				// "yyyy-MM-dd");
				// String start = dateformatYYYYMMDD.format(date);
				// String end = "2020-01-01";
				fw.writeln("    <edge source=\"" + n.getIndex()
						+ "\" target=\"" + out + "\" />");
				// fw.writeln("      <spells>");
				// fw.writeln("        <spell start=\"" + start + "\" end=\""
				// + end + "\" />");
				// fw.writeln("      </spells>");
				// fw.writeln("    </edge>");
			}
			date.setTime(date.getTime() + 1000 * 60 * 60 * 24);
		}
		fw.writeln("  </edges>");
		fw.writeln("</graph>");
		fw.writeln("</gexf>");
		return fw.close();
	}

	public boolean writeGEXF(String graphs, String filename) {
		File[] files = (new File(graphs)).listFiles(new FileNameFilter("",
				"-graph.txt"));
		Arrays.sort(files, new FileIndexComparator("-", 0));

		Graph init = new GtnaGraphReader().read(files[0].getAbsolutePath());
		int end = 0;

		Edges edges = new Edges(init.getNodes(), 100000);
		HashMap<String, Integer> edgeStart = new HashMap<String, Integer>();
		int[] nodeStart = Util.initIntArray(init.getNodes().length, -1);
		for (File f : files) {
			Graph g = new GtnaGraphReader().read(f.getAbsolutePath());
			int cid = Integer.parseInt(f.getName().split("-")[0]);
			end = cid + 1;
			for (Node n : g.getNodes()) {
				if (nodeStart[n.getIndex()] == -1) {
					if (n.getOutDegree() > 0 || n.getInDegree() > 0) {
						nodeStart[n.getIndex()] = cid;
					}
				}
			}
			Edge[] edgeList = g.generateEdges();
			for (Edge e : edgeList) {
				if (!edges.contains(e.getSrc(), e.getDst())) {
					edges.add(e.getSrc(), e.getDst());
					edgeStart.put(e.toString(), cid);
				}
			}
		}

		Filewriter fw = new Filewriter(filename);
		fw.writeln("<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\">");
		fw.writeln("<graph mode=\"dynamic\" defaultedgetype=\"directed\">");
		fw.writeln("  <nodes>");
		for (int index = 0; index < nodeStart.length; index++) {
			fw.writeln("    <node id=\"" + index + "\" start=\""
					+ nodeStart[index] + "\" end=\"" + end + "\" />");
		}
		fw.writeln("  </nodes>");
		fw.writeln("  <edges>");
		for (Edge e : edges.getEdges()) {
			fw.writeln("    <edge source=\"" + e.getSrc() + "\" target=\""
					+ e.getDst() + "\" start=\"" + edgeStart.get(e.toString())
					+ "\" end=\"" + end + "\" />");
		}
		fw.writeln("  </edges>");
		fw.writeln("</graph>");
		fw.writeln("</gexf>");
		return fw.close();
	}

}

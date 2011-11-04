/*
 * ===========================================================
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
 * GraphWriter.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.io;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.io.networks.googlePlus.FileIndexComparator;
import gtna.io.networks.googlePlus.FileNameFilter;
import gtna.util.Config;
import gtna.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class GraphWriter {
	public static boolean write(Graph graph, String filename) {
		String sep1 = Config.get("GRAPH_WRITER_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_WRITER_SEPARATOR_2");

		Filewriter fw = new Filewriter(filename);

		// NAME
		fw.writeComment(Config.get("GRAPH_WRITER_NAME"));
		fw.writeln(graph.getName());

		// NODES
		fw.writeComment(Config.get("GRAPH_WRITER_NODES"));
		fw.writeln(graph.getNodes().length);

		// EDGES
		fw.writeComment(Config.get("GRAPH_WRITER_EDGES"));
		fw.writeln(graph.computeNumberOfEdges());

		// EDGES
		fw.writeln();
		for (Node node : graph.getNodes()) {
			StringBuffer buff = new StringBuffer(node.getIndex() + sep1);
			if (node.getOutDegree() == 0) {
				fw.writeln(buff.toString());
				continue;
			}
			buff.append(node.getOutgoingEdges()[0]);
			for (int i = 1; i < node.getOutDegree(); i++) {
				buff.append(sep2 + node.getOutgoingEdges()[i]);
			}
			fw.writeln(buff.toString());
		}

		return fw.close();
	}

	public static boolean writeWithProperties(Graph graph, String filename) {
		HashMap<String, String> filenames = new HashMap<String, String>();
		String del = Config.get("GRAPH_WRITER_PROPERTY_FILE_DELIMITER");
		for (String key : graph.getProperties().keySet()) {
			filenames.put(key, filename + del + key);
		}
		return GraphWriter.writeWithProperties(graph, filename, filenames);
	}

	public static boolean writeWithProperties(Graph graph, String filename,
			HashMap<String, String> filenames) {
		boolean success = GraphWriter.write(graph, filename);
		for (String key : filenames.keySet()) {
			GraphProperty property = graph.getProperty(key);
			success &= property.write(filenames.get(key), key);
		}
		return success;
	}

	public static boolean writeOld(Graph graph, String filename) {
		String delimiter = Config.get("GRAPH_WRITER_DELIMITER");
		Filewriter fw = new Filewriter(filename);
		Edge[] edges = graph.generateEdges();

		// NAME
		fw.writeComment(Config.get("GRAPH_WRITER_NAME"));
		fw.writeln(graph.getName());

		// NODES
		fw.writeComment(Config.get("GRAPH_WRITER_NODES"));
		fw.writeln(graph.getNodes().length);

		// EDGES
		fw.writeComment(Config.get("GRAPH_WRITER_EDGES"));
		fw.writeln(edges.length);

		// EDGES
		fw.writeln();
		for (Edge e : edges) {
			fw.writeln(e.getSrc() + delimiter + e.getDst());
		}

		return fw.close();
	}

	/**
	 * https://gephi.org/users/supported-graph-formats/csv-format/
	 * 
	 * @param graph
	 * @param filename
	 * @return
	 */
	public static boolean writeCSV(Graph graph, String filename) {
		Filewriter fw = new Filewriter(filename);
		for (Node node : graph.getNodes()) {
			StringBuffer buff = new StringBuffer();
			buff.append(node.getIndex());
			for (int out : node.getOutgoingEdges()) {
				buff.append(";" + out);
			}
			fw.writeln(buff.toString());
		}
		return fw.close();
	}

	/**
	 * http://gexf.net/format/basic.html
	 * 
	 * @param graph
	 * @param filename
	 * @return
	 */
	public static boolean writeGEXF(Graph graph, String filename) {
		Filewriter fw = new Filewriter(filename);
		fw.writeln("<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\">");
		fw.writeln("<graph mode=\"dynamic\" defaultedgetype=\"directed\">");
		fw.writeln("  <nodes>");
		for (Node n : graph.getNodes()) {
			fw.writeln("    <node id=\"" + n.getIndex()
					+ "\" label=\"\" start=\"" + n.getIndex()
					+ "\" end=\"1000\" />");
		}
		fw.writeln("  </nodes>");
		fw.writeln("  <edges>");
		Date date = new Date(System.currentTimeMillis());
		for (Node n : graph.getNodes()) {
			for (int out : n.getOutgoingEdges()) {
				SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat(
						"yyyy-MM-dd");
				String start = dateformatYYYYMMDD.format(date);
				String end = "2020-01-01";
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

	/**
	 * http://gexf.net/format/basic.html
	 * 
	 * @param graph
	 * @param filename
	 * @return
	 */
	public static boolean writeGEXF(String graphs, String filename) {
		File[] files = (new File(graphs)).listFiles(new FileNameFilter("",
				"-graph.txt"));
		Arrays.sort(files, new FileIndexComparator("-", 0));

		Graph init = GraphReader.read(files[0].getAbsolutePath());
		int end = 0;

		Edges edges = new Edges(init.getNodes(), 100000);
		HashMap<String, Integer> edgeStart = new HashMap<String, Integer>();
		int[] nodeStart = Util.initIntArray(init.getNodes().length, -1);
		for (File f : files) {
			Graph g = GraphReader.read(f.getAbsolutePath());
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

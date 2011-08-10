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
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.util.Config;

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
}

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
import gtna.graph.NodeImpl;
import gtna.routing.node.IDNode;
import gtna.util.Config;

public class GraphWriter {
	public static final String DELIMITER = "	";

	public static final int OWN_FORMAT = 1;

	public static final int GML_FORMAT = 2;

	public static final int INFO_FORMAT = 3;

	public static final int DOT_FORMAT = 4;

	public static final int TO_STRING_FORMAT = 5;

	public static void write(Graph g, String filename, int type) {
		if (type == OWN_FORMAT) {
			write(g, filename);
		} else if (type == GML_FORMAT) {
			gml(g, filename);
		} else if (type == INFO_FORMAT) {
			info(g, filename);
		} else if (type == DOT_FORMAT) {
			dot(g, filename);
		} else if (type == TO_STRING_FORMAT) {
			toString(g, filename);
		}
	}

	public static void write(Graph g, String filename) {
		Filewriter fw = new Filewriter(filename);
		fw.writeComment(Config.get("GRAPH_WRITER_NAME"));
		fw.writeln(g.name);
		fw.writeComment(Config.get("GRAPH_WRITER_NODES"));
		fw.writeln(g.nodes.length);
		fw.writeComment(Config.get("GRAPH_WRITER_EDGES"));
		fw.writeln(g.edges);
		fw.writeln();
		for (int i = 0; i < g.nodes.length; i++) {
			NodeImpl[] out = g.nodes[i].out();
			for (int j = 0; j < out.length; j++) {
				fw.writeln(i + DELIMITER + out[j].index());
			}
		}
		fw.close();
	}

	public static void info(Graph g, String filename) {
		if (!(g.nodes[0] instanceof IDNode)) {
			return;
		}
		Filewriter fw = new Filewriter(filename);
		fw.writeComment(Config.get("GRAPH_WRITER_NAME"));
		fw.writeln(g.name);
		fw.writeComment(Config.get("GRAPH_WRITER_NODES"));
		fw.writeln(g.nodes.length);
		fw.writeComment(Config.get("GRAPH_WRITER_EDGES"));
		fw.writeln(g.edges);
		fw.writeln();
		for (int i = 0; i < g.nodes.length; i++) {
			NodeImpl[] out = g.nodes[i].out();
			for (int j = 0; j < out.length; j++) {
				String from = "" + ((IDNode) g.nodes[i]).toString();
				String to = "" + ((IDNode) out[j]).toString();
				fw.writeln(from + DELIMITER + to);
			}
		}
		fw.close();
	}

	public static void toString(Graph g, String filename) {
		Filewriter fw = new Filewriter(filename);
		fw.writeComment(Config.get("GRAPH_WRITER_NAME"));
		fw.writeln(g.name);
		fw.writeComment(Config.get("GRAPH_WRITER_NODES"));
		fw.writeln(g.nodes.length);
		fw.writeComment(Config.get("GRAPH_WRITER_EDGES"));
		fw.writeln(g.edges);
		fw.writeln();
		for (int i = 0; i < g.nodes.length; i++) {
			NodeImpl[] out = g.nodes[i].out();
			for (int j = 0; j < out.length; j++) {
				String from = "" + g.nodes[i].toString();
				String to = "" + out[j].toString();
				fw.writeln(from + DELIMITER + to);
			}
		}
		fw.close();
	}

	public static void gml(Graph g, String filename) {
		Filewriter fw = new Filewriter(filename);
		fw.writeln("graph [");
		fw.writeln("  comment \"" + Config.get("GRAPH_WRITER_NAME") + " "
				+ g.name + "\"");
		fw.writeln("  comment \"" + Config.get("GRAPH_WRITER_NODES") + " "
				+ g.nodes.length + "\"");
		fw.writeln("  comment \"" + Config.get("GRAPH_WRITER_EDGES") + " "
				+ g.edges + "\"");

		for (NodeImpl node : g.nodes) {
			fw.writeln("  node [");
			fw.writeln("    id " + node.index());
			fw.writeln("  ]");
		}
		for (Edge edge : g.edges()) {
			fw.writeln("  edge [");
			fw.writeln("    source " + edge.src.index());
			fw.writeln("    target " + edge.dst.index());
			fw.writeln("  ]");
		}
		fw.writeln("]");
		fw.close();
	}

	public static void dot(Graph g, String filename) {
		Filewriter fw = new Filewriter(filename);
		fw.writeln("# " + Config.get("GRAPH_WRITER_NAME") + " " + g.name);
		fw.writeln("# " + Config.get("GRAPH_WRITER_NODES") + " "
				+ +g.nodes.length);
		fw.writeln("# " + Config.get("GRAPH_WRITER_EDGES") + " " + g.edges);
		fw.writeln("digraph \"" + g.name + "\"{");
		for (Edge edge : g.edges()) {
			fw.writeln("    " + edge.src.index() + " -> " + edge.dst.index()
					+ ";");
		}
		fw.writeln("}");
		fw.close();
	}
}

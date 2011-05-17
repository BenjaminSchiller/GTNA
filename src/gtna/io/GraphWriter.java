package gtna.io;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.routing.node.IDNode;
import gtna.util.Config;

public class GraphWriter {
	public static final String DELIMITER = "	";

	public static final int OWN_FORMAT = 1;

	public static final int GML_FORMAT = 2;

	public static final int INFO_FORMAT = 3;

	public static final int DOT_FORMAT = 4;

	public static void write(Graph g, String filename, int type) {
		if (type == OWN_FORMAT) {
			write(g, filename);
		} else if (type == GML_FORMAT) {
			gml(g, filename);
		} else if (type == INFO_FORMAT) {
			info(g, filename);
		} else if (type == DOT_FORMAT) {
			dot(g, filename);
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
			Node[] out = g.nodes[i].out();
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
			Node[] out = g.nodes[i].out();
			for (int j = 0; j < out.length; j++) {
				String from = "" + ((IDNode) g.nodes[i]).toString();
				String to = "" + ((IDNode) out[j]).toString();
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

		for (Node node : g.nodes) {
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

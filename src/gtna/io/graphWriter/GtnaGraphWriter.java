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
package gtna.io.graphWriter;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Filewriter;
import gtna.util.Config;

public class GtnaGraphWriter extends GraphWriter {

	public GtnaGraphWriter() {
		super("GTNA");
	}

	public boolean write(Graph g, String filename) {
		String sep1 = Config.get("GRAPH_WRITER_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_WRITER_SEPARATOR_2");

		Filewriter fw = new Filewriter(filename);

		// NAME
		fw.writeComment(Config.get("GRAPH_WRITER_NAME"));
		fw.writeln(g.getName());

		// NODES
		fw.writeComment(Config.get("GRAPH_WRITER_NODES"));
		fw.writeln(g.getNodes().length);

		// EDGES
		fw.writeComment(Config.get("GRAPH_WRITER_EDGES"));
		fw.writeln(g.computeNumberOfEdges());

		// EDGES
		fw.writeln();
		for (Node node : g.getNodes()) {
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

}

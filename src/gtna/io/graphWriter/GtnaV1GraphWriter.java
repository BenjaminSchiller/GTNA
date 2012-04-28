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
 * GtnaV1GraphWriter.java
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
import gtna.graph.Graph;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class GtnaV1GraphWriter extends GraphWriter {

	public GtnaV1GraphWriter() {
		super("GTNA_V1");
	}

	@Override
	public boolean write(Graph g, String filename) {
		String delimiter = Config.get("GRAPH_WRITER_DELIMITER");
		Filewriter fw = new Filewriter(filename);
		Edge[] edges = g.generateEdges();

		// NAME
		fw.writeComment(Config.get("GRAPH_WRITER_NAME"));
		fw.writeln(g.getName());

		// NODES
		fw.writeComment(Config.get("GRAPH_WRITER_NODES"));
		fw.writeln(g.getNodes().length);

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

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
 * GtnaV1GraphReader.java
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
package gtna.io.graphReader;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Filereader;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class GtnaV1GraphReader extends GraphReader {

	public GtnaV1GraphReader() {
		super("GTNA_V1");
	}

	@Override
	public Graph read(String filename) {
		String delimiter = Config.get("GRAPH_WRITER_DELIMITER");
		Filereader fr = new Filereader(filename);

		String name = fr.readLine();
		int N = Integer.parseInt(fr.readLine());
		int E = Integer.parseInt(fr.readLine());

		Graph graph = new Graph(name);
		Node[] nodes = Node.init(N, graph);
		Edges edges = new Edges(nodes, E);

		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(delimiter);
			edges.add(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
		}
		edges.fill();
		graph.setNodes(nodes);
		fr.close();
		return graph;
	}

	@Override
	public int nodes(String filename) {
		Filereader fr = new Filereader(filename);
		fr.readLine();
		int N = Integer.parseInt(fr.readLine());
		fr.close();
		return N;
	}

}

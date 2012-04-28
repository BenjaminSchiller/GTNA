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
 * EdgeListGraphReader.java
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author benni
 * 
 */
public class EdgeListGraphReader extends GraphReader {
	protected String separator;

	public EdgeListGraphReader(String separator) {
		this("EDGE_LIST", separator);
	}

	protected EdgeListGraphReader(String key, String separator) {
		super(key);
		this.separator = separator;
	}

	@Override
	public Graph read(String filename) {
		Map<String, Integer> map = this.getIdIndexMap(filename);

		Graph graph = new Graph(this.getGraphName(filename));
		Node[] nodes = Node.init(map.size(), graph);
		Edges edges = new Edges(nodes, map.size());

		Filereader fr = new Filereader(filename);

		try {
			String line = null;
			while ((line = fr.readLine()) != null) {
				String[] temp = line.split(this.separator);
				edges.add(map.get(temp[0]), map.get(temp[1]));
			}
			edges.fill();
			graph.setNodes(nodes);
			return graph;
		} catch (Exception e) {
			return null;
		} finally {
			fr.close();
		}
	}

	protected Map<String, Integer> getIdIndexMap(String filename) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		int index = 0;
		Filereader fr = new Filereader(filename);
		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(this.separator);
			if (!map.containsKey(temp[0])) {
				map.put(temp[0], index++);
			}
			if (!map.containsKey(temp[1])) {
				map.put(temp[1], index++);
			}
		}
		fr.close();

		return map;
	}

	@Override
	public int nodes(String filename) {
		return this.getIdIndexMap(filename).size();
	}

}

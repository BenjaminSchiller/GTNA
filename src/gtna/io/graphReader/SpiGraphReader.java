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
 * SPIReader.java
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
package gtna.io.graphReader;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Filereader;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class SpiGraphReader extends GraphReader {
	public static final String buddySeparator = ";";

	public static final String buddyFileStart = "\"user_active_rid\"";

	public SpiGraphReader() {
		super("SPI");
	}

	@Override
	public Graph read(String filename) {
		Hashtable<String, Integer> ids = new Hashtable<String, Integer>();
		int index = 0;
		int edgeCounter = 0;

		Filereader fr = new Filereader(filename);
		String line = null;
		while ((line = fr.readLine()) != null) {
			if (line.startsWith(SpiGraphReader.buddyFileStart)) {
				continue;
			}
			String[] parts = line.split(SpiGraphReader.buddySeparator);
			String from = parts[0].replace("\"", "");
			String to = parts[1].replace("\"", "");
			if (!ids.containsKey(from)) {
				ids.put(from, index++);
			}
			if (!ids.containsKey(to)) {
				ids.put(to, index++);
			}
			edgeCounter++;
		}
		fr.close();

		Graph graph = new Graph(this.getGraphName(filename));
		Node[] nodes = Node.init(index, graph);
		Edges edges = new Edges(nodes, edgeCounter);

		fr = new Filereader(filename);
		line = null;
		while ((line = fr.readLine()) != null) {
			if (line.startsWith(SpiGraphReader.buddyFileStart)) {
				continue;
			}
			String[] parts = line.split(SpiGraphReader.buddySeparator);
			String from = parts[0].replace("\"", "");
			String to = parts[1].replace("\"", "");
			int fromID = ids.get(from);
			int toID = ids.get(to);
			edges.add(fromID, toID);
		}
		fr.close();

		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	@Override
	public int nodes(String filename) {
		Set<String> nodes = new HashSet<String>();

		Filereader fr = new Filereader(filename);
		String line = null;
		while ((line = fr.readLine()) != null) {
			if (line.startsWith(SpiGraphReader.buddyFileStart)) {
				continue;
			}
			String[] parts = line.split(SpiGraphReader.buddySeparator);
			nodes.add(parts[0].replace("\"", ""));
			nodes.add(parts[1].replace("\"", ""));
		}
		fr.close();

		return nodes.size();
	}
}

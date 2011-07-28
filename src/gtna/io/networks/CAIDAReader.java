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
 * CAIDAReader.java
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
package gtna.io.networks;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Filereader;

import java.util.Hashtable;

public class CAIDAReader extends Filereader {
	private static final String caidaSeparator = "	";

	public CAIDAReader(String filename) {
		super(filename);
	}

	public static Graph read(String filename) {
		Graph graph = new Graph("CAIDA read from " + filename);

		Hashtable<String, Integer> ids = new Hashtable<String, Integer>();
		int index = 0;
		int edgeCounter = 0;
		CAIDAReader reader1 = new CAIDAReader(filename);
		String line1 = null;
		while ((line1 = reader1.readLine()) != null) {
			String[] parts = line1.split(caidaSeparator);
			if ("D".equals(parts[0])) {
				String from = parts[1];
				String to = parts[2];
				if (!ids.containsKey(from)) {
					ids.put(from, index++);
				}
				if (!ids.containsKey(to)) {
					ids.put(to, index++);
				}
			}
			edgeCounter++;
		}
		reader1.close();

		Node[] nodes = Node.init(index, graph);
		Edges edges = new Edges(nodes, edgeCounter);
		CAIDAReader reader = new CAIDAReader(filename);
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(caidaSeparator);
			if ("D".equals(parts[0])) {
				int fromID = ids.get(parts[1]);
				int toID = ids.get(parts[2]);
				edges.add(fromID, toID);
			}
		}
		reader.close();
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}

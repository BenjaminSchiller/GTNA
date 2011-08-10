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
 * GraphReader.java
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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.Config;

public class GraphReader {
	public static Graph read(String filename) {
		String sep1 = Config.get("GRAPH_WRITER_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_WRITER_SEPARATOR_2");
		Filereader fr = new Filereader(filename);
		String line = null;
		String name = fr.readLine();
		int V = Integer.parseInt(fr.readLine());
		int E = Integer.parseInt(fr.readLine());
		Graph graph = new Graph(name);
		Node[] nodes = Node.init(V, graph);
		Edges edges = new Edges(nodes, E);
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(sep1);
			if (temp.length < 2 || temp[1].length() == 0) {
				continue;
			}
			int src = Integer.parseInt(temp[0]);
			String[] temp2 = temp[1].split(sep2);
			for (String dst : temp2) {
				edges.add(src, Integer.parseInt(dst));
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		fr.close();
		return graph;
	}

	public static int nodes(String filename) {
		Filereader fr = new Filereader(filename);
		fr.readLine();
		int V = Integer.parseInt(fr.readLine());
		fr.close();
		return V;
	}

	public static Graph readOld(String filename) {
		String delimiter = Config.get("GRAPH_WRITER_DELIMITER");
		Filereader fr = new Filereader(filename);
		String line = null;
		String name = fr.readLine();
		int V = Integer.parseInt(fr.readLine());
		int E = Integer.parseInt(fr.readLine());
		Graph graph = new Graph(name);
		Node[] nodes = Node.init(V, graph);
		Edges edges = new Edges(nodes, E);
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(delimiter);
			edges.add(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
		}
		edges.fill();
		graph.setNodes(nodes);
		fr.close();
		return graph;
	}
}

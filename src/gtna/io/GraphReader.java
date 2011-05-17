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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
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
import gtna.graph.NodeImpl;
import gtna.util.Config;
import gtna.util.Timer;

import java.util.HashMap;
import java.util.Hashtable;

public class GraphReader {
	public static final int OWN_FORMAT = 1;

	public static final int EDGES_ONLY_FORMAT = 2;

	public static final int EDGES_ONLY_STARTING_AT_1_FORMAT = 3;

	public static final int GML_FORMAT = 5;

	public static final int BIDIRECTIONAL_EDGES_ONLY_FORMAT = 6;

	public static final int ARBITRARY_IDS = 7;

	public static Graph read(String filename, int TYPE, String name) {
		Graph g = null;
		if (TYPE == OWN_FORMAT) {
			g = read(filename);
		} else if (TYPE == EDGES_ONLY_FORMAT) {
			g = edgesOnly(filename, name);
		} else if (TYPE == EDGES_ONLY_STARTING_AT_1_FORMAT) {
			g = edgesOnlyStartingAt1(filename, name);
		} else if (TYPE == GML_FORMAT) {
			g = gml(filename, name);
		} else if (TYPE == BIDIRECTIONAL_EDGES_ONLY_FORMAT) {
			g = bidirectionalEdgesOnly(filename, name);
		} else if (TYPE == ARBITRARY_IDS) {
			g = arbitraryIDs(filename, name);
		}
		return g;
	}

	public static int nodes(String filename, int TYPE) {
		int nodes = -1;
		if (TYPE == OWN_FORMAT) {
			nodes = nodes(filename);
		} else if (TYPE == EDGES_ONLY_FORMAT) {
			nodes = nodesEdgesOnly(filename);
		} else if (TYPE == EDGES_ONLY_STARTING_AT_1_FORMAT) {
			nodes = nodesEdgesOnlyStartingAt1(filename);
		} else if (TYPE == BIDIRECTIONAL_EDGES_ONLY_FORMAT) {
			nodes = nodesEdgesOnly(filename);
		}
		return nodes;
	}

	public static Graph read(String filename) {
		Timer timer = new Timer();
		Filereader fr = new Filereader(filename);
		String name = fr.readLine();
		int numberOfNodes = Integer.parseInt(fr.readLine());
		int numberOfEdges = Integer.parseInt(fr.readLine());
		NodeImpl[] nodes = NodeImpl.init(numberOfNodes);
		Edges edges = new Edges(nodes, numberOfEdges);
		String line = "";
		while ((line = fr.readLine()) != null) {
			String n[] = line.split(GraphWriter.DELIMITER);
			int u = Integer.parseInt(n[0]);
			int v = Integer.parseInt(n[1]);
			edges.add(nodes[u], nodes[v]);
		}
		fr.close();
		edges.fill();
		timer.end();
		Graph graph = new Graph(name, nodes, timer);
		return graph;
	}

	private static int nodes(String filename) {
		Filereader fr = new Filereader(filename);
		fr.readLine();
		int numberOfNodes = Integer.parseInt(fr.readLine());
		fr.close();
		return numberOfNodes;
	}

	private static Graph edgesOnly(String filename, String name) {
		Timer timer = new Timer();
		String delimiter = Config.get("GRAPH_READER_EDGES_ONLY_DELIMITER");
		Filereader fr = new Filereader(filename);
		int numberOfNodes = -1;
		int numberOfEdges = 0;
		String line = "";
		while ((line = fr.readLine()) != null) {
			String[] edge = line.split(delimiter);
			int u = Integer.parseInt(edge[0]);
			int v = Integer.parseInt(edge[1]);
			if (u > numberOfNodes) {
				numberOfNodes = u;
			}
			if (v > numberOfNodes) {
				numberOfNodes = v;
			}
			numberOfEdges++;
		}
		numberOfNodes++;
		fr.close();
		NodeImpl[] nodes = NodeImpl.init(numberOfNodes);
		Edges edges = new Edges(nodes, numberOfEdges);
		fr = new Filereader(filename);
		while ((line = fr.readLine()) != null) {
			String[] edge = line.split(delimiter);
			int u = Integer.parseInt(edge[0]);
			int v = Integer.parseInt(edge[1]);
			edges.add(nodes[u], nodes[v]);
		}
		fr.close();
		edges.fill();
		timer.end();
		return new Graph(name, nodes, timer);
	}

	private static int nodesEdgesOnly(String filename) {
		String delimiter = Config.get("GRAPH_READER_EDGES_ONLY_DELIMITER");
		Filereader fr = new Filereader(filename);
		int max = -1;
		String line = "";
		while ((line = fr.readLine()) != null) {
			String[] edge = line.split(delimiter);
			int u = Integer.parseInt(edge[0]);
			int v = Integer.parseInt(edge[1]);
			if (u > max) {
				max = u;
			}
			if (v > max) {
				max = v;
			}
		}
		fr.close();
		return max + 1;
	}

	private static Graph edgesOnlyStartingAt1(String filename, String name) {
		Timer timer = new Timer();
		String delimiter = Config
				.get("GRAPH_READER_EDGES_ONLY_STARTING_AT_1_DELIMITER");
		Filereader fr = new Filereader(filename);
		int numberOfNodes = 0;
		int numberOfEdges = 0;
		String line = "";
		while ((line = fr.readLine()) != null) {
			String[] edge = line.split(delimiter);
			int u = Integer.parseInt(edge[0]);
			int v = Integer.parseInt(edge[1]);
			if (u > numberOfNodes) {
				numberOfNodes = u;
			}
			if (v > numberOfNodes) {
				numberOfNodes = v;
			}
			numberOfEdges++;
		}
		fr.close();
		NodeImpl[] nodes = NodeImpl.init(numberOfNodes);
		Edges edges = new Edges(nodes, numberOfNodes);
		fr = new Filereader(filename);
		while ((line = fr.readLine()) != null) {
			String[] edge = line.split(delimiter);
			int u = Integer.parseInt(edge[0]) - 1;
			int v = Integer.parseInt(edge[1]) - 1;
			edges.add(nodes[u], nodes[v]);
		}
		fr.close();
		edges.fill();
		timer.end();
		return new Graph(name, nodes, timer);
	}

	private static int nodesEdgesOnlyStartingAt1(String filename) {
		String delimiter = Config
				.get("GRAPH_READER_EDGES_ONLY_STARTING_AT_1_DELIMITER");
		Filereader fr = new Filereader(filename);
		int max = -1;
		String line = "";
		while ((line = fr.readLine()) != null) {
			String[] edge = line.split(delimiter);
			int u = Integer.parseInt(edge[0]);
			int v = Integer.parseInt(edge[1]);
			if (u > max) {
				max = u;
			}
			if (v > max) {
				max = v;
			}
		}
		fr.close();
		return max;
	}

	private static Graph gml(String filename, String name) {
		Timer timer = new Timer();
		Filereader fr = new Filereader(filename);
		int index = 0;
		String line = "";
		HashMap<String, Integer> ids = new HashMap<String, Integer>();
		NodeImpl[] nodes = null;
		while ((line = fr.readLine()) != null) {
			int counter = 0;
			if (line.equals("node [")) {
				while ((line = fr.readLine()) != null) {
					if (line.contains("id")) {
						int start = line.indexOf("\"");
						int end = line.lastIndexOf("\"");
						String id = line.subSequence(start + 1, end).toString();
						ids.put(id, index++);
					}
					if (line.contains("[")) {
						counter++;
					}
					if (line.contains("]")) {
						counter--;
					}
					if (counter == -1) {
						break;
					}
				}
			}
		}
		fr.close();
		nodes = NodeImpl.init(index + 1);
		fr = new Filereader(filename);
		Edges edges = new Edges(nodes, nodes.length);
		while ((line = fr.readLine()) != null) {
			int counter = 0;
			if (line.equals("edge [")) {
				String src = null;
				String dst = null;
				while ((line = fr.readLine()) != null) {
					if (line.contains("source")) {
						int start = line.indexOf("\"");
						int end = line.lastIndexOf("\"");
						src = line.subSequence(start + 1, end).toString();
					}
					if (line.contains("target")) {
						int start = line.indexOf("\"");
						int end = line.lastIndexOf("\"");
						dst = line.subSequence(start + 1, end).toString();
					}
					if (line.contains("[")) {
						counter++;
					}
					if (line.contains("]")) {
						counter--;
					}
					if (counter == -1) {
						if (src != null && dst != null) {
							if (ids.containsKey(src) && ids.containsKey(dst)) {
								int srcIndex = ids.get(src);
								int dstIndex = ids.get(dst);
								edges.add(nodes[srcIndex], nodes[dstIndex]);
							} else {
								if (!ids.containsKey(src)) {
									System.out.println("key \"" + src
											+ "\" not declared as a node");
								}
								if (!ids.containsKey(dst)) {
									System.out.println("key \"" + dst
											+ "\" not declared as a node");
								}
							}
						} else {
							System.out.println("ERROR!!!!!!!");
						}
						break;
					}
				}
			}
		}
		fr.close();
		edges.fill();
		timer.end();
		return new Graph(name, nodes, timer);
	}

	private static Graph bidirectionalEdgesOnly(String filename, String name) {
		Timer timer = new Timer();
		String delimiter = Config.get("GRAPH_READER_EDGES_ONLY_DELIMITER");
		Filereader fr = new Filereader(filename);
		int numberOfNodes = -1;
		int numberOfEdges = 0;
		String line = "";
		while ((line = fr.readLine()) != null) {
			String[] edge = line.split(delimiter);
			int u = Integer.parseInt(edge[0]);
			int v = Integer.parseInt(edge[1]);
			if (u > numberOfNodes) {
				numberOfNodes = u;
			}
			if (v > numberOfNodes) {
				numberOfNodes = v;
			}
			numberOfEdges++;
		}
		numberOfNodes++;
		fr.close();
		NodeImpl[] nodes = NodeImpl.init(numberOfNodes);
		Edges edges = new Edges(nodes, numberOfEdges);
		fr = new Filereader(filename);
		while ((line = fr.readLine()) != null) {
			String[] edge = line.split(delimiter);
			int u = Integer.parseInt(edge[0]);
			int v = Integer.parseInt(edge[1]);
			edges.add(nodes[u], nodes[v]);
			edges.add(nodes[v], nodes[u]);
		}
		fr.close();
		edges.fill();
		timer.end();
		return new Graph(name, nodes, timer);
	}

	private static Graph arbitraryIDs(String filename, String name) {
		Timer timer = new Timer();
		String delimiter = Config.get("GRAPH_ARBITRARY_IDS_DELIMITER");
		Filereader fr = new Filereader(filename);
		Hashtable<String, Integer> ids = new Hashtable<String, Integer>();
		String line = null;
		int index = 0;
		int numberOfEdges = 0;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(delimiter);
			String from = temp[0].trim();
			String to = temp[1].trim();
			if (!ids.containsKey(from)) {
				ids.put(from, index++);
			}
			if (!ids.containsKey(to)) {
				ids.put(to, index++);
			}
			numberOfEdges++;
		}
		fr.close();
		NodeImpl[] nodes = NodeImpl.init(ids.size());
		Edges edges = new Edges(nodes, numberOfEdges);
		fr = new Filereader(filename);
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(delimiter);
			String from = temp[0].trim();
			String to = temp[1].trim();
			int f = ids.get(from);
			int t = ids.get(to);
			edges.add(nodes[f], nodes[t]);
		}
		fr.close();
		edges.fill();
		timer.end();
		return new Graph(name, nodes, timer);
	}
}

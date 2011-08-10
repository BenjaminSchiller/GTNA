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
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.util.Config;

import java.io.File;
import java.io.FilenameFilter;

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

	public static Graph readWithProperties(String filename) {
		File file = new File(filename);
		File folder = file.getParentFile();
		String del = Config.get("GRAPH_WRITER_PROPERTY_FILE_DELIMITER");
		PrefixFilter filter = new PrefixFilter(file.getName() + del);
		File[] propertyFiles = folder.listFiles(filter);
		String[] properties = new String[propertyFiles.length];
		for (int i = 0; i < propertyFiles.length; i++) {
			properties[i] = propertyFiles[i].getAbsolutePath();
		}
		return GraphReader.readWithProperties(filename, properties);
	}

	private static class PrefixFilter implements FilenameFilter {
		String prefix;

		private PrefixFilter(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.startsWith(this.prefix);
		}

	}

	public static Graph readWithProperties(String filename, String[] properties) {
		Graph graph = GraphReader.read(filename);
		for (String prop : properties) {
			Filereader fr = new Filereader(prop);
			String className = fr.readLine();
			fr.close();
			try {
				GraphProperty property = (GraphProperty) ClassLoader
						.getSystemClassLoader().loadClass(className)
						.newInstance();
				property.read(prop, graph);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
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

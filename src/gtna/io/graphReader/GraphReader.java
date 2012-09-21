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
 * GraphReader.java
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

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.util.Config;
import gtna.util.filenameFilter.PrefixFilenameFilter;

import java.io.File;

/**
 * @author benni
 * 
 */
public abstract class GraphReader {
	protected String key;

	/**
	 * reads a graph from the specified file
	 * 
	 * @param filename
	 *            filename of the graph
	 * @return graph read from the specified file
	 */
	public abstract Graph read(String filename);

	/**
	 * extracts the number of nodes from the graph
	 * 
	 * @param filename
	 *            filename of the graph
	 * @return number of nodes in the graph
	 */
	public abstract int nodes(String filename);

	/**
	 * 
	 * @param key
	 *            key of this graph reader
	 */
	public GraphReader(String key) {
		this.key = key;
	}

	/**
	 * returns the key of this graph reader
	 * 
	 * @return key of this graph reader
	 */
	public String getKey() {
		return this.key;
	}

	protected String getGraphName(String filename) {
		return this.getKey() + " read from " + new File(filename).getName();
	}

	/**
	 * reads a graph from the given file. properties are read from all files in
	 * the same folder that start with
	 * ${filename}Config.get("GRAPH_WRITER_PROPERTY_FILE_DELIMITER").
	 * 
	 * @param filename
	 *            filename of the graph
	 * @return graph with properties
	 */
	public Graph readWithProperties(String filename) {
		File file = new File(filename);
		File folder = file.getParentFile();

		String del = Config.get("GRAPH_WRITER_PROPERTY_FILE_DELIMITER");
		PrefixFilenameFilter filter = new PrefixFilenameFilter(file.getName()
				+ del);

		File[] propertyFiles = folder.listFiles(filter);
		String[] properties = new String[propertyFiles.length];
		for (int i = 0; i < propertyFiles.length; i++) {
			properties[i] = propertyFiles[i].getAbsolutePath();
		}

		return this.readWithProperties(filename, properties);
	}

	/**
	 * reads a graph from the given file. properties are read from the files
	 * listed in properties.
	 * 
	 * @param filename
	 *            filename of the graph
	 * @param properties
	 *            list of property filenames
	 * @return graph with properties
	 */
	public Graph readWithProperties(String filename, String[] properties) {
		Graph graph = this.read(filename);
		if (graph == null) {
			return null;
		}
		for (String prop : properties) {
			Filereader fr = null;
			try {
				fr = new Filereader(prop);
				String className = fr.readLine();
				GraphProperty property = (GraphProperty) ClassLoader
						.getSystemClassLoader().loadClass(className)
						.newInstance();
				String key = property.read(prop);
				graph.addProperty(key, property);
			} catch (InstantiationException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			} catch (ClassNotFoundException e) {
				return null;
			} finally {
				fr.close();
			}
		}
		return graph;
	}
}

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
 * GraphWriter.java
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

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public abstract class GraphWriter {
	protected String key;

	/**
	 * writes the given graph to the specified filename
	 * 
	 * @param g
	 *            graph
	 * @param filename
	 *            destination filename
	 * @return true, if the graph was successfully written; false otherwise
	 */
	public abstract boolean write(Graph g, String filename);

	/**
	 * 
	 * @param key
	 *            key of this graph reader
	 */
	public GraphWriter(String key) {
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

	/**
	 * writes the given graph to the specified file. all graph properties are
	 * written to
	 * ${filename}Config.get("GRAPH_WRITER_PROPERTY_FILE_DELIMITER")${key}
	 * 
	 * @param g
	 *            graph
	 * @param filename
	 *            destination filename
	 * @return true, if the graph and all properties were written successfully;
	 *         false otherwise
	 */
	public boolean writeWithProperties(Graph g, String filename) {
		if (!this.write(g, filename)) {
			return false;
		}
		String del = Config.get("GRAPH_WRITER_PROPERTY_FILE_DELIMITER");
		for (String key : g.getProperties().keySet()) {
			String dst = filename + del + key;
			GraphProperty gp = g.getProperty(key);
			if (!gp.write(dst, key)) {
				return false;
			}
		}
		return true;
	}

}

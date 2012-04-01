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
 * ReadableList.java
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
package gtna.networks.util;

import gtna.graph.Graph;
import gtna.io.GraphReader;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Config;

/**
 * Implements a graph generator for a list of snapshots. It works like the
 * ReadableFile network generator and simply takes a list of snapshots as
 * parameter. Every time the generate method is called, the next snapshot in the
 * list is used. Therefore, LIST.length different graphs can be generated with
 * this implementation. Hence, if a series with more than LIST.length iterations
 * is generated, there will be duplicates in the "generated" network graphs.
 * 
 * The parameters are the list snapshot filename and the type (as defined by the
 * GraphReader class).
 * 
 * @author benni
 * 
 */
public class ReadableList extends Network {
	private String[] files;
    private int index;

	public ReadableList(String name, String folder, String[] files,
			Transformation[] t) {
		super(ReadableList.key(name, folder), Integer.MIN_VALUE, t);
		this.files = files;
		this.index = -1;
		super.setNodes(GraphReader.nodes(this.files[0]));
	}

	public static String key(String name, String folder) {
		Config.overwrite("READABLE_LIST_" + folder + "_NAME", name);
		Config.overwrite("READABLE_LIST_" + folder + "_NAME_SHORT", name);
		Config.overwrite("READABLE_LIST_" + folder + "_NAME_LONG", name);
		Config.overwrite("READABLE_LIST_" + folder + "_FOLDER", folder);
		return "READABLE_LIST_" + folder;
	}

	public Graph generate() {
		this.index = (this.index + 1) % this.files.length;
		Graph graph = GraphReader.readWithProperties(this.files[this.index]);
		graph.setName(this.getDescription());
		return graph;
	}

}

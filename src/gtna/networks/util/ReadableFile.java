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
 * ReadableFile.java
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
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Config;

/**
 * Implements a network generator that reads an existing network topology
 * snapshot from a specified file. All types of snapshots can be used which are
 * supported by the GraphReader class. Since only one file is specified,
 * multiple calls of the generate method always result in the exact same
 * topology.
 * 
 * The parameters are the filename of the snapshot and the type (as defined by
 * the GraphReader class).
 * 
 * @author benni
 * 
 */
public class ReadableFile extends NetworkImpl implements Network {
	private String FILENAME;

	private int TYPE;

	public ReadableFile(String KEY, String FILENAME, int TYPE,
			RoutingAlgorithm ra, Transformation[] t) {
		super(KEY, Integer.MIN_VALUE, new String[] {}, new String[] {}, ra, t);
		this.FILENAME = FILENAME;
		this.TYPE = TYPE;
		super.setNodes(GraphReader.nodes(this.FILENAME, this.TYPE));
	}

	public ReadableFile(String name, String folder, String FILENAME, int TYPE,
			RoutingAlgorithm ra, Transformation[] t) {
		super(key(name, folder), Integer.MIN_VALUE, new String[] {},
				new String[] {}, ra, t);
		this.FILENAME = FILENAME;
		this.TYPE = TYPE;
		super.setNodes(GraphReader.nodes(this.FILENAME, this.TYPE));
	}

	public static String key(String name, String folder) {
		Config.overwrite("READABLE_FILE_" + folder + "_NAME", name);
		Config.overwrite("READABLE_FILE_" + folder + "_FOLDER", folder);
		return "READABLE_FILE_" + folder;
	}

	public Graph generate() {
		return GraphReader.read(this.FILENAME, this.TYPE, this.description());
	}
}

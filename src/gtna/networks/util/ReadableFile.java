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
import gtna.io.graphReader.GtnaGraphReader;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.parameter.Parameter;

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
public class ReadableFile extends Network {
	private String filename;

	public ReadableFile(String name, String folder, String filename,
			Transformation[] t) {
		this(name, folder, filename, new Parameter[0], t);
	}

	public ReadableFile(String name, String folder, String filename,
			Parameter[] parameters, Transformation[] t) {
		super(ReadableFile.key(folder, name), new GtnaGraphReader()
				.nodes(filename), parameters, t);
		this.filename = filename;
		for (Parameter p : parameters) {
			ReadableFile.parameterKey(folder, p.getKey(), p.getKey());
		}
	}

	public static String key(String folder, String name) {
		Config.overwrite("READABLE_FILE_" + folder + "_NAME", name);
		Config.overwrite("READABLE_FILE_" + folder + "_NAME_SHORT", name);
		Config.overwrite("READABLE_FILE_" + folder + "_NAME_LONG", name);
		Config.overwrite("READABLE_FILE_" + folder + "_FOLDER", folder);
		return "READABLE_FILE_" + folder;
	}

	public static void parameterKey(String folder, String key, String name) {
		Config.overwrite("READABLE_FILE_" + folder + "_" + key + "_NAME", name);
		Config.overwrite("READABLE_FILE_" + folder + "_" + key + "_NAME_SHORT",
				name);
		Config.overwrite("READABLE_FILE_" + folder + "_" + key + "_NAME_LONG",
				name);
	}

	public Graph generate() {
		Graph graph = new GtnaGraphReader().readWithProperties(this.filename);
		graph.setName(this.getDescription());
		return graph;
	}
}

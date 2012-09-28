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
 * ReadableFolder.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.util;

import gtna.graph.Graph;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.filenameFilter.SuffixFilenameFilter;
import gtna.util.parameter.Parameter;

import java.io.File;
import java.util.Arrays;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class ReadableFolder extends Network {
	private File[] files;

	private int index;

	public ReadableFolder(String name, String folder, String srcFolder,
			String suffix, Transformation[] t) {
		this(name, folder, srcFolder, suffix, new Parameter[0], t);
	}

	public ReadableFolder(String name, String folder, String srcFolder,
			String suffix, Parameter[] parameters, Transformation[] t) {
		super(ReadableFolder.key(folder, name), ReadableFolder.getNodes(
				srcFolder, suffix), parameters, t);
		File d = new File(srcFolder);
		if (!d.exists()) {
			this.files = new File[0];
		} else {
			this.files = d.listFiles(new SuffixFilenameFilter(suffix));
			Arrays.sort(this.files);
		}
		this.index = 0;
		for (Parameter p : parameters) {
			ReadableFolder.parameterKey(folder, p.getKey(), p.getKey());
		}
	}

	public Graph generate() {
		if (this.files.length == 0) {
			return null;
		}
		Graph graph = new GtnaGraphReader()
				.readWithProperties(this.files[this.index].getAbsolutePath());
		graph.setName(this.getDescription());
		this.incIndex();
		return graph;
	}

	private static int getNodes(String srcFolder, String suffix) {
		File d = new File(srcFolder);
		if (!d.exists()) {
			return 0;
		}
		File[] f = d.listFiles(new SuffixFilenameFilter(suffix));
		if (f.length == 0) {
			return 0;
		}
		return new GtnaGraphReader().nodes(f[0].getAbsolutePath());
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

	public void incIndex() {
		this.index = (this.index + 1) % this.files.length;
	}

	public File[] getFiles() {
		return this.files;
	}
}

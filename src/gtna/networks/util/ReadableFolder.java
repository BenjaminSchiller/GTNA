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
import gtna.io.GraphReader;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Config;

import java.io.File;
import java.util.ArrayList;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class ReadableFolder extends Network {
	private ArrayList<String> files;

	private int index;

	private String[] properties;

	public ReadableFolder(String name, String folder, String srcFolder,
			String extension, Transformation[] t) {
		super(ReadableFolder.key(name, folder), Integer.MIN_VALUE, t);
		File d = new File(srcFolder);
		if (!d.exists()) {
			this.files = new ArrayList<String>();
		} else {
			File[] f = d.listFiles();
			this.files = new ArrayList<String>();
			for (int i = 0; i < f.length; i++) {
				if (f[i].getName().endsWith(extension)) {
					this.files.add(f[i].getAbsolutePath());
				}
			}
		}
		this.index = -1;
		if (this.files.size() == 0) {
			super.setNodes(0);
		} else {
			super.setNodes(GraphReader.nodes(this.files.get(0)));
		}
		this.properties = properties;
	}

	public static String key(String name, String folder) {
		Config.overwrite("READABLE_FOLDER_" + folder + "_NAME", name);
		Config.overwrite("READABLE_FOLDER_" + folder + "_NAME_SHORT", name);
		Config.overwrite("READABLE_FOLDER_" + folder + "_NAME_LONG", name);
		Config.overwrite("READABLE_FOLDER_" + folder + "_FOLDER", folder);
		return "READABLE_FOLDER_" + folder;
	}

	public Graph generate() {
		if (this.files.size() == 0) {
			return null;
		}
		this.index = (this.index + 1) % this.files.size();
		Graph graph = GraphReader.read(this.files.get(this.index));
		graph.setName(this.getDescription());
		return graph;
	}

	public ArrayList<String> getFiles() {
		return this.files;
	}
}

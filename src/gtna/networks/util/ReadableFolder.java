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
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Config;

import java.io.File;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class ReadableFolder extends NetworkImpl implements Network {
	private int type;

	private String[] files;

	private int index;

	public ReadableFolder(String name, String folder, String src, int type,
			RoutingAlgorithm ra, Transformation[] t) {
		super(key(name, folder), Integer.MIN_VALUE, new String[] {},
				new String[] {}, ra, t);
		this.type = type;
		File d = new File(src);
		if (!d.exists()) {
			this.files = new String[0];
		} else {
			File[] f = d.listFiles();
			this.files = new String[f.length];
			for (int i = 0; i < f.length; i++) {
				this.files[i] = f[i].getAbsolutePath();
			}
		}
		this.index = -1;
		if (this.files.length == 0) {
			super.setNodes(0);
			// TODO remove again, just a quick fix for LMC paper
			if (super.nodes() == 0) {
				super.setNodes(9223);
			}
		} else {
			super.setNodes(GraphReader.nodes(this.files[0], this.type));
			// TODO remove again, just a quick fix for LMC paper
			if (super.nodes() == 0) {
				super.setNodes(9223);
			}
		}
	}

	public static String key(String name, String folder) {
		Config.overwrite("READABLE_FOLDER_" + folder + "_NAME", name);
		Config.overwrite("READABLE_FOLDER_" + folder + "_FOLDER", folder);
		return "READABLE_FOLDER_" + folder;
	}

	public Graph generate() {
		if (this.files.length == 0) {
			return null;
		}
		this.index = (index + 1) % this.files.length;
		return GraphReader.read(this.files[this.index], this.type, this
				.description());
	}

	public String[] getFiles() {
		return this.files;
	}
}

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
 * NodeColors.java
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
package gtna.drawing;

import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

import java.awt.Color;

/**
 * @author benni
 * 
 */
public class NodeColors extends GraphProperty {

	private Color[] colors;

	public NodeColors() {
		this.colors = new Color[0];
	}

	public NodeColors(Color[] colors) {
		this.colors = colors;
	}

	public Color[] getColors() {
		return this.colors;
	}

	protected String asString(Color c) {
		return c.getRed() + "/" + c.getGreen() + "/" + c.getBlue();
	}

	protected Color fromString(String s) {
		String[] temp = s.split("/");
		int r = Integer.parseInt(temp[0]);
		int g = Integer.parseInt(temp[1]);
		int b = Integer.parseInt(temp[2]);
		return new Color(r, g, b);
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		// CLASS
		fw.writeComment(Config.get("GRAPH_PROPERTY_CLASS"));
		fw.writeln(this.getClass().getCanonicalName().toString());

		// KEYS
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);

		// # OF NODES
		fw.writeComment("# of Nodes");
		fw.writeln(this.colors.length);

		fw.writeln();

		// LIST OF NODE COLORS
		for (Color color : this.colors) {
			fw.writeln(this.asString(color));
		}

		return fw.close();
	}

	@Override
	public String read(String filename) {
		Filereader fr = new Filereader(filename);

		// CLASS
		fr.readLine();

		// KEYS
		String key = fr.readLine();

		// # OF NODES
		int nodes = Integer.parseInt(fr.readLine());
		this.colors = new Color[nodes];

		// NODE COLORS
		String line = null;
		int index = 0;
		while ((line = fr.readLine()) != null) {
			this.colors[index++] = this.fromString(line);
		}

		fr.close();

		return key;
	}

}

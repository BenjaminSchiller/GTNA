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

	@Override
	public boolean write(String filename, String key) {
		// TODO implement GraphProperty.write(...)
		return false;
	}

	@Override
	public String read(String filename) {
		// TODO implement GraphProperty.read(...)
		return null;
	}

}

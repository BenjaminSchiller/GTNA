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
 * RemoveGraphProperty.java
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
package gtna.transformation.util;

import gtna.graph.Graph;
import gtna.transformation.Transformation;

/**
 * @author benni
 * 
 */
public class RemoveGraphProperty extends Transformation {
	public static enum RemoveType {
		ALL, ALL_OF_TYPE, SELECTED
	};

	private RemoveType type;

	private String[] remove;

	public RemoveGraphProperty() {
		this(RemoveType.ALL, new String[0]);
	}

	public RemoveGraphProperty(RemoveType type, String remove) {
		this(type, new String[] { remove });
	}

	public RemoveGraphProperty(RemoveType type, String[] remove) {
		super("REMOVE_GRAPH_PROPERTY");
		this.type = type;
		this.remove = remove;
	}

	@Override
	public Graph transform(Graph g) {
		switch (this.type) {

		case ALL:
			g.getProperties().clear();
			break;

		case ALL_OF_TYPE:
			for (String rm : this.remove) {
				int index = 0;
				while (g.hasProperty(rm + "_" + index)) {
					g.getProperties().remove(rm + "_" + index);
					index++;
				}
			}
			break;

		case SELECTED:
			for (String rm : this.remove) {
				g.getProperties().remove(rm);
			}
			break;
		}

		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

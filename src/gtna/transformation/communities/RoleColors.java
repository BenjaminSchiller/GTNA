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
 * RoleColors.java
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
package gtna.transformation.communities;

import gtna.communities.Role.RoleType;
import gtna.communities.RoleList;
import gtna.drawing.NodeColors;
import gtna.graph.Graph;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.awt.Color;

/**
 * @author benni
 * 
 */
public class RoleColors extends Transformation {

	private RoleType type;

	public RoleColors(RoleType type) {
		super("ROLE_COLORS", new Parameter[] { new StringParameter("TYPE",
				type.toString()) });
		this.type = type;
	}

	private static final Color[] colors2 = new Color[] { Color.green,
			Color.red, Color.blue, Color.cyan, Color.black, Color.orange,
			Color.yellow, Color.MAGENTA, Color.pink, Color.darkGray, Color.gray };

	private static final int c1 = 255;

	private static final int c2 = 100;

	private static final Color[] colors = new Color[] { new Color(c1, 0, 0),
			new Color(0, c1, 0), new Color(0, 0, c1), new Color(c2, 0, 0),
			new Color(0, c2, 0), new Color(0, 0, c2) };

	@Override
	public Graph transform(Graph g) {
		RoleList roles = (RoleList) g.getProperty("ROLES_"
				+ this.type.toString() + "_0");

		Color[] colors = new Color[g.getNodeCount()];
		for (int i = 0; i < g.getNodeCount(); i++) {
			colors[i] = RoleColors.colors[roles.getRole(i).toIndex()
					% RoleColors.colors.length];
		}
		NodeColors nc = new NodeColors(colors);
		g.addProperty(g.getNextKey("NODE_COLORS"), nc);
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("ROLES_" + this.type.toString() + "_0");
	}

}

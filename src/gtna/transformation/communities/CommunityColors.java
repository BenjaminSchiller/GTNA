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
 * CommunityColors.java
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

import gtna.communities.CommunityList;
import gtna.drawing.NodeColors;
import gtna.graph.Graph;
import gtna.transformation.Transformation;

import java.awt.Color;

/**
 * @author benni
 * 
 */
public class CommunityColors extends Transformation {

	/**
	 * @param key
	 * @param configKeys
	 * @param configValues
	 */
	public CommunityColors() {
		super("COMMUNITY_COLORS");
	}

	@Override
	public Graph transform(Graph g) {
		CommunityList c = (CommunityList) g.getProperty("COMMUNITIES_0");
		Color[] C = this.getColors(c.getCommunities().length);
		Color[] colors = new Color[g.getNodes().length];
		for (int i = 0; i < g.getNodes().length; i++) {
			colors[i] = C[c.getCommunityOfNode(i).getIndex()];
		}
		NodeColors cc = new NodeColors(colors);
		g.addProperty(g.getNextKey("NODE_COLORS"), cc);
		return g;
	}

	private Color[] getColors(int number) {
		Color[] init = new Color[] { Color.green, Color.red, Color.blue,
				Color.cyan, Color.black, Color.orange, Color.yellow,
				Color.MAGENTA, Color.pink, Color.darkGray, Color.gray };
		Color[] c = new Color[number];
		for (int i = 0; i < c.length; i++) {
			c[i] = init[i % init.length];
		}
		return c;

		// if (number <= init.length || true) {
		// }

		// int steps = (int) Math.min(255.0,
		// 255.0 / Math.ceil(((double) (number) / 3.0)));
		// int c1 = 0;
		// int c2 = 0;
		// int c3 = 0;
		// Color[] c = new Color[number];
		// for (int i = 0; i < c.length; i++) {
		// c[i] = new Color(c1, c2, c3);
		// if ((i % 3) == 0) {
		// c1 += steps;
		// } else if ((i % 3) == 1) {
		// c2 += steps;
		// } else {
		// c3 += steps;
		// }
		// }
		// return c;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("COMMUNITIES_0");
	}

}

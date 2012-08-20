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
package gtna.transformation.communities;

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
public class NodeColor extends Transformation {

	private Color color;

	public NodeColor(Color color) {
		super("NODE_COLOR",
				new Parameter[] { new StringParameter("COLOR", color.getRed()
						+ "-" + color.getGreen() + "-" + color.getBlue()) });
		this.color = color;
	}

	@Override
	public Graph transform(Graph g) {
		Color[] colors = new Color[g.getNodeCount()];
		for (int i = 0; i < g.getNodeCount(); i++) {
			colors[i] = this.color;
		}
		NodeColors nc = new NodeColors(colors);
		g.addProperty(g.getNextKey("NODE_COLORS"), nc);
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

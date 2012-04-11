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
 * RemoveMax.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.remove;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author stef remove all nodes whose degree exceeds a certian bound
 */
public class RemoveLargest extends RemoveNodes {
	int max;
	Type type;

	public static enum Type {
		IN, OUT, TOTAL
	}

	/**
	 * @param key
	 * @param parameters
	 */
	public RemoveLargest(int max, Type type) {
		super("REMOVE_LARGEST", new Parameter[] { new IntParameter("MAX", max),
				new StringParameter("TYPE", type.toString()) });
		this.max = max;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.remove.RemoveNodes#getNodeSet(gtna.graph.Graph)
	 */
	@Override
	public boolean[] getNodeSet(Graph g) {
		Node[] nodes = g.getNodes();
		boolean[] remove = new boolean[nodes.length];
		for (int j = 0; j < nodes.length; j++) {
			if (this.type == Type.TOTAL && nodes[j].getDegree() > this.max) {
				remove[j] = true;
			}
			if (this.type == Type.IN && nodes[j].getInDegree() > this.max) {
				remove[j] = true;
			}
			if (this.type == Type.OUT && nodes[j].getOutDegree() > this.max) {
				remove[j] = true;
			}
		}
		return remove;
	}

}

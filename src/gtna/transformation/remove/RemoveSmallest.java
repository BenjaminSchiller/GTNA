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
 * RemoveSmallest.java
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
import gtna.transformation.remove.RemoveLargest.Type;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author stef
 * 
 */
public class RemoveSmallest extends RemoveNodes {

	int min;
	Type type;

	public static enum Type {
		IN, OUT, TOTAL
	}

	/**
	 * @param key
	 * @param parameters
	 */
	public RemoveSmallest(int min, Type type) {
		super("REMOVE_SMALLEST", new Parameter[] {
				new IntParameter("MIN", min),
				new StringParameter("TYPE", type.toString()) });
		this.min = min;
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
			if (this.type == Type.TOTAL && nodes[j].getDegree() < this.min) {
				remove[j] = true;
			}
			if (this.type == Type.IN && nodes[j].getInDegree() < this.min) {
				remove[j] = true;
			}
			if (this.type == Type.OUT && nodes[j].getOutDegree() < this.min) {
				remove[j] = true;
			}
		}
		return remove;
	}

}

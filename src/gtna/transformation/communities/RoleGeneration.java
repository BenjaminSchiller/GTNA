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
 * Roles.java
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

import gtna.communities.Communities;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class RoleGeneration extends TransformationImpl implements
		Transformation {

	public RoleGeneration() {
		super("ROLES", new String[] {}, new String[] {});
	}
	
	// TODO remove
	private int offset;
	
	// TODO remove
	public RoleGeneration(int offset){
		this();
		this.offset = offset;
	}

	@Override
	public Graph transform(Graph g) {
		GraphProperty[] properties = g.getProperties("COMMUNITIES");
		for (GraphProperty gp : properties) {
			Communities communities = (Communities) gp;
			HashMap<Integer, Byte> map = new HashMap<Integer, Byte>();
			// TODO implement - start
			for (Node n : g.getNodes()) {
				map.put(n.getIndex(), (byte) (((n.getIndex() + this.offset) % 7) + 1));
			}
			// TODO implement - end
			g.addProperty(g.getNextKey("ROLES"),
					new gtna.communities.Roles(map));
		}
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("COMMUNITIES_0");
	}

}

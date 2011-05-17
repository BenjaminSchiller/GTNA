/*
 * ===========================================================
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
 * GridNodeManhattan.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.routing.node;

import gtna.graph.NodeImpl;
import gtna.routing.node.identifier.GridIDManhattan;
import gtna.routing.node.identifier.Identifier;

import java.util.Random;

public class GridNodeManhattan extends NodeImpl implements IDNode {
	private GridIDManhattan id;

	public GridNodeManhattan(int index, double[] pos) {
		super(index);
		this.id = new GridIDManhattan(pos);
	}

	public boolean contains(Identifier id) {
		return this.id.equals(id);
	}

	public double dist(Identifier id) {
		return this.id.dist(id);
	}

	public Identifier randomID(Random rand, NodeImpl[] nodes) {
		GridIDManhattan id = ((GridNodeManhattan) nodes[rand
				.nextInt(nodes.length)]).id;
		while (this.contains(id)) {
			id = ((GridNodeManhattan) nodes[rand.nextInt(nodes.length)]).id;
		}
		return id;
	}

	public double dist(IDNode node) {
		return ((GridNodeManhattan) node).id.dist(this.id);
	}
}

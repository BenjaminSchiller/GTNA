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
 * RandomWalk.java
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
package gtna.routing.random;

import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;

import java.util.Random;

public class RandomWalk extends RoutingAlgorithmImpl implements
		RoutingAlgorithm {
	private int ttl;

	private boolean check;

	public RandomWalk(int ttl, boolean check) {
		super("RANDOM_WALK", new String[] { "TTL", "CHECK" }, new String[] {
				"" + ttl, "" + check });
		this.ttl = ttl;
		this.check = check;
	}

	public boolean applicable(NodeImpl[] nodes) {
		return true;
	}

	public void init(NodeImpl[] nodes) {
		return;
	}

	// TODO adapt to return Route object
	public Route randomRoute(NodeImpl[] nodes, NodeImpl src, Random rand) {
		int l = this
				.route(src, src, nodes[rand.nextInt(nodes.length)], rand, 0);
		if (l > this.ttl) {
			return null;
		}
		// return new double[l + 1];
		return null;
	}

	private int route(Node src, Node current, Node dest, Random rand, int hops) {
		if (current.index() == dest.index()) {
			return 0;
		}
		if (hops >= this.ttl) {
			return this.ttl * 2;
		}
		Node[] out = current.out();
		if (this.check) {
			for (int i = 0; i < out.length; i++) {
				if (out[i].index() == dest.index()) {
					return 1;
				}
			}
		}
		return 1 + this.route(src, out[rand.nextInt(out.length)], dest, rand,
				hops + 1);
	}
}

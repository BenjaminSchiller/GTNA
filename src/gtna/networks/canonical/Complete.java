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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * Complete.java
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
package gtna.networks.canonical;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

/**
 * Implements the network generator for a complete / fully-connected network of
 * given size. In a fully-connected network, every node has a connection to
 * every other node.
 * 
 * @author benni
 * 
 */
public class Complete extends NetworkImpl implements Network {
	public Complete(int nodes, RoutingAlgorithm ra, Transformation[] t) {
		super("COMPLETE", nodes, new String[] {}, new String[] {}, ra, t);
	}

	public Graph generate() {
		Timer timer = new Timer();
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		for (int i = 0; i < nodes.length; i++) {
			NodeImpl[] edges = new NodeImpl[nodes.length - 1];
			int index = 0;
			for (int j = 0; j < nodes.length; j++) {
				if (j != i) {
					edges[index++] = nodes[j];
				}
			}
			nodes[i].init(edges, edges);
		}
		timer.end();
		return new Graph(this.description(), nodes, timer);
	}
}

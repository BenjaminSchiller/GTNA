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
 * Kademlia.java
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
package gtna.networks.p2p.kademlia;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomKademliaIDSpace;

/**
 * @author benni
 * 
 */
public class Kademlia extends NetworkImpl implements Network {
	private int bits;
	
	private boolean uniform;

	public Kademlia(int nodes, int bits, boolean uniform, RoutingAlgorithm ra,
			Transformation[] t) {
		super("KADEMLIA", nodes, new String[] { "BITS", "ID_SELECTION" },
				new String[] { "" + bits, uniform ? "uniform" : "random" }, ra,
				t);
		this.bits = bits;
		this.uniform = uniform;
	}

	@Override
	public Graph generate() {
		Graph graph = new Graph(this.description());
		Node[] nodes = Node.init(this.nodes(), graph);
		graph.setNodes(nodes);
		RandomKademliaIDSpace t = new RandomKademliaIDSpace(this.bits, this.uniform);
		graph = t.transform(graph);
		
		// TODO Auto-generated method stub
		return graph;
	}

}

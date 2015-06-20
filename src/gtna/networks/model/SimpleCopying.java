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
 * SimpleCopying.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Dirk;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.transformation.partition.LargestStronglyConnectedComponent;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Dirk
 * 
 */
public class SimpleCopying extends Network {

	private int epn;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public SimpleCopying(int nodes, int epn, Transformation[] transformations) {
		super("SIMPLE_COPYING", nodes, new Parameter[] { new IntParameter(
				"EPN", epn) }, transformations);
		this.epn = epn;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Random rand = new Random(System.currentTimeMillis());
		Graph graph = new Graph(this.getDescription());

		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, this.getNodes() * 10);

		List<Integer>[] inNeighbors;
		List<Integer>[] outNeighbors;
		inNeighbors = new ArrayList[this.getNodes()];
		outNeighbors = new ArrayList[this.getNodes()];
		
		for (int i = 0; i < this.getNodes(); i++) {
			inNeighbors[i] = new ArrayList<Integer>(50);
			outNeighbors[i] = new ArrayList<Integer>(50);
		}
		
		// Init Graph

		LargestStronglyConnectedComponent scc = new LargestStronglyConnectedComponent();
		ErdosRenyi er = new ErdosRenyi(epn, epn+2, false,
				new Transformation[] { scc });

		Graph initGraph = scc.transform(er.generate());
		
		int nc = initGraph.getNodeCount();
		
		for (Node n : initGraph.getNodes()) {
			for (int x : n.getOutgoingEdges()) {
				edges.add(n.getIndex(), x);
				outNeighbors[n.getIndex()].add(x);
				inNeighbors[x].add(n.getIndex());
			}
		}
		

		for (int v = nc+1; v < this.getNodes(); v++) {
			int added = 0;

			HashSet<Integer> visited = new HashSet<Integer>();

			while (added < epn && visited.size()<v) {
				int w = rand.nextInt(v);
				if (!visited.contains(w)) {
					visited.add(w);
					edges.add(v, w);
					outNeighbors[v].add(w);
					inNeighbors[w].add(v);
					added++;

					// Copy edges
					LinkedList<Integer> incoming = new LinkedList<Integer>(
							inNeighbors[w]);
					LinkedList<Integer> outgoing = new LinkedList<Integer>(
							outNeighbors[w]);
					
					Collections.shuffle(incoming);
					Collections.shuffle(outgoing);

					while (added < epn
							&& (incoming.size() > 0 || outgoing.size() > 0)) {
						if (rand.nextBoolean()) {
							if (!incoming.isEmpty()) {
								int u = incoming.poll();
								edges.add(u, v);
								outNeighbors[u].add(v);
								inNeighbors[v].add(u);
								added++;
							}
						} else {
							if (!outgoing.isEmpty()) {
								int u = outgoing.poll();
								edges.add(v, u);
								outNeighbors[v].add(u);
								inNeighbors[u].add(v);
								added++;
							}
						}
					}
				}
			}
			
			System.out.println(v);
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

}

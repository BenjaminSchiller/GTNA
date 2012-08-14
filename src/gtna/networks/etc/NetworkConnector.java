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
 * NetworkConnector.java
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
package gtna.networks.etc;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class NetworkConnector extends Network {

	public static final String separator = "__";

	protected Network[] networks;

	protected int edges;

	protected boolean directed;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public NetworkConnector(Network[] networks, int edges, boolean directed,
			Transformation[] transformations) {
		super("NETWORK_CONNECTOR", getNodes(networks), new Parameter[] {
				new StringParameter("NETWORKS", getFolder(networks)),
				new IntParameter("EDGES", edges) }, transformations);
		this.networks = networks;
		this.edges = edges;
		this.directed = directed;
	}

	private static int getNodes(Network[] networks) {
		int nodes = 0;
		for (Network nw : networks) {
			nodes += nw.getNodes();
		}
		return nodes;
	}

	private static String getFolder(Network[] networks) {
		StringBuffer folder = new StringBuffer();
		for (Network nw : networks) {
			if (folder.length() == 0) {
				folder.append(nw.getFolderName());
			} else {
				folder.append(NetworkConnector.separator + nw.getFolderName());
			}
		}
		return folder.toString();
	}

	@Override
	public Graph generate() {
		Random rand = new Random();

		int offsets[] = new int[this.networks.length];
		for (int i = 1; i < this.networks.length; i++) {
			offsets[i] = offsets[i - 1] + this.networks[i - 1].getNodes();
		}

		Graph graph = new Graph(this.getDescription(), this.getNodes());
		Edges edges = new Edges(graph.getNodes(), this.nodes);

		for (int i = 0; i < this.networks.length; i++) {
			Graph G = this.networks[i].generate();
			for (Transformation t : this.networks[i].getTransformations()) {
				G = t.transform(G);
			}
			Edges E = G.getEdges();

			for (Edge edge : E.getEdges()) {
				int src = edge.getSrc() + offsets[i];
				int dst = edge.getDst() + offsets[i];
				edges.add(src, dst);
			}
		}

		for (int i = 0; i < this.networks.length; i++) {
			for (int j = 0; j < this.networks.length; j++) {
				if (j == i || (!this.directed && j < i)) {
					continue;
				}
				int added = 0;
				while (added < this.edges) {
					int src = rand.nextInt(this.networks[i].getNodes())
							+ offsets[i];
					int dst = rand.nextInt(this.networks[j].getNodes())
							+ offsets[j];
					if (!edges.contains(src, dst)) {
						edges.add(src, dst);
						if (!this.directed) {
							edges.add(dst, src);
						}
						added++;
					}
				}
			}
		}

		edges.fill();

		return graph;
	}
}

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
 * Communities2.java
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
package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Util;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleArrayParameter;
import gtna.util.parameter.IntArray2dParameter;
import gtna.util.parameter.IntArrayParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Random;

public class Communities2 extends Network {
	private int[] sizes;

	private double[] avgInLinks;

	private int[][] interCommunityLinks;

	private boolean bidirectional;

	public Communities2(int[] sizes, double[] avgInLinks,
			int[][] interCommunityLinks, boolean bidirectional,
			Transformation[] t) {
		super("COMMUNITIES_NETWORK_2", Util.sum(sizes), new Parameter[] {
				new IntArrayParameter("COMMUNITY_SIZES", sizes),
				new DoubleArrayParameter("AVG_IN_LINKS", avgInLinks),
				new IntArray2dParameter("INTER_COMMUNITY_LINKS",
						interCommunityLinks),
				new BooleanParameter("BIDIRECTIONAL", bidirectional) }, t);
		this.sizes = sizes;
		this.avgInLinks = avgInLinks;
		this.interCommunityLinks = interCommunityLinks;
		this.bidirectional = bidirectional;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, 0);
		Random rand = new Random(System.currentTimeMillis());
		Node[][] communities = new Node[this.sizes.length][];
		// fill communities
		int index = 0;
		for (int i = 0; i < communities.length; i++) {
			communities[i] = new Node[this.sizes[i]];
			for (int j = 0; j < communities[i].length; j++) {
				communities[i][j] = nodes[index++];
			}
		}
		// create internal links
		for (int i = 0; i < communities.length; i++) {
			double prob = this.avgInLinks[i]
					/ (double) (communities[i].length - 1);
			for (int j = 0; j < communities[i].length; j++) {
				for (int k = 0; k < communities[i].length; k++) {
					if (j != k && rand.nextDouble() <= prob) {
						edges.add(communities[i][j].getIndex(),
								communities[i][k].getIndex());
						if (this.bidirectional) {
							edges.add(communities[i][k].getIndex(),
									communities[i][j].getIndex());
						}
					}
				}
			}
		}
		// create inter-community links
		for (int i = 0; i < this.interCommunityLinks.length; i++) {
			for (int j = 0; j < this.interCommunityLinks[i].length; j++) {
				if (i == j) {
					continue;
				}
				Node[] f = communities[i];
				Node[] t = communities[j];
				ArrayList<Node> from = this.fill(f, t.length);
				ArrayList<Node> to = this.fill(t, f.length);
				this.addInterCommunityEdges(from, to,
						this.interCommunityLinks[i][j], edges, rand);
			}
		}

		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	private ArrayList<Node> fill(Node[] community, int copies) {
		ArrayList<Node> set = new ArrayList<Node>(community.length * copies);
		for (int i = 0; i < community.length; i++) {
			for (int j = 0; j < copies; j++) {
				set.add(community[i]);
			}
		}
		return set;
	}

	private void addInterCommunityEdges(ArrayList<Node> from,
			ArrayList<Node> to, int links, Edges edges, Random rand) {
		int added = 0;
		while (added < links) {
			Node FROM = from.get(rand.nextInt(from.size()));
			Node TO = to.get(rand.nextInt(to.size()));
			from.remove(FROM);
			to.remove(TO);
			if (!edges.contains(FROM.getIndex(), TO.getIndex())) {
				added++;
				edges.add(FROM.getIndex(), TO.getIndex());
				if (this.bidirectional) {
					edges.add(TO.getIndex(), FROM.getIndex());
				}
			}
		}
	}
}

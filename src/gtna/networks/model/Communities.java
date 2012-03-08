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
 * Communities.java
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
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Util;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleArrayParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntArrayParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Random;

public class Communities extends Network {
	private int[] sizes;

	private double[] avgInLinks;

	private double[] avgOutLinks;

	private boolean bidirectional;

	public Communities(int n, double avgCommunitySize, double avgInLinks,
			double avgOutLinks, boolean bidirectional, Transformation[] t) {
		super("COMMUNITIES_NETWORK", n, new Parameter[] {
				new DoubleParameter("AVG_IN_LINKS", avgCommunitySize),
				new DoubleParameter("AVG_IN_LINKS", avgInLinks),
				new DoubleParameter("AVG_OUT_LINKS", avgOutLinks) }, t);
		Random rand = new Random(System.currentTimeMillis());
		this.bidirectional = bidirectional;
		if (n <= avgCommunitySize) {
			this.sizes = new int[] { n };
			this.avgInLinks = new double[] { avgInLinks };
			this.avgOutLinks = new double[] { avgOutLinks };
		} else {
			ArrayList<Integer> sizes = new ArrayList<Integer>();
			int total = n;
			while (total > 0) {
				if (total <= avgCommunitySize) {
					sizes.add(total);
					total = 0;
				} else {
					int current = (int) (avgCommunitySize + ((double) avgCommunitySize * rand
							.nextGaussian()));
					if (current > total) {
						sizes.add(total);
						total = 0;
					} else if (current > 0) {
						sizes.add(current);
						total -= current;
					}
				}
			}
			this.sizes = Util.toIntegerArray(sizes);
			// this.sizes = new int[(int) Math.ceil((double) n /
			// avgCommunitySize)];
			// for (int i = 0; i < this.sizes.length - 1; i++) {
			// this.sizes[i] = (int) (avgCommunitySize + ((double)
			// avgCommunitySize * rand
			// .nextGaussian()));
			// }
			// this.sizes[this.sizes.length - 1] = n - Util.sum(this.sizes);
			this.avgInLinks = new double[this.sizes.length];
			for (int i = 0; i < this.avgInLinks.length; i++) {
				this.avgInLinks[i] = avgInLinks + avgInLinks
						* rand.nextGaussian();
			}
			this.avgOutLinks = new double[this.sizes.length];
			for (int i = 0; i < this.avgOutLinks.length; i++) {
				this.avgOutLinks[i] = avgOutLinks + avgOutLinks
						* rand.nextGaussian();
			}
		}
	}

	public Communities(int[] sizes, double[] avgInLinks, double[] avgOutLinks,
			boolean bidirectional, RoutingAlgorithm r, Transformation[] t) {
		super("COMMUNITIES_NETWORK", Util.sum(sizes), new Parameter[] {
				new IntArrayParameter("AVG_COMMUNITY_SIZE", sizes),
				new DoubleArrayParameter("AVG_IN_LINKS", avgInLinks),
				new DoubleArrayParameter("AVG_OUT_LINKS", avgOutLinks),
				new BooleanParameter("BIDIRECTIONAL", bidirectional) }, t);
		this.sizes = sizes;
		this.avgInLinks = avgInLinks;
		this.avgOutLinks = avgOutLinks;
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
		// create external links
		for (int i = 0; i < communities.length; i++) {
			double prob = this.avgOutLinks[i]
					/ (double) (this.getNodes() - communities[i].length);
			int start = communities[i][0].getIndex();
			int end = communities[i][communities[i].length - 1].getIndex();
			for (int j = 0; j < communities[i].length; j++) {
				for (int k = 0; k < nodes.length; k++) {
					if ((k < start || k > end) && rand.nextDouble() <= prob) {
						edges.add(communities[i][j].getIndex(), k);
						if (this.bidirectional) {
							edges.add(k, communities[i][j].getIndex());
						}
					}
				}
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}

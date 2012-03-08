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
 * Communities3.java
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
import gtna.util.parameter.DoubleArray2dParameter;
import gtna.util.parameter.IntArrayParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Random;

/**
 * Network generator for generating fully connected sub-graphs (communities)
 * with a specified number of edges between them. The array sizes gives the size
 * of each of the sizes.length communities. The double[][] p gives the
 * probability of an edge existing between two nodes in each community where
 * p[i][j] is the probability of an edge from community i to community j
 * (entries in p[i][i] are ignored). If the flag bidirectional is true, for
 * every edge the backwards edge is created as well. Please note that it might
 * lead to duplicate entries if p[i][j] != 0 && p[j][i] != 0 && bidirectional ==
 * true. Therefore, only fill one part of the matrix to avoid such problems.
 * 
 * The flag LESS_MEMORY can be set to choose a generation procedure that uses
 * less memory but requires more computation time.
 * 
 * The parameter "order" can be used to change the position of the nodes in the
 * main node array of the generated graph object. If it is set to SORTED_ORDER,
 * the main array contains first all nodes from the first community, then the
 * nodes from the second community, and so on. In case the flag is true, the
 * first node is assigned to the first community, the second node to the second
 * and so on. If the communities do not have the same size, the smaller
 * communities are simply discarded after they received all their nodes. E.g.,
 * sizes = {2, 3} would lead to the communities with node indices {0, 2} {1, 3,
 * 4}. sizes = {3, 1, 3} would result in the node assignments {0, 3, 5} {1} {2,
 * 4, 6}. If order is set to RANDOM_ORDER, the nodes are assigned to the
 * communities at random.
 * 
 * @author benni
 * 
 */
public class Communities3 extends Network {
	private int[] sizes;

	private double[][] p;

	private int order;

	private boolean bidirectional;

	private Node[][] communities;

	public static final int SORTED_ORDER = 1;

	public static final int ALTERNATING_ORDER = 2;

	public static final int RANDOM_ORDER = 3;

	public Communities3(int[] sizes, double[][] p, int order,
			boolean bidirectional, Transformation[] t) {
		super("COMMUNITIES_NETWORK_3", Util.sum(sizes), new Parameter[] {
				new IntArrayParameter("COMMUNITY_SIZES", sizes),
				new DoubleArray2dParameter("INTER_COMMUNITY_LINKS", p),
				new IntParameter("ORDER", order),
				new BooleanParameter("BIDIRECTIONAL", bidirectional) }, t);
		this.sizes = sizes;
		this.p = p;
		this.order = order;
		this.bidirectional = bidirectional;
	}

	public Node[][] getCommunities() {
		return this.communities;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Random rand = new Random(System.currentTimeMillis());
		// init communities
		this.communities = new Node[this.sizes.length][];
		for (int i = 0; i < this.communities.length; i++) {
			this.communities[i] = new Node[this.sizes[i]];
		}
		// fill communities depending on order
		if (this.order == SORTED_ORDER) {
			int index = 0;
			for (int i = 0; i < this.communities.length; i++) {
				for (int j = 0; j < this.communities[i].length; j++) {
					this.communities[i][j] = nodes[index++];
				}
			}
		} else if (this.order == ALTERNATING_ORDER) {
			int[] indices = new int[this.communities.length];
			for (int i = 0; i < nodes.length;) {
				for (int j = 0; j < this.communities.length; j++) {
					if (indices[j] < this.communities[j].length) {
						this.communities[j][indices[j]++] = nodes[i++];
					}
					if (i >= nodes.length) {
						break;
					}
				}
			}
		} else if (this.order == RANDOM_ORDER) {
			ArrayList<Node> list = new ArrayList<Node>(nodes.length);
			for (int i = 0; i < nodes.length; i++) {
				list.add(nodes[i]);
			}
			for (int i = 0; i < this.communities.length; i++) {
				for (int j = 0; j < this.communities[i].length; j++) {
					int index = rand.nextInt(list.size());
					this.communities[i][j] = list.get(index);
					list.remove(index);
				}
			}
		}
		// create inter-community links
		Edges edges = new Edges(nodes, 0);
		for (int i = 0; i < this.p.length; i++) {
			for (int j = 0; j < this.p[i].length; j++) {
				if (i == j) {
					continue;
				}
				Node[] from = this.communities[i];
				Node[] to = this.communities[j];
				this.addInterCommunityEdges(from, to, this.p[i][j], rand, edges);
			}
		}
		edges.fill();
		// create internal links
		for (int i = 0; i < this.communities.length; i++) {
			for (int j = 0; j < this.communities[i].length; j++) {
				Node n = this.communities[i][j];
				int[] inOld = n.getIncomingEdges();
				int[] outOld = n.getOutgoingEdges();
				int[] inNew = new int[inOld.length + this.communities[i].length
						- 1];
				int[] outNew = new int[outOld.length
						+ this.communities[i].length - 1];
				int inIndex = 0;
				int outIndex = 0;
				for (int k = 0; k < inOld.length; k++) {
					inNew[inIndex++] = inOld[k];
				}
				for (int k = 0; k < outOld.length; k++) {
					outNew[outIndex++] = outOld[k];
				}
				for (int k = 0; k < this.communities[i].length; k++) {
					if (j != k) {
						inNew[inIndex++] = this.communities[i][k].getIndex();
						outNew[outIndex++] = this.communities[i][k].getIndex();
					}
				}
				n.setIncomingEdges(inNew);
				n.setOutgoingEdges(outNew);
			}
		}
		graph.setNodes(nodes);
		return graph;
	}

	private void addInterCommunityEdges(Node[] from, Node[] to, double p,
			Random rand, Edges edges) {
		for (int i = 0; i < from.length; i++) {
			for (int j = 0; j < to.length; j++) {
				if (rand.nextDouble() <= p) {
					edges.add(from[i].getIndex(), to[j].getIndex());
					if (this.bidirectional) {
						edges.add(to[j].getIndex(), from[i].getIndex());
					}
				}
			}
		}
	}
}

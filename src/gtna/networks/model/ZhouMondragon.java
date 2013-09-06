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
 * ZhouMOndragon.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Tim Grube;
 * Contributors:    -;
 * 
 * Changes since 2013-09-02
 * ---------------------------------------
 */
package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.DeterministicRandom;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.Random;

/**
 * Implements the so-called Rich Club network described by Zhou and Mondragon in
 * their publication "The Rich-Club Phenomenon In The Internet" (2003)
 * 
 * Parameters are the initial network size and the probabilities for adding a
 * new node, a new edge or both
 * 
 * @author tim
 * 
 */
public class ZhouMondragon extends Network {
    private int INIT_NETWORK_SIZE = 10;

    private double p;
    private int epn;

    private Random rng;

    public static ZhouMondragon[] get(int nodes, double[] newEdgeProbability,
	    int edgesPerNode, Transformation[] t) {
	int non = newEdgeProbability.length;
	ZhouMondragon[] nw = new ZhouMondragon[non];
	for (int i = 0; i < non; i++) {
	    nw[i] = new ZhouMondragon(nodes, newEdgeProbability[i],
		    edgesPerNode, t);
	}
	return nw;
    }

    public static ZhouMondragon[] get(int[] nodes, double newEdgeProbability,
	    int edgesPerNode, Transformation[] t) {
	ZhouMondragon[] nw = new ZhouMondragon[nodes.length];
	for (int i = 0; i < nodes.length; i++) {
	    nw[i] = new ZhouMondragon(nodes[i], newEdgeProbability,
		    edgesPerNode, t);
	}
	return nw;
    }

    /**
     * @param nodes
     * @param newEdgeProbability
     * @param edgesPerNode
     */
    public ZhouMondragon(int nodes, double newEdgeProbability,
	    int edgesPerNode, Transformation[] t) {
	super("ZHOU_MONDRAGON", nodes, new Parameter[] {
		new DoubleParameter("EDGE_PROBABILITY", newEdgeProbability),
		new IntParameter("EDGES_PER_NODE", edgesPerNode) }, t);
	this.p = newEdgeProbability;
	this.epn = edgesPerNode;

    }

    public Graph generate() {
	Graph graph = new Graph(this.getDescription());
	rng = new DeterministicRandom(System.currentTimeMillis());
	Node[] nodes = Node.init(this.getNodes(), graph);

	Graph temp = new BarabasiAlbert(this.getNodes(), epn, null).generate();
	Edges edges = new Edges(nodes, nodes.length);

	int[] in = new int[this.getNodes()];
	int[] out = new int[this.getNodes()];

	Arrays.fill(in, 0);
	Arrays.fill(out, 0);

	for (int i = 0; i < temp.getNodes().length; i++) {
	    in[i] = temp.getNodes()[i].getInDegree();
	    out[i] = temp.getNodes()[i].getOutDegree();
	    int[] Out = temp.getNodes()[i].getOutgoingEdges();
	    for (int j = 0; j < Out.length; j++) {
		edges.add(i, Out[j]);
	    }
	}

	boolean addedNode = false;
	double c;

	for (int i = 0; i < nodes.length; i++) {

	    c = rng.nextDouble();
	    if (c < (p)) {
		int s = rng.nextInt(Math.max(i, 1));
		edges = addNewEdge(s, in, out, c, edges);
	    }

	}

	edges.fill();

	graph.setNodes(nodes);
	return graph;
    }

   
    /**
     * 
     * @param s
     *            index of the <b>source</b> of the new edge
     * @param out
     * @param in
     * @param dn
     *            networkdegree
     * @param edges
     *            current edges of the graph
     * @return
     */
    private Edges addNewEdge(int s, int[] in, int[] out, double c, Edges edges) {
	int dn = edges.size(); // network degree

	int i = 0;
	while (i < in.length) {

	    int d = rng.nextInt(this.getNodes());
	    int dd = in[d] + out[d]; // (potential) destination degree

	    if (s != d && !edges.contains(s, d)) {
		double np = (double) dd / (double) dn;
		if (c < np) {
		    edges.add(s, d);
		    edges.add(d, s);
//		    System.out.println("Added edge: " + s + " -> " + d);
		    in[s]++;
		    out[s]++;
		    in[d]++;
		    out[d]++;
		    return edges;
		} else {
		    i++;
		}
	    }

	}

	return edges;
    }
}

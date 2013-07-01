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
 * Regular.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Tim Grube
 * Contributors:    -;
 * 
 * Changes since 2013-06-25
 * ---------------------------------------
 */
package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Implements a generator for a regular network. 
 * A regular network is a network with a constant node degree.
 * 
 * http://en.wikipedia.org/wiki/Regular_graph
 * 
 * The parameters are 
 * 	- the number of nodes, 
 * 	- the node degree as well as
 * 	- a flag for ring or random topology 
 * 	- a flag for the bidirectionality of edges
 * 
 * Note that in this implementation, loops are not permitted, i.e., there is no
 * edge of the form (a,a).
 * 
 * @author Tim  
 */
public class Regular extends Network {
	private int DEGREE;
	private boolean RING;
	private boolean BIDIRECTIONAL;

	/**
	 * Generates n.length regular networks
	 * @param n sizes of the regular networks
	 * @param d degree of the nodes 
	 * @param r true: ring topology, false: random topology
	 * @param b bidirectional
	 * @param t	transformations
	 * @return	array of n.length regular networks
	 */
	public static Regular[] get(int[] n, int d, boolean r, boolean b,
			Transformation[] t) {
		Regular[] nw = new Regular[n.length];
		for (int i = 0; i < n.length; i++) {
			nw[i] = new Regular(n[i], d, r, b, t);
		}
		return nw;
	}

	/**
	 * Generates d.length regular networks
	 * @param n size of the regular networks
	 * @param d degrees of the nodes 
	 * @param r true: ring topology, false: random topology
	 * @param b bidirectional
	 * @param t	transformations
	 * @return	array of d.length regular networks
	 */
	public static Regular[] get(int n, int[] d, boolean r, boolean b,
			Transformation[] t) {
		Regular[] nw = new Regular[d.length];
		for (int i = 0; i < d.length; i++) {
			nw[i] = new Regular(n, d[i], r, b, t);
		}
		return nw;
	}

	/**
	 * Generates n.length x d.length regular networks
	 * @param n sizes of the regular networks
	 * @param d degrees of the nodes 
	 * @param r true: ring topology, false: random topology
	 * @param b bidirectional
	 * @param t	transformations
	 * @return	array of d.length regular networks
	 */
	public static Regular[][] get(int[] n, int[] d, boolean r, boolean b,
			Transformation[] t) {
		Regular[][] nw = new Regular[d.length][n.length];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < n.length; j++) {
				nw[i][j] = new Regular(n[j], d[i], r, b, t);
			}
		}
		return nw;
	}

	public Regular(int nodes, int DEGREE, boolean RING, boolean BIDIRECTIONAL,
			Transformation[] t) {
		super("REGULAR", nodes, new Parameter[] {
				new IntParameter("DEGREE", DEGREE),
				new BooleanParameter("RING_TOPOLOGY", RING),
				new BooleanParameter("BIDIRECTIONAL", BIDIRECTIONAL) }, t);
		this.DEGREE = DEGREE;
		this.RING = RING;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		int toAdd = BIDIRECTIONAL ? DEGREE*nodes.length*2 : DEGREE*nodes.length;
		Edges edges = new Edges(nodes, toAdd);
		
		if(RING) addRingEdges(nodes, edges, toAdd); 
		else addRandomEdges(nodes, edges, toAdd);
		
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
	
	
	private boolean addRandomEdges(Node[] nodes, Edges edges, int toAdd) {
		ArrayList<Node> listDst = new ArrayList<Node>();
		ArrayList<Node> listSrc = new ArrayList<Node>();
		
		
		/*
		 *  initialize source and destination lists
		 *  As we built a regular network, we can add the whole node list 
		 *  again and again
		 *  
		 */
		for(int i = 0; i<DEGREE; i++){
			listDst.addAll(Arrays.asList(nodes));
			listSrc.addAll(Arrays.asList(nodes));
		}
		
		Random rand = new Random();
		int retries = 30;
		while(!listSrc.isEmpty() && !listDst.isEmpty() && retries > 0){
//			int srcId = rand.nextInt(listSrc.size());
//			Node srcN = listSrc.get(srcId);
//			int src = srcN.getIndex();
//			
//			int dstId = rand.nextInt(listDst.size());
//			Node dstN = listDst.get(dstId); 
//			int dst = dstN.getIndex();
			
			int srcId = rand.nextInt();
			srcId = (srcId < 0) ? srcId*(-1) : srcId;
			srcId = srcId % listSrc.size();
			Node srcN = listSrc.get(srcId);
			int src = srcN.getIndex();
			
			int dstId = rand.nextInt();
			dstId = (dstId < 0) ? dstId*(-1) : dstId;
			dstId = dstId % listDst.size();
			Node dstN = listDst.get(dstId); 
			int dst = dstN.getIndex();
			
			if (src == dst) {
				System.err.println("SELFLOOP! src=" + src + " dst=" +dst);
				try {
				    Thread.sleep(500);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				retries--;
				continue;
			}
			
			if (this.BIDIRECTIONAL) {
				edges.add(src, dst);
				edges.add(dst, src);
				listSrc.remove(srcN);listDst.remove(srcN);
				listDst.remove(dstN);listSrc.remove(dstN);
				
			} else {
				edges.add(src, dst);
				listSrc.remove(srcN);
				listDst.remove(dstN);
			}
			retries = 30;
		}
		
		if(edges.size() != toAdd){
			System.err.println("Restarting Generator! " +
					"(" + edges.size() + " - " + toAdd + ")");
			edges = null;
			edges = new Edges(nodes, toAdd);
			return addRandomEdges(nodes, edges, toAdd);
		}
		
		return true;
	}

	private boolean addRingEdges(Node[] nodes, Edges edges, int toAdd){
		for(int i = 0; i < nodes.length; i++){
			for(int j = 1; j <= DEGREE; j++){
				int src = i;	
				int dst = (i+j)%(nodes.length);	// get the next neighbor
				if (src == dst) {
					continue;
				}	
				if (this.BIDIRECTIONAL) {
					edges.add(src, dst);
					edges.add(dst, src);
				} else {
					edges.add(src, dst);
				}
			}
		}
		return true;
	}
}

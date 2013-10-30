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
 * CondonAndKarp.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model;

import java.util.ArrayList;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Tim Grube
 * 
 * 	based on CondonAndKarp.java and
 * 
 *         Brandes, U., M. Gaertler, and D. Wagner, 2003, in Proceed- ings of
 *         ESA (Springer-Verlag, Berlin, Germany), pp. 568{ 579.
 * 
 */
public class GeneralizedCondonAndKarp extends Network {

	private double pout;
	private Network[] communities;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public GeneralizedCondonAndKarp(Network[] communities, double pout,
			Transformation[] transformations) {
		super("GENERALIZED_CONDON_KARP", sumNodes(communities), new Parameter[] {
				new IntParameter("GROUPS", communities.length),
				new DoubleParameter("POUT", pout) }, transformations);

		this.communities = communities;
		this.pout = pout;
	}

	/**
	 * @param communities
	 * @return
	 */
	private static int sumNodes(Network[] communities) {
	   int n = 0;
	   
	   for(Network nw : communities) {
	       n += nw.getNodes();
	   }
	   
	   return n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());

		
		Node[] nodes = Node.init(sumNodes(communities), graph);
		Edges edges = new Edges(nodes, 1);
		int[] communitySizes = new int[communities.length];
		
		int nodeOffset = 0;
		// join communities and add existing edges
		for(int i = 0; i < communities.length; i++) {
		    Network nw = communities[i];
		    
		    Graph g = nw.generate();
		    
		    ArrayList<Edge> e = g.getEdges().getEdges();
		    
		    for(Edge ei : e) {
			edges.add(ei.getSrc()+nodeOffset, ei.getDst()+nodeOffset);
		    }
		    
		    communitySizes[i] = g.getNodeCount();
		    nodeOffset += g.getNodeCount();
		}
		
		
		// add additional edges between the communities
		for(int i = 0; i < nodes.length; i++) {
		    for(int j = 0; j < nodes.length; j++) {
			if(!sameCommunity(i, j, communitySizes)) {
			    if(Math.random() < pout) {
				edges.add(i, j);
			    }
			}
		    }
		}
		
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	/**
	 * @param i	node 1
	 * @param j	node 2
	 * @param communitySizes borders of communities
	 * @return
	 */
	private boolean sameCommunity(int i, int j, int[] communitySizes) {
	    int lb = 0;
	    int hb = 0;
	    for(int c = 0; c < communitySizes.length; c++) {
		if(c>0) {
		    lb = communitySizes[c-1];
		} else if(c==0){
		    lb = 0;
		}
		hb = communitySizes[c];
		
		if(lb < i && i < hb && lb < j && j < hb) {
		    return true;
		}
	    }
	    
	    return false;
	}

	

}

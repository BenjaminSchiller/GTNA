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
 * Clique.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;


/**
 * @author Tim
 *
 */
public class Clique extends Network {

    
    /**
     * Generates n.length clique networks
     * @param n	sizes of the networks
     * @param t	transformations
     * @return	array of n.length clique networks
     */
    public static Clique[] get(int[] n, Transformation[] t) {
	Clique[] nw = new Clique[n.length];
	
	for(int i = 0; i < n.length; i++) {
	    nw[i] = new Clique(n[i], t);
	}
	
	return nw;
    }
    
    
    /**
     * @param nodes	number of nodes
     * @param t		transformations to be applied after generationon
     */
    public Clique(int nodes, Transformation[] t) {
	super("CLIQUE", nodes, null, t);
	
    }
    
    
    
    /* (non-Javadoc)
     * @see gtna.networks.Network#generate()
     */
    @Override
    public Graph generate() {
	Graph g = new Graph(this.getDescription());
	Node[] nodes = Node.init(this.getNodes(), g);
	int toAdd = (nodes.length * (nodes.length -1)) / 2;
	Edges edges = new Edges(nodes, toAdd);
	
	for(Node ni : nodes) {
	    addEdges(ni, edges, nodes.length);
	}
	
	edges.fill();
	g.setNodes(nodes);
	return g;
    }


    /**
     * @param ni
     * @param i
     */
    private void addEdges(Node ni, Edges e, int i) {
	for(int j = 0; j < i; j++) {
	    // for all indices add an incoming/outgoing edge
	    // iff its not a selfloop
	    if(ni.getIndex() != j) {
		e.add(ni.getIndex(), j);
	    }
	}
	
    }

}

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
 * AStartNodeSelector.java
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
package gtna.transformation.sampling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.Parameter;

/**
 * @author Tim
 * 
 */
public class StartNodeSelector extends Parameter {

    /**
     * @param key
     * @param value
     */
    public StartNodeSelector(String startnodeselector) {
	super("STARTNODESELECTOR", startnodeselector);
    }

    /**
     * Select start nodes from the original graph The default implementation
     * choses dimension random nodes
     * 
     * @param g
     *            Graph
     * @param dimension
     *            Number of chosen startnodes
     * @return Array of chosen startnodes
     */
    public Node[] selectStartNodes(Graph g, int dimension, Random r) {
	Collection<Node> sn = new ArrayList<Node>();

	int gsize = g.getNodeCount();
	int nid;
	Node n;

	while (sn.size() < dimension) {
	    nid = r.nextInt(gsize - 1);
	    nid = nid % gsize;
	    n = g.getNode(nid);

	    // add node to selected nodes
	    if (!sn.contains(n)) {
		sn.add(n);
	    }
	}
	return sn.toArray(new Node[0]);
    }

}

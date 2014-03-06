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
 * VisitedNodeSampler.java
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
package gtna.transformation.sampling.sampler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import gtna.graph.Node;
import gtna.transformation.sampling.ASampler;

/**
 * @author Tim
 *
 */
public class VisitedNodeSampler extends ASampler {

    /**
     * @param key
     * @param value
     */
    public VisitedNodeSampler() {
	super("VISITED_NODE_SAMPLER");
    }

    /* (non-Javadoc)
     * @see gtna.transformation.sampling.ASampler#sampleNodes(java.util.Map)
     */
    @Override
    protected List<Node> sampleNodes(
	    Map<Node, List<Node>> filteredCandidates, int round) {
	
    	List<Node> selected = new ArrayList<Node>();
	
	// add keySet as we sample all visited nodes!
	selected.addAll(filteredCandidates.keySet());

	return selected;
    }

}

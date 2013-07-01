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
 * CandidateFilter.java
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

<<<<<<< HEAD
import gtna.graph.Node;
import gtna.transformation.sampling.sample.NetworkSample;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Tim
 * 
 */
public class CandidateFilter {

    private boolean revisiting = false;

    /**
     * instantiate the candidate filter
     * 
     * @param revisiting
     *            true if the filter ignores visited nodes, false if the filter
     *            removes visited nodes
     */
    public CandidateFilter(boolean revisiting) {
	this.revisiting = revisiting;
    }

    /**
     * Filter the candidate collection with respect to @classfield revisiting
     * 
     * calls filterCandidatesSelfAware OR filterCandidatesRevisiting
     * 
     * @param c
     *            candidate set
     * @param ns
     *            current network sample
     * @return filtered set of candidates
     */
    public Collection<Node> filterCandidates(Collection<Node> c,
	    NetworkSample ns) {
	if (revisiting)
	    return filterCandidatesRevisiting(c, ns);
	else
	    return filterCandidatesSelfAware(c, ns);
    }

    /**
     * This default implementation removes all in the current sample contained
     * nodes from the candidate collection
     * 
     * @param c
     *            candidate set
     * @param ns
     *            current network sample
     * @return c without ns.nodes
     */
    public Collection<Node> filterCandidatesSelfAware(Collection<Node> c,
	    NetworkSample ns) {
	Collection<Node> filtered = new ArrayList<Node>();

	for (Node n : c) {
	    if (!ns.contains(n))
		filtered.add(n);
	}

	return filtered;
    }

    /**
     * This default implementation does NOT remove any nodes from the candidate
     * collection. In our standard case of revisiting sampling algorithms, all
     * candidates are real candidates.
     * 
     * @param c
     *            candidate set
     * @param ns
     *            current networksample
     * @return == c
     */
    public Collection<Node> filterCandidatesRevisiting(Collection<Node> c,
	    NetworkSample ns) {
	return c;
    }
=======
/**
 * @author Tim
 *
 */
public class CandidateFilter {

>>>>>>> Class Structure
}

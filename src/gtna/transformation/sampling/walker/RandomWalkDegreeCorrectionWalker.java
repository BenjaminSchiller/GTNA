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
 * RandomWalkWalker.java
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
package gtna.transformation.sampling.walker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;

/**
 * @author Tim
 *
 */
public class RandomWalkDegreeCorrectionWalker extends AWalker {

	/**
	 * @param walker
	 */
	public RandomWalkDegreeCorrectionWalker() {
		super("RANDOM_WALK_wITHdEGREEcORRECTION_WALKER");
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.AWalker#selectNextNode(java.util.Collection)
	 */
	@Override
	protected Node selectNextNode(Collection<Node> candidates) {
<<<<<<< HEAD
<<<<<<< HEAD
		Random r = super.getRNG();
=======
		Random r = new Random();
>>>>>>> RandomWalk Walker with degree correction and entry for the sampling algorithm factory
=======
		Random r = super.getRNG();
>>>>>>> Usage of the deterministic-rng
		ArrayList<Node> can = new ArrayList<Node>();
		can.addAll(candidates);
		
		double pnext = r.nextDouble();
		double pd = 0.0;
		int i = 0;
		Node next;
		do{
			// if the whole candidate set is tried, reset counter and draw a new random
			if(i >= can.size()){
				i = 0;
				pnext = r.nextDouble();
			}
			next = can.get(i);
			pd = 1.0 / next.getOutDegree();
			
			i++;
		}while(pnext > pd);
		
		return next;
	}
	
	
	 /**
     * returns the list of neighbors as candidates
     * 
     * @param g
     *            Graph
     * @param n
     *            Current node
     * @return List of candidates
     */
	@Override
    public Collection<Node> resolveCandidates(Graph g, Node n) {
    	int[] nids = n.getOutgoingEdges();
    	ArrayList<Node> nn = new ArrayList<Node>();
    	for (int i : nids) {
    		nn.add(g.getNode(i));
    	}
    	return nn;
    }

}

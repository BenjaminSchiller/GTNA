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
 * CanonicalCircularCrossing.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.gd;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.plot.GraphPlotter;

/**
 * @author Nico
 *
 */
public class CanonicalCircularCrossing extends CircularAbstract {
	public CanonicalCircularCrossing() {
		super("GDA_CANONICALCIRCULARCROSSING", new String[]{}, new String[]{});
	}
	
	public CanonicalCircularCrossing(int realities, double modulus, boolean wrapAround, GraphPlotter plotter) {
		super("GDA_CANONICALCIRCULARCROSSING", new String[]{}, new String[]{});
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.graphPlotter = plotter;
	}	

	@Override
	public Graph transform(Graph g) {
		int crossingsStart, crossingsEnd;
		Node currentNode;
		int currentCrossings, currentCrossingsPred, swappedCrossings, swappedCrossingsPred, predecessor;
		
		initIDSpace(g);
		graphPlotter.plotStartGraph(g, idSpace);
		
		/*
		 * Add all nodes to the todolist
		 */
		LinkedList<Node> todolist = new LinkedList<Node>();
		todolist.addAll(Arrays.asList(g.getNodes()));
		
		crossingsStart = countAllCrossings(g);
		while ( ( currentNode = todolist.poll() ) != null ) {
			predecessor = getPredecessor( currentNode.getIndex() );
			currentCrossings = countCrossings(g, currentNode);
			currentCrossingsPred = countCrossings(g, g.getNode(predecessor));
			swapPositions(currentNode.getIndex(), predecessor);
			swappedCrossings = countCrossings(g, currentNode);
			swappedCrossingsPred = countCrossings(g, g.getNode(predecessor));
			if ( swappedCrossings < currentCrossings && swappedCrossingsPred < currentCrossingsPred) {
				/*
				 * Leave it that way, boy! But: there might be
				 * some more success with that node...
				 */
				todolist.add(currentNode);
				todolist.add(g.getNode(predecessor));
			} else {
				swapPositions(currentNode.getIndex(), predecessor);
			}
		}

		crossingsEnd = countAllCrossings(g);
		System.out.println("Crossings at the beginning: " + crossingsStart + " - and afterwards: " + crossingsEnd);
		
		graphPlotter.plotFinalGraph(g, idSpace);		
		writeIDSpace(g);
		return g;
	}
}

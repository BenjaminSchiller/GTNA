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
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.plot.GraphPlotter;

/**
 * @author Nico
 * 
 */
public class CanonicalCircularCrossing extends CircularAbstract {
	public CanonicalCircularCrossing() {
		super("GDA_CANONICALCIRCULARCROSSING", new String[] {}, new String[] {});
	}

	public CanonicalCircularCrossing(int realities, double modulus, boolean wrapAround, GraphPlotter plotter) {
		super("GDA_CANONICALCIRCULARCROSSING", new String[] {}, new String[] {});
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.graphPlotter = plotter;
	}

	@Override
	public Graph transform(Graph g) {
		int crossingsStart, crossingsEnd;
		Node currentNode, predecessor;
		int currentCrossings, swappedCrossings;

		initIDSpace(g);
		graphPlotter.plotStartGraph(g, idSpace);

		/*
		 * Add all nodes to the todolist
		 */
		LinkedList<Node> todolist = new LinkedList<Node>();
		todolist.addAll(Arrays.asList(g.getNodes()));

		crossingsStart = countAllCrossings(g);
		int countLoop = 0;
		long startTime = System.currentTimeMillis();
		while ((currentNode = todolist.poll()) != null) {
			predecessor = g.getNode(getPredecessor(currentNode.getIndex()));
			currentCrossings = countCrossings(currentNode, predecessor);
			if (currentCrossings == 0) {
					/*
					 * If there are no actual crossings caused by a node and its predecessor,
					 * there is nothing to improve
					 */
				continue;
			}
			swapPositions(currentNode.getIndex(), predecessor.getIndex());
			swappedCrossings = countCrossings(currentNode, predecessor);
			if (swappedCrossings < currentCrossings) {
				/*
				 * Leave it that way, boy! But: there might be some more success
				 * with that node...
				 * 
				 * Remark: a prior implementation also checked whether the
				 * swapping increased the number of crossings for the
				 * predecessor. This took a lot more time, but did not lead to
				 * better result. A full check for the predecessor absorbs this
				 * - because the predecessor might also have a predecessor
				 * sharing a lot of edge crossings...
				 */
				todolist.add(currentNode);
				todolist.add(predecessor);
			} else {
				swapPositions(currentNode.getIndex(), predecessor.getIndex());
			}
			countLoop++;
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		System.out.println("Did " + countLoop + " loops in " + totalTime + " msec");
		crossingsEnd = countAllCrossings(g);
		System.out.println("Crossings at the beginning: " + crossingsStart + " - and afterwards: " + crossingsEnd);

		graphPlotter.plotFinalGraph(g, idSpace);
		writeIDSpace(g);
		return g;
	}
}

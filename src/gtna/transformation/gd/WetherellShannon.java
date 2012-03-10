/* ===========================================================
w * GTNA : Graph-Theoretic Network Analyzer
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
 * WetherellShannon.java
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

import gtna.drawing.GraphPlotter;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.spanningTree.SpanningTree;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Nico
 * 
 */
public class WetherellShannon extends HierarchicalAbstract {
	private double[] heightModifiers, nodeModifiers;
	private double[] nextPos;

	private double modifierSum;

	public WetherellShannon(double modulusX, double modulusY,
			GraphPlotter plotter) {
		super("GDA_WETHERELL_SHANNON", new Parameter[] {
				new DoubleParameter("MODULUS_X", modulusX),
				new DoubleParameter("MODULUS_Y", modulusY) });
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.graphPlotter = plotter;
	}

	public GraphDrawingAbstract clone() {
		return new WetherellShannon(modulusX, modulusY, graphPlotter);
	}

	@Override
	public Graph transform(Graph g) {
		tree = (SpanningTree) g.getProperty("SPANNINGTREE");
		if (tree == null) {
			throw new GDTransformationException("SpanningTree property missing");
		}

		int source = tree.getSrc();
		int maxHeight = g.getNodes().length;

		heightModifiers = new double[maxHeight];
		nodeModifiers = new double[maxHeight];
		nextPos = new double[maxHeight];
		nodePositionsX = new double[maxHeight];
		nodePositionsY = new double[maxHeight];
		for (int i = 0; i < maxHeight; i++) {
			heightModifiers[i] = 0;
			nextPos[i] = 1;
			nodePositionsX[i] = 0;
			nodePositionsY[i] = 0;
		}
		firstWalk(tree, source, 0);

		modifierSum = 0;
		secondWalk(tree, source, 0);

		for (Node i : g.getNodes()) {
			if (i == null) {
				System.out.println("Missing node");
				continue;
			}
			// System.out.print("Node " + i.getIndex() + " resides at " +
			// nodePositionsX[i.getIndex()] + "|" + nodePositionsY[i.getIndex()]
			// + " and has edges to ");
			// for ( int j: tree.getChildren(i.getIndex()) ) System.out.print(j
			// + " ");
			// System.out.println();
		}

		writeIDSpace(g);
		if (graphPlotter != null)
			graphPlotter.plotFinalGraph(g, idSpace);
		if (graphPlotter != null)
			graphPlotter.plotSpanningTree(g, idSpace);

		return g;
	}

	private void firstWalk(SpanningTree tree, int n, int height) {
		int[] sons = tree.getChildren(n);
		for (int singleSon : sons) {
			firstWalk(tree, singleSon, height + 1);
		}

		/*
		 * So, for now, we have traveled through all children and should either
		 * have gotten to a leaf or we're on the way back to the top of the tree
		 */
		double place = 0;
		if (sons.length == 0) {
			/*
			 * Current node has no childs, so use the next free position
			 */
			place = nextPos[height];
		} else {
			/*
			 * Put the node centered over its children
			 */
			for (int singleSon : sons) {
				place += nodePositionsX[singleSon];
			}
			place = place / sons.length;
		}

		heightModifiers[height] = Math.max(heightModifiers[height],
				nextPos[height] - place);
		if (sons.length == 0) {
			nodePositionsX[n] = place;
		} else {
			nodePositionsX[n] = place + heightModifiers[height];
		}
		/*
		 * This might be a problematic point, as +2 results from binary trees
		 */
		nextPos[height] = nodePositionsX[n] + 2;
		nodeModifiers[n] = heightModifiers[height];
	}

	private void secondWalk(SpanningTree tree, int n, int height) {
		nodePositionsX[n] = nodePositionsX[n] + modifierSum;
		modifierSum = modifierSum + nodeModifiers[n];
		nodePositionsY[n] = 2 * height + 1;

		int[] sons = tree.getChildren(n);
		for (int singleSon : sons) {
			secondWalk(tree, singleSon, height + 1);
		}

		modifierSum = modifierSum - nodeModifiers[n];
	}
}

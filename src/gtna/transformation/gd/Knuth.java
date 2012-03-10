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
 * Knuth.java
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
public class Knuth extends HierarchicalAbstract {
	int nextFreePosition;

	public Knuth(double modulusX, double modulusY, GraphPlotter plotter) {
		super("GDA_KNUTH", new Parameter[] {
				new DoubleParameter("MODULUS_X", modulusX),
				new DoubleParameter("MODULUS_Y", modulusY) });
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.graphPlotter = plotter;
	}

	public GraphDrawingAbstract clone() {
		return new Knuth(modulusX, modulusY, graphPlotter);
	}

	@Override
	public Graph transform(Graph g) {
		tree = (SpanningTree) g.getProperty("SPANNINGTREE");
		if (tree == null) {
			throw new GDTransformationException("SpanningTree property missing");
		}
		int source = tree.getSrc();
		int maxHeight = g.getNodes().length;
		nextFreePosition = 0;

		nodePositionsX = new double[maxHeight];
		nodePositionsY = new double[maxHeight];
		for (int i = 0; i < maxHeight; i++) {
			nodePositionsX[i] = 0;
			nodePositionsY[i] = 0;
		}

		walkTree(tree, source);

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

	private void walkTree(SpanningTree tree, int node) {
		int depth = tree.getDepth(node);
		nodePositionsY[node] = depth;

		int[] children = tree.getChildren(node);
		if (children.length == 0) {
			/*
			 * Direct positioning at next place
			 */
			nodePositionsX[node] = nextFreePosition++;
		} else {
			int positionOfParent = (int) Math.floor(children.length / 2);
			for (int singleChild : children) {
				if (positionOfParent-- == 0) {
					nodePositionsX[node] = nextFreePosition++;
				}
				walkTree(tree, singleChild);
			}
		}
	}
}

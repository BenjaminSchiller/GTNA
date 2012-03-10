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
 * MelanconHerman.java
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
import gtna.graph.spanningTree.SpanningTree;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Nico
 * 
 */
public class MelanconHerman extends HierarchicalAbstract {
	private NodeParameter[] np;

	/*
	 * leafRadius will hold the radius of a single leaf node
	 */
	private final double leafRadius = 2d;

	public MelanconHerman(double modulusX, double modulusY, GraphPlotter plotter) {
		super("GDA_MELANCONHERMAN", new Parameter[] {
				new DoubleParameter("MODULUS_X", modulusX),
				new DoubleParameter("MODULUS_Y", modulusY) });
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.graphPlotter = plotter;
	}

	public GraphDrawingAbstract clone() {
		return new MelanconHerman(modulusX, modulusY, graphPlotter);
	}

	@Override
	public Graph transform(Graph g) {
		tree = (SpanningTree) g.getProperty("SPANNINGTREE");
		if (tree == null) {
			throw new GDTransformationException("SpanningTree property missing");
		}

		int source = tree.getSrc();
		np = new NodeParameter[g.getNodes().length];
		nodePositionsX = new double[g.getNodes().length];
		nodePositionsY = new double[g.getNodes().length];

		firstWalk(source);

		secondWalk(source, 0, 0, 1, 0);

		writeIDSpace(g);
		if (graphPlotter != null)
			graphPlotter.plotFinalGraph(g, idSpace);
		if (graphPlotter != null)
			graphPlotter.plotSpanningTree(g, idSpace);

		return g;
	}

	private void firstWalk(int node) {
		np[node] = new NodeParameter();
		np[node].d = 0;
		double s = 0;
		int[] sons = tree.getChildren(node);
		for (int k : sons) {
			firstWalk(k);
			np[node].d = Math.max(np[node].d, np[k].r);
		}
		for (int k : sons) {
			np[k].alpha = Math.atan(np[k].r / (np[node].d + np[k].r));
			s += np[k].alpha;
		}
		adjustChildren(node, s);
		setRadius(node);
	}

	private void adjustChildren(int node, double s) {
		if (s > Math.PI) {
			np[node].c = Math.PI / s;
			np[node].f = 0;
		} else {
			np[node].c = 1;
			np[node].f = Math.PI - s;
		}
	}

	private void setRadius(int node) {
		np[node].r = Math.max(np[node].d, leafRadius) + 2 * np[node].d;
	}

	private void secondWalk(int node, double x, double y, double lambda,
			double theta) {
		nodePositionsX[node] = x;
		nodePositionsY[node] = y;

		int[] children = tree.getChildren(node);

		double dd = lambda * np[node].d;
		double phi = theta + Math.PI;
		double freeSpace = np[node].f / children.length;
		double previous = 0;
		double currAlpha, currRadius;

		for (int singleSon : children) {
			currAlpha = np[node].c * np[singleSon].alpha;
			currRadius = np[node].d
					* (Math.tan(currAlpha) / (1 - Math.tan(currAlpha)));
			phi = phi + previous + np[singleSon].alpha + freeSpace;
			double kX = (lambda * currRadius + dd) * Math.cos(phi);
			double kY = (lambda * currRadius + dd) * Math.sin(phi);
			previous = np[singleSon].alpha;
			secondWalk(singleSon, kX + x, kY + y, lambda
					* (currRadius / np[singleSon].r), phi);
		}
	}

	private class NodeParameter {
		public double d, f, c, r, alpha;
	}
}
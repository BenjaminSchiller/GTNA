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

import gtna.graph.Graph;
import gtna.graph.spanningTree.SpanningTree;
import gtna.plot.GraphPlotter;
import gtna.transformation.Transformation;

/**
 * @author Nico
 * 
 */
public class MelanconHerman extends HierarchicalAbstract implements Transformation {
	private NodeParameter[] np;

	/*
	 * leafRadius will hold the radius of a single leaf node
	 */
	double leafRadius = 2d;
	double d = 20d;
	double previous = 0;

	public MelanconHerman() {
		this("GDA_MELANCONHERMAN", new String[] {}, new String[] {});
	}

	public MelanconHerman(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}

	public MelanconHerman(double modulusX, double modulusY, GraphPlotter plotter) {
		this("GDA_MELANCONHERMAN", new String[] {}, new String[] {});
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.graphPlotter = plotter;
	}

	@Override
	public Graph transform(Graph g) {
		tree = (SpanningTree) g.getProperty("SPANNINGTREE");
		int source = tree.getSrc();
		np = new NodeParameter[g.getNodes().length];
		nodePositionsX = new double[g.getNodes().length];
		nodePositionsY = new double[g.getNodes().length];

		firstWalk(source);

		secondWalk(source, 0, 0, 1, 0);

		writeIDSpace(g);
		graphPlotter.plotFinalGraph(g, idSpace);
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
			np[k].alpha = Math.atan( np[k].r / ( np[node].d + np[k].r) );
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

	private void secondWalk(int node, double x, double y, double lambda, double theta) {
		nodePositionsX[node] = x;
		nodePositionsY[node] = y;
		
		double dd = lambda * np[node].d;
		double gamma = theta + Math.PI;
		double freeSpace = np[node].f / (tree.getChildren(node).length + 1 );
		double previous = 0;
		double currAlpha, currRadius;

		for (int singleSon : tree.getChildren(node)) {
			currAlpha = np[node].c * np[singleSon].alpha;
			currRadius = np[node].d * (Math.tan(currAlpha) / (1 - Math.tan(currAlpha)));
			gamma = ( gamma + previous + np[singleSon].alpha + freeSpace );// % ( 2 * Math.PI );
			double kX = (lambda * currRadius + dd) * Math.cos(gamma);
			double kY = (lambda * currRadius + dd) * Math.sin(gamma);
			previous = np[singleSon].alpha;
			secondWalk(singleSon, kX + x, kY
					+ y, lambda * (currRadius / np[singleSon].r), gamma);
		}
	}

	private class NodeParameter {
		public double d, f, c, r, alpha;
	}
}

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
 * BubbleTree.java
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

import java.util.ArrayList;

import gtna.graph.Graph;
import gtna.graph.spanningTree.SpanningTree;
import gtna.plot.GraphPlotter;
import gtna.transformation.Transformation;

/**
 * @author Nico
 * 
 */
public class BubbleTree extends HierarchicalAbstract {
	/*
	 * leafRadius will hold the radius of a single leaf node
	 */
	double leafRadius = 2d;

	/*
	 * Radiuses will hold the radius of each circle around a node
	 */
	double[] radiuses;

	double[] delta;
	double[] angularSector;

	public BubbleTree(double modulusX, double modulusY, GraphPlotter plotter) {
		this("GDA_BUBBLETREE", modulusX, modulusY, plotter);
	}

	public BubbleTree(String key, double modulusX, double modulusY, GraphPlotter plotter) {
		super(key, new String[] {}, new String[] {});
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.graphPlotter = plotter;
	}

	@Override
	public Graph transform(Graph g) {
		tree = (SpanningTree) g.getProperty("SPANNINGTREE");
		int source = tree.getSrc();
		radiuses = new double[g.getNodes().length];
		delta = new double[g.getNodes().length];
		angularSector = new double[g.getNodes().length];
		calculateRadius(tree, source);
		Point center = new Point(0,0);
		coordAssign(tree, source, center);
		return g;
	}

	private void calculateRadius(SpanningTree tree, int node) {
		int[] sons = tree.getChildren(node);
		for (int singleSon : sons) {
			calculateRadius(tree, singleSon);
		}

		if (sons.length == 0) {
			/*
			 * Current node is a leaf, so the enclosing circle is of fixed size
			 */
			radiuses[node] = leafRadius;
			return;
		}

		/*
		 * From here on, the currently processed node definitely has children
		 */
		calculateAngularSector(tree, node);
		double deltaSum = 0;
		for (int singleSon : sons) {
			delta[singleSon] = Math.max(leafRadius + radiuses[singleSon],
					radiuses[singleSon] / Math.sin(angularSector[singleSon] / 2));
			deltaSum += angularSector[singleSon];
			nodePositionsX[singleSon] = delta[singleSon] * Math.cos(deltaSum - (angularSector[singleSon] / 2));
			nodePositionsY[singleSon] = delta[singleSon] * Math.sin(deltaSum - (angularSector[singleSon] / 2));
		}

		radiuses[node] = calculateSmallestEnclosingCircle(tree, node);
	}

	private void calculateAngularSector(SpanningTree tree, int node) {
		/*
		 * angularSector will hold the size of each sector of the child nodes.
		 * It is chosen one element larger as we need sons.length links to the
		 * sons and one to the parent node. sumRadius will sum up the radiuses
		 * of the children. Initially, it is filled with the radius of a single
		 * leaf node to ensure that there is space for a connection to the
		 * parent node
		 */
		int[] sons = tree.getChildren(node);
		double sumRadius = leafRadius;
		for (int singleSon : sons) {
			sumRadius += radiuses[singleSon];
		}
		/*
		 * Now we will set the angular sector that each child may use. First:
		 * check whether there is a child that needs more than sumRadius/2 --
		 * this will be handled different!
		 */
		int largestSon = -1;
		for (int singleSon : sons) {
			if (radiuses[singleSon] >= 0.5 * sumRadius) {
				largestSon = singleSon;
				angularSector[singleSon] = Math.PI;
				sumRadius -= radiuses[singleSon];
			}
		}

		for (int singleSon : sons) {
			if (largestSon == singleSon) {
				/*
				 * We already handled this special case
				 */
				continue;
			}
			angularSector[singleSon] = (radiuses[singleSon] / sumRadius) * 2 * Math.PI;
		}
	}

	private double calculateSmallestEnclosingCircle(SpanningTree tree, int node) {
		ArrayList<Point> pointSet = new ArrayList<Point>();
		for (int singleChild : tree.getChildren(node)) {
			pointSet.add(new Point(nodePositionsX[singleChild], nodePositionsY[singleChild]));
		}
		return smallestEnclosingCircle(pointSet, new ArrayList<Point>());
	}

	private double smallestEnclosingCircle(ArrayList<Point> p, ArrayList<Point> r) {
		if (p.isEmpty() || r.size() == 3) {
			return calculateRadius(r);
		} else {
			Point randP = p.remove(rand.nextInt(p.size()));
			double d = smallestEnclosingCircle(p,r);
			if ( d > randP.distanceToZero() ) {
				r.add(randP);
				return smallestEnclosingCircle(p, r);
			} else {
				return d;
			}
		}
	}

	private double calculateRadius(ArrayList<Point> r) {
		if (r.size() <= 1) {
			return leafRadius;
		} else if (r.size() == 2) {
			return r.get(0).distanceTo(r.get(1));
		} else if (r.size() == 3) {
			/*
			 * Borrowed from
			 * http://delphiforfun.org/Programs/math_topics/circle_from_3_points
			 * .htm
			 */
			Point x = r.get(0);
			Point y = r.get(1);
			Point z = r.get(2);
			double a = x.distanceTo(y);
			double b = x.distanceTo(z);
			double c = y.distanceTo(z);
			double k = 0.5 * Math.abs((x.x - z.x) * (y.y - x.y) - (x.x - y.x) * (z.y - x.y));
			return (a * b * c) / (4 * k);
		} else {
			throw new RuntimeException("Do not call cR with more than three elements!");
		}
	}

	private void coordAssign(SpanningTree tree, int source, Point center) {
		throw new RuntimeException("Coordinate assignment is not ready yet");
	}

	private class Point {
		public double x, y;

		public Point(double nodePositionsX, double nodePositionsY) {
			this.x = nodePositionsX;
			this.y = nodePositionsY;
		}

		public double distanceToZero() {
			return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
		}
		
		public double distanceTo(Point p2) {
			return Math.sqrt(Math.pow(this.x + p2.x, 2) + Math.pow(this.y + p2.y, 2));
		}
	}
}

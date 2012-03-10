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

import java.util.Collections;
import java.util.LinkedList;

import gtna.drawing.GraphPlotter;
import gtna.graph.Graph;
import gtna.graph.spanningTree.SpanningTree;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

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

	double[] angularSector;
	double[] freeAngle;

	public BubbleTree(double modulusX, double modulusY, GraphPlotter plotter) {
		super("GDA_BUBBLETREE", new Parameter[] {
				new DoubleParameter("MODULUS_X", modulusX),
				new DoubleParameter("MODULUS_Y", modulusY) });
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.graphPlotter = plotter;
	}

	public GraphDrawingAbstract clone() {
		return new BubbleTree(modulusX, modulusY, graphPlotter);
	}

	@Override
	public Graph transform(Graph g) {
		tree = (SpanningTree) g.getProperty("SPANNINGTREE");
		if (tree == null) {
			throw new GDTransformationException("SpanningTree property missing");
		}
		int source = tree.getSrc();

		int numNodes = g.getNodes().length;
		nodePositionsX = new double[numNodes];
		nodePositionsY = new double[numNodes];

		radiuses = new double[numNodes];
		angularSector = new double[numNodes];
		freeAngle = new double[numNodes];
		calculateRadius(tree, source);
		Point center = new Point(0, 0);

		// System.out.println("Source: " + source);
		// for (int i = 0; i < numNodes; i++) {
		// System.out.println("angSector for " + i + ": " + angularSector[i] +
		// " (" + Math.toDegrees(angularSector[i])
		// + ")");
		// System.out.println("Free angle for " + i + ": " +
		// Math.toDegrees(freeAngle[i]));
		// }

		coordAssign(tree, source, center);

		writeIDSpace(g);
		if (graphPlotter != null)
			graphPlotter.plotFinalGraph(g, idSpace);
		if (graphPlotter != null)
			graphPlotter.plotSpanningTree(g, idSpace);

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
		double thetaSum = 0;
		double delta;
		for (int singleSon : sons) {
			delta = Math.max(
					leafRadius + radiuses[singleSon],
					radiuses[singleSon]
							/ Math.sin(angularSector[singleSon] / 2));
			thetaSum += angularSector[singleSon];
			nodePositionsX[singleSon] = delta
					* Math.cos(thetaSum - (angularSector[singleSon] / 2));
			nodePositionsY[singleSon] = delta
					* Math.sin(thetaSum - (angularSector[singleSon] / 2));
			// System.out.println("Put " + singleSon + " with an angle of " +
			// Math.toDegrees(angularSector[singleSon])
			// + " to (" + nodePositionsX[singleSon] + "|" +
			// nodePositionsY[singleSon] + "), tS = "
			// + Math.toDegrees(thetaSum));
		}
		freeAngle[node] = (2 * Math.PI) - thetaSum;
		radiuses[node] = 1.3 * calculateSmallestEnclosingCircle(tree, node);
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
			angularSector[singleSon] = (radiuses[singleSon] / sumRadius) * 2
					* Math.PI;
		}
	}

	private double calculateSmallestEnclosingCircle(SpanningTree tree, int node) {
		int[] sons = tree.getChildren(node);
		LinkedList<Circle> s = new LinkedList<BubbleTree.Circle>();
		for (int singleSon : sons) {
			Circle temp = new Circle(nodePositionsX[singleSon],
					nodePositionsY[singleSon], radiuses[singleSon]);
			s.add(temp);
		}
		Collections.shuffle(s);
		Circle c1 = s.removeFirst();
		Circle c2;
		while (!s.isEmpty()) {
			c2 = s.removeFirst();
			c1 = enclosingCircle(c1, c2);
		}

		return c1.radius;
	}

	private Circle enclosingCircle(Circle c1, Circle c2) {
		/*
		 * Thanks to http://stackoverflow.com/a/2086118/1116230
		 */
		if (circleInEnclosingCircle(c1, c2)) {
			return c2;
		} else if (circleInEnclosingCircle(c2, c1)) {
			return c1;
		} else {
			if (c1.radius > c2.radius)
				return enclosingCircle(c2, c1);
			double newRadius = (c1.radius + c2.radius + c1.getCenter()
					.distanceTo(c2.getCenter())) / 2;
			double theta = 0.5 + (c2.radius - c1.radius)
					/ (2 * c1.getCenter().distanceTo(c2.getCenter()));
			double centerX = (1 - theta) * c1.x + theta * c2.x;
			double centerY = (1 - theta) * c1.y + theta * c2.y;
			return new Circle(centerX, centerY, newRadius);
		}
	}

	private boolean circleInEnclosingCircle(Circle inner, Circle outer) {
		double distanceOfCenters = inner.getCenter().distanceTo(
				outer.getCenter());
		return (distanceOfCenters + inner.radius < outer.radius);
	}

	private void coordAssign(SpanningTree tree, int source, Point center) {
		nodePositionsX[source] = center.x;
		nodePositionsY[source] = center.y;

		int[] children = tree.getChildren(source);
		if (children.length == 0) {
			/*
			 * No more to do for a leaf
			 */
			return;
		}

		double rotation = 0;
		int parentID = tree.getParent(source);
		if (parentID != -1) {
			Point parent = new Point(nodePositionsX[parentID],
					nodePositionsY[parentID]);
			rotation = parent.angleTo(center);
			if (center.x > parent.x) {
				rotation = rotation + Math.PI;
			}
			// System.out.println("Calculated an angle of " +
			// Math.toDegrees(rotation) + " between " + source + " at "
			// + center + " and its parent " + parentID + " at " + parent);
			// System.out.print("Setting rotation to -" +
			// Math.toDegrees(rotation) + " + "
			// + Math.toDegrees(freeAngle[source] / 2) + "=");
			rotation = rotation + (freeAngle[source] / 2);
			// System.out.println(Math.toDegrees(rotation));
		}

		for (int singleChild : children) {
			Point temp = new Point(nodePositionsX[singleChild],
					nodePositionsY[singleChild]);
			// System.out.println("Rotating " + singleChild + " by " +
			// Math.toDegrees(rotation));
			temp.rotateBy(rotation);
			temp.add(center);
			coordAssign(tree, singleChild, temp);
		}
	}

	private class Circle {
		public double x, y, radius;

		public Circle(double x, double y, double radius) {
			this.x = x;
			this.y = y;
			this.radius = radius;
		}

		public Point getCenter() {
			return new Point(x, y);
		}
	}

	private class Point {
		public double x, y;

		public Point(double nodePositionsX, double nodePositionsY) {
			this.x = nodePositionsX;
			this.y = nodePositionsY;
		}

		public double distanceTo(Point p2) {
			return Math.sqrt(Math.pow(this.x + p2.x, 2)
					+ Math.pow(this.y + p2.y, 2));
		}

		public void add(Point p2) {
			this.x += p2.x;
			this.y += p2.y;
		}

		public void rotateBy(double angle) {
			double newX = x * Math.cos(angle) - y * Math.sin(angle);
			double newY = x * Math.sin(angle) + y * Math.cos(angle);
			this.x = newX;
			this.y = newY;
		}

		public double angleTo(Point p2) {
			return Math.atan((p2.y - this.y) / (p2.x - this.x));
		}

		public String toString() {
			return "(" + x + "|" + y + ")";
		}
	}
}
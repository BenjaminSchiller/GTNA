///*
// * ===========================================================
// * GTNA : Graph-Theoretic Network Analyzer
// * ===========================================================
// * 
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors
// * 
// * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
// * 
// * GTNA is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * 
// * GTNA is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// * 
// * ---------------------------------------
// * CAN.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// * 
// * Original Author: Benjamin Schiller;
// * Contributors:    -;
// * 
// * Changes since 2011-05-17
// * ---------------------------------------
// */
//package gtna.trash.networks.p2p;
//
//import gtna.graph.Edges;
//import gtna.graph.Graph;
//import gtna.graph.Node;
//import gtna.networks.Network;
//import gtna.networks.NetworkImpl;
//import gtna.routing.RoutingAlgorithm;
//import gtna.transformation.Transformation;
//import gtna.trash.routing.node.IDNode;
//import gtna.trash.routing.node.identifier.GridID;
//import gtna.trash.routing.node.identifier.Identifier;
//
//import java.util.ArrayList;
//import java.util.Random;
//
///**
// * Implements the network generator for the P2P network topology of CAN, the
// * Content Addressable Network described by Ratnasamy et al. in their paper
// * "A scalable content-addressable network" from 2001.
// * 
// * Implemented parameters are the number of dimensions of the identifier space
// * and the number of realities.
// * 
// * @author benni
// * 
// */
//public class CAN extends NetworkImpl implements Network {
//	private int DIMENSIONS = 2;
//
//	private int REALITIES = 1;
//
//	private static double MAX_ZONE_LENGTH = 1024;
//
//	public CAN(int nodes, int DIMENSIONS, int REALITIES, RoutingAlgorithm ra,
//			Transformation[] t) {
//		super("CAN", nodes, new String[] { "DIMENSIONS", "REALITIES" },
//				new String[] { "" + DIMENSIONS, "" + REALITIES }, ra, t);
//		this.DIMENSIONS = DIMENSIONS;
//		this.REALITIES = REALITIES;
//	}
//
//	public static CAN[] get(int[] n, int d, int r, RoutingAlgorithm ra,
//			Transformation[] t) {
//		CAN[] nw = new CAN[n.length];
//		for (int i = 0; i < n.length; i++) {
//			nw[i] = new CAN(n[i], d, r, ra, t);
//		}
//		return nw;
//	}
//
//	public static CAN[] get(int n, int d[], int r, RoutingAlgorithm ra,
//			Transformation[] t) {
//		CAN[] nw = new CAN[d.length];
//		for (int i = 0; i < d.length; i++) {
//			nw[i] = new CAN(n, d[i], r, ra, t);
//		}
//		return nw;
//	}
//
//	public static CAN[] get(int n, int d, int r[], RoutingAlgorithm ra,
//			Transformation[] t) {
//		CAN[] nw = new CAN[r.length];
//		for (int i = 0; i < r.length; i++) {
//			nw[i] = new CAN(n, d, r[i], ra, t);
//		}
//		return nw;
//	}
//
//	public static CAN[][] get(int[] n, int[] d, int r, RoutingAlgorithm ra,
//			Transformation[] t) {
//		CAN[][] nw = new CAN[d.length][n.length];
//		for (int i = 0; i < d.length; i++) {
//			for (int j = 0; j < n.length; j++) {
//				nw[i][j] = new CAN(n[j], d[i], r, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static CAN[][] get(int[] n, int d, int[] r, RoutingAlgorithm ra,
//			Transformation[] t) {
//		CAN[][] nw = new CAN[r.length][n.length];
//		for (int i = 0; i < r.length; i++) {
//			for (int j = 0; j < n.length; j++) {
//				nw[i][j] = new CAN(n[j], d, r[i], ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static CAN[][] getXY(int n, int[] d, int[] r, RoutingAlgorithm ra,
//			Transformation[] t) {
//		CAN[][] nw = new CAN[r.length][d.length];
//		for (int i = 0; i < r.length; i++) {
//			for (int j = 0; j < d.length; j++) {
//				nw[i][j] = new CAN(n, d[j], r[i], ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static CAN[][] getYX(int n, int[] d, int[] r, RoutingAlgorithm ra,
//			Transformation[] t) {
//		CAN[][] nw = new CAN[d.length][r.length];
//		for (int i = 0; i < d.length; i++) {
//			for (int j = 0; j < r.length; j++) {
//				nw[i][j] = new CAN(n, d[i], r[j], ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static CAN[] get(int n, int d, int r, RoutingAlgorithm[] ra,
//			Transformation[] t) {
//		CAN[] nw = new CAN[ra.length];
//		for (int i = 0; i < nw.length; i++) {
//			nw[i] = new CAN(n, d, r, ra[i], t);
//		}
//		return nw;
//	}
//
//	public static CAN[][] get(int n, int d, int r, RoutingAlgorithm[][] ra,
//			Transformation[] t) {
//		CAN[][] nw = new CAN[ra.length][ra[0].length];
//		for (int i = 0; i < nw.length; i++) {
//			for (int j = 0; j < nw[i].length; j++) {
//				nw[i][j] = new CAN(n, d, r, ra[i][j], t);
//			}
//		}
//		return nw;
//	}
//
//	public Graph generate() {
//		Graph graph = new Graph(this.description());
//		Random rand = new Random(System.currentTimeMillis());
//		CANNode[] nodes = new CANNode[this.nodes()];
//		for (int i = 0; i < nodes.length; i++) {
//			nodes[i] = new CANNode(this.REALITIES, i, graph);
//		}
//		for (int reality = 0; reality < this.REALITIES; reality++) {
//			double[][] coordinates = new double[this.DIMENSIONS][2];
//			for (int i = 0; i < this.DIMENSIONS; i++) {
//				coordinates[i][0] = 0;
//				coordinates[i][1] = MAX_ZONE_LENGTH;
//			}
//			Reality start = new Reality(coordinates, 0, 0, nodes[0]);
//			nodes[0].addRealitiy(start, reality);
//			for (int i = 1; i < nodes.length; i++) {
//				double[] point = this.getRandomPoint(rand);
//				for (int j = 0; j < i; j++) {
//					if (nodes[j].realities[reality].contains(point)) {
//						Reality newOne = nodes[j].realities[reality].split(i,
//								nodes[i]);
//						nodes[i].addRealitiy(newOne, reality);
//						// System.out
//						// .println(i
//						// + ". "
//						// + nodes[i].realities[reality]
//						// .isNeighbor(nodes[j].realities[reality])
//						// + " from " + j);
//						// System.out.println("    " + i + ". "
//						// + nodes[i].realities[reality]);
//						// System.out.println("    " + j + ". "
//						// + nodes[j].realities[reality]);
//						break;
//					}
//				}
//			}
//
//			for (int i = 0; i < nodes.length; i++) {
//				nodes[i].realities[reality].neighbors = new ArrayList<Reality>();
//				for (int j = 0; j < nodes.length; j++) {
//					if (nodes[i].realities[reality]
//							.isNeighbor(nodes[j].realities[reality])) {
//						// System.out.println("\n    "
//						// + nodes[i].realities[reality]);
//						// System.out
//						// .println(" => " + nodes[j].realities[reality]);
//						nodes[i].realities[reality].neighbors
//								.add(nodes[j].realities[reality]);
//					} else {
//						// System.out.println("\n     "
//						// + nodes[i].realities[reality]);
//						// System.out.println(" !=> "
//						// + nodes[j].realities[reality]);
//					}
//				}
//			}
//		}
//
//		Edges edges = new Edges(nodes, (int) (this.nodes()
//				* Math.pow(2, this.DIMENSIONS) * 1.5));
//		for (int i = 0; i < nodes.length; i++) {
//			for (int j = 0; j < nodes[i].realities.length; j++) {
//				for (int k = 0; k < nodes[i].realities[j].neighbors.size(); k++) {
//					edges.add(i, nodes[i].realities[j].neighbors.get(k).node
//							.getIndex());
//				}
//			}
//		}
//		edges.fill();
//		graph.setNodes(nodes);
//		return graph;
//	}
//
//	private double[] getRandomPoint(Random rand) {
//		double[] point = new double[this.DIMENSIONS];
//		for (int i = 0; i < this.DIMENSIONS; i++) {
//			point[i] = rand.nextDouble() * MAX_ZONE_LENGTH;
//		}
//		return point;
//	}
//
//	// private static String toString(double[] point) {
//	// String bla = point[0] + "";
//	// for (int i = 1; i < point.length; i++) {
//	// bla += " / " + point[i];
//	// }
//	// return "[" + bla + "]";
//	// }
//
//	private static String toString(double[][] point) {
//		String bla = "";
//		for (int i = 0; i < point.length; i++) {
//			bla += " [" + (int) point[i][0] + ", " + (int) point[i][1] + "]";
//		}
//		return bla;
//	}
//
//	// private class CANID implements ID {
//	// double[] id;
//	//
//	// private CANID(double[] id) {
//	// this.id = id;
//	// }
//	//
//	// public double dist(ID id) {
//	// double distance = 0;
//	// for (int i = 0; i < this.id.length; i++) {
//	// double min = Math.min(((CANID) id).id[i], this.id[i]);
//	// double max = Math.max(((CANID) id).id[i], this.id[i]);
//	// double dist1 = max - min;
//	// double dist2 = MAX_ZONE_LENGTH + min - max;
//	// distance += Math.pow(Math.min(dist1, dist2), this.id.length);
//	// }
//	// return distance;
//	// }
//	//
//	// }
//
//	private class CANNode extends Node {
//		private Reality[] realities;
//
//		private boolean marked = false;
//
//		private CANNode(int realities, int index, Graph graph) {
//			super(index, graph);
//			this.realities = new Reality[realities];
//		}
//
//		private void addRealitiy(Reality node, int reality) {
//			this.realities[reality] = node;
//		}
//
//		public int route(Node n2) {
//			return this.route(((CANNode) n2).realities[0].center);
//		}
//
//		private int route(double[] point) {
//			if (this.contains(point)) {
//				return 0;
//			}
//			double distance = Double.MAX_VALUE;
//			CANNode best = null;
//			for (int i = 0; i < this.realities.length; i++) {
//				for (int j = 0; j < this.realities[i].neighbors.size(); j++) {
//					Reality r = this.realities[i].neighbors.get(j);
//					if (r.node.marked) {
//						continue;
//					}
//					double d = r.distance(point);
//					if (d < distance) {
//						distance = d;
//						best = r.node;
//					}
//				}
//			}
//			this.marked = true;
//			int rl = 1 + best.route(point);
//			this.marked = false;
//			return rl;
//		}
//
//		private boolean contains(double[] point) {
//			for (int i = 0; i < this.realities.length; i++) {
//				if (this.realities[i].contains(point)) {
//					return true;
//				}
//			}
//			return false;
//		}
//
//		public boolean contains(Identifier id) {
//			for (int i = 0; i < this.realities.length; i++) {
//				if (this.realities[i].contains(((GridID) id).x)) {
//					return true;
//				}
//			}
//			return false;
//		}
//
//		public double dist(Identifier id) {
//			double min = Double.MAX_VALUE;
//			for (int i = 0; i < this.realities.length; i++) {
//				double dist = this.realities[i].distance(((GridID) id).x);
//				if (dist < min) {
//					min = dist;
//				}
//			}
//			return min;
//		}
//
//		public Identifier randomID(Random rand, Node[] nodes) {
//			double[] id = new double[this.realities[0].center.length];
//			for (int i = 0; i < id.length; i++) {
//				id[i] = rand.nextDouble() * MAX_ZONE_LENGTH;
//			}
//			return new GridID(id);
//		}
//
//		public double dist(IDNode node) {
//			return ((CANNode) node).dist(this.realities[0].id);
//		}
//
//		public String toString() {
//			return this.getIndex() + " "
//					+ CAN.toString(this.realities[0].coordinates);
//		}
//	}
//
//	private class Reality {
//		private double[][] coordinates;
//
//		private double[] center;
//
//		private GridID id;
//
//		private int timesDivided;
//
//		private ArrayList<Reality> neighbors;
//
//		private CANNode node;
//
//		private Reality(double[][] coordinates, int index, int timesDivided,
//				CANNode node) {
//			this.coordinates = coordinates;
//			this.timesDivided = timesDivided;
//			this.center = new double[this.coordinates.length];
//			this.neighbors = new ArrayList<Reality>();
//			for (int i = 0; i < center.length; i++) {
//				this.center[i] = this.coordinates[i][0]
//						+ (this.coordinates[i][1] - this.coordinates[i][0]) / 2;
//			}
//			this.id = new GridID(this.center);
//			this.node = node;
//		}
//
//		private boolean isCenter(double[] point) {
//			for (int i = 0; i < point.length; i++) {
//				if (point[i] != this.center[i]) {
//					return false;
//				}
//			}
//			return true;
//		}
//
//		private boolean contains(double[] point) {
//			for (int i = 0; i < point.length; i++) {
//				if (!this.in(this.coordinates[i], point[i])) {
//					return false;
//				}
//			}
//			return true;
//		}
//
//		private double distance(double[] point) {
//			double distance = 0;
//			for (int i = 0; i < point.length; i++) {
//				double p = point[i];
//				double x1 = this.coordinates[i][0];
//				double x2 = this.coordinates[i][1];
//				if (p < x1) {
//					distance += Math.pow(x1 - p, point.length);
//				} else if (p > x2) {
//					distance += Math.pow(p - x2, point.length);
//				}
//			}
//			return Math.pow(distance, 1.0 / (double) point.length);
//		}
//
//		private boolean isNeighbor(Reality reality) {
//			if (this.isCenter(reality.center)) {
//				return false;
//			}
//			int meet = 0;
//			int overlap = 0;
//			for (int i = 0; i < this.coordinates.length; i++) {
//				double[] i1 = this.coordinates[i];
//				double[] i2 = reality.coordinates[i];
//				if (this.overlap(i1, i2)) {
//					overlap++;
//				}
//				if (this.meet(i1, i2)) {
//					meet++;
//				}
//			}
//			return meet >= 1 && overlap == this.coordinates.length - 1;
//		}
//
//		private Reality split(int index, CANNode container) {
//			int splitDimension = this.timesDivided % this.coordinates.length;
//			double[][] coordinates = new double[this.coordinates.length][2];
//			for (int i = 0; i < this.coordinates.length; i++) {
//				if (i == splitDimension) {
//					double a = this.coordinates[i][0];
//					double b = this.coordinates[i][1];
//					double middle = a + (b - a) / 2;
//					coordinates[i][0] = middle;
//					coordinates[i][1] = this.coordinates[i][1];
//					this.coordinates[i][1] = middle;
//				} else {
//					coordinates[i][0] = this.coordinates[i][0];
//					coordinates[i][1] = this.coordinates[i][1];
//				}
//			}
//			for (int i = 0; i < center.length; i++) {
//				this.center[i] = this.coordinates[i][0]
//						+ (this.coordinates[i][1] - this.coordinates[i][0]) / 2;
//			}
//			this.timesDivided++;
//			Reality reality = new Reality(coordinates, index,
//					this.timesDivided, container);
//			ArrayList<Reality> allNeighbors = this.neighbors;
//			allNeighbors.add(this);
//			allNeighbors.add(reality);
//			this.neighbors = new ArrayList<Reality>();
//			for (int i = 0; i < allNeighbors.size(); i++) {
//				Reality neighbor = allNeighbors.get(i);
//				if (neighbor.isNeighbor(this)) {
//					this.neighbors.add(neighbor);
//				}
//				if (neighbor.isNeighbor(reality)) {
//					reality.neighbors.add(neighbor);
//				}
//			}
//			return reality;
//		}
//
//		private boolean in(double[] interval, double point) {
//			double x = interval[0];
//			double y = interval[1];
//			if (x <= point && point < y) {
//				return true;
//			}
//			return false;
//		}
//
//		private boolean overlap(double[] i1, double[] i2) {
//			double a1 = i1[0];
//			double a2 = i1[1];
//			double b1 = i2[0];
//			double b2 = i2[1];
//			if (b2 <= a1) {
//				return false;
//			}
//			if (a2 <= b1) {
//				return false;
//			}
//			return true;
//		}
//
//		private boolean meet(double[] i1, double[] i2) {
//			double a1 = i1[0];
//			double a2 = i1[1];
//			double b1 = i2[0];
//			double b2 = i2[1];
//			if (a2 == b1) {
//				return true;
//			}
//			if (b2 == a1) {
//				return true;
//			}
//			if (a2 == MAX_ZONE_LENGTH && b1 == 0) {
//				return true;
//			}
//			if (b2 == MAX_ZONE_LENGTH && a1 == 0) {
//				return true;
//			}
//			return false;
//		}
//
//		public String toString() {
//			return this.node.getIndex() + ". " + CAN.toString(this.coordinates);
//		}
//	}
//}

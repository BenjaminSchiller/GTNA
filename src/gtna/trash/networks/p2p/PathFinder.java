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
// * PathFinder.java
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
//import gtna.util.Util;
//
//import java.util.ArrayList;
//import java.util.Random;
//
///**
// * Implements a network generator for the P2P network PathFinder based on
// * deterministically built random graphs. It was developed by Bradler et al. and
// * introduced in their paper
// * "PathFinder: Efficient Lookups and Efficient Search in Peer-to-Peer Networks"
// * (2011).
// * 
// * The parameters are the average number of virtual nodes per peer, the average
// * number of neighbors per peer, the number of additional lookups to achieve
// * better load balancing between all peers (not part of the publication), and a
// * flag for the bidirectionality of links.
// * 
// * @author benni
// * 
// */
//public class PathFinder extends NetworkImpl implements Network {
//	private double VIRTUAL_NODES_PER_PEER = 2.0;
//
//	private int AVERAGE_NUMBER_OF_NEIGHBORS = 20;
//
//	private int ADDITIONAL_LOOKUPS = 0;
//
//	private boolean BIDIRECTIONAL = true;
//
//	private int NUMBER_OF_VIRTUAL_NODES = -1;
//
//	private long SEED1 = -1;
//
//	private long SEED2 = -1;
//
//	public PathFinder(int nodes, double VIRTUAL_NODES_PER_PEER,
//			int AVERAGE_NUMBER_OF_NEIGHBORS, int ADDITIONAL_LOOKUPS,
//			boolean BIDIRECTIONAL, RoutingAlgorithm ra, Transformation[] t) {
//		super("PATH_FINDER", nodes, new String[] { "VIRTUAL_NODES_PER_PEER",
//				"AVERAGE_NUMBER_OF_NEIGHBORS", "ADDITIONAL_LOOKUPS",
//				"BIDIRECTIONAL" }, new String[] { "" + VIRTUAL_NODES_PER_PEER,
//				"" + AVERAGE_NUMBER_OF_NEIGHBORS, "" + ADDITIONAL_LOOKUPS,
//				"" + BIDIRECTIONAL }, ra, t);
//		this.VIRTUAL_NODES_PER_PEER = VIRTUAL_NODES_PER_PEER;
//		this.AVERAGE_NUMBER_OF_NEIGHBORS = AVERAGE_NUMBER_OF_NEIGHBORS;
//		this.ADDITIONAL_LOOKUPS = ADDITIONAL_LOOKUPS;
//		this.BIDIRECTIONAL = BIDIRECTIONAL;
//		this.NUMBER_OF_VIRTUAL_NODES = (int) (this.nodes() * this.VIRTUAL_NODES_PER_PEER);
//		this.SEED1 = computeRandomSeed();
//		this.SEED2 = computeRandomSeed();
//	}
//
//	public static PathFinder[] get(int[] n, double v, int a, int l, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		PathFinder[] nw = new PathFinder[n.length];
//		for (int i = 0; i < nw.length; i++) {
//			nw[i] = new PathFinder(n[i], v, a, l, b, ra, t);
//		}
//		return nw;
//	}
//
//	public static PathFinder[] get(int n, double[] v, int a, int l, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		PathFinder[] nw = new PathFinder[v.length];
//		for (int i = 0; i < nw.length; i++) {
//			nw[i] = new PathFinder(n, v[i], a, l, b, ra, t);
//		}
//		return nw;
//	}
//
//	public static PathFinder[][] get(int[] n, double[] v, int a, int l,
//			boolean b, RoutingAlgorithm ra, Transformation[] t) {
//		PathFinder[][] nw = new PathFinder[v.length][n.length];
//		for (int i = 0; i < nw.length; i++) {
//			for (int j = 0; j < n.length; j++) {
//				nw[i][j] = new PathFinder(n[j], v[i], a, l, b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static PathFinder[] get(int n, double v, int[] a, int l, boolean b,
//			RoutingAlgorithm ra, Transformation[] t) {
//		PathFinder[] nw = new PathFinder[a.length];
//		for (int i = 0; i < nw.length; i++) {
//			nw[i] = new PathFinder(n, v, a[i], l, b, ra, t);
//		}
//		return nw;
//	}
//
//	public static PathFinder[][] get(int[] n, double v, int[] a, int l,
//			boolean b, RoutingAlgorithm ra, Transformation[] t) {
//		PathFinder[][] nw = new PathFinder[a.length][n.length];
//		for (int i = 0; i < nw.length; i++) {
//			for (int j = 0; j < n.length; j++) {
//				nw[i][j] = new PathFinder(n[j], v, a[i], l, b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static PathFinder[][] getXY(int n, double[] v, int[] a, int l,
//			boolean b, RoutingAlgorithm ra, Transformation[] t) {
//		PathFinder[][] nw = new PathFinder[a.length][v.length];
//		for (int i = 0; i < nw.length; i++) {
//			for (int j = 0; j < nw[i].length; j++) {
//				nw[i][j] = new PathFinder(n, v[j], a[i], l, b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public static PathFinder[][] getYX(int n, double[] v, int[] a, int l,
//			boolean b, RoutingAlgorithm ra, Transformation[] t) {
//		PathFinder[][] nw = new PathFinder[v.length][a.length];
//		for (int i = 0; i < nw.length; i++) {
//			for (int j = 0; j < nw[i].length; j++) {
//				nw[i][j] = new PathFinder(n, v[i], a[j], l, b, ra, t);
//			}
//		}
//		return nw;
//	}
//
//	public Graph generate() {
//		Graph graph = new Graph(this.description());
//
//		VirtualNode[] virtualNodes = new VirtualNode[this.NUMBER_OF_VIRTUAL_NODES];
//		for (int i = 0; i < virtualNodes.length; i++) {
//			virtualNodes[i] = new VirtualNode(i);
//		}
//		for (int i = 0; i < virtualNodes.length; i++) {
//			int[] neighborIDs = getVirtualNeighborIDs(i, this);
//			virtualNodes[i].neighbors = getVirtualNeighbors(neighborIDs,
//					virtualNodes);
//		}
//
//		long seed = computeRandomSeed();
//		Random rand = new Random(seed);
//		PhysicalNode[] nodes = new PhysicalNode[this.nodes()];
//		nodes[0] = new PhysicalNode(0, graph, virtualNodes.clone(), this);
//		for (int i = 1; i < nodes.length; i++) {
//			PhysicalNode currentOwner;
//			int newVirtualID;
//			VirtualNode newVirtualNode;
//			do {
//				if ((this.nodes() % 10) == 0) {
//					newVirtualID = rand.nextInt(this.NUMBER_OF_VIRTUAL_NODES);
//					newVirtualNode = virtualNodes[newVirtualID];
//					currentOwner = newVirtualNode.physicalNode;
//					for (int j = 0; j < this.ADDITIONAL_LOOKUPS; j++) {
//						int replacementID = rand
//								.nextInt(this.NUMBER_OF_VIRTUAL_NODES);
//						VirtualNode replacementNode = virtualNodes[replacementID];
//						PhysicalNode replacementOwner = replacementNode.physicalNode;
//						if (replacementOwner.virtualNodes.length > currentOwner.virtualNodes.length) {
//							newVirtualID = replacementID;
//							newVirtualNode = replacementNode;
//							currentOwner = replacementOwner;
//						}
//					}
//				} else {
//					newVirtualID = rand.nextInt(this.NUMBER_OF_VIRTUAL_NODES);
//					newVirtualNode = virtualNodes[newVirtualID];
//					currentOwner = newVirtualNode.physicalNode;
//				}
//			} while (currentOwner.virtualNodes.length <= 1);
//			VirtualNode[] newList = currentOwner.handOver(rand);
//			nodes[i] = new PhysicalNode(i, graph, newList, this);
//		}
//
//		int numberOfEdges = this.NUMBER_OF_VIRTUAL_NODES
//				* (this.AVERAGE_NUMBER_OF_NEIGHBORS + 1);
//		if (this.BIDIRECTIONAL) {
//			numberOfEdges *= 2;
//		}
//		Edges edges = new Edges(nodes, numberOfEdges);
//		for (int i = 0; i < virtualNodes.length; i++) {
//			PhysicalNode src = virtualNodes[i].physicalNode;
//			for (int j = 0; j < virtualNodes[i].neighbors.length; j++) {
//				PhysicalNode dst = virtualNodes[i].neighbors[j].physicalNode;
//				if (src.getIndex() != dst.getIndex()) {
//					edges.add(src.getIndex(), dst.getIndex());
//					if (this.BIDIRECTIONAL) {
//						edges.add(dst.getIndex(), src.getIndex());
//					}
//				}
//			}
//		}
//		edges.fill();
//		graph.setNodes(nodes);
//		return graph;
//	}
//
//	private static long computeRandomSeed() {
//		return ((System.currentTimeMillis() % 100) + 1)
//				* ((System.currentTimeMillis() % 1000) + 1)
//				* ((System.currentTimeMillis() % 100) + 1)
//				* ((System.currentTimeMillis() % 10) + 1);
//	}
//
//	private static int getNumberOfVirtualNeighbors(int id, PathFinder nw) {
//		double c = nw.AVERAGE_NUMBER_OF_NEIGHBORS;
//		Random rand = new Random(nw.SEED1 + id);
//		int x = 0;
//		double t = 0.0;
//		while (true) {
//			t -= Math.log(rand.nextDouble()) / c;
//			if (t > 1.0) {
//				if (x < 1)
//					x = 1;
//				return x;
//			}
//			x++;
//		}
//	}
//
//	private static int[] getVirtualNeighborIDs(int id, PathFinder nw) {
//		Random rand = new Random(nw.SEED2 + id);
//		int numberOfNeighbors = getNumberOfVirtualNeighbors(id, nw);
//		ArrayList<Integer> list = new ArrayList<Integer>(numberOfNeighbors);
//		while (list.size() < numberOfNeighbors) {
//			int newId = rand.nextInt(nw.NUMBER_OF_VIRTUAL_NODES);
//			if (!list.contains(newId) && newId != id) {
//				list.add(newId);
//			}
//		}
//		return Util.toIntegerArray(list);
//	}
//
//	private static VirtualNode[] getVirtualNeighbors(int[] ids,
//			VirtualNode[] virtualNodes) {
//		VirtualNode[] nodes = new VirtualNode[ids.length];
//		for (int i = 0; i < nodes.length; i++) {
//			nodes[i] = virtualNodes[ids[i]];
//		}
//		return nodes;
//	}
//
//	public static int[] vnd(Graph g) {
//		int max = 0;
//		for (int i = 0; i < g.getNodes().length; i++) {
//			if (((PhysicalNode) g.getNodes()[i]).virtualNodes.length > max) {
//				max = ((PhysicalNode) g.getNodes()[i]).virtualNodes.length;
//			}
//		}
//		int[] vns = new int[max + 1];
//		for (int i = 0; i < g.getNodes().length; i++) {
//			vns[((PhysicalNode) g.getNodes()[i]).virtualNodes.length]++;
//		}
//		return vns;
//	}
//
//	public static double[] vndp(Graph g) {
//		int[] vnd = vnd(g);
//		double[] vndp = new double[vnd.length];
//		for (int i = 0; i < vnd.length; i++) {
//			vndp[i] = (double) vnd[i] / (double) g.getNodes().length;
//		}
//		return vndp;
//	}
//
//	private static class PhysicalNode extends Node {
//		private VirtualNode[] virtualNodes;
//
//		private PathFinder nw;
//
//		private PhysicalNode(int index, Graph graph, VirtualNode[] vns,
//				PathFinder nw) {
//			super(index, graph);
//			this.virtualNodes = vns;
//			this.nw = nw;
//			for (int i = 0; i < vns.length; i++) {
//				vns[i].physicalNode = this;
//			}
//		}
//
//		public int route(Node node) {
//			PhysicalNode pn = (PhysicalNode) node;
//			return this.routePN(pn);
//		}
//
//		private int routePN(PhysicalNode pn) {
//			int min = this.routeVN(pn.virtualNodes[0]);
//			for (int i = 1; i < pn.virtualNodes.length; i++) {
//				int r = this.routeVN(pn.virtualNodes[i]);
//				if (r < min) {
//					min = r;
//				}
//			}
//			return min;
//		}
//
//		private int routeVN(VirtualNode vn) {
//			if (this.nw.BIDIRECTIONAL) {
//				return this.routeBidirectional(vn);
//			} else {
//				return this.routeUnidirectional(vn);
//			}
//		}
//
//		private int routeUnidirectional(VirtualNode dst) {
//			int[] fromSrc = new int[this.nw.NUMBER_OF_VIRTUAL_NODES];
//			for (int i = 0; i < fromSrc.length; i++) {
//				fromSrc[i] = -1;
//			}
//			for (int i = 0; i < this.virtualNodes.length; i++) {
//				fromSrc[this.virtualNodes[i].id] = 0;
//			}
//
//			int step = 0;
//			while (step < 100) {
//				for (int i = 0; i < fromSrc.length; i++) {
//					if (fromSrc[i] == step) {
//						int[] neighbors = getVirtualNeighborIDs(i, this.nw);
//						for (int j = 0; j < neighbors.length; j++) {
//							if (fromSrc[neighbors[j]] == -1) {
//								fromSrc[neighbors[j]] = fromSrc[i] + 1;
//							}
//							if (neighbors[j] == dst.id) {
//								return fromSrc[neighbors[j]];
//							}
//						}
//					}
//				}
//				step++;
//			}
//			return 666;
//		}
//
//		private int routeBidirectional(VirtualNode dest) {
//			int[] fromSrc = new int[this.nw.NUMBER_OF_VIRTUAL_NODES];
//			int[] fromDst = new int[this.nw.NUMBER_OF_VIRTUAL_NODES];
//			for (int i = 0; i < fromSrc.length; i++) {
//				fromSrc[i] = -1;
//				fromDst[i] = -1;
//			}
//			for (int i = 0; i < this.virtualNodes.length; i++) {
//				fromSrc[this.virtualNodes[i].id] = 0;
//			}
//			fromDst[dest.id] = 0;
//
//			int step = 0;
//			while (step < 100) {
//				for (int i = 0; i < fromSrc.length; i++) {
//					if (fromSrc[i] == step) {
//						int[] neighbors = getVirtualNeighborIDs(i, this.nw);
//						for (int j = 0; j < neighbors.length; j++) {
//							if (fromSrc[neighbors[j]] == -1) {
//								fromSrc[neighbors[j]] = fromSrc[i] + 1;
//							}
//						}
//					}
//				}
//				for (int i = 0; i < fromDst.length; i++) {
//					if (fromDst[i] == step) {
//						int[] neighbors = getVirtualNeighborIDs(i, this.nw);
//						for (int j = 0; j < neighbors.length; j++) {
//							if (fromDst[neighbors[j]] == -1) {
//								fromDst[neighbors[j]] = fromDst[i] + 1;
//							}
//						}
//					}
//				}
//				int min = Integer.MAX_VALUE;
//				for (int i = 0; i < fromSrc.length; i++) {
//					if (fromSrc[i] != -1 && fromDst[i] != -1
//							&& fromSrc[i] + fromDst[i] < min) {
//						min = fromSrc[i] + fromDst[i];
//					}
//				}
//				if (min != Integer.MAX_VALUE) {
//					return min;
//				}
//				step++;
//			}
//			return 666;
//		}
//
//		private VirtualNode[] handOver(Random rand) {
//			if (this.virtualNodes.length == 1) {
//				return null;
//			}
//			int amount = -1;
//			if (this.virtualNodes.length % 2 == 0) {
//				amount = this.virtualNodes.length / 2;
//			} else if (rand.nextBoolean()) {
//				amount = (this.virtualNodes.length + 1) / 2;
//			} else {
//				amount = (this.virtualNodes.length - 1) / 2;
//			}
//			VirtualNode[] newList = new VirtualNode[amount];
//			VirtualNode[] oldList = new VirtualNode[this.virtualNodes.length
//					- amount];
//			for (int i = 0; i < amount; i++) {
//				newList[i] = this.virtualNodes[i];
//			}
//			for (int i = amount; i < this.virtualNodes.length; i++) {
//				oldList[i - amount] = this.virtualNodes[i];
//			}
//			this.virtualNodes = oldList;
//			return newList;
//		}
//	}
//
//	private class VirtualNode {
//		private int id;
//
//		private VirtualNode[] neighbors;
//
//		private PhysicalNode physicalNode;
//
//		private VirtualNode(int id) {
//			this.id = id;
//		}
//	}
//}

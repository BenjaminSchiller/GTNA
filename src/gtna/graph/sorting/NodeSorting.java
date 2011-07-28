/*
 * ===========================================================
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
 * NodeSorting.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.graph.sorting;

import gtna.graph.Node;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class NodeSorting {
	public static Node[] degreeDesc(Node[] nodes, Random rand) {
		Node[] sorted = nodes.clone();
		Arrays.sort(sorted, new DegreeDesc());
		int value = sorted[0].getDegree();
		int from = 0;
		for (int i = 1; i < sorted.length; i++) {
			if (sorted[i].getDegree() != value) {
				randomize(sorted, rand, from, i - 1);
				value = sorted[i].getDegree();
				from = i;
			}
		}
		randomize(sorted, rand, from, sorted.length - 1);
		return sorted;
	}

	private static class DegreeDesc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			int v1 = n1.getDegree();
			int v2 = n2.getDegree();
			if (v1 == v2) {
				return 0;
			} else if (v1 > v2) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	public static Node[] inDegreeDesc(Node[] nodes, Random rand) {
		Node[] sorted = nodes.clone();
		Arrays.sort(sorted, new InDegreeDesc());
		int value = sorted[0].getInDegree();
		int from = 0;
		for (int i = 1; i < sorted.length; i++) {
			if (sorted[i].getInDegree() != value) {
				randomize(sorted, rand, from, i - 1);
				value = sorted[i].getInDegree();
				from = i;
			}
		}
		randomize(sorted, rand, from, sorted.length - 1);
		return sorted;
	}

	private static class InDegreeDesc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			int v1 = n1.getInDegree();
			int v2 = n2.getInDegree();
			if (v1 == v2) {
				return 0;
			} else if (v1 > v2) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	public static Node[] outDegreeDesc(Node[] nodes, Random rand) {
		Node[] sorted = nodes.clone();
		Arrays.sort(sorted, new OutDegreeDesc());
		int value = sorted[0].getOutDegree();
		int from = 0;
		for (int i = 1; i < sorted.length; i++) {
			if (sorted[i].getOutDegree() != value) {
				randomize(sorted, rand, from, i - 1);
				value = sorted[i].getOutDegree();
				from = i;
			}
		}
		randomize(sorted, rand, from, sorted.length - 1);
		return sorted;
	}

	private static class OutDegreeDesc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			int v1 = n1.getOutDegree();
			int v2 = n2.getOutDegree();
			if (v1 == v2) {
				return 0;
			} else if (v1 > v2) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	public static Node[] random(Node[] nodes, Random rand) {
		Node[] sorted = nodes.clone();
		randomize(sorted, rand, 0,
				sorted.length - 1);
		return sorted;
	}

	private static void randomize(Node[] nodes, Random rand, int from, int to) {
		for (int i = to - from; i > 1; i--) {
			int index = rand.nextInt(i) + from;
			Node temp = nodes[index];
			nodes[index] = nodes[i + from];
			nodes[i + from] = temp;
		}
	}

	public static int[] byDegreeDesc(Node[] nodes, Random rand) {
		int[] degree = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			degree[i] = nodes[i].getDegree();
		}
		return getNodesSortedDescending(degree, rand);
	}

	private static int[] getNodesSortedDescending(int[] values, Random rand) {
		WrapperInt[] array = new WrapperInt[values.length];
		for (int i = 0; i < values.length; i++) {
			array[i] = new WrapperInt(i, values[i]);
		}
		Arrays.sort(array, new ValueDescInt());
		int[] sorted = new int[values.length];
		for (int i = 0; i < sorted.length; i++) {
			sorted[i] = array[i].node;
		}
		randomize(sorted, values, rand);
		return sorted;
	}

	// private static int[] getNodesSortedDescending(double[] values) {
	// WrapperDouble[] array = new WrapperDouble[values.length];
	// for (int i = 0; i < values.length; i++) {
	// array[i] = new WrapperDouble(i, values[i]);
	// }
	// Arrays.sort(array, new ValueDescDouble());
	// int[] sorted = new int[values.length];
	// for (int i = 0; i < sorted.length; i++) {
	// sorted[i] = array[i].node;
	// }
	// return sorted;
	// }

	private static void randomize(int[] sorted, int[] values, Random rand) {
		int from = 0;
		int to = 0;
		for (int i = 1; i < sorted.length; i++) {
			if (values[sorted[i]] == values[sorted[i - 1]]) {
				to++;
			} else {
				Randomizing.randomizeOrder(sorted, rand, from, to);
				from = i;
				to = i;
			}
		}
		Randomizing.randomizeOrder(sorted, rand, from, to);
	}

	private static class WrapperInt {
		private int node;

		private int value;

		private WrapperInt(int node, int value) {
			this.node = node;
			this.value = value;
		}
	}

	// private static class WrapperDouble {
	// private int node;
	//
	// private double value;
	//
	// private WrapperDouble(int node, double value) {
	// this.node = node;
	// this.value = value;
	// }
	// }

	private static class ValueDescInt implements Comparator<WrapperInt> {
		public int compare(WrapperInt a, WrapperInt b) {
			if (a.value == b.value) {
				return 0;
			} else if (a.value > b.value) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	// private static class ValueDescDouble implements Comparator<WrapperDouble>
	// {
	// public int compare(WrapperDouble a, WrapperDouble b) {
	// if (a.value == b.value) {
	// return 0;
	// } else if (a.value > b.value) {
	// return -1;
	// } else {
	// return 1;
	// }
	// }
	// }
}

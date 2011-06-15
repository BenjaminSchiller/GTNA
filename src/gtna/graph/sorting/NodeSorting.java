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

import gtna.graph.NodeImpl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class NodeSorting {
	public static NodeImpl[] degreeDesc(NodeImpl[] nodes, Random rand) {
		NodeImpl[] sorted = nodes.clone();
		Arrays.sort(sorted, new DegreeDesc());
		int value = sorted[0].in().length + sorted[0].out().length;
		int from = 0;
		for (int i = 1; i < sorted.length; i++) {
			if (sorted[i].in().length + sorted[i].out().length != value) {
				randomize(sorted, rand, from, i - 1);
				value = sorted[i].in().length + sorted[i].out().length;
				from = i;
			}
		}
		randomize(sorted, rand, from, sorted.length - 1);
		return sorted;
	}

	private static class DegreeDesc implements Comparator<NodeImpl> {
		public int compare(NodeImpl n1, NodeImpl n2) {
			int v1 = n1.in().length + n1.out().length;
			int v2 = n2.in().length + n2.out().length;
			if (v1 == v2) {
				return 0;
			} else if (v1 > v2) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	public static NodeImpl[] inDegreeDesc(NodeImpl[] nodes, Random rand) {
		NodeImpl[] sorted = nodes.clone();
		Arrays.sort(sorted, new InDegreeDesc());
		int value = sorted[0].in().length;
		int from = 0;
		for (int i = 1; i < sorted.length; i++) {
			if (sorted[i].in().length != value) {
				randomize(sorted, rand, from, i - 1);
				value = sorted[i].in().length;
				from = i;
			}
		}
		randomize(sorted, rand, from, sorted.length - 1);
		return sorted;
	}

	private static class InDegreeDesc implements Comparator<NodeImpl> {
		public int compare(NodeImpl n1, NodeImpl n2) {
			int v1 = n1.in().length;
			int v2 = n2.in().length;
			if (v1 == v2) {
				return 0;
			} else if (v1 > v2) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	public static NodeImpl[] outDegreeDesc(NodeImpl[] nodes, Random rand) {
		NodeImpl[] sorted = nodes.clone();
		Arrays.sort(sorted, new OutDegreeDesc());
		int value = sorted[0].out().length;
		int from = 0;
		for (int i = 1; i < sorted.length; i++) {
			if (sorted[i].out().length != value) {
				randomize(sorted, rand, from, i - 1);
				value = sorted[i].out().length;
				from = i;
			}
		}
		randomize(sorted, rand, from, sorted.length - 1);
		return sorted;
	}

	private static class OutDegreeDesc implements Comparator<NodeImpl> {
		public int compare(NodeImpl n1, NodeImpl n2) {
			int v1 = n1.out().length;
			int v2 = n2.out().length;
			if (v1 == v2) {
				return 0;
			} else if (v1 > v2) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	public static NodeImpl[] random(NodeImpl[] nodes, Random rand) {
		NodeImpl[] sorted = nodes.clone();
		randomize(sorted, rand, 0,
				sorted.length - 1);
		return sorted;
	}

	private static void randomize(NodeImpl[] nodes, Random rand, int from, int to) {
		for (int i = to - from; i > 1; i--) {
			int index = rand.nextInt(i) + from;
			NodeImpl temp = nodes[index];
			nodes[index] = nodes[i + from];
			nodes[i + from] = temp;
		}
	}

	public static int[] byDegreeDesc(NodeImpl[] nodes, Random rand) {
		int[] degree = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			degree[i] = nodes[i].in().length + nodes[i].out().length;
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

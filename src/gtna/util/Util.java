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
 * Util.java
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
package gtna.util;

import gtna.data.Series;
import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class Util {

	// ///////////////////////
	// toFolderString
	// ///////////////////////
	public static String toFolderString(int[] values) {
		StringBuffer buff = new StringBuffer("" + values[0]);
		for (int i = 1; i < values.length; i++) {
			buff.append("-" + values[i]);
		}
		return buff.toString();
	}

	public static String toFolderString(int[][] values) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				if (i == 0 && j == 0) {
					buff.append(values[i][j]);
				} else if (j == 0) {
					buff.append("--" + values[i][j]);
				} else {
					buff.append("-" + values[i][j]);
				}
			}
		}
		return buff.toString();
	}

	public static String toFolderStringDouble(double[][] values) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				if (i == 0 && j == 0) {
					buff.append(values[i][j]);
				} else if (j == 0) {
					buff.append("--" + values[i][j]);
				} else {
					buff.append("-" + values[i][j]);
				}
			}
		}
		return buff.toString();
	}

	public static String toFolderString(double[] values) {
		StringBuffer buff = new StringBuffer("" + values[0]);
		for (int i = 1; i < values.length; i++) {
			buff.append("-" + values[i]);
		}
		return buff.toString();
	}

	public static String toFolderString(double[][] values) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				if (i == 0 && j == 0) {
					buff.append(values[i][j]);
				} else if (j == 0) {
					buff.append("--" + values[i][j]);
				} else {
					buff.append("-" + values[i][j]);
				}
			}
		}
		return buff.toString();
	}

	// ///////////////////////
	// randomize int array
	// ///////////////////////
	public static int[] random(int[] values, Random rand) {
		int[] sorted = values.clone();
		randomize(sorted, rand, 0, sorted.length - 1);
		return sorted;
	}

	private static void randomize(int[] values, Random rand, int from, int to) {
		for (int i = to - from; i > 1; i--) {
			int index = rand.nextInt(i) + from;
			int temp = values[index];
			values[index] = values[i + from];
			values[i + from] = temp;
		}
	}

	public static void randomize(Object[] values, Random rand) {
		for (int i = values.length - 1; i > 1; i--) {
			int index = rand.nextInt(i);
			Object temp = values[index];
			values[index] = values[i];
			values[i] = temp;
		}
	}

	// ///////////////////////
	// init
	// ///////////////////////

	public static int[] initIntArray(int length, int value) {
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = value;
		}
		return array;
	}

	// ///////////////////////
	// array from index
	// ///////////////////////
	public static double[] arrayFromIndex(double[][] values, int index) {
		double[] array = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			array[i] = values[i][index];
		}
		return array;
	}

	// ///////////////////////
	// combine
	// ///////////////////////
	public static RoutingAlgorithm[] combine(RoutingAlgorithm[] ra1,
			RoutingAlgorithm[] ra2) {
		RoutingAlgorithm[] ra = new RoutingAlgorithm[ra1.length + ra2.length];
		for (int i = 0; i < ra1.length; i++) {
			ra[i] = ra1[i];
		}
		for (int i = 0; i < ra2.length; i++) {
			ra[i + ra1.length] = ra2[i];
		}
		return ra;
	}

	public static Network[] combine(Network[] nw1, Network[] nw2) {
		return combine(new Network[][] { nw1, nw2 });
	}

	public static Network[] combine(Network[][] nws) {
		int counter = 0;
		for (int i = 0; i < nws.length; i++) {
			counter += nws[i].length;
		}
		Network[] nw = new Network[counter];
		int index = 0;
		for (int i = 0; i < nws.length; i++) {
			for (int j = 0; j < nws[i].length; j++) {
				nw[index++] = nws[i][j];
			}
		}
		return nw;
	}

	public static Series[] combine(Series[] s1, Series[] s2) {
		return combine(new Series[][] { s1, s2 });
	}

	public static Series[] combine(Series[][] s) {
		int counter = 0;
		for (int i = 0; i < s.length; i++) {
			counter += s[i].length;
		}
		Series[] c = new Series[counter];
		int index = 0;
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length; j++) {
				c[index++] = s[i][j];
			}
		}
		return c;
	}

	public static Series[][] combine(Series[][] s1, Series[][] s2) {
		Series[][] s = new Series[s1.length + s2.length][];
		for (int i = 0; i < s1.length; i++) {
			s[i] = s1[i];
		}
		for (int j = 0; j < s2.length; j++) {
			s[j + s1.length] = s2[j];
		}
		return s;
	}

	public static Series[][] combine(Series[][][] s1) {
		int counter = 0;
		for (int i = 0; i < s1.length; i++) {
			counter += s1[i].length;
		}
		Series[][] s = new Series[counter][];
		int index = 0;
		for (int i = 0; i < s1.length; i++) {
			for (int j = 0; j < s1[i].length; j++) {
				s[index++] = s1[i][j];
			}
		}
		return s;
	}

	// ///////////////////////
	// contains
	// ///////////////////////

	public static boolean contains(String[] keys, String key) {
		for (int i = 0; i < keys.length; i++) {
			if (key.equals(keys[i])) {
				return true;
			}
		}
		return false;
	}

	// ///////////////////////
	// Casting
	// ///////////////////////

	public static double[] toDouble(int[] values) {
		double[] array = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			array[i] = (double) values[i];
		}
		return array;
	}

	// ///////////////////////
	// ArrayList to Array
	// ///////////////////////
	public static Node[] toNodeArray(ArrayList<Node> list) {
		Node[] array = new Node[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static Node[] toNodeArray(HashSet<Node> set) {
		Iterator<Node> iter = set.iterator();
		Node[] array = new Node[set.size()];
		int i = 0;
		while (iter.hasNext()) {
			array[i++] = iter.next();
		}
		return array;
	}

	public static String[] toStringArray(Vector<String> v) {
		String[] array = new String[v.size()];
		Iterator<String> iter = v.listIterator();
		int index = 0;
		while (iter.hasNext()) {
			array[index++] = iter.next();
		}
		return array;
	}

	public static String[] toStringArray(ArrayList<String> list) {
		String[] array = new String[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static double[] toDoubleArray(ArrayList<Double> list) {
		double[] array = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static int[] toIntegerArray(ArrayList<Integer> list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static long[] toLongArray(ArrayList<Long> list) {
		long[] array = new long[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static Edge[] toEdgeArray(ArrayList<Edge> list) {
		Edge[] array = new Edge[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static Node[] toNodeImplArray(ArrayList<Node> list) {
		Node[] array = new Node[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static Single[] toValueArray(ArrayList<Single> list) {
		Single[] array = new Single[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	// ///////////////////////
	// Round
	// ///////////////////////

	public static double round(double value, int digits) {
		double temp = value * Math.pow(10, digits);
		temp = Math.round(temp);
		return (double) temp / (double) Math.pow(10, digits);
	}

	// ///////////////////////
	// Distribution
	// ///////////////////////

	public static int[] count(int[] values) {
		int[] counter = new int[max(values) + 1];
		for (int i = 0; i < values.length; i++) {
			counter[values[i]]++;
		}
		return counter;
	}

	public static double[] distribution(int[] values) {
		return distribution(values, sum(values));
	}

	public static double[] distribution(int[] values, int sum) {
		double[] distribution = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			distribution[i] = (double) values[i] / (double) sum;
		}
		return distribution;
	}

	public static double[] cumulative(double[] distribution) {
		double[] cumulative = new double[distribution.length];
		cumulative[0] = distribution[0];
		for (int i = 1; i < distribution.length; i++) {
			cumulative[i] = cumulative[i - 1] + distribution[i];
		}
		return cumulative;
	}

	// ///////////////////////
	// Sum
	// ///////////////////////

	public static int sum(int[] values) {
		int sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum += values[i];
		}
		return sum;
	}

	public static long sum(long[] values) {
		long sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum += values[i];
		}
		return sum;
	}

	public static double sum(double[] values) {
		double sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum += values[i];
		}
		return sum;
	}

	// ///////////////////////
	// Avg
	// ///////////////////////

	public static double[] avg(int[][] values) {
		int max = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i].length > max) {
				max = values[i].length;
			}
		}
		double[] avg = new double[max];
		for (int i = 0; i < avg.length; i++) {
			for (int j = 0; j < values.length; j++) {
				if (values[j].length > i) {
					avg[i] += values[j][i];
				}
			}
			avg[i] /= (double) values.length;
		}
		return avg;
	}

	public static double[] avg(double[][] values) {
		int max = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i].length > max) {
				max = values[i].length;
			}
		}
		double[] avg = new double[max];
		for (int i = 0; i < avg.length; i++) {
			for (int j = 0; j < values.length; j++) {
				if (values[j].length > i) {
					avg[i] += values[j][i];
				}
			}
			avg[i] /= (double) values.length;
		}
		return avg;
	}

	public static double avg(int[] values) {
		int sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum += values[i];
		}
		return (double) sum / (double) values.length;
	}

	public static double avg(double[] values) {
		double sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum += values[i];
		}
		return sum / (double) values.length;
	}

	public static double[] avgArray(double[] values, int maxLength) {
		if (maxLength >= values.length) {
			return values;
		}
		double[] array = new double[maxLength];
		int index = 0;
		int counter = 0;
		for (int i = 0; i < values.length; i++) {
			int newIndex = (int) Math.floor((double) i * (double) array.length
					/ (double) values.length);
			if (newIndex != index) {
				array[index] /= counter;
				counter = 0;
				index = newIndex;
			}
			array[index] += values[i];
			counter++;
		}
		array[index] /= counter;
		return array;
	}

	public static double[] add(double[] v1, double[] v2) {
		if (v1.length != v2.length) {
			return null;
		}
		double[] v = new double[v1.length];
		for (int i = 0; i < v.length; i++) {
			v[i] = v1[i] + v2[i];
		}
		return v;
	}

	public static void divide(double[] v, double div) {
		for (int i = 0; i < v.length; i++) {
			v[i] /= div;
		}
	}

	// ///////////////////////
	// Min / Max DOUBLE
	// ///////////////////////

	public static int min(int[] values) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (min > values[i]) {
				min = values[i];
			}
		}
		return min;
	}

	public static double min(double[] values) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (min > values[i]) {
				min = values[i];
			}
		}
		return min;
	}

	// ///////////////////////
	// Min / Max INT
	// ///////////////////////

	public static int max(int[] values) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (max < values[i]) {
				max = values[i];
			}
		}
		return max;
	}

	public static double max(double[] values) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (max < values[i]) {
				max = values[i];
			}
		}
		return max;
	}

	public static double max(double[][] values, int index) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (max < values[i][index]) {
				max = values[i][index];
			}
		}
		return max;
	}

	// ///////////////////////
	// FLIP
	// ///////////////////////

	public static String[][] flip(String[][] data) {
		String[][] flipped = new String[data[0].length][data.length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				flipped[j][i] = data[i][j];
			}
		}
		return flipped;
	}

	// ///////////////////////
	// TRANSFORM
	// ///////////////////////

	public static int toInt(byte[] b) {
		int i = 0;
		for (int j = 0; j < 4; j++) {
			i <<= 8;
			i ^= (int) b[j] & 0xFF;
		}
		return i;
	}

	/**
	 * Merges the two given String arrays. The elements of the second array are
	 * added after the elements of the first array, the order inside the arrays
	 * is preserved. If the first array is null, the second array is returned,
	 * if the second array is null, the first array is returned, if both arrays
	 * are null, null is returned.
	 * 
	 * @param arr1
	 *            The first array that is to be merged.
	 * @param arr2
	 *            The second array that is to be merged.
	 * @return A new String array of length arr1.length+arr2.length containing
	 *         the elements of arr1 and arr2.
	 * @author Philipp Neubrand
	 */
	public static Parameter[] mergeArrays(Parameter[] arr1, Parameter[] arr2) {
		if (arr2 == null)
			return arr1;
		if (arr1 == null)
			return arr2;

		Parameter[] ret = new Parameter[arr1.length + arr2.length];
		for (int i = 0; i < arr1.length; i++)
			ret[i] = arr1[i];
		for (int i = 0; i < arr2.length; i++)
			ret[arr1.length + i] = arr2[i];

		return ret;
	}
	

	/**
	 * Prefixes all values in the array with the given prefix.
	 * 
	 * @param prefix
	 *            The prefix to be attached to all values in the array.
	 * @param parameters
	 *            The array to which the prefix is to be attached.
	 * @return The same String array with prefix attached to all the values.
	 * @author Philipp Neubrand
	 */
	public static Parameter[] addPrefix(String prefix, Parameter[] parameters) {
		for (int i = 0; i < parameters.length; i++) {
			parameters[i].setKey(prefix+parameters[i].getKey());
		}

		return parameters;
	}

	/**
	 * Maps labels to communities. Labels are mapped in ascending order,
	 * communities are indexed from 0 to N, where N is the number of
	 * communities.
	 * 
	 * @param labels
	 *            array of labels
	 * @return mapping of labels to communities
	 */
	public static HashMap<Integer, Integer> mapLabelsToCommunities(int[] labels) {
		SortedSet<Integer> labelSet = new TreeSet<Integer>();
		for (int label : labels) {
			labelSet.add(label);
		}
		HashMap<Integer, Integer> labelCommunityMapping = new HashMap<Integer, Integer>();
		int communityIndex = 0;
		for (int label : labelSet) {
			labelCommunityMapping.put(label, communityIndex++);
		}
		return labelCommunityMapping;
	}

	// ///////////////////////
	// REMOVE
	// ///////////////////////

	// public static Node[] removeHighestDegree(Node[] nodes) {
	// int largestIndex = 0;
	// int largestDegree = nodes[0].in().length + nodes[0].out().length;
	// for (int i = 1; i < nodes.length; i++) {
	// int degree = nodes[i].in().length + nodes[i].out().length;
	// if (degree > largestDegree) {
	// largestDegree = degree;
	// largestIndex = i;
	// }
	// }
	// Node[] removed = new Node[nodes.length - 1];
	// for (int i = 0; i < nodes.length; i++) {
	// if (i < largestIndex) {
	// removed[i] = nodes[i];
	// } else if (i > largestIndex) {
	// removed[i - 1] = nodes[i];
	// }
	// }
	// Node largest = nodes[largestIndex];
	// for (int i = 0; i < removed.length; i++) {
	// if (removed[i].hasIn(largest)) {
	// removed[i].removeIn(largest);
	// }
	// if (removed[i].hasOut(largest)) {
	// removed[i].removeOut(largest);
	// }
	// }
	// for (int i = 0; i < removed.length; i++) {
	// removed[i].setIndex(i);
	// }
	// return removed;
	// }
	//
	// public static Node[] removeIsolatedClusters(Node[] users) {
	// ArrayList<ArrayList<Node>> clusters = new ArrayList<ArrayList<Node>>();
	// boolean[] visited = new boolean[users.length];
	// for (int i = 0; i < users.length; i++) {
	// if (visited[users[i].index()]) {
	// continue;
	// }
	// clusters.add(new ArrayList<Node>());
	//
	// Queue<Node> queueFW = new LinkedList<Node>();
	// boolean[] visitedFW = new boolean[users.length];
	// visitedFW[users[i].index()] = true;
	// queueFW.add(users[i]);
	// while (!queueFW.isEmpty()) {
	// Node current = queueFW.poll();
	// Node[] out = current.out();
	// for (int j = 0; j < out.length; j++) {
	// if (!visitedFW[out[j].index()]) {
	// queueFW.add((Node) out[j]);
	// visitedFW[out[j].index()] = true;
	// }
	// }
	// }
	//
	// Queue<Node> queueBW = new LinkedList<Node>();
	// boolean[] visitedBW = new boolean[users.length];
	// visitedBW[users[i].index()] = true;
	// queueBW.add(users[i]);
	// while (!queueBW.isEmpty()) {
	// Node current = queueBW.poll();
	// Node[] in = current.in();
	// for (int j = 0; j < in.length; j++) {
	// if (!visitedBW[in[j].index()]) {
	// queueBW.add((Node) in[j]);
	// visitedBW[in[j].index()] = true;
	// }
	// }
	// }
	//
	// ArrayList<Node> currentCluster = clusters
	// .get(clusters.size() - 1);
	// for (int j = 0; j < users.length; j++) {
	// if (visitedFW[users[j].index()] && visitedBW[users[j].index()]) {
	// currentCluster.add(users[j]);
	// visited[users[j].index()] = true;
	// }
	// }
	// }
	// int maxClusterIndex = 0;
	// for (int i = 1; i < clusters.size(); i++) {
	// if (clusters.get(i).size() > clusters.get(maxClusterIndex).size()) {
	// maxClusterIndex = i;
	// }
	// }
	// ArrayList<Node> remove = new ArrayList<Node>();
	// for (int i = 0; i < clusters.size(); i++) {
	// if (i != maxClusterIndex) {
	// remove.addAll(clusters.get(i));
	// }
	// }
	// return remove(users, remove);
	// }
	//
	// private static Node[] remove(Node[] nodes,
	// ArrayList<Node> remove) {
	// ArrayList<Node> removed = new ArrayList<Node>(nodes.length
	// - remove.size());
	// for (int i = 0; i < nodes.length; i++) {
	// if (!remove.contains(nodes[i])) {
	// removed.add(nodes[i]);
	// }
	// }
	// for (int i = 0; i < removed.size(); i++) {
	// for (int j = 0; j < remove.size(); j++) {
	// if (removed.get(i).hasIn(remove.get(j))) {
	// removed.get(i).removeIn(remove.get(j));
	// }
	// if (removed.get(i).hasOut(remove.get(j))) {
	// removed.get(i).removeOut(remove.get(j));
	// }
	// }
	// }
	// for (int i = 0; i < removed.size(); i++) {
	// removed.get(i).setIndex(i);
	// }
	// return Util.toNodeImplArray(removed);
	// }
}

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
 * ArraysUtils.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.util;

import gtna.metrics.Metric;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class ArrayUtils {
	public static void reverse(int[] array) {
		for (int i = 0; i < array.length / 2; i++) {
			int temp = array[i];
			array[i] = array[array.length - i - 1];
			array[array.length - i - 1] = temp;
		}
	}

	public static void reverse(double[] array) {
		for (int i = 0; i < array.length / 2; i++) {
			double temp = array[i];
			array[i] = array[array.length - i - 1];
			array[array.length - i - 1] = temp;
		}
	}

	public static void divide(double[] array, double divisor) {
		for (int i = 0; i < array.length; i++) {
			array[i] /= divisor;
		}
	}

	public static void divide(double[][] array, int index, double divisor) {
		for (int i = 0; i < array.length; i++) {
			array[i][index] /= divisor;
		}
	}

	public static String[] toStringArray(ArrayList<String> list) {
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
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

	public static int[] toIntArray(Integer[] arrayI) {
		int[] array = new int[arrayI.length];
		for (int i = 0; i < arrayI.length; i++) {
			array[i] = arrayI[i];
		}
		return array;
	}

	public static Metric[] toMetricArray(ArrayList<Metric> list) {
		Metric[] array = new Metric[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static void shuffle(int[] array, Random rand) {
		for (int i = 0; i < array.length; i++) {
			int index = rand.nextInt(array.length - i) + i;
			int temp = array[index];
			array[index] = array[i];
			array[i] = temp;
		}
	}

	public static int[] initIntArray(int length) {
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		return array;
	}

	public static int[] initIntArray(int length, int value) {
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = value;
		}
		return array;
	}

	public static Integer[] initIntegerArray(int length) {
		Integer[] array = new Integer[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		return array;
	}

	public static double[] initDoubleArray(int length, double value) {
		double[] array = new double[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = value;
		}
		return array;
	}

	public static double[][] initDoubleArray(int yLength, int xLength,
			double value) {
		double[][] array = new double[yLength][xLength];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				array[i][j] = value;
			}
		}
		return array;
	}

	public static int getMaxInt(int[] array) {
		int max = Integer.MIN_VALUE;
		for (int v : array) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}
}

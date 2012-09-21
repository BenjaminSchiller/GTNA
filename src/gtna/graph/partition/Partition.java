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
 * Partition.java
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
package gtna.graph.partition;

import gtna.graph.GraphProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author benni
 * 
 */
public class Partition extends GraphProperty {

	private int[][] components;

	public Partition() {
		this.components = new int[0][0];
	}

	public Partition(int[][] components) {
		this.components = components;
		this.sort();
	}

	public Partition(ArrayList<ArrayList<Integer>> list) {
		this.components = new int[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			this.components[i] = new int[list.get(i).size()];
			for (int j = 0; j < list.get(i).size(); j++) {
				this.components[i][j] = list.get(i).get(j);
			}
		}
		this.sort();
	}

	private void sort() {
		for (int[] p : this.components) {
			Arrays.sort(p);
		}
		Arrays.sort(this.components, new SizeDesc());
	}

	private class SizeDesc implements Comparator<int[]> {
		@Override
		public int compare(int[] arg0, int[] arg1) {
			return arg1.length - arg0.length;
		}
	}

	/**
	 * @return the components
	 */
	public int[][] getComponents() {
		return this.components;
	}

	public int[] getLargestComponent() {
		return this.components[0];
	}

	@Override
	public boolean write(String filename, String key) {
		// TODO implement GraphProperty.write(...)
		return false;
	}

	@Override
	public String read(String filename) {
		// TODO implement GraphProperty.read(...)
		return null;
	}

}

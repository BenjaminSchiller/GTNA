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
 * FiedlerNodeSorter2.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.graph.sorting;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.graph.sorting.FiedlerNodeSorter.Selection;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.metrics.fragmentation.LaplaceSpectrum;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * @author stef order for node removal in fragmentation based on the second
 *         eigenvector of the Laplacian matric, allows diverse treatments of
 *         nodes with identical values
 */
public class FiedlerNodeSorter2 extends NodeSorter {

	private double[] secondEigenvector;
	private Selection select;
	private Difference diff;
	private int k; // number of skipped neighbours in vector

	public enum Selection {
		SUM, // sum of distance to predeccessor and successor in sorted vector
		MIN, // minimal distance to predeccessor/successor in sorted vector
		MAX, // maximal distance to predeccessor/successor in sorted vector
		NEXT, // distance to succesor
		PREV, // distance to predecessor
	}

	public enum Difference {
		TOTAL, AVERAGE
	}

	public FiedlerNodeSorter2(Selection selection, Difference diff,
			NodeSorterMode mode, int k) {
		super("FIEDLER2", mode);
		this.select = selection;
		this.diff = diff;
		this.k = k;
	}

	@Override
	public String getKey() {
		return super.getKey() + "_" + this.select.toString() + "_"
				+ this.diff.toString() + "_" + this.k;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.graph.sorting.NodeSorter#sort(gtna.graph.Graph,
	 * java.util.Random)
	 */
	@Override
	public Node[] sort(Graph g, Random rand) {
		// retrieve property
		GraphProperty pro = g.getProperty("FIEDLER_VECTOR_0");
		this.secondEigenvector = ((FiedlerVector) pro).getVector();
		// sort nodes according to fiedler vector entry
		Node[] sorted = this.clone(g.getNodes());
		Arrays.sort(sorted, new FiedlerAsc(this.secondEigenvector));
		// compute differences
		double[] dist = new double[sorted.length];
		if (this.select == Selection.SUM) {
			for (int j = 0; j < sorted.length; j++) {
				int pre = this.findNextLowest(j, sorted);
				int next = this.findNextHighest(j, sorted);
				dist[sorted[j].getIndex()] = this.secondEigenvector[sorted[Math
						.min(next, sorted.length - 1)].getIndex()]
						- this.secondEigenvector[sorted[Math.max(pre, 0)]
								.getIndex()];
				if (this.diff == Difference.AVERAGE) {
					dist[sorted[j].getIndex()] = dist[sorted[j].getIndex()]
							/ (double) this.numberEqual(j, sorted);
				}
			}
		}

		if (this.select == Selection.MIN) {
			dist[sorted[0].getIndex()] = this.secondEigenvector[sorted[1]
					.getIndex()] - this.secondEigenvector[sorted[0].getIndex()];
			if (this.diff == Difference.AVERAGE) {
				dist[sorted[0].getIndex()] = dist[sorted[0].getIndex()]
						/ (double) this.numberEqual(0, sorted);
			}
			for (int j = 1; j < sorted.length - 1; j++) {
				int pre = this.findNextLowest(j, sorted);
				int next = this.findNextHighest(j, sorted);
				dist[sorted[j].getIndex()] = this.secondEigenvector[sorted[Math
						.min(next, sorted.length - 1)].getIndex()]
						- this.secondEigenvector[sorted[Math.max(pre, 0)]
								.getIndex()];
				if (this.diff == Difference.AVERAGE) {
					dist[sorted[j].getIndex()] = dist[sorted[j].getIndex()]
							/ (double) this.numberEqual(j, sorted);
				}
			}
			dist[sorted[dist.length - 1].getIndex()] = this.secondEigenvector[sorted[sorted.length - 1]
					.getIndex()]
					- this.secondEigenvector[sorted[sorted.length - 2]
							.getIndex()];
			if (this.diff == Difference.AVERAGE) {
				dist[sorted[dist.length - 1].getIndex()] = dist[sorted[dist.length - 1]
						.getIndex()]
						/ (double) this.numberEqual(dist.length - 1, sorted);
			}
		}
		if (this.select == Selection.MAX) {
			dist[sorted[0].getIndex()] = this.secondEigenvector[sorted[1]
					.getIndex()] - this.secondEigenvector[sorted[0].getIndex()];
			if (this.diff == Difference.AVERAGE) {
				dist[sorted[0].getIndex()] = dist[sorted[0].getIndex()]
						/ (double) this.numberEqual(0, sorted);
			}
			for (int j = 1; j < sorted.length - 1; j++) {
				int pre = this.findNextLowest(j, sorted);
				int next = this.findNextHighest(j, sorted);
				dist[sorted[j].getIndex()] = this.secondEigenvector[sorted[Math
						.min(next, sorted.length - 1)].getIndex()]
						- this.secondEigenvector[sorted[Math.max(pre, 0)]
								.getIndex()];
				if (this.diff == Difference.AVERAGE) {
					dist[sorted[j].getIndex()] = dist[sorted[j].getIndex()]
							/ (double) this.numberEqual(j, sorted);
				}
			}
			dist[sorted[dist.length - 1].getIndex()] = this.secondEigenvector[sorted[sorted.length - 1]
					.getIndex()]
					- this.secondEigenvector[sorted[sorted.length - 2]
							.getIndex()];
			if (this.diff == Difference.AVERAGE) {
				dist[sorted[dist.length - 1].getIndex()] = dist[sorted[dist.length - 1]
						.getIndex()]
						/ (double) this.numberEqual(dist.length - 1, sorted);
			}
		}

		if (this.select == Selection.NEXT) {
			for (int j = 0; j <= sorted.length - 1; j++) {
				int pre = this.findNextLowest(j, sorted);
				int next = this.findNextHighest(j, sorted);
				dist[sorted[j].getIndex()] = this.secondEigenvector[sorted[Math
						.min(next, sorted.length - 1)].getIndex()]
						- this.secondEigenvector[sorted[Math.max(pre, 0)]
								.getIndex()];
				if (this.diff == Difference.AVERAGE) {
					dist[sorted[j].getIndex()] = dist[sorted[j].getIndex()]
							/ (double) this.numberEqual(j, sorted);
				}
			}
			dist[sorted[dist.length - 1].getIndex()] = this.secondEigenvector[sorted[sorted.length - 1]
					.getIndex()]
					- this.secondEigenvector[sorted[sorted.length - 2]
							.getIndex()];
			if (this.diff == Difference.AVERAGE) {
				dist[sorted[dist.length - 1].getIndex()] = dist[sorted[dist.length - 1]
						.getIndex()]
						/ (double) this.numberEqual(dist.length - 1, sorted);
			}
		}

		if (this.select == Selection.PREV) {
			dist[sorted[0].getIndex()] = this.secondEigenvector[sorted[1]
					.getIndex()] - this.secondEigenvector[sorted[0].getIndex()];
			if (this.diff == Difference.AVERAGE) {
				dist[sorted[0].getIndex()] = dist[sorted[0].getIndex()]
						/ (double) this.numberEqual(0, sorted);
			}
			for (int j = 1; j < sorted.length; j++) {
				int pre = this.findNextLowest(j, sorted);
				int next = this.findNextHighest(j, sorted);
				dist[sorted[j].getIndex()] = this.secondEigenvector[sorted[Math
						.min(next, sorted.length - 1)].getIndex()]
						- this.secondEigenvector[sorted[Math.max(pre, 0)]
								.getIndex()];
				if (this.diff == Difference.AVERAGE) {
					dist[sorted[j].getIndex()] = dist[sorted[j].getIndex()]
							/ (double) this.numberEqual(j, sorted);
				}
			}
		}
		// sort according to differences
		Node[] sortedFinal = this.clone(g.getNodes());
		Arrays.sort(sortedFinal, new FiedlerAsc(dist));
		this.randomize(sortedFinal, rand);
		sortedFinal = this.reverse(sortedFinal);
		return sortedFinal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.graph.sorting.NodeSorter#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		GraphProperty pro = g.getProperty("FIEDLER_VECTOR_0");
		if (pro == null) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.graph.sorting.NodeSorter#isPropertyEqual(gtna.graph.Node,
	 * gtna.graph.Node)
	 */
	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		// TODO Auto-generated method stub
		return this.secondEigenvector[n1.getIndex()] == this.secondEigenvector[n2
				.getIndex()];
	}

	private class FiedlerAsc implements Comparator<Node> {
		double[] vec;

		public FiedlerAsc(double[] vec) {
			this.vec = vec;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Node arg0, Node arg1) {
			return Double.compare(this.vec[arg0.getIndex()],
					this.vec[arg1.getIndex()]);
		}

	}

	private int findNextLowest(int index, Node[] sorted) {
		int i = index - 1;
		while (i > -1 && this.isPropertyEqual(sorted[index], sorted[i])) {
			i--;
		}
		return i;
	}

	private int findNextHighest(int index, Node[] sorted) {
		int i = index + 1;
		while (i < sorted.length
				&& this.isPropertyEqual(sorted[index], sorted[i])) {
			i++;
		}
		return i;
	}

	private int numberEqual(int index, Node[] sorted) {
		int n = 1;
		int i = index + 1;
		while (i < sorted.length
				&& this.isPropertyEqual(sorted[index], sorted[i])) {
			n++;
			i++;
		}
		i = index - 1;
		while (i > -1 && this.isPropertyEqual(sorted[index], sorted[i])) {
			i--;
			n++;
		}
		return n;
	}

}

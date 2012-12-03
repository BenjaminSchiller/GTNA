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
 * SpectralSorter.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * @author stefanie
 *
 */
public class SpectralSorter extends NodeSorter{
	private Calculation calc;
	private DegreeOne degree;
	private double[] vec;
	private HashMap<Integer,Integer> mapIndex;

	public enum Calculation{
		SUM, ABSOLUTE
	}
	
	public enum DegreeOne{
		INCLUDE, EXCLUDE, INCLUDE_CAL
	}
	/**
	 * @param key
	 * @param mode
	 */
	public SpectralSorter(NodeSorterMode mode, Calculation calc, DegreeOne deg) {
		super("SPECTRAL", NodeSorterMode.ASC);
		this.calc = calc;
		this.degree = deg;
	}

	@Override
	public String getKey() {
		return super.getKey() + "_" + this.calc.toString() + "_" + this.degree.toString();
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
		vec = ((FiedlerVector) pro).getVector();
		if (this.calc == Calculation.SUM && this.degree == DegreeOne.INCLUDE){
		// sort nodes by their value in fiedler vector
		   Node[] sorted = this.clone(g.getNodes());
		   Arrays.sort(sorted, new FiedlerAsc(this.vec));
		   this.randomize(sorted, rand);
		   // compute distance between successive nodes
		   double[] dist = new double[sorted.length];
			for (int j = 0; j < sorted.length; j++) {
					dist[sorted[j].getIndex()] = this.vec[sorted[Math
							.min(j + 1, sorted.length - 1)].getIndex()]
							- this.vec[sorted[Math.max(j - 1, 0)]
									.getIndex()];
			}
			Node[] sortedFinal = this.clone(g.getNodes());
			Arrays.sort(sortedFinal, new FiedlerAsc(dist));
			this.vec = dist;
			this.randomize(sortedFinal, rand);
			sortedFinal = this.reverse(sortedFinal);
			return sortedFinal;
		}
		if (this.calc == Calculation.ABSOLUTE && this.degree == DegreeOne.INCLUDE){
			// sort nodes by their value in fiedler vector
			   Node[] sorted = this.clone(g.getNodes());
			   for (int i = 0; i < vec.length; i++){
				   vec[i] = Math.abs(vec[i]);
			   }
			   Arrays.sort(sorted, new FiedlerAsc(this.vec));
			   this.randomize(sorted, rand);
				return sorted;
			}
		if (this.calc == Calculation.SUM && this.degree == DegreeOne.INCLUDE_CAL){
			// sort nodes by their value in fiedler vector
			   Node[] sorted = this.clone(g.getNodes());
			   Arrays.sort(sorted, new FiedlerAsc(this.vec));
			   this.randomize(sorted, rand);
			   // compute distance between successive nodes
			   double[] dist = new double[sorted.length];
				for (int j = 0; j < sorted.length; j++) {
						dist[sorted[j].getIndex()] = this.vec[sorted[Math
								.min(j + 1, sorted.length - 1)].getIndex()]
								- this.vec[sorted[Math.max(j - 1, 0)]
										.getIndex()];
				}
				Node[] sortedFinal = this.clone(g.getNodes());
				Arrays.sort(sortedFinal, new FiedlerAsc(dist));
				this.vec = dist;
				this.randomize(sortedFinal, rand);
				sortedFinal = this.reverse(sortedFinal);
				Node[] result = new Node[sorted.length];
				Vector<Integer> degOne = new Vector<Integer>();
				int c = 0;
				for (int i = 0; i < sorted.length; i++){
					if (sortedFinal[i].getInDegree() > 1){
						result[c] = sortedFinal[i];
						c++;
					} else {
						degOne.add(i);
					}
				}
				for (int i = 0; i < degOne.size(); i++){
					result[c] = sortedFinal[degOne.get(i)];
					c++;
				}
				return result;
			}
			if (this.calc == Calculation.ABSOLUTE && this.degree == DegreeOne.INCLUDE_CAL){
				// sort nodes by their value in fiedler vector
				   Node[] sorted = this.clone(g.getNodes());
				   for (int i = 0; i < vec.length; i++){
					   vec[i] = Math.abs(vec[i]);
				   }
				   Arrays.sort(sorted, new FiedlerAsc(this.vec));
				   this.randomize(sorted, rand);
				   Node[] result = new Node[sorted.length];
					Vector<Integer> degOne = new Vector<Integer>();
					int c = 0;
					for (int i = 0; i < sorted.length; i++){
						if (sorted[i].getInDegree() > 1){
							result[c] = sorted[i];
							c++;
						} else {
							degOne.add(i);
						}
					}
					for (int i = 0; i < degOne.size(); i++){
						result[c] = sorted[degOne.get(i)];
						c++;
					}
					return result;
				}
			if (this.calc == Calculation.SUM && this.degree == DegreeOne.EXCLUDE){
				// sort nodes by their value in fiedler vector
				   Node[] sorted = this.clone(g.getNodes());
				   Arrays.sort(sorted, new FiedlerAsc(this.vec));
				   this.randomize(sorted, rand);
				   Node[] nonOne = new Node[sorted.length];
				   int count = 0;
				   for (int i = 0; i < nonOne.length; i++){
					   if (sorted[i].getDegree() <3){
						   count++;
					   }
				   }
				   int c = 0;
				   for (int i = 0; i < nonOne.length; i++){
					   if (sorted[i].getDegree() > 2){
						   nonOne[count] = sorted[i];
						   count++;
					   } else {
						   nonOne[c] = sorted[i];
						   c++; 
					   }
				   }
				   // compute distance between successive nodes
				   double[] dist = new double[sorted.length];
					for (int j = c; j < sorted.length; j++) {
							dist[sorted[j].getIndex()] = this.vec[sorted[Math
									.min(j + 1, sorted.length - 1)].getIndex()]
									- this.vec[sorted[Math.max(j - 1, c)]
											.getIndex()];
					}
					Node[] sortedFinal = this.clone(g.getNodes());
					Arrays.sort(sortedFinal, new FiedlerAsc(dist));
					this.vec = dist;
					this.randomize(sortedFinal, rand);
					sortedFinal = this.reverse(sortedFinal);
					return sortedFinal;
				}
				if (this.calc == Calculation.ABSOLUTE && this.degree == DegreeOne.EXCLUDE){
					// sort nodes by their value in fiedler vector
					   Node[] sorted = this.clone(g.getNodes());
					   for (int i = 0; i < vec.length; i++){
						   if (sorted[i].getDegree() > 2){
						   vec[i] = Math.abs(vec[i]);
						   } else {
							   vec[i] = Double.MAX_VALUE; 
						   }
					   }
					   Arrays.sort(sorted, new FiedlerAsc(this.vec));
					   this.randomize(sorted, rand);
					   
						return sorted;
					}
		return null;
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
		return this.vec[n1.getIndex()] == this.vec[n2
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
}

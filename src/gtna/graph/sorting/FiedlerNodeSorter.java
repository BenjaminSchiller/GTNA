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
 * FiedlerNodeSorter.java
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
import gtna.metrics.fragmentation.LaplaceSpectrum;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * @author stef
 *
 */
public class FiedlerNodeSorter extends NodeSorter {
	private double[] secondEigenvector;
	private Selection select;
   private int k; //number of skipped neighbours in K-neighbours metric
	
	public enum Selection{
		SUM, //sum of distance to predeccessor and successor in sorted vector
		MIN, //minimal distance to predeccessor/successor in sorted vector
		MAX, //maximal distance to predeccessor/successor in sorted vector
		NEXT, // distance to succesor
		PREV, // distance to predecessor
		}

	public FiedlerNodeSorter(Selection selection, NodeSorterMode mode, int k){
		super("FIEDLER", mode);
		this.select = selection;
		this.k = k;
	}
	

	
	
	@Override
	public String getKey() {
		return super.getKey() + "_"+this.select.toString()+ "_"+this.k;
	}
	

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#sort(gtna.graph.Graph, java.util.Random)
	 */
	@Override
	public Node[] sort(Graph g, Random rand) {
//		if (this.file != null){
//		File f = new File(this.file);
//		if (f.exists()){
//			this.secondEigenvector = DataReader.readDouble(this.file);
//		} else {
//			this.generateEigen(g);
//			DataWriter.write(this.secondEigenvector, this.file, true);
//		}
//		} else {
//			this.generateEigen(g);
//		}
		GraphProperty pro = g.getProperty("FIEDLER_VECTOR_0");
		
			this.secondEigenvector = ((FiedlerVector)pro).getVector();
				
		
		Node[] sorted = this.clone(g.getNodes());
		Arrays.sort(sorted, new FiedlerAsc(this.secondEigenvector));
		this.randomize(sorted, rand);
//		HashMap<Double, Vector<Integer>> map = new HashMap<Double,Vector<Integer>>(nodes.length);
//		Vector<Integer> vec; 
//		for (int i = 0; i < this.secondEigenvector.length; i++){
//			vec = map.get(this.secondEigenvector[i]);
//			if (vec == null){
//				vec = new Vector<Integer>();
//				map.put(this.secondEigenvector[i], vec);
//			}
//			vec.add(i);
//		}
//		HashMap<Double, Integer> map = new HashMap<Double,Integer>(nodes.length);
//		Vector<Integer> vec; 
//		for (int i = 0; i < this.secondEigenvector.length; i++){
//			//vec = map.get(this.secondEigenvector[i]);
//			//if (vec == null){
//			//	vec = new Vector<Integer>();
//			    this.secondEigenvector[i] =  this.secondEigenvector[i]+rand.nextDouble()*0.0000001;
//				map.put(this.secondEigenvector[i], i);
//			
//		}
//		double[] sorted = this.secondEigenvector.clone();
//		Arrays.sort(sorted);
//		HashMap<Double, Integer> mapDist = new HashMap<Double,Integer>(sorted.length);
		double[] dist = new double[sorted.length];
		if (this.select == Selection.SUM){
			for (int j = 0; j < sorted.length; j++){
				dist[sorted[j].getIndex()] = this.secondEigenvector[sorted[Math.min(j+k, sorted.length-1)].getIndex()] 
						- this.secondEigenvector[sorted[Math.max(j-k,0)].getIndex()];
						//- sorted [Math.max(j-k,0)];
			}
		}
		
		if (this.select == Selection.MIN){
			dist[sorted[0].getIndex()] = this.secondEigenvector[sorted[1].getIndex()]-this.secondEigenvector[sorted[0].getIndex()];
			for (int j = 1; j < sorted.length-1; j++){
				dist[sorted[j].getIndex()] = Math.min(this.secondEigenvector[sorted[j+1].getIndex()] - this.secondEigenvector[sorted[j].getIndex()],
						this.secondEigenvector[sorted[j].getIndex()] - this.secondEigenvector[sorted[j-1].getIndex()]);
				//dist[j] = Math.min(sorted[j+1] - sorted[j], sorted[j] - sorted[j-1]);
			}
			dist[sorted[dist.length-1].getIndex()] = this.secondEigenvector[sorted[sorted.length-1].getIndex()] 
					- this.secondEigenvector[sorted[sorted.length-2].getIndex()];
		}
		if (this.select == Selection.MAX){
			dist[sorted[0].getIndex()] = this.secondEigenvector[sorted[1].getIndex()]-this.secondEigenvector[sorted[0].getIndex()];
			for (int j = 1; j < sorted.length-1; j++){
				dist[sorted[j].getIndex()] = Math.max(this.secondEigenvector[sorted[j+1].getIndex()] - this.secondEigenvector[sorted[j].getIndex()],
						this.secondEigenvector[sorted[j].getIndex()] - this.secondEigenvector[sorted[j-1].getIndex()]);
				//dist[j] = Math.min(sorted[j+1] - sorted[j], sorted[j] - sorted[j-1]);
			}
			dist[sorted[dist.length -1].getIndex()] = this.secondEigenvector[sorted[sorted.length-1].getIndex()] 
					- this.secondEigenvector[sorted[sorted.length-2].getIndex()];
		}
		
		if (this.select == Selection.NEXT){
			for (int j = 0; j <= sorted.length-1; j++){
				dist[sorted[j].getIndex()] = this.secondEigenvector[sorted[j+1].getIndex()] - this.secondEigenvector[sorted[j].getIndex()];
			}
			dist[sorted[dist.length-1].getIndex()] = this.secondEigenvector[sorted[sorted.length-1].getIndex()] 
					- this.secondEigenvector[sorted[sorted.length-2].getIndex()];
		}
		
		if (this.select == Selection.PREV){
			dist[sorted[0].getIndex()] = this.secondEigenvector[sorted[1].getIndex()]-this.secondEigenvector[sorted[0].getIndex()];
			for (int j = 1; j < sorted.length; j++){
				dist[sorted[j].getIndex()] = this.secondEigenvector[sorted[j].getIndex()] - this.secondEigenvector[sorted[j-1].getIndex()];
			}
		}
		Node[] sortedFinal = this.clone(g.getNodes());
		Arrays.sort(sortedFinal, new FiedlerAsc(dist));
		this.secondEigenvector = dist;
		this.randomize(sortedFinal, rand); 
        sortedFinal = this.reverse(sortedFinal);
        
//		for (int i = 0; i < dist.length; i++){
//			if (mapDist.containsKey(dist[i])){
//				dist[i] = dist[i] + rand.nextDouble()*0.000001;
//			}
//			mapDist.put(dist[i], map.get(sorted[i]));
//			
//		}
//		Arrays.sort(dist);
//		Node[] old = g.getNodes();
//		for (int i = 0; i < dist.length; i++){
//			int nr = mapDist.get(dist[i]);
//			nodes[nodes.length-1-i] = old[mapDist.get(dist[i])];
//			System.out.println(nodes[nodes.length-1-i].getIndex());
//		}
		return sortedFinal;
	}
	
	private void generateEigen(Graph g){
		Matrix L = LaplaceSpectrum.makeLaplacian(g);
		EigenvalueDecomposition eig = new EigenvalueDecomposition(L);
		double[] eigenvals = eig.getRealEigenvalues();
		double[][] eigenvecs = eig.getV().getArray();
		double first,second;
		int index1,index2;
		if (eigenvals[0] < eigenvals[1]){
		  first = eigenvals[0];
		  index1 = 0;
		  second = eigenvals[1];
		  index2 = 1;
		} else {
			first = eigenvals[1];
			index1 = 1;
			  second = eigenvals[0];
			  index2 = 0;
		}
		for (int i = 2; i < eigenvals.length; i++){
			if (eigenvals[i] < first){
				second = first;
				index2 = index1;
				first = eigenvals[i];
				index1 = i;
			} else {
				if (eigenvals[i] < second){
					second = eigenvals[i];
					index2 = i;
				}
			}
		}
		
		this.secondEigenvector = new double[eigenvals.length];
		for (int i = 0; i < this.secondEigenvector.length; i++){
			this.secondEigenvector[i] = eigenvecs[i][index2];
		}
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		GraphProperty pro = g.getProperty("FIEDLER_VECTOR_0");
		if (pro == null){
			return false;
		} else {
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#isPropertyEqual(gtna.graph.Node, gtna.graph.Node)
	 */
	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		// TODO Auto-generated method stub
		return this.secondEigenvector[n1.getIndex()] == this.secondEigenvector[n2.getIndex()];
	}
	
	private class FiedlerAsc implements Comparator<Node> {
		double[] vec;
		
		public FiedlerAsc(double[] vec){
			this.vec = vec;
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Node arg0, Node arg1) {
			return Double.compare(this.vec[arg0.getIndex()], this.vec[arg1.getIndex()]);
		}
		
	}
	
	

	

}

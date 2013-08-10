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
 * StoreFiedler.java
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
package gtna.transformation.eigenvector;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.sorting.FiedlerVector;
import gtna.metrics.fragmentation.LaplaceSpectrum;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.Parameter;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * @author stef computes the Fiedler vector (second eigenvector of the
 *         Laplacian) and stores it as a graph property
 */
public class StoreFiedler extends Transformation {

	public boolean simple;
	private double[] secondEigenvector;

	public StoreFiedler() {
		super("STOREFIEDLER");
		simple = true;
	}
	
	public StoreFiedler(boolean simple) {
		super("STOREFIEDLER", new Parameter[]{new BooleanParameter("SIMPLE",simple)});
		this.simple = simple;
	}

	@Override
	public Graph transform(Graph g) {
		// compute spectrum
		
		Matrix L = LaplaceSpectrum.makeLaplacian(g);
		EigenvalueDecomposition eig = new EigenvalueDecomposition(L);
		double[] eigenvals = eig.getRealEigenvalues();
		double[][] eigenvecs = eig.getV().getArray();
		this.secondEigenvector = new double[eigenvals.length];
		if (simple){
		for (int i = 0; i < this.secondEigenvector.length; i++) {
			this.secondEigenvector[i] = eigenvecs[i][1];
		}
		} else {
			int index = 1;
			while (Math.round(eigenvals[index]) == 0){
				index++;
			}
			int c = (int)Math.round(eigenvals[index]);
			bwhile (Math.round(eigenvals[index]) == c){
				for (int i = 0; i < this.secondEigenvector.length; i++) {
					this.secondEigenvector[i] = this.secondEigenvector[i]+eigenvecs[i][index];
				}
				index++;
			}
			
		}
		GraphProperty vec = new FiedlerVector(this.secondEigenvector);
		g.addProperty(g.getNextKey("FIEDLER_VECTOR"), vec);
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

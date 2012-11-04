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
 * StoreSpectrum.java
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
package gtna.transformation.eigenvector;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.sorting.FiedlerVector;
import gtna.metrics.fragmentation.LaplaceSpectrum;
import gtna.transformation.Transformation;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * @author stefanie
 *
 */
public class StoreSpectrum extends Transformation {

	private double[] eigenvals;
	private double[][] eigenvecs;

	public StoreSpectrum() {
		super("STORESPECTRUM");
	}

	@Override
	public Graph transform(Graph g) {
		// compute spectrum
		Matrix L = LaplaceSpectrum.makeLaplacian(g);
		EigenvalueDecomposition eig = new EigenvalueDecomposition(L);
		eigenvals = eig.getRealEigenvalues();
		eigenvecs = eig.getV().getArray();
		
		GraphProperty vec = new GraphSpectrum(this.eigenvals,this.eigenvecs);
		g.addProperty(g.getNextKey("GRAPH_SPECTRUM"), vec);
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}
}
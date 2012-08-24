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
 * Spetrum.java
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
package gtna.metrics.fragmentation;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Timer;

import java.util.Arrays;
import java.util.HashMap;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * @author stef
 *
 */
public class LaplaceSpectrum extends Metric {
	/**
	 * @param key
	 */
	public LaplaceSpectrum() {
		super("LAPLACE_SPECTRUM");
		// TODO Auto-generated constructor stub
	}

	private double[][] vectors;
	private double[] values;
	private Timer timer;

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		timer = new Timer();
		Matrix L = makeLaplacian(g);
		EigenvalueDecomposition eig = new EigenvalueDecomposition(L);
		values = eig.getRealEigenvalues();
		vectors = eig.getV().getArray();
        timer.end();
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.values,
				"LAPLACE_SPECTRUM_EIGVALUES", folder);
		Arrays.sort(this.values);
		success &= DataWriter.writeWithIndex(this.values,
				"LAPLACE_SPECTRUM_EIGVALUES_SORTED", folder);
		success &= DataWriter.writeWithoutIndex(this.vectors,
				"LAPLACE_SPECTRUM_EIGVECTORS", folder);
		return success;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		// TODO Auto-generated method stub
		return new Single[]{new Single("LAPLACE_SPECTRUM_RUNTIME",timer.getRuntime())};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public static Matrix  makeLaplacian(Graph g) {
		Node[] nodes = g.getNodes();
		double[][] array = new double[nodes.length][nodes.length];
		int[] out;
		for (int i = 0; i < nodes.length; i++){
			out = nodes[i].getOutgoingEdges();
			array[i][i] = out.length;
			for (int j = 0; j < out.length; j++){
				array[i][out[j]] = -1;
			}
		}
		
		
		Matrix L = new Matrix(array);
		return L;
	}

}

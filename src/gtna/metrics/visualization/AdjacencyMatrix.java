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
 * AdjacencyMatrix.java
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
package gtna.metrics.visualization;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class AdjacencyMatrix extends Metric {

	private double[][] adjacencyMatrix;

	public AdjacencyMatrix() {
		super("ADJACENCY_MATRIX");

		this.adjacencyMatrix = new double[][] { new double[] { -1, -1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Edge[] edges = g.generateEdges();
		this.adjacencyMatrix = new double[edges.length][2];
		for (int i = 0; i < edges.length; i++) {
			this.adjacencyMatrix[i][0] = (double) edges[i].getDst()
					/ (double) g.getNodes().length;
			this.adjacencyMatrix[i][1] = (double) edges[i].getSrc()
					/ (double) g.getNodes().length;
		}
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.adjacencyMatrix,
				"ADJACENCY_MATRIX_ADJACENCY_MATRIX", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[] {};
	}
	
	@Override
	public boolean readData(String folder){
		
		/* 2D Value List */
		
		adjacencyMatrix = read2DValues(folder, "ADJACENCY_MATRIX_ADJACENCY_MATRIX");
		
		return true;
	}
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getDistributions()
	 */
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] {};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getNodeValueLists()
	 */
	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {};
	}

}

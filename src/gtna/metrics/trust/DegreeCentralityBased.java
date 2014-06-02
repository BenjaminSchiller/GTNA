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
 * DegreeCentralityBased.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Dirk;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.trust;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Dirk
 * 
 */
public class DegreeCentralityBased extends TrustMetric {

	int maxPathLength;

	BFS bfs;

	/**
	 * @param metricCode
	 * @param sampleSize
	 * @param parameters
	 */
	public DegreeCentralityBased(int sampleSize, boolean computeDistributions, boolean plotMulti, int maxPathLength) {
		super("DC", sampleSize, computeDistributions, plotMulti,  new Parameter[] { new IntParameter("K",
				maxPathLength) });
		this.maxPathLength = maxPathLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.trust.TrustMetric#prepareGraph(gtna.graph.Graph)
	 */
	@Override
	public void prepareGraph(Graph g) {
		long sumDegree = 0;

		for (Node n : g.getNodes())
			sumDegree += n.getDegree();

		final double avgDegree = (double) sumDegree / g.getNodeCount();

		EdgeValuator e = new EdgeValuator() {

			@Override
			public double getEdgeValue(Graph g, int src, int dst) {
				return (double) g.getNode(dst).getDegree() / avgDegree;
			}

		};

		bfs = new BFS(g, maxPathLength, e);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.trust.TrustMetric#prepareNode(gtna.graph.Node)
	 */
	@Override
	public void prepareNode(Node n) {
		bfs.search(n.getIndex());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.trust.TrustMetric#getNoOfTrustedNodes(gtna.graph.Node)
	 */
	@Override
	public int getNoOfTrustedNodes(Node n) {
		return bfs.getNodesCounter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.metrics.trust.TrustMetric#getNoOfEdgesInSubtree(gtna.graph.Node)
	 */
	@Override
	public int getNoOfEdgesInSubtree(Node n) {
		return bfs.getEdgesCounter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.trust.TrustMetric#computeTrust(gtna.graph.Node,
	 * gtna.graph.Node)
	 */
	@Override
	public boolean computeTrust(Node n1, Node n2) {
		return bfs.bidirectionalSearch(n1.getIndex(), n2.getIndex());
	}

}

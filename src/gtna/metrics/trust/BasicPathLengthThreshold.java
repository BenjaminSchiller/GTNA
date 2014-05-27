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
 * BasicPathLengthThreshold.java
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
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.HashMap;
import java.util.List;

/**
 * @author Dirk
 *
 */
public class BasicPathLengthThreshold extends TrustMetric {
	
	int maxPathLength;
	
	BFS bfs;
	
	public BasicPathLengthThreshold(int sampleSize, int maxPathLength) {
		super("BASIC", sampleSize, new Parameter[] {new IntParameter("K", maxPathLength)});
		this.maxPathLength = maxPathLength;
		
		
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.trust.TrustMetric#prepareGraph(gtna.graph.Graph)
	 */
	@Override
	public void prepareGraph(Graph g) {
		bfs = new BFS(g, maxPathLength);
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.trust.TrustMetric#prepareNode(gtna.graph.Node)
	 */
	@Override
	public void prepareNode(Node n) {		
		bfs.search( n.getIndex());
		

	}

	/* (non-Javadoc)
	 * @see gtna.metrics.trust.TrustMetric#getNoOfTrustedNodes(gtna.graph.Node)
	 */
	@Override
	public int getNoOfTrustedNodes(Node n) {
		return bfs.getNodesCounter();
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.trust.TrustMetric#getNoOfEdgesInSubtree(gtna.graph.Node)
	 */
	@Override
	public int getNoOfEdgesInSubtree(Node n) {
		return bfs.getEdgesCounter();
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.trust.TrustMetric#computeTrust(gtna.graph.Node, gtna.graph.Node)
	 */
	@Override
	public boolean computeTrust(Node n1, Node n2) {
		return bfs.bidirectionalSearch(n1.getIndex(), n2.getIndex());
	}
}

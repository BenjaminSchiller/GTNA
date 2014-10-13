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
 * DisjunctPaths.java
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

import java.util.ArrayList;
import java.util.List;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Dirk
 *
 */
public class DisjunctPaths extends TrustMetric {
	
	private int maxPathLength;
	private int minPaths;
	
	BFS bfs;

	/**
	 * @param sampleSize
	 * @param parameters
	 */
	public DisjunctPaths(int sampleSize,boolean computeDistributions, boolean plotMulti, int maxPathLength, int minPaths) {
		super("DP", sampleSize, computeDistributions, plotMulti, new Parameter[] {new IntParameter("K", maxPathLength), new IntParameter("L", minPaths) });
		this.maxPathLength = maxPathLength;
		this.minPaths = minPaths;
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
		bfs.search(n.getIndex());
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.trust.TrustMetric#getNoOfTrustedNodes(gtna.graph.Node)
	 */
	@Override
	public int getNoOfTrustedNodes(Node n) {
		//System.out.println("--- GET_NO_OF_TRUSTED_NODES (" + n.getIndex() + "---");
		List<Integer> possibleNodes = bfs.getVisitedNodes();
		
		int counter = 0;		
		
		for (int i : possibleNodes)
			if (computeTrust(n.getIndex(), i))
				counter++;
		return counter;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.trust.TrustMetric#getNoOfEdgesInSubtree(gtna.graph.Node)
	 */
	@Override
	public int getNoOfEdgesInSubtree(Node n) {
		// TODO
		return 0;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.trust.TrustMetric#computeTrust(gtna.graph.Node, gtna.graph.Node)
	 */
	@Override
	public boolean computeTrust(Node n1, Node n2) {
		return computeTrust(n1.getIndex(), n2.getIndex());
	}
	
	public boolean computeTrust(int node1, int node2) {
		int noPaths = 0;
		List<Integer> closedNodes = new ArrayList<Integer>();
		
		List<Integer> path;
		
		bfs.setClosedNodes(closedNodes);
		
		path = bfs.getPath(node1, node2);
		
		if (path != null && path.size() == 0)
			return true;
		
		while (path != null) {
			
			 noPaths++;
			 if (noPaths >= minPaths)
				 return true;
			 closedNodes.addAll(path);
			 bfs.setClosedNodes(closedNodes);
			 path = bfs.getPath(node1, node2);
		}
		return noPaths >= minPaths;
	}
}

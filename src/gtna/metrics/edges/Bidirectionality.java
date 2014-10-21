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
 * OneWayness.java
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
package gtna.metrics.edges;

import java.util.HashMap;
import java.util.List;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;

/**
 * @author Dirk
 *
 */
public class Bidirectionality extends Metric {
	
	private double value;
	private Distribution avgDegreeBidirectionality;
	private double avgBidirectionalityDegree2;
	
	public Bidirectionality() {
		super("BIDIRECTIONALITY");
	}
	
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}
	
	@Override
	public void computeData(Graph graph, Network nw,
			HashMap<String, Metric> metrics) {
		
		computeBidirectionality(graph);
		computeDegreeBidirectionality(graph);
	}
	
	public void computeBidirectionality(Graph g) {
		long unid = 0;
		long bid = 0;
		
		Edges edges = g.getEdges();
		
		List<Edge> edgeList = edges.getEdges();
		
		for (Edge e : edgeList)
			if (edges.contains(e.getDst(), e.getSrc()))
				bid++;
			else
				unid++;
		
		value = ((double)(bid / 2)) / ((bid / 2) + unid);
	}
	
	public void computeDegreeBidirectionality(Graph g) {	
		int maxDegree = 0;

		for (Node n : g.getNodes())
			if (n.getDegree() > maxDegree)
				maxDegree = n.getDegree();
		
		double[] avgBidirectionality = new double[maxDegree+1];
		int count[] = new int[maxDegree+1];
		
		for (Node n : g.getNodes()) {
			int bid = 0;
			int unid = 0;
			
			int d = n.getDegree();
			
			for (int dst : n.getOutgoingEdges())
				if (g.getEdges().contains(dst, n.getIndex()))
					bid++;
				else
					unid++;
			
			for (int src : n.getIncomingEdges())
				if (g.getEdges().contains(n.getIndex(), src))
					bid++;
				else
					unid++;
			
			double value = ((double)(bid / 2)) / ((bid / 2) + unid);
			
			if (count[d] == 0) {
				count[d]++;
				avgBidirectionality[d] = value;
			} else 
				avgBidirectionality[d] = (avgBidirectionality[d] * count[d] + value) / (++count[d]);
		}
		
		avgDegreeBidirectionality = new Distribution(
				"BIDIRECTIONALITY_DEGREE_BIDIRECTIONALITY", avgBidirectionality);
		
		avgBidirectionalityDegree2 = avgBidirectionality[2];
		
	}
	
	@Override
	public boolean writeData(String folder) {
		boolean success = false;
		success &= DataWriter.writeWithoutIndex(
				this.avgDegreeBidirectionality.getDistribution(),
				"BIDIRECTIONALITY_DEGREE_BIDIRECTIONALITY", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single bidirectionality = new Single(
				"BIDIRECTIONALITY_BIDIRECTIONALITY",
				value);
		Single bidDegree2 = new Single("BIDIRECTIONALITY_AVERAGE_BIDIRECTIONALITY_DEGREE2", avgBidirectionalityDegree2);
		return new Single[] { bidirectionality, bidDegree2 };
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#readData(java.lang.String)
	 */
	@Override
	public boolean readData(String folder) {
		avgDegreeBidirectionality = new Distribution("BIDIRECTIONALITY_DEGREE_BIDIRECTIONALITY", readDistribution(folder, "BIDIRECTIONALITY_DEGREE_BIDIRECTIONALITY"));
		return true;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getDistributions()
	 */
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] {avgDegreeBidirectionality};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getNodeValueLists()
	 */
	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[0];
	}
}

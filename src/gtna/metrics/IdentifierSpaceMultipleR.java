/*
 * ===========================================================
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
 * IdentifierSpaceMultipleR.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-01 : appended multipleR to all names / keywords (BS)
*/
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.routing.node.RingNode;
import gtna.routing.node.RingNodeMultiR;

import java.util.Hashtable;

public class IdentifierSpaceMultipleR extends MetricImpl implements Metric {
	double[][] distances;
	
	double[][] distancesByR1;

	double[][] ids;

	public IdentifierSpaceMultipleR() {
		super("ID_SPACE_MR");
	}

	private void initEmpty() {
		this.distances = new double[][] { new double[] { 0.0, 0.0 } };
		this.distancesByR1 = new double[][] { new double[] { 0.0, 0.0 } };
		this.ids = new double[][] { new double[] { 0.0, 0.0 } };
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		if (!(g.nodes[0] instanceof RingNode)
				&& !(g.nodes[0] instanceof RingNodeMultiR)) {
			this.initEmpty();
			return;
		}
		if (g.nodes[0] instanceof RingNode) {
			this.distances = this.distancesRingNode(g.nodes, g.edges);
			this.distancesByR1 = this.distances;
			this.ids = this.idsRingNode(g.nodes);
		}
		if (g.nodes[0] instanceof RingNodeMultiR) {
			this.distances = this.distancesRingNodeMultiR(g.nodes, g.edges);
			this.distancesByR1 = this.distancesByR1RingNodeMultiR(g.nodes, g.edges);
			this.ids = this.idsRingNodeMultiR(g.nodes);
		}
	}

	private double[][] distancesRingNode(Node[] nodes, int edges) {
		double[][] distances = new double[edges][];
		int index = 0;
		for (int i = 0; i < nodes.length; i++) {
			RingNode node = (RingNode) nodes[i];
			for (int j = 0; j < node.out().length; j++) {
				RingNode out = (RingNode) node.out()[j];
				distances[index] = new double[2];
				distances[index][0] = node.getID().pos;
				distances[index][1] = out.getID().dist(node.getID());
				index++;
			}
		}
		return distances;
	}

	private double[][] distancesRingNodeMultiR(Node[] nodes, int edges) {
		int realities = ((RingNodeMultiR) nodes[0]).getIDs().length;
		double[][] distances = new double[edges * realities][];
		int index = 0;
		for (int r = 0; r < realities; r++) {
			for (int i = 0; i < nodes.length; i++) {
				RingNodeMultiR node = (RingNodeMultiR) nodes[i];
				for (int j = 0; j < node.out().length; j++) {
					RingNodeMultiR out = (RingNodeMultiR) node.out()[j];
					double pos = node.getIDs()[r].pos;
					double dist = out.getIDs()[r].dist(node.getIDs()[r]);
					distances[index] = new double[] { pos, dist + r };
					index++;
				}
			}
		}
		return distances;
	}

	private double[][] distancesByR1RingNodeMultiR(Node[] nodes, int edges) {
		int realities = ((RingNodeMultiR) nodes[0]).getIDs().length;
		double[][] distances = new double[edges * realities][];
		int index = 0;
		for (int r = 0; r < realities; r++) {
			for (int i = 0; i < nodes.length; i++) {
				RingNodeMultiR node = (RingNodeMultiR) nodes[i];
				for (int j = 0; j < node.out().length; j++) {
					RingNodeMultiR out = (RingNodeMultiR) node.out()[j];
					double pos = node.getIDs()[0].pos;
					double dist = out.getIDs()[r].dist(node.getIDs()[r]);
					distances[index] = new double[] { pos, dist + r };
					index++;
				}
			}
		}
		return distances;
	}

	private double[][] idsRingNode(Node[] nodes) {
		double[][] ids = new double[nodes.length][2];
		for (int i = 0; i < nodes.length; i++) {
			double pos = ((RingNode) nodes[i]).getID().pos;
			ids[i] = new double[] { pos, pos };
		}
		return ids;
	}

	private double[][] idsRingNodeMultiR(Node[] nodes) {
		int realities = ((RingNodeMultiR) nodes[0]).getIDs().length;
		double[][] ids = new double[nodes.length * realities][2];
		for (int r = 0; r < realities; r++) {
			for (int i = 0; i < nodes.length; i++) {
				double pos = ((RingNodeMultiR) nodes[i]).getIDs()[r].pos;
				int index = r * nodes.length + i;
				ids[index] = new double[] { pos, pos + r };
			}
		}
		return ids;
	}

	public Value[] getValues(Value[] values) {
		return new Value[] {};
	}

	public boolean writeData(String folder) {
		DataWriter.writeWithoutIndex(this.distances, "ID_SPACE_MR_DISTANCES",
				folder);
		DataWriter.writeWithoutIndex(this.distancesByR1, "ID_SPACE_MR_DISTANCES_BY_R1",
				folder);
		DataWriter.writeWithoutIndex(this.ids, "ID_SPACE_MR_IDS", folder);
		return true;
	}

}

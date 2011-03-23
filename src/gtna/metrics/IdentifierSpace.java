package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.routing.node.RingNode;
import gtna.routing.node.RingNodeMultiR;

import java.util.Hashtable;

public class IdentifierSpace extends MetricImpl implements Metric {
	double[][] distances;
	
	double[][] distancesByR1;

	double[][] ids;

	public IdentifierSpace() {
		super("ID_SPACE");
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

	private double[][] distancesRingNode(NodeImpl[] nodes, int edges) {
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

	private double[][] distancesRingNodeMultiR(NodeImpl[] nodes, int edges) {
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

	private double[][] distancesByR1RingNodeMultiR(NodeImpl[] nodes, int edges) {
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

	private double[][] idsRingNode(NodeImpl[] nodes) {
		double[][] ids = new double[nodes.length][2];
		for (int i = 0; i < nodes.length; i++) {
			double pos = ((RingNode) nodes[i]).getID().pos;
			ids[i] = new double[] { pos, pos };
		}
		return ids;
	}

	private double[][] idsRingNodeMultiR(NodeImpl[] nodes) {
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
		DataWriter.writeWithoutIndex("ID_SPACE_DISTANCES", folder,
				this.distances);
		DataWriter.writeWithoutIndex("ID_SPACE_DISTANCES_BY_R1", folder,
				this.distancesByR1);
		DataWriter.writeWithoutIndex("ID_SPACE_IDS", folder, this.ids);
		return true;
	}

}

package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

public class ClusteringCoefficient extends MetricImpl implements Metric {
	private double[] lcc;

	private double[] lccShort;

	private double cc;

	private double ccw;

	private Timer timer;

	public ClusteringCoefficient() {
		super("CC");
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		this.timer = new Timer();
		double[] lcc = this.computeLCC(g.nodes, g.map());
		Arrays.sort(lcc);
		this.lcc = lcc;
		this.lccShort = Util.avgArray(lcc, Config
				.getInt("LCC_SHORT_MAX_VALUES"));
		this.cc = this.computeCC(this.lcc);
		this.ccw = this.computeCCW(this.lcc, g.nodes);
		this.timer.end();
	}

	private double[] computeLCC(NodeImpl[] nodes, HashMap<String, Edge> map) {
		double[] lcc = new double[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			Node[] neighborhood = this.neighborhood(nodes[i]);
			if (neighborhood.length == 1) {
				lcc[i] = 0;
			} else {
				int edges = this.edgesInNeighborhood_NEW(neighborhood,
						nodes[i], nodes.length, map);
				lcc[i] = edges
						/ (double) (neighborhood.length * (neighborhood.length - 1));
			}
		}
		return lcc;
	}

	private int edgesInNeighborhood_NEW(Node[] nh, Node center, int nodes,
			HashMap<String, Edge> map) {
		boolean[] in = new boolean[nodes];
		for (int i = 0; i < nh.length; i++) {
			in[nh[i].index()] = true;
		}
		int edges = 0;
		for (int i = 0; i < nh.length; i++) {
			Node[] out = nh[i].out();
			for (int j = 0; j < out.length; j++) {
				if (in[out[j].index()]) {
					edges++;
				}
			}
		}
		return edges;
	}

	private Node[] neighborhood(NodeImpl node) {
		ArrayList<NodeImpl> neighborhood = new ArrayList<NodeImpl>(
				node.out().length + node.in().length);
		neighborhood.add(node);
		NodeImpl[] out = node.out();
		for (int i = 0; i < out.length; i++) {
			neighborhood.add(out[i]);
		}
		NodeImpl[] in = node.in();
		for (int i = 0; i < in.length; i++) {
			if (!neighborhood.contains(in[i])) {
				neighborhood.add(in[i]);
			}
		}
		return Util.toArray(neighborhood);
	}

	private double computeCC(double[] lcc) {
		int counter = 0;
		double cc = 0;
		for (int i = 0; i < lcc.length; i++) {
			cc += lcc[i];
			counter += lcc[i] != 0 ? 1 : 0;
		}
		return cc / (double) counter;
	}

	private double computeCCW(double[] lcc, Node[] nodes) {
		double numerator = 0;
		int denominator = 0;
		for (int i = 0; i < lcc.length; i++) {
			int d = nodes[i].in().length + nodes[i].out().length;
			numerator += (double) (d * (d - 1)) * lcc[i];
			denominator += d * (d - 1);
		}
		return (double) numerator / (double) denominator;
	}

	public Value[] getValues(Value[] values) {
		Value CC = new Value("CC", this.cc);
		Value CCW = new Value("CCW", this.ccw);
		Value CC_RT = new Value("CC_RT", this.timer.rt());
		return new Value[] { CC, CCW, CC_RT };
	}

	public boolean writeData(String folder) {
		DataWriter.writeWithIndex(this.lcc, "LCC", folder);
		DataWriter.writeWithIndex(this.lccShort, "LCC_SHORT", folder);
		return true;
	}

}

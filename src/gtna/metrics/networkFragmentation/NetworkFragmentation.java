package gtna.metrics.networkFragmentation;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.graph.sorting.NodeSorting;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricImpl;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.Util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public abstract class NetworkFragmentation extends MetricImpl implements Metric {
	public static final int DEGREE_DESC = 1;

	public static final int IN_DEGREE_DESC = 2;

	public static final int OUT_DEGREE_DESC = 3;

	public static final int RANDOM = 4;

	private int order;

	private boolean bidirectional;

	private double[] mcs;

	private double[] aics;

	private double[] noc;

	private double anoc;

	private double mnoc;

	private double por;

	private Timer timer;

	public NetworkFragmentation(int order, boolean bidirectional) {
		super(key(order, bidirectional));
		this.order = order;
		this.bidirectional = bidirectional;
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		this.timer = new Timer();
		int steps = Math.min(Config.getInt("NETWORK_FRAGMENTATION_STEPS"),
				g.nodes.length);
		this.mcs = new double[steps];
		this.aics = new double[steps];
		this.noc = new double[steps];
		Random rand = new Random(System.currentTimeMillis());
		Node[] sorted = null;
		if (this.order == DEGREE_DESC) {
			sorted = NodeSorting.degreeDesc(g.nodes, rand);
		} else if (this.order == IN_DEGREE_DESC) {
			sorted = NodeSorting.inDegreeDesc(g.nodes, rand);
		} else if (this.order == OUT_DEGREE_DESC) {
			sorted = NodeSorting.outDegreeDesc(g.nodes, rand);
		} else if (this.order == RANDOM) {
			sorted = NodeSorting.random(g.nodes, rand);
		}
		for (int round = 0; round < steps; round++) {
			int number = round * g.nodes.length / steps;
			boolean[] removed = removed(number, sorted);
			int[] sizes = null;
			if (this.bidirectional) {
				sizes = this.clusterSizesBidirectional(g.nodes, removed);
			} else {
				sizes = this.clusterSizesUnidirectional(g.nodes, removed);
			}
			this.mcs[round] = Util.max(sizes);
			if (sizes.length == 1) {
				this.aics[round] = 0;
			} else {
				this.aics[round] = (double) (Util.sum(sizes) - this.mcs[round])
						/ (double) (sizes.length - 1);
			}
			this.noc[round] = sizes.length;
		}
		this.anoc = Util.avg(this.noc);
		this.mnoc = Util.max(this.noc);
		this.por = this.computePOR(this.mcs);
		this.timer.end();
	}

	private double computePOR(double[] mcs) {
		int nodes = (int) mcs[0];
		for (int i = 0; i < mcs.length; i++) {
			int removed = i * nodes / mcs.length;
			int gap = nodes - removed - (int) mcs[i];
			if (gap > mcs[i]) {
				return i;
			}
		}
		return mcs.length;
	}

	private int[] clusterSizesBidirectional(NodeImpl[] nodes, boolean[] removed) {
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		boolean[] visited = new boolean[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			if (removed[nodes[i].index()] || visited[nodes[i].index()]) {
				continue;
			}
			int size = 0;
			Queue<Node> queue = new LinkedList<Node>();
			queue.add(nodes[i]);
			visited[nodes[i].index()] = true;
			while (!queue.isEmpty()) {
				size++;
				Node current = queue.poll();
				Node[] out = current.out();
				for (int j = 0; j < out.length; j++) {
					if (!visited[out[j].index()] && !removed[out[j].index()]) {
						queue.add(out[j]);
						visited[out[j].index()] = true;
					}
				}
				Node[] in = current.in();
				for (int j = 0; j < in.length; j++) {
					if (!visited[in[j].index()] && !removed[in[j].index()]) {
						queue.add(in[j]);
						visited[in[j].index()] = true;
					}
				}
			}
			sizes.add(size);
		}
		return Util.toArray(sizes);
	}

	private int[] clusterSizesUnidirectional(NodeImpl[] nodes, boolean[] removed) {
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		boolean[] visited = new boolean[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			if (removed[nodes[i].index()] || visited[nodes[i].index()]) {
				continue;
			}

			Queue<Node> queueFW = new LinkedList<Node>();
			boolean[] visitedFW = new boolean[nodes.length];
			visitedFW[nodes[i].index()] = true;
			queueFW.add(nodes[i]);
			while (!queueFW.isEmpty()) {
				Node current = queueFW.poll();
				Node[] out = current.out();
				for (int j = 0; j < out.length; j++) {
					if (!visitedFW[out[j].index()] && !removed[out[j].index()]) {
						queueFW.add(out[j]);
						visitedFW[out[j].index()] = true;
					}
				}
			}

			Queue<Node> queueBW = new LinkedList<Node>();
			boolean[] visitedBW = new boolean[nodes.length];
			visitedBW[nodes[i].index()] = true;
			queueBW.add(nodes[i]);
			while (!queueBW.isEmpty()) {
				Node current = queueBW.poll();
				Node[] in = current.in();
				for (int j = 0; j < in.length; j++) {
					if (!visitedBW[in[j].index()] && !removed[in[j].index()]) {
						queueBW.add(in[j]);
						visitedBW[in[j].index()] = true;
					}
				}
			}

			int size = 0;
			for (int j = 0; j < visited.length; j++) {
				if (visitedFW[j] && visitedBW[j]) {
					visited[j] = true;
					size++;
				}
			}
			sizes.add(size);
		}
		return Util.toArray(sizes);
	}

	private boolean[] removed(int number, Node[] sorted) {
		boolean[] removed = new boolean[sorted.length];
		for (int i = 0; i < number; i++) {
			removed[sorted[i].index()] = true;
		}
		return removed;
	}

	private static String key(int order, boolean bidirectional) {
		String key = "NF_";
		if (bidirectional) {
			key += "B_";
		} else {
			key += "U_";
		}
		if (order == DEGREE_DESC) {
			key += "D";
		} else if (order == IN_DEGREE_DESC) {
			key += "DI";
		} else if (order == OUT_DEGREE_DESC) {
			key += "DO";
		} else if (order == RANDOM) {
			key += "R";
		}
		return key;
	}

	public Value[] getValues(Value[] values) {
		Value ANOC = new Value(this.key() + "_ANOC", this.anoc);
		Value MNOC = new Value(this.key() + "_MNOC", this.mnoc);
		Value POR = new Value(this.key() + "_POR", this.por);
		Value RT = new Value(this.key() + "_RT", this.timer.rt());
		return new Value[] { ANOC, MNOC, POR, RT };
	}

	public boolean writeData(String folder) {
		DataWriter.write(this.key() + "_AICS", folder, this.aics);
		DataWriter.write(this.key() + "_MCS", folder, this.mcs);
		DataWriter.write(this.key() + "_NOC", folder, this.noc);
		return false;
	}
}

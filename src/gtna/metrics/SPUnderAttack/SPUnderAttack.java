package gtna.metrics.SPUnderAttack;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorting;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricImpl;
import gtna.metrics.networkFragmentation.NetworkFragmentation;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Timer;

import java.util.Hashtable;
import java.util.Random;

public abstract class SPUnderAttack extends MetricImpl implements Metric {
	private int order;

	private double[] conn;

	private double[] cpl;

	private double[] diam;

	private Timer timer;

	public SPUnderAttack(int order) {
		super(key(order));
		this.order = order;
	}

	private static String key(int order) {
		if (order == NetworkFragmentation.DEGREE_DESC) {
			return "SPLUA_D";
		} else if (order == NetworkFragmentation.IN_DEGREE_DESC) {
			return "SPLUA_DI";
		} else if (order == NetworkFragmentation.OUT_DEGREE_DESC) {
			return "SPLUA_DO";
		} else if (order == NetworkFragmentation.RANDOM) {
			return "SPLUA_R";
		}
		return null;
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		this.timer = new Timer();
		int STEPS = Config.getInt(this.key() + "_ROUNDS");
		int steps = Math.min(STEPS, g.nodes.length);
		Random rand = new Random(System.currentTimeMillis());
		this.conn = new double[steps];
		this.cpl = new double[steps];
		this.diam = new double[steps];
		Node[] sorted = null;
		if (this.order == NetworkFragmentation.DEGREE_DESC) {
			sorted = NodeSorting.degreeDesc(g.nodes, rand);
		} else if (this.order == NetworkFragmentation.IN_DEGREE_DESC) {
			sorted = NodeSorting.inDegreeDesc(g.nodes, rand);
		} else if (this.order == NetworkFragmentation.OUT_DEGREE_DESC) {
			sorted = NodeSorting.outDegreeDesc(g.nodes, rand);
		} else if (this.order == NetworkFragmentation.RANDOM) {
			sorted = NodeSorting.random(g.nodes, rand);
		}
		for (int round = 0; round < steps; round++) {
			int number = round * g.nodes.length / steps;
			boolean[] removed = removed(number, sorted);
			this.computeRound(round, g.nodes, removed);
		}
		timer.end();
	}

	private void computeRound(int round, Node[] nodes, boolean[] removed) {
		int[] spld = new int[1];
		for (int v = 0; v < nodes.length; v++) {
			int[] visited = new int[nodes.length];
			for (int i = 0; i < visited.length; i++) {
				if (removed[i]) {
					visited[i] = Integer.MAX_VALUE / 2;
				} else {
					visited[i] = -1;
				}
			}
			visited[v] = 0;

			short h = 1;
			boolean newPathFound = true;
			while (newPathFound) {
				newPathFound = false;
				for (int i = 0; i < visited.length; i++) {
					if (visited[i] == h - 1) {
						Node[] out = nodes[i].out();
						for (int j = 0; j < out.length; j++) {
							if (visited[out[j].index()] == -1) {
								visited[out[j].index()] = h;
								newPathFound = true;
								try {
									spld[h]++;
								} catch (ArrayIndexOutOfBoundsException e) {
									int[] temp = spld.clone();
									spld = new int[h + 1];
									for (int k = 0; k < temp.length; k++) {
										spld[k] = temp[k];
									}
									spld[h] = 1;
								}
							}
						}
					}
				}
				h++;
			}
		}
		this.cpl[round] = 0;
		long paths = 0;
		for (int i = 1; i < spld.length; i++) {
			this.cpl[round] += spld[i] * i;
			paths += spld[i];
		}
		this.cpl[round] /= (double) paths;
		this.diam[round] = spld.length;
		this.conn[round] = (double) paths
				/ (double) (nodes.length * (nodes.length - 1));

	}

	private boolean[] removed(int number, Node[] sorted) {
		boolean[] removed = new boolean[sorted.length];
		for (int i = 0; i < number; i++) {
			removed[sorted[i].index()] = true;
		}
		return removed;
	}

	public Value[] getValues(Value[] values) {
		Value RT = new Value(this.key() + "_RT", this.timer.rt());
		return new Value[] { RT };
	}

	public boolean writeData(String folder) {
		DataWriter.write(this.key() + "_CPL", folder, this.cpl);
		DataWriter.write(this.key() + "_DIAM", folder, this.diam);
		DataWriter.write(this.key() + "_CONN", folder, this.conn);
		return true;
	}
}

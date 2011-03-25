package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.Util;

import java.util.Arrays;
import java.util.Hashtable;

// TODO problem with big networks (> 40.000)
public class ShortestPath extends MetricImpl implements Metric {
	private double[] lcpl;

	private double[] lcplShort;

	private double[] spld;

	private double[] exp;

	private double diam;

	private double cpl;

	private double conn;

	private Timer timer;

	public ShortestPath() {
		super("SPL");
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		this.timer = new Timer();
		int[] spld = this.computeSPLD(g.nodes);
		int paths = Util.sum(spld);
		// SPLD
		this.spld = new double[spld.length];
		for (int i = 0; i < spld.length; i++) {
			this.spld[i] = (double) spld[i] / (double) paths;
		}
		// LSPL
		Arrays.sort(this.lcpl);
		this.lcplShort = Util.avgArray(this.lcpl, Config
				.getInt("LCPL_SHORT_MAX_VALUES"));
		// EXP
		this.exp = new double[this.spld.length];
		this.exp[0] = this.spld[0];
		for (int i = 1; i < this.spld.length; i++) {
			this.exp[i] = this.exp[i - 1] + this.spld[i];
		}
		// DIAM
		this.diam = this.spld.length - 1;
		// CPL
		this.cpl = 0;
		for (int i = 0; i < this.spld.length; i++) {
			this.cpl += this.spld[i] * i;
		}
		// CONN
		this.conn = (double) paths
				/ (double) (g.nodes.length * (g.nodes.length - 1));
		this.timer.end();
	}

	private int[] computeSPLD(Node[] nodes) {
		int[] spld = new int[1];
		this.lcpl = new double[nodes.length];
		for (int v = 0; v < nodes.length; v++) {
			int[] visited = new int[nodes.length];
			for (int i = 0; i < visited.length; i++) {
				visited[i] = -1;
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
								lcpl[v] += h;
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
			lcpl[v] /= nodes.length;
		}
		return spld;
	}

	public Value[] getValues(Value[] values) {
		Value DIAM = new Value("DIAM", this.diam);
		Value CPL = new Value("CPL", this.cpl);
		Value CONN = new Value("CONN", this.conn);
		Value SPL_RT = new Value("SPL_RT", this.timer.rt());
		return new Value[] { DIAM, CPL, CONN, SPL_RT };
	}

	public boolean writeData(String folder) {
		DataWriter.writeWithIndex(this.lcpl, "LCPL", folder);
		DataWriter.writeWithIndex(this.lcplShort, "LCPL_SHORT", folder);
		DataWriter.writeWithIndex(this.spld, "SPLD", folder);
		DataWriter.writeWithIndex(this.exp, "EXP", folder);
		return true;
	}

}

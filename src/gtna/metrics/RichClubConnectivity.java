package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.sorting.NodeSorting;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Timer;

import java.util.Hashtable;
import java.util.Random;

public class RichClubConnectivity extends MetricImpl implements Metric {
	private double[] rcc;

	private double[] rccShort;

	private Timer timer;

	public RichClubConnectivity() {
		super("RCC");
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		this.timer = new Timer();
		int[] order = NodeSorting.byDegreeDesc(g.nodes, new Random(System
				.currentTimeMillis()));
		int edges = 0;
		this.rcc = new double[order.length + 1];
		for (int p = 2; p <= order.length; p++) {
			int newNode = order[p - 1];
			for (int i = 0; i < p - 1; i++) {
				if (g.nodes[newNode].hasOut(g.nodes[i])) {
					edges++;
				}
				if (g.nodes[i].hasOut(g.nodes[newNode])) {
					edges++;
				}
			}
			this.rcc[p] = (double) edges / (double) (p * (p - 1));
		}
		this.rccShort = new double[Math.min(this.rcc.length, Config
				.getInt("RCC_SHORT_MAX_VALUES"))];
		for(int i=0; i<this.rccShort.length; i++){
			this.rccShort[i] = this.rcc[i];
		}
		timer.end();
	}

	public Value[] getValues(Value[] values) {
		Value RCC_RT = new Value("RCC_RT", this.timer.rt());
		return new Value[] { RCC_RT };
	}

	public boolean writeData(String folder) {
		DataWriter.write("RCC", folder, this.rcc);
		DataWriter.write("RCC_SHORT", folder, this.rccShort);
		return true;
	}
}

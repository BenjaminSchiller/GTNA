package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.networks.Network;

import java.util.Hashtable;

public class Memory extends MetricImpl implements Metric {
	private double mem;

	public Memory() {
		super("MEM");
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
	}

	public Value[] getValues(Value[] values) {
		this.mem = (double) (Runtime.getRuntime().totalMemory() - Runtime
				.getRuntime().freeMemory())
				/ (double) (1024 * 1024);
		Value MEM = new Value("MEM", this.mem);
		return new Value[] { MEM };
	}

	public boolean writeData(String folder) {
		return true;
	}

}

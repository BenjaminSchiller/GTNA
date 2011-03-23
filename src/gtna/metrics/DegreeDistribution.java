package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Timer;

import java.util.Hashtable;

public class DegreeDistribution extends MetricImpl implements Metric {
	public double[] dd;

	public double[] ddi;

	public double[] ddo;

	public double[] ecd;

	public double[] ecdi;

	public double[] ecdo;

	public Timer timer;

	public Graph g;

	public DegreeDistribution() {
		super("DD");
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		this.timer = new Timer();
		this.g = g;
		long[] DD = new long[g.maxDegree + 1];
		long[] DDI = new long[g.maxInDegree + 1];
		long[] DDO = new long[g.maxOutDegree + 1];
		for (int i = 0; i < g.nodes.length; i++) {
			DD[g.nodes[i].in().length + g.nodes[i].out().length]++;
			DDI[g.nodes[i].in().length]++;
			DDO[g.nodes[i].out().length]++;
		}
		// DD
		this.dd = new double[g.maxDegree + 1];
		for (int i = 0; i < DD.length; i++) {
			this.dd[i] = (double) DD[i] / (double) g.nodes.length;
		}
		// DDI
		this.ddi = new double[g.maxInDegree + 1];
		for (int i = 0; i < DDI.length; i++) {
			this.ddi[i] = (double) DDI[i] / (double) g.nodes.length;
		}
		// DDO
		this.ddo = new double[g.maxOutDegree + 1];
		for (int i = 0; i < DDO.length; i++) {
			this.ddo[i] = (double) DDO[i] / (double) g.nodes.length;
		}
		// ECD
		this.ecd = new double[g.maxDegree + 1];
		this.ecd[0] = this.dd[0];
		for (int i = 1; i < this.dd.length; i++) {
			this.ecd[i] = ecd[i - 1] + this.dd[i];
		}
		// ECDI
		this.ecdi = new double[g.maxInDegree + 1];
		this.ecdi[0] = this.ddi[0];
		for (int i = 1; i < this.ddi.length; i++) {
			this.ecdi[i] = ecdi[i - 1] + this.ddi[i];
		}
		// ECDO
		this.ecdo = new double[g.maxOutDegree + 1];
		this.ecdo[0] = this.ddo[0];
		for (int i = 1; i < this.ddo.length; i++) {
			this.ecdo[i] = ecdo[i - 1] + this.ddo[i];
		}
		this.timer.end();
	}

	public Value[] getValues(Value[] values) {
		Value NODES = new Value("NODES", this.g.nodes.length);
		Value EDGES = new Value("EDGES", this.g.edges);
		Value D_AVG = new Value("D_AVG", this.g.avgDegree);
		Value D_MAX = new Value("D_MAX", this.g.maxDegree);
		Value D_MIN = new Value("D_MIN", this.g.minDegree);
		Value DI_MAX = new Value("DI_MAX", this.g.maxInDegree);
		Value DI_MIN = new Value("DI_MIN", this.g.minInDegree);
		Value DO_MAX = new Value("DO_MAX", this.g.maxOutDegree);
		Value DO_MIN = new Value("DO_MIN", this.g.minOutDegree);
		Value DD_RT = new Value("DD_RT", this.timer.rt());
		Value GG_RT = new Value("GG_RT", this.g.timer.rt());
		return new Value[] { NODES, EDGES, D_AVG, D_MAX, D_MIN, DI_MAX, DI_MIN,
				DO_MAX, DO_MIN, DD_RT, GG_RT };
	}

	public boolean writeData(String folder) {
		DataWriter.write("DD", folder, this.dd);
		DataWriter.write("DDI", folder, this.ddi);
		DataWriter.write("DDO", folder, this.ddo);
		DataWriter.write("ECD", folder, this.ecd);
		DataWriter.write("ECDI", folder, this.ecdi);
		DataWriter.write("ECDO", folder, this.ecdo);
		return true;
	}
}

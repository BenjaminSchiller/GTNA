package gtna.metrics;

import gtna.communities.CommunityList;
import gtna.communities.Detection;
import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.Util;

import java.util.Hashtable;

public class Communities extends MetricImpl {

	private double[] com;
	private double[] comShort;

	private double modularity;
	private double runtime;

	public Communities() {
		super("COM");
	}

	public void computeData(Graph g, Network s, Hashtable<String, Metric> m) {
		Timer rt = new Timer();
		CommunityList communities = Detection.detectCommunities(g);
		modularity = communities.calculateModularity();
		com = new double[g.nodes.length];
		for (Node node : g.nodes) {
			com[node.index()] = communities.getCommunity(node).getId() + 1;
		}
		comShort = Util.avgArray(com, Config.getInt("COM_SHORT_MAX_VALUES"));
		runtime = rt.msec();
	}

	public Value[] getValues(Value[] values) {
		Value MODULARITY = new Value("MODULARITY", this.modularity);
		Value COMMUNITY_RT = new Value("COMMUNITIES_RT", this.runtime);
		return new Value[] { MODULARITY, COMMUNITY_RT };
	}

	public boolean writeData(String folder) {
		DataWriter.writeWithIndex(this.com, "COMMUNITIES", folder);
		DataWriter.writeWithIndex(this.comShort, "COMMUNITIES_SHORT", folder);
		return true;
	}
}

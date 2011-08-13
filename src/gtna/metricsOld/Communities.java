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
 * Communities.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.metricsOld;

//TODO reimplement Communities
public class Communities {
	// public class Communities extends MetricImpl {
	//
	// private double[] com;
	// private double[] comShort;
	//
	// private double modularity;
	// private double runtime;
	//
	// public Communities() {
	// super("COM");
	// }
	//
	// public void computeData(Graph g, Network s, Hashtable<String, Metric> m)
	// {
	// Timer rt = new Timer();
	// CommunityList communities = Detection.detectCommunities(g);
	// modularity = communities.calculateModularity();
	// com = new double[g.nodes.length];
	// for (Node node : g.nodes) {
	// com[node.index()] = communities.getCommunity(node).getId() + 1;
	// }
	// comShort = Util.avgArray(com, Config.getInt("COM_SHORT_MAX_VALUES"));
	// runtime = rt.msec();
	// }
	//
	// public Value[] getValues(Value[] values) {
	// Value MODULARITY = new Value("MODULARITY", this.modularity);
	// Value COMMUNITY_RT = new Value("COMMUNITIES_RT", this.runtime);
	// return new Value[] { MODULARITY, COMMUNITY_RT };
	// }
	//
	// public boolean writeData(String folder) {
	// DataWriter.writeWithIndex(this.com, "COMMUNITIES", folder);
	// DataWriter.writeWithIndex(this.comShort, "COMMUNITIES_SHORT", folder);
	// return true;
	// }
}

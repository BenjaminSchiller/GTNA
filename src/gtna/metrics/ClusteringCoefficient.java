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
 * ClusteringCoefficient.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-05-24 : Bugfix - excluding v from v's neighborhood (BS);
 * 2011-05-24 : Performance - switching from array- to set-based 
 *              computations, should be faster especially on 
 *              low-degree graphs (BS);
 * 2011-05-24 : all config parameters starting with CC (BS);
 */
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.GraphProperties;
import gtna.graph.NodeImpl;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

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
		boolean onlyOut = GraphProperties.bidirectional(g)
				|| Config
						.getBoolean("CC_NEIGHBORHOOD_DEFINED_BY_OUTGOING_EDGES");
		this.lcc = this.computeLCC(g.nodes, onlyOut);
		Arrays.sort(this.lcc);
		this.lccShort = this.computeLCCShort(this.lcc);
		this.cc = this.computeCC(this.lcc);
		this.ccw = this.computeCCW(this.lcc, g.nodes);
		this.timer.end();
	}

	private double[] computeLCC(NodeImpl[] nodes, boolean onlyOut) {
		double[] lcc = new double[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			Set<NodeImpl> n = this.neighborhood(nodes[i], onlyOut);
			if (n.size() <= 1) {
				lcc[i] = 0;
			} else {
				int edges = this.edgesInNeighborhood(n);
				lcc[i] = edges / (double) (n.size() * (n.size() - 1));
			}
		}
		return lcc;
	}

	private int edgesInNeighborhood(Set<NodeImpl> n) {
		int edges = 0;
		Iterator<NodeImpl> iter = n.iterator();
		while (iter.hasNext()) {
			NodeImpl current = iter.next();
			NodeImpl[] OUT = current.out();
			for (NodeImpl out : OUT) {
				if (n.contains(out)) {
					edges++;
				}
			}
		}
		return edges;
	}

	private Set<NodeImpl> neighborhood(NodeImpl node, boolean onlyOut) {
		Set<NodeImpl> n = new HashSet<NodeImpl>();
		NodeImpl[] OUT = node.out();
		for (NodeImpl out : OUT) {
			n.add(out);
		}
		if (!onlyOut) {
			NodeImpl[] IN = node.in();
			for (NodeImpl in : IN) {
				n.add(in);
			}
		}
		return n;
	}

	private double[] computeLCCShort(double[] lcc) {
		return Util.avgArray(lcc, Config.getInt("CC_LCC_SHORT_MAX_VALUES"));
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

	private double computeCCW(double[] lcc, NodeImpl[] nodes) {
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
		Value CC = new Value("CC_CC", this.cc);
		Value CCW = new Value("CC_CCW", this.ccw);
		Value RT = new Value("CC_RT", this.timer.rt());
		return new Value[] { CC, CCW, RT };
	}

	public boolean writeData(String folder) {
		DataWriter.writeWithIndex(this.lcc, "CC_LCC", folder);
		DataWriter.writeWithIndex(this.lccShort, "CC_LCC_SHORT", folder);
		return true;
	}
}

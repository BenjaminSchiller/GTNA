/* ===========================================================
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
 * PositiveFeedbackPreference.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: truong;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model;

import java.util.HashMap;
import java.util.Random;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * @author truong
 * 
 */
public class PositiveFeedbackPreference extends Network {

	// parameter
	private double p;
	private double delta;

	// variables to algorithms
	private double[] nodePref;
	private HashMap<String, Boolean> edgesMap;
	private double[] nodeDegree;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public PositiveFeedbackPreference(int nodes, double probability,
			double delta, Transformation[] transformations) {
		super("PFP", nodes, new Parameter[] {
				new DoubleParameter("PROBABILITY", probability),
				new DoubleParameter("DELTA", delta) }, transformations);
		this.p = probability;
		this.delta = delta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		nodePref = new double[nodes.length];
		edgesMap = new HashMap<String, Boolean>();
		nodeDegree = new double[nodes.length];

		Random rand = new Random();
		for (int i = 0; i < nodes.length; i++) {
			// TODO: generate original random graph
			if (rand.nextDouble() < p) {

				// a new node is attached to "host"
				int hostIndex = this.selectNodeUsingPref(i - 1);
				addEdge(i, hostIndex);

				// the host develops new links to two peers
				int peer1Index = this.selectNodeUsingPref(i - 1);
				int peer2Index = this.selectNodeUsingPref(i - 1);
				// TODO: are peers diff from host???
				addEdge(hostIndex, peer1Index);
				addEdge(hostIndex, peer2Index);

			} else {

				// a new node is attached to two hosts
				int host1Index = this.selectNodeUsingPref(i - 1);
				int host2Index = this.selectNodeUsingPref(i - 1);
				// TODO: are hosts diff???
				addEdge(i, host1Index);
				addEdge(i, host2Index);

				// one of the hosts is linked to a peer
				int peerIndex = this.selectNodeUsingPref(i - 1);
				// TODO: are peer diff from hosts???
				if ((new Random()).nextInt(2) == 0) {
					addEdge(host1Index, peerIndex);
				} else {
					addEdge(host2Index, peerIndex);
				}
			}
		}
		return null;
	}

	private void updatePreference(int nodeIndex) {
		double degree = (double) (nodeDegree[nodeIndex]);
		nodePref[nodeIndex] = 1 + delta * Math.log(degree);
	}

	private int selectNodeUsingPref(int maxIndex) {
		double prefSum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			prefSum += nodePref[i];
		}
		double r = (new Random()).nextDouble();
		double threshold = r * prefSum;
		double sum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			sum += nodePref[i];
			if (sum >= threshold) {
				return i;
			}
		}
		return (int) (r * maxIndex);
	}

	private void addEdge(int src, int dst) {
		edgesMap.put(Edge.toString(src, dst), true);
		nodeDegree[src]++;
		nodeDegree[dst]++;
		updatePreference(src);
		updatePreference(dst);
	}
}

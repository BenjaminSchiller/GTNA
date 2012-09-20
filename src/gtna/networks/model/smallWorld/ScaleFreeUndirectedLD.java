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
 * ScaleFreeUndirectedLD.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.smallWorld;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * @author benni
 * 
 */
public class ScaleFreeUndirectedLD extends Network {
	private double interval;
	private double alpha;
	private int cutoff;

	public ScaleFreeUndirectedLD(int nodes, double alpha, int cutoff,
			RoutingAlgorithm ra, Transformation[] t) {
		super("SCALE_FREE_UNDIRECTED_LD", nodes, new Parameter[] {
				new DoubleParameter("ALPHA", alpha),
				new IntParameter("CUTOFF", cutoff) }, t);
		this.interval = (double) 1 / nodes;
		this.alpha = alpha;
		this.cutoff = cutoff;
	}

	public double getAlpha() {
		return this.alpha;
	}

	@Override
	public Graph generate() {
		Node[] nodes = new Node[this.getNodes()];
		Graph g = new Graph(this.getDescription());
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(i, g);
		}
		RingIdentifier[] ids = new RingIdentifier[nodes.length];
		RingPartitionSimple[] parts = new RingPartitionSimple[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			ids[i] = new RingIdentifier(i * this.interval, true);
			parts[i] = new RingPartitionSimple(ids[i]);
		}
		RingIdentifierSpaceSimple idSpace = new RingIdentifierSpaceSimple(
				parts, true);

		// label distribution: each node is given a label that determines its
		// potential degree distribution
		double sum = 0;
		for (int k = 1; k <= this.cutoff; k++) {
			sum = sum + Math.pow((double) k, -this.alpha);
		}
		int[] labels = new int[nodes.length];
		double[] randNum = new double[nodes.length];
		Vector<Integer> vec = new Vector<Integer>(nodes.length);
		Random rand = new Random();
		for (int i = 0; i < nodes.length; i++) {
			vec.add(i);
			randNum[i] = rand.nextDouble();
		}
		Arrays.sort(randNum);
		int count = 0;
		double s = 0;
		for (int k = 1; k < this.cutoff; k++) {
			s = s + Math.pow((double) k, -this.alpha);
			while (randNum.length > count && vec.size() > 0
					&& randNum[count] * sum < s) {
				count++;
				labels[vec.remove(rand.nextInt(vec.size()))] = k;
			}
		}
		for (int k = 0; k < vec.size(); k++) {
			labels[vec.get(k)] = this.cutoff;
		}

		Edges edges = new Edges(nodes, (int) Math.round(sum * this.getNodes()));

		// create long-range edges: use labels and distance for each pair of
		// nodes
		double norm = 0;
		for (int i = 1; i <= nodes.length / 2; i++) {
			norm = norm + 2 / (i * this.interval);
		}
		for (int i = 0; i < nodes.length; i++) {
			for (int j = i + 1; j < nodes.length; j++) {

				double p = 1 - Math.exp(-(labels[i] * labels[j])
						/ (norm * ids[i].distance(ids[j])));
				if (rand.nextDouble() < p) {
					edges.add(i, j);
					edges.add(j, i);
				}

			}
		}

		g.setNodes(nodes);
		edges.fill();
		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);

		return g;
	}

}

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
 * ForestFire.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Dirk;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Timer;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * @author Dirk
 * 
 */
public class ForestFire extends Network {

	private double p;
	private double r;
	private double bd;
	private boolean bootstrap;

	/**
	 * @param key
	 * @param nodes
	 * @param b
	 * @param transformations
	 */
	public ForestFire(int nodes, double p, double r, double bd,
			boolean bootstrap, Transformation[] transformations) {
		super("FOREST_FIRE", nodes, new Parameter[] {
				new DoubleParameter("P", p), new DoubleParameter("R", r),
				new DoubleParameter("BD", bd),
				new BooleanParameter("BS", bootstrap) }, transformations);
		this.p = p;
		this.r = r;
		this.bd = bd;
		this.bootstrap = bootstrap;
	}

	@Override
	public Graph generate() {
		Random rand = new Random(System.currentTimeMillis());
		Graph graph = new Graph(this.getDescription());

		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, this.getNodes() * 10);

		List<Integer>[] inNeighbors;
		List<Integer>[] outNeighbors;
		inNeighbors = new ArrayList[this.getNodes()];
		outNeighbors = new ArrayList[this.getNodes()];

		for (int i = 0; i < this.getNodes(); i++) {
			inNeighbors[i] = new ArrayList<Integer>(50);
			outNeighbors[i] = new ArrayList<Integer>(50);
		}

		int sum_burned = 0;

		for (int v = 1; v < this.getNodes(); v++) {

			Timer t = new Timer();

			// Choose Ambassador node u.a.r.
			int w = rand.nextInt(v);

			// Set<Integer> nodesBurned = this.versionBinomial(w, inNeighbors,
			// outNeighbors, rand);
			// Set<Integer> nodesBurned = this.versionGeometric1(w, inNeighbors,
			// outNeighbors, rand);
			// Set<Integer> nodesBurned = this.versionGeometric2(w, inNeighbors,
			// outNeighbors, rand);
			Set<Integer> nodesBurned = this.versionGeometric3(w, inNeighbors,
					outNeighbors, rand);

			for (int b : nodesBurned) {
				edges.add(v, b);
				inNeighbors[b].add(v);
				outNeighbors[v].add(b);
				if (rand.nextDouble() < bd || (this.bootstrap && b == w)) {
					edges.add(b, v);
					inNeighbors[v].add(b);
					outNeighbors[b].add(v);
				}
			}
			t.end();
			// System.out.println(v + ": t=" + t.getMsec() + ", burned="
			// + nodesBurned.size());
			sum_burned += nodesBurned.size();
		}
		// System.out.println("burned total: " + sum_burned);

		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	/**
	 * 2005 version
	 * http://strategic.mit.edu/docs/matlab_networks/forestFireModel.m
	 */
	private Set<Integer> versionBinomial(int w, List<Integer>[] inNeighbors,
			List<Integer>[] outNeighbors, Random rand) {
		Queue<Integer> nodesToBurn = new LinkedList<Integer>();
		Set<Integer> nodesBurned = new HashSet<Integer>(getNodes());

		nodesToBurn.add(w);

		while (!nodesToBurn.isEmpty()) {
			int b = nodesToBurn.poll();

			if (!nodesBurned.contains(b)) {
				nodesBurned.add(b);

				// v1
				double prob_p = 1.0 / outNeighbors[b].size() / (1 - p);
				// v2
				// double prob_p = Math.pow((1 - p),
				// (outNeighbors[b].size() - 1)) * p;

				// v3 // define geometric(p)
				// return ceiling(ln(1-rand) / ln(1-p))
				// double prob_p = Math.ceil(Math.log(1 - rand.nextDouble())
				// / Math.log(1 - p));
				// v3'
				// GeometricDistribution geo = new GeometricDistribution(p);
				// double prob_p = geo.getProbabilityOfSuccess();

				// Forward Burning
				for (int z : outNeighbors[b]) {
					if (!nodesBurned.contains(z))
						if (rand.nextDouble() < prob_p) {
							nodesToBurn.add(z);
						}

				}

				// v1
				double prob_r = 1.0 / inNeighbors[b].size() / (1 - p);
				// v2
				// double prob_r = Math.pow((1 - p),
				// (inNeighbors[b].size() - 1)) * p * r;

				// v3
				// double prob_r = prob_p * r;

				// Backward Burning
				for (int z : inNeighbors[b]) {
					if (!nodesBurned.contains(z))
						if (rand.nextDouble() < r * prob_r) {
							nodesToBurn.add(z);
						}
				}
			}
		}

		return nodesBurned;
	}

	/**
	 * 2007 version
	 * https://github.com/snap-stanford/snap/tree/master/examples/forestfire
	 */
	private Set<Integer> versionGeometric1(int w, List<Integer>[] inNeighbors,
			List<Integer>[] outNeighbors, Random rand) {
		Queue<Integer> nodesToBurn = new LinkedList<Integer>();
		Set<Integer> nodesBurned = new HashSet<Integer>(getNodes());

		nodesToBurn.add(w);

		while (!nodesToBurn.isEmpty()) {
			int b = nodesToBurn.poll();

			if (!nodesBurned.contains(b)) {
				nodesBurned.add(b);

				int x = (int) Math.floor(Math.log(1 - rand.nextDouble())
						/ Math.log(1 - p));

				LinkedList<Integer> outs = new LinkedList<Integer>(
						outNeighbors[b]);
				Collections.shuffle(outs); // Forward Burning
				int i = 0;
				while (!outs.isEmpty() && i < x) {
					int ww = outs.poll();
					nodesToBurn.add(ww);
					i++;
				}

				int y = (int) Math.floor(Math.log(1 - rand.nextDouble())
						/ Math.log(1 - r * p));

				LinkedList<Integer> ins = new LinkedList<Integer>(
						inNeighbors[b]);
				Collections.shuffle(ins); // Backward Burning
				int j = 0;
				while (!ins.isEmpty() && j < y) {
					int ww = ins.poll();
					nodesToBurn.add(ww);
					j++;
				}
				// System.out.println(x + "/" + outNeighbors[b].size() + " "
				// + y + "/" + inNeighbors[b].size());
			}
		}

		return nodesBurned;
	}

	/**
	 * 2007 version, v2
	 * https://github.com/snap-stanford/snap/tree/master/examples/forestfire
	 */
	private Set<Integer> versionGeometric2(int w, List<Integer>[] inNeighbors,
			List<Integer>[] outNeighbors, Random rand) {
		Queue<Integer> nodesToBurn = new LinkedList<Integer>();
		Set<Integer> nodesBurned = new HashSet<Integer>(getNodes());
		boolean[] seen = new boolean[getNodes()];

		nodesToBurn.add(w);

		while (!nodesToBurn.isEmpty()) {
			int b = nodesToBurn.poll();

			if (!nodesBurned.contains(b)) {
				nodesBurned.add(b);

				int x = (int) Math.floor(Math.log(1 - rand.nextDouble())
						/ Math.log(1 - p));

				LinkedList<Integer> outs = new LinkedList<Integer>(
						outNeighbors[b]);
				Collections.shuffle(outs); // Forward Burning
				int burned_x = 0;
				while (!outs.isEmpty()) {
					int ww = outs.poll();
					if (burned_x < x && !seen[ww]) {
						nodesToBurn.add(ww);
						burned_x++;
					}
					seen[ww] = true;
				}

				int y = (int) Math.floor(Math.log(1 - rand.nextDouble())
						/ Math.log(1 - r * p));

				LinkedList<Integer> ins = new LinkedList<Integer>(
						inNeighbors[b]);
				Collections.shuffle(ins); // Backward Burning
				int burned_y = 0;
				while (!ins.isEmpty()) {
					int ww = ins.poll();
					if (burned_y < y && !seen[ww]) {
						nodesToBurn.add(ww);
						burned_y++;
					}
					seen[ww] = true;
				}
				// System.out.println(x + "/" + outNeighbors[b].size() + " "
				// + y + "/" + inNeighbors[b].size());
			}
		}

		return nodesBurned;
	}

	/**
	 * 2007 version, v3
	 * https://github.com/snap-stanford/snap/tree/master/examples/forestfire
	 */
	private Set<Integer> versionGeometric3(int w, List<Integer>[] inNeighbors,
			List<Integer>[] outNeighbors, Random rand) {
		Queue<Integer> nodesToBurn = new LinkedList<Integer>();
		Set<Integer> nodesBurned = new HashSet<Integer>(getNodes());
		boolean[] nodesSeen = new boolean[getNodes()];
		boolean checkSeen = false;

		nodesToBurn.add(w);

		while (!nodesToBurn.isEmpty()) {
			int b = nodesToBurn.poll();

			if (nodesBurned.contains(b)) {
				continue;
			}

			nodesBurned.add(b);

			int x = this.drawX(rand);
			int y = this.drawY(rand);

			LinkedList<Integer> outs = new LinkedList<Integer>(outNeighbors[b]);
			Collections.shuffle(outs);
			LinkedList<Integer> ins = new LinkedList<Integer>(inNeighbors[b]);
			Collections.shuffle(ins);

			// Forward Burning
			int burned_x = 0;
			while (!outs.isEmpty()) {
				int w_out = outs.poll();
				if (burned_x < x && (!checkSeen || !nodesSeen[w_out])) {
					nodesToBurn.add(w_out);
					burned_x++;
				}
				nodesSeen[w_out] = true;
			}

			// Backward Burning
			int burned_y = 0;
			while (!ins.isEmpty()) {
				int w_in = ins.poll();
				if (burned_y < y && (!checkSeen || !nodesSeen[w_in])) {
					nodesToBurn.add(w_in);
					burned_y++;
				}
				nodesSeen[w_in] = true;
			}
			// System.out.println(x + "/" + outNeighbors[b].size() + " "
			// + y + "/" + inNeighbors[b].size());
		}

		return nodesBurned;
	}

	private int drawX(Random rand) {
		return (int) Math.floor(Math.log(1 - rand.nextDouble()) / Math.log(p));
	}

	private int drawY(Random rand) {
		return (int) Math.floor(Math.log(1 - rand.nextDouble()) / Math.log(r));
	}
}

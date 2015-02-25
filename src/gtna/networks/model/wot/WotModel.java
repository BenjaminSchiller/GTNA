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
 * WotModelX2.java
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
package gtna.networks.model.wot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author Dirk
 * 
 */
public class WotModel extends Network {

	private Graph g;
	private Node[] nodes;
	private Edges edges;

	private int communitySizes[];
	private int firstNode[];
	private int lastNode[];

	Random rnd;

	private double beta;
	private double b;

	private double m;

	private double d;

	private double bAlpha;

	private double alpha;

	private double beta1;

	private double beta2;

	private double beta3;

	private double cC;

	private double cExp;

	private int cMin;

	private int cMax;

	private int cd;

	private double gamma1;

	private double gamma2;

	private double gamma3;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public WotModel(int nodes, double d, double b, double bAlpha, double alpha, double beta,
			double beta1, double beta2, double beta3, double cC, double cExp, int cMin, int cMax, int cd, double gamma1, double gamma2, double gamma3, Transformation[] t) {
		super("WOTMODEL", nodes, new Parameter[] { new DoubleParameter("d", d),
				new DoubleParameter("B", b),
				new DoubleParameter("BALPHA", bAlpha),
				new DoubleParameter("ALPHA", alpha),
				new DoubleParameter("BETA", beta),
				new DoubleParameter("BETA1", beta1),
				new DoubleParameter("BETA2", beta2),
				new DoubleParameter("BETA3", beta3),
				new DoubleParameter("BETA3", beta3),
				new DoubleParameter("CC", cC),
				new DoubleParameter("CEXP", cExp),
				new DoubleParameter("CMIN", cMin),
				new DoubleParameter("CMAX", cMax),
				new DoubleParameter("CD", cd),
				new DoubleParameter("GAMMA1", gamma1),
				new DoubleParameter("GAMMA2", gamma2),
				new DoubleParameter("GAMMA3", gamma3)}, t);

		this.d = d;
		this.b = b;
		this.bAlpha = bAlpha;
		this.alpha = alpha;
		this.beta = beta;
		this.beta1 = beta1;
		this.beta2 = beta2;
		this.beta3 = beta3;
		this.cC = cC;
		this.cExp = cExp;
		this.cMin =cMin;
		this.cMax = cMax;
		this.cd = cd;
		this.gamma1 = gamma1;
		this.gamma2 = gamma2;
		this.gamma3 = gamma3;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {

		rnd = new Random(System.currentTimeMillis());

		generateCommunitiySizes();

		g = new Graph(this.getDescription());
		nodes = Node.init(this.getNodes(), g);
		edges = new Edges(nodes, (int) (m * getNodes()) + 2 * cd);

		// Create and Copy Communites
		for (int i = 0; i < communitySizes.length; i++) {
			int size = communitySizes[i];
			
			Graph c = new WoTModelSingleCommunity(size, d, b, bAlpha, alpha, beta,
					 beta1, beta2, beta3, null).generate();

			// Copy
			for (Node n : c.getNodes()) {
				int out[] = n.getOutgoingEdges();

				for (int dst : out)
					edges.add(n.getIndex() + firstNode[i], dst + firstNode[i]);

			}

		}
		System.out.println("Communities created!");

		edges.fill();
		g.setNodes(nodes);

		if (cC < 1) {
			// Connect Communites
			for (int i = 0; i < communitySizes.length; i++) {

				// Connect all Communites to largest Component
				int c = 0;

				int a, b;

				if (rnd.nextDouble() < gamma1) {
					a = drawPANode(firstNode[i], lastNode[i]);
					b = drawPANode(firstNode[c], lastNode[c]);
				} else {
					a = drawRandomNode(firstNode[i], lastNode[i]);
					b = drawRandomNode(firstNode[c], lastNode[c]);
				}

				edges.add(a, b);
				edges.add(b, a);

			}

			// Additional links b/w communities
			int addedEdges = 0;
			while (addedEdges < cd * communitySizes.length) {

				int c1, c2;
				if (rnd.nextDouble() < gamma2) {
					c1 = drawPACommunity(-1);
					c2 = drawPACommunity(c1);
				} else {
					c1 = drawRandomCommunity(-1);
					c2 = drawRandomCommunity(c1);
				}

				int n1, n2;

				if (rnd.nextDouble() < gamma3) {
					n1 = drawPANode(firstNode[c1], lastNode[c1]);
					n2 = drawPANode(firstNode[c2], lastNode[c2]);
				} else {
					n1 = drawRandomNode(firstNode[c1], lastNode[c1]);
					n2 = drawRandomNode(firstNode[c2], lastNode[c2]);
				}

				if (!edges.contains(n1, n2) && !edges.contains(n2, n1)) {
					edges.add(n1, n2);
					edges.add(n2, n1);
					addedEdges++;
				}

			}
		}

		edges.fill();
		g.setNodes(nodes);
		return g;
	}

	/*
	 * Generates the community size distribution
	 */
	private void generateCommunitiySizes() {
		double[] sizeDist = generateSizeDistribution(cMin, cMax, cExp);

		List<Integer> sizes = new ArrayList<Integer>();

		int largestC = (int) (cC * getNodes());

		if (largestC > 0)
			sizes.add(largestC);

		if (cC < 1) {
			int nodeSum = largestC;

			while (nodeSum < getNodes()) {
				int size = cMin;
				double z = rnd.nextDouble();

				while (z > sizeDist[size]) {
					size++;
				}

				if ((getNodes() - nodeSum) < size
						|| (getNodes() - nodeSum - size) < cMin)
					size = (getNodes() - nodeSum);

				sizes.add(size);

				nodeSum += size;

			}
		}

		communitySizes = new int[sizes.size()];
		firstNode = new int[sizes.size()];
		lastNode = new int[sizes.size()];

		for (int i = 0; i < sizes.size(); i++) {
			if (i > 0)
				firstNode[i] = firstNode[i - 1] + communitySizes[i - 1];
			communitySizes[i] = sizes.get(i);
			lastNode[i] = firstNode[i] + communitySizes[i] - 1;
		}

	}

	/*
	 * Returns a power law distribution
	 */
	public double[] generateSizeDistribution(int min, int max, double ple) {
		double[] probs = new double[max + 1];
		double norm = 0;
		for (int i = min; i < probs.length; i++) {
			norm = norm + Math.pow(i, -ple);
			probs[i] = norm;
		}

		for (int i = min; i < probs.length; i++) {
			probs[i] = probs[i] / norm;
		}

		return probs;
	}

	/*
	 * Draws a node according to degree
	 */
	private int drawPANode(int a, int b) {
		int sum1 = 0;

		for (int i = a; i <= b; i++)
			sum1 += g.getNode(i).getDegree();

		int zz = rnd.nextInt(sum1);

		int node = a;
		int sum2 = g.getNode(a).getDegree();

		while (zz > sum2) {
			node++;
			sum2 = sum2 + g.getNode(node).getDegree();
		}

		return node;
	}

	/*
	 * Draws a random node
	 */
	private int drawRandomNode(int a, int b) {
		return rnd.nextInt(b - a + 1) + a;
	}

	/*
	 * Draws a community according to size
	 */
	private int drawPACommunity(int excluded) {
		int c = 0;
		do {
			int z = rnd.nextInt(getNodes());

			c = 0;
			int sum = communitySizes[0];

			while (z > sum) {
				c++;
				sum += communitySizes[c];
			}
		} while (c == excluded);

		return c;
	}

	/*
	 * Draws a random community
	 */
	private int drawRandomCommunity(int excluded) {
		int c = 0;
		do {
			c = rnd.nextInt(communitySizes.length);
		} while (c == excluded);

		return c;
	}

}

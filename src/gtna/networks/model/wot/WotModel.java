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

	private int m;
	private double b;
	private int z;
	private int min;
	private int max;
	private double ple;

	private Graph[] communities;

	private Graph g;
	private Node[] nodes;
	private Edges edges;

	private int communitySizes[];
	private int firstNode[];
	private int lastNode[];
	private int bridgeNode[];

	Random rnd;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public WotModel(int nodes, int m, double b, int min, int max, double ple,
			Transformation[] t) {
		super("WOTMODEL", nodes, new Parameter[] { new IntParameter("M", m),
				new DoubleParameter("BIDIRECTIONALITY", b),
				new IntParameter("MIN", min), new IntParameter("MAX", max), new DoubleParameter("PLE", ple) }, t);

		this.b = b;
		this.m = m;
		this.min = min;
		this.max = max;
		this.ple = ple;
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
		
		/*
		for (int size : communitySizes) {
			System.out.println(size);
		}
		
		*/

		g = new Graph(this.getDescription());
		nodes = Node.init(this.getNodes(), g);
		edges = new Edges(nodes, m * getNodes() + 2 * z);

		// Create and Copy Communites
		for (int i = 0; i < communitySizes.length; i++) {
			int size = communitySizes[i];

			Graph c = new WoTModelSingleCommunity(size, 8, 0, 1, 0.52, "C2", null).generate();
			
			ShortestPaths sp = new ShortestPaths();//TODO
			sp.computeData(c, null, null);
			System.out.println("*****" + sp.getConnectivity());
			if (sp.getConnectivity() < 1.0)
				System.exit(0);

			// Copy
			for (Node n : c.getNodes()) {
				int out[] = n.getOutgoingEdges();

				for (int dst : out)
					edges.add(n.getIndex() + firstNode[i], dst + firstNode[i]);

			}
			
			//System.out.println("COPY DONE!");
		}

		edges.fill();
		g.setNodes(nodes);

		// Find Bridgung-Nodes
		
		bridgeNode = new int[communitySizes.length];
		for (int i = 0; i < communitySizes.length; i++) {
			bridgeNode[i] = drawPANode(firstNode[i], lastNode[i]);
			//System.out.println(i +  ": [" + firstNode[i] +  " " +lastNode[i] + "]" + " B: " + bridgeNode[i]);
		}
		
		int bridge = getNodeWithMaxDegree();
		
		// Connect Communites
		for (int i = 0; i < communitySizes.length; i++) {
			
			
			int c = drawPACommunity(i);
			
			int a = bridgeNode[i];
			int b = 0; //bridgeNode[c];
			
			edges.add(a, bridge);
			edges.add(bridge, a);
			
			System.out.println(a + "-> " + b);
			
			//System.out.println(i + " -> " + c + " (" + communitySizes[c] + ")");
		}

		edges.fill();
		g.setNodes(nodes);
		return g;
	}

	private void generateCommunitiySizes() {
		double[] sizeDist = generateSizeDistribution(min, max, ple);

		int nodeSum = 0;

		List<Integer> sizes = new ArrayList<Integer>();

		while (nodeSum < getNodes()) {
			int size = min;
			double sum = sizeDist[min];
			double z = rnd.nextDouble();

			while (z > sizeDist[size]) {
				size++;
			}

			if ((getNodes() - nodeSum) < size || (getNodes() - nodeSum - size) < min)
				size = (getNodes() - nodeSum);
			

			sizes.add(size);

			nodeSum += size;
			
		}
		

		communitySizes = new int[sizes.size()];
		firstNode = new int[sizes.size()];
		lastNode = new int[sizes.size()];
		

		for (int i = 0; i < sizes.size(); i++) {
			if (i > 0)
				firstNode[i] = firstNode[i-1] + communitySizes[i - 1];
			communitySizes[i] = sizes.get(i);
			lastNode[i] = firstNode[i] + communitySizes[i] - 1;
			System.out.println(i + ": " + firstNode[i] + " " + lastNode[i]);
		}

	}

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

	private int drawPANode(int a, int b) {
		int sum1 = 0;

		for (int i = a; i <= b; i++)
			sum1 += g.getNode(a).getDegree();

		int zz = rnd.nextInt(sum1);

		int node = a;
		int sum2 = g.getNode(a).getDegree();

		while (zz > sum2) {
			node++;
			sum2 = sum2 + g.getNode(a).getDegree();
		}

		return node;
	}

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
	
	private int getNodeWithMaxDegree() {
		int n=0;
		int d = g.getNode(0).getDegree();
		
		for (int i = 1; i<getNodes(); i++) {
			if (g.getNode(i).getDegree() > d) {
				n = i;
				d = g.getNode(i).getDegree();
			}
		}
		
		return n;
		
	}

}

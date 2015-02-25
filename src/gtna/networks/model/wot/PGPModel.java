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
 * PGPModel.java
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Dirk
 * 
 *         Implements the PGP graph model as presented in: Capkun, Srdjan,
 *         Levente Buttyán, and Jean-Pierre Hubaux.
 *         "Small worlds in security systems: an analysis of the PGP certificate graph."
 *         Proceedings of the 2002 workshop on New security paradigms. ACM,
 *         2002.
 * 
 */
public class PGPModel extends Network {

	double phi;
	private int[] inDegrees;
	private int[] outDegrees;

	final static String origfolder = "./wot-graphs-original/";

	static Random rnd;

	public PGPModel(int nodes, int[] inDegrees, int[] outDegrees, double phi,
			Transformation[] transformations) {
		super("PGPMODEL", nodes, new Parameter[] { new DoubleParameter("PHI",
				phi) }, transformations);
		this.phi = phi;
		this.inDegrees = inDegrees;
		this.outDegrees = outDegrees;
	}

	public PGPModel(int nodes, double phi, Transformation[] transformations) {
		super("PGPMODEL", nodes, new Parameter[] { new DoubleParameter("PHI",
				phi) }, transformations);
		this.phi = phi;

		ReadableFile wot = new ReadableFile("WoT Original", "wot-original",
				origfolder + nodes + ".gtna", null);
		Graph wotGraph = wot.generate();

		inDegrees = new int[nodes];
		outDegrees = new int[nodes];

		for (int v = 0; v < nodes; v++) {
			inDegrees[v] = wotGraph.getNode(v).getInDegree();
			outDegrees[v] = wotGraph.getNode(v).getOutDegree();
			//System.out.println("v:\t"  + v + "\t" +inDegrees[v] + "\t" + outDegrees[v]);
		}
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

		int noEdges = 0;
		for (int i : inDegrees)
			noEdges += i;
		Edges edges = new Edges(nodes, noEdges);

		int[] inDegree = new int[nodes.length];

		HashMap<Integer, Set<Integer>> outNeighbors = new HashMap<Integer, Set<Integer>>();
		for (int a = 0; a < nodes.length; a++)
			outNeighbors.put(a, new HashSet<Integer>());

		HashMap<Integer, Set<Integer>> inNeighbors = new HashMap<Integer, Set<Integer>>();
		for (int a = 0; a < nodes.length; a++)
			inNeighbors.put(a, new HashSet<Integer>());

		// Create the substrate graph
		for (int v = 0; v < nodes.length; v++) {
			int k = 0;
			for (int i = 1; i <= nodes.length; i++) {
				int u = ((v - i) + nodes.length) % nodes.length;
				if (inDegree[u] <= inDegrees[u]) {
					outNeighbors.get(v).add(u);
					inNeighbors.get(u).add(v);
					inDegree[u]++;
					k++;
				}
				if (k > (outDegrees[v] / 2))
					break;
			}
			if (k==0)
				System.out.println("KKK");
			k = 0;
			for (int i = 1; i <= nodes.length; i++) {
				int w = ((v + i) + nodes.length) % nodes.length;
				if (inDegree[w] <= inDegrees[w]) {
					outNeighbors.get(v).add(w);
					inNeighbors.get(w).add(v);
					inDegree[w]++;
					k++;
				}
				if (k >= (outDegrees[v] / 2))
					break;
			}
		}

		// Rewire graph
		rnd = new Random(System.currentTimeMillis());
		System.out.println("\n\n");

		int noShortcuts = 0;

		HashMap<Integer, Set<Integer>> shortcuts = new HashMap<Integer, Set<Integer>>();
		for (int a = 0; a < nodes.length; a++)
			shortcuts.put(a, new HashSet<Integer>());

		int counter=0;
		
		while (noShortcuts < phi * noEdges) {
			System.out.println(++counter);
			int u=-1;
			int v=-1;
			do {
				// Pick a vertex u at random
				u = rnd.nextInt(nodes.length);

				// Pick a neighbor v			
				v = getRandomElementFromSet(outNeighbors.get(u));
			} while (shortcuts.get(u).contains(v));

			// Delete edge (u,v)
			outNeighbors.get(u).remove(v);
			inNeighbors.get(v).remove(u);

			int w=-1;
			int x=-1;
			do {
				// Choose edge e connected to w
				w = rnd.nextInt(nodes.length);
				x = getRandomElementFromSet(inNeighbors.get(w));

			} while (shortcuts.get(x).contains(w));
			
			// Create Shortcut (u,w)
			outNeighbors.get(u).add(w);
			inNeighbors.get(w).add(u);
			shortcuts.get(u).add(w);

			// Disconnect e from w and connect it to v
			inNeighbors.get(w).remove(x);
			outNeighbors.get(x).remove(w);
			outNeighbors.get(x).add(v);
			inNeighbors.get(v).add(x);

			noShortcuts++;
		}

		for (int a = 0; a < nodes.length; a++) {
			for (int b : outNeighbors.get(a))
				edges.add(a, b);
		}

		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	public static int getRandomElementFromSet(Set<Integer> set) {
		int size = set.size();
		int item = rnd.nextInt(size);
		int i = 0;
		for (Integer value : set) {
			if (i == item)
				return value;
			i = i + 1;
		}
		return -1;
	}

}

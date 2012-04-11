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
 * ArbitaryDegreeSequence.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.randomGraphs;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * @author stef create an undirected graph with a arbitrary degree sequence
 */
public class ArbitraryDegreeSequence extends Network {
	int[] sequence;
	int[] sequenceIn;
	int[] sequenceOut;
	boolean directed;

	/**
	 * 
	 * @param nodes
	 * @param name
	 * @param sequence
	 *            : position i = degree of node i
	 * @param ra
	 * @param t
	 */
	public ArbitraryDegreeSequence(int nodes, String name, int[] sequence,
			Transformation[] t) {
		super("ARBITRARY_DEGREE_SEQUENCE", nodes, new Parameter[] {
				new StringParameter("NAME", name),
				new BooleanParameter("DIRECTED", false) }, t);
		this.sequence = sequence;
		this.directed = false;
	}

	public ArbitraryDegreeSequence(int nodes, String name, int[] sequenceIn,
			int[] sequenceOut, Transformation[] t) {
		super("ARBITRARY_DEGREE_SEQUENCE", nodes, new Parameter[] {
				new StringParameter("NAME", name),
				new BooleanParameter("DIRECTED", true) }, t);
		this.sequenceIn = sequenceIn;
		this.sequenceOut = sequenceOut;
		this.directed = true;
	}

	public ArbitraryDegreeSequence(String name, Graph g, Transformation[] t,
			boolean directed) {
		super("ARBITRARY_DEGREE_SEQUENCE", g.getNodes().length,
				new Parameter[] { new StringParameter("NAME", name),
						new BooleanParameter("DIRECTED", directed) }, t);
		this.directed = directed;
		Node[] nodes = g.getNodes();
		if (directed) {
			this.sequenceIn = new int[nodes.length];
			for (int i = 0; i < nodes.length; i++) {
				this.sequenceIn[i] = nodes[i].getInDegree();
			}
			this.sequenceOut = new int[nodes.length];
			for (int i = 0; i < nodes.length; i++) {
				this.sequenceOut[i] = nodes[i].getOutDegree();
			}
		} else {
			this.sequence = new int[nodes.length];
			for (int i = 0; i < nodes.length; i++) {
				this.sequenceIn[i] = nodes[i].getInDegree();
			}
		}
	}

	/**
	 * @param key
	 * @param nodes
	 * @param configKeys
	 * @param configValues
	 * @param ra
	 * @param t
	 */
	public ArbitraryDegreeSequence(int nodes, Parameter[] parameters,
			int[] sequence, Transformation[] t) {
		super("ARBITRARY_DEGREE_SEQUENCE", nodes, parameters, t);
		this.sequence = sequence;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		if (this.directed) {
			return this.generateDirected();
		} else {
			return this.generateUndirected();
		}

	}

	private Graph generateUndirected() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		int sum = 0;
		for (int j = 0; j < sequence.length; j++) {
			sum = sum + sequence[j];
		}
		Edges edges = new Edges(nodes, sum);
		Vector<Integer> stubs = new Vector<Integer>();
		Arrays.sort(sequence);
		for (int i = 0; i < sequence.length; i++) {
			for (int j = 0; j < sequence[sequence.length - i - 1]; j++) {
				stubs.add(sequence.length - i - 1);
			}
		}
		int k = 0;
		int src, dst;
		Vector<Integer> cStubs = (Vector<Integer>) stubs.clone();
		while (k < 1000 && stubs.size() > 1) {
			src = stubs.remove(0);
			int med = rand.nextInt(stubs.size());
			dst = stubs.remove(med);
			int t = 0;
			while (t < 1000 && src == dst) {
				stubs.add(med, dst);
				t++;
				med = rand.nextInt(stubs.size());
				dst = stubs.remove(med);
			}
			if (src == dst) {
				k++;
				stubs = cStubs;
			} else {
				edges.add(src, dst);
				edges.add(dst, src);
			}
		}
		if (stubs.size() > 0) {
			throw new IllegalArgumentException(
					"Graph construction not possible");
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	private Graph generateDirected() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		int sum = 0;
		for (int j = 0; j < this.sequenceIn.length; j++) {
			sum = sum + this.sequenceIn[j];
		}
		Edges edges = new Edges(nodes, sum);
		Vector<Integer> stubsOut = new Vector<Integer>(sum);
		Vector<Integer> stubsIn = new Vector<Integer>(sum);
		for (int i = 0; i < this.sequenceOut.length; i++) {
			for (int j = 0; j < this.sequenceOut[i]; j++) {
				stubsOut.add(i);
			}
			for (int j = 0; j < this.sequenceIn[i]; j++) {
				stubsIn.add(i);
			}
		}
		if (stubsIn.size() != stubsOut.size()) {
			throw new IllegalArgumentException(
					"Graph construction not possible");
		}
		int src, dst;
		while (stubsIn.size() > 0 && stubsOut.size() > 0) {
			src = stubsOut.remove(stubsOut.size() - 1);
			dst = stubsIn.remove(rand.nextInt(stubsIn.size()));
			edges.add(src, dst);
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

}

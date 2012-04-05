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
 * AribitraryDegreeDistribution.java
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

import gtna.graph.Graph;
import gtna.io.DataReader;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;

/**
 * @author stef create a graph with a arbitrary degree sequence
 */
public class ArbitraryDegreeDistribution extends Network {
	double[] cdf;
	double[] cdfIn;
	double[] cdfOut;
	boolean directed;

	/**
	 * 
	 * @param nodes
	 * @param name
	 * @param distribution
	 *            : P(degree=i)
	 * @param ra
	 * @param t
	 */
	public ArbitraryDegreeDistribution(int nodes, String name,
			double[] distribution, Transformation[] t) {
		super("ARBITRARY_DEGREE_DISTRIBUTION", nodes, new Parameter[] {
				new StringParameter("NAME", name),
				new BooleanParameter("DIRECTED", false) }, t);
		this.cdf = distribution;
		this.directed = false;
	}

	public ArbitraryDegreeDistribution(int nodes, String name,
			double[] distributionIn, double[] distributionOut,
			Transformation[] t) {
		super("ARBITRARY_DEGREE_DISTRIBUTION", nodes, new Parameter[] {
				new StringParameter("NAME", name),
				new BooleanParameter("DIRECTED", true) }, t);
		this.cdfIn = distributionIn;
		this.cdfOut = distributionOut;
		this.directed = true;
	}

	public ArbitraryDegreeDistribution(int nodes, String name, String file,
			Transformation[] t) {
		super("ARBITRARY_DEGREE_DISTRIBUTION", nodes, new Parameter[] {
				new StringParameter("NAME", name),
				new BooleanParameter("DIRECTED", false) }, t);
		this.cdf = DataReader.readDouble(file);
		this.directed = false;
	}

	public ArbitraryDegreeDistribution(int nodes, String name, String fileIn,
			String fileOut, Transformation[] t) {
		super("ARBITRARY_DEGREE_DISTRIBUTION", nodes, new Parameter[] {
				new StringParameter("NAME", name),
				new BooleanParameter("DIRECTED", true) }, t);
		this.cdfIn = DataReader.readDouble(fileIn);
		this.cdfOut = DataReader.readDouble(fileOut);
		this.directed = true;
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
		int[] sequence = new int[this.getNodes()];
		Random rand = new Random(System.currentTimeMillis());
		int sum = 0;
		int k = 0;
		for (int i = 0; i < sequence.length; i++) {
			double bound = rand.nextDouble();
			k = 0;
			while (this.cdf[k] < bound) {
				k++;
			}
			sequence[i] = k;
			sum = sum + k;
		}
		while (sum % 2 == 1) {
			sum = sum - k;
			double bound = rand.nextDouble();
			k = 0;
			while (this.cdf[k] < bound) {
				k++;
			}
			sequence[sequence.length - 1] = k;
			sum = sum + k;
		}

		return (new ArbitraryDegreeSequence(this.getNodes(), this.getName(),
				sequence, null)).generate();
	}

	private Graph generateDirected() {
		int[] sequenceIn = new int[this.getNodes()];
		int[] sequenceOut = new int[this.getNodes()];
		Random rand = new Random(System.currentTimeMillis());
		int sumIn = 0, sumOut = 0;
		int k = 0;
		for (int i = 0; i < sequenceIn.length; i++) {
			double bound = rand.nextDouble();
			k = 0;
			while (this.cdfIn[k] < bound) {
				k++;
			}
			sequenceIn[i] = k;
			sumIn = sumIn + k;

			bound = rand.nextDouble();
			k = 0;
			while (this.cdfOut[k] < bound) {
				k++;
			}
			sequenceOut[i] = k;
			sumOut = sumOut + k;
		}
		while (sumOut > sumIn) {
			boolean out = rand.nextBoolean();
			if (out) {
				int c = rand.nextInt(sequenceIn.length);
				while (sequenceOut[c] < 2) {
					c = rand.nextInt(sequenceIn.length);
				}
				sequenceOut[c]--;
				sumOut--;
			} else {
				int c = rand.nextInt(sequenceIn.length);
				while (sequenceIn[c] >= cdfIn.length - 1) {
					c = rand.nextInt(sequenceIn.length);
				}
				sequenceIn[c]++;
				sumIn++;
			}
		}
		while (sumOut < sumIn) {
			boolean out = rand.nextBoolean();
			if (out) {
				int c = rand.nextInt(sequenceIn.length);
				while (sequenceOut[c] >= cdfOut.length) {
					c = rand.nextInt(sequenceIn.length);
				}
				sequenceOut[c]++;
				sumOut++;
			} else {
				int c = rand.nextInt(sequenceIn.length);
				while (sequenceIn[c] < 2) {
					c = rand.nextInt(sequenceIn.length);
				}
				sequenceIn[c]--;
				sumIn--;
			}
		}
		return (new ArbitraryDegreeSequence(this.getNodes(), this.getName(),
				sequenceIn, sequenceOut, null)).generate();
	}

}

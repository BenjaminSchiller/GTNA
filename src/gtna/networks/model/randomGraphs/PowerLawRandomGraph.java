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
 * ScaleFreeRandomGraph.java
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
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author stef
 * 
 */
public class PowerLawRandomGraph extends Network {
	double[] alpha;
	int[] min, max;
	boolean directed;

	/**
	 * 
	 * @param nodes
	 * @param alpha
	 *            : exponent of degree
	 * @param minDegree
	 *            : minimal degree
	 * @param maxDegree
	 *            : maximal degree
	 * @param ra
	 * @param t
	 */
	public PowerLawRandomGraph(int nodes, double alpha, int minDegree,
			int maxDegree, boolean directed, Transformation[] t) {
		super("POWER_LAW_RANDOM", nodes, new Parameter[] {
				new DoubleParameter("ALPHA", alpha),
				new IntParameter("MINDEG", minDegree),
				new IntParameter("MAXDEG", maxDegree),
				new BooleanParameter("DIRECTED", directed) }, t);
		this.alpha = new double[] { alpha, alpha };
		this.max = new int[] { maxDegree, maxDegree };
		this.min = new int[] { minDegree, minDegree };
		this.directed = directed;

	}

	public PowerLawRandomGraph(int nodes, double alphaOut, double alphaIn,
			int minDegreeOut, int minDegreeIn, int maxDegreeOut,
			int maxDegreeIn, Transformation[] t) {
		super("POWER_LAW_RANDOM", nodes, new Parameter[] {
				new StringParameter("ALPHA", "(" + alphaOut + "," + alphaIn
						+ ")"),
				new StringParameter("MINDEG", "(" + minDegreeOut + ","
						+ minDegreeIn + ")"),
				new StringParameter("MAXDEG", "(" + maxDegreeOut + ","
						+ maxDegreeIn + ")"),
				new BooleanParameter("DIRECTED", true) }, t);
		this.alpha = new double[] { alphaOut, alphaIn };
		this.max = new int[] { maxDegreeOut, maxDegreeOut };
		this.min = new int[] { minDegreeIn, minDegreeIn };
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
		double[] probs = new double[Math.min(this.max[0], this.getNodes()) + 1];
		double norm = 0;
		for (int i = this.min[0]; i < probs.length; i++) {
			norm = norm + Math.pow(i, -alpha[0]);
			probs[i] = norm;
		}
		for (int i = this.min[0]; i < probs.length; i++) {
			probs[i] = probs[i] / norm;
		}
		return (new ArbitraryDegreeDistribution(this.getNodes(),
				this.getName(), probs, null)).generate();
	}

	private Graph generateDirected() {
		double[] probsOut = new double[Math.min(this.max[0], this.getNodes()) + 1];
		double norm = 0;
		for (int i = this.min[0]; i < probsOut.length; i++) {
			norm = norm + Math.pow(i, -alpha[0]);
			probsOut[i] = norm;
		}
		for (int i = this.min[0]; i < probsOut.length; i++) {
			probsOut[i] = probsOut[i] / norm;
		}
		double[] probsIn = new double[Math.min(this.max[1], this.getNodes()) + 1];
		norm = 0;
		for (int i = this.min[1]; i < probsIn.length; i++) {
			norm = norm + Math.pow(i, -alpha[1]);
			probsIn[i] = norm;
		}
		for (int i = this.min[1]; i < probsIn.length; i++) {
			probsIn[i] = probsIn[i] / norm;
		}
		return (new ArbitraryDegreeDistribution(this.getNodes(),
				this.getName(), probsOut, probsIn, null)).generate();
	}

}

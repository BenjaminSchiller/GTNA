/* ===========================================================
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
 * CriticalPoints.java
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
package gtna.metrics.fragmentation;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Timer;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashMap;

/**
 * @author stef
 * 
 */
public class CriticalPointsTheory extends Metric {
	private boolean dir;

	public static enum Selection {
		// different types of node selection
		RANDOM, // random deletion of nodes
		LARGEST, // deletion of nodes with highest degree
		LARGESTOUT, // deletion of nodes with highest out-degree
		LARGESTIN, // deletion of nodes with highest out-degree
		DEGREEBOUND // deletion of edges of nodes so that maximal degree is
					// bounded
	}

	private double p;
	private double deg;
	private Selection selection;

	public CriticalPointsTheory(boolean directed, Selection selection) {
		super("CRITICAL_POINTS", new Parameter[] {
				new BooleanParameter("DIRECTED", directed),
				new StringParameter("TYPE", selection.toString()) });
		this.dir = directed;
		this.selection = selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Node[] nodes = g.getNodes();
		if (this.dir) {
			int maxOut = 0;
			int maxIn = 0;
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i].getInDegree() > maxIn) {
					maxIn = nodes[i].getInDegree();
				}
				if (nodes[i].getOutDegree() > maxOut) {
					maxOut = nodes[i].getOutDegree();
				}
			}
			double[][] dist = new double[maxOut + 1][maxIn + 1];
			for (int i = 0; i < nodes.length; i++) {
				dist[nodes[i].getOutDegree()][nodes[i].getInDegree()]++;
			}
			for (int i = 0; i < dist.length; i++) {
				for (int j = 0; j < dist[i].length; j++) {
					dist[i][j] = dist[i][j] / (double) nodes.length;
				}
			}
			boolean success = false;
			if (this.selection == Selection.RANDOM) {
				success = true;
				double[] r = getCPDirectedRandom(dist);
				this.p = r[0];
				this.deg = r[1];
			}
			if (this.selection == Selection.LARGEST) {
				success = true;
				double[] r = getCPDirectedLargest(dist);
				this.p = r[0];
				this.deg = r[1];
			}
			if (this.selection == Selection.LARGESTOUT) {
				success = true;
				double[] r = getCPDirectedLargestOut(dist);
				this.p = r[0];
				this.deg = r[1];
			}
			if (this.selection == Selection.LARGESTIN) {
				success = true;
				double[] r = getCPDirectedLargestIn(dist);
				this.p = r[0];
				this.deg = r[1];
			}
			if (!success) {
				throw new IllegalArgumentException("Unknown deletion type "
						+ this.selection + " directed: " + this.dir
						+ " in CriticalPointsTheory");
			}
		} else {
			int max = 0;
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i].getOutDegree() > max) {
					max = nodes[i].getOutDegree();
				}
			}
			double[] dd = new double[max + 1];
			for (int i = 0; i < nodes.length; i++) {
				dd[nodes[i].getOutDegree()]++;
			}
			for (int i = 0; i < dd.length; i++) {
				dd[i] = dd[i] / nodes.length;
			}
			double[] res = null;
			if (this.selection == Selection.RANDOM) {
				res = this.getCPUndirectedRandom(dd);
			}
			if (this.selection == Selection.LARGEST) {
				res = this.getCPUndirectedLargest(dd);
			}
			if (this.selection == Selection.DEGREEBOUND) {
				res = this.getCPUndirectedMaxDegree(dd);
			}
			if (res == null) {
				throw new IllegalArgumentException("Unknown deletion type "
						+ this.selection + " directed: " + this.dir
						+ " in CriticalPointsTheory");
			}
			this.p = res[0];
			this.deg = res[1];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single pR = new Single("CRITICAL_POINTS_CP", this.p);
		Single degR = new Single("CRITICAL_POINTS_DEG", this.deg);
		return new Single[] { pR, degR };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	private double[] getCPDirectedRandom(double[][] dist) {
		int degR = dist.length + dist[0].length;
		double[] r = getk0Directed(dist);
		double pR;
		if (r[0] - r[1] <= 0) {
			pR = 0;
		} else {
			pR = (r[0] - r[1]) / r[0];
		}
		return new double[] { pR, degR };
	}

	private double[] getCPUndirectedRandom(double[] dd) {
		double k0 = getk0(dd);
		double r = 1 - 1 / (k0 - 1);
		if (r < 0) {
			return new double[] { 0, dd.length - 1 };
		} else {
			return new double[] { r, dd.length - 1 };
		}
	}

	public static double[] getCPUndirectedLargest(double[] dist) {
		double k0 = getk0(dist);
		double av = 0;
		for (int i = 1; i < dist.length; i++) {
			av = av + dist[i] * i;
		}
		double r = 1 - 1 / (k0 - 1);
		if (r < 0) {
			return new double[] { 0, dist.length - 1 };
		} else {
			double p = 0;
			double[] distN = new double[dist.length];
			double edgef = 0;
			double fpN;
			for (int j = dist.length - 1; j > 0; j--) {
				p = p + dist[j];
				edgef = edgef + dist[j] * j / (av);
				for (int i = 0; i < j; i++) {
					distN[i] = dist[i] / (1 - p);
				}
				for (int i = j; i < dist.length; i++) {
					distN[i] = 0;
				}
				k0 = getk0(distN, j - 1);
				fpN = 1 - 1 / (k0 - 1);
				if (edgef > fpN) {
					return new double[] { p, j };
				}
			}
		}
		return new double[] { 1, 1 };
	}

	public static double[] getCPDirectedLargest(double[][] dist) {
		double av = 0;
		int max = 0;
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[i].length; j++) {
				av = av + i * dist[i][j];
			}
			if (dist[i].length - 1 + i > max) {
				max = dist[i].length - 1 + i;
			}
		}
		double p = 0;
		double out = 0;
		double in = 0;
		double fpN;
		double k0;
		for (int j = max; j > 0; j--) {
			for (int i = 0; i < j; i++) {
				if (i < dist.length && dist[i].length > j - i) {
					p = p + dist[i][j - i];
					out = out + dist[i][j - i] * i / av;
					in = in + dist[i][j - i] * (j - i) / av;
				}
			}

			double[] r = getk0Directed(dist, j, Integer.MAX_VALUE,
					Integer.MAX_VALUE);
			fpN = (1 - out) * (1 - in) * 2 * r[0] - (1 - in) * r[1] - (1 - out)
					* r[2];
			if (fpN < 0) {
				return new double[] { p, j - 1 };
			}

		}
		return new double[] { 1, 1 };
	}

	public static double[] getCPDirectedLargestOut(double[][] dist) {
		double av = 0;
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[i].length; j++) {
				av = av + i * dist[i][j];
			}
		}
		double p = 0;
		double out = 0;
		double in = 0;
		double fpN;
		double k0;
		for (int j = dist.length - 1; j > 0; j--) {
			for (int i = 0; i < dist[j].length; i++) {
				p = p + dist[j][i];
				out = out + dist[j][i] * j / (av);
				in = in + dist[j][i] * i / av;
			}
			double[] r = getk0Directed(dist, Integer.MAX_VALUE, j,
					Integer.MAX_VALUE);
			fpN = (1 - out) * (1 - in) * 2 * r[0] - (1 - in) * r[1] - (1 - out)
					* r[2];
			if (fpN < 0) {
				return new double[] { p, j - 1 };
			}
		}
		return new double[] { 1, 1 };
	}

	public static double[] getCPDirectedLargestIn(double[][] dist) {
		double av = 0;
		int max = 0;
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[i].length; j++) {
				av = av + j * dist[i][j];
			}
			if (dist[i].length - 1 > max) {
				max = dist[i].length - 1;
			}
		}
		double p = 0;
		double out = 0;
		double in = 0;
		double fpN;
		for (int j = max; j > 0; j--) {
			for (int i = 0; i < dist.length; i++) {
				if (j < dist[i].length) {
					p = p + dist[i][j];
					out = out + dist[i][j] * i / (av);
					in = in + dist[i][j] * j / av;
				}
			}
			double[] r = getk0Directed(dist, Integer.MAX_VALUE,
					Integer.MAX_VALUE, j);
			fpN = (1 - out) * (1 - in) * 2 * r[0] - (1 - in) * r[1] - (1 - out)
					* r[2];
			if (fpN < 0) {
				return new double[] { p, j - 1 };
			}
		}
		return new double[] { 1, 1 };
	}

	public static double[] getCPUndirectedMaxDegree(double[] dist) {
		double k0 = getk0(dist);
		double av = 0;
		for (int i = 1; i < dist.length; i++) {
			av = av + dist[i] * i;
		}
		double r = 1 - 1 / (k0 - 1);
		if (r < 0) {
			return new double[] { 0, dist.length - 1 };
		} else {
			double p = 0;
			double[] distN = dist.clone();
			double edgef = 0;
			double fpN;
			for (int j = dist.length - 1; j > 0; j--) {
				p = p + dist[j];
				edgef = edgef + p / (av);
				distN[j - 1] = dist[j - 1] + p;
				for (int i = j; i < dist.length; i++) {
					distN[i] = 0;
				}
				k0 = getk0(distN, j - 1);
				fpN = 1 - 1 / (k0 - 1);
				if (edgef > fpN) {
					return new double[] { p, j };
				}
			}
		}
		return new double[] { 1, 1 };
	}

	private static double getk0(double[] dist, int bound) {
		double firstMoment = 0;
		double secondMoment = 0;
		for (int i = 0; i < Math.min(dist.length, bound + 1); i++) {
			firstMoment = firstMoment + i * dist[i];
			secondMoment = secondMoment + i * i * dist[i];
		}
		return secondMoment / firstMoment;
	}

	private static double getk0(double[] dist) {
		return getk0(dist, dist.length);
	}

	private static double[] getk0Directed(double[][] dist, int bound,
			int boundOut, int boundIn) {
		double jk = 0;
		double j = 0;
		double k = 0;
		for (int i = 1; i < Math.min(Math.min(dist.length, bound), boundOut); i++) {
			for (int i2 = 1; i2 < Math.min(Math.min(dist[i].length, bound - i),
					boundIn); i2++) {
				jk = jk + i * i2 * dist[i][i2];
				j = j + i * dist[i][i2];
				k = k + i2 * dist[i][i2];
			}
		}
		return new double[] { jk, j, k };
	}

	private static double[] getk0Directed(double[][] dist) {
		return getk0Directed(dist, Integer.MAX_VALUE, Integer.MAX_VALUE,
				Integer.MAX_VALUE);
	}

}

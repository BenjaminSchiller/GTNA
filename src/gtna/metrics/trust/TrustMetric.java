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
 * TrustMetric.java
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
package gtna.metrics.trust;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;
import gtna.util.parameter.StringParameter;

/**
 * @author Dirk
 * 
 */
public abstract class TrustMetric extends Metric {

	private Distribution trustedNodesDistribution;
	private Distribution edgesInSubtreeDistribution;

	private int sampleSize;

	private long runtimeAvg;
	private long runtimeTrustedAvg;
	private long runtimeUntrustedAvg;

	private long minRuntime = Long.MAX_VALUE;
	private long maxRuntime = 0;
	private long minRuntimeTrusted = Long.MAX_VALUE;
	private long maxRuntimeTrusted = 0;
	private long minRuntimeUntrusted = Long.MAX_VALUE;
	private long maxRuntimeUntrusted = 0;

	private long runtimeMed;
	private long runtimeTrustedMed;
	private long runtimeUntrustedMed;

	private long runtimeGraphPreparation;

	private Random rnd;
	private boolean computeDistributions;
	private boolean plotMulti;

	public TrustMetric(String metricCode, int sampleSize, boolean computeDistributions,
			boolean plotMulti,
			Parameter[] parameters) {
		super("TRUST_METRIC", ParameterList.append(parameters, new Parameter[] {
				new StringParameter("METRIC_CODE", metricCode),
				new IntParameter("SAMPLE_SIZE", sampleSize) }));

		this.sampleSize = sampleSize;
		this.computeDistributions = computeDistributions;
		this.plotMulti = plotMulti;

		config();

		rnd = new Random(System.currentTimeMillis());
	}

	private void config() {
		if (!computeDistributions)
			plotMulti = false;

		if (!plotMulti) {
			Config.overwrite("TRUST_METRIC_DATA_KEYS", "");
			Config.overwrite("TRUST_METRIC_DATA_PLOTS", "");
		}

		if (!computeDistributions) {
			Config.overwrite(
					"TRUST_METRIC_SINGLES_KEYS",
					"TRUST_METRIC_RUNTIME_GRAPH_PREP, TRUST_METRIC_RUNTIME_AVG, TRUST_METRIC_RUNTIME_TRUSTED_AVG, TRUST_METRIC_RUNTIME_UNTRUSTED_AVG,TRUST_METRIC_RUNTIME_MED, TRUST_METRIC_RUNTIME_TRUSTED_MED, TRUST_METRIC_RUNTIME_UNTRUSTED_MED,TRUST_METRIC_RUNTIME_MAX, TRUST_METRIC_RUNTIME_TRUSTED_MAX, TRUST_METRIC_RUNTIME_UNTRUSTED_MAX,TRUST_METRIC_RUNTIME_MIN, TRUST_METRIC_RUNTIME_TRUSTED_MIN, TRUST_METRIC_RUNTIME_UNTRUSTED_MIN");
			Config.overwrite(
					"TRUST_METRIC_SINGLES_PLOTS",
					"TRUST_METRIC_RUNTIME_GRAPH_PREP, TRUST_METRIC_RUNTIME_AVG, TRUST_METRIC_RUNTIME_TRUSTED_AVG, TRUST_METRIC_RUNTIME_UNTRUSTED_AVG,TRUST_METRIC_RUNTIME_MED, TRUST_METRIC_RUNTIME_TRUSTED_MED, TRUST_METRIC_RUNTIME_UNTRUSTED_MED,TRUST_METRIC_RUNTIME_MAX, TRUST_METRIC_RUNTIME_TRUSTED_MAX, TRUST_METRIC_RUNTIME_UNTRUSTED_MAX,TRUST_METRIC_RUNTIME_MIN, TRUST_METRIC_RUNTIME_TRUSTED_MIN, TRUST_METRIC_RUNTIME_UNTRUSTED_MIN");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		long t0 = System.nanoTime();
		prepareGraph(g);
		long t1 = System.nanoTime();
		runtimeGraphPreparation = t1 - t0;

		if (computeDistributions)
			computeTrustDistributions(g);
		
		computeRuntimes(g);

	}

	private void computeTrustDistributions(Graph g) {

		int[] trustedNodes = new int[sampleSize];
		int[] edgesInSubtree = new int[sampleSize];

		int maxTrustedNodes = 0;
		int maxEdges = 0;

		for (int i = 0; i < sampleSize; i++) {

			Node n = getRandomNode(g);

			prepareNode(n);

			trustedNodes[i] = getNoOfTrustedNodes(n);
			edgesInSubtree[i] = getNoOfEdgesInSubtree(n);

			if (trustedNodes[i] > maxTrustedNodes)
				maxTrustedNodes = trustedNodes[i];
			if (edgesInSubtree[i] > maxEdges)
				maxEdges = edgesInSubtree[i];
		}
		double trustedDist[] = new double[maxTrustedNodes + 1];
		for (int c : trustedNodes)
			trustedDist[c]++;
		for (int i = 0; i < trustedDist.length; i++)
			trustedDist[i] /= sampleSize;

		double subtreeEdgesDist[] = new double[maxEdges + 1];
		for (int c : edgesInSubtree)
			subtreeEdgesDist[c]++;
		for (int i = 0; i < subtreeEdgesDist.length; i++)
			subtreeEdgesDist[i] /= sampleSize;

		trustedNodesDistribution = new Distribution(
				"TRUST_METRIC_TRUSTED_NODES_DISTRIBUTION", trustedDist);

		edgesInSubtreeDistribution = new Distribution(
				"TRUST_METRIC_EDGES_IN_SUBTREE_DISTRIBUTION", subtreeEdgesDist);

	}

	private void computeRuntimes(Graph g) {
		long sumRuntime = 0;
		long sumTrustedRuntime = 0;
		long sumUntrustedRuntime = 0;

		minRuntime = Long.MAX_VALUE;
		maxRuntime = 0;
		minRuntimeTrusted = Long.MAX_VALUE;
		maxRuntimeTrusted = 0;
		minRuntimeUntrusted = Long.MAX_VALUE;
		maxRuntimeUntrusted = 0;

		long[] runtimes = new long[sampleSize];
		long[] runtimesTrustedTemp = new long[sampleSize];
		long[] runtimesUntrustedTemp = new long[sampleSize];

		int countTrusted = 0;
		int countUntrusted = 0;

		for (int i = 0; i < sampleSize; i++) {
			long t0 = System.nanoTime();
			boolean b = computeTrust(getRandomNode(g), getRandomNode(g));
			long t1 = System.nanoTime();

			long t = (t1 - t0);

			runtimes[i] = t;

			sumRuntime += t;

			if (t < minRuntime) {
				minRuntime = t;
			}
			if (t > maxRuntime) {
				maxRuntime = t;
			}

			if (b) {
				countTrusted++;
				sumTrustedRuntime += t;

				runtimesTrustedTemp[countTrusted - 1] = t;

				if (t < minRuntimeTrusted) {
					minRuntimeTrusted = t;
				}
				if (t > maxRuntimeTrusted) {
					maxRuntimeTrusted = t;
				}

			} else {
				countUntrusted++;
				sumUntrustedRuntime += t;

				runtimesUntrustedTemp[countUntrusted - 1] = t;

				if (t < minRuntimeUntrusted) {
					minRuntimeUntrusted = t;
				}
				if (t > maxRuntimeUntrusted) {
					maxRuntimeUntrusted = t;
				}
			}
		}

		runtimeAvg = sumRuntime / (countTrusted + countUntrusted);

		if (countTrusted > 0)
			runtimeTrustedAvg = sumTrustedRuntime / (countTrusted);
		if (countUntrusted > 0)
			runtimeUntrustedAvg = sumUntrustedRuntime / (countUntrusted);

		Arrays.sort(runtimes);
		if (runtimes.length % 2 == 0) {
			runtimeMed = (runtimes[runtimes.length / 2] + runtimes[runtimes.length / 2 - 1]) / 2;

		} else {
			runtimeMed = runtimes[runtimes.length / 2];

		}

		if (countUntrusted > 0) {
			long[] runtimesUntrusted = new long[countUntrusted];
			runtimesUntrusted = Arrays.copyOf(runtimesUntrustedTemp,
					countUntrusted);
			Arrays.sort(runtimesUntrusted);
			if (runtimesUntrusted.length % 2 == 0) {
				runtimeUntrustedMed = (runtimesUntrusted[runtimesUntrusted.length / 2] + runtimesUntrusted[runtimesUntrusted.length / 2 - 1]) / 2;
			} else {
				runtimeUntrustedMed = runtimesUntrusted[runtimesUntrusted.length / 2];
			}
		}

		if (countTrusted > 0) {
			long[] runtimesTrusted = new long[countTrusted];
			runtimesTrusted = Arrays.copyOf(runtimesTrustedTemp, countTrusted);
			Arrays.sort(runtimesTrusted);
			if (runtimesTrusted.length % 2 == 0) {
				runtimeTrustedMed = (runtimesTrusted[runtimesTrusted.length / 2] + runtimesTrusted[runtimesTrusted.length / 2 - 1]) / 2;
			} else {
				runtimeTrustedMed = runtimesTrusted[runtimesTrusted.length / 2];
			}
		}

	}

	private Node getRandomNode(Graph g) {

		return g.getNodes()[rnd.nextInt(g.getNodeCount())];
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		if (plotMulti) {
			success &= DataWriter.writeWithoutIndex(
					this.trustedNodesDistribution.getDistribution(),
					"TRUST_METRIC_TRUSTED_NODES_DISTRIBUTION", folder);
			success &= DataWriter.writeWithoutIndex(
					this.trustedNodesDistribution.getCdf(),
					"TRUST_METRIC_TRUSTED_NODES_DISTRIBUTION_CDF", folder);
			success &= DataWriter.writeWithoutIndex(
					this.edgesInSubtreeDistribution.getDistribution(),
					"TRUST_METRIC_EDGES_IN_SUBTREE_DISTRIBUTION", folder);
			success &= DataWriter.writeWithoutIndex(
					this.edgesInSubtreeDistribution.getCdf(),
					"TRUST_METRIC_EDGES_IN_SUBTREE_DISTRIBUTION_CDF", folder);
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#readData(java.lang.String)
	 */
	@Override
	public boolean readData(String folder) {
		if (plotMulti) {
			trustedNodesDistribution = new Distribution(
					"TRUST_METRIC_TRUSTED_NODES_DISTRIBUTION",
					readDistribution(folder,
							"TRUST_METRIC_TRUSTED_NODES_DISTRIBUTION"));
			edgesInSubtreeDistribution = new Distribution(
					"TRUST_METRIC_EDGES_IN_SUBTREE_DISTRIBUTION",
					readDistribution(folder,
							"TRUST_METRIC_EDGES_IN_SUBTREE_DISTRIBUTION"));

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single trustedNodesMin, trustedNodesMed, trustedNodesAvg, trustedNodesMax, edgesMin, edgesMed, edgesAvg, edgesMax;

		Single runtimeAvg = new Single("TRUST_METRIC_RUNTIME_AVG",
				this.runtimeAvg);
		Single runtimeTrustedAvg = new Single(
				"TRUST_METRIC_RUNTIME_TRUSTED_AVG", this.runtimeTrustedAvg);
		Single runtimeUntrustedAvg = new Single(
				"TRUST_METRIC_RUNTIME_UNTRUSTED_AVG", this.runtimeUntrustedAvg);

		Single runtimeMed = new Single("TRUST_METRIC_RUNTIME_MED",
				this.runtimeMed);
		Single runtimeTrustedMed = new Single(
				"TRUST_METRIC_RUNTIME_TRUSTED_MED", this.runtimeTrustedMed);
		Single runtimeUntrustedMed = new Single(
				"TRUST_METRIC_RUNTIME_UNTRUSTED_MED", this.runtimeUntrustedMed);

		Single runtimeMin = new Single("TRUST_METRIC_RUNTIME_MIN",
				this.minRuntime);
		Single runtimeMax = new Single("TRUST_METRIC_RUNTIME_MAX",
				this.maxRuntime);

		Single runtimeTrustedMin = new Single(
				"TRUST_METRIC_RUNTIME_TRUSTED_MIN", this.minRuntimeTrusted);
		Single runtimeTrustedMax = new Single(
				"TRUST_METRIC_RUNTIME_TRUSTED_MAX", this.maxRuntimeTrusted);

		Single runtimeUntrustedMin = new Single(
				"TRUST_METRIC_RUNTIME_UNTRUSTED_MIN", this.minRuntimeUntrusted);
		Single runtimeUntrustedMax = new Single(
				"TRUST_METRIC_RUNTIME_UNTRUSTED_MAX", this.maxRuntimeUntrusted);

		Single runtimeGraphPreparation = new Single(
				"TRUST_METRIC_RUNTIME_GRAPH_PREP", this.runtimeGraphPreparation);

		if (computeDistributions) {
			trustedNodesMin = new Single("TRUST_METRIC_TRUSTED_NODES_MIN",
					this.trustedNodesDistribution.getMin());
			trustedNodesMed = new Single("TRUST_METRIC_TRUSTED_NODES_MED",
					this.trustedNodesDistribution.getMedian());
			trustedNodesAvg = new Single("TRUST_METRIC_TRUSTED_NODES_AVG",
					this.trustedNodesDistribution.getAverage());
			trustedNodesMax = new Single("TRUST_METRIC_TRUSTED_NODES_MAX",
					this.trustedNodesDistribution.getMax());

			edgesMin = new Single("TRUST_METRIC_EDGES_IN_SUBTREE_MIN",
					this.edgesInSubtreeDistribution.getMin());
			edgesMed = new Single("TRUST_METRIC_EDGES_IN_SUBTREE_MED",
					this.edgesInSubtreeDistribution.getMedian());
			edgesAvg = new Single("TRUST_METRIC_EDGES_IN_SUBTREE_AVG",
					this.edgesInSubtreeDistribution.getAverage());
			edgesMax = new Single("TRUST_METRIC_EDGES_IN_SUBTREE_MAX",
					this.edgesInSubtreeDistribution.getMax());

			return new Single[] { trustedNodesMin, trustedNodesMed,
					trustedNodesAvg, trustedNodesMax, runtimeAvg,
					runtimeTrustedAvg, runtimeUntrustedAvg, runtimeMed,
					runtimeTrustedMed, runtimeUntrustedMed, runtimeMin,
					runtimeTrustedMin, runtimeUntrustedMin, runtimeMax,
					runtimeTrustedMax, runtimeUntrustedMax, edgesMin, edgesMed,
					edgesAvg, edgesMax, runtimeGraphPreparation };

		} else {
			return new Single[] { runtimeAvg, runtimeTrustedAvg,
					runtimeUntrustedAvg, runtimeMed, runtimeTrustedMed,
					runtimeUntrustedMed, runtimeMin, runtimeTrustedMin,
					runtimeUntrustedMin, runtimeMax, runtimeTrustedMax,
					runtimeUntrustedMax, runtimeGraphPreparation };
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getDistributions()
	 */
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] { trustedNodesDistribution,
				edgesInSubtreeDistribution };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getNodeValueLists()
	 */
	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[0];
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	public abstract void prepareGraph(Graph g);

	public abstract void prepareNode(Node n);

	public abstract int getNoOfTrustedNodes(Node n);

	public abstract int getNoOfEdgesInSubtree(Node n);

	public abstract boolean computeTrust(Node n1, Node n2);

}

/*
 * ===========================================================
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
 * Routing.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.metrics.routing;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.Timer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

// TODO reimplement Routing
public class Routing extends Metric {
	private Distribution hopDistribution;

	private Distribution hopDistributionAbsolute;

	private Route[] routes;

	private double[] betweennessCentrality;

	private Timer runtime;

	public Routing() {
		super("ROUTING");
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	@Override
	public void computeData(Graph graph, Network network,
			HashMap<String, Metric> metrics) {
		// FIXME add routingAlgorithm as parameter to Routing metric
		// RoutingAlgorithm ra = network.routingAlgorithm();
		RoutingAlgorithm ra = null;
		if (ra == null || !ra.applicable(graph)) {
			this.initEmpty();
			return;
		}
		this.runtime = new Timer();
		ra.preprocess(graph);
		int times = Config.getInt("ROUTING_ROUTES_PER_NODE");
		Random rand = new Random();
		this.routes = new Route[graph.getNodes().length * times];
		// int index = 0;
		// for (Node start : graph.getNodes()) {
		// for (int i = 0; i < times; i++) {
		// this.routes[index++] = ra.routeToRandomTarget(graph,
		// start.getIndex(), rand);
		// }
		// }
		RoutingThread[] threads = new RoutingThread[Config
				.getInt("PARALLEL_ROUTINGS")];
		for (int i = 0; i < threads.length; i++) {
			int start = graph.getNodes().length / threads.length * i;
			int end = graph.getNodes().length / threads.length * (i + 1) - 1;
			if (i == threads.length - 1) {
				end = graph.getNodes().length - 1;
			}
			threads[i] = new RoutingThread(start, end, times, graph, ra, rand);
			threads[i].start();
		}
		int index = 0;
		for (RoutingThread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Route r : t.getRoutes()) {
				this.routes[index++] = r;
			}
		}

		this.hopDistribution = this.computeHopDistribution();
		this.hopDistributionAbsolute = this.computeHopDistributionAbsolute();
		this.betweennessCentrality = this.computeBetweennessCentrality(graph
				.getNodes().length);
		this.runtime.end();
	}

	private double[] computeBetweennessCentrality(int nodes) {
		double[] bc = new double[nodes];
		for (Route route : this.routes) {
			for (int i = 1; i < route.getRoute().length - 1; i++) {
				bc[route.getRoute()[i]]++;
			}
		}
		Arrays.sort(bc);
		return bc;
	}

	private Distribution computeHopDistribution() {
		long[] hops = new long[1];
		long counter = 0;
		for (Route route : this.routes) {
			if (route.isSuccessful()) {
				hops = this.inc(hops, route.getHops());
				counter++;
			}
		}
		return new Distribution(hops, counter);
	}

	private Distribution computeHopDistributionAbsolute() {
		long[] hops = new long[1];
		for (Route route : this.routes) {
			if (route.isSuccessful()) {
				hops = this.inc(hops, route.getHops());
			}
		}
		return new Distribution(hops, this.routes.length);
	}

	private long[] inc(long[] values, int index) {
		try {
			values[index]++;
			return values;
		} catch (ArrayIndexOutOfBoundsException e) {
			long[] valuesNew = new long[index + 1];
			System.arraycopy(values, 0, valuesNew, 0, values.length);
			valuesNew[index] = 1;
			return valuesNew;
		}
	}

	public Route[] getRoutes() {
		return this.routes;
	}

	private void initEmpty() {
		this.hopDistribution = new Distribution(new double[] { 0 });
		this.hopDistributionAbsolute = new Distribution(new double[] { 0 });
		this.betweennessCentrality = new double[] { 0 };
		this.routes = new Route[0];
		this.runtime = new Timer();
		this.runtime.end();
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.hopDistribution.getDistribution(),
				"ROUTING_HOP_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.hopDistribution.getCdf(),
				"ROUTING_HOP_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.hopDistributionAbsolute.getDistribution(),
				"ROUTING_HOP_DISTRIBUTION_ABSOLUTE", folder);
		success &= DataWriter.writeWithIndex(
				this.hopDistributionAbsolute.getCdf(),
				"ROUTING_HOP_DISTRIBUTION_ABSOLUTE_CDF", folder);
		success &= DataWriter.writeWithIndex(this.betweennessCentrality,
				"ROUTING_BETWEENNESS_CENTRALITY", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single averageHops = new Single("ROUTING_HOPS_AVG",
				this.hopDistribution.getAverage());
		Single medianHops = new Single("ROUTING_HOPS_MED",
				this.hopDistribution.getMedian());
		Single maximumHops = new Single("ROUTING_HOPS_MAX",
				this.hopDistribution.getMax());
		double[] cdf = this.hopDistributionAbsolute.getCdf();
		Single successRate = new Single("ROUTING_SUCCESS_RATE",
				cdf[cdf.length - 1]);
		Single failureRate = new Single("ROUTING_FAILURE_RATE",
				1 - cdf[cdf.length - 1]);
		Single runtime = new Single("ROUTING_RUNTIME",
				this.runtime.getRuntime());
		return new Single[] { averageHops, medianHops, maximumHops,
				successRate, failureRate, runtime };
	}

	private class RoutingThread extends Thread {
		private int start;

		private int end;

		private int times;

		private Graph graph;

		private RoutingAlgorithm ra;

		private Random rand;

		private Route[] routes;

		private RoutingThread(int start, int end, int times, Graph graph,
				RoutingAlgorithm ra, Random rand) {
			this.start = start;
			this.end = end;
			this.times = times;
			this.graph = graph;
			this.ra = ra;
			this.rand = rand;
			this.routes = new Route[(end - start + 1) * times];
		}

		public void run() {
			int index = 0;
			for (int i = this.start; i <= this.end; i++) {
				for (int j = 0; j < this.times; j++) {
					this.routes[index++] = ra.routeToRandomTarget(graph, i,
							rand);
				}
			}
		}

		/**
		 * @return the routes
		 */
		public Route[] getRoutes() {
			return this.routes;
		}

		public String toString() {
			return this.start + " => " + this.end;
		}
	}
}

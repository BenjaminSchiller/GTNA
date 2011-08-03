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
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.Timer;

import java.util.HashMap;
import java.util.Random;

// TODO reimplement Routing
public class Routing extends MetricImpl implements Metric {
	private Distribution hopDistribution;

	private Distribution hopDistributionAbsolute;

	private Route[] routes;

	private Timer runtime;

	public Routing() {
		super("R");
	}

	@Override
	public void computeData(Graph graph, Network network,
			HashMap<String, Metric> metrics) {
		this.runtime = new Timer();
		RoutingAlgorithm ra = network.routingAlgorithm();
		if (ra == null || !ra.applicable(graph)) {
			this.initEmpty();
			return;
		}
		ra.preprocess(graph);
		int times = Config.getInt("R_ROUTES_PER_NODE");
		Random rand = new Random();
		this.routes = new Route[graph.getNodes().length * times];
		int index = 0;
		for (Node start : graph.getNodes()) {
			for (int i = 0; i < times; i++) {
				this.routes[index++] = ra.routeToRandomTarget(graph,
						start.getIndex(), rand);
				// if (this.routes[index - 1].isSuccessful()) {
				// System.out.println(this.routes[index - 1].getHops() + " / "
				// + this.routes[index - 1].getRoute().length);
				// }else{
				// System.out.println("ewrgo8wzidghiowdhghwdgi");
				// }
			}
		}
		this.hopDistribution = this.computeHopDistribution();
		this.hopDistributionAbsolute = this.computeHopDistributionAbsolute();
		this.runtime.end();
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

	private void initEmpty() {
		this.hopDistribution = new Distribution(new double[] { 0 });
		this.hopDistributionAbsolute = new Distribution(new double[] { 0 });
		this.routes = new Route[0];
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.hopDistribution.getDistribution(), "R_HOP_DISTRIBUTION",
				folder);
		success &= DataWriter.writeWithIndex(this.hopDistribution.getCdf(),
				"R_HOP_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.hopDistributionAbsolute.getDistribution(),
				"R_HOP_DISTRIBUTION_ABSOLUTE", folder);
		success &= DataWriter.writeWithIndex(
				this.hopDistributionAbsolute.getCdf(),
				"R_HOP_DISTRIBUTION_ABSOLUTE_CDF", folder);
		return success;
	}

	@Override
	public Value[] getValues() {
		Value averageHops = new Value("R_HOPS_AVG",
				this.hopDistribution.getAverage());
		Value medianHops = new Value("R_HOPS_MED",
				this.hopDistribution.getMedian());
		Value maximumHops = new Value("R_HOPS_MAX",
				this.hopDistribution.getMax());
		double[] cdf = this.hopDistributionAbsolute.getCdf();
		Value successRate = new Value("R_SUCCESS_RATE", cdf[cdf.length - 1]);
		Value failureRate = new Value("R_FAILURE_RATE", 1 - cdf[cdf.length - 1]);
		Value runtime = new Value("R_RUNTIME", this.runtime.getRuntime());
		return new Value[] { averageHops, medianHops, maximumHops, successRate,
				failureRate, runtime };
	}
}

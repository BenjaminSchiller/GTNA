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
 * 2014-02-05: readData, getDistributions, getNodeValueLists (Tim Grube)
 */
package gtna.metrics.routing;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.id.Identifier;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.selection.source.ConsecutiveSourceSelection;
import gtna.routing.selection.source.SourceSelection;
import gtna.routing.selection.target.RepresentativeIdTargetSelection;
import gtna.routing.selection.target.TargetSelection;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterListParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Routing extends Metric {

	private RoutingAlgorithm ra;
	private SourceSelection sourceSelection;
	private TargetSelection targetSelection;

	private Route[] routes;

	private Distribution hopDistribution;
	private Distribution hopDistributionAbsolute;

	private NodeValueList betweennessCentrality;

	private double successRate;
	private double failureRate;

	public Routing(RoutingAlgorithm ra, SourceSelection sourceSelection,
			TargetSelection targetSelection) {
		super("ROUTING",
				new Parameter[] {
						new ParameterListParameter("ROUTING_ALGORITHM", ra),
						new ParameterListParameter("SOURCE_SELECTION",
								sourceSelection),
						new ParameterListParameter("TARGET_SELECTION",
								targetSelection) });

		this.ra = ra;
		this.sourceSelection = sourceSelection;
		this.targetSelection = targetSelection;

		this.init();
	}

	public Routing(RoutingAlgorithm ra) {
		super("ROUTING", new Parameter[] { new ParameterListParameter(
				"ROUTING_ALGORITHM", ra) });

		this.ra = ra;
		this.sourceSelection = new ConsecutiveSourceSelection();
		this.targetSelection = new RepresentativeIdTargetSelection();

		this.init();
	}

	private void init() {
		this.hopDistribution = new Distribution("ROUTING_HOP_DISTRIBUTION", new double[] { -1 });
		this.hopDistributionAbsolute = new Distribution("ROUTING_HOP_DISTRIBUTION_ABSOLUTE", new double[] { -1 });
		this.betweennessCentrality = new NodeValueList("ROUTING_BETWEENNESS_CENTRALITIY", new double[] { -1 });
		this.routes = new Route[0];
		this.successRate = -1;
		this.failureRate = -1;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return this.ra.applicable(g) && this.sourceSelection.applicable(g)
				&& this.targetSelection.applicable(g);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void computeData(Graph graph, Network network,
			HashMap<String, Metric> metrics) {
		this.ra.preprocess(graph);
		this.sourceSelection.init(graph);
		this.targetSelection.init(graph);

		Random rand = new Random();
		int routesPerNode = Config.getInt("ROUTING_ROUTES_PER_NODE");
		this.routes = new Route[graph.getNodes().length * routesPerNode];
		int index = 0;

		// int parallel = Config.getInt("PARALLEL_ROUTINGS");
		// if (parallel == 1) {
		for (int n = 0; n < graph.getNodeCount(); n++) {
			for (int i = 0; i < routesPerNode; i++) {
				int start = this.sourceSelection.getNextSource();
				Identifier target = this.targetSelection.getNextTarget();
				Route r = this.ra.routeToTarget(graph, start, target, rand);
				this.routes[index++] = r;
			}
		}
		// } else {
		// System.err
		// .println("parallelization in routing not supported right now");
		// RoutingThread[] threads = new RoutingThread[parallel];
		// for (int i = 0; i < threads.length; i++) {
		// int start = graph.getNodes().length / threads.length * i;
		// int end = graph.getNodes().length / threads.length * (i + 1)
		// - 1;
		// if (i == threads.length - 1) {
		// end = graph.getNodes().length - 1;
		// }
		// threads[i] = new RoutingThread(start, end, routesPerNode,
		// graph, ra, rand);
		// threads[i].start();
		// }
		// index = 0;
		// for (RoutingThread t : threads) {
		// try {
		// t.join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// for (Route r : t.getRoutes()) {
		// this.routes[index++] = r;
		// }
		// }
		// }

		this.hopDistribution = this.computeHopDistribution();
		this.hopDistributionAbsolute = this.computeHopDistributionAbsolute();
		this.betweennessCentrality = this.computeBetweennessCentrality(graph
				.getNodes().length);

		this.successRate = this.computeSuccessRate();
		this.failureRate = 1 - this.successRate;
	}

	private double computeSuccessRate() {
		int success = 0;
		for (Route route : this.routes) {
			if (route.isSuccessful()) {
				success++;
			}
		}
		return (double) success / (double) this.routes.length;
	}

	private NodeValueList computeBetweennessCentrality(int nodes) {
		double[] bc = new double[nodes];
		for (Route route : this.routes) {
			for (int i = 1; i < route.getRoute().length - 1; i++) {
				bc[route.getRoute()[i]]++;
			}
		}
		Arrays.sort(bc);
		for (int i = 0; i < bc.length; i++) {
			bc[i] /= (double) this.routes.length;
		}
		return new NodeValueList("ROUTING_BETWEENNESS_CENTRALITIY", bc);
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
		return new Distribution("ROUTING_HOP_DISTRIBUTION", hops, counter);
	}

	private Distribution computeHopDistributionAbsolute() {
		long[] hops = new long[1];
		for (Route route : this.routes) {
			if (route.isSuccessful()) {
				hops = this.inc(hops, route.getHops());
			}
		}
		return new Distribution("ROUTING_HOP_DISTRIBUTION_ABSOLUTE", hops, this.routes.length);
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

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(
				this.hopDistribution.getDistribution(),
				"ROUTING_HOP_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(this.hopDistribution.getCdf(),
				"ROUTING_HOP_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithoutIndex(
				this.hopDistributionAbsolute.getDistribution(),
				"ROUTING_HOP_DISTRIBUTION_ABSOLUTE", folder);
		success &= DataWriter.writeWithoutIndex(
				this.hopDistributionAbsolute.getCdf(),
				"ROUTING_HOP_DISTRIBUTION_ABSOLUTE_CDF", folder);
		success &= DataWriter.writeWithIndex(this.betweennessCentrality.getValues(),
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

		Single successRate = new Single("ROUTING_SUCCESS_RATE",
				this.successRate);
		Single failureRate = new Single("ROUTING_FAILURE_RATE",
				this.failureRate);

		return new Single[] { averageHops, medianHops, maximumHops,
				successRate, failureRate };
	}
	
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] {hopDistribution, hopDistributionAbsolute};
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {betweennessCentrality};
	}
	
	
	@Override
	public boolean readData(String folder){
		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
				if("ROUTING_SUCCESS_RATE".equals(single[0])){
					this.successRate = Double.valueOf(single[1]);
				} else if("ROUTING_FAILURE_RATE".equals(single[0])){
					this.failureRate = Double.valueOf(single[1]);
				} 
			}
		}
		
		
		/* DISTRIBUTIONS */
		
		hopDistribution = new Distribution("ROUTING_HOP_DISTRIBUTION", readDistribution(folder, "ROUTING_HOP_DISTRIBUTION"));
		hopDistributionAbsolute = new Distribution("ROUTING_HOP_DISTRIBUTION_ABSOLUTE", readDistribution(folder, "ROUTING_HOP_DISTRIBUTION_ABSOLUTE"));
		
		/* Node-Value List */
		betweennessCentrality = new NodeValueList("ROUTING_BETWEENNESS_CENTRALITIY", readDistribution(folder, "ROUTING_BETWEENNESS_CENTRALITY"));
		
		
		return true;
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
					// this.routes[index++] = ra.routeToRandomTarget(graph, i,
					// rand);
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

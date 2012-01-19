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
 * Routing_HopDegreeDistribution.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.routing.Route;
import gtna.util.Config;
import gtna.util.Distribution;

import java.util.ArrayList;
import java.util.HashMap;

public class Routing_HopDegreeDistribution extends MetricImpl implements Metric {
	private int hopSteps;
	private Distribution hopDegree;

	public Routing_HopDegreeDistribution() {
		super("ROUTING_HOPDEGREEDISTRIBUTION");
	}

	@Override
	public void computeData(Graph graph, Network network, HashMap<String, Metric> metrics) {
		/*
		 * First: check whether routing was already applied
		 */
		if (!metrics.containsKey("ROUTING")) {
			System.err.println("There is no metric called ROUTING -- cannot evaluate ROUTING_HOPDEGREEDISTRIBUTION");
			this.initEmpty();
			return;
		}
		if (!(metrics.get("ROUTING") instanceof Routing)) {
			System.err
					.println("There is a metric called ROUTING that does not contain routing -- cannot evaluate ROUTING_HOPDEGREEDISTRIBUTION");
			this.initEmpty();
			return;
		}
		Routing routing = (Routing) metrics.get("ROUTING");

		hopSteps = Config.getInt("ROUTING_HOPDEGREEDISTRIBUTION_HOPS");

		Route[] routes = routing.getRoutes();
		long[][] degrees = new long[hopSteps + 1][];
		for (int i = 0; i <= hopSteps; i++) {
			degrees[i] = new long[1];
		}

		int tempDegree;
		int[] hops;
		int counter = 0;
		for (Route singleRoute : routes) {
			hops = singleRoute.getRoute();
			for (int i = 0; i <= hopSteps; i++) {
				if (i > (hops.length - 1)) {
					continue;
				}
				tempDegree = graph.getNode(hops[i]).getDegree();
				degrees[i] = inc(degrees[i], tempDegree);
				if (i == hopSteps)
					counter++;
			}
		}
		this.hopDegree = new Distribution(degrees[hopSteps], counter);
	}

	private void initEmpty() {
		this.hopDegree = new Distribution(new double[] { 0 });
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

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.hopDegree.getDistribution(),
				"ROUTING_HOPDEGREEDISTRIBUTION_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.hopDegree.getCdf(), "ROUTING_HOPDEGREEDISTRIBUTION_DISTRIBUTION_CDF",
				folder);
		return success;
	}

	@Override
	public Value[] getValues() {
		Value averageHopDegree = new Value("ROUTING_HOPDEGREEDISTRIBUTION_AVG", this.hopDegree.getAverage());
		Value medianHopDegree = new Value("ROUTING_HOPDEGREEDISTRIBUTION_MED", this.hopDegree.getMedian());
		Value maximumHopDegree = new Value("ROUTING_HOPDEGREEDISTRIBUTION_MAX", this.hopDegree.getMax());
		Value minimumHopDegree = new Value("ROUTING_HOPDEGREEDISTRIBUTION_MIN", this.hopDegree.getMin());
		return new Value[] { averageHopDegree, medianHopDegree, maximumHopDegree, minimumHopDegree };
	}
}

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
package gtna.metrics.routing;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.routing.Route;
import gtna.util.Config;
import gtna.util.Distribution;

import java.util.HashMap;

public class Routing_HopDegreeDistribution extends Metric {
	private int hopSteps;
	private Distribution[] hopDegree;

	public Routing_HopDegreeDistribution() {
		super("ROUTING_HOPDEGREEDISTRIBUTION");
		initConfig();
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	@Override
	public void computeData(Graph graph, Network network,
			HashMap<String, Metric> metrics) {
		initConfig();

		/*
		 * First: check whether routing was already applied
		 */
		if (!metrics.containsKey("ROUTING")) {
			System.err
					.println("There is no metric called ROUTING -- cannot evaluate ROUTING_HOPDEGREEDISTRIBUTION");
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

		Route[] routes = routing.getRoutes();
		long[][] degrees = new long[hopSteps + 1][];
		int[] counter = new int[hopSteps + 1];
		for (int i = 0; i <= hopSteps; i++) {
			degrees[i] = new long[] { 0 };
			counter[i] = 0;
		}

		int tempDegree;
		int[] hops;

		for (Route singleRoute : routes) {
			hops = singleRoute.getRoute();
			for (int i = 0; (i <= hopSteps && i < hops.length); i++) {
				tempDegree = graph.getNode(hops[i]).getOutDegree();
				degrees[i] = inc(degrees[i], tempDegree);
				counter[i]++;
			}
		}

		this.hopDegree = new Distribution[hopSteps + 1];
		for (int i = 0; i <= hopSteps; i++) {
			this.hopDegree[i] = new Distribution(degrees[i], counter[i]);
		}
	}

	private void initConfig() {
		hopSteps = Config.getInt("ROUTING_HOPDEGREEDISTRIBUTION_HOPS");
		String formerDataKeys = Config
				.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DATA_KEYS");
		String formerDataPlots = Config
				.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DATA_PLOTS");
		if (!formerDataKeys.contains("_HOP_" + hopSteps)) {
			String newDataKeys = "";
			String newDataPlots = "";
			for (int i = 0; i <= hopSteps; i++) {
				if (formerDataKeys.contains("_HOP_" + i)) {
					throw new RuntimeException(
							"Caught doubled initialization of RHDD");
				}
				if (i > 0) {
					newDataKeys += ", ";
					newDataPlots += ", ";
				}
				newDataKeys += formerDataKeys
						.replace(
								"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION",
								"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_"
										+ i);
				newDataPlots += formerDataPlots
						.replace(
								"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION",
								"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_"
										+ i);

				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_DATA_NAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_DATA_NAME"));
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_DATA_FILENAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_DATA_FILENAME")
								+ "-" + i);
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_CDF_DATA_NAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_DATA_NAME"));
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_CDF_DATA_FILENAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_CDF_DATA_FILENAME")
								+ "-" + i);
				Config.overwrite(
						"ROUTING_HOPDEGREEDISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_CDF_DATA_IS_CDF",
						Config.get("ROUTING_HOPDEGREEDISTRIBUTION_DISTRIBUTION_CDF_DATA_IS_CDF"));

				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_PLOT_DATA",
						Config.get(
								"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_PLOT_DATA")
								.replace(
										"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION",
										"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_"
												+ i));
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_PLOT_FILENAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_PLOT_FILENAME")
								+ "-" + i);
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_PLOT_TITLE",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_PLOT_TITLE")
								+ " after " + i + " hops");
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_PLOT_X",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_PLOT_X"));
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_PLOT_Y",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_PLOT_Y"));

				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_CDF_PLOT_DATA",
						Config.get(
								"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_CDF_PLOT_DATA")
								.replace(
										"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION",
										"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_"
												+ i));
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_CDF_PLOT_FILENAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_CDF_PLOT_FILENAME")
								+ "-" + i);
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_CDF_PLOT_TITLE",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_CDF_PLOT_TITLE")
								+ " after " + i + " hops");
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_CDF_PLOT_X",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_CDF_PLOT_X"));
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
								+ "_CDF_PLOT_Y",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_CDF_PLOT_Y"));

				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_" + i
								+ "_AVG_SINGLE_NAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_AVG_SINGLE_NAME")
								+ "-" + i);
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_" + i
								+ "_MED_SINGLE_NAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_MED_SINGLE_NAME")
								+ "-" + i);
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_" + i
								+ "_MAX_SINGLE_NAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_MAX_SINGLE_NAME")
								+ "-" + i);
				Config.overwrite(
						"ROUTING_HOP_DEGREE_DISTRIBUTION_" + i
								+ "_MIN_SINGLE_NAME",
						Config.get("ROUTING_HOP_DEGREE_DISTRIBUTION_MIN_SINGLE_NAME")
								+ "-" + i);
			}
			Config.overwrite("ROUTING_HOP_DEGREE_DISTRIBUTION_DATA_KEYS",
					newDataKeys);
			Config.overwrite("ROUTING_HOP_DEGREE_DISTRIBUTION_DATA_PLOTS",
					newDataPlots);
		}
	}

	private void initEmpty() {
		this.hopSteps = -1;
		this.hopDegree = new Distribution[hopSteps + 1];
		for (int i = 0; i <= hopSteps; i++) {
			this.hopDegree[i] = new Distribution(new double[] { 0 });
		}
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
		for (int i = 0; i <= hopSteps; i++) {
			success &= DataWriter.writeWithIndex(
					this.hopDegree[i].getDistribution(),
					"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i,
					folder);
			success &= DataWriter.writeWithIndex(this.hopDegree[i].getCdf(),
					"ROUTING_HOP_DEGREE_DISTRIBUTION_DISTRIBUTION_HOP_" + i
							+ "_CDF", folder);
		}
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single[] result = new Single[(hopSteps + 1) * 4];
		int counter = 0;

		for (int i = 0; i <= hopSteps; i++) {
			result[counter++] = new Single("ROUTING_HOP_DEGREE_DISTRIBUTION_"
					+ i + "_AVG", this.hopDegree[i].getAverage());
			result[counter++] = new Single("ROUTING_HOP_DEGREE_DISTRIBUTION_"
					+ i + "_MED", this.hopDegree[i].getMedian());
			result[counter++] = new Single("ROUTING_HOP_DEGREE_DISTRIBUTION_"
					+ i + "_MAX", this.hopDegree[i].getMax());
			result[counter++] = new Single("ROUTING_HOP_DEGREE_DISTRIBUTION_"
					+ i + "_MIN", this.hopDegree[i].getMin());
		}
		return result;
	}
}

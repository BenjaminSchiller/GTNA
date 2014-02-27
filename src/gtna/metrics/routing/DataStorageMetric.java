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
 * DataStorageMetric.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
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
import gtna.id.data.DataStore;
import gtna.id.data.DataStoreList;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.ArrayUtils;
import gtna.util.Distribution;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class DataStorageMetric extends Metric {

	private Distribution dataItemsDistribution;

	public DataStorageMetric() {
		super("DATA_STORAGE_METRIC");

		this.dataItemsDistribution = new Distribution("DATA_STORAGE_METRIC_DATA_ITEM_DISTRIBUTION", new double[] { -1 });
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		DataStoreList dsl = (DataStoreList) g.getProperty("DATA_STORAGE_0");

		int max = 0;
		for (DataStore ds : dsl.getList()) {
			// System.out.println("DS.size = " + ds.size());
			if (ds.sizeOfStore() > max) {
				max = ds.sizeOfStore();
			}
		}

		double[] distr = new double[max + 1];
		for (DataStore ds : dsl.getList()) {
			distr[ds.sizeOfStore()]++;
		}
		ArrayUtils.divide(distr, dsl.getList().length);

		this.dataItemsDistribution = new Distribution("DATA_STORAGE_METRIC_DATA_ITEM_DISTRIBUTION", distr);
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(
				this.dataItemsDistribution.getDistribution(),
				"DATA_STORAGE_METRIC_DATA_ITEM_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(
				this.dataItemsDistribution.getCdf(),
				"DATA_STORAGE_METRIC_DATA_ITEM_DISTRIBUTION_CDF", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single avg = new Single("DATA_STORAGE_METRIC_"
				+ "NUMBER_OF_DATA_ITEMS_AVG",
				this.dataItemsDistribution.getAverage());
		Single med = new Single("DATA_STORAGE_METRIC_"
				+ "NUMBER_OF_DATA_ITEMS_MED",
				this.dataItemsDistribution.getMedian());
		return new Single[] { avg, med };
	}
	
	@Override
	public Distribution[] getDistributions(){
		return new Distribution[] {dataItemsDistribution};
	}
	
	@Override
	public NodeValueList[] getNodeValueLists(){
		return new NodeValueList[] {};
	}
	
	@Override
	public boolean readData(String folder) {
		
		/* DISTRIBUTION */
		this.dataItemsDistribution = new Distribution("DATA_STORAGE_METRIC_DATA_ITEM_DISTRIBUTION", readDistribution(folder, "DATA_STORAGE_METRIC_DATA_ITEM_DISTRIBUTION"));
		
		return true;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("DATA_STORAGE_0")
				&& g.getProperty("DATA_STORAGE_0") instanceof DataStoreList;
	}

}

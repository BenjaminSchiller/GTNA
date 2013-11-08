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
 *
 */
package gtna.metrics.routing;

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

	private Distribution totalDataItems;

	private Distribution sourceDataItems;

	private Distribution replicaDataItems;

	public DataStorageMetric() {
		super("DATA_STORAGE_METRIC");

		this.totalDataItems = new Distribution(new double[] { -1 });
		this.sourceDataItems = new Distribution(new double[] { -1 });
		this.replicaDataItems = new Distribution(new double[] { -1 });
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		DataStoreList dsl = (DataStoreList) g.getProperty("DATA_STORAGE_0");

		int max_total = 0;
		int max_source = 0;
		int max_replica = 0;
		for (DataStore ds : dsl.getList()) {
			if (ds.sizeOfStore() > max_total) {
				max_total = ds.sizeOfStore();
			}
			if (ds.sizeOfSourceStore() > max_source) {
				max_source = ds.sizeOfSourceStore();
			}
			if (ds.sizeOfReplicaStore() > max_replica) {
				max_replica = ds.sizeOfReplicaStore();
			}
		}

		double[] total = new double[max_total + 1];
		double[] source = new double[max_source + 1];
		double[] replica = new double[max_replica + 1];
		for (DataStore ds : dsl.getList()) {
			total[ds.sizeOfStore()]++;
			source[ds.sizeOfSourceStore()]++;
			replica[ds.sizeOfReplicaStore()]++;
		}
		ArrayUtils.divide(total, dsl.getList().length);
		ArrayUtils.divide(source, dsl.getList().length);
		ArrayUtils.divide(replica, dsl.getList().length);

		this.totalDataItems = new Distribution(total);
		this.sourceDataItems = new Distribution(source);
		this.replicaDataItems = new Distribution(replica);
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;

		success &= DataWriter.writeWithIndex(
				this.totalDataItems.getDistribution(),
				"DATA_STORAGE_METRIC_TOTAL_DATA_ITEM_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.totalDataItems.getCdf(),
				"DATA_STORAGE_METRIC_TOTAL_DATA_ITEM_DISTRIBUTION_CDF", folder);

		success &= DataWriter.writeWithIndex(
				this.sourceDataItems.getDistribution(),
				"DATA_STORAGE_METRIC_SOURCE_DATA_ITEM_DISTRIBUTION", folder);
		success &= DataWriter
				.writeWithIndex(
						this.sourceDataItems.getCdf(),
						"DATA_STORAGE_METRIC_SOURCE_DATA_ITEM_DISTRIBUTION_CDF",
						folder);

		success &= DataWriter.writeWithIndex(
				this.replicaDataItems.getDistribution(),
				"DATA_STORAGE_METRIC_REPLICA_DATA_ITEM_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.replicaDataItems.getCdf(),
				"DATA_STORAGE_METRIC_REPLICA_DATA_ITEM_DISTRIBUTION_CDF",
				folder);

		return success;
	}

	@Override
	public Single[] getSingles() {

		Single avg1 = new Single("DATA_STORAGE_METRIC_"
				+ "NUMBER_OF_TOTAL_DATA_ITEMS_AVG",
				this.totalDataItems.getAverage());
		Single med1 = new Single("DATA_STORAGE_METRIC_"
				+ "NUMBER_OF_TOTAL_DATA_ITEMS_MED",
				this.totalDataItems.getMedian());

		Single avg2 = new Single("DATA_STORAGE_METRIC_"
				+ "NUMBER_OF_SOURCE_DATA_ITEMS_AVG",
				this.sourceDataItems.getAverage());
		Single med2 = new Single("DATA_STORAGE_METRIC_"
				+ "NUMBER_OF_SOURCE_DATA_ITEMS_MED",
				this.sourceDataItems.getMedian());

		Single avg3 = new Single("DATA_STORAGE_METRIC_"
				+ "NUMBER_OF_REPLICA_DATA_ITEMS_AVG",
				this.replicaDataItems.getAverage());
		Single med3 = new Single("DATA_STORAGE_METRIC_"
				+ "NUMBER_OF_REPLICA_DATA_ITEMS_MED",
				this.replicaDataItems.getMedian());

		return new Single[] { avg1, med1, avg2, med2, avg3, med3 };
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("DATA_STORAGE_0")
				&& g.getProperty("DATA_STORAGE_0") instanceof DataStoreList;
	}

}

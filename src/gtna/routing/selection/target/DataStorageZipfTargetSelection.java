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
 * RandomTargetSelection.java
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
package gtna.routing.selection.target;

import gtna.graph.Graph;
import gtna.id.Identifier;
import gtna.id.data.DataItem;
import gtna.id.data.DataStoreList;
import gtna.util.ArrayUtils;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class DataStorageZipfTargetSelection extends TargetSelection {

	protected double alpha;

	protected double[] distribution;

	protected double[] cdf;

	protected int[] mapping;

	protected DataStoreList dataStoreList;

	protected DataItem[] items;

	protected Random rand;

	public DataStorageZipfTargetSelection(double alpha) {
		super("TARGET_SELECTION_DATA_STORAGE_RANDOM",
				new Parameter[] { new DoubleParameter("ALPHA", alpha) });
		this.alpha = alpha;

		this.distribution = new double[35];
		this.cdf = new double[this.distribution.length];
		ZipfGenerator gen = new ZipfGenerator(this.distribution.length, alpha);
		double sum = 0;
		for (int i = 0; i < this.distribution.length; i++) {
			this.distribution[i] = gen.getProbability(i + 1);
			sum += this.distribution[i];
			if (i > 0) {
				this.cdf[i] = this.distribution[i] + this.cdf[i - 1];
			} else {
				this.cdf[i] = this.distribution[i];
			}
		}
	}

	public void init(Graph graph) {
		super.init(graph);
		this.dataStoreList = (DataStoreList) this.graph
				.getProperty("DATA_STORAGE_0");
		this.items = (DataItem[]) this.dataStoreList.getDataItems().toArray(
				new DataItem[this.dataStoreList.getDataItems().size()]);

		this.distribution = new double[this.items.length];
		this.cdf = new double[this.distribution.length];
		ZipfGenerator gen = new ZipfGenerator(this.distribution.length, alpha);
		double sum = 0;
		for (int i = 0; i < this.distribution.length; i++) {
			this.distribution[i] = gen.getProbability(i + 1);
			sum += this.distribution[i];
			if (i > 0) {
				this.cdf[i] = this.distribution[i] + this.cdf[i - 1];
			} else {
				this.cdf[i] = this.distribution[i];
			}
		}
		this.rand = new Random();
		this.mapping = new int[this.items.length];
		for (int i = 0; i < this.mapping.length; i++) {
			this.mapping[i] = i;
		}
		ArrayUtils.shuffle(this.mapping, this.rand);
	}

	@Override
	public Identifier getNextTarget() {
		double r = this.rand.nextDouble();
		if (r <= this.cdf[0]) {
			return this.items[this.mapping[0]].getId();
		}
		for (int i = 1; i < this.cdf.length; i++) {
			if (this.cdf[i - 1] < r && r <= this.cdf[i]) {
				return this.items[this.mapping[i]].getId();
			}
		}
		return this.items[this.mapping[this.items.length - 1]].getId();
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("DATA_STORAGE_0")
				&& graph.getProperty("DATA_STORAGE_0") instanceof DataStoreList;
	}

	public class ZipfGenerator {
		private Random rnd = new Random(System.currentTimeMillis());
		private int size;
		private double skew;
		private double bottom = 0;

		public ZipfGenerator(int size, double skew) {
			this.size = size;
			this.skew = skew;

			for (int i = 1; i < size; i++) {
				this.bottom += (1 / Math.pow(i, this.skew));
			}
		}

		// the next() method returns an random rank id.
		// The frequency of returned rank ids are follows Zipf distribution.
		public int next() {
			int rank;
			double friquency = 0;
			double dice;

			rank = rnd.nextInt(size);
			friquency = (1.0d / Math.pow(rank, this.skew)) / this.bottom;
			dice = rnd.nextDouble();

			while (!(dice < friquency)) {
				rank = rnd.nextInt(size);
				friquency = (1.0d / Math.pow(rank, this.skew)) / this.bottom;
				dice = rnd.nextDouble();
			}

			return rank;
		}

		// This method returns a probability that the given rank occurs.
		public double getProbability(int rank) {
			return (1.0d / Math.pow(rank, this.skew)) / this.bottom;
		}

	}

}

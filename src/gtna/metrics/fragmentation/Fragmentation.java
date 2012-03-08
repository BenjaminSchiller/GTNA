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
 * Fragmentation.java
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
package gtna.metrics.fragmentation;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.graph.sorting.NodeSorter;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;

import java.util.HashMap;
import java.util.Random;

/**
 * @author benni
 * 
 */
public abstract class Fragmentation extends Metric {
	protected NodeSorter sorter;

	protected Resolution resolution;

	public static enum Type {
		STRONG, WEAK
	}

	public static enum Resolution {
		SINGLE, PERCENT
	};

	public Fragmentation(Type type, NodeSorter sorter, Resolution resolution) {
		super("FRAGMENTATION", new Parameter[] {
				new Parameter("TYPE", type.toString()),
				new Parameter("SORTER", sorter.getKey()),
				new Parameter("RESOLUTION", resolution.toString()) });
		this.sorter = sorter;
		this.resolution = resolution;
	}

	private double[] isolatedComponentSizeAvg;

	private double[] isolatedComponentSizeMax;

	private double[] isolatedComponentSizeMed;

	private double[] isolatedComponentSizeMin;

	private double[] numberOfIsolatedComponents;

	private double[] largestComponentSize;

	private double criticalPoint;

	private Timer runtime;

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.runtime = new Timer();
		int[] excludeFirst = this.getExcludeFirst(g.getNodes().length);
		this.isolatedComponentSizeAvg = new double[excludeFirst.length];
		this.isolatedComponentSizeMax = new double[excludeFirst.length];
		this.isolatedComponentSizeMed = new double[excludeFirst.length];
		this.isolatedComponentSizeMin = new double[excludeFirst.length];
		this.numberOfIsolatedComponents = new double[excludeFirst.length];
		this.largestComponentSize = new double[excludeFirst.length];
		this.criticalPoint = g.getNodes().length;
		Random rand = new Random();
		Node[] sorted = this.sorter.sort(g, rand);
		for (int i = 0; i < excludeFirst.length; i++) {
			boolean[] exclude = this.getExclude(sorted, excludeFirst[i]);
			Partition p = this.partition(g, sorted, exclude);

			this.numberOfIsolatedComponents[i] = p.getComponents().length - 1;
			this.largestComponentSize[i] = p.getLargestComponent().length;

			if (this.numberOfIsolatedComponents[i] == 0) {
				this.isolatedComponentSizeAvg[i] = 0;
				this.isolatedComponentSizeMax[i] = 0;
				this.isolatedComponentSizeMed[i] = 0;
				this.isolatedComponentSizeMin[i] = 0;
			} else {
				this.isolatedComponentSizeAvg[i] = this.avgIsolatedSize(p);
				this.isolatedComponentSizeMax[i] = p.getComponents()[p
						.getComponents().length - 2].length;
				this.isolatedComponentSizeMed[i] = p.getComponents()[(int) Math
						.floor(p.getComponents().length / 2)].length;
				this.isolatedComponentSizeMin[i] = p.getComponents()[p
						.getComponents().length - 1].length;
			}

			if (this.largestComponentSize[i] < 0.5 * (g.getNodes().length - excludeFirst[i])
					&& excludeFirst[i] < this.criticalPoint) {
				this.criticalPoint = excludeFirst[i];
			}
		}
		this.runtime.end();
	}

	private double avgIsolatedSize(Partition p) {
		double sum = 0;
		for (int i = 1; i < p.getComponents().length; i++) {
			sum += p.getComponents()[i].length;
		}
		return sum / p.getComponents().length;
	}

	private int[] getExcludeFirst(int nodes) {
		if (nodes < 100 || this.resolution == Resolution.SINGLE) {
			int[] exclude = new int[nodes];
			for (int i = 0; i < nodes; i++) {
				exclude[i] = i;
			}
			return exclude;
		} else {
			int[] exclude = new int[100];
			for (int i = 0; i < 100; i++) {
				exclude[i] = (int) Math
						.floor(((double) nodes * (double) i) / 100.0);
			}
			return exclude;
		}
	}

	private boolean[] getExclude(Node[] sorted, int excludeFirst) {
		boolean[] exclude = new boolean[sorted.length];
		for (int i = 0; i < excludeFirst; i++) {
			exclude[sorted[i].getIndex()] = true;
		}
		return exclude;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeAvg,
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_AVG", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMax,
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MAX", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMed,
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MED", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMin,
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MIN", folder);
		success &= DataWriter.writeWithIndex(this.numberOfIsolatedComponents,
				"FRAGMENTATION_NUMBER_OF_ISOLATED_COMPONENTS", folder);
		success &= DataWriter.writeWithIndex(this.largestComponentSize,
				"FRAGMENTATION_LARGEST_COMPONENT_SIZE", folder);
		return success;
	}

	@Override
	public Value[] getValues() {
		Value CP = new Value("FRAGMENTATION_CRITICAL_POINT", this.criticalPoint);
		Value RT = new Value("FRAGMENTATION_RUNTIME", this.runtime.getRuntime());
		return new Value[] {CP, RT};
	}

	protected abstract Partition partition(Graph g, Node[] sorted,
			boolean[] exclude);

}

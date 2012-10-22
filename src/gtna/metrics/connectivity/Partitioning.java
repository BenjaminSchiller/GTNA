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
 * Connectivity.java
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
package gtna.metrics.connectivity;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.partition.Partition;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public abstract class Partitioning extends Metric {
	private double largestComponent;

	private double largestComponentFraction;

	private double[] components;

	private double[] componentsFraction;

	public Partitioning(String key) {
		super(key);
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Partition p = this.getPartition(g);
		this.largestComponent = p.getComponents()[0].length;
		this.largestComponentFraction = this.largestComponent
				/ (double) g.getNodes().length;
		this.components = new double[p.getComponents().length];
		this.componentsFraction = new double[p.getComponents().length];
		for (int i = 0; i < p.getComponents().length; i++) {
			this.components[i] = p.getComponents()[i].length;
			this.componentsFraction[i] = this.components[i]
					/ (double) g.getNodes().length;
		}
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.components, this.getKey()
				+ "_COMPONENTS", folder);
		success &= DataWriter.writeWithIndex(this.componentsFraction,
				this.getKey() + "_COMPONENTS_FRACTION", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single largestComponent = new Single(this.getKey()
				+ "_LARGEST_COMPONENT", this.largestComponent);
		Single largestComponentFraction = new Single(this.getKey()
				+ "_LARGEST_COMPONENT_FRACTION", this.largestComponentFraction);
		return new Single[] { largestComponent, largestComponentFraction };
	}

	protected abstract Partition getPartition(Graph g);

}

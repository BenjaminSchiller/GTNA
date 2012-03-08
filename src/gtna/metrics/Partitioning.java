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
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.partition.Partition;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Timer;

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

	private String property;

	private Timer runtime;

	public Partitioning(String key, String property) {
		super(key);
		this.property = property;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if (!g.hasProperty(this.property + "_0")) {
			g = this.addProperty(g);
		}
		this.runtime = new Timer();
		Partition p = (Partition) g.getProperty(this.property + "_0");
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
		this.runtime.end();
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
	public Value[] getValues() {
		Value largestComponent = new Value(
				this.getKey() + "_LARGEST_COMPONENT", this.largestComponent);
		Value largestComponentFraction = new Value(this.getKey()
				+ "_LARGEST_COMPONENT_FRACTION", this.largestComponentFraction);
		Value runtime = new Value(this.getKey() + "_RUNTIME",
				this.runtime.getRuntime());
		return new Value[] { largestComponent, largestComponentFraction,
				runtime };
	}

	protected abstract Graph addProperty(Graph g);

}

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
 * Network.java
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
package gtna.networks;

import gtna.graph.Graph;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;

/**
 * Interface that must be implemented by all network generators.
 * 
 * @author benni
 * 
 */
public abstract class Network extends ParameterList {
	protected int nodes;

	protected Transformation[] transformations;

	public Network(String key, int nodes, Transformation[] transformations) {
		this(key, nodes, new Parameter[0], transformations);
	}

	public Network(String key, int nodes, Parameter[] parameters,
			Transformation[] transformations) {
		super(key, Network.add(parameters, nodes));
		this.nodes = nodes;
		this.transformations = transformations;
	}

	private static Parameter[] add(Parameter[] p1, int nodes) {
		Parameter[] p2 = new Parameter[p1.length + 1];
		p2[0] = new IntParameter("NODES", nodes);
		for (int i = 0; i < p1.length; i++) {
			p2[i + 1] = p1[i];
		}
		return p2;
	}

	public int getNodes() {
		return this.nodes;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}

	public Transformation[] getTransformations() {
		return this.transformations;
	}

	/**
	 * Generate an instance of the network topology specified by the class and
	 * the individual configuration parameter given to the constructor.
	 * 
	 * @return generated network instance
	 */
	public abstract Graph generate();

	// public String compareName(Network nw, String key);
	//
	// public String compareName(Network nw);
	//
	// public String compareNameShort(Network nw);
	//
	// public String compareNameLong(Network nw);
	//
	// public String compareValue(Network nw);
	//
	// public String description();
	//
	// public String description(Network compare);
	//
	// public String description(Network compare1, Network compare2);
	//
	// public String description(String key);
	//
	// public String description(String key, Network compare);
	//
	// public String description(String key, Network compare1, Network
	// compare2);
}

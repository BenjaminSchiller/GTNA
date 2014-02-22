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
 * ErdosRenyi.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Tim Grube;
 * Contributors:    -;
 * 
 * Changes since 2014-02-11
 * ---------------------------------------
 */
package gtna.networks.util;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * Implements an empty network. Mainly to allow the error comparison. 
 * 
 * used to "hold" a key for calculating/plotting
 * 
 * @author tim
 * 
 */
public class EmptyNetwork extends DescriptionWrapper {
	

	private String emptyKey;

	public EmptyNetwork(Network n, String key) {
		super(n, key);
		
		emptyKey = key;
		
		Config.appendToList(key + "_NAME", "Empty Network " + key);
		Config.appendToList(key + "_NAME_LONG", "EmptyNetwork");
		Config.appendToList(key + "_NAME_SHORT", "EN");
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());

		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, 0);
		
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
	
	public String getFolderName() {
		return emptyKey + "-comparison";
	}
}

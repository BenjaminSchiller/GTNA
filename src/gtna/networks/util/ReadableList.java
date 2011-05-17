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
 * ReadableList.java
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
package gtna.networks.util;

import gtna.graph.Graph;
import gtna.io.GraphReader;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

/**
 * Implements a graph generator for a list of snapshots. It works like the
 * ReadableFile network generator and simply takes a list of snapshots as
 * parameter. Every time the generate method is called, the next snapshot in the
 * list is used. Therefore, LIST.length different graphs can be generated with
 * this implementation. Hence, if a series with more than LIST.length iterations
 * is generated, there will be duplicates in the "generated" network graphs.
 * 
 * The parameters are the list snapshot filename and the type (as defined by the
 * GraphReader class).
 * 
 * @author benni
 * 
 */
public class ReadableList extends NetworkImpl implements Network {
	private String[] LIST;

	private int TYPE;

	private int CURRENT_INDEX;

	public ReadableList(String key, String[] LIST, int TYPE,
			RoutingAlgorithm ra, Transformation[] t) {
		super(key, Integer.MIN_VALUE, new String[] {}, new String[] {}, ra, t);
		this.LIST = LIST;
		this.TYPE = TYPE;
		this.CURRENT_INDEX = -1;
		int nodes = 0;
		for (int i = 0; i < this.LIST.length; i++) {
			nodes += GraphReader.nodes(this.LIST[i], this.TYPE);
		}
		nodes /= this.LIST.length;
		super.setNodes(nodes);
	}

	public ReadableList(String key, int nodes, String[] LIST, int TYPE,
			String[] keys, String[] values, RoutingAlgorithm ra,
			Transformation[] t) {
		super(key, nodes, keys, values, ra, t);
		this.LIST = LIST;
		this.TYPE = TYPE;
		this.CURRENT_INDEX = -1;
	}

	public Graph generate() {
		this.CURRENT_INDEX = (this.CURRENT_INDEX + 1) % this.LIST.length;
		return GraphReader.read(this.LIST[this.CURRENT_INDEX], this.TYPE, this
				.description());
	}

}

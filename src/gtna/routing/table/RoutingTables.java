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
 * RoutingTables.java
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
package gtna.routing.table;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.ring.RingPartition;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class RoutingTables implements GraphProperty {
	
	protected RoutingTable[] tables;
	
	public static final int noRoute = -1;

	public RoutingTables(RoutingTable[] tables) {
		this.tables = tables;
	}

	public RoutingTable getRoutingTable(int node) {
		return this.tables[node];
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		// CLASS
		fw.writeComment(Config.get("GRAPH_PROPERTY_CLASS"));
		fw.writeln(this.getClass().getCanonicalName().toString());

		// KEY
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);

		// # MODULUS
		fw.writeComment("Modulus");
		fw.writeln(this.modulus);

		// # WRAP-AROUND
		fw.writeComment("Wrap-around");
		fw.writeln(this.wrapAround + "");

		// # PARTITIONS
		fw.writeComment("Partitions");
		fw.writeln(this.partitions.length);

		fw.writeln();

		// PARTITIONS
		int index = 0;
		for (RingPartition p : this.partitions) {
			fw.writeln(index++ + ":" + p.toString());
		}

		return fw.close();
	}

	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub

	}
}

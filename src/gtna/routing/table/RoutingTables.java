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

import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;

/**
 * @author benni
 * 
 */
public class RoutingTables extends GraphProperty {

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

		this.writeHeader(fw, this.getClass(), key);

		this.writeParameter(fw, "Routing Table Class",
				this.tables[0].getClass());
		this.writeParameter(fw, "Routing Tables", this.tables.length);

		fw.writeln();

		for (RoutingTable table : this.tables) {
			fw.writeln(table.asString());
		}

		return fw.close();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String read(String filename) {
		Filereader fr = new Filereader(filename);

		String key = this.readHeader(fr);

		Class routingTableClass = this.readClass(fr);
		int routingTables = this.readInt(fr);

		this.tables = new RoutingTable[routingTables];
		for (int i = 0; i < routingTables; i++) {
			try {
				RoutingTable rt = (RoutingTable) routingTableClass
						.newInstance();
				this.tables[i] = rt;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		fr.close();

		return key;
	}
}

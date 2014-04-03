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
 * EdgeListGraphWriter.java
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
package gtna.io.graphWriter;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.io.Filewriter;

/**
 * @author benni
 * 
 */
public class EdgeListGraphWriter extends GraphWriter {

	private String separator;

	/**
	 * @param key
	 */
	public EdgeListGraphWriter(String separator) {
		super("EDGE_LIST");
		this.separator = separator;
	}

	@Override
	public boolean write(Graph g, String filename) {
		Filewriter fw = new Filewriter(filename);
		for (Edge edge : g.getEdges().getEdges()) {
			fw.writeln(edge.getSrc() + this.separator + edge.getDst());
		}
		return fw.close();
	}

}

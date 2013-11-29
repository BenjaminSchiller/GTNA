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
 * TransformGraphs.java
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
package gtna.projects.ccnRouting;

import gtna.graph.Graph;
import gtna.io.graphReader.EdgeListGraphReader;
import gtna.io.graphReader.GraphReader;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;

/**
 * @author benni
 * 
 */
public class TransformGraphs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String folder = "/Users/benni/Downloads/";
		String filename1 = "test.cch";
		String filename2 = "test.gtna";
		String separator = " ";

		GraphReader r = new EdgeListGraphReader(separator);
		Graph g = r.read(folder + filename1);
		GraphWriter w = new GtnaGraphWriter();
		w.write(g, folder + filename2);
	}

}

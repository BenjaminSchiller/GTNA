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
 * SnapReaderWriter.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.graph.Graph;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.SnapStandardFile;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Tim
 *
 */
public class SnapReaderWriter {

	/**
	 * 
	 */
	public SnapReaderWriter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		String folder = "/Users/Tim/Dropbox/Master-Thesis/SNAP-samples/";
		String[] paths = new String[] { "p2p-Gnutella05.txt" };
		Network[] net = loadNetworks(folder, paths);
		
		Collection<Graph> graphs = new ArrayList<Graph>();
		
		for(Network n : net){
			graphs.add(n.generate());
		}
		System.out.println("Loaded " + net.length + " SNAP networks");

	}
	
	
	public static Network[] loadNetworks(String folder, String[] networkPaths) {
		Collection<Network> nets = new ArrayList<Network>();

		for (String i : networkPaths) {
			Network ni = new SnapStandardFile(i, folder, i, null);
			nets.add(ni);
		}
		return nets.toArray(new Network[0]);
	}

}

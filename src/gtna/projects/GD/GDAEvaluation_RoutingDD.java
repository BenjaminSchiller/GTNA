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
 * GDAEvaluation_RoutingDD.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.GD;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.networks.util.ReadableList;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.util.Config;

/**
 * @author Nico
 *
 */
public class GDAEvaluation_RoutingDD {

	public static void main(String[] args) {
		int lengthOfSeries = 20;
		
		String folder = "./resources/evaluation/10000/barabasiAlbert-10-b-wcp-gcc-stBFS-hd-mh-100.0-100.0/";
		String[] files = new String[lengthOfSeries];
		for ( int i = 0; i < lengthOfSeries; i++) {
			files[i] = folder + i + ".txt";
		}
		
		RoutingAlgorithm ra = new Greedy(5);
		Network nw = new ReadableList("", folder, files, ra, null);
		
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + false);
		Series s = Series.generate(nw, lengthOfSeries);
		
	}

}

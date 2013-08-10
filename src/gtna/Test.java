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
 * Test.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.transformation.Transformation;
import gtna.transformation.eigenvector.StoreSpectrum;
import gtna.util.Config;

/**
 * @author stefanie
 *
 */
public class Test {
	
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/ex9/");
		Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Network nw = new ReadableFile("G","G","data/G.txt",new Transformation[]
		{new StoreSpectrum()});
Metric[] m = new Metric[]{};
Series.generate(nw, m, 1);
//		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
//		Network nw = new BarabasiAlbert(1000,2,new Transformation[]
//				{new LargestWeaklyConnectedComponent(),new StoreFiedler()});
//		Metric[] m = new Metric[]{new WeakFragmentation(new DegreeNodeSorter(NodeSorterMode.DESC), Resolution.PERCENT),
//				new WeakFragmentation(new SpectralSorter(NodeSorterMode.DESC,Calculation.SUM,DegreeOne.INCLUDE), Resolution.PERCENT),
//				new WeakFragmentation(new SpectralSorter(NodeSorterMode.DESC,Calculation.ABSOLUTE,DegreeOne.INCLUDE), Resolution.PERCENT),
//				new WeakFragmentation(new SpectralSorter(NodeSorterMode.DESC,Calculation.SUM,DegreeOne.INCLUDE_CAL), Resolution.PERCENT),
//				new WeakFragmentation(new SpectralSorter(NodeSorterMode.DESC,Calculation.ABSOLUTE,DegreeOne.INCLUDE_CAL), Resolution.PERCENT),
//				new WeakFragmentation(new SpectralSorter(NodeSorterMode.DESC,Calculation.SUM,DegreeOne.EXCLUDE), Resolution.PERCENT),
//				new WeakFragmentation(new SpectralSorter(NodeSorterMode.DESC,Calculation.ABSOLUTE,DegreeOne.EXCLUDE), Resolution.PERCENT),
//				new WeakFragmentation(new RandomNodeSorter(), Resolution.PERCENT)};
//		Series s = Series.generate(nw, m, 10);
//		Plotting.multi(s, m, "data/test/");
//		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
//		Config.overwrite("MAIN_DATA_FOLDER", "./data/examples/");
//		Config.overwrite("SERIES_GRAPH_WRITE", "true");
//		//int nr = 9;
//		for (int nr = 1; nr < 10; nr++){
//		Network nw = new ReadableFile("TEST"+nr,"TEST"+nr,"data/exgraphs/test"+nr+".txt",new Transformation[]
//				{new StoreSpectrum()});
//		Metric[] m = new Metric[]{};
//		Series.generate(nw, m, 1);
//		}
	}

}

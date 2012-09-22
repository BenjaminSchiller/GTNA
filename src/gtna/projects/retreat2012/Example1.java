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
 * Example1.java
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
package gtna.projects.retreat2012;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data.Type;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Example1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/retreat2012-example1/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/retreat2012-example1/");

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		int times = 10;

		Metric dd = new DegreeDistribution();
		Metric sp = new ShortestPaths();

		Metric[] metrics = new Metric[] { dd, sp };

		Transformation[] transformations = new Transformation[] {};

		Network[] nw = ErdosRenyi.get(1000, new double[] { 2.0, 4.0, 6.0, 8.0,
				10.0 }, false, transformations);

		Series[] series = Series.generate(nw, metrics, times);

		Plotting.multi(series, metrics, "multi/", Type.average,
				Style.linespoint);
		
		Plotting.single(series, metrics, "single/");
		
		Plotting.singleBy(series, metrics, "single-by-edges/", dd, "EDGES");
	}

}

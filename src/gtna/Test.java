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
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.data.Series;
import gtna.metrics.DegreeDistribution;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.subGraphs.BuildSubGraphMax;

/**
 * @author stef
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Network net = new BarabasiAlbert(200,4,new Transformation[]{new BuildSubGraphMax(100,1,5,4,BuildSubGraphMax.SELECTION_OUTDEGREE)});
		//Network net = new BarabasiAlbert(200,4,null);
		Metric[] m = new Metric[]{new DegreeDistribution()};
        Series ser = Series.generate(net, m , 5); 
        Plotting.multi(ser, m, "testSub/");
	}

}

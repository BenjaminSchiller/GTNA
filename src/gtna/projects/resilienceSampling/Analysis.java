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
 * ResilienceSampling.java
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
package gtna.projects.resilienceSampling;

import gtna.data.Series;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.fragmentation.CriticalPointsTheory;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.StrongFragmentation;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.util.Config;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author benni
 * 
 */
public class Analysis {

	public static final String dataFolder = SampleGeneration.graphFolder
			+ "data/";

	// undirected graphs (SPI)
	// public static Metric[] m_u = new Metric[] { new DegreeDistribution() };
	public static Metric[] m_u = new Metric[] {
			new DegreeDistribution(),
			new WeakFragmentation(new RandomNodeSorter(), Resolution.PERCENT),
			new WeakFragmentation(new DegreeNodeSorter(NodeSorterMode.DESC),
					Resolution.PERCENT),
			new CriticalPointsTheory(false,
					CriticalPointsTheory.Selection.RANDOM),
			new CriticalPointsTheory(false,
					CriticalPointsTheory.Selection.LARGEST) };

	// directed graphs (WOT)
	public static Metric[] m_d = new Metric[] {
			new DegreeDistribution(),
			new StrongFragmentation(new RandomNodeSorter(), Resolution.PERCENT),
			new StrongFragmentation(new DegreeNodeSorter(NodeSorterMode.DESC),
					Resolution.PERCENT),
			new CriticalPointsTheory(true,
					CriticalPointsTheory.Selection.RANDOM),
			new CriticalPointsTheory(true,
					CriticalPointsTheory.Selection.LARGEST) };

	public static void main(String[] args) {
		// generateData("spi", m_u, SampleGeneration.times);
		generateData("wot", m_d, SampleGeneration.times);
	}

	public static void generateData(String name, Metric[] metrics, int times) {
		Config.overwrite("MAIN_DATA_FOLDER",
				"/Users/benni/Downloads/criticalPoints/_data/");
		Config.overwrite("MAIN_PLOT_FOLDER",
				"/Users/benni/Downloads/criticalPoints/_plots/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		Network nw = SampleGeneration.getNetwork(name);
		Network[][] nw_s = SampleGeneration.getSamples(name);
		for (Network[] nw_s1 : nw_s) {
			for (Network nw_s2 : nw_s1) {
				System.out.println(nw_s2.getDescription());
			}
		}
		Series s = Series.generate(
				new DescriptionWrapper(nw, nw.getDescription(),
						new Parameter[] {
								new StringParameter("ALGORITHM", "none"),
								new IntParameter("PERCENT", 100) }), metrics,
				times);
		Series[][] s_s = Series.generate(nw_s, metrics, times);

		Type type = Type.average;
		Style style = Style.linespoint;

		Plotting.multi(append(s_s, s), metrics, name + "/-multi/", type, style);
		Plotting.single(append(s_s, s), metrics, name + "/-single/", type,
				style);

		for (int i = 0; i < s_s.length; i++) {
			Plotting.multi(append(s_s[i], s), metrics, name + "/"
					+ SampleGeneration.algorithms[i].toString() + "-multi/",
					type, style);
			Plotting.single(append(s_s[i], s), metrics, name + "/"
					+ SampleGeneration.algorithms[i].toString() + "-single/",
					type, style);
		}

		for (int i = 0; i < SampleGeneration.percents.length; i++) {
			int p = SampleGeneration.percents[i];
			Series[] s_p = new Series[s_s.length];
			for (int j = 0; j < s_s.length; j++) {
				s_p[j] = s_s[j][i];
			}
			Plotting.multi(append(s_p, s), metrics, name + "/" + p + "-multi/",
					type, style);
			Plotting.single(append(s_p, s), metrics, name + "/" + p
					+ "-single/", type, style);
		}
	}

	public static Series[] append(Series[] s, Series a) {
		Series[] s_ = new Series[s.length + 1];
		for (int i = 0; i < s.length; i++) {
			s_[i] = s[i];
		}
		s_[s.length] = a;
		return s_;
	}

	public static Series[][] append(Series[][] s, Series a) {
		Series[][] s_ = new Series[s.length + 1][];
		for (int i = 0; i < s.length; i++) {
			s_[i] = s[i];
		}
		s_[s.length] = new Series[] { a };
		return s_;
	}

}

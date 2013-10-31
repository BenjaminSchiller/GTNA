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
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;

/**
 * @author benni
 * 
 */
public class Analysis {

	public static final String dataFolder = SampleGeneration.graphFolder
			+ "data/";

	// undirected graphs (SPI)
	public static Metric[] m_u = new Metric[] { new DegreeDistribution() };
	// public static Metric[] m_u = new Metric[] {
	// new DegreeDistribution(),
	// new WeakFragmentation(new RandomNodeSorter(), Resolution.PERCENT),
	// new WeakFragmentation(new DegreeNodeSorter(NodeSorterMode.DESC),
	// Resolution.PERCENT),
	// new CriticalPointsTheory(false,
	// CriticalPointsTheory.Selection.RANDOM),
	// new CriticalPointsTheory(false,
	// CriticalPointsTheory.Selection.LARGEST) };

	// directed graphs (WOT)
	public static Metric[] m_d = new Metric[] {
			new DegreeDistribution(),
			new WeakFragmentation(new RandomNodeSorter(), Resolution.PERCENT),
			new WeakFragmentation(new DegreeNodeSorter(NodeSorterMode.DESC),
					Resolution.PERCENT),
			new CriticalPointsTheory(true,
					CriticalPointsTheory.Selection.RANDOM),
			new CriticalPointsTheory(true,
					CriticalPointsTheory.Selection.LARGEST) };

	public static void main(String[] args) {
		generateData("er", m_u, 1);
	}

	public static void generateData(String name, Metric[] metrics, int times) {
		Network nw = SampleGeneration.getNetwork(name);
		Network[][] nw_s = SampleGeneration.getSamples(name);
		Series.generate(nw, metrics, times);
		Series.generate(nw_s, metrics, times);
	}

}

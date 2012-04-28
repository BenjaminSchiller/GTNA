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
 * GraphSizeComparator.java
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
package gtna.io.networks.googlePlus;

import gtna.io.graphReader.GtnaGraphReader;

import java.io.File;
import java.util.Comparator;

/**
 * @author benni
 * 
 */
public class GraphSizeComparator implements Comparator<File> {

	private Comparator<File> c2;

	public GraphSizeComparator(Comparator<File> c2) {
		this.c2 = c2;
	}

	@Override
	public int compare(File f1, File f2) {
		int s1 = new GtnaGraphReader().nodes(f1.getAbsolutePath());
		int s2 = new GtnaGraphReader().nodes(f2.getAbsolutePath());
		int v = s1 - s2;
		if (v == 0) {
			return this.c2.compare(f1, f2);
		}
		return v;
	}

}

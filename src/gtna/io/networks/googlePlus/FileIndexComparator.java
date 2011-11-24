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
 * FileIndexComparator.java
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

import java.io.File;
import java.util.Comparator;

/**
 * @author benni
 * 
 */
public class FileIndexComparator implements Comparator<File> {

	private String split;

	private int index;

	public FileIndexComparator(String split, int index) {
		this.split = split;
		this.index = index;
	}

	@Override
	public int compare(File f1, File f2) {
		int v1 = Integer.parseInt(f1.getName().split(this.split)[this.index]);
		int v2 = Integer.parseInt(f2.getName().split(this.split)[this.index]);
		return v1 - v2;
	}

}

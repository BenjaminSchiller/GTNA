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
 * DoubleArrayParameter.java
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
package gtna.util.parameter;

/**
 * @author benni
 * 
 */
public class IntArray2dParameter extends Parameter {
	private int[][] intArray2dValue;

	public IntArray2dParameter(String key, int[][] intArray2dValue) {
		super(key, IntArray2dParameter.toString(intArray2dValue));
		this.intArray2dValue = intArray2dValue;
	}

	public int[][] getIntArray2dValue() {
		return this.intArray2dValue;
	}

	private static String toString(int[][] v) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < v.length; i++) {
			if (i > 0) {
				buff.append("__");
			}
			for (int j = 0; j < v[i].length; j++) {
				if (j == 0) {
					buff.append(v[i][j]);
				} else {
					buff.append("_" + v[i][j]);
				}
			}
		}
		return buff.toString();
	}
}

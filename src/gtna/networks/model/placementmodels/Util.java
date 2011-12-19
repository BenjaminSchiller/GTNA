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
 * Util.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.placementmodels;

/**
 * @author Flipp
 * 
 */
public class Util {

	/**
	 * @param strings
	 * @param configKeys
	 * @return
	 */
	public static String[] addToArray(String[] arr1, String[] arr2) {
		if (arr2 == null)
			return arr1;
		if (arr1 == null)
			return arr2;

		String[] ret = new String[arr1.length + arr2.length];
		for (int i = 0; i < arr1.length; i++)
			ret[i] = arr1[i];
		for (int i = 0; i < arr2.length; i++)
			ret[arr1.length + i] = arr2[i];

		return ret;
	}

	/**
	 * @param string
	 * @param configKeys
	 * @return
	 */
	static String[] addPrefix(String str, String[] arr1) {
		for (int i = 0; i < arr1.length; i++) {
			arr1[i] = str + arr1[i];
		}

		return arr1;
	}

}

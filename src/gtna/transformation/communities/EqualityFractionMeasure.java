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
 * SimilarityMeasure.java
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
package gtna.transformation.communities;

import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * The <code>EqualityFractionMeasure</code> calculates the similarity of the two
 * <code>int[]</code> by (AND / (AND + XOR)).
 * 
 * @author Philipp Neubrand
 * 
 */
public class EqualityFractionMeasure implements SimilarityMeasure {

	@Override
	public double minValue() {
		return 0;
	}

	/**
	 * Calculates the similarity of the two arrays as (AND / (AND * XOR)).
	 */
	@Override
	public double calcSimilarity(int[] arr1, int[] arr2) {
		double and = 0;
		double xor = 0;
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] == arr2[i] && arr1[i] == 1) {
				and++;
			} else if (arr1[i] != arr2[i]) {
				xor++;
			}
		}
		if (xor == 0 && and == 0)
			return 0;

		return and / (and + xor);
	}

	@Override
	public Parameter[] getParameterArray() {
		return new StringParameter[] { new StringParameter("SM_KEY",
				"EFMeasure") };
	}

	@Override
	public double getMaxValue(int[] arr1, int[] arr2) {
		return 1;
	}

}

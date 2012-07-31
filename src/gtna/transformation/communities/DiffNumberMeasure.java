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
 * DiffNumberMeasure.java
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
 * The <code>DiffNumberMeasure</code> calculates the difference between two
 * <code>int[]</code> as the number of differences between their components.
 * This similarity measure was used in the the paper
 * "A Local Method for Detecting Communities"
 * http://arxiv.org/pdf/cond-mat/0412482.pdf
 * 
 * @author Philipp Neubrand
 * 
 */
public class DiffNumberMeasure implements SimilarityMeasure {

	@Override
	public double minValue() {
		return 0;
	}

	@Override
	public double calcSimilarity(int[] arr1, int[] arr2) {
		int val1 = 0;
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i])
				val1++;
		}

		return arr1.length - val1;
	}

	@Override
	public Parameter[] getParameterArray() {
		return new Parameter[] { new StringParameter("SM_KEY",
				"DiffNumberMeasure") };
	}

	@Override
	public double getMaxValue(int[] arr1, int[] arr2) {
		return arr1.length;
	}

}

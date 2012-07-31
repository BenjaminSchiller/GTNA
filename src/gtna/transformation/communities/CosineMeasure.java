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
 * CosineSimilarityMeasure.java
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
 * The <code>CosineMeasure</code> calculates the similarity of the two
 * <code>int[]</code> by calculating the cosine similarity, as defined for
 * example here: http://en.wikipedia.org/wiki/Cosine_similarity
 * 
 * The basic idea is that the arrays are assumed to be Vectors and the angle
 * between them reflects the similarity.
 * 
 * @author Philipp Neubrand
 * 
 */
public class CosineMeasure implements SimilarityMeasure {


	@Override
	public double minValue() {
		return -1;
	}

	@Override
	public double calcSimilarity(int[] arr1, int[] arr2) {
		double val1 = 0;
		double val2 = 0;
		double val3 = 0;
		for(int i = 0; i < arr1.length; i++){
			val1 += arr1[i] * arr2[i];
			val2 += arr1[i] * arr1[i];
			val3 += arr2[i] * arr2[i];
		}
		
		return val1 / (Math.sqrt(val2) * Math.sqrt(val3));
	}

	@Override
	public Parameter[] getParameterArray() {
		return new Parameter[]{new StringParameter("SM_KEY", "CosMeasure")};
	}

}

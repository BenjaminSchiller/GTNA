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

/**
 * A <code>SimilarityMeasure</code> is a way to calculate the similarity between
 * two <code>int[]</code>. This class is used to calculate the similarities
 * between the detected communities of two nodes.
 * 
 * @author Philipp Neubrand
 * 
 */
public interface SimilarityMeasure {

	/**
	 * Returns the minimal value for this similarity measure.
	 * 
	 * @return The minimal value.
	 */
	public double minValue();

	/**
	 * Calculates the similarity between the two supplied int arrays.
	 * 
	 * @param arr1
	 *            The first array.
	 * @param arr2
	 *            The second array.
	 * @return The similarity between those two arrays.
	 */
	public double calcSimilarity(int[] arr1, int[] arr2);

	/**
	 * Returns a <code>Parameter[]</code> containing the configuration
	 * parameters.
	 * 
	 * @return A <code>Parameter[]</code> of the configuration parameters.
	 */
	public Parameter[] getParameterArray();

	/**
	 * Returns the maximal possible value for the similarity between the two
	 * arrays.
	 * 
	 * @param arr1
	 *            The first array.
	 * @param arr2
	 *            The second array.
	 * @return Maximum possible similarity between the two arrays.
	 */
	double getMaxValue(int[] arr1, int[] arr2);

}
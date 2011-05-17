/*
 * ===========================================================
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
 * Transformation.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.transformation;

import gtna.graph.Graph;

/**
 * Interface that must be implemented by all graph transformations.
 * 
 * @author benni
 * 
 */
public interface Transformation {
	/**
	 * Transforms the given graph and returns the transformed version. Note that
	 * in some cases, the given graph might simply be transformed and returned.
	 * Therefore, a copy of the original graph should be created if it should be
	 * processed afterwards.
	 * 
	 * @param g
	 *            graph to transform
	 * @return transformed graph (can be a transformed version of the old object
	 *         of a new object)
	 */
	public Graph transform(Graph g);

	/**
	 * Checks if the given graph is applicable to the transformation. E.g., to
	 * allow greedy routing nodes must implement the IDNode interface.
	 * 
	 * @param g
	 *            graph to check for applicability
	 * @return true of the graph is applicable for this transformation, false
	 *         otherwise
	 */
	public boolean applicable(Graph g);

	/**
	 * 
	 * @return key of the transformation (used in the configuration)
	 */
	public String key();

	/**
	 * 
	 * @return name of the transformation
	 */
	public String name();

	/**
	 * 
	 * @return long version of the name
	 */
	public String nameLong();

	/**
	 * 
	 * @return short version of the name
	 */
	public String nameShort();

	/**
	 * 
	 * @return part of the folder name representing the transformation
	 */
	public String folder();

	/**
	 * 
	 * @return configuration keys of the transformation's configuration
	 *         parameters
	 */
	public String[] configKeys();

	/**
	 * 
	 * @return configuration parameters of the transformation's instance
	 */
	public String[] configValues();

	/**
	 * 
	 * @param t
	 *            transformation to compare to
	 * @return first configuration parameter (actual value) that differs between
	 *         the two compared transformations
	 */
	public String compareValue(Transformation t);

	/**
	 * 
	 * @param t
	 *            transformation to compare to
	 * @param key
	 *            representation of the name
	 * @return name / type of the first configuration parameter that differs
	 *         between the two compared transformations
	 */
	public String compareName(Transformation t, String key);
}

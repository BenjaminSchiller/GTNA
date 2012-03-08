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
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;

/**
 * Interface that must be implemented by all graph transformations.
 * 
 * @author benni
 * 
 */
public abstract class Transformation extends ParameterList {
	public Transformation(String key){
		super(key);
	}
	
	public Transformation(String key, Parameter[] parameters) {
		super(key, parameters);
	}

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
	public abstract Graph transform(Graph g);

	/**
	 * Checks if the given graph is applicable to the transformation. E.g., to
	 * allow greedy routing nodes must implement the IDNode interface.
	 * 
	 * @param g
	 *            graph to check for applicability
	 * @return true of the graph is applicable for this transformation, false
	 *         otherwise
	 */
	public abstract boolean applicable(Graph g);
}

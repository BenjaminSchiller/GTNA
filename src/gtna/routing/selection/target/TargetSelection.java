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
 * TargetSelection.java
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
package gtna.routing.selection.target;

import gtna.graph.Graph;
import gtna.id.Identifier;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;

import java.util.Random;

/**
 * @author benni
 * 
 */
public abstract class TargetSelection extends ParameterList {

	protected Graph graph;

	protected Random rand;

	public TargetSelection(String key) {
		super(key);
		this.rand = new Random();
	}

	public TargetSelection(String key, Parameter[] parameters) {
		super(key, parameters);
	}

	public void init(Graph graph) {
		this.graph = graph;
	}

	public abstract Identifier getNextTarget();

	public abstract boolean applicable(Graph graph);

}

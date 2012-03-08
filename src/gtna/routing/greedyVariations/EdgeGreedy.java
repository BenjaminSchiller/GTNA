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
 * EdgeGreedy.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.routing.greedyVariations;

import gtna.util.parameter.Parameter;

import java.util.HashMap;
import java.util.Vector;

/**
 * abstract class for variations marking edges
 * 
 * @author stefanie
 * 
 */
public abstract class EdgeGreedy extends GreedyTemplate {
	HashMap<Integer, Vector<Integer>> from;

	public EdgeGreedy(String name) {
		super(name);
	}

	public EdgeGreedy(int ttl, String name) {
		super(ttl, name);
	}

	public EdgeGreedy(String name, Parameter[] parameters) {
		super(name, parameters);
	}

	public EdgeGreedy(int ttl, String name, Parameter[] parameters) {
		super(ttl, name, parameters);
	}

	@Override
	public void setSets(int nr) {
		from = new HashMap<Integer, Vector<Integer>>();
	}

}

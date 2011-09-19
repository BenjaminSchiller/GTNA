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
 * ObfuscatedLookaheadRouting.java
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
package gtna.transformation.lookahead;

import gtna.graph.Graph;
import gtna.id.ID;
import gtna.transformation.TransformationImpl;

/**
 * @author benni
 * 
 */
public abstract class ObfuscatedLookaheadRouting extends TransformationImpl {
	protected double minEpsilon;

	protected double maxEpsilon;

	public ObfuscatedLookaheadRouting(String key, double minEpsilon,
			double maxEpsilon, String[] configKeys, String[] configValues) {
		super(key, ObfuscatedLookaheadRouting.add(configKeys, "MIN_EPSILON",
				"MAX_EPSILON"), ObfuscatedLookaheadRouting.add(configValues, ""
				+ minEpsilon, "" + maxEpsilon));
		this.minEpsilon = minEpsilon;
		this.maxEpsilon = maxEpsilon;
	}

	private static String[] add(String[] values, String v1, String v2) {
		if (values.length == 0) {
			return new String[] { v1, v2 };
		}
		String[] newValues = new String[values.length + 2];
		for (int i = 0; i < values.length; i++) {
			newValues[i] = values[i];
		}
		newValues[values.length] = v1;
		newValues[values.length + 1] = v2;
		return newValues;
	}

	protected ID obfuscateID(ID id) {
		// TODO implement
		return null;
	}

	@Override
	public boolean applicable(Graph g) {
		// TODO Auto-generated method stub
		return false;
	}

}

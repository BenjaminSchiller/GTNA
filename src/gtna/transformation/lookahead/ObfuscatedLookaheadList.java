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
import gtna.graph.GraphProperty;
import gtna.id.BIID;
import gtna.id.BIIDSpace;
import gtna.id.DID;
import gtna.id.DIDSpace;
import gtna.id.Identifier;
import gtna.id.plane.PlaneID;
import gtna.id.ring.RingID;
import gtna.networks.p2p.chord.ChordID;
import gtna.transformation.TransformationImpl;

import java.util.Random;

/**
 * @author benni
 * 
 */
public abstract class ObfuscatedLookaheadList extends TransformationImpl {
	protected double minEpsilon;

	protected double maxEpsilon;

	protected double size;

	protected ObfuscatedLookaheadList(String key, double minEpsilon,
			double maxEpsilon) {
		super(key, new String[] { "MIN_EPSILON", "MAX_EPSILON" }, new String[] {
				"" + minEpsilon, "" + maxEpsilon });
		this.minEpsilon = minEpsilon;
		this.maxEpsilon = maxEpsilon;
		this.size = maxEpsilon - minEpsilon;
	}

	protected ObfuscatedLookaheadList(String key, String[] configKeys,
			String[] configValues) {
		super(key, configKeys, configValues);
		this.minEpsilon = 0;
		this.maxEpsilon = 0;
		this.size = 0;
	}

	protected ObfuscatedLookaheadList(String key, double minEpsilon,
			double maxEpsilon, String[] configKeys, String[] configValues) {
		super(key, ObfuscatedLookaheadList.add(configKeys, "MIN_EPSILON",
				"MAX_EPSILON"), ObfuscatedLookaheadList.add(configValues, ""
				+ minEpsilon, "" + maxEpsilon));
		this.minEpsilon = minEpsilon;
		this.maxEpsilon = maxEpsilon;
		this.size = maxEpsilon - minEpsilon;
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

	@SuppressWarnings("rawtypes")
	protected Identifier obfuscateID(Identifier id, Random rand) {
		if (id instanceof RingID) {
			RingID ID = (RingID) id;
			double sign = rand.nextBoolean() ? 1.0 : -1.0;
			double epsilon = minEpsilon + rand.nextDouble() * this.size;
			double position = ID.getPosition() + sign * epsilon;
			return new RingID(position, ID.getIdSpace());
		} else if (id instanceof ChordID) {
			// TODO implement
			return null;
		} else if (id instanceof PlaneID) {
			// TODO implement
			return null;
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	protected Identifier copyID(Identifier id) {
		if (id instanceof RingID) {
			RingID ID = (RingID) id;
			return new RingID(ID.getPosition(), ID.getIdSpace());
		} else if (id instanceof ChordID) {
			ChordID ID = (ChordID) id;
			return new ChordID(ID.getIdSpace(), ID.getId());
		} else if (id instanceof PlaneID) {
			PlaneID ID = (PlaneID) id;
			return new PlaneID(ID.getX(), ID.getY(), ID.getIdSpace());
		} else {
			return null;
		}
	}

	@Override
	public boolean applicable(Graph g) {
		Random rand = new Random();
		for (GraphProperty p : g.getProperties("ID_SPACE")) {
			if (p instanceof DIDSpace) {
				DID id = (DID) ((DIDSpace) p).randomID(rand);
				if (!(id instanceof RingID)) {
					return false;
				}
			} else if (p instanceof BIIDSpace) {
				BIID id = (BIID) ((BIIDSpace) p).randomID(rand);
				if (!(id instanceof ChordID)) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

}

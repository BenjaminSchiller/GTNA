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
import gtna.id.BIIdentifier;
import gtna.id.BIIdentifierSpace;
import gtna.id.DIdentifier;
import gtna.id.DIdentifierSpace;
import gtna.id.Partition;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDPartitionSimple;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlanePartitionSimple;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingPartition;
import gtna.id.ring.RingPartitionSimple;
import gtna.networks.p2p.chord.ChordIdentifier;
import gtna.networks.p2p.chord.ChordPartition;
import gtna.transformation.TransformationImpl;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author benni
 * 
 */
public abstract class ObfuscatedLookaheadList extends TransformationImpl {
	protected double minEpsilon;

	protected double maxEpsilon;

	protected double size;

	protected int minBits;

	protected int maxBits;

	protected int diff;

	protected BigInteger min;

	protected ObfuscatedLookaheadList(String key, int minBits, int maxBits) {
		super(key, new String[] { "MIN_EPSILON", "MAX_EPSILON" }, new String[] {
				"" + minBits, "" + maxBits });
		System.out.println("BI");
		this.minBits = minBits;
		this.maxBits = maxBits;
		this.diff = maxBits - minBits;
		if (this.minBits == 0) {
			this.min = BigInteger.ZERO;
		} else {
			this.min = BigInteger.ONE.shiftLeft(minBits);
		}
	}

	protected ObfuscatedLookaheadList(String key, double minEpsilon,
			double maxEpsilon) {
		super(key, new String[] { "MIN_EPSILON", "MAX_EPSILON" }, new String[] {
				"" + minEpsilon, "" + maxEpsilon });
		System.out.println("D");
		this.minEpsilon = minEpsilon;
		this.maxEpsilon = maxEpsilon;
		this.size = maxEpsilon - minEpsilon;
	}

	protected ObfuscatedLookaheadList(String key, String[] configKeys,
			String[] configValues) {
		super(key, configKeys, configValues);
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

	protected ObfuscatedLookaheadList(String key, int minBits, int maxBits,
			String[] configKeys, String[] configValues) {
		super(key, ObfuscatedLookaheadList.add(configKeys, "MIN_EPSILON",
				"MAX_EPSILON"), ObfuscatedLookaheadList.add(configValues, ""
				+ minBits, "" + maxBits));
		this.minBits = minBits;
		this.maxBits = maxBits;
		this.diff = maxBits - minBits;
		if (this.minBits == 0) {
			this.min = BigInteger.ZERO;
		} else {
			this.min = BigInteger.ONE.shiftLeft(minBits);
		}
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
	protected Partition obfuscatePartition(Partition partition, Random rand) {
		if (this.minBits == 0 && this.maxBits == 0 && this.minEpsilon == 0.0
				&& this.maxEpsilon == 0.0) {
			return this.copyPartition(partition);
		}
		if (partition instanceof RingPartitionSimple) {
			RingPartitionSimple p = (RingPartitionSimple) partition;
			double sign = rand.nextBoolean() ? 1.0 : -1.0;
			double epsilon = minEpsilon + rand.nextDouble() * this.size;
			double position = p.getId().getPosition() + sign * epsilon;
			return new RingPartitionSimple(new RingIdentifier(position, p
					.getId().getIdSpace()));
		} else if (partition instanceof RingPartition) {
			RingPartition p = (RingPartition) partition;
			double sign1 = rand.nextBoolean() ? 1.0 : -1.0;
			double epsilon1 = minEpsilon + rand.nextDouble() * this.size;
			double position1 = p.getStart().getPosition() + sign1 * epsilon1;
			double sign2 = rand.nextBoolean() ? 1.0 : -1.0;
			double epsilon2 = minEpsilon + rand.nextDouble() * this.size;
			double position2 = p.getEnd().getPosition() + sign2 * epsilon2;
			return new RingPartition(new RingIdentifier(position1, p.getStart()
					.getIdSpace()), new RingIdentifier(position2, p.getEnd()
					.getIdSpace()));
		} else if (partition instanceof PlanePartitionSimple) {
			PlanePartitionSimple p = (PlanePartitionSimple) partition;
			double sign1 = rand.nextBoolean() ? 1.0 : -1.0;
			double epsilon1 = minEpsilon + rand.nextDouble() * this.size;
			double position1 = p.getId().getX() + sign1 * epsilon1;
			double sign2 = rand.nextBoolean() ? 1.0 : -1.0;
			double epsilon2 = minEpsilon + rand.nextDouble() * this.size;
			double position2 = p.getId().getY() + sign2 * epsilon2;
			return new PlanePartitionSimple(new PlaneIdentifier(position1,
					position2, p.getId().getIdSpace()));
		} else if (partition instanceof ChordPartition) {
			ChordPartition p = (ChordPartition) partition;
			BigInteger epsilon1 = new BigInteger(this.diff, rand);
			BigInteger position1 = rand.nextBoolean() ? p.getPred().getId()
					.add(this.min).add(epsilon1)
					.mod(p.getPred().getIdSpace().getModulus()) : p.getPred()
					.getId().subtract(this.min).subtract(epsilon1).abs()
					.mod(p.getPred().getIdSpace().getModulus());
			BigInteger epsilon2 = new BigInteger(this.diff, rand);
			BigInteger position2 = rand.nextBoolean() ? p.getSucc().getId()
					.add(this.min).add(epsilon2)
					.mod(p.getSucc().getIdSpace().getModulus()) : p.getSucc()
					.getId().subtract(this.min).subtract(epsilon2)
					.mod(p.getSucc().getIdSpace().getModulus());
			return new ChordPartition(new ChordIdentifier(p.getPred()
					.getIdSpace(), position1), new ChordIdentifier(p.getSucc()
					.getIdSpace(), position2));
		} else {
			throw new RuntimeException("Cannot create obfuscated partition for " + partition.getClass());
		}
	}

	@SuppressWarnings("rawtypes")
	protected Partition copyPartition(Partition partition) {
		if (partition instanceof RingPartitionSimple) {
			RingPartitionSimple p = (RingPartitionSimple) partition;
			return new RingPartitionSimple(new RingIdentifier(p.getId()
					.getPosition(), p.getId().getIdSpace()));
		} else if (partition instanceof RingPartition) {
			RingPartition p = (RingPartition) partition;
			return new RingPartition(new RingIdentifier(p.getStart()
					.getPosition(), p.getStart().getIdSpace()),
					new RingIdentifier(p.getEnd().getPosition(), p.getEnd()
							.getIdSpace()));
		} else if (partition instanceof PlanePartitionSimple) {
			PlanePartitionSimple p = (PlanePartitionSimple) partition;
			return new PlanePartitionSimple(new PlaneIdentifier(p.getId()
					.getX(), p.getId().getY(), p.getId().getIdSpace()));
		} else if (partition instanceof ChordPartition) {
			ChordPartition p = (ChordPartition) partition;
			return new ChordPartition(new ChordIdentifier(p.getPred()
					.getIdSpace(), p.getPred().getId()), new ChordIdentifier(p
					.getSucc().getIdSpace(), p.getSucc().getId()));
		} else if (partition instanceof MDPartitionSimple) {
			MDPartitionSimple p = (MDPartitionSimple) partition;
			return new MDPartitionSimple(new MDIdentifier(p.getId().getCoordinates(), p.getId().getIdSpace()));
		} else {
			throw new RuntimeException("Cannot copy partition for " + partition.getClass());
		}
	}

	@Override
	public boolean applicable(Graph g) {
		Random rand = new Random();
		for (GraphProperty p : g.getProperties("ID_SPACE")) {
			if (p instanceof DIdentifierSpace) {
				DIdentifier id = (DIdentifier) ((DIdentifierSpace) p)
						.randomID(rand);
				if (!(id instanceof RingIdentifier) && !(id instanceof MDIdentifier) && !(id instanceof PlaneIdentifier)) {
					return false;
				}
			} else if (p instanceof BIIdentifierSpace) {
				BIIdentifier id = (BIIdentifier) ((BIIdentifierSpace) p)
						.randomID(rand);
				if (!(id instanceof ChordIdentifier)) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

}

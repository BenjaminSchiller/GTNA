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
 * ChordIdentifierSpace.java
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
package gtna.networks.p2p.chord;

import gtna.graph.Graph;
import gtna.id.BIIdentifier;
import gtna.id.BIIdentifierSpace;
import gtna.id.BIPartition;
import gtna.id.Partition;
import gtna.id.ring.RingPartition;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class ChordIdentifierSpace implements BIIdentifierSpace {
	private int bits;

	private BigInteger modulus;

	private ChordPartition[] partitions;

	public ChordIdentifierSpace() {
		this.bits = 0;
		this.modulus = BigInteger.ZERO;
	}

	public ChordIdentifierSpace(int bits) {
		this.bits = bits;
		this.modulus = BigInteger.ONE.add(BigInteger.ONE).pow(this.bits);
	}

	@Override
	public BIPartition[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(Partition<BigInteger>[] partitions) {
		this.partitions = (ChordPartition[]) partitions;
	}

	@Override
	public BIIdentifier randomID(Random rand) {
		return ChordIdentifier.rand(rand, this);
	}

	@Override
	public BigInteger getMaxDistance() {
		return this.modulus;
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		// CLASS
		fw.writeComment(Config.get("GRAPH_PROPERTY_CLASS"));
		fw.writeln(this.getClass().getCanonicalName().toString());

		// KEY
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);

		// # BITS
		fw.writeComment("Bits");
		fw.writeln(this.bits);

		// # MODULUS
		fw.writeComment("Modulus");
		fw.writeln(this.modulus.toString());

		// # PARTITIONS
		fw.writeComment("Partitions");
		fw.writeln(this.partitions.length);

		fw.writeln();

		// PARTITIONS
		int index = 0;
		for (ChordPartition p : this.partitions) {
			fw.writeln(index++ + ":" + p.toString());
		}

		return fw.close();
	}

	@Override
	public void read(String filename, Graph graph) {
		Filereader fr = new Filereader(filename);

		// CLASS
		fr.readLine();

		// KEY
		String key = fr.readLine();

		// # BITS
		this.bits = Integer.parseInt(fr.readLine());

		// # MUDULUS
		this.modulus = new BigInteger(fr.readLine());

		// # PARTITIONS
		int partitions = Integer.parseInt(fr.readLine());
		this.partitions = new ChordPartition[partitions];

		// PARTITIONS
		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(":");
			int index = Integer.parseInt(temp[0]);
			this.partitions[index] = new ChordPartition(temp[1], this);
		}

		fr.close();

		graph.addProperty(key, this);
	}

	/**
	 * @return the bits
	 */
	public int getBits() {
		return this.bits;
	}

	/**
	 * @param bits
	 *            the bits to set
	 */
	public void setBits(int bits) {
		this.bits = bits;
	}

	/**
	 * @return the modulus
	 */
	public BigInteger getModulus() {
		return this.modulus;
	}

	/**
	 * @param modulus
	 *            the modulus to set
	 */
	public void setModulus(BigInteger modulus) {
		this.modulus = modulus;
	}

	public String toString() {
		return this.bits + "-bit (% " + this.modulus + ")";
	}

	public ChordIdentifierSpace clone() {
		ChordIdentifierSpace result = new ChordIdentifierSpace(this.bits);
		result.setPartitions(this.partitions.clone());
		return result;
	}
}

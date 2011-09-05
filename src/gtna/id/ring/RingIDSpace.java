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
 * RingIDSpace.java
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
package gtna.id.ring;

import gtna.graph.Graph;
import gtna.id.IDSpace;
import gtna.id.Partition;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RingIDSpace implements IDSpace {
	private RingPartition[] partitions;
	
	protected double modulus;
	
	protected boolean wrapAround;
	
	protected double maxDistance;

	public RingIDSpace() {
		this.partitions = new RingPartition[] {};
		this.modulus = Double.MAX_VALUE;
		this.wrapAround = false;
		this.maxDistance = Double.MAX_VALUE;
	}

	public RingIDSpace(RingPartition[] partitions, double modulus, boolean wrapAround) {
		this.partitions = partitions;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.maxDistance = this.wrapAround ? this.modulus / 2.0 : this.modulus;
	}

	@Override
	public Partition[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(Partition[] partitions) {
		this.partitions = (RingPartition[]) partitions;
	}

	@Override
	public RingID randomID(Random rand) {
		return RingID.rand(rand, this);
	}

	@Override
	public double getMaxDistance() {
		return this.maxDistance;
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

		// # MODULUS
		fw.writeComment("Modulus");
		fw.writeln(this.modulus);

		// # WRAP-AROUND
		fw.writeComment("Wrap-around");
		fw.writeln(this.wrapAround + "");

		// # PARTITIONS
		fw.writeComment("Partitions");
		fw.writeln(this.partitions.length);

		fw.writeln();

		// PARTITIONS
		int index = 0;
		for (RingPartition p : this.partitions) {
			fw.writeln(index++ + ":" + p.getStringRepresentation());
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

		// # MUDULUS
		this.modulus = Double.parseDouble(fr.readLine());

		// # WRAP-AROUND
		this.wrapAround = Boolean.parseBoolean(fr.readLine());

		// # PARTITIONS
		int partitions = Integer.parseInt(fr.readLine());
		this.partitions = new RingPartition[partitions];

		this.maxDistance = this.wrapAround ? this.modulus / 2.0 : this.modulus;

		// PARTITIONS
		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(":");
			int index = Integer.parseInt(temp[0]);
			this.partitions[index] = new RingPartition(temp[1], this);
		}

		fr.close();

		graph.addProperty(key, this);
	}

	/**
	 * @return the modulus
	 */
	public double getModulus() {
		return this.modulus;
	}

	/**
	 * @param modulus the modulus to set
	 */
	public void setModulus(double modulus) {
		this.modulus = modulus;
	}

	/**
	 * @return the wrapAround
	 */
	public boolean isWrapAround() {
		return this.wrapAround;
	}

	/**
	 * @param wrapAround the wrapAround to set
	 */
	public void setWrapAround(boolean wrapAround) {
		this.wrapAround = wrapAround;
	}

}

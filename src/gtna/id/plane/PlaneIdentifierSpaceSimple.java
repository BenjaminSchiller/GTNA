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
 * PlaneIdentifierSpaceSimple.java
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
package gtna.id.plane;

import gtna.graph.Graph;
import gtna.id.DIdentifier;
import gtna.id.DIdentifierSpace;
import gtna.id.DPartition;
import gtna.id.Partition;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class PlaneIdentifierSpaceSimple extends DIdentifierSpace {
	private PlanePartitionSimple[] partitions;

	private double modulusX;

	private double modulusY;

	private boolean wrapAround;

	private double maxDistance;

	public PlaneIdentifierSpaceSimple() {
		this.partitions = new PlanePartitionSimple[] {};
		this.modulusX = Double.MAX_VALUE;
		this.modulusY = Double.MAX_VALUE;
		this.wrapAround = true;
		this.maxDistance = Double.MAX_VALUE;
	}

	public PlaneIdentifierSpaceSimple(PlanePartitionSimple[] partitions,
			double modulusX, double modulusY, boolean wrapAround) {
		this.partitions = partitions;
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.wrapAround = wrapAround;
		if (this.wrapAround) {
			this.maxDistance = Math.sqrt(Math.pow(this.modulusX / 2.0, 2)
					+ Math.pow(this.modulusY / 2.0, 2));
		} else {
			this.maxDistance = Math.sqrt(Math.pow(this.modulusX, 2)
					+ Math.pow(this.modulusY, 2));
		}
	}

	@Override
	public DPartition[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(Partition<Double>[] partitions) {
		this.partitions = (PlanePartitionSimple[]) partitions;
	}

	@Override
	public DIdentifier randomID(Random rand) {
		return this.partitions[rand.nextInt(this.partitions.length)].getId();
	}

	@Override
	public Double getMaxDistance() {
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

		// # MODULUS_X
		fw.writeComment("Modulus-X");
		fw.writeln(this.modulusX);

		// # MODULUS_Y
		fw.writeComment("Modulus-Y");
		fw.writeln(this.modulusY);

		// # WRAP-AROUND
		fw.writeComment("Wrap-around");
		fw.writeln(this.wrapAround + "");

		// # PARTITIONS
		fw.writeComment("Partitions");
		fw.writeln(this.partitions.length);

		fw.writeln();

		// PARTITIONS
		int index = 0;
		for (PlanePartitionSimple p : this.partitions) {
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

		// # MUDULUS_X
		this.modulusX = Double.parseDouble(fr.readLine());

		// # MUDULUS_Y
		this.modulusY = Double.parseDouble(fr.readLine());

		// # WRAP-AROUND
		this.wrapAround = Boolean.parseBoolean(fr.readLine());

		// # PARTITIONS
		int partitions = Integer.parseInt(fr.readLine());
		this.partitions = new PlanePartitionSimple[partitions];

		if (this.wrapAround) {
			this.maxDistance = Math.sqrt(Math.pow(this.modulusX / 2.0, 2)
					+ Math.pow(this.modulusY / 2.0, 2));
		} else {
			this.maxDistance = Math.sqrt(Math.pow(this.modulusX, 2)
					+ Math.pow(this.modulusY, 2));
		}

		// PARTITIONS
		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(":");
			int index = Integer.parseInt(temp[0]);
			this.partitions[index] = new PlanePartitionSimple(temp[1], this);
		}

		fr.close();

		graph.addProperty(key, this);
	}

	/**
	 * @return the modulusX
	 */
	public double getModulusX() {
		return this.modulusX;
	}

	/**
	 * @return the modulusY
	 */
	public double getModulusY() {
		return this.modulusY;
	}

	/**
	 * @return the wrapAround
	 */
	public boolean isWrapAround() {
		return this.wrapAround;
	}
}

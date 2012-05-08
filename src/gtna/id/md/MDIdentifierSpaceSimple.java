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
 * MDIdentifierSpaceSimple.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.id.md;

import java.util.Arrays;
import java.util.Random;

import gtna.graph.Graph;
import gtna.id.DIdentifier;
import gtna.id.DIdentifierSpace;
import gtna.id.DPartition;
import gtna.id.Partition;
import gtna.id.plane.PlanePartitionSimple;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * @author Nico
 * 
 */
public class MDIdentifierSpaceSimple implements DIdentifierSpace {
	private MDPartitionSimple[] partitions;

	private double[] modulus;

	private boolean wrapAround;

	private double maxDistance;

	private DistanceMD distFunc;

	public static enum DistanceMD {
		EUCLIDEAN, MANHATTAN
	}

	public MDIdentifierSpaceSimple() {
		this.partitions = new MDPartitionSimple[] {};
		this.wrapAround = true;
		this.distFunc = DistanceMD.EUCLIDEAN;
		this.computeMaxDist();
	}

	public MDIdentifierSpaceSimple(MDPartitionSimple[] partitions,
			double[] modulus, boolean wrapAround, DistanceMD dist) {
		this.partitions = partitions;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.distFunc = dist;
		this.computeMaxDist();
	}
	
	public MDIdentifierSpaceSimple(MDPartitionSimple[] partitions,
			double[] modulus, boolean wrapAround) {
		this.partitions = partitions;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.distFunc = DistanceMD.EUCLIDEAN;
		this.computeMaxDist();
	}

	private void computeMaxDist() {
		if (this.modulus == null) {
			this.maxDistance = Double.MAX_VALUE;
		} else {
			this.maxDistance = 0;
			if (this.wrapAround) {
				if (this.distFunc == DistanceMD.MANHATTAN) {
					for (int i = 0; i < modulus.length; i++) {
						this.maxDistance = this.maxDistance + modulus[i]
								/ (double) 2;
					}
				}
				if (this.distFunc == DistanceMD.EUCLIDEAN) {
					for (int i = 0; i < modulus.length; i++) {
						this.maxDistance = this.maxDistance + modulus[i]
								* modulus[i] / (double) 4;
					}
					this.maxDistance = Math.sqrt(this.maxDistance);
				}
			} else {
				if (this.distFunc == DistanceMD.MANHATTAN) {
					for (int i = 0; i < modulus.length; i++) {
						this.maxDistance = this.maxDistance + modulus[i];
					}
				}
				if (this.distFunc == DistanceMD.EUCLIDEAN) {
					for (int i = 0; i < modulus.length; i++) {
						this.maxDistance = this.maxDistance + modulus[i]
								* modulus[i];
					}
					this.maxDistance = Math.sqrt(this.maxDistance);
				}
			}
		}
	}

	/**
	 * @return
	 */
	public double[] getModuli() {
		return this.modulus;
	}

	public double getModulus(int i) {
		return this.modulus[i];
	}

	public double getMaxModulus() {
		double result = Double.MIN_VALUE;
		for (double x : modulus) {
			result = Math.max(result, x);
		}
		return result;
	}

	public double getMinModulus() {
		double result = Double.MAX_VALUE;
		for (double x : modulus) {
			result = Math.min(result, x);
		}
		return result;
	}

	public int getDimensions() {
		return modulus.length;
	}

	@Override
	public DPartition[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(Partition<Double>[] partitions) {
		this.partitions = (MDPartitionSimple[]) partitions;
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

		// # DIMENSIONS
		fw.writeComment("Dimensions");
		fw.writeln(this.getDimensions());

		// # MODULUS
		fw.writeComment("Modulus");
		fw.writeln(Arrays.toString(modulus));

		// # WRAP-AROUND
		fw.writeComment("Wrap-around");
		fw.writeln(this.wrapAround + "");

		// # PARTITIONS
		fw.writeComment("Partitions");
		fw.writeln(this.partitions.length);

		fw.writeln();

		// PARTITIONS
		int index = 0;
		for (MDPartitionSimple p : this.partitions) {
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

		// DIMENSIONS
		int dimensions = Integer.parseInt(fr.readLine());

		// # MODULUS_Y
		this.modulus = new double[dimensions];

		String[] modulusTemp = fr.readLine().replace("[", "").replace("]", "")
				.split(",");
		if (modulusTemp.length != dimensions) {
			throw new RuntimeException(
					"Error: written dimension does not match the number of written modulus values");
		}
		for (int i = 0; i < dimensions; i++) {
			this.modulus[i] = Double.parseDouble(modulusTemp[i]);
		}

		// # WRAP-AROUND
		this.wrapAround = Boolean.parseBoolean(fr.readLine());

		// # PARTITIONS
		int partitions = Integer.parseInt(fr.readLine());
		this.partitions = new MDPartitionSimple[partitions];

		// compute max distance
		this.computeMaxDist();

		// PARTITIONS
		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(":");
			int index = Integer.parseInt(temp[0]);
			this.partitions[index] = new MDPartitionSimple(temp[1], this);
		}

		fr.close();

		graph.addProperty(key, this);
	}

	public boolean isWrapAround() {
		return this.wrapAround;
	}

	/**
	 * @return the distance function
	 */
	public DistanceMD getDistFunc() {
		return distFunc;
	}

}

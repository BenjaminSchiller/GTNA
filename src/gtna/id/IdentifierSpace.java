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
 * IdentifierSpace.java
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
package gtna.id;

import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * @author benni
 * 
 */
public abstract class IdentifierSpace extends GraphProperty {

	public static final String delimiter = "_";

	protected Partition[] partitions;

	protected IdentifierSpace(Partition[] partitions) {
		this.partitions = partitions;
	}

	public Partition[] getPartitions() {
		return this.partitions;
	}

	public Partition getPartition(int node) {
		return this.partitions[node];
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		this.writeHeader(fw, this.getClass(), key);
		this.writeParameter(fw, "Partition count", this.partitions.length);
		this.writeParameter(fw, "Partition class",
				this.partitions[0].getClass());
		this.writeParameters(fw);

		for (Partition p : this.partitions) {
			fw.writeln(p.asString());
		}

		return fw.close();
	}

	@Override
	public String read(String filename) {
		Filereader fr = new Filereader(filename);

		String key = this.readHeader(fr);
		int partitionCount = this.readInt(fr);
		Class<?> partitionClass = this.readClass(fr);
		this.partitions = new Partition[partitionCount];
		this.readParameters(fr);

		for (int i = 0; i < partitionCount; i++) {
			try {
				Constructor<?> con = partitionClass
						.getConstructor(new Class[] { String.class });
				this.partitions[i] = (Partition) con
						.newInstance(new Object[] { fr.readLine() });
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		fr.close();

		return key;
	}

	/**
	 * Write all the parameters required for reading in this IdentifierSpace
	 * using .writeParameter(.).
	 * 
	 * @param fw
	 */
	protected abstract void writeParameters(Filewriter fw);

	/**
	 * Read all parameters required for initiating this IdentifierSpace.
	 * 
	 * @param fr
	 */
	protected abstract void readParameters(Filereader fr);

	/**
	 * 
	 * @param rand
	 * @return random identifier selected uniformly from all possible
	 *         identifiers from the identifier space
	 */
	public abstract Identifier getRandomIdentifier(Random rand);
}

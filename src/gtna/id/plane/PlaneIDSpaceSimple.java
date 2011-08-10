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
 * PlaneIDSpaceSimple.java
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
import gtna.id.ID;
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
public class PlaneIDSpaceSimple implements IDSpace {
	private PlanePartitionSimple[] partitions;

	public PlaneIDSpaceSimple() {
		this.partitions = new PlanePartitionSimple[] {};
	}

	public PlaneIDSpaceSimple(PlanePartitionSimple[] partitions) {
		this.partitions = partitions;
	}

	@Override
	public Partition[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(Partition[] partitions) {
		this.partitions = (PlanePartitionSimple[]) partitions;
	}

	@Override
	public ID randomID(Random rand) {
		return this.partitions[rand.nextInt(this.partitions.length)].getId();
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		// CLASS
		fw.writeComment(Config.get("GRAPH_PROPERTY_CLASS"));
		fw.writeln(this.getClass().getCanonicalName().toString());

		// KEYS
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);

		// # OF PARTITIONS
		fw.writeComment("Partitions");
		fw.writeln(this.partitions.length);

		// PARTITIONS
		int index = 0;
		for (PlanePartitionSimple p : this.partitions) {
			fw.writeln(index++ + ":" + p.getStringRepresentation());
		}

		return fw.close();
	}

	@Override
	public void read(String filename, Graph graph) {
		Filereader fr = new Filereader(filename);

		// CLASS
		fr.readLine();

		// KEYS
		String key = fr.readLine();

		// # OF PARTITIONS
		int partitions = Integer.parseInt(fr.readLine());
		this.partitions = new PlanePartitionSimple[partitions];

		// PARTITIONS
		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(":");
			int index = Integer.parseInt(temp[0]);
			this.partitions[index] = new PlanePartitionSimple(temp[1]);
		}

		fr.close();

		graph.addProperty(key, this);
	}

}

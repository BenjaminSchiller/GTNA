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
 * RangeProperty.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.placementmodels.connectors;

import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;

/**
 * The <code>RangeProperty</code> contains the radii of the disks of the disk
 * graph used to connect the nodes. Note that depending on the actual connector
 * that is setting this property, this might not be a correct assumption. Ranges
 * are stored for each node individually, there is a convenience constructor
 * <code>RangeProperty(double range, int nodes)</code> for cases in which the
 * range is the same for every node.
 * 
 * 
 * @author Philipp Neubrand
 * 
 */
public class RangeProperty extends GraphProperty {
	private double[] ranges;

	public RangeProperty() {
		this(new double[0]);
	}

	/**
	 * Convenience constructor, assumes that the range is the same for all the
	 * <code>count</code> nodes.
	 * 
	 * 
	 * @param range
	 *            The range for every node.
	 * @param count
	 *            The count for every node.
	 */
	public RangeProperty(double range, int count) {
		ranges = new double[count];
		for (int i = 0; i < count; i++)
			ranges[i] = range;
	}

	/**
	 * Standard constructor, stores the supplied ranges.
	 * 
	 * @param ranges
	 *            The ranges of the nodes.
	 */
	public RangeProperty(double[] ranges) {
		this.ranges = ranges;
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		this.writeHeader(fw, this.getClass(), key);

		this.writeParameter(fw, "Nodes", this.ranges.length);

		for (double d : this.ranges) {
			fw.writeln(d);
		}

		return fw.close();
	}

	@Override
	public String read(String filename) {
		Filereader fr = new Filereader(filename);

		String key = this.readHeader(fr);

		this.ranges = new double[this.readInt(fr)];

		String line = null;
		int index = 0;
		while ((line = fr.readLine()) != null) {
			this.ranges[index++] = Double.parseDouble(line);
		}

		fr.close();

		return key;
	}

	/**
	 * Getter for the ranges.
	 * 
	 * @return The ranges.
	 */
	public double[] getRanges() {
		return ranges;
	}

}

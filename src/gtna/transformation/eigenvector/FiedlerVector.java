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
 * FiedlerVector.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Sony;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.eigenvector;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.ring.RingPartitionSimple;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * @author stef adds fiedler vector of a graph as property
 */
public class FiedlerVector implements GraphProperty {
	private double[] vector;

	public FiedlerVector(double[] vector) {
		this.setVector(vector);
	}

	public FiedlerVector() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.graph.GraphProperty#read(java.lang.String, gtna.graph.Graph)
	 */
	@Override
	public void read(String filename, Graph graph) {
		Filereader fr = new Filereader(filename);
		// CLASS
		fr.readLine();

		// KEY
		String key = fr.readLine();

		this.vector = new double[graph.getNodes().length];

		// PARTITIONS
		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp = line.split(":");
			int index = Integer.parseInt(temp[0]);
			this.vector[index] = Double.parseDouble(temp[1]);
		}

		fr.close();
		graph.addProperty(key, this);

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

		fw.writeln();

		for (int i = 0; i < this.vector.length; i++) {
			fw.writeln(i + ":" + this.vector[i]);
		}

		return fw.close();
	}

	/**
	 * @param vector
	 *            the vector to set
	 */
	public void setVector(double[] vector) {
		this.vector = vector;
	}

	public double[] getVector() {
		return this.vector;
	}

}

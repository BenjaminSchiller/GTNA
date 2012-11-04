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
 * GraphSpectrum.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.eigenvector;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * @author stefanie
 *
 */
public class GraphSpectrum implements GraphProperty {
	private double[][] vector;
	private double[] vals;
	
	public GraphSpectrum(double[] vals, double[][] vector) {
		this.setSpectrum(vals,vector);
	}

	/**
	 * @param vals
	 * @param vector2
	 */
	private void setSpectrum(double[] vals, double[][] vector) {
		this.vals = vals;
		this.vector = vector;
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

		this.vals = new double[graph.getNodes().length];
		this.vector = new double[graph.getNodes().length][graph.getNodes().length];
		// PARTITIONS
		String line = fr.readLine();
		String[] temp = line.split(":");
		for (int i = 0; i < temp.length; i++){
			vals[i] = Double.parseDouble(temp[i]);
		}
		int count = 0;
		while ((line = fr.readLine()) != null) {
			temp = line.split(":");
			for (int i = 0; i < temp.length; i++){
				vector[count][i] = Double.parseDouble(temp[i]);
			}
			count++;
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

		String temp = ""+this.vals[0];
		for (int i = 0; i < this.vals.length; i++) {
			temp = temp + ":" + this.vals[i];
		}
		fw.writeln(temp);
		for (int i = 0; i < this.vals.length; i++) {
			temp = ""+this.vector[i][0];
			for (int j = 0; j < this.vals.length; j++) {
				temp = temp + ":" + this.vector[i][j];
			}
			fw.writeln(temp);
		}

		return fw.close();
	}

}	

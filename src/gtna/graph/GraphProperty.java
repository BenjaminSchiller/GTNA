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
 * GraphProperty.java
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
package gtna.graph;

import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public abstract class GraphProperty {
	/**
	 * Writes the GraphProperty into a file that should readable using the
	 * read(filename, graph) method. Always use the methods .writeHeader(.) and
	 * .writeParameter(.) to write the classname, key, and other parameters into
	 * the file before starting the main content.
	 * 
	 * @param filename
	 *            where to write the data
	 * @param key
	 *            key of this GraphProperty (must be added to the header of the
	 *            file)
	 * @return true if writing was successful; false otherwise
	 */
	public abstract boolean write(String filename, String key);

	/**
	 * Reads the GraphProperty from a file that should be written using
	 * .write(filename, key). Always use .readHeader(.) and read*(.) to read
	 * classname, key, and parameters before starting to read the content.
	 * 
	 * @param filename
	 *            where to read the GraphProperty from
	 * @param graph
	 *            Graph object to which the property should be added after
	 *            reading it
	 * @return key given in the file for adding it as a property to a graph
	 */
	public abstract String read(String filename);

	protected void writeHeader(Filewriter fw, Class<?> c, String key) {
		// CLASS
		fw.writeComment(Config.get("GRAPH_PROPERTY_CLASS"));
		fw.writeln(this.getClass().getCanonicalName().toString());

		// KEY
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);
	}

	protected void writeParameter(Filewriter fw, String name, String value) {
		fw.writeComment(name);
		fw.writeln(value);
	}

	protected void writeParameter(Filewriter fw, String name, boolean value) {
		this.writeParameter(fw, name, value + "");
	}

	protected void writeParameter(Filewriter fw, String name, double value) {
		this.writeParameter(fw, name, value + "");
	}

	protected void writeParameter(Filewriter fw, String name, int value) {
		this.writeParameter(fw, name, value + "");
	}

	protected void writeParameter(Filewriter fw, String name, Class<?> value) {
		this.writeParameter(fw, name, value.getCanonicalName().toString());
	}

	protected String readHeader(Filereader fr) {
		// CLASS
		fr.readLine();

		// KEY
		String key = fr.readLine();

		return key;
	}

	protected String readString(Filereader fr) {
		return fr.readLine();
	}

	protected boolean readBoolean(Filereader fr) {
		return Boolean.parseBoolean(this.readString(fr));
	}

	protected double readDouble(Filereader fr) {
		return Double.parseDouble(this.readString(fr));
	}

	protected int readInt(Filereader fr) {
		return Integer.parseInt(this.readString(fr));
	}

	protected Class<?> readClass(Filereader fr) {
		String className = fr.readLine();
		try {
			return ClassLoader.getSystemClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}

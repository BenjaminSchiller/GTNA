/*
 * ===========================================================
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
 * DataReader.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.io;

import gtna.util.Config;
import gtna.util.Util;

import java.util.ArrayList;

/**
 * Provides methods to read data from files written using the methods provided
 * by the DataWriter class.
 * 
 * The configuration parameter DATA_WRITER_DELIMITER is assumed as a delimiter
 * for separating multiple values on a single line.
 * 
 * @author benni
 * 
 */
public class DataReader {
	/**
	 * Reads a two-dimensional array of double values from the specified file.
	 * Each array entry represents the content of a single line which are
	 * separated using the delimiter DATA_WRITER_DELIMITER.
	 * 
	 * @param filename
	 *            file to read from
	 * @return two-dimensional double array of the values read from the
	 *         specified file
	 */
	public static double[][] readDouble2D(String filename) {
		ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
		Filereader fr = new Filereader(filename);
		String line;
		int index = -1;
		String delimiter = Config.get("DATA_WRITER_DELIMITER");
		while ((line = fr.readLine()) != null) {
			String[] elements = line.trim().split(delimiter);
			list.add(new ArrayList<Double>(elements.length));
			index++;
			for (int j = 0; j < elements.length; j++) {
				list.get(index).add(Double.valueOf(elements[j]));
			}
		}
		fr.close();
		return toDouble2D(list);
	}

	/**
	 * Reads the values of the multi-scalar metric specified by metricKey from
	 * the given folder. It is assumed that each line of the file contains
	 * exactly two entries. The first entry is used as the index in the array
	 * and the second as the value.
	 * 
	 * @param metricKey
	 *            key of the metric used in the configuration
	 * @param folder
	 *            folder where to read the file from
	 * @return single-dimensional double array read for the specified metric
	 */
	public static double[] readDouble(String metricKey, String folder) {
		return readDouble(DataWriter.filename(metricKey, folder));
	}

	/**
	 * Reads the values from the specified file. It is assumed that each line of
	 * the file contains exactly two entries. The first entry is used as the
	 * index in the array and the second as the value.
	 * 
	 * @param filename
	 *            file to read from
	 * @return single-dimensional double array read from the specified file
	 */
	public static double[] readDouble(String filename) {
		return Util.arrayFromIndex(readDouble2D(filename), 1);
	}

	/**
	 * Generates a two-dimensional double array from the given ArrayList object.
	 * 
	 * @param list
	 *            ArrayList to transform
	 * @return transformed two-dimensional double array
	 */
	private static double[][] toDouble2D(ArrayList<ArrayList<Double>> list) {
		double[][] array = new double[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			array[i] = new double[list.get(i).size()];
			for (int j = 0; j < list.get(i).size(); j++) {
				array[i][j] = list.get(i).get(j);
			}
		}
		return array;
	}
}

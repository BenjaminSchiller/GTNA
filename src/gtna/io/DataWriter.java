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
 * DataWriter.java
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

/**
 * Provides methods to write data to files which then can be read using the
 * methods provided by the DataReader class. It should be used by all metrics to
 * write the results of multi-scalar metrics in their writeData() method.
 * 
 * The configuration parameter DATA_WRITER_DELIMITER is used as a delimiter for
 * separating multiple values on a single line.
 * 
 * @author benni
 * 
 */
public class DataWriter {
	/**
	 * Writes the given data to the specified file. The flag addIndex can be
	 * used to add the array's index as the first value of each entry. If the
	 * flag is true, the values are separated using the delimiter defined by
	 * DATA_WRITER_DELIMITER.
	 * 
	 * If addIndex==true, a separate line of the form "i data[i][0]" is created
	 * for every value. If addIndex==false, each value is written as "data[i]".
	 * 
	 * @param values
	 *            double[] array containing the values
	 * @param dest
	 *            destination where to write the data
	 * @param addIndex
	 *            flag for adding the index at the beginning of each line
	 * @return true if the operation was successful, false otherwise
	 */
	public static boolean write(double[] values, String dest, boolean addIndex) {
		Filewriter fw = new Filewriter(dest);
		String delimiter = Config.get("DATA_WRITER_DELIMITER");
		for (int i = 0; i < values.length; i++) {
			if (addIndex) {
				fw.writeln(i + delimiter + values[i]);
			} else {
				fw.writeln(i + delimiter + values[i]);
			}
		}
		return fw.close();
	}

	/**
	 * Writes the given data to the specified file. The flag addIndex can be
	 * used to add the array's index as the first value of each entry. In each
	 * line, the values are separated using the delimiter defined by
	 * DATA_WRITER_DELIMITER.
	 * 
	 * If addIndex==true, a separate line of the form
	 * "i data[i][0] data[i][1] ... data[i][data[i].length-1]" is created for
	 * every entry. If addIndex==false, each entry is written as
	 * "data[i][0] data[i][1] ... data[i][data[i].length-1]".
	 * 
	 * @param values
	 *            double[][] array containing the data
	 * @param dest
	 *            destination where to write the data
	 * @param addIndex
	 *            flag for adding the index at the beginning of each line
	 * @return true if the operation was successful, false otherwise
	 */
	public static boolean write(double[][] values, String dest, boolean addIndex) {
		Filewriter fw = new Filewriter(dest);
		String delimiter = Config.get("DATA_WRITER_DELIMITER");
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				if (j == 0 && addIndex) {
					fw.write(i + delimiter + values[i][j]);
				} else if (j == 0) {
					fw.write("" + values[i][j]);
				} else {
					fw.write(delimiter + values[i][j]);
				}
			}
			fw.writeln("");
		}
		return fw.close();
	}

	/**
	 * Writes the given data for the metric identified by the metricKey using
	 * the extension DATA_EXTENSION to the filename specified in the
	 * configuration and stores it in the specified folder. The array's index of
	 * each value is also included, separated by the delimiter defined by
	 * DATA_WRITER_DELIMITER. It should be used for writing multi-scalar metrics
	 * whose x-value is represented by the array's index.
	 * 
	 * For every entry, a separate line of the form "i data[i]" is created.
	 * 
	 * @param values
	 *            double[] array containing the values of the metric
	 * @param metricKey
	 *            key of the metric used in the configuration
	 * @param folder
	 *            folder where to store the output file
	 * @return true if the operation was successful, false otherwise
	 */
	public static boolean writeWithIndex(double[] values, String metricKey,
			String folder) {
		return write(values, filename(metricKey, folder), true);
	}

	/**
	 * Writes the given data for the metric identified by the metricKey using
	 * the extension DATA_EXTENSION to the filename specified in the
	 * configuration and stores it in the specified folder. In each line, the
	 * values are separated using the delimiter defined by
	 * DATA_WRITER_DELIMITER. It should be used for writing multi-scalar metrics
	 * whose x-value may note be represented by the array's index. It is also
	 * used during the generation of a series to write the confidence intervals
	 * of multiple runs.
	 * 
	 * For every entry, a separate line of the form
	 * "data[i][0] data[i][1] ... data[i][data[i].length-1]" is created.
	 * 
	 * @param values
	 *            double[][] array containing the values to write
	 * @param metricKey
	 *            key of the metric used in the configuration
	 * @param folder
	 *            folder where to store the output file
	 * 
	 * @return true if the operation was successful, false otherwise
	 */
	public static boolean writeWithoutIndex(double[][] values,
			String metricKey, String folder) {
		return write(values, filename(metricKey, folder), false);
	}

	/**
	 * Generates the filename for the metric identified by the metricKey and
	 * appends it to the specified output folder. The filename for every
	 * multi-scalar metric is specified in the configuration files by the value
	 * metrikKey_DATA_FILENAME. The extension specified by DATA_EXTENSION is
	 * then appended to that filename.
	 * 
	 * @param metricKey
	 *            key of the metric used in the configuration files
	 * @param folder
	 *            destination folder
	 * @return filename where the specified metric is stored
	 */
	public static String filename(String metricKey, String folder) {
		return folder + Config.get(metricKey + "_DATA_FILENAME")
				+ Config.get("DATA_EXTENSION");
	}
}

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
 * Singles.java
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
package gtna.data;

import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.metrics.Metric;
import gtna.util.Config;
import gtna.util.Util;

import java.util.ArrayList;
import java.util.Arrays;

public class Singles {
	private String name;

	private Value[] values;

	private Singles(String name, Value[] values) {
		this.name = name;
		this.values = values;
	}

	public double getValue(String key) {
		return Value.get(this.values, key);
	}

	public Singles(String name, Metric[] metrics) {
		this.name = name;
		ArrayList<Value> values = new ArrayList<Value>();
		for (int i = 0; i < metrics.length; i++) {
			Value[] current = metrics[i].getValues(Util.toValueArray(values));
			for (int j = 0; j < current.length; j++) {
				if (current[j] != null && !Double.isNaN(current[j].value)) {
					values.add(current[j]);
				}
			}
		}
		this.values = Util.toValueArray(values);
	}

	public Singles(String filename) {
		ArrayList<Value> values = new ArrayList<Value>();
		String line;
		String delimiter = Config.get("SINGLES_DELIMITER");
		Filereader fr = new Filereader(filename);
		this.name = fr.readLine();
		while ((line = fr.readLine()) != null) {
			if (line.contains(delimiter)) {
				String key = line.split(delimiter)[0].trim();
				double value = Double.parseDouble(line.split(delimiter)[1]
						.trim());
				values.add(new Value(key, value));
			}
		}
		fr.close();
		this.values = new Value[values.size()];
		for (int i = 0; i < values.size(); i++) {
			this.values[i] = values.get(i);
		}
	}

	public void write(String filename) {
		String delimiter = Config.get("SINGLES_DELIMITER");
		Filewriter fw = new Filewriter(filename);
		fw.writeln(this.name);
		fw.writeln("");
		for (int i = 0; i < this.values.length; i++) {
			fw.writeln(this.values[i].key + delimiter + "  "
					+ this.values[i].value);
		}
		fw.close();
	}

	public void add(Value value) {
		Value[] temp = this.values;
		this.values = new Value[temp.length + 1];
		for (int i = 0; i < temp.length; i++) {
			this.values[i] = temp[i];
		}
		this.values[temp.length] = value;
	}

	public static Singles average(Singles[] summaries) {
		Value[] values = new Value[summaries[0].values.length];
		String name = "Average of " + summaries[0].name;
		for (int i = 0; i < summaries[0].values.length; i++) {
			double value = 0;
			for (int j = 0; j < summaries.length; j++) {
				value += summaries[j].values[i].value;
			}
			value /= (double) summaries.length;
			values[i] = new Value(summaries[0].values[i].key, value);
		}
		return new Singles(name, values);
	}

	public static void write(Series[] s, String dest) {
		Singles[][] summaries = new Singles[s.length][];
		String[] names = new String[s.length];
		for (int i = 0; i < s.length; i++) {
			summaries[i] = s[i].summaries();
			names[i] = s[i].network().description();
		}
		write(summaries, dest, names);
	}

	public static void write(Singles[][] summaries, String filename,
			String[] names) {
		Filewriter fw = new Filewriter(filename);

		int valueCount = summaries[0][0].values.length;
		for (int v = 0; v < valueCount; v++) {
			double[][] values = new double[summaries.length][];
			for (int i = 0; i < summaries.length; i++) {
				values[i] = new double[summaries[i].length];
				for (int j = 0; j < summaries[i].length; j++) {
					values[i][j] = summaries[i][j].values[v].value;
				}
			}
			String name = Config.get(summaries[0][0].values[v].key
					+ "_SINGLE_NAME");
			printData(values, name, names, fw);
		}
		fw.close();
	}

	private static void printData(double[][] values, String name,
			String[] names, Filewriter fw) {
		String line = Config.get("SINGLES_PRINT_LINE");
		String column = Config.get("SINGLES_PRINT_COLUMN");
		String space = Config.get("SINGLES_PRINT_SPACE");
		boolean sortValues = Config.getBoolean("SINGLES_PRINT_SORT_VALUES");
		String start = column + space;
		String middle = space + column + space;
		String end = space + column;

		double[] average = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			average[i] = Util.avg(values[i]);
		}
		int[] max = new int[values.length];
		int total = 0;
		for (int i = 0; i < values.length; i++) {
			max[i] = Math.max(Math.max(names[i].length(),
					getMaxLength(values[i])), (average[i] + "").length());
			total += max[i];
		}
		total += start.length();
		total += middle.length() * (max.length - 1);
		total += end.length();
		/**
		 * NAME
		 */
		fw.writeln(line(total, line));
		fw.write(start);
		int firstHalf = (int) Math.floor((double) (total - start.length()
				- end.length() - name.length())
				/ (double) 2);
		int secondHalf = (int) Math.ceil((double) (total - start.length()
				- end.length() - name.length())
				/ (double) 2);
		fw.write(space(firstHalf, space));
		fw.write(name);
		fw.write(space(secondHalf, space));
		fw.writeln(end);
		fw.writeln(line(total, line));
		/**
		 * NAMES
		 */
		fw.write(start);
		for (int i = 0; i < names.length; i++) {
			if (i > 0) {
				fw.write(middle);
			}
			int firstH = (int) Math.floor((double) (max[i] - names[i].length())
					/ (double) 2);
			int secondH = (int) Math.ceil((double) (max[i] - names[i].length())
					/ (double) 2);
			fw.write(space(firstH, space));
			fw.write(names[i]);
			fw.write(space(secondH, space));
		}
		fw.writeln(end);
		fw.writeln(line(total, line));
		/**
		 * VALUES
		 */
		if (sortValues) {
			for (int i = 0; i < values.length; i++) {
				Arrays.sort(values[i]);
			}
		}
		int maxIndex = getMaxIndex(values);
		for (int j = 0; j < maxIndex; j++) {
			fw.write(start);
			for (int i = 0; i < values.length; i++) {
				if (i > 0) {
					fw.write(middle);
				}
				if (values[i].length > j) {
					fw.write(values[i][j] + "");
					fw
							.write(space(max[i] - (values[i][j] + "").length(),
									space));
				} else {
					fw.write(space(max[i], space));
				}
			}
			fw.writeln(end);
		}
		fw.writeln(line(total, line));
		/**
		 * AVERAGE
		 */
		fw.write(start);
		for (int i = 0; i < average.length; i++) {
			if (i > 0) {
				fw.write(middle);
			}
			fw.write(average[i] + "");
			fw.write(space(max[i] - (average[i] + "").length(), space));
		}
		fw.writeln(end);
		fw.writeln(line(total, line));
		fw.writeln("\n");
	}

	private static String space(int length, String space) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buff.append(space);
		}
		return buff.toString();
	}

	private static String line(int length, String line) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buff.append(line);
		}
		return buff.toString();
	}

	private static int getMaxLength(double[] values) {
		int max = 0;
		for (int i = 0; i < values.length; i++) {
			int length = (values[i] + "").length();
			if (max < length) {
				max = length;
			}
		}
		return max;
	}

	private static int getMaxIndex(double[][] values) {
		int max = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i].length > max) {
				max = values[i].length;
			}
		}
		return max;
	}

	public Value[] values() {
		return this.values;
	}

	public static double[] getValues(Singles[] singles, String key) {
		double[] values = new double[singles.length];
		for (int i = 0; i < singles.length; i++) {
			values[i] = singles[i].getValue(key);
		}
		return values;
	}
}

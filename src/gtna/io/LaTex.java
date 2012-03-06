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
 * LaTex.java
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

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.util.Config;

import java.text.DecimalFormat;

public class LaTex {
	/**
	 * PLOTS
	 */

	public static void writeDataPlots(String filename, String folder,
			Series[] s, Metric[] metrics) {
		String nwDescription = s[0].network().description(
				Config.get("LATEX_PLOTS_DESCRIPTION_TYPE"),
				s[1 % s.length].network());
		writePlot(filename, folder, nwDescription,
				Config.allKeys("_DATA_PLOTS", metrics), metrics);
	}

	public static void writeSinglePlots(String filename, String folder,
			Series[][] s, Metric[] metrics) {
		String nwDescription = s[0][0].network().description(
				Config.get("LATEX_PLOTS_DESCRIPTION_TYPE"),
				s[0][1 % s[0].length].network(), s[1 % s.length][0].network());
		writePlot(filename, folder, nwDescription,
				Config.allKeys("_SINGLES_PLOTS", metrics), metrics);
	}

	private static void writePlot(String filename, String folder,
			String nwDescription, String[][] keys, Metric[] metrics) {
		Filewriter fw = new Filewriter(filename);
		fw.writeln("\\subsection{" + nwDescription
				+ Config.get("LATEX_PLOTS_APPENDIX") + "}");
		int counter = 0;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].length == 0) {
				continue;
			}
			counter = 0;
			fw.writeln("\\subsubsection*{" + metrics[i].name() + "}");
			for (int j = 0; j < keys[i].length; j++) {
				writePlot(fw, folder, nwDescription, keys[i][j]);
				counter++;
				if ((counter % 2) == 0 && counter != keys[i].length) {
					fw.writeln("\\newline");
				}
			}
			// if ((counter % 2) != 0) {
			// fw.writeln("\\newline");
			// }
		}
		fw.close();
	}

	private static void writePlot(Filewriter fw, String folder,
			String nwDescription, String plotKey) {
		String filename = Config.get(plotKey + "_PLOT_FILENAME");
		String ext = Config.get("PLOT_EXTENSION");
		String file = folder + filename + ext;
		// String label = folder.replace(
		// Config.get("FILESYSTEM_FOLDER_DELIMITER"), "-")
		// + plotKey;
		// String caption = nwDescription + " - "
		// + Config.get(plotKey + "_PLOT_TITLE");
		fw.writeln("\\includegraphics[width=0.49\\textwidth]{" + file + "}");
	}

	/**
	 * TABLES for Series[]
	 */

	public static void writeSingleTables(Series[] s, String filename,
			String folder, Metric[] metrics) {
		int decimals = Config.getInt("LATEX_TABLE_DECIMALS");
		String[][] keys = Config.allKeys("_TABLE_KEYS", metrics);
		Filewriter fw = new Filewriter(filename);
		String name = s[0].network().description(
				Config.get("LATEX_TABLE_DESCRIPTION_TYPE"),
				s[1 % s.length].network());
		fw.writeln("\\subsection{" + name + Config.get("LATEX_TABLE_APPENDIX")
				+ "}");
		boolean first = true;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].length == 0) {
				continue;
			}
			String[][] table = new String[s.length + 1][keys[i].length + 1];
			table[0][0] = s[0].network().compareName(s[1 % s.length].network());
			for (int j = 0; j < keys[i].length; j++) {
				table[0][j + 1] = makeIndex(keys[i][j]);
			}
			for (int j = 0; j < s.length; j++) {
				table[j + 1][0] = s[j].network().compareValue(
						s[(j + 1) % s.length].network());
			}
			for (int j = 0; j < keys[i].length; j++) {
				for (int k = 0; k < s.length; k++) {
					table[k + 1][j + 1] = formatNumber(s[k].avgSingles()
							.getValue(keys[i][j]), decimals);
				}
			}
			fw.writeln("\n");
			if (!first) {
				fw.writeln("\\vspace{1em}");
			}
			first = false;
			writeTable(fw, table, null, metrics[i].name());
			fw.writeln("\n");
			fw.writeln("\\vspace{1em}");
			writeTable(fw, invert(table), null, metrics[i].name());
			String fn = folder + metrics[i].key()
					+ Config.get("LATEX_EXTENSION");
			Filewriter fw2 = new Filewriter(fn);
			writeTable(fw2, table, null, null);
			fw2.writeln("\n");
			fw2.writeln("\\vspace{1em}");
			writeTable(fw2, invert(table), null, null);
			fw2.close();
		}
		fw.close();
	}

	/**
	 * TABLES for Series[][]
	 */

	public static void writeSingleTables(Series[][] s, String filename,
			String folder, Metric[] metrics) {
		for (int i = 1; i < s.length; i++) {
			if (s[i].length != s[0].length) {
				return;
			}
		}
		int decimals = Config.getInt("LATEX_TABLE_DECIMALS");
		String[][] keys = Config.allKeys("_TABLE_KEYS", metrics);
		Filewriter fw = new Filewriter(filename);
		String x = s[0][0].network().compareNameShort(
				s[0][1 % s[0].length].network());
		String y = s[0][0].network().compareNameShort(
				s[1 % s.length][0].network());
		String name = s[0][0].network().description(
				Config.get("LATEX_TABLE_DESCRIPTION_TYPE"),
				s[0][1 % s[0].length].network(), s[1 % s.length][0].network());
		fw.writeln("\\subsection{" + name + Config.get("LATEX_TABLE_APPENDIX")
				+ "}");
		boolean first = true;
		String[][] table = new String[s.length + 1][s[0].length + 1];
		table[0][0] = y + " $\\backslash$ " + x;
		for (int i = 0; i < s[0].length; i++) {
			table[0][i + 1] = s[0][i].network().compareValue(
					s[0][(i + 1) % s[0].length].network());
		}
		for (int i = 0; i < s.length; i++) {
			table[i + 1][0] = s[i][0].network().compareValue(
					s[(i + 1) % s.length][0].network());
		}
		for (int i = 0; i < keys.length; i++) {
			for (int j = 0; j < keys[i].length; j++) {
				String key = keys[i][j];
				for (int k = 0; k < s.length; k++) {
					for (int l = 0; l < s[k].length; l++) {
						double value = s[k][l].avgSingles().getValue(key);
						table[k + 1][l + 1] = formatNumber(value, decimals);
					}
				}
				fw.writeln("\n\n");
				if (!first) {
					fw.writeln("\\vspace{1em}");
				}
				first = false;
				writeTable(fw, table, "label", makeIndex(key));
				fw.writeln("\n");
				fw.writeln("\\vspace{1em}");
				writeTable(fw, invert(table), "label", makeIndex(key));
				String fn = folder + keys[i][j] + Config.get("LATEX_EXTENSION");
				Filewriter fw2 = new Filewriter(fn);
				writeTable(fw2, table, null, null);
				fw2.writeln("\n");
				fw2.writeln("\\vspace{1em}");
				writeTable(fw2, invert(table), null, null);
				fw2.close();
			}
		}
		fw.close();
	}

	/**
	 * TABLES util
	 */
	public static String[][] invert(String[][] table) {
		String[][] inverted = new String[table[0].length][table.length];
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				inverted[j][i] = table[i][j];
			}
		}
		return inverted;
	}

	private static void writeTable(Filewriter fw, String[][] table,
			String label, String caption) {
		if (Config.getBoolean("LATEX_TABLE_USE_BOOKTABS")) {
			fw.writeln("\\scriptsize");
			if (caption != null) {
				fw.writeln("\\subsubsection*{" + caption + "}");
			}
			fw.write("\\begin{tabular}{l");
			// fw.write("\\begin{tabular*}{\\textwidth}{l");
			for (int i = 1; i < table[0].length; i++) {
				fw.write("r");
			}
			fw.writeln("}");
			fw.writeln("\\toprule");
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[i].length; j++) {
					if (j > 0) {
						fw.write(" & ");
					}
					if (i == 0 || j == 0) {
						fw.write("\\textbf{" + table[i][j] + "}");
					} else {
						fw.write(table[i][j]);
					}
				}
				fw.writeln("\\\\");
				if (i == 0) {
					fw.writeln("\\midrule");
				}
			}
			fw.writeln("\\bottomrule");
			fw.writeln("\\end{tabular}");
			// fw.writeln("\\end{tabular*}");
			fw.writeln("\\normalsize");
		} else {
			fw.writeln("\\scriptsize");
			// fw.writeln("\\begin{table*}[!ht]");
			// fw.writeln("\\center");
			if (caption != null) {
				fw.writeln("\\subsubsection*{" + caption + "}");
			}
			fw.write("\\begin{tabular}{|l|");
			for (int i = 1; i < table[0].length; i++) {
				fw.write("r|");
			}
			fw.writeln("}");
			fw.writeln("\\hline");
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[i].length; j++) {
					if (j > 0) {
						fw.write(" & ");
					}
					if (i == 0 || j == 0) {
						fw.write("\\textbf{" + table[i][j] + "}");
					} else {
						fw.write(table[i][j]);
					}
				}
				fw.writeln("\\\\ \\hline");
			}
			fw.writeln("\\end{tabular}");
			// fw.writeln("\\label{" + label + "}");
			// fw.writeln("\\caption{" + caption + "}");
			// fw.writeln("\\end{table*}");
			fw.writeln("\\normalsize");
		}
	}

	private static String makeIndex(String name) {
		if (!name.contains("_")) {
			return name;
		}
		name = name.replaceFirst("_", "%%%");
		name = name.replace("_", "\\_");
		name = name.replace("%%%", "$_{") + "}$";
		return name;
	}

	private static final DecimalFormat showAllDigitsFormat = new DecimalFormat(
			"0.0#####################################");

	public static String formatNumber(double d, int decimalPlaces) {

		if (decimalPlaces < 0) {
			throw new IllegalArgumentException(
					"Cannot have negative decimal places.");
		}

		if (decimalPlaces == 0) {
			long rounded = Math.round(d);
			return addCommas(rounded + "");
		} else {
			long rounded = Math.round(d * Math.pow(10, decimalPlaces));
			d = rounded;
			for (int i = 0; i < decimalPlaces; i++) {
				d /= 10.0d;
			}
			String s = showAllDigitsFormat.format(d);
			int decimalIndex = 0;
			if (s.contains(".")) {
				decimalIndex = s.indexOf('.');
			} else if (s.contains(",")) {
				decimalIndex = s.indexOf(',');
			}

			String leftSide = s.substring(0, decimalIndex);
			String rightSide = s.substring(decimalIndex + 1);

			leftSide = addCommas(leftSide);
			if (decimalPlaces < rightSide.length()) {
				rightSide = rightSide.substring(0, decimalPlaces);
			} else if (decimalPlaces > rightSide.length()) {
				StringBuilder sb = new StringBuilder(rightSide);
				for (int i = 0; i < decimalPlaces - rightSide.length(); i++) {
					sb.append('0');
				}
				rightSide = sb.toString();
			}

			return leftSide + "." + rightSide;
		}

	}

	private static String addCommas(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = s.length() - 1; i >= 0; i--) {
			sb.insert(0, s.charAt(i));
			if (i > 0 && (s.length() - i) % 3 == 0) {
				sb.insert(0, ',');
			}
		}
		return sb.toString();
	}
}

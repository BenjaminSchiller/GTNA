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
import gtna.util.ArrayUtils;
import gtna.util.Config;
import gtna.util.Execute;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;

import java.io.File;

public class LaTex {
	public static boolean writeTables(Series s, Metric[] metrics,
			String folder, String filename) {
		return LaTex.writeTables(new Series[][] { new Series[] { s } },
				metrics, folder, filename);
	}

	public static boolean writeTables(Series[] s, Metric[] metrics,
			String folder, String filename) {
		Series[][] s_ = new Series[s.length][];
		for (int i = 0; i < s.length; i++) {
			s_[i] = new Series[] { s[i] };
		}
		return LaTex.writeTables(s_, metrics, folder, filename);
	}

	public static boolean writeTables(Series[][] s, Metric[] metrics,
			String folder, String filename) {
		Timer timer = new Timer("writing table => " + folder + filename);

		String texFilename = folder + filename + Config.get("LATEX_EXTENSION");
		Filewriter fw = new Filewriter(texFilename);

		String title = "Single-scalar Values for " + s.length + "x"
				+ s[0].length + " Series";
		String author = "GTNA";

		/*
		 * write latex file
		 */
		LaTex.writeHeader(fw, title, author);
		for (Metric metric : metrics) {
			fw.writeln("\\section{" + metric.getDescriptionLong() + "}");
			for (String key : metric.getSingleKeys()) {
				fw.writeln(LaTex.getLaTeXTable(s, metric, key));
			}
		}
		LaTex.writeFooter(fw);

		/*
		 * generate file from latex
		 */
		boolean success = fw.close();
		String cmd = Config.get("LATEX_PATH") + " --jobname=" + folder
				+ filename + " -enable-write18 " + folder + filename
				+ Config.get("LATEX_EXTENSION");
		success &= Execute.exec(cmd, true);

		/*
		 * delete auxiliary files
		 */
		String[] aux = Config.keys("LATEX_AUXILIARY_EXTENSIONS");
		for (String extension : aux) {
			String fn = folder + filename + extension;
			if ((new File(fn)).exists()) {
				success &= (new File(fn)).delete();
			}
		}

		timer.end();

		return success;
	}

	private static void writeHeader(Filewriter fw, String title, String author) {
		fw.writeln("\\documentclass[11pt]{amsart}");
		fw.writeln("\\usepackage{epstopdf}");
		fw.writeln("\\usepackage{tabularx}");
		fw.writeln("\\usepackage{multirow}");
		fw.writeln("\\usepackage{booktabs}");

		fw.writeln("\\title{" + title + "}");
		fw.writeln("\\author{" + author + "}");
		fw.writeln("\\date{\\today}");
		fw.writeln("\\begin{document}");
		fw.writeln("\\maketitle");
	}

	private static void writeFooter(Filewriter fw) {
		fw.writeln("\\end{document}");
	}

	public static String getLaTeXTable(Series[][] s, Metric metric, String key) {
		String xLabel = s[0][0].getNetwork().getDiffParameterNameShort(
				s[0][1 % s[0].length].getNetwork());
		String yLabel = s[0][0].getNetwork().getDiffParameterNameShort(
				s[1 % s.length][0].getNetwork());

		String[] xLabels = new String[s[0].length];
		for (int i = 0; i < s[0].length; i++) {
			Parameter p = s[0][i].getNetwork().getDiffParameter(
					s[0][(i + 1) % s[0].length].getNetwork());
			if (p != null) {
				xLabels[i] = p.getValue();
			} else {
				xLabels[i] = s[0][i].getNetwork().getDescriptionShort();
			}
		}

		String[] yLabels = new String[s.length];
		for (int i = 0; i < s.length; i++) {
			Parameter p = s[i][0].getNetwork().getDiffParameter(
					s[(i + 1) % s.length][0].getNetwork());
			if (p != null) {
				yLabels[i] = p.getValue();
			} else {
				yLabels[i] = "";
			}
		}

		double[][] entries = ArrayUtils.initDoubleArray(s.length,
				LaTex.getMaxLengthArray(s).length, Table.EMPTY_VALUE);
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length; j++) {
				entries[i][j] = s[i][j].getSingle(metric, key).getValue();
			}
		}

		String caption = Config.get(key + "_SINGLE_NAME");
		String label = key.replace("_", "-");
		Table table = new Table(entries, caption, label, xLabel, xLabels,
				yLabel, yLabels);

		return table.toLaTex();
	}

	private static Parameter getDiffParameter(Series[] s) {
		if (s.length <= 1) {
			return null;
		}

		Parameter diff = s[0].getNetwork().getDiffParameter(s[1].getNetwork());

		if (diff == null) {
			return null;
		}

		for (int i = 1; i < s.length; i++) {
			Parameter diff2 = s[i].getNetwork().getDiffParameter(
					s[(i + 1) % s.length].getNetwork());
			if (diff2 == null || !diff.getKey().equals(diff2.getKey())) {
				return null;
			}
		}

		return diff;
	}

	private static Parameter getDiffParameterX(Series[][] s) {
		Parameter diff = LaTex.getDiffParameter(LaTex.getMaxLengthArray(s));
		if (diff == null) {
			return null;
		}

		for (Series[] s1 : s) {
			Parameter diff2 = LaTex.getDiffParameter(s1);
			if (!diff.getKey().equals(diff2.getKey())) {
				return null;
			}
		}

		return diff;
	}

	private static Series[] getMaxLengthArray(Series[][] s) {
		Series[] s1 = null;
		int max = 0;
		for (Series[] S : s) {
			if (S.length > max) {
				max = S.length;
				s1 = S;
			}
		}
		return s1;
	}
}

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
 * Plotting.java
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
package gtna.plot;

import gtna.data.Series;
import gtna.data.Single;
import gtna.data.SingleList;
import gtna.metrics.Metric;
import gtna.plot.Data.Type;
import gtna.plot.Gnuplot.Style;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.io.File;
import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class Plotting {

	public static boolean single(Series[][] s, Metric[] metrics, String folder,
			Type type, Style style) {
		Timer timer = new Timer("single (" + s.length + "|" + s[0].length
				+ ") (" + type + "|" + style + ")");
		double[][] x = new double[s.length][];
		ArrayList<String> labels = new ArrayList<String>();
		for (int i = 0; i < s.length; i++) {
			Parameter diffP = s[i][0].getNetwork().getDiffParameter(
					s[i][1 % s[i].length].getNetwork());
			if (diffP == null || diffP.getKey() == null) {
				System.err.println("no diff param found");
				return false;
			}
			String label = s[i][0].getNetwork().getDiffParameterName(
					s[i][1 % s[i].length].getNetwork());
			if (!labels.contains(label)) {
				labels.add(label);
			}
			x[i] = new double[s[i].length];
			for (int j = 0; j < s[i].length; j++) {
				Parameter p = s[i][j].getNetwork().getDiffParameter(
						s[i][(j + 1) % s[i].length].getNetwork());
				if (p instanceof IntParameter) {
					x[i][j] = ((IntParameter) p).getIntValue();
				} else if (p instanceof DoubleParameter) {
					x[i][j] = ((DoubleParameter) p).getDoubleValue();
				} else {
					System.err.println("diff param not of type int or long");
					return false;
				}
			}
		}
		StringBuffer xLabel = new StringBuffer();
		for (String l : labels) {
			if (xLabel.length() == 0) {
				xLabel.append(l);
			} else {
				xLabel.append(", " + l);
			}
		}
		boolean success = Plotting.singleMetrics(s, metrics, folder, type,
				style, x, xLabel.toString());
		timer.end();
		return success;
	}

	public static boolean singleBy(Series[][] s, Metric[] metrics,
			String folder, Metric metricX, String keyX, Type type, Style style) {
		Timer timer = new Timer("single (" + s.length + "|" + s[0].length
				+ ") (" + type + "|" + style + ") by " + keyX);
		double[][] x = new double[s.length][];
		for (int i = 0; i < s.length; i++) {
			x[i] = new double[s[i].length];
			for (int j = 0; j < s[i].length; j++) {
				Single single = s[i][j].getSingle(metricX, metricX.getKey()
						+ "_" + keyX);
				if (single == null) {
					System.err.println("cannot find " + keyX + " for "
							+ metricX.getFolderName() + " in "
							+ s[i][j].getNetwork().getFolderName());
					return false;
				}
				x[i][j] = single.getValue();
			}
		}
		String xLabel = Config.get(metricX.getKey() + "_" + keyX
				+ "_SINGLE_NAME");
		boolean success = Plotting.singleMetrics(s, metrics, folder, type,
				style, x, xLabel);
		timer.end();
		return success;
	}

	private static boolean singleMetrics(Series[][] s, Metric[] metrics,
			String folder, Type type, Style style, double[][] x, String xLabel) {
		if (x == null) {
			return false;
		}
		boolean subfolders = Config.getBoolean("PLOT_SUBFOLDERS");
		boolean success = true;
		for (Metric m : metrics) {
			for (String key : m.getSinglePlotKeys()) {
				String pre = Config.get("MAIN_PLOT_FOLDER") + folder
						+ (subfolders ? m.getFolder() : m.getFolderName());
				(new File(pre)).mkdirs();
				success &= Plotting.singleMetric(s, m, key, pre, type, style,
						x, xLabel);
			}
		}
		return success;
	}

	private static boolean singleMetric(Series[][] s, Metric m, String plotKey,
			String pre, Type type, Style style, double[][] x, String xLabel) {
		String[] dataKeys = Config.keys(plotKey + "_PLOT_DATA");
		Data[] data = new Data[s.length * dataKeys.length];
		int index = 0;
		for (int i = 0; i < s.length; i++) {
			for (String key : dataKeys) {
				double[][] d = new double[s[i].length][];
				for (int j = 0; j < s[i].length; j++) {
					SingleList sl = SingleList.read(m,
							s[i][j].getSinglesFilename(m));
					Single single = sl.get(key);
					d[j] = new double[single.getData().length + 1];
					d[j][0] = x[i][j];
					for (int k = 0; k < single.getData().length; k++) {
						d[j][k + 1] = single.getData()[k];
					}
				}
				String filename = Gnuplot.writeTempData(m, plotKey, index, d);
				if (filename == null) {
					return false;
				}
				String title = s[i][0].getNetwork().getDiffDescriptionShort(
						s[i][1 % s[i].length].getNetwork());
				data[index++] = Data.get(filename, style, title, type);
			}
		}
		String terminal = Config.get("GNUPLOT_TERMINAL");
		String output = pre + Config.get(plotKey + "_PLOT_FILENAME")
				+ Config.get("PLOT_EXTENSION");
		Plot plot = new Plot(data, terminal, output);
		plot.setTitle(Config.get(plotKey + "_PLOT_TITLE"));
		plot.setxLabel(xLabel);
		plot.setyLabel(Config.get(plotKey + "_PLOT_Y"));

		return Gnuplot.plot(plot, m, plotKey);
	}

	public static boolean multi(Series[] s, Metric[] metrics, String folder,
			Type type, Style style) {
		Timer timer = new Timer("multi (" + s.length + ") " + type + " / "
				+ style);
		boolean subfolders = Config.getBoolean("PLOT_SUBFOLDERS");
		boolean success = true;
		for (Metric m : metrics) {
			for (String key : m.getDataPlotKeys()) {
				String pre = Config.get("MAIN_PLOT_FOLDER") + folder
						+ (subfolders ? m.getFolder() : m.getFolderName());
				(new File(pre)).mkdirs();
				success &= Plotting.multi(s, m, key, pre, type, style);
			}
		}
		timer.end();
		return success;
	}

	private static boolean multi(Series[] s, Metric m, String plotKey,
			String pre, Type type, Style style) {
		String[] dataKeys = Config.keys(plotKey + "_PLOT_DATA");
		Data[] data = new Data[s.length * dataKeys.length];
		int index = 0;
		for (Series S : s) {
			for (String key : dataKeys) {
				data[index++] = Data.get(S.getMultiFilename(m, key), style, S
						.getNetwork().getDescriptionShort(), type);
			}
		}
		String terminal = Config.get("GNUPLOT_TERMINAL");
		String output = pre + Config.get(plotKey + "_PLOT_FILENAME")
				+ Config.get("PLOT_EXTENSION");
		Plot plot = new Plot(data, terminal, output);
		plot.setTitle(Config.get(plotKey + "_PLOT_TITLE"));
		plot.setxLabel(Config.get(plotKey + "_PLOT_X"));
		plot.setyLabel(Config.get(plotKey + "_PLOT_Y"));

		if (Config.getBoolean(dataKeys[0] + "_DATA_IS_CDF")) {
			plot.setKey(Config.get("GNUPLOT_KEY_CDF"));
		} else {
			plot.setKey(Config.get("GNUPLOT_KEY"));
		}
		return Gnuplot.plot(plot, m, plotKey);
	}

	public static boolean single(Series s, Metric[] metrics, String folder) {
		return Plotting.single(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean single(Series[] s, Metric[] metrics, String folder) {
		return Plotting.single(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean single(Series[][] s, Metric[] metrics, String folder) {
		return Plotting.single(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean single(Series s, Metric[] metrics, String folder,
			Type type, Style style) {
		return Plotting
				.single(new Series[] { s }, metrics, folder, type, style);
	}

	public static boolean single(Series[] s, Metric[] metrics, String folder,
			Type type, Style style) {
		return Plotting.single(new Series[][] { s }, metrics, folder, type,
				style);
	}

	public static boolean singleBy(Series s, Metric[] metrics, String folder,
			Metric metricX, String keyX) {
		return Plotting.singleBy(s, metrics, folder, metricX, keyX,
				Type.average, Style.linespoint);
	}

	public static boolean singleBy(Series[] s, Metric[] metrics, String folder,
			Metric metricX, String keyX) {
		return Plotting.singleBy(s, metrics, folder, metricX, keyX,
				Type.average, Style.linespoint);
	}

	public static boolean singleBy(Series[][] s, Metric[] metrics,
			String folder, Metric metricX, String keyX) {
		return Plotting.singleBy(s, metrics, folder, metricX, keyX,
				Type.average, Style.linespoint);
	}

	public static boolean singleBy(Series s, Metric[] metrics, String folder,
			Metric metricX, String keyX, Type type, Style style) {
		return Plotting.singleBy(new Series[] { s }, metrics, folder, metricX,
				keyX, type, style);
	}

	public static boolean singleBy(Series[] s, Metric[] metrics, String folder,
			Metric metricX, String keyX, Type type, Style style) {
		return Plotting.singleBy(new Series[][] { s }, metrics, folder,
				metricX, keyX, type, style);
	}

	public static boolean multi(Series s, Metric[] metrics, String folder) {
		return Plotting.multi(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean multi(Series[] s, Metric[] metrics, String folder) {
		return Plotting.multi(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean multi(Series[][] s, Metric[] metrics, String folder) {
		return Plotting.multi(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean multi(Series s, Metric[] metrics, String folder,
			Type type, Style style) {
		return Plotting.multi(new Series[] { s }, metrics, folder, type, style);
	}

	public static boolean multi(Series[][] s, Metric[] metrics, String folder,
			Type type, Style style) {
		int index = 0;
		for (Series[] s1 : s) {
			index += s1.length;
		}
		Series[] S = new Series[index];
		index = 0;
		for (Series[] s1 : s) {
			for (Series s2 : s1) {
				S[index++] = s2;
			}
		}
		return Plotting.multi(S, metrics, folder, type, style);
	}
}

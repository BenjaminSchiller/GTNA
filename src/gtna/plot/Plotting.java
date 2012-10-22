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
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data;
import gtna.plot.data.Data.Type;
import gtna.util.ArrayUtils;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.parameter.DateParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author benni
 * 
 */
public class Plotting {

	/**
	 * Generate plots for all single-scalar metric in the given list. As x value
	 * int the plots, the first differing configuration parameter in the
	 * networks' configuration is used. No plots are generated in case the
	 * network cannot be compared.
	 * 
	 * @param s
	 *            list of series to be plotted
	 * @param metrics
	 *            list of metric to be plotted
	 * @param folder
	 *            main destination folder for the plots (subfolders for each
	 *            metric will be generated)
	 * @param type
	 *            data type to be used
	 * @param style
	 *            gnuplot style to be used
	 * @return true, if all plots are generated successfully; false otherwise
	 */
	public static boolean single(Series[][] s, Metric[] metrics, String folder,
			Type type, Style style) {
		Timer timer = new Timer("single (" + s.length + "|" + s[0].length
				+ ") (" + type + "|" + style + ")");
		double[][] x = new double[s.length][];
		String[] config = new String[0];
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
				} else if (p instanceof DateParameter) {
					x[i][j] = ((DateParameter) p).getDateValue().getTime() / 1000;
					config = new String[] { "set xdata time",
							"set timefmt \"%s\"" };
				} else {
					System.err
							.println("diff param not of type int, long, or datetime");
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
				style, x, xLabel.toString(), config);
		if (Config.getBoolean("RUNTIME_PLOTS_GENERATE")) {
			success &= Plotting.runtimes(s, metrics, folder, type, style, x,
					xLabel.toString(), new String[0]);
		}
		if (Config.getBoolean("ETC_PLOTS_GENERATE")) {
			success &= Plotting.etc(s, folder, type, style, x,
					xLabel.toString(), new String[0]);
		}
		timer.end();
		return success;
	}

	/**
	 * Generate plots for all single-scalar metric in the given list. Instead of
	 * using the differing networks' parameter as x value, the specified
	 * single-scalar metric from the given metric is used.
	 * 
	 * @param s
	 *            list of series to be plotted
	 * @param metrics
	 *            list of metrics to be plotted
	 * @param folder
	 *            main destination folder for the plots (subfolders for each
	 *            metric will be generated)
	 * @param metricX
	 *            the metric holding the single-scalar metric to be used as
	 *            value on the x-axis
	 * @param keyX
	 *            key of the single-scalar metric to be used as value on the
	 *            x-axis
	 * @param type
	 *            data type to be used
	 * @param style
	 *            gnuplot style to be used
	 * @return true, if all plots are generated successfully; false otherwise
	 */
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
				style, x, xLabel, new String[0]);
		if (Config.getBoolean("RUNTIME_PLOTS_GENERATE")) {
			success &= Plotting.runtimes(s, metrics, folder, type, style, x,
					xLabel.toString(), new String[0]);
		}
		if (Config.getBoolean("ETC_PLOTS_GENERATE")) {
			success &= Plotting.etc(s, folder, type, style, x,
					xLabel.toString(), new String[0]);
		}
		timer.end();
		return success;
	}

	private static boolean singleMetrics(Series[][] s, Metric[] metrics,
			String folder, Type type, Style style, double[][] x, String xLabel,
			String[] config) {
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
				success &= Plotting.singleMetric(s, new Metric[] { m }, key,
						pre, type, style, x, xLabel, config);
			}
		}

		Metric[][] grouped = Plotting.groupMetrics(metrics);
		for (Metric[] group : grouped) {
			for (String key : group[0].getSinglePlotKeys()) {
				String pre = Config.get("MAIN_PLOT_FOLDER")
						+ folder
						+ group[0].getKey()
						+ Config.get("PLOT_GROUPED_KEYWORD")
						+ (subfolders ? Config
								.get("FILESYSTEM_FOLDER_DELIMITER") : "");
				(new File(pre)).mkdirs();
				success &= Plotting.singleMetric(s, group, key, pre, type,
						style, x, xLabel, config);
			}
		}

		return success;
	}

	private static boolean singleMetric(Series[][] s, Metric[] metrics,
			String plotKey, String pre, Type type, Style style, double[][] x,
			String xLabel, String[] config) {
		String[] dataKeys = Config.keys(plotKey + "_PLOT_DATA");
		Data[] data = new Data[s.length * dataKeys.length * metrics.length];
		int index = 0;
		for (int i = 0; i < s.length; i++) {
			for (String key : dataKeys) {
				for (Metric metric : metrics) {
					double[][] d = new double[s[i].length][];
					for (int j = 0; j < s[i].length; j++) {
						SingleList sl = SingleList.read(metric,
								s[i][j].getSinglesFilename(metric));
						Single single = sl.get(key);
						d[j] = new double[single.getData().length + 1];
						d[j][0] = x[i][j];
						for (int k = 0; k < single.getData().length; k++) {
							d[j][k + 1] = single.getData()[k];
						}
					}
					String filename = Gnuplot.writeTempData(metric, plotKey,
							index, d);
					if (filename == null) {
						return false;
					}
					String title = s[i][0].getNetwork()
							.getDiffDescriptionShort(
									s[i][1 % s[i].length].getNetwork());
					if (metrics.length > 1) {
						title = metric.getDescriptionShort() + " - " + title;
					}
					data[index++] = Data.get(filename, style, title, type);
				}
			}
		}
		String terminal = Config.get("GNUPLOT_TERMINAL");
		String output = pre + Config.get(plotKey + "_PLOT_FILENAME")
				+ Config.get("PLOT_EXTENSION");
		Plot plot = new Plot(data, terminal, output);
		plot.setTitle(Config.get(plotKey + "_PLOT_TITLE"));
		plot.setxLabel(xLabel);
		plot.setyLabel(Config.get(plotKey + "_PLOT_Y"));
		for (String cfg : config) {
			plot.addConfig(cfg);
		}

		return Gnuplot.plot(plot, metrics, plotKey);
	}

	private static boolean etc(Series[][] s, String folder, Type type,
			Style style, double[][] x, String xLabel, String[] config) {
		String[] keys = Config.keys("ETC_PLOTS");

		SingleList[][] sl = new SingleList[s.length][];
		for (int i = 0; i < s.length; i++) {
			sl[i] = new SingleList[s[i].length];
			for (int j = 0; j < s[i].length; j++) {
				sl[i][j] = SingleList.read(null, s[i][j].getEtcFilename());
			}
		}

		String pre = Config.get("MAIN_PLOT_FOLDER") + folder
				+ Config.get("ETC_PLOTS_FOLDER");
		(new File(pre)).mkdirs();

		boolean success = true;
		for (String key : keys) {
			String yLabel = Config.get("ETC_PLOTS_" + key + "_Y_LABEL");
			String title = Config.get("ETC_PLOTS_" + key + "_TITLE");
			success &= Plotting.fromSingleList(s, sl, key, pre, type, style, x,
					xLabel, yLabel, title, config);
		}
		return success;
	}

	private static boolean runtimes(Series[][] s, Metric[] metrics,
			String folder, Type type, Style style, double[][] x, String xLabel,
			String[] config) {
		String[] keys = new String[metrics.length + 1];
		for (int i = 0; i < metrics.length; i++) {
			keys[i] = metrics[i].getRuntimeSingleName();
		}
		keys[keys.length - 1] = "G_RUNTIME";

		SingleList[][] sl = new SingleList[s.length][];
		for (int i = 0; i < s.length; i++) {
			sl[i] = new SingleList[s[i].length];
			for (int j = 0; j < s[i].length; j++) {
				sl[i][j] = SingleList.read(null, s[i][j].getRuntimesFilename());
			}
		}

		String pre = Config.get("MAIN_PLOT_FOLDER") + folder
				+ Config.get("RUNTIME_PLOTS_FOLDER");
		(new File(pre)).mkdirs();

		boolean success = true;
		for (String key : keys) {
			success &= Plotting.fromSingleList(s, sl, key, pre, type, style, x,
					xLabel, Config.get("RUNTIME_PLOTS_Y_LABEL"), key, config);
		}
		return success;
	}

	private static boolean fromSingleList(Series[][] s, SingleList[][] sl,
			String key, String pre, Type type, Style style, double[][] x,
			String xLabel, String yLabel, String title, String[] config) {
		Data[] data = new Data[s.length];
		int index = 0;
		for (int i = 0; i < s.length; i++) {
			double[][] d = new double[s[i].length][];
			for (int j = 0; j < s[i].length; j++) {
				Single single = sl[i][j].get(key);
				d[j] = new double[single.getData().length + 1];
				d[j][0] = x[i][j];
				for (int k = 0; k < single.getData().length; k++) {
					d[j][k + 1] = single.getData()[k];
				}
			}
			String filename = Gnuplot.writeTempData(null, key, index, d);
			if (filename == null) {
				return false;
			}
			String t = s[i][0].getNetwork().getDiffDescriptionShort(
					s[i][1 % s[i].length].getNetwork());
			data[index++] = Data.get(filename, style, t, type);
		}

		String terminal = Config.get("GNUPLOT_TERMINAL");
		String output = pre + key + Config.get("PLOT_EXTENSION");
		Plot plot = new Plot(data, terminal, output);
		plot.setTitle(title);
		plot.setxLabel(xLabel);
		plot.setyLabel(yLabel);
		for (String cfg : config) {
			plot.addConfig(cfg);
		}

		return Gnuplot.plot(plot, null, key);
	}

	/**
	 * Generates plots for all multi-scalar metrics in the given list.
	 * 
	 * @param s
	 *            list of series to be plotted
	 * @param metrics
	 *            list of metrics to be plotted
	 * @param folder
	 *            main destination folder for the plots (subfolders for each
	 *            metric will be generated)
	 * @param type
	 *            data type to be used
	 * @param style
	 *            gnuplot style to be used
	 * @return true, if all plots are generated successfully; false otherwise
	 */
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
				success &= Plotting.multiMetric(s, new Metric[] { m }, key,
						pre, type, style);
			}
		}

		Metric[][] grouped = Plotting.groupMetrics(metrics);
		for (Metric[] group : grouped) {
			for (String key : group[0].getDataPlotKeys()) {
				String pre = Config.get("MAIN_PLOT_FOLDER")
						+ folder
						+ group[0].getKey()
						+ Config.get("PLOT_GROUPED_KEYWORD")
						+ (subfolders ? Config
								.get("FILESYSTEM_FOLDER_DELIMITER") : "");
				(new File(pre)).mkdirs();
				success &= Plotting
						.multiMetric(s, group, key, pre, type, style);
			}
		}

		timer.end();
		return success;
	}

	private static boolean multiMetric(Series[] s, Metric[] metrics,
			String plotKey, String pre, Type type, Style style) {
		if (Config.containsKey(plotKey + "_PLOT_TYPE")
				&& Config.containsKey(plotKey + "_PLOT_STYLE")) {
			type = Type.valueOf(Config.get(plotKey + "_PLOT_TYPE"));
			style = Style.valueOf(Config.get(plotKey + "_PLOT_STYLE"));
		}

		String[] dataKeys = Config.keys(plotKey + "_PLOT_DATA");
		Data[] data = new Data[s.length * dataKeys.length * metrics.length];
		int index = 0;
		if (Config.getBoolean("PLOT_GROUPED_SERIES_FIRST")) {
			for (Metric metric : metrics) {
				for (Series S : s) {
					for (String key : dataKeys) {
						String name = S.getNetwork().getDescriptionShort();
						if (metrics.length > 1) {
							name = metric.getDescriptionShort() + " - " + name;
						}
						data[index++] = Data.get(
								S.getMultiFilename(metric, key), style, name,
								type);
					}
				}
			}
		} else {
			for (Series S : s) {
				for (String key : dataKeys) {
					for (Metric metric : metrics) {
						String name = S.getNetwork().getDescriptionShort();
						if (metrics.length > 1) {
							name = metric.getDescriptionShort() + " - " + name;
						}
						data[index++] = Data.get(
								S.getMultiFilename(metric, key), style, name,
								type);
					}
				}
			}
		}
		String terminal = Config.get("GNUPLOT_TERMINAL");
		String output = pre + Config.get(plotKey + "_PLOT_FILENAME")
				+ Config.get("PLOT_EXTENSION");
		Plot plot = new Plot(data, terminal, output);
		plot.setTitle(Config.get(plotKey + "_PLOT_TITLE"));
		plot.setxLabel(Config.get(plotKey + "_PLOT_X"));
		plot.setyLabel(Config.get(plotKey + "_PLOT_Y"));

		if (Config.containsKey(plotKey + "_PLOT_OFFSET_X")
				&& Config.get(plotKey + "_PLOT_OFFSET_X").length() > 0) {
			plot.setOffsetX(Config.getDouble(plotKey + "_PLOT_OFFSET_X"));
		}

		if (Config.containsKey(plotKey + "_PLOT_OFFSET_Y")
				&& Config.get(plotKey + "_PLOT_OFFSET_Y").length() > 0) {
			plot.setOffsetY(Config.getDouble(plotKey + "_PLOT_OFFSET_Y"));
		}

		if (Config.containsKey(plotKey + "_PLOT_LW")) {
			plot.setLW(Config.getInt(plotKey + "_PLOT_LW"));
		}

		if (Config.getBoolean(dataKeys[0] + "_DATA_IS_CDF")) {
			plot.setKey(Config.get("GNUPLOT_KEY_CDF"));
		} else {
			plot.setKey(Config.get("GNUPLOT_KEY"));
		}

		String xtics = Config.get(plotKey + "_PLOT_XTICS");
		if (xtics != null && xtics.length() > 0) {
			plot.addConfig("set xtics (" + xtics + ")");
		}

		String ytics = Config.get(plotKey + "_PLOT_YTICS");
		if (ytics != null && ytics.length() > 0) {
			plot.addConfig("set ytics (" + ytics + ")");
		}

		String key = Config.get(plotKey + "_PLOT_KEY");
		if (key != null && key.length() > 0) {
			plot.setKey(key);
		}

		String logscale = Config.get(plotKey + "_PLOT_LOGSCALE");
		if (logscale != null && logscale.length() > 0) {
			plot.addConfig("set logscale " + logscale);
		}

		return Gnuplot.plot(plot, metrics, plotKey);
	}

	private static Metric[][] groupMetrics(Metric[] metrics) {
		Map<String, ArrayList<Metric>> map = new HashMap<String, ArrayList<Metric>>();
		for (Metric m : metrics) {
			if (map.containsKey(m.getKey())) {
				map.get(m.getKey()).add(m);
			} else {
				ArrayList<Metric> list = new ArrayList<Metric>();
				list.add(m);
				map.put(m.getKey(), list);
			}
		}

		Set<String> remove = new HashSet<String>();
		for (String key : map.keySet()) {
			if (map.get(key).size() < 2) {
				remove.add(key);
			}
		}

		for (String key : remove) {
			map.remove(key);
		}

		Metric[][] grouped = new Metric[map.size()][];
		int index = 0;
		for (ArrayList<Metric> list : map.values()) {
			grouped[index++] = ArrayUtils.toMetricArray(list);
		}

		return grouped;
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

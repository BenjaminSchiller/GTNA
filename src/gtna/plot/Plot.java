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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * Plot.java
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
package gtna.plot;

import gtna.data.ConfidenceData;
import gtna.data.Series;
import gtna.data.Singles;
import gtna.io.DataWriter;
import gtna.io.Filewriter;
import gtna.io.LaTex;
import gtna.util.Config;
import gtna.util.Timer;

/**
 * 
 * @author benni
 * 
 */
public class Plot {
	public static void singlesAvg(Series[][] series, String folder) {
		singles(series, folder, false, false, true);
	}

	public static void singlesAvg(Series[] series, String folder) {
		singlesAvg(new Series[][] { series }, folder);
	}

	public static void singlesAvg(Series series, String folder) {
		singlesAvg(new Series[][] { new Series[] { series } }, folder);
	}

	public static void singlesConf(Series[][] series, String folder) {
		singles(series, folder, false, true, true);
	}

	public static void singlesConf(Series[] series, String folder) {
		singlesAvg(new Series[][] { series }, folder);
	}

	public static void singlesConf(Series series, String folder) {
		singlesAvg(new Series[][] { new Series[] { series } }, folder);
	}

	public static void singlesAvgByEdges(Series[][] series, String folder) {
		singles(series, folder, true, false, true);
	}

	public static void singlesAvgByEdges(Series[] series, String folder) {
		singlesAvgByEdges(new Series[][] { series }, folder);
	}

	public static void singlesConfByEdges(Series[][] series, String folder) {
		singles(series, folder, true, true, true);
	}

	public static void singlesConfByEdges(Series[] series, String folder) {
		singlesConfByEdges(new Series[][] { series }, folder);
	}

	public static void singlesAvgByEdgesNoLine(Series[][] series, String folder) {
		singles(series, folder, true, false, false);
	}

	public static void singlesAvgByEdgesNoLine(Series[] series, String folder) {
		singlesAvgByEdgesNoLine(new Series[][] { series }, folder);
	}

	public static void singlesConfByEdgesNoLine(Series[][] series, String folder) {
		singles(series, folder, true, true, false);
	}

	public static void singlesConfByEdgesNoLine(Series[] series, String folder) {
		singlesConfByEdgesNoLine(new Series[][] { series }, folder);
	}

	public static void allSingleByEdges(Series[] series, String folder) {
		allSingleByEdges(new Series[][] { series }, folder);
	}

	public static void allSingle(Series[] series, String folder) {
		allSingle(new Series[][] { series }, folder);
	}

	public static void allSingle(Series[][] series, String folder) {
		singlesAvg(series, folder + Config.get("PLOT_ALL_SINGLES_AVG_FOLDER"));
		singlesConf(series, folder + Config.get("PLOT_ALL_SINGLES_CONF_FOLDER"));
		if (Config.getBoolean("PLOT_ALL_SINGLES_TABLE_WRITE")) {
			String fn = Config.get("MAIN_PLOT_FOLDER") + folder
					+ Config.get("PLOT_ALL_SINGLES_TABLE_FILENAME");
			String f = Config.get("MAIN_PLOT_FOLDER") + folder
					+ Config.get("PLOT_ALL_SINGLES_TABLE_FOLDER");
			LaTex.writeSingleTables(series, fn, f);
		}
	}

	public static void allSingleByEdges(Series[][] series, String folder) {
		singlesAvgByEdges(series, folder
				+ Config.get("PLOT_ALL_SINGLES_AVG_FOLDER"));
		singlesConfByEdges(series, folder
				+ Config.get("PLOT_ALL_SINGLES_CONF_FOLDER"));
	}

	public static void allSingleByEdgesNoLine(Series[][] series, String folder) {
		singlesAvgByEdgesNoLine(series, folder
				+ Config.get("PLOT_ALL_SINGLES_AVG_FOLDER"));
		singlesConfByEdgesNoLine(series, folder
				+ Config.get("PLOT_ALL_SINGLES_CONF_FOLDER"));
	}

	public static void multiAvg(Series[] series, String folder) {
		multi(series, folder, false);
	}

	public static void multiAvg(Series series, String folder) {
		multiAvg(new Series[] { series }, folder);
	}

	public static void multiConf(Series[] series, String folder) {
		multi(series, folder, true);
	}

	public static void multiConf(Series series, String folder) {
		multiConf(new Series[] { series }, folder);
	}

	public static void allMulti(Series series, String folder) {
		allMulti(new Series[] { series }, folder);
	}

	public static void allMulti(Series[] series, String folder) {
		multiAvg(series, folder + Config.get("PLOT_ALL_MULTI_AVG_FOLDER"));
		multiConf(series, folder + Config.get("PLOT_ALL_MULTI_CONF_FOLDER"));
		if (Config.getBoolean("PLOT_ALL_MULTI_TABLE_WRITE")) {
			String fn = Config.get("MAIN_PLOT_FOLDER") + folder
					+ Config.get("PLOT_ALL_MULTI_TABLE_FILENAME");
			String f = Config.get("MAIN_PLOT_FOLDER") + folder
					+ Config.get("PLOT_ALL_MULTI_TABLE_FOLDER");
			LaTex.writeSingleTables(series, fn, f);
		}
	}

	/**
	 * SINGLES
	 */
	
	// TODO add plot modes for sinlges as well

	private static void singles(Series[][] series, String folder,
			boolean byEdges, boolean conf, boolean line) {
		String name = "";
		if (byEdges) {
			if (conf) {
				name = "PLOT_SINGLES_CONF_BY_EDGES";
			} else {
				name = "PLOT_SINGLES_AVG_BY_EDGES";
			}
		} else {
			if (conf) {
				name = "PLOT_SINGLES_CONF";
			} else {
				name = "PLOT_SINGLES_AVG";
			}
		}
		Timer timer = new Timer(Config.get(name).replace("%SERIES",
				"" + series.length).replace("%DEST", folder));
		Filewriter.generateFolders(Config.get("MAIN_PLOT_FOLDER") + folder);
		String[][] keys = Config.allKeys("_SINGLES_PLOTS");
		for (int i = 0; i < keys.length; i++) {
			for (int j = 0; j < keys[i].length; j++) {
				plotSingles(series, folder, byEdges, conf, line, keys[i][j]);
			}
		}
		if (Config.getBoolean("PLOT_SINGLES_LATEX_WRITE")) {
			LaTex
					.writeSinglePlots(Config.get("MAIN_PLOT_FOLDER") + folder
							+ Config.get("PLOT_SINGLES_LATEX_FILENAME"),
							folder, series);
		}
		timer.end();
	}

	private static void plotSingles(Series[][] series, String folder,
			boolean byEdges, boolean conf, boolean line, String plotKey) {
		String[] data = Config.keys(plotKey + "_PLOT_DATA");
		String fn = Config.get(plotKey + "_PLOT_FILENAME");
		String ext = Config.get("PLOT_EXTENSION");
		String dest = folder + fn + ext;
		String title = Config.get(plotKey + "_PLOT_TITLE");
		String yLabel = Config.get(plotKey + "_PLOT_Y");
		String key = Config.get(plotKey + "_PLOT_KEY");
		String xLabel = null;
		if (byEdges) {
			xLabel = Config.get("NETWORK_COMPARE_EDGES_NAME");
		} else {
			xLabel = series[0][0].network().compareName(
					series[0][1 % series[0].length].network());
			for (int i = 1; i < series.length; i++) {
				String name = series[i][0].network().compareName(
						series[i][1 % series[i].length].network());
				if (!xLabel.contains(name)) {
					xLabel += Config.get("SINGLES_PLOT_XLABEL_SEPARATOR")
							+ name;
				}
			}
		}
		for (int i = 0; i < data.length; i++) {
			if (Double.isNaN(series[0][0].avgSingles().getValue(data[i]))) {
				if (!data[i].startsWith(Config.get("PLOT_FUNCTION_KEYWORD"))) {
					return;
				}
			}
		}
		if (conf) {
			plotSinglesConf(series, byEdges, line, data, dest, title, yLabel,
					xLabel, key, plotKey);
		} else {
			plotSinglesAvg(series, byEdges, line, data, dest, title, yLabel,
					xLabel, key, plotKey);
		}
	}

	/**
	 * SINGLES AVG
	 */

	private static void plotSinglesAvg(Series[][] series, boolean byEdges,
			boolean line, String[] data, String dest, String title,
			String yLabel, String xLabel, String key, String plotKey) {
		double[][][][] values = new double[data.length][series.length][][];
		int max = 0;
		int maxIndex = -1;
		for (int i = 0; i < series.length; i++) {
			if (series[i].length > max) {
				max = series[i].length;
				maxIndex = i;
			}
		}
		for (int d = 0; d < data.length; d++) {
			for (int i = 0; i < series.length; i++) {
				if (series[i].length == 1) {
					values[d][i] = new double[max][2];
					for (int j = 0; j < max; j++) {
						if (byEdges) {
							values[d][i][j][0] = series[i][0].network().edges();
						} else {
							values[d][i][j][0] = Double
									.parseDouble(series[maxIndex][j]
											.network()
											.compareValue(
													series[maxIndex][(j + 1)
															% series[maxIndex].length]
															.network()));
						}
						values[d][i][j][1] = series[i][0].avgSingles()
								.getValue(data[d]);
					}
				} else {
					values[d][i] = new double[series[i].length][2];
					for (int j = 0; j < series[i].length; j++) {
						if (byEdges) {
							values[d][i][j][0] = series[i][j].network().edges();
						} else {
							values[d][i][j][0] = Double
									.parseDouble(series[i][j].network()
											.compareValue(
													series[i][(j + 1)
															% series[i].length]
															.network()));
						}
						values[d][i][j][1] = series[i][j].avgSingles()
								.getValue(data[d]);
					}
				}
			}
		}
		int lineWidth = Config.getInt("SINGLES_PLOT_LINE_WIDTH");
		int pointWidth = Config.getInt("SINGLES_PLOT_POINT_WIDTH");
		PlotData[] plotData = new PlotData[series.length * 2 * values.length];
		if (!line) {
			plotData = new PlotData[series.length * values.length];
		}
		for (int i = 0; i < series.length; i++) {
			for (int sv = 0; sv < values.length; sv++) {
				int index1 = i * values.length + sv;
				int index2 = index1 + values.length * series.length;

				String filename = Config.get("TEMP_FOLDER")
						+ (sv * series.length + i)
						+ Config.get("DATA_EXTENSION");
				String name = series[i][0].network().description(
						series[i][1 % series[i].length].network());

				String pre = Config.get(plotKey + "_PLOT_PRE_" + data[sv]);
				String app = Config.get(plotKey + "_PLOT_APP_" + data[sv]);
				if (pre != null && pre.trim().length() > 0) {
					name = pre + " " + name;
				}
				if (app != null && app.trim().length() > 0) {
					name = name + " " + app;
				}

				DataWriter.write(values[sv][i], filename, false);
				plotData[index1] = new PlotData(filename, name,
						PlotData.POINTS, i + 1, pointWidth);
				if (line) {
					plotData[index2] = new PlotData(filename, null,
							PlotData.LINE, i + 1, lineWidth);
				}
			}
		}
		boolean logscaleX = Config.getBoolean(plotKey + "_PLOT_LOGSCALE_X");
		boolean logscaleY = Config.getBoolean(plotKey + "_PLOT_LOGSCALE_Y");
		GNUPlot.plot(dest, plotData, title, xLabel, yLabel, key, logscaleX,
				logscaleY);
	}

	/**
	 * SINGLES CONF
	 */

	private static void plotSinglesConf(Series[][] series, boolean byEdges,
			boolean line, String[] data, String dest, String title,
			String yLabel, String xLabel, String key, String plotKey) {
		double[][][][] conf = new double[data.length][series.length][][];
		double[][][][] avg = new double[data.length][series.length][][];
		for (int d = 0; d < data.length; d++) {
			for (int i = 0; i < series.length; i++) {
				conf[d][i] = new double[series[i].length][];
				avg[d][i] = new double[series[i].length][2];
				for (int j = 0; j < series[i].length; j++) {
					if (byEdges) {
						avg[d][i][j][0] = series[i][j].network().edges();
					} else {
						avg[d][i][j][0] = Double.parseDouble(series[i][j]
								.network().compareValue(
										series[i][(j + 1) % series[i].length]
												.network()));
					}
					avg[d][i][j][1] = series[i][j].avgSingles().getValue(
							data[d]);
					conf[d][i][j] = ConfidenceData.getConfidenceInterval(
							avg[d][i][j][0], Singles.getValues(series[i][j]
									.summaries(), data[d]));
				}
			}
		}
		int lineWidth = Config.getInt("SINGLES_PLOT_LINE_WIDTH");
		int whiskerWidth = Config.getInt("SINGLES_PLOT_WHISKER_WIDTH");
		PlotData[] plotData = new PlotData[series.length * 2 * conf.length];
		if (!line) {
			plotData = new PlotData[series.length * conf.length];
		}
		for (int i = 0; i < series.length; i++) {
			for (int sv = 0; sv < conf.length; sv++) {
				String filenameConf = Config.get("TEMP_FOLDER")
						+ (sv * series.length + i) + "_conf"
						+ Config.get("DATA_EXTENSION");
				String filenameAvg = Config.get("TEMP_FOLDER")
						+ (sv * series.length + i) + "_avg"
						+ Config.get("DATA_EXTENSION");
				String name = series[i][0].network().description(
						series[i][1 % series[i].length].network());

				String pre = Config.get(plotKey + "_PLOT_PRE_" + data[sv]);
				String app = Config.get(plotKey + "_PLOT_APP_" + data[sv]);
				if (pre != null && pre.trim().length() > 0) {
					name = pre + " " + name;
				}
				if (app != null && app.trim().length() > 0) {
					name = name + " " + app;
				}

				DataWriter.write(conf[sv][i], filenameConf, false);
				DataWriter.write(avg[sv][i], filenameAvg, false);
				int index1 = i * conf.length + sv;
				int index2 = index1 + conf.length * series.length;
				plotData[index1] = new PlotData(filenameConf, null,
						PlotData.WHISKER, i + 1, whiskerWidth);
				if (line) {
					plotData[index2] = new PlotData(filenameAvg, name,
							PlotData.LINE, i + 1, lineWidth);
				}
			}
		}
		boolean logscaleX = Config.getBoolean(plotKey + "_PLOT_LOGSCALE_X");
		boolean logscaleY = Config.getBoolean(plotKey + "_PLOT_LOGSCALE_Y");
		GNUPlot.plot(dest, plotData, title, xLabel, yLabel, key, logscaleX,
				logscaleY);
	}

	/**
	 * MULTI
	 */

	private static void multi(Series[] series, String folder, boolean conf) {
		String name = "";
		if (conf) {
			name = "PLOT_MULTI_CONF";
		} else {
			name = "PLOT_MULTI_AVG";
		}
		Timer timer = new Timer(Config.get(name).replace("%SERIES",
				series.length + "").replace("%DEST", folder));
		Filewriter.generateFolders(Config.get("MAIN_PLOT_FOLDER") + folder);

		String[][] keys = Config.allKeys("_DATA_PLOTS");
		for (int i = 0; i < keys.length; i++) {
			for (int j = 0; j < keys[i].length; j++) {
				if (conf) {
					multiConf(series, folder, keys[i][j]);
				} else {
					multiAvg(series, folder, keys[i][j]);
				}
			}
		}
		if (Config.getBoolean("PLOT_MULTI_LATEX_WRITE")) {
			LaTex.writeDataPlots(Config.get("MAIN_PLOT_FOLDER") + folder
					+ Config.get("PLOT_MULTI_LATEX_FILENAME"), Config
					.get("MAIN_PLOT_FOLDER")
					+ folder, series);
		}
		timer.end();
	}

	/**
	 * MULTI CONF
	 */

	private static void multiConf(Series[] series, String folder, String plotKey) {
		String[] data = Config.keys(plotKey + "_PLOT_DATA");
		for (int i = 0; i < data.length; i++) {
			if (!Config.containsData(data[i])) {
				return;
			}
		}
		String filename = Config.get(plotKey + "_PLOT_FILENAME");
		String ext = Config.get("PLOT_EXTENSION");
		String title = Config.get(plotKey + "_PLOT_TITLE");
		String xLabel = Config.get(plotKey + "_PLOT_X");
		String yLabel = Config.get(plotKey + "_PLOT_Y");
		String key = Config.get(plotKey + "_PLOT_KEY");
		String dest = folder + filename + ext;
		int lineWidth = Config.getInt("CONF_PLOT_LINE_WIDTH");
		int whiskerWidth = Config.getInt("CONF_PLOT_WHISKER_WIDTH");
		String mode = Config.get(plotKey + "_PLOT_MODE_CONF");
		boolean whiskersOnly = Config.get("PLOT_MODE_CONF_DEFAULT").equals(
				Config.get("PLOT_MODE_CONF_WHISKERS_ONLY_KEYWORD"));
		if (Config.get("PLOT_MODE_CONF_WHISKERS_ONLY_KEYWORD").equals(mode)) {
			whiskersOnly = true;
		}
		PlotData[] plotData = new PlotData[series.length * 2 * data.length];
		if (whiskersOnly) {
			plotData = new PlotData[series.length * data.length];
		}
		for (int d = 0; d < data.length; d++) {
			for (int i = 0; i < series.length; i++) {
				String filenameConf = series[i].confDataFolder()
						+ Config.get(data[d] + "_DATA_FILENAME")
						+ Config.get("DATA_EXTENSION");
				String filenameAvg = series[i].avgDataFolder()
						+ Config.get(data[d] + "_DATA_FILENAME")
						+ Config.get("DATA_EXTENSION");
				String name = series[i].network().description();

				String pre = Config.get(plotKey + "_PLOT_PRE_" + data[d]);
				String app = Config.get(plotKey + "_PLOT_APP_" + data[d]);
				if (pre != null && pre.trim().length() > 0) {
					name = pre + " " + name;
				}
				if (app != null && app.trim().length() > 0) {
					name = name + " " + app;
				}

				if (whiskersOnly) {
					int index1 = i * data.length + d;
					plotData[index1] = new PlotData(filenameConf, null,
							PlotData.WHISKER, i + 1, whiskerWidth);
				} else {
					int index1 = i * data.length + d;
					int index2 = index1 + data.length * series.length;
					plotData[index1] = new PlotData(filenameConf, null,
							PlotData.WHISKER, i + 1, whiskerWidth);
					plotData[index2] = new PlotData(filenameAvg, name,
							PlotData.LINE, i + 1, lineWidth + d);
				}
			}
		}
		boolean logscaleX = Config.getBoolean(plotKey + "_PLOT_LOGSCALE_X");
		boolean logscaleY = Config.getBoolean(plotKey + "_PLOT_LOGSCALE_Y");
		GNUPlot.plot(dest, plotData, title, xLabel, yLabel, key, logscaleX,
				logscaleY);
	}

	/**
	 * MULTI AVG
	 */

	private static void multiAvg(Series[] series, String folder, String plotKey) {
		String[] data = Config.keys(plotKey + "_PLOT_DATA");
		for (int i = 0; i < data.length; i++) {
			if (!Config.containsData(data[i])) {
				return;
			}
		}
		String filename = Config.get(plotKey + "_PLOT_FILENAME");
		String ext = Config.get("PLOT_EXTENSION");
		String title = Config.get(plotKey + "_PLOT_TITLE");
		String xLabel = Config.get(plotKey + "_PLOT_X");
		String yLabel = Config.get(plotKey + "_PLOT_Y");
		String key = Config.get(plotKey + "_PLOT_KEY");
		String dest = folder + filename + ext;
		int lineWidth = Config.getInt("AVERAGE_PLOT_LINE_WIDTH");
		int pointWidth = Config.getInt("AVERAGE_PLOT_POINT_WIDTH");
		int dotWidth = Config.getInt("AVERAGE_PLOT_DOT_WIDTH");
		String mode = Config.get(plotKey + "_PLOT_MODE_AVG");
		boolean pointsOnly = Config.get("PLOT_MODE_AVG_DEFAULT").equals(
				Config.get("PLOT_MODE_AVG_POINTS_ONLY_KEYWORD"));
		boolean dotsOnly = Config.get("PLOT_MODE_AVG_DEFAULT").equals(
				Config.get("PLOT_MODE_AVG_DOTS_ONLY_KEYWORD"));
		boolean lineOnly = Config.get("PLOT_MODE_AVG_DEFAULT").equals(
				Config.get("PLOT_MODE_AVG_LINE_ONLY_KEYWORD"));
		if (Config.get("PLOT_MODE_AVG_POINTS_ONLY_KEYWORD").equals(mode)) {
			pointsOnly = true;
		}
		if (Config.get("PLOT_MODE_AVG_DOTS_ONLY_KEYWORD").equals(mode)) {
			dotsOnly = true;
		}
		if (Config.get("PLOT_MODE_AVG_LINE_ONLY_KEYWORD").equals(mode)) {
			lineOnly = true;
		}
		PlotData[] plotData = new PlotData[series.length * 2 * data.length];
		if (pointsOnly || dotsOnly || lineOnly) {
			plotData = new PlotData[series.length * data.length];
		}
		for (int d = 0; d < data.length; d++) {
			for (int i = 0; i < series.length; i++) {
				String file = series[i].avgDataFolder()
						+ Config.get(data[d] + "_DATA_FILENAME")
						+ Config.get("DATA_EXTENSION");
				String name = series[i].network().description();

				String pre = Config.get(plotKey + "_PLOT_PRE_" + data[d]);
				String app = Config.get(plotKey + "_PLOT_APP_" + data[d]);
				if (pre != null && pre.trim().length() > 0) {
					name = pre + " " + name;
				}
				if (app != null && app.trim().length() > 0) {
					name = name + " " + app;
				}
				if (pointsOnly) {
					int index1 = i * data.length + d;
					plotData[index1] = new PlotData(file, name,
							PlotData.POINTS, i + 1, pointWidth);
				} else if (dotsOnly) {
					int index1 = i * data.length + d;
					plotData[index1] = new PlotData(file, name, PlotData.DOTS,
							i + 1, dotWidth);
				} else if (lineOnly) {
					int index1 = i * data.length + d;
					plotData[index1] = new PlotData(file, name, PlotData.LINE,
							i + 1, pointWidth);
				} else {
					int index1 = i * data.length + d;
					int index2 = index1 + data.length * series.length;
					plotData[index1] = new PlotData(file, name,
							PlotData.POINTS, i + 1, pointWidth);
					plotData[index2] = new PlotData(file, null, PlotData.LINE,
							i + 1, lineWidth);
				}
			}
		}
		boolean logscaleX = Config.getBoolean(plotKey + "_PLOT_LOGSCALE_X");
		boolean logscaleY = Config.getBoolean(plotKey + "_PLOT_LOGSCALE_Y");
		GNUPlot.plot(dest, plotData, title, xLabel, yLabel, key, logscaleX,
				logscaleY);
	}

	/**
	 * FAST PLOT
	 */
	public static void fast(double[][] data, String[] labels, String title,
			String x, String y, String dest) {
		fast(data, labels, title, x, y, dest, "right", PlotData.LINE, 2);
	}

	public static void fast(double[][] data, String[] labels, String title,
			String x, String y, String dest, String key, int type, int width) {
		PlotData[] pd = new PlotData[data.length];
		for (int i = 0; i < data.length; i++) {
			String file = Config.get("TEMP_FOLDER") + (i)
					+ Config.get("DATA_EXTENSION");
			DataWriter.write(data[i], file, true);
			pd[i] = new PlotData(file, labels[i], type, i + 1, width);
		}
		GNUPlot.plot(dest, pd, title, x, y, key, false, false);
	}
}

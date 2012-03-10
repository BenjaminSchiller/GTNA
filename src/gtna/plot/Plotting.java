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
import gtna.metrics.Metric;
import gtna.plot.Data.Type;
import gtna.plot.Gnuplot.Style;
import gtna.util.Config;
import gtna.util.Timer;

import java.io.File;

/**
 * @author benni
 * 
 */
public class Plotting {

	public static boolean single(Series[][] s, Metric[] metrics, String folder,
			Type type, Style style) {
		Timer timer = new Timer("single (" + s.length + ") " + type + " / "
				+ style);
		boolean subfolders = Config.getBoolean("PLOT_SUBFOLDERS");
		boolean success = true;
		for (Metric m : metrics) {
			for (String key : m.getDataPlotKeys()) {
				String pre = Config.get("MAIN_PLOT_FOLDER") + folder
						+ (subfolders ? m.getFolder() : m.getFolderName());
				(new File(pre)).mkdirs();
				success &= Plotting.single(s, m, key, pre, type, style);
			}
		}
		timer.end();
		return success;
	}

	private static boolean single(Series[][] s, Metric m, String plotKey,
			String pre, Type type, Style style) {
		// TODO implement
		return false;
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

	public static boolean multi(Series s, Metric[] metrics, String folder) {
		return Plotting.multi(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean multi(Series[] s, Metric[] metrics, String folder) {
		return Plotting.multi(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean multi(Series[][] s, Metric[] metrics, String folder) {
		return Plotting.plotMulti(s, metrics, folder, Type.average,
				Style.linespoint);
	}

	public static boolean multi(Series s, Metric[] metrics, String folder,
			Type type, Style style) {
		return Plotting.multi(new Series[] { s }, metrics, folder, type, style);
	}

	public static boolean plotMulti(Series[][] s, Metric[] metrics,
			String folder, Type type, Style style) {
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

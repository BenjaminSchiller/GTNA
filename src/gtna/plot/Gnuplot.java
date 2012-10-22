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
 * Gnuplot.java
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

import gtna.io.DataWriter;
import gtna.io.Output;
import gtna.metrics.Metric;
import gtna.plot.data.Data;
import gtna.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author benni
 * 
 */
public class Gnuplot {
	// http://t16web.lanl.gov/Kawano/gnuplot/intro/style-e.html
	public static enum Style {
		lines, dots, points, linespoint, impulses, steps, boxes, candlesticks, yerrorbars
	}

	private static String getScriptName(Metric[] m, String plotKey) {
		if (m == null) {
			return Config.get("TEMP_FOLDER") + plotKey + ".gnuplot.txt";
		} else if (m.length > 1) {
			return Config.get("TEMP_FOLDER") + m[0].getKey()
					+ Config.get("PLOT_GROUPED_KEYWORD") + "." + plotKey
					+ ".gnuplot.txt";
		} else {
			return Config.get("TEMP_FOLDER") + m[0].getFolderName() + "."
					+ plotKey + ".gnuplot.txt";
		}
	}

	private static String getDataName(Metric m, String plotKey, int index) {
		if (m == null) {
			return Config.get("TEMP_FOLDER") + plotKey + ".data." + index
					+ ".txt";
		}
		return Config.get("TEMP_FOLDER") + m.getFolderName() + "." + plotKey
				+ ".data." + index + ".txt";
	}

	public static String writeTempData(Metric m, String plotKey, int index,
			double[][] data) {
		String filename = Gnuplot.getDataName(m, plotKey, index);
		if (!DataWriter.write(data, filename, false)) {
			return null;
		}
		return filename;
	}

	public static boolean plot(Plot plot, Metric[] m, String plotKey) {
		String filename = Gnuplot.getScriptName(m, plotKey);
		String config = null;
		int index = 0;
		while ((config = Config.get("GNUPLOT_CONFIG_" + index++)) != null) {
			plot.addConfig(config);
		}
		for (Data data : plot.getData()) {
			for (String c : data.getConfig()) {
				plot.addConfig(c);
			}
		}
		plot.write(filename);
		String cmd = Config.get("GNUPLOT_PATH") + " " + filename;
		return execute(cmd);
	}

	private static boolean execute(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd, null);

			if (Config.getBoolean("GNUPLOT_PRINT_ERRORS")) {
				InputStream stderr = p.getErrorStream();
				InputStreamReader isr = new InputStreamReader(stderr);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					Output.writeln(line);
				}
			}
			p.waitFor();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
}

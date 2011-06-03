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
 * GNUPlot.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-03 : adding configurable offsets to plots (BS)
*/
package gtna.plot;

import gtna.io.Filewriter;
import gtna.io.Output;
import gtna.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author benni
 *
 */
public class GNUPlot {
	public static boolean plot(String dest, PlotData[] data, String title,
			String xlabel, String ylabel, String key, boolean logscaleX,
			boolean logscaleY) {
		dest = Config.get("MAIN_PLOT_FOLDER") + dest;
		Filewriter.generateFolders(dest);

		if (key == null || key.equals("null")) {
			key = Config.get("GNUPLOT_DEFAULT_KEY");
		}

		String COMMAND = Config.get("GNUPLOT_COMMAND");

		String TERMINAL = Config.get("GNUPLOT_CMD_TERMINAL");
		COMMAND = COMMAND.replace("%TERMINAL", TERMINAL);

		String OUTPUT = Config.get("GNUPLOT_CMD_OUTPUT");
		OUTPUT = OUTPUT.replace("%OUTPUT", dest);
		COMMAND = COMMAND.replace("%OUTPUT", OUTPUT);

		String KEY = Config.get("GNUPLOT_CMD_KEY");
		KEY = KEY.replace("%KEY", key);
		COMMAND = COMMAND.replace("%KEY", KEY);

		String TITLE = Config.get("GNUPLOT_CMD_TITLE");
		TITLE = TITLE.replace("%TITLE", title);
		COMMAND = COMMAND.replace("%TITLE", TITLE);

		String ETC = Config.get("GNUPLOT_CMD_ETC");
		COMMAND = COMMAND.replace("%ETC", ETC);

		if (logscaleX) {
			String LOGX = Config.get("GNUPLOT_CMD_LOGSCALE_X");
			COMMAND = COMMAND.replace("%LOGX", LOGX);
		} else {
			COMMAND = COMMAND.replace("%LOGX", "");
		}

		if (logscaleY) {
			String LOGY = Config.get("GNUPLOT_CMD_LOGSCALE_Y");
			COMMAND = COMMAND.replace("%LOGY", LOGY);
		} else {
			COMMAND = COMMAND.replace("%LOGY", "");
		}

		if (xlabel.equals(Config.get("PLOT_NO_TICS_KEYWORD"))) {
			COMMAND = COMMAND.replace("%X", Config.get("GNUPLOT_CMD_NO_XTICS"));
		} else {
			String X = Config.get("GNUPLOT_CMD_XLABEL");
			X = X.replace("%XLABEL", xlabel);
			COMMAND = COMMAND.replace("%X", X);
		}

		if (ylabel.equals(Config.get("PLOT_NO_TICS_KEYWORD"))) {
			COMMAND = COMMAND.replace("%Y", Config.get("GNUPLOT_CMD_NO_YTICS"));
		} else {
			String Y = Config.get("GNUPLOT_CMD_YLABEL");
			Y = Y.replace("%YLABEL", ylabel);
			COMMAND = COMMAND.replace("%Y", Y);
		}

		String PLOT = Config.get("GNUPLOT_CMD_PLOT");
		String sep = Config.get("GNUPLOT_CMD_DATA_SEPARATOR");
		StringBuffer DATA = new StringBuffer(getDataItem(data[0]));
		for (int i = 1; i < data.length; i++) {
			DATA.append(sep);
			DATA.append(getDataItem(data[i]));
		}
		PLOT = PLOT.replace("%DATA", DATA);
		COMMAND = COMMAND.replace("%PLOT", PLOT);

		String TEMP = Config.get("GNUPLOT_TEMP_FILE");
		Filewriter fw = new Filewriter(TEMP);
		fw.write(COMMAND);
		fw.close();

		return plot(TEMP);
	}

	private static String getDataItem(PlotData data) {
		if (data.type == PlotData.FUNCTION) {
			// return data.file + " title \"" + data.title + "\"";
			return null;
		}

		String TYPE = null;
		if (data.type == PlotData.DOTS) {
			TYPE = Config.get("GNUPLOT_CMD_DATA_TYPE_DOTS");
		} else if (data.type == PlotData.LINE) {
			TYPE = Config.get("GNUPLOT_CMD_DATA_TYPE_LINE");
		} else if (data.type == PlotData.POINTS) {
			TYPE = Config.get("GNUPLOT_CMD_DATA_TYPE_POINTS");
		} else if (data.type == PlotData.WHISKER) {
			TYPE = Config.get("GNUPLOT_CMD_DATA_TYPE_WHISKER");
		}
		TYPE = TYPE.replace("%OFFSETX", data.offsetX + "");
		TYPE = TYPE.replace("%OFFSETY", data.offsetY + "");

		String TITLE = null;
		if (data.title != null) {
			TITLE = Config.get("GNUPLOT_CMD_DATA_TITLE");
			TITLE = TITLE.replace("%TITLE", data.title);
		} else {
			TITLE = Config.get("GNUPLOT_CMD_DATA_NO_TITLE");
		}

		String ITEM = Config.get("GNUPLOT_CMD_DATA_ITEM");
		ITEM = ITEM.replace("%FILE", data.file);
		ITEM = ITEM.replace("%TYPE", TYPE);
		ITEM = ITEM.replace("%LINE_TYPE", data.lineType + "");
		ITEM = ITEM.replace("%LINE_WIDTH", data.lineWidth + "");
		ITEM = ITEM.replace("%TITLE", TITLE);

		return ITEM;
	}

	private static boolean plot(String filename) {
		String cmd = Config.get("GNUPLOT_PATH") + " " + filename;
		return execute(cmd);
	}

	private static boolean execute(String cmd) {
		try {
			String[] envp = new String[] { Config.get("GNUPLOT_ENVP") };
			if (envp[0] == null || envp.length == 0) {
				envp = null;
			}
			Process p = Runtime.getRuntime().exec(cmd, envp);

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

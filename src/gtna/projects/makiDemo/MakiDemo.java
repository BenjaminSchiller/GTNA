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
 * MakiDemo.java
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
package gtna.projects.makiDemo;

import gtna.util.Config;
import gtna.util.Execute;

/**
 * @author benni
 * 
 */
public class MakiDemo {
	protected static String terminal;

	protected static void config(int demo, boolean skip, String terminal, int lw) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/maki/demo" + demo + "/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/maki/demo" + demo + "/");

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skip);
		Config.overwrite("PLOT_EXTENSION", "." + terminal);
		Config.overwrite("GNUPLOT_TERMINAL", terminal);
		Config.overwrite("GNUPLOT_LW", "" + lw);

		MakiDemo.terminal = terminal;
	}

	protected static void open(String[] locations) {
		for (String location : locations) {
			open(location);
		}
	}

	protected static void open(String location) {
		System.out.println(">>> opening " + location);
		Execute.exec("open " + location);
	}

	protected static void openPlots(String[] plots) {
		for (String plot : plots) {
			openPlot(plot);
		}
	}

	protected static void openPlot(String plot) {
		open(Config.get("MAIN_PLOT_FOLDER") + plot + "." + terminal);
	}
}

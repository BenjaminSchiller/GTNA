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
 * Plot.java
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

import gtna.io.Filewriter;
import gtna.util.Config;

import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class Plot {
	private Data[] data;

	private String terminal;

	private String output;

	private String key;

	private String title;

	private String xLabel;

	private String yLabel;

	private ArrayList<String> config;

	public Plot(Data[] data, String terminal, String output) {
		this.data = data;
		this.terminal = terminal;
		this.output = output;
		this.config = new ArrayList<String>();
	}

	public boolean write(String filename) {
		Filewriter fw = new Filewriter(filename);
		fw.writeln("set terminal " + this.terminal);
		fw.writeln("set output \"" + this.output + "\"");
		if (this.key != null) {
			fw.writeln("set key " + this.key);
		}
		if (this.title != null) {
			fw.writeln("set title \"" + this.title + "\"");
		}
		if (this.xLabel != null) {
			fw.writeln("set xlabel \"" + this.xLabel + "\"");
		}
		if (this.yLabel != null) {
			fw.writeln("set ylabel \"" + this.yLabel + "\"");
		}
		for (String config : this.config) {
			fw.writeln(config);
		}
		fw.write("plot ");
		int lw = Config.getInt("GNUPLOT_LW");
		double offsetX = Config.getDouble("GNUPLOT_OFFSET_X");
		double offsetY = Config.getDouble("GNUPLOT_OFFSET_Y");
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				fw.write(", \\\n"
						+ data[i].getEntry(i + 1, lw, offsetX * i, offsetY * i));
			} else {
				fw.write(data[i].getEntry(i + 1, lw, offsetX * i, offsetY * i));
			}
		}
		return fw.close();
	}

	public Data[] getData() {
		return this.data;
	}

	public void addConfig(String config) {
		this.config.add(config);
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}
}

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
 * GnuplotData.java
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
package gtna.plot.data;

import gtna.plot.Gnuplot.Style;

/**
 * @author benni
 * 
 */
public abstract class Data {
	public static enum Type {
		average, median, minimum, maximum, variance, confidence1, confidence2, function
	}

	protected String data;

	protected Style style;

	protected String title;

	public Data(String data, Style style, String title) {
		this.data = data;
		this.style = style;
		this.title = title;
	}

	public abstract boolean isStyleValid();

	public abstract String getEntry(int lt, int lw, double offsetX,
			double offsetY);

	public String[] getConfig() {
		return new String[0];
	}

	public static Data get(String data, Style style, String title, Type type) {
		switch (type) {
		case average:
			return new AverageData(data, style, title);
		case median:
			return new MedianData(data, style, title);
		case minimum:
			return new MinimumData(data, style, title);
		case maximum:
			return new MaximumData(data, style, title);
		case variance:
			return new VarianceData(data, style, title);
		case confidence1:
			return new ConfidenceData1(data, style, title);
		case confidence2:
			return new ConfidenceData2(data, style, title);
		case function:
			return new FunctionData(data, style, title);
		default:
			return null;
		}
	}
}

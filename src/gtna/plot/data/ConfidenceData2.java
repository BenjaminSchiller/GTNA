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
 * ConfidenceData.java
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
public class ConfidenceData2 extends Data {

	public ConfidenceData2(String data, Style style, String title) {
		super(data, style, title);
	}

	@Override
	public boolean isStyleValid() {
		return this.style.equals(Style.candlesticks);
	}

	@Override
	public String getEntry(int lt, int lw, double offsetX, double offsetY) {
		StringBuffer buff = new StringBuffer();
		// 2 avg
		// 3 med
		// 4 min
		// 5 max
		// 6 var
		// 7 varLow
		// 8 varUp
		// 9 confLow
		// 10 confUp
		// X Min 1stQuartile Median 3rdQuartile Max
		buff.append("'" + this.data + "' using ($1 + " + offsetX + "):($9 + "
				+ offsetY + "):($4 + " + offsetY + "):($5 + " + offsetY
				+ "):($10 + " + offsetY + ") with " + this.style);
		buff.append(" lt " + lt + " lw " + lw);
		buff.append(title == null ? " notitle" : " title \"" + this.title
				+ "\"");
		buff.append(",\\\n");
		buff.append("'' using ($1 + " + offsetX + "):($3 + " + offsetY
				+ "):($3 + " + offsetY + "):($3 + " + offsetY + "):($3 + "
				+ offsetY + ") with " + this.style + " lt -1 lw " + lw
				+ " notitle");
		buff.append(",\\\n");
		buff.append("'' using ($1 + " + offsetX + "):($2 + " + offsetY
				+ ") with " + Style.lines + " lt " + lt + " lw " + lw
				+ " notitle");
		return buff.toString();
	}

	public String[] getConfig() {
		return new String[] { "set style fill empty", "set boxwidth 0.2" };
	}

}

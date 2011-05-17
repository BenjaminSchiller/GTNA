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
 * PlotData.java
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

/**
 * 
 * @author benni
 *
 */
public class PlotData {
	public static final int POINTS = 1;

	public static final int DOTS = 2;

	public static final int LINE = 3;

	public static final int WHISKER = 4;

	public static final int FUNCTION = 5;

	public String file;

	public String title;

	public int type;

	public int lineType;

	public int lineWidth;

	public PlotData(String file, String title, int type, int lineType,
			int lineWidth) {
		this.file = file;
		this.title = title;
		this.type = type;
		this.lineType = lineType;
		this.lineWidth = lineWidth;
	}
	
	public String toString(){
		return "(" + this.type + ") " + this.title + " in " + this.file;
	}
}

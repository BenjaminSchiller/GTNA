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
 * Table.java
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
package gtna.io;

import gtna.util.Config;

import java.text.DecimalFormat;

/**
 * @author benni
 * 
 */
public class Table {

	private double[][] entries;

	private String caption;

	private String label;

	private String xLabel;
	private String[] xLabels;

	private String yLabel;
	private String[] yLabels;

	public static final double EMPTY_VALUE = Double.MIN_VALUE;

	public Table(double[][] entries) {
		this.entries = entries;
	}

	public Table(double[][] entries, String caption, String label,
			String xLabel, String[] xLabels, String yLabel, String[] yLabels) {
		this.entries = entries;
		this.caption = caption;
		this.label = label;
		this.xLabel = xLabel;
		this.xLabels = xLabels;
		this.yLabel = yLabel;
		this.yLabels = yLabels;
	}

	public String toLaTex() {
		StringBuffer buff = new StringBuffer();

		buff.append("\\begin{center}\n");
		buff.append("\\begin{table*}[ht]\n");
		buff.append("\\center\n");

		buff.append("\\begin{tabular}{ll");
		for (int i = 0; i < this.xLabels.length; i++) {
			buff.append("r");
		}
		buff.append("}\n");
		buff.append("\\toprule\n");

		buff.append("& & \\multicolumn{" + (this.xLabels.length) + "}{c}{\\it{"
				+ this.xLabel + "}}\\\\\n");

		buff.append("\\multirow{" + this.yLabels.length + "}{*}{\\it{"
				+ this.yLabel + "}} & ");
		for (int i = 0; i < this.xLabels.length; i++) {
			buff.append(" & \\textbf{" + this.xLabels[i] + "}");
		}
		buff.append("\\\\\n");

		buff.append("\\midrule\n");

		int decimals = Config.getInt("LATEX_TABLE_DECIMALS");
		boolean cutZero = this.allZero(this.entries);
		boolean cutDecimals = this.allInt(this.entries, decimals);

		for (int i = 0; i < this.entries.length; i++) {
			buff.append(" & \\textbf{" + this.yLabels[i] + "}");
			for (int j = 0; j < this.xLabels.length; j++) {
				if ((this.entries[i][j] != Table.EMPTY_VALUE)
						&& (this.entries[i].length - 1 >= j)) {
					String v = this.formatNumber(this.entries[i][j], decimals,
							cutZero, cutDecimals, true);
					buff.append(" & " + v);
				} else {
					buff.append(" & -");
				}
			}
			buff.append("\\\\\n");
		}

		buff.append("\\bottomrule\n");
		buff.append("\\end{tabular}\n");
		if (caption != null) {
			buff.append("\\caption{" + this.caption + "}\n");
		}
		if (label != null) {
			buff.append("\\label{" + this.label + "}\n");
		}
		buff.append("$\\;$\\\\$\\;$\\\\\n");
		buff.append("\\end{table*}\n");
		buff.append("\\end{center}\n");

		return buff.toString();
	}

	private static final DecimalFormat showAllDigitsFormat = new DecimalFormat(
			"0.0#####################################");

	private boolean allInt(double[][] entries, int decimals) {
		for (double[] e : entries) {
			for (double v : e) {
				double d = Double.parseDouble(this.formatNumber(v, decimals,
						false, false, false));
				if (Math.ceil(d) != Math.floor(d)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean allZero(double[][] entries) {
		for (double[] e : entries) {
			for (double v : e) {
				if (!(0 <= v && v < 1)) {
					return false;
				}
			}
		}
		return true;
	}

	private String formatNumber(double d, int decimalPlaces, boolean cutZero,
			boolean cutDecimals, boolean addCommas) {

		if (decimalPlaces < 0) {
			throw new IllegalArgumentException(
					"Cannot have negative decimal places.");
		}

		if (decimalPlaces == 0) {
			long rounded = Math.round(d);
			if (addCommas) {
				return this.addCommas(rounded + "");
			} else {
				return rounded + "";
			}
		} else {
			long rounded = Math.round(d * Math.pow(10, decimalPlaces));
			d = rounded;
			for (int i = 0; i < decimalPlaces; i++) {
				d /= 10.0d;
			}
			String s = showAllDigitsFormat.format(d);
			int decimalIndex = 0;
			if (s.contains(".")) {
				decimalIndex = s.indexOf('.');
			} else if (s.contains(",")) {
				decimalIndex = s.indexOf(',');
			}

			String leftSide = s.substring(0, decimalIndex);
			String rightSide = s.substring(decimalIndex + 1);

			if (addCommas) {
				leftSide = this.addCommas(leftSide);
			}
			if (decimalPlaces < rightSide.length()) {
				rightSide = rightSide.substring(0, decimalPlaces);
			} else if (decimalPlaces > rightSide.length()) {
				StringBuilder sb = new StringBuilder(rightSide);
				for (int i = 0; i < decimalPlaces - rightSide.length(); i++) {
					sb.append('0');
				}
				rightSide = sb.toString();
			}

			if (cutZero) {
				return "." + rightSide;
			} else if (cutDecimals) {
				return leftSide;
			} else {
				return leftSide + "." + rightSide;
			}
		}

	}

	private String addCommas(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = s.length() - 1; i >= 0; i--) {
			sb.insert(0, s.charAt(i));
			if (i > 0 && (s.length() - i) % 3 == 0) {
				sb.insert(0, ',');
			}
		}
		return sb.toString();
	}

	/**
	 * @return the entries
	 */
	public double[][] getEntries() {
		return this.entries;
	}

	/**
	 * @param entries
	 *            the entries to set
	 */
	public void setEntries(double[][] entries) {
		this.entries = entries;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the xLabel
	 */
	public String getxLabel() {
		return this.xLabel;
	}

	/**
	 * @param xLabel
	 *            the xLabel to set
	 */
	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	/**
	 * @return the xLabels
	 */
	public String[] getxLabels() {
		return this.xLabels;
	}

	/**
	 * @param xLabels
	 *            the xLabels to set
	 */
	public void setxLabels(String[] xLabels) {
		this.xLabels = xLabels;
	}

	/**
	 * @return the yLabel
	 */
	public String getyLabel() {
		return this.yLabel;
	}

	/**
	 * @param yLabel
	 *            the yLabel to set
	 */
	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}

	/**
	 * @return the yLabels
	 */
	public String[] getyLabels() {
		return this.yLabels;
	}

	/**
	 * @param yLabels
	 *            the yLabels to set
	 */
	public void setyLabels(String[] yLabels) {
		this.yLabels = yLabels;
	}
}

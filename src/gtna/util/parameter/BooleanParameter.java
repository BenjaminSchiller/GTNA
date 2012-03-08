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
 * BooleanParameter.java
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
package gtna.util.parameter;

/**
 * @author benni
 * 
 */
public class BooleanParameter extends Parameter {
	private boolean booleanValue;

	private String t;

	private String f;

	public BooleanParameter(String key, boolean booleanValue) {
		this(key, booleanValue, "true", "false");
	}

	public BooleanParameter(String key, boolean booleanValue, String t, String f) {
		super(key, booleanValue ? t : f);
		this.booleanValue = booleanValue;
		this.t = t;
		this.f = f;
	}

	public boolean getBooleanValue() {
		return this.booleanValue;
	}

	public String getT() {
		return this.t;
	}

	public String getF() {
		return this.f;
	}

}

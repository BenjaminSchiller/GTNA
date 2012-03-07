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
 * ParameterList.java
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
package gtna.util;

/**
 * @author benni
 * 
 */
public class ParameterList {
	private String key;

	private Parameter[] parameters;

	public ParameterList(String key) {
		this(key, new Parameter[0]);
	}

	public ParameterList(String key, Parameter[] parameters) {
		this.key = key;
		this.parameters = parameters;
	}

	public String getKey() {
		return this.key;
	}

	public Parameter[] getParameters() {
		return this.parameters;
	}

	public String getFolder() {
		StringBuffer buff = new StringBuffer(this.key);
		for (Parameter p : this.parameters) {
			buff.append("-" + p.getValue());
		}
		return buff.toString();
	}

	public String getDescription() {
		return this.getDescription("");
	}

	public String getDescriptionLong() {
		return this.getDescriptionLong("");
	}

	public String getDescriptionShort() {
		return this.getDescriptionShort("");
	}

	public String diffDescription(ParameterList compareTo) {
		return this.getDescription(this.diffParameterKey(compareTo));
	}

	public String diffDescriptionLong(ParameterList compareTo) {
		return this.getDescriptionLong(this.diffParameterKey(compareTo));
	}

	public String diffDescriptionShort(ParameterList compareTo) {
		return this.getDescriptionShort(this.diffParameterKey(compareTo));
	}

	private String getDescription(String keyX) {
		StringBuffer buff = new StringBuffer(this.getName());
		if (this.parameters.length == 0) {
			return buff.toString();
		}
		buff.append(" (");
		for (int i = 0; i < this.parameters.length; i++) {
			if (i > 0) {
				buff.append(", ");
			}
			String value = this.parameters[i].getValue();
			if (keyX != null && keyX.equals(this.parameters[i].getKey())) {
				value = "X";
			}
			buff.append(this.getParameterName(this.parameters[i]));
			buff.append(" = " + value);
		}
		buff.append(")");
		return buff.toString();
	}

	private String getDescriptionLong(String keyX) {
		StringBuffer buff = new StringBuffer(this.getNameLong());
		if (this.parameters.length == 0) {
			return buff.toString();
		}
		buff.append(" (");
		for (int i = 0; i < this.parameters.length; i++) {
			if (i > 0) {
				buff.append(", ");
			}
			String value = this.parameters[i].getValue();
			if (keyX != null && keyX.equals(this.parameters[i].getKey())) {
				value = "X";
			}
			buff.append(this.getParameterNameLong(this.parameters[i]));
			buff.append("=" + value);
		}
		buff.append(")");
		return buff.toString();
	}

	private String getDescriptionShort(String keyX) {
		StringBuffer buff = new StringBuffer(this.getNameShort());
		if (this.parameters.length == 0) {
			return buff.toString();
		}
		buff.append("(");
		for (int i = 0; i < this.parameters.length; i++) {
			if (i > 0) {
				buff.append(",");
			}
			String value = this.parameters[i].getValue();
			if (keyX != null && keyX.equals(this.parameters[i].getKey())) {
				value = "X";
			}
			buff.append(this.getParameterNameShort(this.parameters[i]));
			buff.append("=" + value);
		}
		buff.append(")");
		return buff.toString();
	}

	public String diffParameterName(ParameterList p2) {
		return this.diffParameterNameXY(p2, "_NAME");
	}

	public String diffParameterNameLong(ParameterList p2) {
		return this.diffParameterNameXY(p2, "_NAME_LONG");
	}

	public String diffParameterNameShort(ParameterList p2) {
		return this.diffParameterNameXY(p2, "_NAME_SHORT");
	}

	private String diffParameterNameXY(ParameterList p2, String xy) {
		if (!p2.getKey().equals(this.key)) {
			return null;
		}
		if (p2.getParameters().length != this.parameters.length) {
			return null;
		}
		for (int i = 0; i < this.parameters.length; i++) {
			if (!this.parameters[i].getKey().equals(
					p2.getParameters()[i].getKey())) {
				return null;
			}
			if (!this.parameters[i].getValue().equals(
					p2.getParameters()[i].getValue())) {
				return this.getParameterNameXY(this.parameters[i], xy);
			}
		}
		return null;
	}

	private String diffParameterKey(ParameterList p2) {
		if (!p2.getKey().equals(this.key)) {
			return null;
		}
		if (p2.getParameters().length != this.parameters.length) {
			return null;
		}
		for (int i = 0; i < this.parameters.length; i++) {
			if (!this.parameters[i].getKey().equals(
					p2.getParameters()[i].getKey())) {
				return null;
			}
			if (!this.parameters[i].getValue().equals(
					p2.getParameters()[i].getValue())) {
				return this.parameters[i].getKey();
			}
		}
		return null;
	}

	public double diffParameterValue(ParameterList p2) {
		if (!p2.getKey().equals(this.key)) {
			return -1;
		}
		if (p2.getParameters().length != this.parameters.length) {
			return -1;
		}
		for (int i = 0; i < this.parameters.length; i++) {
			if (!this.parameters[i].getKey().equals(
					p2.getParameters()[i].getKey())) {
				return -1;
			}
			if (!this.parameters[i].getValue().equals(
					p2.getParameters()[i].getValue())) {
				try {
					return Double.parseDouble(this.parameters[i].getValue());
				} catch (Exception e) {
					return -1;
				}
			}
		}
		return -1;
	}

	public String getName() {
		return this.getNameXY("_NAME");
	}

	public String getNameLong() {
		return this.getNameXY("_NAME_LONG");
	}

	public String getNameShort() {
		return this.getNameXY("_NAME_SHORT");
	}

	public String getParameterName(Parameter p) {
		return this.getParameterNameXY(p, "_NAME");
	}

	public String getParameterNameLong(Parameter p) {
		return this.getParameterNameXY(p, "_NAME_LONG");
	}

	public String getParameterNameShort(Parameter p) {
		return this.getParameterNameXY(p, "_NAME_SHORT");
	}

	private String getNameXY(String xy) {
		return Config.get(this.key + xy);
	}

	private String getParameterNameXY(Parameter p, String xy) {
		if (p.getKey().equals("NODES")) {
			if (xy.endsWith("_LONG")) {
				return "Nodes";
			} else if (xy.endsWith("_SHORT")) {
				return "N";
			} else {
				return "Nodes";
			}
		}
		return Config.get(this.key + "_" + p.getKey() + xy);
	}
}

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
package gtna.util.parameter;

import gtna.util.Config;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class ParameterList {
	protected String key;

	protected Parameter[] parameters;

	protected HashMap<String, Parameter> map;

	public ParameterList(String key) {
		this(key, new Parameter[0]);
	}

	public ParameterList(String key, Parameter[] parameters) {
		this.key = key;
		this.parameters = parameters;
		this.map = new HashMap<String, Parameter>();
		for (Parameter p : parameters) {
			this.map.put(p.getKey(), p);
		}
	}

	public String getKey() {
		return this.key;
	}

	public Parameter[] getParameters() {
		return this.parameters;
	}

	public Parameter getParameter(String key) {
		return this.map.get(key);
	}

	public String getFolderName() {
		StringBuffer buff = new StringBuffer(this.key);
		for (Parameter p : this.parameters) {
			buff.append("-" + p.getValue());
		}
		String folderName = buff.toString();
		if(folderName.length() > 255){
			return Integer.toString(folderName.hashCode());
		}
		return folderName;
	}

	public String getFolder() {
		return this.getFolderName() + Config.get("FILESYSTEM_FOLDER_DELIMITER");
	}

	protected static Parameter[] append(Parameter[] p1, Parameter p2) {
		return ParameterList.append(p1, new Parameter[] { p2 });
	}

	protected static Parameter[] prepend(Parameter[] p1, Parameter p2) {
		return ParameterList.append(new Parameter[] { p2 }, p1);
	}

	protected static Parameter[] append(Parameter[] p1, Parameter[] p2) {
		Parameter[] p = new Parameter[p1.length + p2.length];
		int index = 0;
		for (Parameter param : p1) {
			p[index++] = param;
		}
		for (Parameter param : p2) {
			p[index++] = param;
		}
		return p;
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

	public String getDiffDescription(ParameterList compareTo) {
		return this.getDescription(this.getDiffParameter(compareTo).getKey());
	}

	public String getDiffDescriptionLong(ParameterList compareTo) {
		return this.getDescriptionLong(this.getDiffParameter(compareTo)
				.getKey());
	}

	public String getDiffDescriptionShort(ParameterList compareTo) {
		return this.getDescriptionShort(this.getDiffParameter(compareTo)
				.getKey());
	}

	public String getDescription(String keyX) {
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

	public String getDescriptionLong(String keyX) {
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

	public String getDescriptionShort(String keyX) {
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

	public String getDiffParameterName(ParameterList p2) {
		return this.getDiffParameterNameXY(p2, "_NAME");
	}

	public String getDiffParameterNameLong(ParameterList p2) {
		return this.getDiffParameterNameXY(p2, "_NAME_LONG");
	}

	public String getDiffParameterNameShort(ParameterList p2) {
		return this.getDiffParameterNameXY(p2, "_NAME_SHORT");
	}

	public Parameter getDiffParameter(ParameterList pl2) {
		if (!pl2.getKey().equals(this.key)) {
			return null;
		}
		if (pl2.getParameters().length != this.parameters.length) {
			return null;
		}
		for (int i = 0; i < this.parameters.length; i++) {
			Parameter p1 = this.parameters[i];
			Parameter p2 = pl2.getParameters()[i];
			if (!p1.getKey().equals(p2.getKey())) {
				return null;
			}
			if (!p1.getValue().equals(p2.getValue())
					&& (p1 instanceof DoubleParameter || p1 instanceof IntParameter)) {
				return p1;
			}
		}
		return null;
	}

	public String getDiffParameterNameXY(ParameterList pl2, String xy) {
		Parameter p = this.getDiffParameter(pl2);
		if (p == null) {
			return null;
		}
		return this.getParameterNameXY(p, xy);
	}

	public double getDiffParameterValue(ParameterList pl2) {
		Parameter p = this.getDiffParameter(pl2);
		if (p instanceof DoubleParameter) {
			return ((DoubleParameter) p).getDoubleValue();
		}
		if (p instanceof IntParameter) {
			return ((IntParameter) p).getIntValue();
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

	public String getNameXY(String xy) {
		return Config.get(this.key + xy);
	}

	public String getParameterNameXY(Parameter p, String xy) {
		if (p.getKey().equals("NODES")) {
			if (xy.endsWith("_LONG")) {
				return "Nodes";
			} else if (xy.endsWith("_SHORT")) {
				return "N";
			} else {
				return "Nodes";
			}
		}
		if (p.getKey().equals("TIMES")) {
			if (xy.endsWith("_LONG")) {
				return "Times";
			} else if (xy.endsWith("_SHORT")) {
				return "T";
			} else {
				return "Times";
			}
		}
		return Config.get(this.key + "_" + p.getKey() + xy);
	}
}

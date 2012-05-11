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
 * DescriptionWrapper.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-05 : v1 (BS)
 *
 */
package gtna.networks.util;

import gtna.graph.Graph;
import gtna.networks.Network;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class DescriptionWrapper extends Network {

	private Network nw;

	private String description;

	public DescriptionWrapper(Network nw, String description) {
		this(nw, description, new Parameter[0]);
	}

	public DescriptionWrapper(Network nw, String description,
			Parameter parameter) {
		this(nw, description, new Parameter[] { parameter });
	}

	public DescriptionWrapper(Network nw, String description,
			Parameter[] parameters) {
		super(nw.getKey(), nw.getNodes(), parameters, nw.getTransformations());
		this.nw = nw;
		this.description = description;
	}

	@Override
	public Graph generate() {
		return this.nw.generate();
	}

	public String getFolderName() {
		return this.nw.getFolderName();
	}

	public String getDescription(String keyX) {
		return this.description;
	}

	public String getDescriptionLong(String keyX) {
		return this.description;
	}

	public String getDescriptionShort(String keyX) {
		return this.description;
	}

	public Parameter getDiffParameter(ParameterList pl2) {
		if (super.getParameters().length > 0) {
			return super.getDiffParameter(pl2);
		} else {
			return this.nw.getDiffParameter(pl2);
		}
	}

	public String getDiffParameterNameXY(ParameterList pl2, String xy) {
		if (super.getParameters().length > 0) {
			return super.getDiffParameterNameXY(pl2, xy);
		} else {
			return this.nw.getDiffParameterNameXY(pl2, xy);
		}
	}

	public Parameter[] getParameters() {
		if (super.getParameters().length > 0) {
			return this.parameters;
		} else {
			return this.nw.getParameters();
		}
	}

}

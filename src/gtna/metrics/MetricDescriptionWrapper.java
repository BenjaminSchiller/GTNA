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
 * MetricDescriptionWrapper.java
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
package gtna.metrics;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class MetricDescriptionWrapper extends Metric {

	private Metric metric;

	private String description;

	private int run;

	public MetricDescriptionWrapper(Metric metric, String description) {
		this(metric, description, -1, new Parameter[0]);
	}

	public MetricDescriptionWrapper(Metric metric, String description,
			Parameter parameter) {
		this(metric, description, -1, new Parameter[] { parameter });
	}

	public MetricDescriptionWrapper(Metric metric, String description,
			Parameter[] parameters) {
		this(metric, description, -1, parameters);
	}

	public MetricDescriptionWrapper(Metric metric, String description, int run) {
		this(metric, description, run, new Parameter[0]);
	}

	public MetricDescriptionWrapper(Metric metric, String description, int run,
			Parameter parameter) {
		this(metric, description, run, new Parameter[] { parameter });
	}

	public MetricDescriptionWrapper(Metric metric, String description, int run,
			Parameter[] parameters) {
		super(metric.getKey(), parameters);
		this.metric = metric;
		this.description = description;
		this.run = run;
	}

	public String getFolder() {
		return this.getFolderName() + Config.get("FILESYSTEM_FOLDER_DELIMITER");
	}

	public String getFolderName() {
		if (this.run > -1) {
			return this.metric.getFolderName() + "-" + this.run;
		} else {
			return this.metric.getFolderName();
		}
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
			return this.metric.getDiffParameter(pl2);
		}
	}

	public String getDiffParameterNameXY(ParameterList pl2, String xy) {
		if (super.getParameters().length > 0) {
			return super.getDiffParameterNameXY(pl2, xy);
		} else {
			return this.metric.getDiffParameterNameXY(pl2, xy);
		}
	}

	public Parameter[] getParameters() {
		if (super.getParameters().length > 0) {
			return this.parameters;
		} else {
			return this.metric.getParameters();
		}
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.metric.computeData(g, n, m);
	}

	@Override
	public boolean writeData(String folder) {
		return this.metric.writeData(folder);
	}

	@Override
	public Single[] getSingles() {
		return this.metric.getSingles();
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return this.metric.applicable(g, n, m);
	}

}

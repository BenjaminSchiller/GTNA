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
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class DescriptionWrapper implements Network {

	private Network nw;

	private String description;

	public DescriptionWrapper(Network nw, String description) {
		this.nw = nw;
		this.description = description;
	}

	public String compareName(Network nw, String key) {
		return this.nw.compareName(nw, key);
	}

	public String compareName(Network nw) {
		return this.nw.compareName(nw);
	}

	public String compareNameLong(Network nw) {
		return this.nw.compareNameLong(nw);
	}

	public String compareNameShort(Network nw) {
		return this.nw.compareNameShort(nw);
	}

	public String compareValue(Network nw) {
		return this.nw.compareValue(nw);
	}

	public String[] configKeys() {
		return this.nw.configKeys();
	}

	public String[] configValues() {
		return this.nw.configValues();
	}

	public String description() {
		return this.description;
	}

	public String description(Network compare) {
		return this.description;
		// return this.nw.description(compare);
	}

	public String description(Network compare1, Network compare2) {
		return this.description;
		// return this.nw.description(compare1, compare2);
	}

	public String description(String key) {
		return this.description;
	}

	public String description(String key, Network compare) {
		return this.description;
		// return this.nw.description(key, compare);
	}

	public String description(String key, Network compare1, Network compare2) {
		return this.description;
		// return this.nw.description(key, compare1, compare2);
	}

	public int edges() {
		return this.nw.edges();
	}

	public String folder() {
		return this.nw.folder();
	}

	public Graph generate() {
		return this.nw.generate();
	}

	public double getConnectivity() {
		return this.nw.getConnectivity();
	}

	public double getRoutingSuccess() {
		return this.nw.getRoutingSuccess();
	}

	public boolean isConnected() {
		return this.nw.isConnected();
	}

	public boolean isRoutingFailure() {
		return this.nw.isRoutingFailure();
	}

	public String key() {
		return this.nw.key();
	}

	public String name() {
		return this.nw.name();
	}

	public int nodes() {
		return this.nw.nodes();
	}

	public RoutingAlgorithm routingAlgorithm() {
		return this.nw.routingAlgorithm();
	}

	public void setConnected(boolean connected) {
		this.nw.setConnected(connected);
	}

	public void setConnectivity(double connectivity) {
		this.nw.setConnectivity(connectivity);
	}

	public void setEdges(int edges) {
		this.nw.setEdges(edges);
	}

	public void setNodes(int nodes) {
		this.nw.setNodes(nodes);
	}

	public void setRoutingFailure(boolean routingFailure) {
		this.nw.setRoutingFailure(routingFailure);
	}

	public void setRoutingSuccess(double routingSuccess) {
		this.nw.setRoutingSuccess(routingSuccess);
	}

	public Transformation[] transformations() {
		return this.nw.transformations();
	}

}

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
 * WiFiNetwork.java
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
package gtna.projects.placement;

import gtna.networks.Network;
import gtna.networks.etc.GpsNetwork;
import gtna.transformation.Transformation;

/**
 * @author benni
 * 
 */
public class WiFiNetwork {
	private String name;

	private int nodes;

	private double maxWidth;

	private double maxHeight;

	private double[] radii;

	public WiFiNetwork(String name, int nodes, double maxWidth,
			double maxHeight, double[] radii) {
		this.name = name;
		this.nodes = nodes;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		if (radii.length > 0) {
			this.radii = radii;
		} else {
			double r = Math.sqrt(this.maxWidth * this.maxHeight
					/ (double) this.nodes);
			double start = 0.7;
			double end = 2.0;
			this.radii = new double[(int) ((end - start) / 0.1)];
			for (int i = 0; i < this.radii.length; i++) {
				this.radii[i] = r * (start + ((double) i * 0.1));
			}
		}
	}

	public Network[] getNetworks(Transformation[] t) {
		Network[] nw = new Network[this.radii.length];
		// for (int i = 0; i < nw.length; i++) {
		// nw[i] = new GpsNetwork(this.getSrc(), this.name, this.radii[i],
		// this.maxWidth, this.maxHeight, t);
		// }
		return nw;
	}

	public String getSrc() {
		return "./resources/wifi/" + this.name + ".txt";
	}

	public String getPlot1(Network nw) {
		return "./plots/placement/wifi/" + nw.getFolderName() + ".pdf";
	}

	public String getPlot2(Network nw) {
		return "./plots/placement/wifi/" + nw.getFolderName() + ".jpg";
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the nodes
	 */
	public int getNodes() {
		return this.nodes;
	}

	/**
	 * @return the maxWidth
	 */
	public double getMaxWidth() {
		return this.maxWidth;
	}

	/**
	 * @return the maxHeight
	 */
	public double getMaxHeight() {
		return this.maxHeight;
	}

	/**
	 * @return the radii
	 */
	public double[] getRadii() {
		return this.radii;
	}
}
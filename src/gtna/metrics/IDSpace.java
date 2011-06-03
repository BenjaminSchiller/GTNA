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
 * IDSpace.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-07-01 : v1 (BS)
 *
 */
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.routing.node.RingNode;

import java.util.Arrays;
import java.util.Hashtable;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class IDSpace extends MetricImpl implements Metric {
	private double[][] circle;

	private double[][] line;

	public IDSpace() {
		super("ID_SPACE");
	}

	private void initEmpty() {
		this.circle = new double[][] { new double[] { 0.0, 0.00 } };
		this.line = new double[][] { new double[] { 0.0, 0.00 } };
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		if (!(g.nodes[0] instanceof RingNode)) {
			this.initEmpty();
		}
		this.circle = this.computeCircle(g);
		this.line = this.computeLine(g);
	}

	private double[][] computeCircle(Graph g) {
		// double offsetX = Config.getDouble("ID_SPACE_CIRCLE_OFFSET_X");
		// double offsetY = Config.getDouble("ID_SPACE_CIRCLE_OFFSET_Y");
		double[][] circle = new double[g.nodes.length][2];
		double[] ids = new double[g.nodes.length];
		for (int i = 0; i < g.nodes.length; i++) {
			ids[i] = ((RingNode) g.nodes[i]).getID().pos;
		}
		Arrays.sort(ids);
		for (int i = 0; i < ids.length; i++) {
			// double x = Math.sin(ids[i] * 2 * Math.PI) + offsetX;
			// double y = Math.cos(ids[i] * 2 * Math.PI) + offsetY;
			double x = Math.sin(ids[i] * 2 * Math.PI);
			double y = Math.cos(ids[i] * 2 * Math.PI);
			circle[i] = new double[] { x, y };
		}
		return circle;
	}

	private double[][] computeLine(Graph g) {
		// double offsetX = Config.getDouble("ID_SPACE_LINE_OFFSET_X");
		// double offsetY = Config.getDouble("ID_SPACE_LINE_OFFSET_Y");
		double[][] circle = new double[g.nodes.length][2];
		double[] ids = new double[g.nodes.length];
		for (int i = 0; i < g.nodes.length; i++) {
			ids[i] = ((RingNode) g.nodes[i]).getID().pos;
		}
		Arrays.sort(ids);
		for (int i = 0; i < ids.length; i++) {
			// double x = ids[i] + offsetX;
			// double y = ids[i] + offsetY;
			double x = ids[i];
			double y = ids[i];
			circle[i] = new double[] { x, y };
		}
		return circle;
	}

	public Value[] getValues(Value[] values) {
		return new Value[] {};
	}

	public boolean writeData(String folder) {
		DataWriter.writeWithoutIndex(this.circle, "ID_SPACE_CIRCLE", folder);
		DataWriter.writeWithoutIndex(this.line, "ID_SPACE_LINE", folder);
		return true;
	}

}
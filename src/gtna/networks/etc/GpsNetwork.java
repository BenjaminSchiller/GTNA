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
 * GpsNetwork.java
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
package gtna.networks.etc;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.io.Filereader;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.transformation.connectors.UnitDiscGraph;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class GpsNetwork extends Network {
	private String filename;

	private double radius;

	public GpsNetwork(String filename, String name, double radius,
			Transformation[] t) {
		super("GPS_NETWORK", GpsNetwork.getNodes(filename), new Parameter[] {
				new StringParameter("NAME", name),
				new DoubleParameter("RADIUS", radius) }, t);
		this.filename = filename;
		this.radius = radius;
	}

	private static int getNodes(String filename) {
		int lines = 0;
		Filereader fr = new Filereader(filename);
		while (fr.readLine() != null) {
			lines++;
		}
		return lines;
	}

	@Override
	public Graph generate() {
		Filereader fr = new Filereader(filename);
		String line = null;
		ArrayList<String> lines = new ArrayList<String>();
		while ((line = fr.readLine()) != null) {
			String[] temp = line.trim().split(" ");
			double x = Double.parseDouble(temp[0]);
			double y = Double.parseDouble(temp[temp.length - 1]);
			if (x != 0.0 && y != 0.0) {
				lines.add(line);
			}
		}
		double[][] coords = new double[lines.size()][2];
		for (int i = 0; i < lines.size(); i++) {
			String[] temp = lines.get(i).trim().split(" ");
			double x = Double.parseDouble(temp[0]);
			double y = Double.parseDouble(temp[temp.length - 1]);
			coords[i] = new double[] { x, y };

			// double phi = Double.parseDouble(temp[0]);
			// double lambda = Double.parseDouble(temp[temp.length - 1]);
			// double X = (0.0) * Math.cos(phi) * Math.cos(lambda);
			// double Y = (0.0) * Math.cos(phi) * Math.sin(lambda);
		}
		this.add(coords, 0, -1 * this.min(coords, 0));
		this.add(coords, 1, -1 * this.min(coords, 1));
		this.mult(coords, 0, 999.0 / this.max(coords, 0));
		this.mult(coords, 1, 999.0 / this.max(coords, 1));
		int N = coords.length;
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(N, graph);
		graph.setNodes(nodes);
		double modulusX = Math.ceil(this.max(coords, 0)) + 1;
		double modulusY = Math.ceil(this.max(coords, 1)) + 1;
		boolean wrapAround = false;
		PlanePartitionSimple[] partitions = new PlanePartitionSimple[graph
				.getNodes().length];
		PlaneIdentifierSpaceSimple idSpace = new PlaneIdentifierSpaceSimple(
				partitions, modulusX, modulusY, wrapAround);
		for (int i = 0; i < partitions.length; i++) {
			double x = coords[i][0];
			double y = coords[i][1];
			PlaneIdentifier id = new PlaneIdentifier(x, y, modulusX, modulusY,
					wrapAround);
			partitions[i] = new PlanePartitionSimple(id);
		}
		graph.addProperty(graph.getNextKey("ID_SPACE"), idSpace);
		graph.setNodes(nodes);
		UnitDiscGraph udg = new UnitDiscGraph(radius);
		graph = udg.transform(graph);
		return graph;
	}

	private double min(double[][] values, int index) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (values[i][index] < min) {
				min = values[i][index];
			}
		}
		return min;
	}

	private double max(double[][] values, int index) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (values[i][index] > max) {
				max = values[i][index];
			}
		}
		return max;
	}

	private void add(double[][] values, int index, double value) {
		for (int i = 0; i < values.length; i++) {
			values[i][index] += value;
		}
	}

	private void mult(double[][] values, int index, double value) {
		for (int i = 0; i < values.length; i++) {
			values[i][index] *= value;
		}
	}

}

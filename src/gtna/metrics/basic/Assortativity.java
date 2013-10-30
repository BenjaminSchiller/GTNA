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
 * Assortativity.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.basic;

import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Tim
 * 
 * 
 *  Calculate the Assortativity Coefficient according to
 *   Assortative Mixing in Networks (M.E.J. Newman, 2002, Eq 4)
 */
public class Assortativity extends Metric {

	public final static int NODE_NODE = 0;
	public final static int IN_IN = 1;
	public final static int IN_OUT = 2;
	public final static int OUT_IN = 3;
	public final static int OUT_OUT = 4;

	private int type;

	/**
	 * assortativitiy coefficient
	 */
	private double r;
	private int nodes;
	private int edges;

	/**
	 * @param key
	 */
	public Assortativity() {
		super("ASSORTATIVITY");
		this.type = NODE_NODE;
	}

	/**
	 * @param key
	 * @param parameters
	 */
	public Assortativity(int t) {
		super("ASSORTATIVITY", new Parameter[] { new IntParameter("TYPE", t) });
		this.type = t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		
		this.nodes = g.getNodeCount();
		this.edges = g.getEdges().size();
		
		r = 0.0;

		ArrayList<Edge> edges = g.getEdges().getEdges();
		int dSource = 0;				// Degree of the Source
		int dDestination = 0;			// Degree of the Destination

		double sumk1k2mult = 0.0; 		// Sum{dSource*dDestination}
		double sumk1k2add = 0.0; 		// Sum{dSource+dDestination}
		double sumk1k2squareadd = 0.0; 	// Sum{dSource^2+dDestination^2}
		double M = edges.size();		// Number of Edges

		for (Edge e : edges) {
			dSource = this.getSourceDegree(g.getNode(e.getSrc()));
			dDestination = this.getDestinationDegree(g.getNode(e.getDst()));
			sumk1k2mult += (dSource * dDestination);
			sumk1k2add += dSource + dDestination;
			sumk1k2squareadd += (dSource * dSource) + (dDestination * dDestination);
		}

		double numerator = (1 / M * sumk1k2mult)
				- (Math.pow(1 / M * 0.5 * sumk1k2add, 2));
		double denominator = (1 / M * 0.5 * sumk1k2squareadd)
				- (Math.pow(1 / M * 0.5 * sumk1k2add, 2));

		this.r = numerator / denominator;
	}

	private int getSourceDegree(Node src) {
		switch (type) {
		case NODE_NODE:
			return src.getDegree();
		case IN_IN:
			return src.getInDegree();
		case IN_OUT:
			return src.getInDegree();
		case OUT_IN:
			return src.getOutDegree();
		case OUT_OUT:
			return src.getOutDegree();
		default:
			return src.getDegree();
		}
	}

	private int getDestinationDegree(Node dst) {
		switch (type) {
		case NODE_NODE:
			return dst.getDegree();
		case IN_IN:
			return dst.getInDegree();
		case IN_OUT:
			return dst.getOutDegree();
		case OUT_IN:
			return dst.getInDegree();
		case OUT_OUT:
			return dst.getOutDegree();
		default:
			return dst.getDegree();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;

		// success &= DataWriter.writeWithIndex(new double[]{this.r},
		// "ASSORTATIVITY_ASSORTATIVITY_COEFFCIENT", folder);

		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single assortativityCoefficient = new Single(
				"ASSORTATIVITY_ASSORTATIVITY_COEFFICIENT", this.r);
		Single nodes = new Single("NODES", this.nodes);
		Single edges = new Single("EDGES", this.edges);

		return new Single[] { assortativityCoefficient };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

}

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
 * FruchtermanReingold.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.gd;

import gtna.drawing.GraphPlotter;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.MDVector;
import gtna.util.Util;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleArrayParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Nico
 * 
 */
public class FruchtermanReingold extends ForceDrivenAbstract {
	/*
	 * How many iterations should the algorithm run?
	 */
	private int iterations = 100;

	/*
	 * Maximal space for the drawing, as defined per the moduli
	 */
	private double space;

	/*
	 * Optimal distance, named k by FR
	 */
	private double k;

	/*
	 * A factor to increase the attraction force and the cooling factor which is
	 * applied onto the temperature in each step
	 */
	private final double attractionFactor = 1.0;
	private final double coolingFactor = 0.95;

	/*
	 * Global cooling temperature
	 */
	private double t;

	private Edge[] edgeList;

	public FruchtermanReingold(int realities, double[] moduli,
			Boolean wrapAround, int iterations, GraphPlotter plotter) {
		super("GDA_FRUCHTERMAN_REINGOLD", new Parameter[] {
				new IntParameter("REALITIES", realities),
				new DoubleArrayParameter("MODULI", moduli),
				new BooleanParameter("WRAPAROUND", wrapAround) });
		this.realities = realities;
		this.moduli = moduli;
		this.wrapAround = wrapAround;
		this.graphPlotter = plotter;
		this.iterations = iterations;
	}

	public GraphDrawingAbstract clone() {
		return new FruchtermanReingold(iterations, moduli, wrapAround,
				iterations, graphPlotter);
	}

	@Override
	public Graph transform(Graph g) {
		initIDSpace(g);

		double[] moduli = this.idSpace.getModulus();
		this.space = 1;
		for (double singleModulus : moduli)
			this.space = this.space * singleModulus;
		k = Math.pow(this.space / this.partitions.length, 1.0 / moduli.length);
		// System.out.println("Best distance: " + k);

		this.t = Util.max(idSpace.getModulus());
		edgeList = g.generateEdges();

		for (int i = 0; i < this.iterations; i++) {
			// System.out.println("\n\n   >>> in iteration " + i + " <<<");
			if (graphPlotter != null)
				graphPlotter.plotIteration(g, idSpace, i);
			g = this.doIteration(g);
		}
		if (graphPlotter != null)
			graphPlotter.plotFinalGraph(g, idSpace);
		writeIDSpace(g);
		return g;
	}

	private Graph doIteration(Graph g) {
		MDVector delta, currDisp;

		/*
		 * Displacement array which will hold the displacement throughout one
		 * single iteration
		 */
		MDVector[] disp = new MDVector[this.partitions.length];

		// First step: repulsive forces
		for (Node v : g.getNodes()) {
			if (v == null)
				continue;

			// Reset displacement
			disp[v.getIndex()] = new MDVector(idSpace.getModulus().length, 0d);

			// Calculate repulsive forces to *all* other nodes
			for (Node u : g.getNodes()) {
				if (u == null)
					continue;
				if (u.getIndex() == v.getIndex())
					continue;
				delta = getCoordinate(v);
				delta.subtract(getCoordinate(u));
				currDisp = new MDVector(delta.getDimension(),
						delta.getCoordinates());
				double currDispNorm = currDisp.getNorm();
				if (Double.isNaN(currDispNorm))
					throw new GDTransformationException("You broke it");
				currDisp.divideBy(currDispNorm);
				currDisp.multiplyWith(fr(currDispNorm));
				disp[v.getIndex()].add(currDisp);
			}
		}

		// Second step: attractive forces
		for (Edge e : edgeList) {
			if (e == null)
				continue;

			delta = getCoordinate(e.getSrc());
			delta.subtract(getCoordinate(e.getDst()));
			currDisp = new MDVector(delta.getDimension(),
					delta.getCoordinates());
			double currDispNorm = currDisp.getNorm();
			if (Double.isNaN(currDispNorm) || currDispNorm == 0)
				continue;
			currDisp.divideBy(currDispNorm);
			currDisp.multiplyWith(fa(currDispNorm));

			disp[e.getSrc()].subtract(currDisp);
			disp[e.getDst()].add(currDisp);
		}

		// Last but not least: assign new coordinates
		for (Node v : g.getNodes()) {
			MDVector vVector = getCoordinate(v);

			currDisp = new MDVector(disp[v.getIndex()].getDimension(),
					disp[v.getIndex()].getCoordinates());
			double currDispNorm = currDisp.getNorm();
			currDisp.divideBy(currDispNorm);
			currDisp.multiplyWith(Math.min(currDispNorm, t));
			// System.out.println("Move " + vVector + " by " + currDisp +
			// " (calculated disp: " + disp[v.getIndex()] + ", t: " + t + ")" );
			// System.out.println("Old pos: " + vVector );

			vVector.add(currDisp);
			vVector = setNormalized(vVector);
			setCoordinate(v, vVector);

			// System.out.println("New pos: " + vVector + " for " +
			// v.getIndex());
		}

		t = cool(t);
		return g;
	}

	private double fr(Double x) {
		return ((k * k) / x);
	}

	private double fa(Double x) {
		return attractionFactor * ((x * x) / k);
	}

	private double cool(Double t) {
		return coolingFactor * t;
	}
}

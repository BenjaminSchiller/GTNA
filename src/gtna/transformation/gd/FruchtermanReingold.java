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

import java.util.Arrays;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.plot.GraphPlotter;
import gtna.util.MDVector;
import gtna.util.Util;

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
		 * A factor to increase the attraction force and
		 * the cooling factor which is applied onto the temperature
		 * in each step
		 */	
	private double attractionFactor = 1.0;
	private double coolingFactor = 0.95;
	
		/*
		 * Global cooling temperature
		 */
	private double t;
	
	public FruchtermanReingold(int realities, double[] moduli, Boolean wrapAround, int iterations, GraphPlotter plotter) {
		super("GDA_FRUCHTERMAN_REINGOLD", new String[]{"REALITIES", "MODULI", "WRAPAROUND", "ITERATIONS"}, new String[]{"" + realities, Arrays.toString(moduli), "" + wrapAround, "" + iterations});
		this.realities = realities;
		this.moduli = moduli;
		this.wrapAround = wrapAround;
		this.graphPlotter = plotter;
		this.iterations = iterations;
	}
	
	@Override
	public Graph transform(Graph g) {
		initIDSpace(g);

		double[] moduli = this.idSpace.getModuli();
		this.space = 1;
		for ( double singleModulus: moduli ) this.space = this.space * singleModulus;
		k = Math.pow( this.space / this.partitions.length, 1.0 / moduli.length );
//		System.out.println("Best distance: " + k);
		
		this.t = idSpace.getMaxModulus();		

		for ( int i = 0; i < this.iterations; i++ ) {
//			System.out.println("\n\n   >>> in iteration " + i + " <<<");
			if (graphPlotter != null)
				graphPlotter.plotIteration(g, idSpace, i);
			g = this.doIteration ( g );
		}
		if (graphPlotter != null)
			graphPlotter.plotFinalGraph(g, idSpace);
		writeIDSpace(g);
		return g;
	}
	
	private Graph doIteration(Graph g) {
		MDVector delta, currDisp;
		
			/*
			 * Displacement array which will hold the displacement
			 * throughout one single iteration
			 */		
		MDVector[] disp = new MDVector[this.partitions.length];
		
		// First step: repulsive forces
		for ( Node v: g.getNodes() ) {
			if ( v == null ) continue;
			
				// Reset displacement
			disp[v.getIndex()] = new MDVector(idSpace.getDimensions(), 0d);
			
				// Calculate repulsive forces to *all* other nodes
			for ( Node u: g.getNodes() ) {
				if ( u == null ) continue;
				if ( u.getIndex() == v.getIndex() ) continue;
				delta = getCoordinate(v);
				delta.subtract ( getCoordinate(u) );
				currDisp = new MDVector(delta.getDimension(), delta.getCoordinates());
				double currDispNorm = currDisp.getNorm(); 
				if ( Double.isNaN(currDispNorm) ) throw new RuntimeException("You broke it");
				currDisp.divideBy(currDispNorm);
				currDisp.multiplyWith(fr ( currDispNorm ) );
				disp[v.getIndex()].add(currDisp);
			}
		}

		// Second step: attractive forces
		for ( Edge e: g.generateEdges() ) {
			if ( e == null ) continue;
			
			delta = getCoordinate( e.getSrc() );
			delta.subtract ( getCoordinate( e.getDst()) );
			currDisp = new MDVector(delta.getDimension(), delta.getCoordinates());
			double currDispNorm = currDisp.getNorm(); 
			if ( Double.isNaN(currDispNorm) || currDispNorm == 0 ) continue;
			currDisp.divideBy(currDispNorm);
			currDisp.multiplyWith(fa ( currDispNorm ) );

			disp[e.getSrc()].subtract(currDisp);
			disp[e.getDst()].add(currDisp);
		}
		
			// Last but not least: assign new coordinates
		for ( Node v: g.getNodes() ) {
			MDVector vVector = getCoordinate(v);
			
			currDisp = new MDVector(disp[v.getIndex()].getDimension(), disp[v.getIndex()].getCoordinates());
			double currDispNorm = currDisp.getNorm(); 
			currDisp.divideBy(currDispNorm);
			currDisp.multiplyWith( Math.min(currDispNorm, t) );
//			System.out.println("Move " + vVector + " by " + currDisp + " (calculated disp: " + disp[v.getIndex()] + ", t: " + t + ")" );
//			System.out.println("Old pos: " + vVector );

			vVector.add(currDisp);
			vVector = setNormalized ( vVector );
			setCoordinate(v, vVector);
			
//			System.out.println("New pos: " + vVector + " for " + v.getIndex());
		}
		
		t = cool ( t );	
		return g;
	}
	
	private double fr ( Double x ) {
		return ( ( k * k ) / x );
	}
	
	private double fa ( Double x ) {
		return attractionFactor * ( ( x * x ) / k );
	}
	
	private double cool ( Double t ) {
		return coolingFactor * t;
	}
}

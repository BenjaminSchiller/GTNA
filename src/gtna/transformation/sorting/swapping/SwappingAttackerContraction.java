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
 * SwappingAttackerContraction.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.sorting.swapping;

import gtna.graph.NodeImpl;
import gtna.transformation.sorting.SortingNode;

import java.util.Random;
import java.util.Vector;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class SwappingAttackerContraction extends SwappingNode {
	//length of random walk;
	private int length;
	Vector<Boolean> report;
	private static final int m = 50;
	private static final double per = 0.5;
	private int count = 0;
	int neighborIndex = -1;

	public SwappingAttackerContraction(int index, double pos, Swapping swapping) {
		super(index, pos, swapping);
		length = 2;
		report = new Vector<Boolean>();
	}
	
	/**
	 * try to distribute ID of a neighbor
	 */
	public void turn(Random rand) {
		//System.out.println("performing turn @ SwappingAttackerContraction " + this.toString());
		
		
		//in first turn => choose neighbor
		if (neighborIndex == -1){
			neighborIndex = rand.nextInt(this.out().length);
		}
		
		/**
		 * start random walk offering ID
		 */
		double loc = this.knownIDs[neighborIndex] + rand.nextDouble()*SwappingNode.epsilon;
		if (loc > 1){
			loc--;
		}
		double[] locs = new double[this.out().length];
		for (int i = 0; i < locs.length; i++){
			locs[i] = loc +0.5 + rand.nextDouble()*SwappingNode.epsilon;
			if (locs[i] > 1){
				locs[i]--;
			}
		}
		
		/**
		 * check if ID is accepted
		 * eventually increase random walk length
		 */
		double res = ((SwappingNode)this.out()[neighborIndex]).swap(loc, locs, length, rand);
		if (res == SwappingNode.NO_SWAP || Math.abs(res-loc)<2*SwappingNode.epsilon){
			report.add(false);
		} else {
			report.add(true);
		}
		if (report.size() >= m && length < 6){
			int countPos = 0;
			for (int i = 0; i < report.size(); i++){
				if (report.get(i)){
					countPos++;
				}
			}
			double q = (double)countPos/report.size();
			if (q < per){
				report = new Vector<Boolean>();
				length++;
			} else {
				report.remove(0);
			}
		}
	}

	/**
	 * return id close to neighbor
	 */
	protected double ask(SwappingNode caller, Random rand) {
		//in first turn => choose neighbor
		if (neighborIndex == -1){
			neighborIndex = rand.nextInt(this.out().length);
		}
		
		double loc = this.knownIDs[neighborIndex] + rand.nextDouble()*SwappingNode.epsilon;
		if (loc > 1){
			loc--;
		}
		return loc;
	}

	/**
	 * return ID close to neighbor
	 */
	protected double swap(double callerID, double[] callerNeighborIDs, int ttl,
			Random rand) {
		// TODO implement
		//in first turn => choose neighbor
		return this.ask(this, rand);
	}
	
	

}

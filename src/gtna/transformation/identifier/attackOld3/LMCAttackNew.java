/*
 * ===========================================================
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
 * LMCAttackNew.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Stefanie Roos;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-13 : v1 (SR)
 */

package gtna.transformation.identifier.attackOld3;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.routing.node.RingNode;
import gtna.routing.node.identifier.RingID;
import gtna.transformation.TransformationImpl;

import java.util.HashMap;
import java.util.Random;

public abstract class LMCAttackNew extends TransformationImpl {
	// public static int interval = 1;
	// public static int intervalAccept = 100;
	int it = 0;
	// NodeImpl[] curNodes;
	int count;
	int maxit;
	double prandom;
	double minDist;
	public double[] acceptanceRate;
	public double[] distanceRate;
	public double[] acceptanceRateNode;
	public double[] distanceRateNode;
	public double[] distanceRatePur;
	public double[] distanceRatePurNode;
	/*
	 * modi 1: accept any distance & include into calculations 2: do not accept
	 * a new location with a neighbor in distance 1/n 3: do accept all
	 * distances, but do not include distances < 1/ n in calculations
	 */
	int modus;
	int C;
	boolean useDeg1;
	String myFile;
	HashMap<Integer, HashMap<Integer, RingID>> last;

	public LMCAttackNew(String name, int maxIter, int mode, double pRand,
			double delta, int C, boolean deg1, String file, int num) {
		super(name, new String[] { "MAXITER", "MODE", "PRANDOM", "DELTA",
				"DEG1", "NUM" }, new String[] { "" + maxIter, "" + mode,
				"" + pRand, "" + delta, "" + deg1, "" + num });
		maxit = maxIter;
		modus = mode;
		pRand = prandom;
		minDist = delta;
		this.C = C;
		useDeg1 = deg1;
		acceptanceRate = new double[maxIter];
		myFile = file;
		/*
		 * allAccepts = new Vector<double[][]>(); allDists = new
		 * Vector<double[][]>(); allAcceptsNodes = new Vector<int[][]>();
		 * allDistsNodes = new Vector<int[][]>();
		 */
	}

	public boolean applicable(Graph g) {
		return g.nodes[0] instanceof RingNode;
	}

	@Override
	public Graph transform(Graph g) {

		if (this.minDist <= 0) {
			this.minDist = (double) 1 / (double) (g.nodes.length);
		}
		NodeImpl[] curNodes = g.nodes;
		// Random raN = new Random();
		// for (int i = 0; i < curNodes.length; i++) {
		// ((RingNode) curNodes[i]).getID().pos = raN.nextDouble();
		// }
		count = 0;
		RingNode cur, neighbor;
		Random rand = new Random();
		int n = curNodes.length;
		int accept = 0;
		int pos = 0;
		/*
		 * distances = new Vector<Vector<Double>>(); acceptances = new
		 * Vector<Vector<Double>>();
		 */
		int[] countsAll = new int[n];
		acceptanceRate = new double[maxit];
		distanceRate = new double[maxit];
		distanceRatePur = new double[maxit];
		acceptanceRateNode = new double[n];
		distanceRateNode = new double[n];
		distanceRatePurNode = new double[n];
		/*
		 * for (int i= 0; i < n; i++){ distances.add(new Vector<Double>());
		 * acceptances.add(new Vector<Double>()); }
		 */
		determineAttacker(curNodes, rand);
		while (count < this.maxit * curNodes.length) {
			cur = (RingNode) curNodes[rand.nextInt(n)];

			countsAll[cur.index()]++;
			if (cur.out().length == 1 && !useDeg1) {
				continue;
			}
			count++;
			if (isAttacker(cur)) {
				continue;
			}

			// determine new ID
			int sign;
			// if (rand.nextBoolean() || cur.myId instanceof AntiCircleID) {
			if (rand.nextBoolean()) {
				sign = 1;
			} else {
				sign = -1;
			}

			double loc;
			neighbor = (RingNode) cur.out()[rand.nextInt(cur.out().length)];
			if (rand.nextDouble() < prandom) {
				loc = neighbor.getID().pos + sign * minDist + sign
						* rand.nextDouble() * C * minDist;
				while (cur.getID().pos > 1) {
					loc--;
				}
				while (cur.getID().pos < 0) {
					loc++;
				}
			} else {
				loc = rand.nextDouble();
			}
			RingID newID = new RingID(loc);

			double before = 1;
			for (int i = 0; i < cur.out().length; i++) {
				neighbor = (RingNode) cur.out()[i];
				if (!useDeg1 && neighbor.out().length == 1) {

				} else {
					if (isAttacker(neighbor)) {
						before = before
								* cur.dist(this.asked(neighbor, cur, minDist,
										rand, cur.getID(), newID));
					} else {
						before = before * cur.dist(neighbor.getID());
					}
				}
			}
			RingID oldLoc = cur.getID();
			cur.setID(newID);

			double after = 1;
			double curDist;
			for (int i = 0; i < cur.out().length; i++) {
				if (useDeg1 || cur.out()[i].out().length > 1) {
					if (isAttacker(neighbor)) {
						curDist = cur.dist(last.get(neighbor.index()).get(
								cur.index()));
					} else {
						curDist = cur.dist(((RingNode) cur.out()[i]).getID());
					}
					if (curDist < minDist) {
						switch (modus) {
						case 1:
							after = after * curDist;
							break;
						case 2:
							cur.setID(oldLoc);
							break;
						case 3:
							break;
						case 4:
							after = after * Math.max(minDist, curDist);
							break;
						default:
							throw new IllegalArgumentException("Modus unknown");
						}
					} else {
						after = after * curDist;
					}
				}
			}
			if (rand.nextDouble() > before / after) {
				cur.setID(oldLoc);
			} else {

			}

			/*
			 * if (countsD[cur.index()] == interval){
			 * distances.get(cur.index()).
			 * add((double)dists[cur.index()]/interval); countsD[cur.index()] =
			 * 0; dists[cur.index()] = 0; } if (countsAll[cur.index()] ==
			 * intervalAccept){ countsAll[cur.index()] = 0;
			 * acceptances.get(cur.index
			 * ()).add((double)countsA[cur.index()]/intervalAccept);
			 * countsA[cur.index()] = 0; }
			 */

		}
		/*
		 * if (myFile != null){ this.writeMetric(acceptances, "Accept");
		 * this.writeMetric(distances, "Dist"); it++; }
		 */

		if (!useDeg1) {
			for (int i = 0; i < curNodes.length; i++) {
				cur = (RingNode) curNodes[i];
				if (cur.out().length == 1) {
					neighbor = (RingNode) cur.out()[0];
					cur.getID().pos = neighbor.getID().pos + minDist
							* rand.nextDouble();
					while (cur.getID().pos > 1) {
						cur.getID().pos--;
					}
					while (cur.getID().pos < 0) {
						cur.getID().pos++;
					}
				}
			}
		}
		return g;
	}

	public abstract void determineAttacker(NodeImpl[] curNodes, Random rand);

	public abstract RingID asked(RingNode node, RingNode cur, double min,
			Random rand, RingID oldID, RingID newID);

	public abstract boolean isAttacker(RingNode node);
}

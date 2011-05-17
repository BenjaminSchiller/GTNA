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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * Modules.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.metrics.roles;

//rev tobi2
import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.io.GraphReader;
import gtna.metrics.Metric;
import gtna.metrics.MetricImpl;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.util.Config;
import gtna.util.Timer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;

/**
 * @deprecated
 */
public class Modules extends MetricImpl implements Metric {

	ArrayList<ArrayList<Integer>> roles;
	private static double EPSILON_MOD = 0.000001;

	@SuppressWarnings("unused")
	// initialize global variables
	private long runtime;
	FileWriter log, log2;
	FileWriter temp;
	int test = 0;
	int test2 = 0;
	int deletedLoops = 0;
	int deletedParallelEdges = 0;
	BFS bfs;

	// constructor
	public Modules() {
		super("ROLE");
	}

	public static int type = GraphReader.OWN_FORMAT;
	public static String filename = "./power.txt";

	// main method
	public void computeData(Graph g, Network nw, Hashtable<String, Metric> m) {

		// let the original graph as it is, for other metrics
		Graph graph = new ReadableFile("ROLE", filename, type, null, null).generate();
		// create bfs object
		bfs = new BFS();

		// Delete loops and parallel edges
		ClearGraph CG = new ClearGraph();
		graph = CG.clearGraph(graph);
		// graph = CG.downsizeGraph(graph, 500);

		// start the timer
		Timer timer = new Timer();

		// #########################################################
		// algorithm parameter
		char initial_sw = 'o'; // groupmode
		int ngroup = 5; // number of initial groups
		double Ti = 0; // initial temperature
		double Tf = 0.000; // finishing temperature
		int fac = Integer.valueOf(Config.get("ROLE_ITERATIONFACTOR"));
		; // iteration factor
		double Ts = Double.valueOf(Config.get("ROLE_COOLINGFACTOR")); // cooling
		// factor
		int collective_sw = 1; // collective moves: 1 = on
		char output_sw = 's';

		try {
			log = new FileWriter("log.txt");
			log2 = new FileWriter("log2.txt");
			temp = new FileWriter("TEMP.txt");
		} catch (Exception e) {

		}

		// #########################################################
		// define and initialize variables
		int nnod; // number of nodes
		int cicle1, cicle2;
		double T;
		double energy = -1.0, dE = 0.0;

		int totallinks = (graph.edges - this.deletedLoops - this.deletedParallelEdges) * 2;
		int dice;
		int count = 0, limit = 25; // to stop the search if the energy does not
		int newg, oldg;
		int innew, inold, nlink;
		int g1, g2;
		int empty;
		ArrayList<Integer> split = new ArrayList<Integer>();
		double energyant = 0.0;
		double best_E = -100.0;

		Group[] glist_best = null;
		int[] nlist_best = null;

		// #########################################################

		nnod = graph.nodes.length; // number of nodes

		int[] nlist = new int[nnod];

		// initiate nodes
		for (int i = 0; i < nnod; i++) {
			nlist[i] = -1;
		}

		Group[] glist = null;

		// Create the groups and assign nodes to the initial group according to
		// initial_sw.
		switch (initial_sw) {
		case 'o': // One node in each group
			glist = new Group[nnod];
			for (int i = 0; i < nnod; i++) {
				glist[i] = new Group(i);
				nlist = AddNodeToGroup(i, glist[i], graph, nlist);
			}
			ngroup = nnod;
			break;
		case 'r': // Random placement of nodes in groups
			glist = new Group[ngroup];
			for (int i = 0; i < ngroup; i++) {
				glist[i] = new Group(i);
			}
			for (int i = 0; i < nnod; i++) {
				dice = new Random().nextInt(ngroup);
				nlist[i] = dice;
				AddNodeToGroup(i, glist[dice], graph, nlist);
			}
			break;
		}

		// Determine the number of iterations at each temperature
		if (fac * (double) (nnod * nnod) < 10)
			cicle1 = 10;
		else
			cicle1 = (int) Math.floor(fac * (double) (nnod * nnod));

		if (fac * (double) nnod < 2)
			cicle2 = 2;
		else
			cicle2 = (int) Math.floor(fac * (double) nnod);

		// Do the simulated annealing
		Ti = 2.0 / (double) nnod; // initial temperature
		T = Ti;
		// calculate the starting energy
		energy = Modularity(glist, graph);

		/* Temperature loop */
		while (T > Tf && count < limit) {

			/* Output */
			switch (output_sw) {
			case 'n':
				break;
			case 'b':
				break;
			case 's':
				System.out.println(1.0 / T + " :: " + energy + " :: " + best_E
						+ " :: " + T);
				break;
			case 'm':
				System.out.println(1.0 / T + " :: " + energy + " :: " + T);
				break;
			case 'v':
				System.out.println(1.0 / T + " :: " + energy + " :: "
						+ Modularity(glist, graph) + " :: " + T);
				break;
			case 'd':
				System.out.println(1.0 / T + " :: " + energy + " :: "
						+ Modularity(glist, graph) + " :: " + T);
			}

			/* Do all the individual moves */
			for (int i = 0; i < cicle1; i++) {

				dice = new Random().nextInt(nnod); // look for a random node
				oldg = nlist[dice]; // group of the random node
				do {
					newg = new Random().nextInt(ngroup); // look for another
					// group
				} while (newg == oldg); // thats not the same

				/* Calculate the change of energy */
				inold = GeneralGraphMethods.NLinksToGroup(dice, glist[oldg], graph, nlist); // number
				// of
				// links
				// from
				// the
				// random
				// node
				// to
				// its
				// old
				// group
				innew = GeneralGraphMethods.NLinksToGroup(dice, glist[newg], graph, nlist); // number
				// of
				// links
				// from
				// the
				// random
				// node
				// to
				// its
				// new
				// group
				nlink = degreeOfNode(dice, graph); // degree of the random node

				dE = 0.0;
				// subtract energy of the old group with the random node
				dE -= (double) (2 * glist[oldg].getInlinks())
						/ (double) totallinks
						- (double) (glist[oldg].getTotlinks() + glist[oldg]
								.getInlinks())
						* (double) (glist[oldg].getTotlinks() + glist[oldg]
								.getInlinks())
						/ ((double) totallinks * (double) totallinks);
				// subtract energy of the new group without the random node
				dE -= (double) (2 * glist[newg].getInlinks())
						/ (double) totallinks
						- (double) (glist[newg].getTotlinks() + glist[newg]
								.getInlinks())
						* (double) (glist[newg].getTotlinks() + glist[newg]
								.getInlinks())
						/ ((double) totallinks * (double) totallinks);
				// add energy of the old group without the random node
				dE += (double) (2 * glist[oldg].getInlinks() - 2 * inold)
						/ (double) totallinks
						- (double) (glist[oldg].getTotlinks()
								+ glist[oldg].getInlinks() - nlink)
						* (double) (glist[oldg].getTotlinks()
								+ glist[oldg].getInlinks() - nlink)
						/ ((double) totallinks * (double) totallinks);
				// add energy of the new group with the random node
				dE += (double) (2 * glist[newg].getInlinks() + 2 * innew)
						/ (double) totallinks
						- (double) (glist[newg].getTotlinks()
								+ glist[newg].getInlinks() + nlink)
						* (double) (glist[newg].getTotlinks()
								+ glist[newg].getInlinks() + nlink)
						/ ((double) totallinks * (double) totallinks);

				/* Accept the change according to Metroppolis */
				if ((dE >= 0.0)
						|| (new Random().nextDouble() < Math.exp(dE / T))) { // if
					// new
					// energy
					// is
					// better
					// or
					// randomly
					nlist = MoveNode(dice, glist[oldg], glist[newg], graph,
							glist, nlist); // move node from old to new group
					energy += dE;
				}
			} /* End of individual moves */

			/* Do all the collective moves */
			if (collective_sw == 1) {
				for (int i = 0; i < cicle2; i++) {

					/* MERGE */
					dice = new Random().nextInt(nnod); // look for an random
					// group
					g1 = nlist[dice];

					if (glist[g1].getNodeList().size() < nnod) { // Unless all
						// nodes are
						// together
						do {
							dice = new Random().nextInt(nnod); // look for
							// another group
							g2 = nlist[dice]; // thats not the same one
						} while (g1 == g2);

						/* Calculate the change of energy */
						nlink = NG2GLinks(glist[g1], glist[g2], graph, nlist);// number
						// of
						// links
						// between
						// g1
						// and
						// g2

						dE = 0.0;
						//
						// subtract the energy of g1
						dE -= (double) (2 * glist[g1].getInlinks())
								/ (double) totallinks
								- ((double) (glist[g1].getTotlinks() + glist[g1]
										.getInlinks()) * (double) (glist[g1]
										.getTotlinks() + glist[g1].getInlinks()))
								/ ((double) totallinks * (double) totallinks);
						// subtract the energy of g2
						dE -= (double) (2 * glist[g2].getInlinks())
								/ (double) totallinks
								- ((double) (glist[g2].getTotlinks() + glist[g2]
										.getInlinks()) * (double) (glist[g2]
										.getTotlinks() + glist[g2].getInlinks()))
								/ ((double) totallinks * (double) totallinks);
						// add twice the energy of g1 and g2 merged
						dE += 2.0
								* (double) (glist[g1].getInlinks()
										+ glist[g2].getInlinks() + nlink)
								/ (double) totallinks
								- (double) (glist[g1].getTotlinks()
										+ glist[g1].getInlinks()
										+ glist[g2].getTotlinks() + glist[g2]
										.getInlinks())
								* (double) (glist[g1].getTotlinks()
										+ glist[g1].getInlinks()
										+ glist[g2].getTotlinks() + glist[g2]
										.getInlinks())
								/ ((double) totallinks * (double) totallinks);

						/* Accept the change according to Metroppolis */
						if ((dE >= 0.0)
								|| (new Random().nextDouble() < Math
										.exp(dE / T))) { // if the new energy is
							// better or
							// randomly
							nlist = MergeGroups(glist[g1], glist[g2], graph,
									glist, nlist); // merge g1 and g2
							energy += dE;
						}
					}

					/* SPLIT */
					dice = new Random().nextInt(nnod); /* target node */
					dice = nlist[dice]; /* target group */

					/* Look for an empty group */
					empty = -1;
					int o = 0;
					while (o < glist.length && empty < 0) {
						if (glist[o].getNodeList().isEmpty()) {
							empty = glist[o].getLabel();
						}
						o++;
					}

					if (empty >= 0) { /* if there are no empty groups, do nothing */
						/* Find a reasonable split */
						split = SAGroupSplit(glist[dice], Ti, T, 0.95, 1, graph);

						/* Split the group */
						for (int j = 0; j < split.size(); j++) {
							nlist = MoveNode(split.get(j), glist[dice],
									glist[empty], graph, glist, nlist);
						}

						/*
						 * Calculate the change of energy associated to
						 * remerging the groups
						 */
						nlink = NG2GLinks(glist[dice], glist[empty], graph,
								nlist);
						dE = 0.0;
						// subtract the energy of the target group after split
						dE -= (double) (2 * glist[dice].getInlinks())
								/ (double) totallinks
								- ((double) (glist[dice].getTotlinks() + glist[dice]
										.getInlinks()) * (double) (glist[dice]
										.getTotlinks() + glist[dice]
										.getInlinks()))
								/ ((double) totallinks * (double) totallinks);
						// subtract the energy of the empty group after split
						// (has nodes then)
						dE -= (double) (2 * glist[empty].getInlinks())
								/ (double) totallinks
								- ((double) (glist[empty].getTotlinks() + glist[empty]
										.getInlinks()) * (double) (glist[empty]
										.getTotlinks() + glist[empty]
										.getInlinks()))
								/ ((double) totallinks * (double) totallinks);
						// add twice the energy as if they wheren't split
						dE += 2.0
								* (double) (glist[dice].getInlinks()
										+ glist[empty].getInlinks() + nlink)
								/ (double) totallinks
								- (double) (glist[dice].getTotlinks()
										+ glist[dice].getInlinks()
										+ glist[empty].getTotlinks() + glist[empty]
										.getInlinks())
								* (double) (glist[dice].getTotlinks()
										+ glist[dice].getInlinks()
										+ glist[empty].getTotlinks() + glist[empty]
										.getInlinks())
								/ ((double) totallinks * (double) totallinks);

						/*
						 * Accept the change according to "inverse" Metroppolis.
						 * Inverse means that the algorithm is applied to the
						 * split and NOT to the merge!
						 */
						if ((dE > 0.0)
								&& (new Random().nextDouble() > Math.exp(-dE
										/ T))) { // if new groups are worse
							nlist = MergeGroups(glist[dice], glist[empty],
									graph, glist, nlist); // undo the split
						} else {
							energy -= dE;
						}
					} /* End of if empty >= 0 */
				} /* End of collective moves */
			} /* End of if collective_sw==1 */

			/* Update the no-change counter */
			if (Math.abs(energy - energyant) / Math.abs(energyant) < EPSILON_MOD
					|| Math.abs(energyant) < EPSILON_MOD) {
				count++;
				/*
				 * If the SA is ready to stop (count==limit) but the current
				 * partition is not the best one so far, replace the current
				 * partition by the best one and continue from there.
				 */
				if ((count == limit) && (energy + EPSILON_MOD < best_E)) {
					switch (output_sw) {
					case 'n':
						break;
					case 'b':
						break;
					default:
						System.out.println("# Resetting partition");
						break;
					}

					/* copy glist and best energy */
					glist = glist_best;
					nlist = nlist_best;

					/* Reset energy and counter */
					energy = best_E;
					count = 0;
				}
			} else {
				count = 0;
			}

			/* Update the last energy */
			energyant = energy;

			/*
			 * Compare the current partition to the best partition so far and
			 * save the current if it is better than the best so far.
			 */
			if (energy > best_E) {
				glist_best = glist;
				nlist_best = nlist;
				best_E = energy;
			}

			/* Save the partition to a file if necessary */

			switch (output_sw) {
			case 'b':
				FPrintPartition(glist_best);
				;
			case 's':
				FPrintPartition(glist_best);
			default:
				break;
			}

			/* Uptade the temperature */
			T = T * Ts;

		}/* End of simulated annealing */
		FPrintPartition(glist_best);

		// time the calculation took
		this.runtime = timer.msec();

		// debug print
		groupOutprint(glist, nlist);
		System.out
				.println("##############################################################################################");
		System.out.println("");
		System.out.println("\nModularity: " + Modularity(glist, graph) + "\n");

		// assign role to each node
		CalcRoles CR = new CalcRoles(nlist, glist, graph);
		roles = CR.getRoles();
	}

	// debug print method
	private void FPrintPartition(Group[] glistCopy) {
		try {
			for (int i = 0; i < glistCopy.length; i++) {
				temp.write(glistCopy[i].getLabel() + " - "
						+ glistCopy[i].getNodeList().size() + " - "
						+ glistCopy[i].getTotlinks() + " - "
						+ glistCopy[i].getInlinks() + " - "
						+ glistCopy[i].getOutlinks() + " .:. "
						+ glistCopy[i].getNodeList().toString() + "\n\r");
			}
		} catch (Exception e) {

		}

	}

	// Find a reasonable split
	private ArrayList<Integer> SAGroupSplit(Group group, double Ti, double Tf,
			double Ts, int cluster_sw, Graph g) {
		// initialize variables
		ArrayList<Integer> nodeList = group.getNodeList();
		ArrayList<Integer> splitList = new ArrayList<Integer>();
		int ngroups = 0;
		double prob = 0.0;
		int g1, g2;
		int totallinks = 0;
		int dice;
		double T;
		int target, oldg, newg;
		int inold, innew, nlink;
		double dE = 0.0, energy = 0.0;
		Group[] glist;
		int[] gsize;

		Node[] nodeListOfGivenGroup = new Node[nodeList.size()]; // nodelist of
		// the given
		// group
		for (int i = 0; i < nodeList.size(); i++) {
			nodeListOfGivenGroup[i] = g.nodes[nodeList.get(i)];
		}

		int[] nlist = new int[g.nodes.length]; // in which splitgroup every node
		// is
		Arrays.fill(nlist, -1);

		bfs.calculateBFS(nodeList, g); // do brights first search
		int[] scc = bfs.getSCC(); // get the strongly connected components
		ngroups = bfs.getNumberSCC(); // get the number of SSCs

		// if there is more than one SSC and random
		if (cluster_sw == 1 && ngroups > 1 && new Random().nextDouble() < prob) {

			glist = new Group[ngroups]; // create grouplist for SSCs
			for (int i = 0; i < ngroups; i++) {
				glist[i] = new Group(i);
			}
			gsize = new int[ngroups];
			Arrays.fill(gsize, 0);

			for (int i = 0; i < scc.length; i++) { // add node to its SSC
				nlist = AddNodeToGroup(nodeList.get(i), glist[scc[i]], g, nlist);
				gsize[scc[i]]++;
			}

			int notEmptyGroups = ngroups;
			// Merge groups randomly until only two are left
			while (notEmptyGroups > 2) {
				do {
					g1 = new Random().nextInt(ngroups); // look for an group g1
				} while (gsize[g1] == 0);
				do {
					g2 = new Random().nextInt(ngroups); // look for another
					// group g2
				} while (g1 == g2 && gsize[g2] == 0);

				// merge the two groups
				nlist = MergeGroups(glist[g1], glist[g2], g, glist, nlist);
				gsize[g1] = 0;
				notEmptyGroups--; // after each merge there is one less empty
				// group
			}
			Group[] glist2 = new Group[2]; // put the last two groups into
			// glist2
			boolean first = true;
			for (int i = 0; i < ngroups; i++) {
				if (gsize[i] > 0 && first) {
					glist2[0] = glist[i];
					first = false;
				}
				if (gsize[i] > 0 && !first) {
					glist2[1] = glist[i];
				}
			}
			glist = glist2;
		} else {
			// Randomly assign the nodes to the groups if there is only one SCC
			glist = new Group[2];
			glist[0] = new Group(0);
			glist[1] = new Group(1);

			for (int i = 0; i < nodeListOfGivenGroup.length; i++) {
				totallinks += degreeOfNode(nodeListOfGivenGroup[i].index(), g);
				dice = new Random().nextInt(1);
				nlist = AddNodeToGroup(nodeListOfGivenGroup[i].index(),
						glist[dice], g, nlist);
			}
			totallinks /= 2;

			// Do the SA to "optimize" the splitting
			if (totallinks > 0) {
				T = Ti;
				while (T > Tf) {

					for (int i = 0; i < nodeListOfGivenGroup.length; i++) {
						target = new Random()
								.nextInt(nodeListOfGivenGroup.length); // get a
						// random
						// node
						oldg = nlist[nodeListOfGivenGroup[target].index()]; // and
						// it's
						// group
						if (oldg == 0) // get the other group
							newg = 1;
						else
							newg = 0;

						/* Calculate the change of energy */
						inold = GeneralGraphMethods.NLinksToGroup(nodeListOfGivenGroup[target]
								.index(), glist[oldg], g, nlist); // number of
																	// links
																	// from
						// the random node to
						// it's old group
						innew = GeneralGraphMethods.NLinksToGroup(nodeListOfGivenGroup[target]
								.index(), glist[newg], g, nlist); // number of
																	// links
																	// from
						// the random node to
						// the new group
						nlink = degreeOfNode(nodeListOfGivenGroup[target]
								.index(), g); // degree
						// of
						// the
						// random
						// node

						dE = 0.0;
						// subtract the energy of the old group with the random
						// node
						dE -= (double) (2 * glist[oldg].getInlinks())
								/ (double) totallinks
								- (double) (glist[oldg].getTotlinks() + glist[oldg]
										.getInlinks())
								* (double) (glist[oldg].getTotlinks() + glist[oldg]
										.getInlinks())
								/ ((double) totallinks * (double) totallinks);
						// subtract the energy of the new group without the
						// ranodom node
						dE -= (double) (2 * glist[newg].getInlinks())
								/ (double) totallinks
								- (double) (glist[newg].getTotlinks() + glist[newg]
										.getInlinks())
								* (double) (glist[newg].getTotlinks() + glist[newg]
										.getInlinks())
								/ ((double) totallinks * (double) totallinks);
						// add the energy of the new group without the random
						// node
						dE += (double) (2 * glist[oldg].getInlinks() - 2 * inold)
								/ (double) totallinks
								- (double) (glist[oldg].getTotlinks()
										+ glist[oldg].getInlinks() - nlink)
								* (double) (glist[oldg].getTotlinks()
										+ glist[oldg].getInlinks() - nlink)
								/ ((double) totallinks * (double) totallinks);
						// add the energy of the old group with the random node
						dE += (double) (2 * glist[newg].getInlinks() + 2 * innew)
								/ (double) totallinks
								- (double) (glist[newg].getTotlinks()
										+ glist[newg].getInlinks() + nlink)
								* (double) (glist[newg].getTotlinks()
										+ glist[newg].getInlinks() + nlink)
								/ ((double) totallinks * (double) totallinks);

						/* Accept the move according to Metropolis */
						// if the energy of the new setup is better move node
						// from old to new and change energy
						if ((dE >= 0.0)
								|| (new Random().nextDouble() < Math
										.exp(dE / T))) {
							nlist = MoveNode(nodeListOfGivenGroup[target]
									.index(), glist[oldg], glist[newg], g,
									glist, nlist);
							energy += dE;
						}
					}

					T = T * Ts;
				} /* End of temperature loop */
			} /* End if totallinks > 0 */
		}
		splitList = glist[0].getNodeList();
		// return the list of nodes in the split
		return splitList;
	}

	// Move all nodes in group to group2
	private int[] MergeGroups(Group group, Group group2, Graph g,
			Group[] glist, int[] nlist) {
		ArrayList<Integer> nodeList = group.getNodeList();
		int size = nodeList.size();
		if (nodeList.size() > 0 && nodeList != null) {
			for (int i = size - 1; i >= 0; i--) {
				nlist = MoveNode(nodeList.get(i), group, group2, g, glist,
						nlist);
			}
		}
		return nlist;
	}

	// number of links between group and group2
	private int NG2GLinks(Group group, Group group2, Graph g, int[] nlist) {
		int counter = 0;
		ArrayList<Integer> nodeList = group.getNodeList();

		for (int i = 0; i < nodeList.size(); i++) {
			counter += NLinksToGroupInLinks(nodeList.get(i), group2, g, nlist);
		}

		nodeList = group2.getNodeList();

		for (int i = 0; i < nodeList.size(); i++) {
			counter += NLinksToGroupInLinks(nodeList.get(i), group, g, nlist);
		}
		return counter;
	}

	// move node from group to group2
	private int[] MoveNode(int node, Group group, Group group2, Graph g,
			Group[] glist, int[] nlist) {
		try {
			nlist = RemoveNodeFromGroup(node, group, g, nlist);
			nlist = AddNodeToGroup(node, group2, g, nlist);
		} catch (Exception e) {

		}
		return nlist;
	}

	// remove node from group
	private int[] RemoveNodeFromGroup(int node, Group group, Graph g,
			int[] nlist) {
		if (nlist[node] != group.getLabel()) {
			System.out.println("REMOVE - Node " + node + " not in group "
					+ group.getLabel());
		}

		int inLinks = GeneralGraphMethods.NLinksToGroup(node, group, g, nlist);
		int degree = degreeOfNode(node, g);

		group.setTotlinks(group.getTotlinks() - (degree - inLinks));
		group.setInlinks(group.getInlinks() - inLinks);
		group.setOutlinks(group.getTotlinks() - group.getInlinks());
		group.removeFromNodeList(node);
		nlist[node] = -1;
		return nlist;
	}

	// add node to group
	private int[] AddNodeToGroup(int node, Group group, Graph g, int[] nlist) {
		group.addToNodeList(node);
		nlist[node] = group.getLabel();
		int inLinks = GeneralGraphMethods.NLinksToGroup(node, group, g, nlist);
		int degree = degreeOfNode(node, g);

		group.setTotlinks(group.getTotlinks() + degree - inLinks);
		group.setInlinks(group.getInlinks() + inLinks);
		group.setOutlinks(group.getTotlinks() - group.getInlinks());
		return nlist;
	}

	// return the degree of given node
	public int degreeOfNode(int node, Graph g) {
		int degree = g.nodes[node].in().length + g.nodes[node].out().length;
		return degree;
	}

	// Count the number of links from a node to a given group // just inlinks
	private int NLinksToGroupInLinks(int node, Group group, Graph g, int[] nlist) {
		int counter = 0;

		Node[] out = g.nodes[node].out();
		for (int i = 0; i < out.length; i++) {
			if (nlist[out[i].index()] == group.getLabel()
					&& out[i].index() != node) {
				counter++;
			}
		}
		return counter;
	}

	// calculate the modularity of the group
	private double Modularity(Group[] glist, Graph g) {
		int links = 0;
		double modul = 0.0;
		for (Group i : glist) {
			links += i.getTotlinks() + i.getInlinks();
		}
		for (Group i : glist) {
			modul += (double) ((2 * i.getInlinks()) / (double) links)
					- (((double) (i.getInlinks() + i.getTotlinks()) * (double) (i
							.getInlinks() + i.getTotlinks())) / ((double) links * (double) links));
		}
		return modul;
	}

	@Override
	// GTNA output value
	public Value[] getValues(Value[] values) {
		Value domn = new Value("ROLE", 1);
		Value runtime = new Value("ROLE", 2);
		return new Value[] { domn, runtime };
	}

	@Override
	// GTNA output data for gnuplot
	public boolean writeData(String folder) {

		double[] role = new double[7];
		for (int i = 0; i < roles.size(); i++) {
			role[i] = (double) roles.get(i).size();
		}

		DataWriter.writeWithIndex(role, "ROLE", folder);
		// File test = new File(folder);
		try {
			FileWriter FW = new FileWriter(folder + "roles.txt");
			BufferedWriter bw = new BufferedWriter(FW);
			for (int i = 0; i < roles.size(); i++) {
				bw.write(roles.get(i).toString() + "\n");
			}
			bw.flush();
			bw.close();
			FW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// debug print method
	private void groupOutprint(Group[] glist, int[] nlist) {
		System.out
				.println("---------------------------------------------------###");
		for (int i = 0; i < glist.length; i++) {
			System.out.println(glist[i].getLabel() + " - "
					+ glist[i].getNodeList().size() + " - "
					+ glist[i].getTotlinks() + " - " + glist[i].getInlinks()
					+ " - " + glist[i].getOutlinks() + " .:. "
					+ glist[i].getNodeList().toString());
		}
		System.out
				.println("---------------------------------------------------###");
	}

	// debug print method
	private void graphOutPrint(Graph g) {
		System.out.println("");
		for (int i = 0; i < g.nodes.length; i++) {
			System.out.print("Node: " + i + " -> Inlinks: ");
			Node[] in = g.nodes[i].in();
			for (int j = 0; j < in.length; j++) {
				System.out.print(in[j].index() + ", ");
			}
			System.out.print(":: Outlinks: ");
			Node[] out = g.nodes[i].out();
			for (int j = 0; j < out.length; j++) {
				System.out.print(out[j].index() + ", ");
			}
			System.out.println("");
		}
		System.out.println("");

	}

	// debug print method
	private void Ausgabe(Group[] groups, int[] nlist, int o) {
		try {
			log2.write("" + "\n\r");
			log2
					.write(o
							+ " - "
							+ "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
							+ "\n\r");
			for (int i = 0; i < groups.length; i++) {
				log2.write(o + " - " + "Group " + i + ": "
						+ groups[i].getNodeList().toString() + "\n\r");
			}
			for (int i = 0; i < nlist.length; i++) {
				log2.write(o + " - " + "Node " + i + ": " + nlist[i] + "\n\r");
			}
			log2
					.write(o
							+ " - "
							+ "-------------------------------------------------------------"
							+ "\n\r");
		} catch (Exception e) {

		}
	}

}

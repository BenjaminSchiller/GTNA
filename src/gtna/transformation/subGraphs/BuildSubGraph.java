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
 * BuildSubGraph.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.subGraphs;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author stef
 * 
 *         build a subgraph of an existing graph starting from a clique of
 *         predefined size in case such a clique does not exist, an empty graph
 *         is returned
 */
public class BuildSubGraph extends Transformation {

	/**
	 * choice of the clique in the beginning: -clique with highest number of
	 * outgoing links: MAXOUTDEGREE -zufällige Clique der gewünschten Größe:
	 * RANDOM
	 */
	public static enum CliqueSelection {
		RANDOM, MAXOUTDEGREE
	};

	/**
	 * choice of the next node: -randomly from all nodes who have the required
	 * minimal degree: RANDOM -randomly from all nodes with highest number of
	 * links into subGraph: MAX -randomly from all nodes with lowest (exceeding
	 * minimal required degree) number of links into the subgraph: MIN
	 */
	public static enum NextSelection {
		RANDOM, MAX, MIN
	};

	int include, minDegree, maxDegree, startNodes;
	CliqueSelection selection;
	NextSelection selectionNext;

	/**
	 * 
	 * @param include
	 *            : nodes that are MAXIMALLY included in subgraph
	 * @param minDegree
	 *            : minimum number of links a node has to have to nodes that are
	 *            already included in the subgraph to be added
	 * @param maxDegree
	 *            : maximum degree a node in the subgraph is allowed to have
	 * @param startNodes
	 *            : size of the initial clique
	 * @param selection
	 *            : selection of the initial clique:
	 * @param selectionNext
	 *            : selection criteria for next node to add
	 */
	public BuildSubGraph(int include, int minDegree, int maxDegree,
			int startNodes, CliqueSelection selection,
			NextSelection selectionNext) {
		super("BUILD_SUB_GRAPH",
				new Parameter[] {
						new IntParameter("INCLUDE", include),
						new IntParameter("MINDEGREE", minDegree),
						new IntParameter("MAXDEGREE", maxDegree),
						new IntParameter("STARTNODES", startNodes),
						new StringParameter("SELECTION", selection.toString()),
						new StringParameter("SELECTION_NEXT",
								selectionNext.toString()) });
		this.selection = selection;
		this.minDegree = Math.max(minDegree, 1);
		this.maxDegree = maxDegree;
		this.include = include;
		this.startNodes = startNodes;
		this.selectionNext = selectionNext;
		if (maxDegree < startNodes - 1 || minDegree > maxDegree) {
			throw new IllegalArgumentException(
					"Degree constraints do not agree in BuildSubGraphMax "
							+ maxDegree);
		}
	}

	/**
	 * 
	 * @param include
	 *            : nodes that are MAXIMALLY included in subgraph
	 * @param minDegree
	 *            : minimum number of links a node has to have to nodes that are
	 *            already included in the subgraph to be added
	 * @param maxDegree
	 *            : maximum degree a node in the subgraph is allowed to have
	 * @param startNodes
	 *            : size of the initial clique
	 * @param selection
	 *            : selection of the initial clique: a) RANDOM: choose a clique
	 *            randomly from all cliques in the graph b) OUTDEGREE: choose
	 *            the clique for which the number of outgoing links is highest
	 */
	public BuildSubGraph(int include, int minDegree, int maxDegree,
			int startNodes, CliqueSelection selection) {
		this(include, minDegree, maxDegree, startNodes, selection,
				NextSelection.MAX);
	}

	/**
	 * 
	 * constructor allowing reseting the name
	 */
	public BuildSubGraph(String key, int include, int minDegree, int maxDegree,
			int startNodes, CliqueSelection selection,
			NextSelection selectionNext, Parameter[] params) {
		super(key, params);
		this.selection = selection;
		this.minDegree = Math.max(minDegree, 1);
		this.maxDegree = maxDegree;
		this.include = include;
		this.startNodes = startNodes;
		this.selectionNext = selectionNext;
		if (maxDegree < startNodes - 1 || minDegree > maxDegree) {
			throw new IllegalArgumentException(
					"Degree constraints do not agree in BuildSubGraphMax "
							+ maxDegree);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		// PHASE 1: initialize variables and get initial clique
		Node[] nodesOld = g.getNodes();
		boolean[] added = new boolean[nodesOld.length]; // added nodes
		int[] degree = new int[nodesOld.length]; // current number of links into
													// the subgraphs
		Random rand = new Random();
		int[] out;
		int maxdeg = nodesOld[this.getMaxDegree(nodesOld, rand)].getDegree();
		if (this.maxDegree < maxdeg) {
			maxdeg = this.maxDegree;
		}
		int[] start = this.getStartIndex(nodesOld, rand);
		HashMap<Integer, Integer> newIndex = new HashMap<Integer, Integer>(
				Math.min(nodesOld.length, this.include)); // index of node in
															// subgraph
		HashMap<Integer, Vector<Integer>> neighs = new HashMap<Integer, Vector<Integer>>(
				nodesOld.length); // neighbors in subgraph (only those added
									// earlier!!)
		Vector<Vector<Integer>> inDegree = new Vector<Vector<Integer>>(
				maxdeg + 1); // current links into subgraph for nodes not yet
								// contained;
		// i-th entry=list of node index with i links
		for (int i = 0; i < maxdeg + 1; i++) {
			inDegree.add(new Vector<Integer>());
		}

		// PHASE 2: create neighbor list for nodes in initial clique
		int count = start.length;
		Vector<Integer> list;
		for (int i = 0; i < start.length; i++) {
			added[start[i]] = true;
			System.out.println("Added Initial " + start[i]);
			degree[start[i]] = start.length - 1;
			Vector<Integer> neighbors = new Vector<Integer>();
			for (int k = 0; k < i; k++) {
				neighbors.add(start[k]);
			}
			neighs.put(start[i], neighbors);
		}
		for (int i = 0; i < start.length; i++) {
			if (degree[start[i]] == this.maxDegree) {
				// no neighbor is added later on since maximum degree is reached
			} else {
				// add in-link for each neighbor
				out = nodesOld[start[i]].getOutgoingEdges();
				for (int j = 0; j < out.length; j++) {
					if (!added[out[j]] && degree[out[j]] < this.maxDegree) {
						inDegree.get(degree[out[j]]).removeElement(out[j]);
						degree[out[j]]++;
						inDegree.get(degree[out[j]]).add(out[j]);
					}
				}
			}

		}

		// PHASE 3: add nodes until desired size is reached or no node can be
		// added
		while (count < this.include && count < nodesOld.length) {
			int chosen = this.getNext(inDegree, rand);
			if (chosen == -1) {
				break;
			}
			neighs.put(chosen, this.addNode(added, degree, nodesOld[chosen],
					inDegree, nodesOld));
			count++;
		}

		// PHASE 4: create new graph
		Node[] nodesNew = new Node[count];
		Edges edges = new Edges(nodesNew, g.computeNumberOfEdges());
		int c = 0;
		for (int i = 0; i < added.length; i++) {
			if (added[i]) {
				nodesNew[c] = nodesOld[i];
				newIndex.put(i, c);
				nodesNew[c].setIndex(c);
				c++;

			}
		}
		c = 0;
		for (int i = 0; i < added.length; i++) {
			if (added[i]) {
				list = neighs.get(i);
				for (int j = 0; j < list.size(); j++) {
					edges.add(c, newIndex.get(list.get(j)));
					edges.add(newIndex.get(list.get(j)), c);
				}
				c++;
			}
		}
		edges.fill();
		g.setNodes(nodesNew);
		return g;
	}

	private int getNext(Vector<Vector<Integer>> degs, Random rand) {
		if (this.selectionNext == NextSelection.MAX) {
			int max = degs.size() - 1;
			while (max >= this.minDegree && degs.get(max).size() == 0) {
				max--;
			}
			if (max < this.minDegree) {
				return -1;
			} else {
				return degs.get(max).remove(rand.nextInt(degs.get(max).size()));
			}
		}
		if (this.selectionNext == NextSelection.RANDOM) {
			int count = 0;
			for (int j = this.minDegree; j < degs.size(); j++) {
				count = count + degs.get(j).size();
			}
			if (count == 0) {
				return -1;
			} else {
				int c = rand.nextInt(count);
				count = 0;
				for (int j = this.minDegree; j < degs.size(); j++) {
					if (count + degs.get(j).size() > c) {
						return degs.get(j).remove(c - count);
					}
					count = count + degs.get(j).size();
				}
			}
		}
		if (this.selectionNext == NextSelection.MIN) {
			int min = this.minDegree;
			while (min < degs.size() && degs.get(min).size() == 0) {
				min++;
			}
			if (min >= degs.size()) {
				return -1;
			}
			return degs.get(min).remove(rand.nextInt(degs.get(min).size()));

		}
		return -1;
	}

	/**
	 * add a node to the subgraph and change inDegree accordingly
	 * 
	 * @param added
	 * @param degree
	 * @param n
	 * @param inDegree
	 * @param nodes
	 * @return
	 */
	private Vector<Integer> addNode(boolean[] added, int[] degree, Node n,
			Vector<Vector<Integer>> inDegree, Node[] nodes) {
		int[] out = n.getIncomingEdges();
		Vector<Integer> neighbors = new Vector<Integer>();
		added[n.getIndex()] = true;
		for (int i = 0; i < out.length; i++) {
			if (added[out[i]] && degree[out[i]] < this.maxDegree
					&& neighbors.size() < this.maxDegree) {
				neighbors.add(out[i]);
				degree[out[i]]++;
				if (degree[out[i]] == this.maxDegree) {
					int[] o = nodes[out[i]].getOutgoingEdges();
					for (int k = 0; k < o.length; k++) {
						if (!added[o[k]] && degree[o[k]] > 0) {
							inDegree.get(degree[o[k]]).removeElement(o[k]);
							degree[o[k]]--;
							if (degree[o[k]] > 0)
								inDegree.get(degree[o[k]]).add(o[k]);
						}
					}
				}
			}
			if (!added[out[i]] && degree[out[i]] < this.maxDegree) {
				inDegree.get(degree[out[i]]).removeElement(out[i]);
				degree[out[i]]++;
				inDegree.get(degree[out[i]]).add(out[i]);
			}
		}

		return neighbors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	/**
	 * returns indices of initial nodes
	 * 
	 * @param nodes
	 * @param rand
	 * @return
	 */
	private int[] getStartIndex(Node[] nodes, Random rand) {
		if (this.selection == CliqueSelection.RANDOM) {
			return determineCliqueRandom(rand, nodes);
		}
		if (this.selection == CliqueSelection.MAXOUTDEGREE) {
			return determineCliqueMostLinks(rand, nodes);
		}
		throw new IllegalArgumentException("Selection type "
				+ this.selection.toString() + " in BuildSubGraph not known");
	}

	/**
	 * determine initial nodes for random case
	 * 
	 * @param rand
	 * @param nodes
	 * @return
	 */
	private int[] determineCliqueRandom(Random rand, Node[] nodes) {
		if (this.startNodes == 1) {
			return new int[] { rand.nextInt(nodes.length) };
		}
		if (this.startNodes == 2) {
			int s = rand.nextInt(nodes.length);
			while (nodes[s].getDegree() == 0) {
				s = rand.nextInt(nodes.length);
			}
			int[] out = nodes[s].getOutgoingEdges();
			return new int[] { s, out[rand.nextInt(out.length)] };
		}
		int[] result = new int[this.startNodes];
		int n = rand.nextInt(nodes.length);
		int count = 0;
		boolean found = false;
		while (count < nodes.length & !found) {
			count++;
			if (nodes[n].getOutDegree() >= this.startNodes
					&& nodes[n].getInDegree() >= this.startNodes) {
				Vector<Node> neighs = new Vector<Node>();
				Node cur;
				for (int i = 0; i < nodes[n].getOutDegree(); i++) {
					cur = nodes[nodes[n].getOutgoingEdges()[i]];
					if (cur.getInDegree() >= this.startNodes
							&& cur.getOutDegree() >= this.startNodes)
						neighs.add(cur);
				}
				neighs.add(nodes[n]);
				int round = 0;
				int pos = 0;
				while (round < neighs.size()
						&& neighs.size() >= this.startNodes) {
					if (this.checkLinked(neighs.get(pos).getIndex(), neighs) < this.startNodes) {
						if (pos == neighs.size()) {
							neighs = new Vector<Node>();
						} else {
							round = 0;
							neighs.remove(pos);
						}
					} else {
						pos = (pos + 1) % neighs.size();
						round++;
					}
				}
				if (neighs.size() >= this.startNodes) {
					neighs.remove(neighs.get(neighs.size() - 1));
					Vector<Node[]> old = determinePairs(neighs);
					Vector<Node[]> next;
					int m = 3;
					while (old.size() > 0 && m < this.startNodes) {
						m++;
						next = new Vector<Node[]>();
						for (int j = 0; j < old.size(); j++) {
							next.addAll(this.determineMClique(old.get(j),
									neighs));
						}
						old = next;
					}
					if (m == this.startNodes && old.size() > 0) {
						Node[] res = old.get(rand.nextInt(old.size()));
						for (int j = 0; j < this.startNodes - 1; j++) {
							result[j] = res[j].getIndex();
						}
						result[this.startNodes - 1] = nodes[n].getIndex();
						return result;
					}
				}
			}

			n = (n + 1) % nodes.length;
		}
		if (!found) {
			return new int[0];
		}
		return result;
	}

	/**
	 * return number of links node with index index has into the set neighs
	 * 
	 * @param index
	 * @param neighs
	 * @return
	 */
	private int checkLinked(int index, Vector<Node> neighs) {
		int c = 0;
		for (int i = 0; i < neighs.size(); i++) {
			if (neighs.get(i).hasIn(index) && neighs.get(i).hasOut(index)) {
				c++;
			}
		}
		return c;
	}

	/**
	 * all connected pairs in neighs (bidirectional connection required)
	 * 
	 * @param neighs
	 * @return
	 */
	private Vector<Node[]> determinePairs(Vector<Node> neighs) {
		Vector<Node[]> res = new Vector<Node[]>();
		Node[] array = new Node[2];
		for (int i = 0; i < neighs.size(); i++) {
			array[0] = neighs.get(i);
			for (int j = i + 1; j < neighs.size(); j++) {
				if (array[0].hasIn(neighs.get(j).getIndex())
						&& array[0].hasOut(neighs.get(j).getIndex())) {
					array[1] = neighs.get(j);
					res.add(array.clone());
				}
			}
		}
		return res;
	}

	/**
	 * return all cliques that can be generated from oldClique by adding a node
	 * in neighs
	 * 
	 * @param oldClique
	 * @param neighs
	 * @return
	 */
	private Vector<Node[]> determineMClique(Node[] oldClique,
			Vector<Node> neighs) {
		Vector<Node[]> res = new Vector<Node[]>();
		Node[] array = new Node[oldClique.length + 1];
		for (int i = 0; i < array.length - 1; i++) {
			array[i] = oldClique[i];
		}
		for (int i = 0; i < neighs.size(); i++) {
			boolean ok = true;
			Node cur = neighs.get(i);
			for (int j = 0; j < oldClique.length; j++) {
				if (cur.getIndex() < oldClique[j].getIndex()
						|| !cur.hasIn(oldClique[j].getIndex())
						|| !cur.hasOut(oldClique[j].getIndex())) {
					ok = false;
				}
			}
			if (ok) {
				array[array.length - 1] = neighs.get(i);
				res.add(array.clone());
			}
		}
		return res;
	}

	/**
	 * determine clique for case OUTDEGREE
	 * 
	 * @param rand
	 * @param nodes
	 * @return
	 */
	private int[] determineCliqueMostLinks(Random rand, Node[] nodes) {
		if (this.startNodes == 1) {
			return new int[] { this.getMaxDegree(nodes, rand) };
		}
		if (this.startNodes == 2) {
			return getMaxPair(nodes, rand);
		}
		int[] result = new int[this.startNodes];
		int n = rand.nextInt(nodes.length);
		int count = 0;
		boolean found = false;
		Vector<int[]> maxIndex = new Vector<int[]>();
		int max = 0;
		int c;
		while (count < nodes.length) {
			count++;
			if (nodes[n].getOutDegree() >= this.startNodes
					&& nodes[n].getInDegree() >= this.startNodes) {
				Vector<Node> neighs = new Vector<Node>();
				Node cur;
				for (int i = 0; i < nodes[n].getOutDegree(); i++) {
					cur = nodes[nodes[n].getOutgoingEdges()[i]];
					if (cur.getInDegree() >= this.startNodes
							&& cur.getOutDegree() >= this.startNodes)
						neighs.add(cur);
				}
				neighs.add(nodes[n]);
				int round = 0;
				int pos = 0;
				while (round < neighs.size()
						&& neighs.size() >= this.startNodes) {
					if (this.checkLinked(neighs.get(pos).getIndex(), neighs) < this.startNodes) {
						if (pos == neighs.size()) {
							neighs = new Vector<Node>();
						} else {
							round = 0;
							neighs.remove(pos);
						}
					} else {
						pos = (pos + 1) % neighs.size();
						round++;
					}
				}
				if (neighs.size() >= this.startNodes) {
					neighs.remove(neighs.get(neighs.size() - 1));
					Vector<Node[]> old = determinePairs(neighs);
					Vector<Node[]> next;
					int m = 3;
					while (old.size() > 0 && m < this.startNodes) {
						m++;
						next = new Vector<Node[]>();
						for (int j = 0; j < old.size(); j++) {
							next.addAll(this.determineMClique(old.get(j),
									neighs));
						}
						old = next;
					}
					if (m == this.startNodes && old.size() > 0) {
						for (int i = 0; i < old.size(); i++) {
							Node[] res = old.get(rand.nextInt(old.size()));
							c = 0;
							for (int j = 0; j < res.length; j++) {
								c = c + res[j].getDegree();
							}
							if (c > max) {
								max = c;
								maxIndex = new Vector<int[]>();
							}
							if (c == max) {
								for (int j = 0; j < this.startNodes - 1; j++) {
									result[j] = res[j].getIndex();
								}
								result[this.startNodes - 1] = nodes[n]
										.getIndex();
								maxIndex.add(result.clone());
							}
						}
						found = true;
					}
				}
			}

			n = (n + 1) % nodes.length;
		}
		if (!found) {
			return new int[0];
		}
		return maxIndex.get(rand.nextInt(maxIndex.size()));
	}

	/**
	 * index of highest-degree node ties are broken randomly
	 * 
	 * @param nodes
	 * @param rand
	 * @return
	 */
	private int getMaxDegree(Node[] nodes, Random rand) {
		int max = 0;
		Vector<Integer> maxIndex = new Vector<Integer>();
		for (int j = 0; j < nodes.length; j++) {
			if (nodes[j].getDegree() > max) {
				max = nodes[j].getDegree();
				maxIndex = new Vector<Integer>();
			}
			if (nodes[j].getDegree() >= max) {
				maxIndex.add(j);
			}
		}
		return maxIndex.get(rand.nextInt(maxIndex.size()));
	}

	/**
	 * connected pair of nodes with highest degree
	 * 
	 * @param nodes
	 * @param rand
	 * @return
	 */
	private int[] getMaxPair(Node[] nodes, Random rand) {
		int max = 0;
		int cur;
		Vector<int[]> maxIndex = new Vector<int[]>();
		for (int j = 0; j < nodes.length; j++) {
			int[] out = nodes[j].getOutgoingEdges();
			for (int i = 0; i < out.length; i++) {
				if (out[i] > j && nodes[j].hasIn(out[j])) {
					cur = nodes[j].getDegree() + nodes[out[j]].getDegree();
					if (cur > max) {
						max = cur;
						maxIndex = new Vector<int[]>();
					}
					if (cur == max) {
						maxIndex.add(new int[] { j, out[i] });
					}
				}
			}
		}
		return maxIndex.get(rand.nextInt(maxIndex.size()));
	}

}

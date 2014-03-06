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
 * UniformRandomWalker.java
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
package gtna.transformation.sampling.walker;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;
import gtna.transformation.sampling.sample.INetworkSample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Tim
 * 
 */
public class UniformRandomWalker extends AWalker {
	
	/**
	 * @param key
	 * @param value
	 * @param awc
	 */
	public UniformRandomWalker() {
		super("UNIFORM_RANDOM_WALKER");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.sampling.AWalker#selectNextNode(java.util.Collection)
	 */
	@Override
	protected Node selectNextNode(Collection<Node> candidates) {
		ArrayList<Node> al = new ArrayList<Node>(candidates.size());
		al.addAll(candidates);
		Collections.shuffle(al);

		return al.get(0);

	}

	/**
	 * Returns all nodes of the graph as candidates
	 * 
	 * @param g
	 *            Graph
	 * @param n
	 *            Current node
	 * @return List of candidates
	 */
	@Override
	public List<Node> resolveCandidates(Graph g, Node n) {
		List<Node> c = new ArrayList<Node>();

		List<Node> a = Arrays.asList(g.getNodes());
		Collections.shuffle(a);

		c.add(this.filterCandidates(a).toArray(new Node[0])[0]);

		return c;
	}

	
	
	
	
	/**
	 * Returns the current neighbors of current nodes of the walker
	 * 
	 * @return Map: <br>
	 *         <b>key</b>: current node <br>
	 *         <b>value</b>: neighbors of the current node
	 */
	@Override
	public Map<Node, List<Node>> getCurrentCandidates() {
		Map<Node, List<Node>> cn = new HashMap<Node, List<Node>>();

		for (Node n : currents) {
			List<Node> nn = new ArrayList<Node>();
//			Collection<Node> nn = resolveCandidates(this.getGraph(), n);
			cn.put(n, nn);
		}
		return cn;
	}


	private HashSet<Integer> visited = new HashSet<Integer>();
	private LinkedList<Integer> walkingList = new LinkedList<Integer>();
	
	
	/**
	 * Move walker one step
	 */
	@Override
	public void takeAStep(Graph g, INetworkSample ns) {
		
		
//		Set<Integer> sampled = ns.getSampleNodeMapping().keySet();
		
		int next;
		
		
		double nc = ns.getScaledown() * g.getNodeCount();
//		int nc = g.getNodeCount();
		
		if(walkingList.size() == 0){
			initWalkingList(nc);
		}
		
		next = walkingList.pollFirst();
		
		
//		
//		next = this.getRNG().nextInt(nc);
//	
//		while(visited.contains(next)){
//			next = this.getRNG().nextInt(nc);		
//		}
//		
//		visited.add(next);

		
		
		Node n = g.getNode(next);
		
		currents.clear();
		currents.add(n);
		
		
//		Map<Node, Collection<Node>> cc = this.getCurrentCandidates();
//
//		Collection<Node> c;
//
//		// TODO
//		if (cc.size() > 0) {
//			c = cc.keySet();
//		} else {
//			// System.err.println("Candidate Set is empty! catching restart nodes");
//			c = getRestartNodes();
//		}
//		for (Node n : c) {
//			Collection<Node> candidates = new ArrayList<Node>();
//			do {
//				// TODO
//				if (cc.size() > 0) {
//					candidates = controller.filterCandidates(cc.get(n));
//				} else {
//					candidates = controller.filterCandidates(c);
//				}
//				if (candidates.size() == 0) {
//					// System.err.println("Candidate Set is empty! catching restart nodes");
//					cc.clear();
//					c = getRestartNodes();
//				}
//
//			} while (candidates.size() == 0);
//
//			Node next = this.selectNextNode(candidates);
//
//			currents.remove(n);
//			currents.add(next);
//		}
	}

	/**
	 * @param nc
	 */
	private void initWalkingList(double nc) {
		for(int i = 0; i < nc;i++){
			walkingList.offer(i);
		}
		Collections.shuffle(walkingList);
	}

}

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
 * CDRandomWalk.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;

/**
 * @author Flipp
 *
 */
public class CDRandomWalk extends Transformation {
	
	public CDRandomWalk(){
		super("CD_RANDOMWALK");
		
	}
	
	
	private RWCommunity getCommunityAroundNode(Node s, Graph g, HashMap<Node, Boolean> ignore){
		RWCommunityList cl = randomWalk(s, g, ignore);
		addUnpicked(cl, s, g, ignore);
		for(RWCommunity akt : cl.getCommunities()){
			for(Node aktNode : akt.getNodes()){
				akt.calculateGamma(aktNode);
			}
			
			akt.sortVertices();
			akt.computeGamma();
		}
		
		cl.sortCommunities();
		
		boolean changed = true;
		System.out.println("Exchanging nodes...");
		while(changed){
			changed = false;
			for(int i = 0; i < cl.getCommunities().length-1; i++){
				if(doExchange(cl.getCommunities()[i], cl.getCommunities()[i+1])){
					changed = true;
				}
			}
			
			
			cl.removeEmptyCommunities();
			
			cl.sortCommunities();
		}
		System.out.println();
		System.out.println("Resulted in " + cl.getCommunities().length);
		
		return cl.getCommunities()[0];
		
		
	}

	/**
	 * @param cl
	 * @param s
	 * @param g
	 * @param ignore 
	 */
	private void addUnpicked(RWCommunityList cl, Node s, Graph g, HashMap<Node, Boolean> ignore) {
		RWCommunity nw = new RWCommunity(g);
		for(Node a : g.getNodes()){
			if(!cl.containsNode(a) && !ignore.containsKey(a))
				nw.add(a);
		}
		
		cl.add(nw);
	}


	/**
	 * @param community
	 * @param community2
	 * @return
	 */
	private boolean doExchange(RWCommunity c1, RWCommunity c2) {
		
		boolean changed = false;
		double temp;
		Node[] n1 = c1.getNodes();
		Node[] n2 = c2.getNodes();
		for(Node akt : n1){
			if(c2.contains(akt))
				continue;
			
			for(Node akt2 : n2){
				if(c1.contains(akt2))
					continue;
				
				if(c1.getGamma(akt) >= c2.getGamma(akt2)){
					temp = c1.getGamma(akt);
					if(temp > c2.calculateGamma(akt) && !c2.contains(akt)){
						System.out.println(akt.getIndex() + "=>" + c2.hashCode() + ":" + "old:" + temp + "new:" +c2.calculateGamma(akt));
						c2.add(akt);
						c1.remove(akt);
						c1.computeGamma();
						c2.computeGamma();
						changed = true;
						continue;
					}
				}
				else {
					temp = c2.getGamma(akt2);
					if(temp > c1.calculateGamma(akt2) && !c1.contains(akt2)){
						System.out.println(akt2.getIndex() + "=>" + c1.hashCode() + ":" + "old:" + temp + "new:" +c1.calculateGamma(akt2));
						c1.add(akt2);
						c2.remove(akt2);
						c1.computeGamma();
						c2.computeGamma();
						changed =  true;
						continue;
					}
				}
			}
		}
		
		return changed;
	}


	/**
	 * @param s
	 * @param g
	 * @return
	 */
	private RWCommunityList randomWalk(Node s, Graph g, HashMap<Node, Boolean> ignore) {
		RWCommunityList ret = new RWCommunityList();
		boolean finished = false;
				
		Node aktNode;
		Node next;
		RWCommunity aktCom;
		System.out.println("Starting Random Walk");
		while(!finished){
			System.out.print(".");
			aktNode = s;
			aktCom = new RWCommunity(g);
			while(true){
				if(aktCom.contains(aktNode))
					break;
				
				aktCom.add(aktNode);
				if(!hasPickableNeighbors(aktNode, g, ignore))
					break;
				
				do{
					next = g.getNode(aktNode.getOutgoingEdges()[(int) Math.floor(Math.random() * aktNode.getOutDegree())]);
				}while(ignore.containsKey(next));
				aktNode = next;
			}
			
			finished = ret.contains(aktCom);
			
			ret.add(aktCom);
		}
		System.out.println();
		System.out.println("Found " + ret.getCommunities().length);
		
		return ret;
	}


	/**
	 * @param aktNode
	 * @param ignore 
	 * @param g 
	 * @return
	 */
	private boolean hasPickableNeighbors(Node aktNode, Graph g, HashMap<Node, Boolean> ignore) {
		boolean ret = false;
		for(int akt : aktNode.getOutgoingEdges()){
			if(!ignore.containsKey(g.getNode(akt)))
				ret = true;
		}
		
		return ret;
	}


	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		if(!applicable(g))
			return g;
		
		Stack<Node> s = new Stack<Node>();
		for(Node akt : g.getNodes())
			s.add(akt);
		
		gtna.communities.Community temp;
		ArrayList<gtna.communities.Community> t = new ArrayList<gtna.communities.Community>();
		RWCommunity c;
		
		Node n;
		int index = 0;
		int index2 = 0;
		HashMap<Node, Boolean> ignore = new HashMap<Node, Boolean>();
		
		int[] nodes;
		while(!s.empty()){
			System.out.println("Remaining nodes: " + s.size());
			n = s.pop();
			c = this.getCommunityAroundNode(n, g, ignore);
			nodes = new int[c.getNodes().length];
			index = 0;
			
			for(Node akt : c.getNodes()){
				s.remove(akt);
				ignore.put(akt, true);
				nodes[index] = akt.getIndex();
				index++;
			}
			temp = new gtna.communities.Community(index2, nodes);
			index2++;
			t.add(temp);
		}
		
		g.addProperty(g.getNextKey("COMMUNITIES"), new gtna.communities.CommunityList(t));
		
		
		return g;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

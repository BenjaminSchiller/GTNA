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
 * CDExpandingShells.java
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
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Flipp
 *
 */
public class CDExpandingShells extends Transformation {
	
	private double alpha;

	public CDExpandingShells(double alpha){
		super("CD_EXPSHELLS", new Parameter[]{new DoubleParameter("ALPHA", alpha)});
		this.alpha = alpha;
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
		
		Node akt;
		ArrayList<Integer> t;
		ArrayList<gtna.communities.Community> ct = new ArrayList<gtna.communities.Community>();
		int index = 0;
		HashMap<Node, Boolean> ignore = new HashMap<Node, Boolean>();
		while(!s.empty()){
			akt = s.pop();
			t = getCommunityAroundNode(akt, g, ignore);
			for(int aktN : t){
				ignore.put(g.getNode(aktN), true);
				s.remove(g.getNode(aktN));
			}
			
			ct.add(new gtna.communities.Community(index, t));
			index++;
			
			
		}
		System.out.println(ct.size());
		g.addProperty(g.getNextKey("COMMUNITIES"), new gtna.communities.CommunityList(ct));
		
		return g;
	}

	/**
	 * @param akt
	 * @param g
	 * @param ignore
	 * @return
	 */
	private ArrayList<Integer> getCommunityAroundNode(Node akt, Graph g,
			HashMap<Node, Boolean> ignore) {
		
		
		ArrayList<Integer> sphere = new ArrayList<Integer>();
		sphere.add(akt.getIndex());
		double curr = akt.getDegree();
		double last;
		
		do{
			last = curr;
			
			sphere = expandSphere(sphere, g, ignore);
			
			curr = calcEmergingDegree(sphere, g);
			
			
			
			
		}while(curr/last > alpha );
		
		System.out.println("Found com with " + sphere.size());
		return sphere;
		
	}

	/**
	 * @param sphere
	 * @param g
	 * @return
	 */
	private double calcEmergingDegree(ArrayList<Integer> sphere, Graph g) {
		int count = 0;
		for(int akt : sphere){
			for(int akt2 : g.getNode(akt).getOutgoingEdges()){
				if(contains(sphere, akt2))
					count++;
			}
		}
		
		return count;
	}

	/**
	 * @param sphere
	 * @param akt2
	 * @return
	 */
	private boolean contains(ArrayList<Integer> sphere, int akt2) {
		boolean ret = false;
		for(int akt : sphere)
			if(akt == akt2)
				ret = true;
		
		return ret;
	}	

	/**
	 * @param sphere
	 * @param g
	 * @param ignore
	 * @return 
	 */
	private ArrayList<Integer> expandSphere(ArrayList<Integer> sphere, Graph g,
			HashMap<Node, Boolean> ignore) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int akt : sphere){
			if(!contains(ret, akt) && !contains(ignore, g.getNode(akt)))
				ret.add(akt);
			
			for(int akt2 : g.getNode(akt).getOutgoingEdges())
				if(!contains(ret, akt2) && !contains(ignore, g.getNode(akt)))
					ret.add(akt2);
		}
		
		return ret;
		
	}

	/**
	 * @param ignore
	 * @param akt
	 * @return
	 */
	private boolean contains(HashMap<Node, Boolean> ignore, Node n) {
		return ignore.containsKey(n);
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

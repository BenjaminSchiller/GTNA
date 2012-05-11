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
 * Community.java
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

import gtna.graph.Edge;
import gtna.graph.EdgeWeights;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.NodeWeights;

/**
 * @author Flipp
 *
 */
public class Community {

	private int id;
	private Graph g;
	private double internalEdges = 0;
	private HashMap<Integer, Node> nodes;
	private CommunityList coms;
	private double externalEdges = 0;
	private NodeWeights nw;
	private EdgeWeights ew;

	/**
	 * @param i
	 * @param g
	 */
	public Community(int i, Graph g, CommunityList coms, NodeWeights nw, EdgeWeights ew) {
		id = i;
		this.nw = nw;
		this.ew = ew;
		this.g = g;
		this.coms = coms;
		nodes = new HashMap<Integer, Node>();
	}

	/**
	 * @return
	 */
	public int getIndex() {
		return id;
	}

	/**
	 * @return
	 */
	public double getInternalEdges() {
		return internalEdges;
	}


	/**
	 * @return
	 */
	public Node[] getNodes() {
		return nodes.values().toArray(new Node[nodes.size()]);
	}

	/**
	 * @param i
	 */
	public void removeNode(int i) {
		Node n = g.getNode(i);
		int t;
		if(nw != null)
			internalEdges -= nw.getWeight(i);
		
		for(Edge akt : n.getEdges()){
			if(akt.getSrc() == i)
				t = akt.getDst();
			else
				t = akt.getSrc();
			
			if(coms.getCommunityByNode(t).getIndex() == this.getIndex()){
				internalEdges -= (ew != null)?ew.getWeight(akt):1;
			}
			else
				externalEdges -= (ew != null)?ew.getWeight(akt):1;
		}
		nodes.remove(i);
		
	}
	
	public void setNode(Node node){
		nodes.put(node.getIndex(), node);
		if(nw != null)
			internalEdges += nw.getWeight(node.getIndex());
			
		for(Edge akt : node.getEdges())
			externalEdges += (ew != null)?ew.getWeight(akt):1;
			
	}

	/**
	 * @param node
	 */
	public void addNode(Node node) {
		int t;
		if(nw != null)
			internalEdges += nw.getWeight(node.getIndex());
		
		for(Edge akt : node.getEdges()){
			if(akt.getSrc() == node.getIndex())
				t = akt.getDst();
			else
				t = akt.getSrc();
			
			if(coms.getCommunityByNode(t).getIndex() == this.getIndex()){
				internalEdges += (ew != null)?ew.getWeight(akt):1;
			}
			else
				externalEdges += (ew != null)?ew.getWeight(akt):1;
		}
		
		nodes.put(node.getIndex(), node);
		
	}

	/**
	 * @return
	 */
	public double getExternalEdges() {
		return externalEdges;
	}

	/**
	 * @param i
	 */
	public void setID(int i) {
		id = i;
	}
}



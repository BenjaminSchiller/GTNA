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
 * Assortativity.java
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
package gtna.metrics.basic;

import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Tim
 * 
 */
public class Assortativity extends Metric {

	public final static int NODE_NODE = 0;
	public final static int IN_IN = 1;
	public final static int IN_OUT = 2;
	public final static int OUT_IN = 3;
	public final static int OUT_OUT = 4;

	private int type;

	/**
	 * @param key
	 */
	public Assortativity() {
		super("ASSORTATIVITY");
		this.type = NODE_NODE;
	}

	/**
	 * @param key
	 * @param parameters
	 */
	public Assortativity(int t) {
		super("ASSORTATIVITY", new Parameter[] { new IntParameter("TYPE", t) });
		this.type = t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		double r = 0.0; // assortativity coefficient

		Edges E = g.getEdges();
		Node[] V = g.getNodes();

		int M = E.size();

		double numerator = sumPairDegreeProduct(E, g) - (1/M)*sumSourceNodeDegree(E, g)*sumDestinationNodeDegree(E, g);
		double denominator = 1;
		
		r = numerator / denominator;

	}
	
	
	

	/**
	 * @param e
	 * @param g
	 * @return
	 */
	private int sumDestinationNodeDegree(Edges e, Graph g) {
		int sdnd = 0;
		
		ArrayList<Edge> el = e.getEdges();
		
		int d;
		Node dst;
		for(Edge p : el){
			d = p.getDst();
			
			dst = g.getNode(d);
			
			sdnd += getDestinationDegree(dst);
		}
		
		return sdnd;
	}

	/**
	 * @param e
	 * @param g
	 * @return
	 */
	private int sumSourceNodeDegree(Edges e, Graph g) {
		int ssnd = 0;
		
		ArrayList<Edge> el = e.getEdges();
		
		int s;
		Node src;
		for(Edge p : el){
			s = p.getSrc();
			
			src = g.getNode(s);
			
			ssnd += getSourceDegree(src);
		}
		
		
		return ssnd;
	}

	/**
	 * @param e
	 * @return
	 */
	private int sumPairDegreeProduct(Edges e, Graph g) {
		int spdp = 0;
		
		ArrayList<Edge> el = e.getEdges();
		
		int s;
		int d;
		Node src;
		Node dst;
		int src_d;
		int dst_d;
		
		for(Edge p : el){
			s = p.getSrc();
			d = p.getDst();
			
			src = g.getNode(s);
			dst = g.getNode(d);
			
			src_d = getSourceDegree(src);
			dst_d = getDestinationDegree(dst);
			
			spdp = src_d * dst_d;
			
		}		
		return spdp;
	}
	
	private int getSourceDegree(Node src){
		switch(type){
			case NODE_NODE: return src.getDegree();
			case IN_IN: return src.getInDegree();
			case IN_OUT: return src.getInDegree();
			case OUT_IN: return src.getOutDegree();
			case OUT_OUT: return src.getOutDegree();
			default: return src.getDegree();
		}
	}
	
	private int getDestinationDegree(Node dst){
		switch(type){
			case NODE_NODE: return dst.getDegree();
			case IN_IN: return dst.getInDegree();
			case IN_OUT: return dst.getOutDegree();
			case OUT_IN: return dst.getInDegree();
			case OUT_OUT: return dst.getOutDegree();
			default: return dst.getDegree();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		// TODO Auto-generated method stub
		return false;
	}

}

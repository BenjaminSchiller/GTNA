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
 * Complete.java
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
package gtna.networks.model.placementmodels;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public abstract class AbstractPlacementModel extends NetworkImpl implements Network {
	private PlaneIdentifierSpaceSimple coords;
	private double range;
	private ConnectionType ct;
	
	public AbstractPlacementModel(String key, int nodes, double range, String[] keys, String[] values, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {
		super(key, nodes, addKeys(keys), addValues(values, range, ct), ra, t);
		this.ct = ct;
		this.range = range;
	}
	
	private static String[] addKeys(String[] keys) {
		String[] ret = new String[keys.length+2];
		ret[0] = "range";
		ret[1] = "ct";

		
		for(int i = 0; i < keys.length; i++){
			ret[i+2] = keys[i];
		}
		
		return ret;
	}
	
	private static String[] addValues(String[] vals, double range, ConnectionType ct) {
		String[] ret = new String[vals.length+2];
		ret[0] = Double.toString(range);
		ret[1] = ct.toString();		
		
		for(int i = 0; i < vals.length; i++){
			ret[i+5] = vals[i];
		}
		
		return ret;
	}

	protected Graph finish(){
		Graph graph = new Graph(this.description());
		graph.addProperty("PlacementCoords", coords);
		Node[] nodes = Node.init(this.nodes(), graph);
		
		Edges edges = new Edges(nodes, this.nodes() * (this.nodes() - 1));
		
		switch(ct){
		case udg:
			for(int i = 0; i < nodes.length; i++){
				for(int j=0; j < nodes.length; j++){
					if(i != j && coords.getPartitions()[i].distance( (coords.getPartitions()[j].getRepresentativeID()) ) < range) {
						edges.add(i, j);
					}
				}
			}
			break;			
		}
		
		edges.fill();
		graph.setNodes(nodes);

		return graph;
	}

	public PlaneIdentifierSpaceSimple getCoords() {
		return this.coords;
	}

	public void setCoords(PlaneIdentifierSpaceSimple coords) {
		this.coords = coords;
	}
	

}

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
 * NodeConnector.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Philipp Neubrand;
 * Contributors:    -;
 *
 * ---------------------------------------
 */
package gtna.networks.model.placementmodels;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public abstract class AbstractPlacementModel extends NetworkImpl implements Network {
	private PlaneIdentifierSpaceSimple coords;
	private NodeConnector nc;
	private double width;
	private double height;
	
	public AbstractPlacementModel(String key, int nodes, double width,
			double height, String[] keys, String[] values, NodeConnector nc,
			RoutingAlgorithm ra, Transformation[] t) {
		super(key, nodes, addToArray(keys, new String[]{"NC", "WIDTH", "HEIGHT"}), addToArray(values, new String[]{nc.getDescription(), Double.toString(width), Double.toString(height)}), ra, t);
		setNodes(nodes);
		setWidth(width);
		setHeight(height);
		setNodeConnector(nc);
		setCoords(new PlaneIdentifierSpaceSimple(null, width, height, false));
	}
	

	public void setNodeConnector(NodeConnector nc) {
		this.nc = nc;
	}
	
	public NodeConnector getNodeConnector(){
		return nc;
	}

	public static String[] addToArray(String[] array, String val){
		return addToArray(array, new String[]{val});
	}
	
	public static String[] addToArray(String[] array, String[] tba) {
		String[] ret = new String[array.length+tba.length];
		for(int i = 0; i < array.length; i++){
			ret[i] = array[i];
		}
		for(int i = 0; i < tba.length; i++){
			ret[array.length + i] = tba[i];
		}
		
		return ret;
	}
	
	protected Graph finish(){
		Graph graph = new Graph(this.description());
		graph.addProperty("PlacementCoords", coords);
		Node[] nodes = Node.init(this.nodes(), graph);
		
		getNodeConnector().connect(nodes, getCoords());
		
		graph.setNodes(nodes);

		return graph;
	}

	public PlaneIdentifierSpaceSimple getCoords() {
		return this.coords;
	}

	public void setCoords(PlaneIdentifierSpaceSimple coords) {
		this.coords = coords;
	}
	
	public double getWidth(){
		return width;
	}
	
	public void setWidth(double width){
		this.width = width;
	}
	
	public void setHeight(double height){
		this.height = height;
	}
	
	public double getHeight(){
		return height;
	}
}

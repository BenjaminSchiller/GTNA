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
 * PlacementModelContainer.java
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
package gtna.networks.model.placementmodels;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Util;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * A <code>PlacementModelContainer</code> is a network definition that contains
 * two <code>PlacementModel</code>s which are used to determine the position of
 * the nodes. The network uses a layered model, first, one of the placement
 * models is used to generate the position of a number of hotspots in which the
 * nodes are then placed by the second placement model. The number of nodes in
 * each hotspot is determined by a <code>Partitioner</code>. After all nodes
 * have been placed, they are connected by a <code>NodeConnector</code>, for
 * example a simple <code>UDGConnector</code>.
 * 
 * 
 * @author Philipp Neubrand
 * 
 */
public class PlacementModelContainer extends Network {
	private int nodes;
	private int hotspots;
	private Partitioner partitioner;
	private PlacementModel hotspotPlacer;
	private PlacementModel nodePlacer;
	private NodeConnector connector;
	private PlaneIdentifierSpaceSimple idSpace;
	private double nodeBoxHeight;
	private double nodeBoxWidth;
	private double hotspotBoxHeight;
	private double hotspotBoxWidth;

	/**
	 * Standard and only constructor for this class, gets all needed classes and
	 * all the needed information.
	 * 
	 * @param nodes
	 *            The number of nodes in the network.
	 * @param hotspots
	 *            The number of hotspots in the network.
	 * @param nodeBoxWidth
	 *            The width of the field in which nodes can be placed.
	 * @param nodeBoxHeight
	 *            The height of the field in which nodes can be placed.
	 * @param hotspotBoxWidth
	 *            The width of the field in which hotspots can be placed. Is
	 *            assumed to be =< nodeBoxWidth. The hotspotBox is centered at
	 *            the center of the nodeBox and therefore as the coordinates
	 *            (nodeBoxWidth/2 - hotspotBoxWidth/2, nodeBoxHeight/2 -
	 *            hotspotBoxHeight/2)-(nodeBoxWidth/2 + hotspotBoxWidth/2,
	 *            nodeBoxHeight/2 + hotspotBoxHeight/2).
	 * @param hotspotBoxHeight
	 *            The height of the field in which hotspots can be placed. Is
	 *            assumed to be =< nodeBoxHeight. The hotspotBox is centered at
	 *            the center of the nodeBox and therefore as the coordinates
	 *            (nodeBoxWidth/2 - hotspotBoxWidth/2, nodeBoxHeight/2 -
	 *            hotspotBoxHeight/2)-(nodeBoxWidth/2 + hotspotBoxWidth/2,
	 *            nodeBoxHeight/2 + hotspotBoxHeight/2).
	 * @param hotspotPlacer
	 *            The <code>PlacementModel</code> used to place the hotspots.
	 * @param nodePlacer
	 *            The <code>PlacementModel</code> used to place the nodes within
	 *            the hotspots.
	 * @param partitioner
	 *            The <code>Partitioner</code> used to determine how much nodes
	 *            are placed in each hotspot.
	 * @param nodeConnector
	 *            The <code>NodeConnector</code> used to connect nodes depending
	 *            on their position.
	 * @param t
	 *            The transformations that should be applied to the graph of the
	 *            network.
	 */
	public PlacementModelContainer(int nodes, int hotspots,
			double nodeBoxWidth, double nodeBoxHeight, double hotspotBoxWidth,
			double hotspotBoxHeight, PlacementModel hotspotPlacer,
			PlacementModel nodePlacer, Partitioner partitioner,
			NodeConnector nodeConnector, Transformation[] t) {
		super("HSM", nodes, Util.mergeArrays(new Parameter[] {
				new StringParameter("KEY", getKey(hotspotPlacer, nodePlacer)),
				new IntParameter("HOTSPOTS", hotspots),
				new DoubleParameter("NODEBOX_WIDTH", nodeBoxWidth),
				new DoubleParameter("NODEBOX_HEIGHT", nodeBoxHeight),
				new DoubleParameter("HOTSPOTBOX_WIDTH", hotspotBoxWidth),
				new DoubleParameter("HOTSPOTBOX_HEIGHT", hotspotBoxHeight) },
				Util.mergeArrays(Util.addPrefix("PART_",
						partitioner.getConfigParameters()), Util.mergeArrays(
						Util.addPrefix("CON_",
								nodeConnector.getConfigParameters()), Util
								.mergeArrays(Util.addPrefix("HP_",
										hotspotPlacer.getConfigParameters()),
										Util.addPrefix("NP_", nodePlacer
												.getConfigParameters()))))), t);
		this.nodes = nodes;
		this.hotspots = hotspots;
		this.hotspotPlacer = hotspotPlacer;
		this.nodePlacer = nodePlacer;
		this.partitioner = partitioner;
		this.connector = nodeConnector;
		this.nodeBoxWidth = nodeBoxWidth;
		this.nodeBoxHeight = nodeBoxHeight;
		this.hotspotBoxWidth = hotspotBoxWidth;
		this.hotspotBoxHeight = hotspotBoxHeight;
	}

	/**
	 * Constructs the key of this network, which will be
	 * HSM_<hotspotPlacer.getKey()>_<nodePlacer.getKey()>.
	 * 
	 * @param hotspotPlacer
	 *            The <code>PlacementModel</code> used to place the hotspots.
	 * @param nodePlacer
	 *            The <code>PlacementModel</code> used to place the nodes within
	 *            the hotspots.
	 * @return The key of this network.
	 */
	private static String getKey(PlacementModel hotspotPlacer,
			PlacementModel nodePlacer) {
		return "HSM_" + hotspotPlacer.getKey() + "_" + nodePlacer.getKey();
	}

	/**
	 * Creates the graph for this network by first placing the hotspots, then
	 * placing the nodes within those hotspots. The coordinates of the nodes are
	 * stored in an <code>IdentifierSpace</code>, which is added as "ID_SPACE_0"
	 * to the attributes of the graph. In the end, the nodes are connected based
	 * on that id-space.
	 */
	@Override
	public Graph generate() {
		// get hotspot coordinates
		Point center = new Point(getNodeBoxWidth() / 2, getNodeBoxHeight() / 2);
		Point[] hotspotCoords = hotspotPlacer.place(hotspots, center, center,
				getHotspotBoxWidth(), getHotspotBoxHeight());

		// Prepare ID space
		PlanePartitionSimple[] coords = new PlanePartitionSimple[nodes];
		idSpace = new PlaneIdentifierSpaceSimple(coords, getNodeBoxWidth(),
				getNodeBoxHeight(), false);

		// get assignment of nodes to spots
		int[] nodesPerSpot = partitioner.partition(nodes, hotspots);

		// place the nodes for every Hotspot and add the coordinates to the
		// IDSpace
		int temp = 0;
		int curNodes;
		for (int i = 0; i < hotspots; i++) {
			curNodes = nodesPerSpot[i];
			addNodeCoords(coords, nodePlacer.place(curNodes, hotspotCoords[i],
					center, getNodeBoxWidth(), getNodeBoxHeight()), temp);
			temp += curNodes;
		}

		// create the required Graph object and fill it
		Graph g = new Graph(this.getDescription());

		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);

		Node[] nodes = Node.init(this.getNodes(), g);
		connector.connect(nodes, idSpace, g);

		g.setNodes(nodes);

		return g;
	}

	private double getHotspotBoxWidth() {
		return hotspotBoxWidth;
	}

	private double getHotspotBoxHeight() {
		return hotspotBoxHeight;
	}

	/**
	 * Getter for the width.
	 * 
	 * @return The width.
	 */
	private double getNodeBoxWidth() {
		return nodeBoxWidth;
	}

	/**
	 * Getter for the height.
	 * 
	 * @return The height.
	 */
	private double getNodeBoxHeight() {
		return nodeBoxHeight;
	}

	/**
	 * Convenience method to convert all the node coordinates from
	 * <code>Point</code> objects to <code>PlanePartitionSimple</code> objects.
	 * In the process, the supplied offset is added to all those coordinates.
	 * Instead of returning a newly created array, the
	 * <code>PlanePartitionSimple</code>s are stored in the supplied array,
	 * starting from the supplied offset. If the array is shorter than required,
	 * an Exception will be thrown.
	 * 
	 * @param pps
	 *            The array to which the resulting
	 *            <code>PlanePartitionSimple</code>s are to be written, starting
	 *            at <code>arrayOffset</code>.
	 * @param nodeCoords
	 *            The coordinates at which nodes are to be placed.
	 * @param arrayOffset
	 *            The offset for the array, pointing to the first EMPTY element.
	 */
	private void addNodeCoords(PlanePartitionSimple[] pps, Point[] nodeCoords,
			int arrayOffset) {
		for (int i = 0; i < nodeCoords.length; i++) {
			pps[i + arrayOffset] = new PlanePartitionSimple(
					new PlaneIdentifier(nodeCoords[i].getX(),
							nodeCoords[i].getY(), idSpace.getxModulus(),
							idSpace.getyModulus(), idSpace.isWrapAround()));
		}
	}
}

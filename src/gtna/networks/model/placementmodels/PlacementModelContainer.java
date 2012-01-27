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
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Util;

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
public class PlacementModelContainer extends NetworkImpl {
	private int nodes;
	private int hotspots;
	private Partitioner partitioner;
	private PlacementModel hotspotPlacer;
	private PlacementModel nodePlacer;
	private NodeConnector connector;
	private PlaneIdentifierSpaceSimple idSpace;

	/**
	 * Standard and only constructor for this class, gets all needed classes and
	 * all the needed information.
	 * 
	 * @param nodes
	 *            The number of nodes in the network.
	 * @param hotspots
	 *            The number of hotspots in the network.
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
	 * @param r
	 *            The routing to be used in the network.
	 * @param t
	 *            The transformations that should be applied to the graph of the
	 *            network.
	 */
	public PlacementModelContainer(int nodes, int hotspots,
			PlacementModel hotspotPlacer, PlacementModel nodePlacer,
			Partitioner partitioner, NodeConnector nodeConnector,
			RoutingAlgorithm r, Transformation[] t) {
		super("HSM", nodes, getConfigKeys(hotspotPlacer, nodePlacer,
				nodeConnector, partitioner), getConfigValues(hotspotPlacer,
				nodePlacer, nodeConnector, partitioner), r, t);
		this.nodes = nodes;
		this.hotspots = hotspots;
		this.hotspotPlacer = hotspotPlacer;
		this.nodePlacer = nodePlacer;
		this.partitioner = partitioner;
		this.connector = nodeConnector;

		idSpace = new PlaneIdentifierSpaceSimple(null,
				hotspotPlacer.getWidth(), hotspotPlacer.getHeight(), false);
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
	 * Constructs an array containing all the configuration values used for the
	 * particular <code>PlacementModelContainer</code>. Basically combines all
	 * the configuration values of both <code>PlacementModels</code>, the
	 * <code>Partitioner</code> and the <code>NodeConnector</code> into one big
	 * string array and adds the key of the <code>PlacementModelContainer</code>
	 * to this array.
	 * 
	 * @param hotspotPlacer
	 *            The <code>PlacementModel</code> used to place the hotspots.
	 * @param nodePlacer
	 *            The <code>PlacementModel</code> used to place the nodes within
	 *            the hotspots.
	 * @param partitioner
	 *            The <code>Partitioner</code> used to determine the number of
	 *            nodes in each hotspot.
	 * @param nodeConnector
	 *            The <code>NodeConnector</code> used to connect the nodes after
	 *            placing them.
	 * @return A string array containg all the configuration values.
	 */
	private static String[] getConfigValues(PlacementModel hotspotPlacer,
			PlacementModel nodePlacer, NodeConnector connector,
			Partitioner partitioner) {
		return Util.mergeArrays(
				new String[] { getKey(hotspotPlacer, nodePlacer) },
				Util.mergeArrays(partitioner.getConfigValues(), Util
						.mergeArrays(connector.getConfigValues(), Util
								.mergeArrays(hotspotPlacer.getConfigValues(),
										nodePlacer.getConfigValues()))));
	}

	/**
	 * Constructs an array containing all the configuration keys used for the
	 * particular <code>PlacementModelContainer</code>. Basically combines all
	 * the configuration keys of both <code>PlacementModels</code> (prefixed by
	 * "HP_" and "NP_" respectively), the <code>Partitioner</code> (prefixed by
	 * "PART_") and the <code>NodeConnector</code> (prefixed by "NC_") into one
	 * big string array and adds "KEY" without a prefix to it.
	 * 
	 * @param hotspotPlacer
	 *            The <code>PlacementModel</code> used to place the hotspots.
	 * @param nodePlacer
	 *            The <code>PlacementModel</code> used to place the nodes within
	 *            the hotspots.
	 * @param partitioner
	 *            The <code>Partitioner</code> used to determine the number of
	 *            nodes in each hotspot.
	 * @param nodeConnector
	 *            The <code>NodeConnector</code> used to connect the nodes after
	 *            placing them.
	 * @return A string array containg all the configuration keys.
	 */
	private static String[] getConfigKeys(PlacementModel hotspotPlacer,
			PlacementModel nodePlacer, NodeConnector connector,
			Partitioner partitioner) {
		return Util.mergeArrays(new String[] { "KEY" }, Util.mergeArrays(
				Util.addPrefix("PART_", partitioner.getConfigKeys()),
				Util.mergeArrays(
						Util.addPrefix("CON_", connector.getConfigKeys()),
						Util.mergeArrays(
								Util.addPrefix("HP_",
										hotspotPlacer.getConfigKeys()),
								Util.addPrefix("NP_",
										nodePlacer.getConfigKeys())))));
	}

	/**
	 * Creates the graph for this network by first placing the hotspots, then
	 * placing the nodes within those hotspots. The coordinates of the nodes are
	 * stored in an <code>IdentifierSpace</code>, which is added as "id_space_0"
	 * to the attributes of the graph. In the end, the nodes are connected based
	 * on that id-space.
	 */
	@Override
	public Graph generate() {
		// get hotspot coordinates
		Point[] hotspotCoords = hotspotPlacer.place(hotspots);

		// Prepare ID space
		PlanePartitionSimple[] coords = new PlanePartitionSimple[nodes];

		// get assignment of nodes to spots
		int[] nodesPerSpot = partitioner.partition(nodes, hotspots);

		// place the nodes for every Hotspot and add the coordinates to the
		// IDSpace
		int temp = 0;
		int curNodes;
		for (int i = 0; i < hotspots; i++) {
			curNodes = nodesPerSpot[i];
			addNodeCoords(coords, nodePlacer.place(curNodes), hotspotCoords[i],
					temp);
			temp += curNodes;
		}

		// create the required Graph object and fill it
		Graph g = new Graph(description());

		idSpace.setPartitions(coords);

		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);

		Node[] nodes = Node.init(this.nodes(), g);
		connector.connect(nodes, idSpace);

		g.setNodes(nodes);

		return g;
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
	 * @param coordOffset
	 *            The offset for the coordinates.
	 * @param arrayOffset
	 *            The offset for the array, pointing to the first EMPTY element.
	 */
	private void addNodeCoords(PlanePartitionSimple[] pps, Point[] nodeCoords,
			Point coordOffset, int arrayOffset) {
		for (int i = 0; i < nodeCoords.length; i++) {
			pps[i + arrayOffset] = new PlanePartitionSimple(
					new PlaneIdentifier(nodeCoords[i].getX()
							+ coordOffset.getX(), nodeCoords[i].getY()
							+ coordOffset.getY(), idSpace));
		}
	}
}

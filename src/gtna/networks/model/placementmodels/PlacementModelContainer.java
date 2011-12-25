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
 * @author Flipp
 * 
 */
public class PlacementModelContainer extends NetworkImpl {
	private int nodes;
	private int hotspots;
	private AbstractPartitioner partitioner;
	private AbstractPlacementModel hotspotPlacer;
	private AbstractPlacementModel nodePlacer;
	private AbstractNodeConnector connector;
	private PlaneIdentifierSpaceSimple idSpace;

	public PlacementModelContainer(int nodes, int hotspots,
			AbstractPlacementModel hotspotPlacer,
			AbstractPlacementModel nodePlacer, AbstractPartitioner partitioner,
			AbstractNodeConnector nodeConnector, RoutingAlgorithm r,
			Transformation[] t) {
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
	 * @param hotspotPlacer
	 * @param nodePlacer
	 * @return
	 */
	private static String getKey(AbstractPlacementModel hotspotPlacer,
			AbstractPlacementModel nodePlacer) {
		return "HSM_" + hotspotPlacer.getKey() + "_" + nodePlacer.getKey();
	}

	/**
	 * @param hotspotPlacer
	 * @param nodePlacer
	 * @param partitioner2
	 * @param nodeConnector
	 * @return
	 */
	private static String[] getConfigValues(
			AbstractPlacementModel hotspotPlacer,
			AbstractPlacementModel nodePlacer, AbstractNodeConnector connector,
			AbstractPartitioner partitioner) {
		return Util.mergeArrays(new String[] { "KEY" }, Util.mergeArrays(
				partitioner.getConfigValues(), Util.mergeArrays(connector
						.getConfigValues(), Util.mergeArrays(hotspotPlacer
						.getConfigValues(), nodePlacer.getConfigValues()))));
	}

	/**
	 * @param hotspotPlacer
	 * @param nodePlacer
	 * @return
	 */
	private static String[] getConfigKeys(AbstractPlacementModel hotspotPlacer,
			AbstractPlacementModel nodePlacer, AbstractNodeConnector connector,
			AbstractPartitioner partitioner) {
		return Util.mergeArrays(
				new String[] { getKey(hotspotPlacer, nodePlacer) }, Util
						.mergeArrays(Util.addPrefix("PART_", partitioner
								.getConfigKeys()), Util.mergeArrays(Util
								.addPrefix("CON_", connector.getConfigKeys()),
								Util.mergeArrays(Util.addPrefix("HP_",
										hotspotPlacer.getConfigKeys()), Util
										.addPrefix("NP_", nodePlacer
												.getConfigKeys())))));
	}

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

		g.addProperty("id_space_0", idSpace);

		Node[] nodes = Node.init(this.nodes(), g);
		connector.connect(nodes, idSpace);

		g.setNodes(nodes);

		return g;
	}

	/**
	 * @param pps
	 * @param nodeCoords
	 * @param ds
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

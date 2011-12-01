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
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public abstract class AbstractHotspotModel extends AbstractPlacementModel
		implements Network {
	private double sigma;
	private boolean inCenter;
	private double spotWidth;
	private double spotHeight;
	private int cur = 0;
	protected int spots;

	/**
	 * Prefix 
	 * @param prefix
	 * @param spots
	 * @param nodesperspot
	 * @param overallWidth
	 * @param overallHeight
	 * @param spotWidth
	 * @param spotHeight
	 * @param sigma
	 * @param inCenter
	 * @param keys
	 * @param vals
	 * @param nc
	 * @param ra
	 * @param t
	 */
	public AbstractHotspotModel(String prefix, int spots, int nodesperspot,
			double overallWidth, double overallHeight, double spotWidth,
			double spotHeight, double sigma, boolean inCenter, String[] keys,
			String[] vals, NodeConnector nc, RoutingAlgorithm ra,
			Transformation[] t) {
		super(prefix + "HOTSPOTMODEL", nodesperspot * spots, overallWidth,
				overallHeight, addToArray(keys,
										new String[] { "SPOTS", "SPOT_WIDTH", "SPOT_HEIGHT" }),
				addToArray(vals,
						new String[] { Integer.toString(spots),
								Double.toString(spotWidth),
								Double.toString(spotHeight) }), nc, ra, t);
		this.sigma = sigma;
		this.spotWidth = spotWidth;
		this.spotHeight = spotHeight;
		this.inCenter = inCenter;
		this.spots = spots;
		setCoords(new PlaneIdentifierSpaceSimple(null, overallWidth,
				overallHeight, false));
	}

	/**
	 * Generates the graph, overwritten method of the default network
	 * implementation. First, the position of all the HotSpots is determined by
	 * calling the abstract <i>getHotspots(...)</i> method. This method is to be
	 * implemented by the extending subclasses. Then, the nodes are distributed
	 * around the Hotspots using the <i>CommunityModel</i>.
	 * 
	 * The x and y values of each hotspot are taken to describe the bottom left
	 * corner for the <i>CommunityModel</i>, while using spotWidth and
	 * spotHeight as their width and height. Therefore, the coordinates of the
	 * hotspots need to be adjusted if they are close to either of the borders
	 * (x and y axis as well as overallWidth and overallHeight) so that no nodes
	 * are placed outside of the allowed field. This is done by adjusting the
	 * coordinates of each hotspot to be between (0, 0) and
	 * ((overallWidth-spotWidth), (overallHeight-spotHeight)).
	 */
	public Graph generate() {
		PlanePartitionSimple[] hotspots = getHotspots(getCoords());
		double x = 0;
		double y = 0;

		PlanePartitionSimple[] ret = new PlanePartitionSimple[nodes()];
		double maxX = getWidth()-spotWidth;
		double maxY = getHeight()-spotHeight;
		System.out.println(hotspots.length);
		for (int i = 0; i < hotspots.length; i++) {
			System.out.println(hotspots[i]);
			x = hotspots[i].getId().getX();
			if(x > maxX)
				x = maxX;
			
			y = hotspots[i].getId().getY();
			if(y > maxY)
				y = maxY;

			addAll(ret, Placement.placeByCommunityModel(spotWidth, spotHeight, nodes()/spots,
					sigma, inCenter, getCoords()), x, y);

		}

		this.getCoords().setPartitions(ret);

		return finish();
	}

	private void addAll(PlanePartitionSimple[] ret, PlanePartitionSimple[] temp, double xoffset, double yoffset) {
		for (int i = 0; i < temp.length; i++) {
			ret[cur + i] = temp[i];
			ret[cur+i].getId().setX(ret[cur+i].getId().getX() + xoffset);
			ret[cur+i].getId().setY(ret[cur+i].getId().getY() + yoffset);
		}
		cur += temp.length;
	}

	/**
	 * @param idspace
	 * @return
	 */
	protected abstract PlanePartitionSimple[] getHotspots(
			PlaneIdentifierSpaceSimple idspace);

}

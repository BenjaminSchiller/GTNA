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
 * CommunityPlacementModel.java
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
package gtna.networks.model.placementmodels.models;

import gtna.networks.model.placementmodels.PlacementModelImpl;
import gtna.networks.model.placementmodels.PlacementNotPossibleException;
import gtna.networks.model.placementmodels.Point;

import java.util.Random;

/**
 * @author Flipp
 * 
 */
public class CommunityPlacementModel extends PlacementModelImpl {

	private boolean inCenter;
	private double sigma;
	private final int maxTries = 100;

	/**
	 * 
	 * @param width
	 *            The width of the field in which the nodes are to be
	 *            distributed.
	 * @param height
	 *            The height of the field in which the nodes are to be
	 *            distributed.
	 * @param sigma
	 *            The variance of the gaussian distribution used to distribute
	 *            nodes within the field. The nodes are placed at
	 *            <code> (getWidth() / 2) + Math.nextGaussian() * sigma * (getWidth() / 2)</code>
	 *            (similar with height).
	 * @param inCenter
	 *            If a node should be placed in the center of the model.
	 */
	public CommunityPlacementModel(double width, double height, double sigma,
			boolean inCenter) {
		setWidth(width);
		setHeight(height);
		this.sigma = sigma;
		this.inCenter = inCenter;
		setKey("COMMUNITY");

		setAdditionalConfigKeys(new String[] { "SIGMA", "IN_CENTER" });
		setAdditionalConfigValues(new String[] { Double.toString(sigma),
				Boolean.toString(inCenter) });
	}

	@Override
	public Point[] place(int count) {
		Random rnd = new Random();

		Point[] ret = new Point[count];

		int i = 0;
		double dx = 0;
		double dy = 0;
		int tries;
		double centerx = getWidth() / 2;
		double centery = getHeight() / 2;

		if (inCenter) {
			ret[0] = new Point(centerx, centery);
			i++;
		}

		while (i < count) {

			tries = 0;
			do {
				dx = centerx + rnd.nextGaussian() * sigma * centerx;
				dy = centery + rnd.nextGaussian() * sigma * centery;
				tries++;
			} while ((dx < 0 || dx > getWidth() || dy < 0 || dy > getHeight())
					&& tries <= maxTries);
			if (tries > maxTries)
				throw new PlacementNotPossibleException("Could not place node "
						+ i + " for settings: F=(" + getWidth() + ", "
						+ getHeight() + "), count=" + count + ", sigma="
						+ sigma + ", inCenter=" + inCenter);

			ret[i] = new Point(dx, dy);

			i++;
		}

		return ret;
	}

}

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
 * Places the nodes normally distributed within the field of the
 * <code>PlacementModel</code> with variance <code>sigma</code>.
 * 
 * @author Philipp Neubrand
 * 
 */
public class CommunityPlacementModel extends PlacementModelImpl {
	private double sigma;
	private double height;
	private double width;

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
	 * 
	 *            <code> (getWidth() / 2) + Math.nextGaussian() * sigma * (getWidth() / 2)</code>
	 *            (similar with height).
	 * @param inCenter
	 *            If a node should be placed in the center of the model.
	 */
	public CommunityPlacementModel(double width, double height, double sigma,
			boolean inCenter) {
		this.sigma = sigma;
		setInCenter(inCenter);
		this.width = width;
		this.height = height;
		setKey("COMMUNITY");

		setAdditionalConfigKeys(new String[] { "SIGMA", "WIDTH", "HEIGHT" });
		setAdditionalConfigValues(new String[] { Double.toString(sigma), Double.toString(width), Double.toString(height) });
	}

	/**
	 * Places the nodes normally distributed within the field of the
	 * <code>PlacementModel</code> with variance <code>sigma</code>.
	 */
	@Override
	public Point[] place(int count, Point center, double maxX, double maxY) {
		Random rnd = new Random();

		Point[] ret = new Point[count];

		int i = 0;
		double dx = 0;
		double dy = 0;
		int tries;

		if (getInCenter()) {
			ret[0] = new Point(center.getX(), center.getY());
			i++;
		}

		while (i < count) {

			tries = 0;
			do {
				dx = center.getX() + rnd.nextGaussian() * sigma * width;
				dy = center.getY() + rnd.nextGaussian() * sigma * height;
				tries++;
			} while ((dx < 0 || dx > maxX || dy < 0 || dy > maxY)
					&& tries <= maxTries);
			if (tries > maxTries)
				throw new PlacementNotPossibleException("Could not place node "
						+ i + " for settings: F=(" + width + ", "
						+ height + "), count=" + count + ", sigma="
						+ sigma + ", inCenter=" + getInCenter());

			ret[i] = new Point(dx, dy);

			i++;
		}

		return ret;
	}

}

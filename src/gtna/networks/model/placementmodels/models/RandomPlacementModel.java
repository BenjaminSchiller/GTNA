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
 * RandomPlacementModel.java
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
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * Places the nodes uniformly in the field.
 * 
 * @author Philipp Neubrand
 * 
 */
public class RandomPlacementModel extends PlacementModelImpl {
	private double width;
	private double height;

	/**
	 * 
	 * @param width
	 *            The width of the field in which the nodes are to be
	 *            distributed.
	 * @param height
	 *            The height of the field in which the nodes are to be
	 *            distributed.
	 * @param inCenter
	 *            If a node should be placed in the center of the model.
	 */
	public RandomPlacementModel(double width, double height, boolean inCenter) {
		this.width = width;
		this.height = height;
		setInCenter(inCenter);
		setKey("RANDOM");
		setAdditionalConfigParameters(new Parameter[] {
				new DoubleParameter("WIDTH", width),
				new DoubleParameter("HEIGHT", height) });
	}

	/**
	 * Places the nodes uniformly in the field.
	 */
	@Override
	public Point[] place(int count, Point placementCenter, Point boxCenter, double boxWidth, double boxHeight) {
		Random rnd = new Random();

		double dx = 0;
		double dy = 0;
		int i = 0;

		Point[] ret = new Point[count];

		if (getInCenter()) {
			ret[0] = new Point(placementCenter.getX(), placementCenter.getY());
			i++;
		}
		int tries;

		while (i < count) {
			tries = 0;
			do {
				double x = (this.width / 2.0)
						* (rnd.nextBoolean() ? 1.0 : -1.0);
				double y = (this.height / 2.0)
						* (rnd.nextBoolean() ? 1.0 : -1.0);
				dx = placementCenter.getX() + x * (rnd.nextDouble());
				dy = placementCenter.getY() + y * (rnd.nextDouble());
				tries++;
			} while (!inBounds(dx,dy, boxCenter, boxWidth, boxHeight)
					&& tries <= maxTries);

			if (tries > maxTries)
				throw new PlacementNotPossibleException("Could not place node "
						+ i + " for settings: F=(" + width + ", " + height
						+ "), count=" + count + ", inCenter=" + getInCenter());

			ret[i] = new Point(dx, dy);

			i++;
		}

		return ret;
	}

}

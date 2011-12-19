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

import gtna.networks.model.placementmodels.AbstractPlacementModel;
import gtna.networks.model.placementmodels.PlacementNotPossibleException;
import gtna.networks.model.placementmodels.Point;

import java.util.Random;

/**
 * @author Flipp
 * 
 */
public class CommunityPlacementModel extends AbstractPlacementModel {

	private boolean inCenter;
	private double sigma;
	private final int maxTries = 100;

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

		if (inCenter) {
			ret[0] = new Point((getWidth() / 2), (getHeight() / 2));
			i++;
		}
		while (i < count) {

			tries = 0;
			do {
				dx = rnd.nextGaussian() * sigma;
				dy = rnd.nextGaussian() * sigma;
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

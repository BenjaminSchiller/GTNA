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
 * CirclePlacementModel.java
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
import gtna.networks.model.placementmodels.DistributionType;
import gtna.networks.model.placementmodels.PlacementNotPossibleException;
import gtna.networks.model.placementmodels.Point;

import java.util.Random;

/**
 * @author Flipp
 * 
 */
public class CirclePlacementModel extends AbstractPlacementModel {

	private double radius;
	private DistributionType oalpha;
	private DistributionType od;
	private final int maxTries = 100;

	/**
	 * @param width
	 *            The width of the field in which the nodes are to be placed.
	 * @param height
	 *            The height of the field in which the nodes are to be placed.
	 * @param radius
	 *            The radius of the circle on which the nodes are to be placed.
	 * @param oalpha
	 *            The distribution type of alpha. "FIXED" will place node i at
	 *            <code>i*(360/nodes)</code> degrees. "UNIFORM" will place nodes
	 *            uniformly distributed around the center determined by the
	 *            "FIXED" distribution. "NORMAL" will place nodes normal
	 *            distributed around the center determined by the "FIXED"
	 *            distribution.
	 * @param od
	 *            The distribution type of the radius. If "FIXED", all nodes are
	 *            placed at distance "r" from the center. If "UNIFORM", the
	 *            distance from the center is uniformly distributed, if "NORMAL"
	 *            it is normally distributed.
	 */
	public CirclePlacementModel(double width, double height, double radius,
			DistributionType oalpha, DistributionType od) {
		setWidth(width);
		setHeight(height);
		this.radius = radius;
		this.oalpha = oalpha;
		this.od = od;
		setKey("CIRCLE");
		setAdditionalConfigKeys(new String[] { "RADIUS", "OALPHA", "OD" });
		setAdditionalConfigValues(new String[] { Double.toString(radius),
				oalpha.toString(), od.toString() });
	}

	@Override
	public Point[] place(int count) {
		Random rnd = new Random();
		Point[] ret = new Point[count];
		double gamma = (2 * Math.PI) / (double) count;
		double alpha = 0;
		double d = 0;
		double x, y;
		double centerx = getWidth() / 2;
		double centery = getHeight() / 2;

		int tries;

		for (int i = 0; i < count; i++) {
			tries = 0;
			do {

				switch (oalpha) {
				case FIXED:
					alpha = (i + 0.5) * gamma;
					break;
				case UNIFORM:
					alpha = i * gamma + rnd.nextDouble() * gamma;
					break;
				case NORMAL:
					alpha = (i + 0.5) * gamma + rnd.nextGaussian()
							* (gamma / 6);
					break;
				}

				switch (od) {
				case FIXED:
					d = radius;
					break;
				case UNIFORM:
					d = rnd.nextDouble() * 2 * radius;
					break;
				case NORMAL:
					d = radius + rnd.nextGaussian() * (radius / 3);
					break;
				}

				x = centerx + d * Math.cos(alpha);
				System.out.println(d);
				y = centery + d * Math.sin(alpha);
				tries++;
			} while ((x < 0 || x > getWidth() || y < 0 || y > getHeight())
					&& tries <= maxTries);
			if (tries > maxTries)
				throw new PlacementNotPossibleException("Could not place node "
						+ i + " for settings: center=(" + centerx + ", "
						+ centery + "), radius=" + radius + ", F=("
						+ getWidth() + ", " + getHeight() + "), count=" + count
						+ ", Distribs=(" + oalpha + ", " + od + ")");
			System.out.println("Setting to " + x + " " + y);
			ret[i] = new Point(x, y);

		}

		return ret;
	}

}

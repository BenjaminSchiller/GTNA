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

import gtna.networks.model.placementmodels.PlacementModelImpl;
import gtna.networks.model.placementmodels.PlacementNotPossibleException;
import gtna.networks.model.placementmodels.Point;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;

/**
 * A <code>CirclePlacementModel</code> places the nodes around the center of the
 * field at radius <code>r</code>.
 * 
 * @author Philipp Neubrand
 * 
 */
public class CirclePlacementModel extends PlacementModelImpl {
	/**
	 * Identifies the distribution type for alpha and the radius.
	 * <ul>
	 * <li><code>FIXED</code>: will not vary those values at all: nodes will be
	 * placed exactly at distance r every (360/n) degrees (angle = (i+0.5) *
	 * (360/n)).</li>
	 * <li><code>UNIFORM</code>: will distribute the angle uniformly around the
	 * center of their interval (angle = i * angle + angle * rnd.nextDouble()),
	 * the radius uniformly between 0 and 2r.</li>
	 * <li><code>NORMAL</code>: will distribute the angle normal around the
	 * center of their interval with variance (gamma/6) and r normal around r
	 * with variance (r/3)</li>
	 * </ul>
	 * 
	 * @author Philipp Neubrand
	 * 
	 */
	public enum DistributionType {
		NORMAL, FIXED, UNIFORM
	}

	private double radius;
	private DistributionType oalpha;
	private DistributionType od;

	/**
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
	 * @param inCenter
	 *            If set to <code>true</code> will place a node in the center of
	 *            the circle.
	 */
	public CirclePlacementModel(double radius, DistributionType oalpha,
			DistributionType od, boolean inCenter) {
		this.radius = radius;
		this.oalpha = oalpha;
		setInCenter(inCenter);
		this.od = od;
		setKey("CIRCLE");
		setAdditionalConfigParameters(new Parameter[] {
				new DoubleParameter("RADIUS", radius),
				new StringParameter("OALPHA", oalpha.toString()),
				new StringParameter("OD", od.toString()) });
	}

	/**
	 * Places the nodes on a circle around the center of the field.
	 */
	@Override
	public Point[] place(int count, Point center, Point boxCenter, double boxWidth, double boxHeight) {
		Random rnd = new Random();
		Point[] ret = new Point[count];
		int i = 0;
		if (getInCenter()) {
			ret[0] = new Point(center.getX(), center.getY());
			i = 1;
		}
		double gamma = (2 * Math.PI) / (double) count;
		double alpha = 0;
		double d = 0;
		double x, y;

		int tries;
		
		while(i < count) {
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

				x = center.getX() + d * Math.cos(alpha);
				y = center.getY() + d * Math.sin(alpha);
				tries++;
			} while (!inBounds(x, y, boxCenter, boxWidth, boxHeight)
					&& tries <= maxTries);
			if (tries > maxTries)
				throw new PlacementNotPossibleException("Could not place node "
						+ i + " for settings: center=(" + center.getX() + ", "
						+ center.getY() + "), radius=" + radius + ", F=("
						+ boxWidth + ", " + boxHeight + "), count=" + count
						+ ", Distribs=(" + oalpha + ", " + od + ")");
			ret[i] = new Point(x, y);
			i++;
		}

		return ret;
	}

}

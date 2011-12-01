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

import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;

import java.util.Random;

/**
 * @author Flipp
 * 
 */
public class Placement {
	private static Random rnd = new Random();

	private static final int maxTries = 100;

	public static PlanePartitionSimple[] placeByCommunityModel(double width,
			double height, int count, double sigma, boolean inCenter,
			PlaneIdentifierSpaceSimple idspace) {
		PlanePartitionSimple[] ret = new PlanePartitionSimple[count];

		int i = 0;
		double dx = 0;
		double dy = 0;
		int tries;

		if (inCenter) {
			ret[0] = new PlanePartitionSimple(new PlaneIdentifier((width / 2),
					(height / 2), idspace));
			i++;
		}
		while (i < count) {

			tries = 0;
			do {
				dx = rnd.nextGaussian() * sigma;
				dy = rnd.nextGaussian() * sigma;
				tries++;
			} while ((dx < 0 || dx > width || dy < 0 || dy > height)
					&& tries <= maxTries);
			if (tries > maxTries)
				throw new PlacementNotPossibleException("Could not place node "
						+ i + " for settings: F=(" + width + ", " + height
						+ "), count=" + count + ", sigma=" + sigma
						+ ", inCenter=" + inCenter);

			ret[i] = new PlanePartitionSimple(new PlaneIdentifier(dx, dy,
					idspace));

			i++;
		}

		return ret;
	}

	public static PlanePartitionSimple[] placeByRandomModel(double width,
			double height, int count, boolean inCenter,
			PlaneIdentifierSpaceSimple idspace) {
		PlanePartitionSimple[] ret = new PlanePartitionSimple[count];
		double dx = 0;
		double dy = 0;
		int i = 0;

		if (inCenter) {
			ret[0] = new PlanePartitionSimple(new PlaneIdentifier((width / 2),
					(height / 2), idspace));
			i++;
		}

		while (i < count) {
			dx = width * (rnd.nextDouble() - 0.5);
			dy = height * (rnd.nextDouble() - 0.5);

			ret[i] = new PlanePartitionSimple(new PlaneIdentifier(dx, dy,
					idspace));

			i++;
		}

		return ret;
	}

	public static PlanePartitionSimple[] placeByGridModel(double width,
			double height, int cols, int rows,
			PlaneIdentifierSpaceSimple idspace) {
		PlanePartitionSimple[] ret = new PlanePartitionSimple[cols * rows];
		double xoffset = width / cols;
		double yoffset = height / rows;

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				System.out.println("Creating");
				ret[(i*cols)+j] = new PlanePartitionSimple(new PlaneIdentifier(i
						* xoffset, j * yoffset, idspace));
			}
		}

		return ret;
	}

	public static PlanePartitionSimple[] placeByCircleModel(double centerx,
			double centery, double radius, double width, double height,
			int count, DistributionType oalpha, DistributionType od,
			PlaneIdentifierSpaceSimple idspace) {
		PlanePartitionSimple[] ret = new PlanePartitionSimple[count];
		double gamma = (2 * Math.PI) / (double) count;
		double alpha = 0;
		double d = 0;
		double x;
		double y;

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
					d = radius
							+ rnd.nextGaussian()
									* (radius / 3);
					break;
				}
				
				x = centerx + d * Math.cos(alpha);
				System.out.println(d);
				y = centery + d * Math.sin(alpha);
				tries++;
			} while ((x < 0 || x > width || y < 0 || y > height)
					&& tries <= maxTries);
			if (tries > maxTries)
				throw new PlacementNotPossibleException("Could not place node "
						+ i + " for settings: center=(" + centerx + ", "
						+ centery + "), radius=" + radius + ", F=(" + width
						+ ", " + height + "), count=" + count + ", Distribs=("
						+ oalpha + ", " + od + ")");
			System.out.println("Setting to " + x +" " +y);
			ret[i] = new PlanePartitionSimple(
					new PlaneIdentifier(x, y, idspace));

		}

		return ret;
	}

}

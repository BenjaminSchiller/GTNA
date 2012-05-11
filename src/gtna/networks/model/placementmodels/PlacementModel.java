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
 * IPlacementModel.java
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

import gtna.util.parameter.Parameter;

/**
 * A <code>PlacementModel</code> is a way to determine a number of positions in
 * a two-dimensional coordinate system. These positions can then be used to
 * construct a graph. All positions are to be within the field defined by the
 * parameters <code>maxX</code> and <code>maxY</code> of the
 * <code>place(...)</code> method.
 * 
 * @author Philipp Neubrand
 * 
 */
public interface PlacementModel {

	/**
	 * Getter for the key of the particular placement model.
	 * 
	 * @return The key of the placement model.
	 */
	public String getKey();

	/**
	 * 
	 * Finds and returns <code>number</code> positions. All positions are
	 * guaranteed to be within the rectangle between (boxCenter.getX() -
	 * boxWidth / 2, boxCenter.getY() - boxHeight / 2) and (boxCenter.getX() +
	 * boxWidth() / 2, boxCenter.getY() + boxHeight / 2). If not enough nodes
	 * can be placed within those constraints, an Exception is thrown.
	 * 
	 * @param count
	 *            The number of positions to find.
	 * @param placementCenter
	 *            The center relative to which every node should be placed.
	 * @param boxCenter
	 *            The center of the bounding box.
	 * @param boxWidth
	 *            The width of the bounding box.
	 * @param boxHeight
	 *            The height of the bounding box.
	 * @return An array with size <code>count</code> containing the positions.
	 */
	Point[] place(int count, Point placementCenter, Point boxCenter,
			double boxWidth, double boxHeight);

	/**
	 * Getter for the configuration Parameters.
	 * 
	 * @return An array containing all the configuration parameters.
	 */
	public Parameter[] getConfigParameters();

}
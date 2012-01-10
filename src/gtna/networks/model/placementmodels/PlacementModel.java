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

/**
 * A <code>PlacementModel</code> is a way to determine a number of positions in
 * a two-dimensional coordinate system. These positions can then be used to
 * construct a graph. All positions are to be within the field defined by
 * <code>getWidth()</code> and <code>getHeight()</code>.
 * 
 * @author Philipp Neubrand
 * 
 */
public interface PlacementModel {

	/**
	 * Finds and returns <code>number</code> positions.
	 * 
	 * @param number
	 *            The number of positions to be returned.
	 * @return An array of positions.
	 */
	public Point[] place(int number);

	/**
	 * Getter for the key of the particular placement model.
	 * 
	 * @return The key of the placement model.
	 */
	public String getKey();

	/**
	 * Getter for the width of the field in which the positions are to be
	 * determined.
	 * 
	 * @return The width of the field.
	 */
	public double getWidth();

	/**
	 * Getter for the height of the field in which the positions are to be
	 * determined.
	 * 
	 * @return The height of the field.
	 */
	public double getHeight();

	/**
	 * Getter for all the configuration values.
	 * 
	 * @return A string array containing all the configuration values.
	 */
	public String[] getConfigValues();

	/**
	 * Getter for all the configuration keys.
	 * 
	 * @return A string array containing all the configuration values.
	 */
	public String[] getConfigKeys();

}
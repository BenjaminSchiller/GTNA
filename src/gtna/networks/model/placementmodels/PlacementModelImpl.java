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
 * PlacementModel.java
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

import gtna.util.Util;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * 
 * The default implementation of the <code>PlacementModel</code> interface
 * implements basic functionality as well as a convenient method to add
 * additional configuration keys and values. Extending classes only need to
 * implement the <code>place(...)</code> method and set a key with
 * <code>setKey(...)</code>. If additional configuration keys and values are
 * needed, they can be set by calling <code>setAdditionalConfigKeys(...)</code>
 * (...values(...)). These will be returned by <code>getConfigKeys()</code> in
 * addition to the key of the <code>PlacementModel</code> and the value of
 * inCenter.
 * 
 * @author Philipp Neubrand
 * 
 */
public abstract class PlacementModelImpl implements PlacementModel {
	private String key;
	protected final int maxTries = 100;
	private boolean inCenter;
	private Parameter[] additionalConfigParams;

	/**
	 * Getter for the key of the particular <code>PlacementModel</code>.
	 * 
	 * @return The key of the <code>PlacementModel</code>.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Setter for the key of the particular <code>PlacementModel</code>.
	 * 
	 * @param key
	 *            The new key of the <code>PlacementModel</code>.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public Parameter[] getConfigParameters() {
		return Util.mergeArrays(new Parameter[] {
				new StringParameter("KEY", key),
				new BooleanParameter("IN_CENTER", getInCenter()) },
				getAdditionalConfigParameters());
	}

	/**
	 * Getter for the additional configuration parameters.
	 * 
	 * @return An array containing the additional configuration parameters.
	 */
	protected Parameter[] getAdditionalConfigParameters() {
		return additionalConfigParams;
	}

	/**
	 * Setter for the additional configuration parameters, those will be
	 * returned in addition to the key of the PlacementModel.
	 * 
	 * @param parameters
	 *            The additional parameters.
	 */
	protected void setAdditionalConfigParameters(Parameter[] params) {
		additionalConfigParams = params;
	}

	/**
	 * Setter for inCenter.
	 * 
	 * @param inCenter
	 *            The new value for inCenter.
	 */
	public void setInCenter(boolean inCenter) {
		this.inCenter = inCenter;
	}

	/**
	 * Getter for inCenter.
	 * 
	 * @return The value of inCenter.
	 */
	public boolean getInCenter() {
		return inCenter;
	}

	/**
	 * Determines if a given point is inside the bounds defined by the given
	 * box.
	 * 
	 * @param x
	 *            The x coordinate that is to be tested.
	 * @param y
	 *            The y coordinate that is to be tested.
	 * @param boxCenter
	 *            The center of the bounding box.
	 * @param boxWidth
	 *            The width of the bounding box.
	 * @param boxHeight
	 *            The height of the bounding box.
	 * @return True, if the given point is inside the box (boxCenter.getX() -
	 *         boxWidth / 2, boxCenter.getY() - boxHeight / 2) and
	 *         (boxCenter.getX() + boxWidth() / 2, boxCenter.getY() + boxHeight
	 *         / 2).
	 */
	protected boolean inBounds(double x, double y, Point boxCenter,
			double boxWidth, double boxHeight) {
		return (x >= (boxCenter.getX() - (boxWidth / 2))
				|| x <= boxCenter.getX() + (boxWidth / 2)
				|| y >= boxCenter.getY() - (boxHeight / 2) || y <= (boxCenter
				.getY() + (boxHeight / 2)));
	}

}

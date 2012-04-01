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

import gtna.util.Util;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * An <code>AbstractNodeConnector</code> is the abstract implementation of the
 * <code>INodeConnector</code> interface. It implements all but the actual
 * <code>connect(...)</code> method. Mainly, it provides a way to add additional
 * configuration keys and values which will be returned by the
 * <code>getConfigKeys()</code>/<code>getConfigValues()</code> in addition to
 * the key of the NodeConnector. This is done by setting those additional keys
 * and values by calling
 * <code>setAdditionalConfigKeys/Values(String[] arr)</code>
 * 
 * @author Philipp Neubrand
 * 
 */
public abstract class NodeConnectorImpl implements NodeConnector {
	private String key;

	private Parameter[] additionalConfigParams;

	/**
	 * Sets the key of the current NodeConnector. Will be returned as the first
	 * key/value pair for the in the configuration keys and values.
	 * 
	 * @param key
	 *            The key of the NodeConnector.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Setter for the additional configuration parameters, those will be
	 * returned in addition to the key of the NodeConnector.
	 * 
	 * @param parameters
	 *            The additional parameters.
	 */
	protected void setAdditionalConfigParameters(Parameter[] parameters) {
		additionalConfigParams = parameters;
	}

	/**
	 * Getter for the additional configuration parameters.
	 * 
	 * @return An array containing the additional configuration parameters.
	 */
	protected Parameter[] getAdditionalConfigParameters() {
		return additionalConfigParams;
	}

	@Override
	public Parameter[] getConfigParameters() {
		return Util.mergeArrays(new Parameter[] { new StringParameter("KEY",
				key) }, getAdditionalConfigParameters());
	}

}

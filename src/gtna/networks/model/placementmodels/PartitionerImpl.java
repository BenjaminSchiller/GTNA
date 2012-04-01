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
 * Partitioner.java
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
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * Abstract implementation of the <code>IPartitioner</code> interface,
 * implementing all the common functionality of Partitioners. The only abstract
 * method left to be implemented is
 * <code>partition(int nodes, int hotspots)</code>, defining the actual
 * behaviour of the particular Partitioner.
 * 
 * @author Philipp Neubrand
 * 
 */
public abstract class PartitionerImpl implements Partitioner {
	// contains the key for the partitioner
	private String key;
	private Parameter[] additionalConfigParams;

	/**
	 * Setter for the key of the particular Partitioner. Should be called in the
	 * Constructor and set to a distinct value.
	 * 
	 * @param key
	 *            The key for this Partitioner.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public Parameter[] getConfigParameters() {
		return Util.mergeArrays(new Parameter[] { new StringParameter("KEY",
				key) }, getAdditionalConfigParameters());
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
	 * returned in addition to the key of the Partitioner.
	 * 
	 * @param parameters
	 *            The additional parameters.
	 */
	protected void setAdditionalConfigParams(Parameter[] parameters) {
		additionalConfigParams = parameters;
	}

}

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
 * RangeProperty.java
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
package gtna.networks.model.placementmodels.connectors;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.plane.PlaneIdentifier;

/**
 * @author Flipp
 *
 */
public class RangeProperty implements GraphProperty {
	private double range;
	

	/**
	 * @param range1
	 */
	public RangeProperty(double range1) {
		range = range1; 
	}

	/* (non-Javadoc)
	 * @see gtna.graph.GraphProperty#read(java.lang.String, gtna.graph.Graph)
	 */
	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gtna.graph.GraphProperty#write(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean write(String filename, String key) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return
	 */
	public double getRange() {
		return range;
	}

}

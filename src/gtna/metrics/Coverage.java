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
 * Coverage.java
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
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.id.DPartition;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.connectors.RangeProperty;
import gtna.util.Config;

import java.util.HashMap;

/**
 * @author Philipp Neubrand
 *
 */
public class Coverage extends MetricImpl implements Metric {

	private double percentage;

	public Coverage() {
		super("COVERAGE");
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if(!g.hasProperty("RANGE_0") || !g.hasProperty("ID_SPACE_0"))
			return;
		
		RangeProperty range = (RangeProperty) g.getProperty("RANGE_0");
		PlaneIdentifierSpaceSimple idspace = (PlaneIdentifierSpaceSimple) g.getProperty("ID_SPACE_0");
		
		long in = 0;
		
		double width = idspace.getModulusX();
		double height = idspace.getModulusY();
		
		int cols = Config.getInt("COVERAGE_COLS");
		int rows = Config.getInt("COVERAGE_ROWS");
		
		for(int i = 0; i < cols; i++){
			for(int j = 0; j < rows; j++){
				if(inside(i*(width/cols), j*(height/rows), idspace, range))
						in++;
			}
		}
		
		percentage = (((double) in) / (cols*rows));
	}

	/**
	 * @param d
	 * @param e
	 * @param idspace
	 * @param range
	 * @return
	 */
	private boolean inside(double x, double y,
			PlaneIdentifierSpaceSimple idspace, RangeProperty range) {
		double dist = 0;
		boolean ret = false;
		for(DPartition akt : idspace.getPartitions()){
			dist = Math.sqrt(Math.pow(((PlaneIdentifier) akt.getRepresentativeID()).getX() - x, 2) + Math.pow(((PlaneIdentifier) akt.getRepresentativeID()).getY() - y, 2));
			if(dist < range.getRange()){
					ret = true;
					break;
			}
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getValues()
	 */
	@Override
	public Value[] getValues() {
		return new Value[]{new Value("COVERAGE_PERCENTAGE", percentage)};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		return true;
	}

}

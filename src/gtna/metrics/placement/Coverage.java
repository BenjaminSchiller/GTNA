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
 * 2014-02-05: readData, getDistributions, getNodeValueLists (Tim Grube)
 */
package gtna.metrics.placement;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.id.Partition;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.io.DataReader;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.connectors.RangeProperty;
import gtna.util.Config;
import gtna.util.Distribution;

import java.util.HashMap;

/**
 * The <code>Coverage</code> metric calculates the coverage (in percent) a disk
 * graph has for the given field. The radii are read from the
 * <code>RangeProperty</code> stored in RANGES_0, the positions of the nodes are
 * read from the <code>PlaneIdentifierSpaceSimple</code> stored in ID_SPACE_0.
 * 
 * The coverage is calculated by placing a grid over the rectangle from (0, 0)
 * to (idspace.getModulusX(), idSpace.getModulusY()). The number of rows and
 * columns for this grid can be specified in the configuration file. The center
 * of each cell in the grid is then checked against the disk graph with the
 * given positions and radii. While this only gives an estimate on the real
 * coverage percentage, the resolution of the grid can be adjusted as necessary.
 * In practice, resolutions of about 1000x1000 have proven to be rather accurate
 * while still being fairly fast.
 * 
 * @author Philipp Neubrand
 * 
 */
public class Coverage extends Metric {

	private double percentage;

	public Coverage() {
		super("COVERAGE");
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("RANGE_0") && g.hasProperty("ID_SPACE_0");
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if (!g.hasProperty("RANGE_0") || !g.hasProperty("ID_SPACE_0"))
			return;

		RangeProperty range = (RangeProperty) g.getProperty("RANGE_0");
		PlaneIdentifierSpaceSimple idspace = (PlaneIdentifierSpaceSimple) g
				.getProperty("ID_SPACE_0");

		long in = 0;

		double width = idspace.getxModulus();
		double height = idspace.getyModulus();

		int cols = Config.getInt("COVERAGE_COLS");
		int rows = Config.getInt("COVERAGE_ROWS");

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				if (inside(i * (width / cols), j * (height / rows), idspace,
						range))
					in++;
			}
		}

		percentage = (((double) in) / (cols * rows));
	}

	private boolean inside(double x, double y,
			PlaneIdentifierSpaceSimple idspace, RangeProperty range) {
		double dist = 0;
		boolean ret = false;
		int id = 0;
		for (Partition akt : idspace.getPartitions()) {

			dist = Math.sqrt(Math.pow(
					((PlaneIdentifier) akt.getRepresentativeIdentifier())
							.getX() - x, 2)
					+ Math.pow(
							((PlaneIdentifier) akt
									.getRepresentativeIdentifier()).getY() - y,
							2));
			if (dist < range.getRanges()[id]) {
				ret = true;
				break;
			}
			id++;
		}

		return ret;
	}

	@Override
	public Single[] getSingles() {
		return new Single[] { new Single("COVERAGE_PERCENTAGE", percentage) };
	}
	
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] {};
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {};
	}

	@Override
	public boolean writeData(String folder) {
		return true;
	}
	
	@Override
	public boolean readData(String folder){
		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
				if("COVERAGE_PERCENTAGE".equals(single[0])){
					this.percentage = Double.valueOf(single[1]);
				} 
			}
		}
		
		return true;
	}

}

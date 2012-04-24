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
 * IdSpaceVisualzation.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.id;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Statistics;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class IdentifierSpaceVisualzation extends Metric {
	private int bins;

	private double[][] ids;

	public IdentifierSpaceVisualzation() {
		super("IDENTIFIER_SPACE_VISUALIZATION");
		this.bins = -1;

		this.ids = new double[][] { new double[] { -1, -1 } };
	}

	public IdentifierSpaceVisualzation(int bins) {
		super("IDENTIFIER_SPACE_VISUALIZATION",
				new Parameter[] { new IntParameter("BINS", bins) });
		this.bins = bins;

		this.ids = new double[][] { new double[] { -1, -1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		double[] pos = this.getPos(g);
		double modulus = ((RingIdentifierSpace) g.getProperty("ID_SPACE_0"))
				.getModulus();

		if (this.bins > 0) {
			this.ids = this.getIDsBinned(pos, modulus, this.bins);
		} else {
			this.ids = this.getIDs(pos, modulus);
		}
	}

	private double[][] getIDs(double[] pos, double modulus) {
		double[][] ids = new double[pos.length][2];
		for (int i = 0; i < pos.length; i++) {
			ids[i] = this.getID(pos[i], modulus);
		}
		return ids;
	}

	private double[][] getIDsBinned(double[] pos, double modulus, int bins) {
		double[][] pos2 = Statistics.binning(pos, 0, modulus, modulus
				/ (double) this.bins);
		double[][] ids = new double[pos.length][2];
		int index = 0;
		for (int i = 0; i < pos2.length; i++) {
			for (int j = 0; j < pos2[i].length; j++) {
				ids[index] = this.getID(pos2[i][j], modulus);
				double stretch = 0.5 + 0.5 * ((double) j / pos2[i].length);
				ids[index][0] *= stretch;
				ids[index][1] *= stretch;
				index++;
			}
		}
		return ids;
	}

	private double[] getID(double pos, double modulus) {
		double angle = (pos / modulus) * 360;

		double x = Math.sin(Math.toRadians(angle));
		double y = Math.cos(Math.toRadians(angle));

		return new double[] { x, y };
	}

	@SuppressWarnings("rawtypes")
	private double[] getPos(Graph g) {
		IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");
		RingIdentifierSpace ring = (RingIdentifierSpace) ids;
		double[] pos = new double[g.getNodes().length];
		for (int i = 0; i < pos.length; i++) {
			pos[i] = ((RingIdentifier) ring.getPartitions()[i]
					.getRepresentativeID()).getPosition();
		}
		return pos;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.ids, "VISUALIZATION",
				folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[0];
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		if (!g.hasProperty("ID_SPACE_0")) {
			return false;
		}
		if (!(g.getProperty("ID_SPACE_0") instanceof IdentifierSpace)) {
			return false;
		}
		IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");
		return (ids instanceof RingIdentifierSpace);
	}
}

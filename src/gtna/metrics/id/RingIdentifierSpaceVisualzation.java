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

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.Statistics;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class RingIdentifierSpaceVisualzation extends Metric {
	private int bins;

	private double[][] ring;

	private double[][] line;

	private double[][] horizontalLine;

	private double[][] sorted;

	public RingIdentifierSpaceVisualzation() {
		super("RING_IDENTIFIER_SPACE_VISUALIZATION");
		this.bins = -1;

		this.ring = new double[][] { new double[] { -1, -1 } };
		this.line = new double[][] { new double[] { -1, -1 } };
		this.horizontalLine = new double[][] { new double[] { -1, -1 } };
		this.sorted = new double[][] { new double[] { -1, -1 } };
	}

	public RingIdentifierSpaceVisualzation(int bins) {
		super("RING_IDENTIFIER_SPACE_VISUALIZATION",
				new Parameter[] { new IntParameter("BINS", bins) });
		this.bins = bins;

		this.ring = new double[][] { new double[] { -1, -1 } };
		this.line = new double[][] { new double[] { -1, -1 } };
		this.horizontalLine = new double[][] { new double[] { -1, -1 } };
		this.sorted = new double[][] { new double[] { -1, -1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		double[] pos = this.getPos(g);
		double modulus = 1.0;

		if (this.bins > 0) {
			this.ring = this.getRingBinned(pos, modulus, this.bins);
			this.line = this.getLineBinned(pos, modulus, bins);
			this.horizontalLine = this.getHorizontalLineBinned(pos, modulus,
					this.bins);
		} else {
			this.ring = this.getRing(pos, modulus);
			this.line = this.getLine(pos, modulus);
			this.horizontalLine = this.getHorizontalLine(pos, modulus);
		}

		this.sorted = this.getSorted(pos, modulus);
	}

	private double[][] getRing(double[] pos, double modulus) {
		double[][] ids = new double[pos.length][2];
		for (int i = 0; i < pos.length; i++) {
			ids[i] = this.getRingId(pos[i], modulus);
		}
		return ids;
	}

	private double[][] getLine(double[] pos, double modulus) {
		double[][] ids = new double[pos.length][2];
		for (int i = 0; i < pos.length; i++) {
			ids[i] = new double[] { pos[i] / modulus, pos[i] / modulus };
		}
		return ids;
	}

	private double[][] getHorizontalLine(double[] pos, double modulus) {
		double[][] ids = new double[pos.length][2];
		for (int i = 0; i < pos.length; i++) {
			ids[i] = new double[] { pos[i] / modulus, 0.0 };
		}
		return ids;
	}

	private double[][] getSorted(double[] pos, double modulus) {
		double[] temp = pos.clone();
		Arrays.sort(temp);
		double[][] ids = new double[pos.length][2];
		for (int i = 0; i < temp.length; i++) {
			ids[i] = new double[] { i, temp[i] };
		}
		return ids;
	}

	private double[][] getRingBinned(double[] pos, double modulus, int bins) {
		double[][] pos2 = Statistics.binning(pos, 0, modulus, modulus
				/ (double) this.bins);
		double[][] ids = new double[pos.length][2];

		int maxLength = 0;
		for (double[] p : pos2) {
			if (p.length > maxLength) {
				maxLength = p.length;
			}
		}

		int index = 0;
		for (int i = 0; i < pos2.length; i++) {
			for (int j = 0; j < pos2[i].length; j++) {
				ids[index] = this.getRingId(pos2[i][j], modulus);
				double stretch = 0.5 + 0.5 * ((double) j / maxLength);
				ids[index][0] *= stretch;
				ids[index][1] *= stretch;
				index++;
			}
		}

		return ids;
	}

	private double[][] getLineBinned(double[] pos, double modulus, int bins) {
		double[][] ids = this.getHorizontalLineBinned(pos, modulus, bins);
		for (int i = 0; i < ids.length; i++) {
			ids[i][1] += ids[i][0];
		}
		return ids;
	}

	private double[][] getHorizontalLineBinned(double[] pos, double modulus,
			int bins) {
		double[][] pos2 = Statistics.binning(pos, 0, modulus, modulus
				/ (double) this.bins);
		double[][] ids = new double[pos.length][2];

		int maxLength = 0;
		for (double[] p : pos2) {
			if (p.length > maxLength) {
				maxLength = p.length;
			}
		}

		int index = 0;
		for (int i = 0; i < pos2.length; i++) {
			for (int j = 0; j < pos2[i].length; j++) {
				ids[index][0] = (double) i / (modulus * (double) bins);
				ids[index][1] = (double) j / (double) maxLength;
				index++;
			}
		}

		return ids;
	}

	private double[] getRingId(double pos, double modulus) {
		double angle = (pos / modulus) * 360;
		return new double[] { Math.sin(Math.toRadians(angle)),
				Math.cos(Math.toRadians(angle)) };
	}

	private double[] getPos(Graph g) {
		IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");
		RingIdentifierSpace ring = (RingIdentifierSpace) ids;
		double[] pos = new double[g.getNodes().length];
		for (int i = 0; i < pos.length; i++) {
			pos[i] = ((RingIdentifier) ring.getPartitions()[i]
					.getRepresentativeIdentifier()).getPosition();
		}
		return pos;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.ring,
				"RING_IDENTIFIER_SPACE_VISUALIZATION_RING", folder);
		success &= DataWriter.writeWithoutIndex(this.line,
				"RING_IDENTIFIER_SPACE_VISUALIZATION_LINE", folder);
		success &= DataWriter.writeWithoutIndex(this.horizontalLine,
				"RING_IDENTIFIER_SPACE_VISUALIZATION_HORIZONTAL_LINE", folder);
		success &= DataWriter.writeWithoutIndex(this.sorted,
				"RING_IDENTIFIER_SPACE_VISUALIZATION_SORTED", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[0];
	}
	
	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getDistributions()
	 */
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[0];
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getNodeValueLists()
	 */
	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[0];
	}
	
	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#readData(java.lang.String)
	 */
	@Override
	public boolean readData(String folder) {

		
		/* 2D values */
		
		this.ring = read2DValues(folder, "RING_IDENTIFIER_SPACE_VISUALIZATION_RING");
		this.line = read2DValues(folder, "RING_IDENTIFIER_SPACE_VISUALIZATION_LINE");
		this.horizontalLine = read2DValues(folder, "RING_IDENTIFIER_SPACE_VISUALIZATION_HORIZONTAL_LINE");
		this.sorted = read2DValues(folder, "RING_IDENTIFIER_SPACE_VISUALIZATION_SORTED");
				
		return true;
	}

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

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
 * FragmentationRecompute.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.fragmentation;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorterUpdate;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashMap;
import java.util.Random;

/**
 * @author stefanie
 * 
 */
public abstract class FragmentationRecompute extends Metric {
	protected NodeSorter sorter;

	protected Resolution resolution;
	protected boolean bidirectional;

	public static enum Type {
		STRONG, WEAK
	}

	public static enum Resolution {
		SINGLE, PERCENT
	};

	public FragmentationRecompute(Type type, NodeSorter sorter,
			Resolution resolution, boolean bidirectional) {
		super("FRAGMENTATION", new Parameter[] {
				new StringParameter("TYPE", type.toString()),
				new StringParameter("SORTER", sorter.getKey()),
				new StringParameter("RESOLUTION", resolution.toString()) });
		this.sorter = sorter;
		this.resolution = resolution;
		this.bidirectional = bidirectional;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	private double[] isolatedComponentSizeAvg;

	private double[] isolatedComponentSizeMax;

	private double[] isolatedComponentSizeMed;

	private double[] isolatedComponentSizeMin;

	private double[] numberOfIsolatedComponents;

	private double[] largestComponentSize;

	private double[] largestComponentSizeFraction;

	private double criticalPoint;

	private double[] criticalPoints;

	private int[] cpts;

	private double[][] degDist;

	protected int[][] degs;

	private double[] c;

	private Timer runtime;
	
	private double threshold;
	
	private double thresholdfraction;

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.runtime = new Timer();
		int maxIn = 0;
		int maxOut = 0;
		Node[] nodes = g.getNodes();
		for (int i = 0; i < g.getNodeCount(); i++) {
			if (bidirectional) {
				if (nodes[i].getDegree() > 2 * maxIn) {
					maxIn = nodes[i].getDegree() / 2;
				}
			} else {
				if (nodes[i].getInDegree() > maxIn) {
					maxIn = nodes[i].getInDegree();
				}
				if (nodes[i].getOutDegree() > maxOut) {
					maxOut = nodes[i].getOutDegree();
				}
			}
		}
		if (bidirectional){
			this.degs = new int[nodes.length][1];
		} else {
			this.degs = new int[nodes.length][2];
		}
		if (this.bidirectional) {
			this.degDist = new double[maxIn + 1][1];
			for (int i = 0; i < g.getNodeCount(); i++) {
				this.degDist[nodes[i].getInDegree()][0]++;
				this.degs[i][0] = nodes[i].getInDegree();
			}
		} else {
			this.degDist = new double[maxIn + 1][maxOut + 1];
			for (int i = 0; i < g.getNodeCount(); i++) {
				this.degDist[nodes[i].getInDegree()][nodes[i].getOutDegree()]++;
				this.degs[i][0] = nodes[i].getInDegree();
				this.degs[i][1] = nodes[i].getOutDegree();
			}
		}
		for (int i = 0; i < degDist.length; i++) {
			for (int j = 0; j < degDist[i].length; j++) {
				this.degDist[i][j] = this.degDist[i][j] / (double) nodes.length;
			}
		}
		int[] excludeFirst = this.getExcludeFirst(g.getNodes().length);
		this.isolatedComponentSizeAvg = new double[excludeFirst.length];
		this.isolatedComponentSizeMax = new double[excludeFirst.length];
		this.isolatedComponentSizeMed = new double[excludeFirst.length];
		this.isolatedComponentSizeMin = new double[excludeFirst.length];
		this.numberOfIsolatedComponents = new double[excludeFirst.length];
		this.largestComponentSize = new double[excludeFirst.length];
		this.largestComponentSizeFraction = new double[excludeFirst.length];
		this.criticalPoint = 1.0;
		this.cpts = this.getCriticalPointThreshold();
		this.criticalPoints = new double[this.cpts.length];
		for (int i = 0; i < this.criticalPoints.length; i++) {
			this.criticalPoints[i] = 1.0;
		}
		Random rand = new Random();
		
		
		this.addCriticalPointConfigs();
		Node[] sorted = this.sorter.sort(g, rand);
		boolean[] exclude = new boolean[sorted.length];
		c = new double[excludeFirst.length];
		this.threshold = sorted.length;
		for (int i = 0; i < excludeFirst.length; i++) {
//			int e = 0;
//			for (int j = 0; j < exclude.length; j++){
//				if (exclude[j]){
//					e++;
//				}
//			}
//			System.out.println(i + " " + excludeFirst[i] + " " + e);
			exclude = this.getExclude(sorted, exclude, excludeFirst[i],
					i > 0 ? excludeFirst[i - 1] : 0, rand);
			Partition p = this.partition(g, sorted, exclude.clone());
			
            c[i] = 1/(1-excludeFirst[i]/(double)nodes.length)*this.getC();
           // System.out.println("ci " + c[i]);
            if (c[i] < 0 && threshold == nodes.length){
            	this.threshold = excludeFirst[i];
            	this.thresholdfraction = excludeFirst[i]/(double)sorted.length;
            }
			this.numberOfIsolatedComponents[i] = p.getComponents().length - 1;
			
			this.largestComponentSize[i] = p.getLargestComponent().length;
			this.largestComponentSizeFraction[i] = (double) p
					.getLargestComponent().length
					/ (double) g.getNodes().length;

			if (this.numberOfIsolatedComponents[i] == 0) {
				this.isolatedComponentSizeAvg[i] = 0;
				this.isolatedComponentSizeMax[i] = 0;
				this.isolatedComponentSizeMed[i] = 0;
				this.isolatedComponentSizeMin[i] = 0;
			} else {
				this.isolatedComponentSizeAvg[i] = this.avgIsolatedSize(p);
				this.isolatedComponentSizeMax[i] = p.getComponents()[p
						.getComponents().length - 2].length;
				this.isolatedComponentSizeMed[i] = p.getComponents()[(int) Math
						.floor(p.getComponents().length / 2)].length;
				this.isolatedComponentSizeMin[i] = p.getComponents()[p
						.getComponents().length - 1].length;
			}

			if (this.largestComponentSize[i] < 0.5 * (g.getNodes().length - excludeFirst[i])
					&& (double) excludeFirst[i] / (double) g.getNodes().length < this.criticalPoint) {
				this.criticalPoint = (double) excludeFirst[i]
						/ (double) g.getNodes().length;
			}

			for (int j = 0; j < criticalPoints.length; j++) {
				double cpt = (double) this.cpts[j] / 100;
				if (this.largestComponentSize[i] < cpt
						* (g.getNodes().length - excludeFirst[i])
						&& (double) excludeFirst[i]
								/ (double) g.getNodes().length < this.criticalPoints[j]) {
					this.criticalPoints[j] = (double) excludeFirst[i]
							/ (double) g.getNodes().length;
				}
			}
		}
		this.runtime.end();
	}

	private int[] getCriticalPointThreshold() {
		String[] temp = Config.get("FRAGMENTATION_CRITICAL_POINTS").split(",");
		int[] criticalPoints = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			criticalPoints[i] = Integer.parseInt(temp[i].trim());
		}
		return criticalPoints;
	}

	private void addCriticalPointConfigs() {
		for (int cpt : this.cpts) {
			Config.overwrite(
					"FRAGMENTATION_CRITICAL_POINT_" + cpt + "_SINGLE_NAME",
					Config.get(
							"FRAGMENTATION_CRITICAL_POINT_SINGLE_NAME_TEMPLATE")
							.replace("$PERCENT", "" + cpt));

			Config.overwrite(
					"FRAGMENTATION_CRITICAL_POINT_" + cpt + "_PLOT_DATA",
					Config.get(
							"FRAGMENTATION_CRITICAL_POINT_PLOT_DATA_TEMPLATE")
							.replace("$PERCENT", "" + cpt));
			Config.overwrite(
					"FRAGMENTATION_CRITICAL_POINT_" + cpt + "_PLOT_FILENAME",
					Config.get(
							"FRAGMENTATION_CRITICAL_POINT_PLOT_FILENAME_TEMPLATE")
							.replace("$PERCENT", "" + cpt));
			Config.overwrite(
					"FRAGMENTATION_CRITICAL_POINT_" + cpt + "_PLOT_TITLE",
					Config.get(
							"FRAGMENTATION_CRITICAL_POINT_PLOT_TITLE_TEMPLATE")
							.replace("$PERCENT", "" + cpt));
			Config.overwrite("FRAGMENTATION_CRITICAL_POINT_" + cpt + "_PLOT_Y",
					Config.get("FRAGMENTATION_CRITICAL_POINT_PLOT_Y_TEMPLATE")
							.replace("$PERCENT", "" + cpt));

			Config.appendToList("FRAGMENTATION_SINGLES_KEYS",
					"FRAGMENTATION_CRITICAL_POINT_" + cpt);
			Config.appendToList("FRAGMENTATION_SINGLES_PLOTS",
					"FRAGMENTATION_CRITICAL_POINT_" + cpt);
			Config.appendToList("FRAGMENTATION_TABLE_KEYS",
					"FRAGMENTATION_CRITICAL_POINT_" + cpt);
		}
	}

	private double avgIsolatedSize(Partition p) {
		double sum = 0;
		for (int i = 1; i < p.getComponents().length; i++) {
			sum += p.getComponents()[i].length;
		}
		return sum / p.getComponents().length;
	}

	private int[] getExcludeFirst(int nodes) {
		if (nodes < 100 || this.resolution == Resolution.SINGLE) {
			int[] exclude = new int[nodes];
			for (int i = 0; i < nodes; i++) {
				exclude[i] = i;
			}
			return exclude;
		} else {
			int[] exclude = new int[100];
			for (int i = 0; i < 100; i++) {
				exclude[i] = (int) Math
						.floor(((double) nodes * (double) i) / 100.0);
			}
			return exclude;
		}
	}

	private boolean[] getExclude(Node[] sorted, boolean[] exclude,
			int excludeFirst, int startExclude, Random rand) {
		for (int i = startExclude; i < excludeFirst; i++) {
			exclude[sorted[i].getIndex()] = true;
			if (this.bidirectional) {
				this.degDist[degs[sorted[i].getIndex()][0]][0] = this.degDist[degs[sorted[i]
						.getIndex()][0]][0] - 1 / (double) sorted.length;
				int[] in = sorted[i].getIncomingEdges();
				for (int k = 0; k < in.length; k++) {
					if (!exclude[in[k]]){
					this.degDist[degs[in[k]][0]][0] = this.degDist[degs[in[k]][0]][0]
							- 1 / (double) sorted.length;
					degs[in[k]][0]--;
					this.degDist[degs[in[k]][0]][0] = this.degDist[degs[in[k]][0]][0]
							+ 1 / (double) sorted.length;
					}
				}

			} else {
				this.degDist[degs[sorted[i].getIndex()][0]][degs[sorted[i]
						.getIndex()][1]] = this.degDist[degs[sorted[i]
						.getIndex()][0]][degs[sorted[i].getIndex()][1]]
						- 1
						/ (double) sorted.length;
				int[] in = sorted[i].getIncomingEdges();
				for (int k = 0; k < in.length; k++) {
					if (!exclude[in[k]]){
					this.degDist[degs[in[k]][0]][degs[in[k]][1]] = this.degDist[degs[in[k]][0]][degs[in[k]][1]]
							- 1 / (double) sorted.length;
					degs[in[k]][1]--;
					this.degDist[degs[in[k]][0]][degs[in[k]][1]] = this.degDist[degs[in[k]][0]][degs[in[k]][1]]
							+ 1 / (double) sorted.length;
					}
				}
				int[] out = sorted[i].getOutgoingEdges();
				for (int k = 0; k < out.length; k++) {
					if (!exclude[out[k]]){
					this.degDist[degs[out[k]][0]][degs[out[k]][1]] = this.degDist[degs[out[k]][0]][degs[out[k]][1]]
							- 1 / (double) sorted.length;
					degs[out[k]][0]--;
					this.degDist[degs[out[k]][0]][degs[out[k]][1]] = this.degDist[degs[out[k]][0]][degs[out[k]][1]]
							+ 1 / (double) sorted.length;
					}
				}
			}
			if (this.sorter instanceof NodeSorterUpdate)
			sorted = ((NodeSorterUpdate) this.sorter).update(exclude, i, rand);
		}
		return exclude;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeAvg,
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_AVG", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMax,
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MAX", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMed,
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MED", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMin,
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MIN", folder);
		success &= DataWriter.writeWithIndex(this.numberOfIsolatedComponents,
				"FRAGMENTATION_NUMBER_OF_ISOLATED_COMPONENTS", folder);
		success &= DataWriter.writeWithIndex(this.largestComponentSize,
				"FRAGMENTATION_LARGEST_COMPONENT_SIZE", folder);
		success &= DataWriter.writeWithIndex(this.largestComponentSizeFraction,
				"FRAGMENTATION_LARGEST_COMPONENT_SIZE_FRACTION", folder);
		success &= DataWriter.writeWithIndex(this.c,
				"FRAGMENTATION_CONNECTIVITY_CRITERION", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single[] singles = new Single[this.cpts.length + 4];
		singles[0] = new Single("FRAGMENTATION_CRITICAL_POINT",
				this.criticalPoint);
		singles[1] = new Single("FRAGMENTATION_RUNTIME",
				this.runtime.getRuntime());
		for (int i = 0; i < this.cpts.length; i++) {
			singles[i + 2] = new Single("FRAGMENTATION_CRITICAL_POINT_"
					+ this.cpts[i], this.criticalPoints[i]);
		}
		singles[singles.length-2] = new Single("FRAGMENTATION_THRESHOLD"
				, this.threshold);
		singles[singles.length-1] = new Single("FRAGMENTATION_THRESHOLD_FRACTION"
				, this.thresholdfraction);
		return singles;
	}

	protected abstract Partition partition(Graph g, Node[] sorted,
			boolean[] exclude);

	private double getC() {
		double c = 0;
		if (this.bidirectional) {
			for (int i = 0; i < this.degDist.length; i++) {
				c = c + i * (i - 2) * this.degDist[i][0];
			}
		} else {
			for (int k = 0; k < this.degDist.length; k++) {
				for (int j = 0; j < this.degDist[k].length; j++) {
					c = c + (2 * k * j - k - j) * this.degDist[k][j];
				}
			}
		}
		return c;
	}

}

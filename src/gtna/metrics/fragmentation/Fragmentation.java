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
 * Fragmentation.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-05: readData, getDistributions, getNodeValueLists (Tim Grube) 
 */
package gtna.metrics.fragmentation;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.graph.sorting.NodeSorter;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author benni
 * 
 */
public abstract class Fragmentation extends Metric {
	protected NodeSorter sorter;

	protected Resolution resolution;

	public static enum Type {
		STRONG, WEAK
	}

	public static enum Resolution {
		SINGLE, PERCENT
	};

	public Fragmentation(Type type, NodeSorter sorter, Resolution resolution) {
		super("FRAGMENTATION", new Parameter[] {
				new StringParameter("TYPE", type.toString()),
				new StringParameter("SORTER", sorter.getKey()),
				new StringParameter("RESOLUTION", resolution.toString()) });
		this.sorter = sorter;
		this.resolution = resolution;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	private NodeValueList isolatedComponentSizeAvg;

	private NodeValueList isolatedComponentSizeMax;

	private NodeValueList isolatedComponentSizeMed;

	private NodeValueList isolatedComponentSizeMin;

	private NodeValueList numberOfIsolatedComponents;

	private NodeValueList largestComponentSize;

	private NodeValueList largestComponentSizeFraction;

	private double criticalPoint;

	private double[] criticalPoints;

	private int[] cpts;

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		int[] excludeFirst = this.getExcludeFirst(g.getNodes().length);
		double[] isolatedComponentSizeAvgArray = new double[excludeFirst.length];
		double[] isolatedComponentSizeMaxArray = new double[excludeFirst.length];
		double[] isolatedComponentSizeMedArray = new double[excludeFirst.length];
		double[] isolatedComponentSizeMinArray = new double[excludeFirst.length];
		double[] numberOfIsolatedComponentsArray = new double[excludeFirst.length];
		double[] largestComponentSizeArray = new double[excludeFirst.length];
		double[] largestComponentSizeFractionArray = new double[excludeFirst.length];
		this.criticalPoint = 1.0;
		this.cpts = this.getCriticalPointThreshold();
		this.criticalPoints = new double[this.cpts.length];
		for (int i = 0; i < this.criticalPoints.length; i++) {
			this.criticalPoints[i] = 1.0;
		}
		Random rand = new Random();
		this.addCriticalPointConfigs();
		Node[] sorted = this.sorter.sort(g, rand);
		for (int i = 0; i < excludeFirst.length; i++) {
			boolean[] exclude = this.getExclude(sorted, excludeFirst[i]);
			Partition p = this.partition(g, sorted, exclude);

			numberOfIsolatedComponentsArray[i] = p.getComponents().length - 1;
			largestComponentSizeArray[i] = p.getLargestComponent().length;
			largestComponentSizeFractionArray[i] = (double) p
					.getLargestComponent().length
					/ (double) g.getNodes().length;

			if (numberOfIsolatedComponentsArray[i] == 0) {
				isolatedComponentSizeAvgArray[i] = 0;
				isolatedComponentSizeMaxArray[i] = 0;
				isolatedComponentSizeMedArray[i] = 0;
				isolatedComponentSizeMinArray[i] = 0;
			} else {
				isolatedComponentSizeAvgArray[i] = this.avgIsolatedSize(p);
				isolatedComponentSizeMaxArray[i] = p.getComponents()[p
						.getComponents().length - 2].length;
				isolatedComponentSizeMedArray[i] = p.getComponents()[(int) Math
						.floor(p.getComponents().length / 2)].length;
				isolatedComponentSizeMinArray[i] = p.getComponents()[p
						.getComponents().length - 1].length;
			}

			if (largestComponentSizeArray[i] < 0.5 * (g.getNodes().length - excludeFirst[i])
					&& (double) excludeFirst[i] / (double) g.getNodes().length < this.criticalPoint) {
				this.criticalPoint = (double) excludeFirst[i]
						/ (double) g.getNodes().length;
			}

			for (int j = 0; j < criticalPoints.length; j++) {
				double cpt = (double) this.cpts[j] / 100;
				if (largestComponentSizeArray[i] < cpt
						* (g.getNodes().length - excludeFirst[i])
						&& (double) excludeFirst[i]
								/ (double) g.getNodes().length < this.criticalPoints[j]) {
					this.criticalPoints[j] = (double) excludeFirst[i]
							/ (double) g.getNodes().length;
				}
			}
		}
		
		isolatedComponentSizeAvg = new NodeValueList("FRAGMENTATION_ISOLATED_COMPONENT_SIZE_AVG", isolatedComponentSizeAvgArray);
		isolatedComponentSizeMax = new NodeValueList("FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MAX", isolatedComponentSizeMaxArray);
		isolatedComponentSizeMed = new NodeValueList("FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MED", isolatedComponentSizeMedArray);
		isolatedComponentSizeMin = new NodeValueList("FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MIN", isolatedComponentSizeMinArray);
		numberOfIsolatedComponents = new NodeValueList("FRAGMENTATION_NUMBER_OF_ISOLATED_COMPONENTS", numberOfIsolatedComponentsArray);
		largestComponentSize = new NodeValueList("FRAGMENTATION_LARGEST_COMPONENT_SIZE", largestComponentSizeArray);
		largestComponentSizeFraction = new NodeValueList("FRAGMENTATION_LARGEST_COMPONENT_SIZE_FRACTION", largestComponentSizeFractionArray);
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

	private boolean[] getExclude(Node[] sorted, int excludeFirst) {
		boolean[] exclude = new boolean[sorted.length];
		for (int i = 0; i < excludeFirst; i++) {
			exclude[sorted[i].getIndex()] = true;
		}
		return exclude;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeAvg.getValues(),
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_AVG", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMax.getValues(),
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MAX", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMed.getValues(),
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MED", folder);
		success &= DataWriter.writeWithIndex(this.isolatedComponentSizeMin.getValues(),
				"FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MIN", folder);
		success &= DataWriter.writeWithIndex(this.numberOfIsolatedComponents.getValues(),
				"FRAGMENTATION_NUMBER_OF_ISOLATED_COMPONENTS", folder);
		success &= DataWriter.writeWithIndex(this.largestComponentSize.getValues(),
				"FRAGMENTATION_LARGEST_COMPONENT_SIZE", folder);
		success &= DataWriter.writeWithIndex(this.largestComponentSizeFraction.getValues(),
				"FRAGMENTATION_LARGEST_COMPONENT_SIZE_FRACTION", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single[] singles = new Single[this.cpts.length + 1];
		singles[0] = new Single("FRAGMENTATION_CRITICAL_POINT",
				this.criticalPoint);
		for (int i = 0; i < this.cpts.length; i++) {
			singles[i + 1] = new Single("FRAGMENTATION_CRITICAL_POINT_"
					+ this.cpts[i], this.criticalPoints[i]);
		}
		return singles;
	}
	
	
	@Override
	public Distribution[] getDistributions(){
		return new Distribution[0];
	}
	
	@Override
	public NodeValueList[] getNodeValueLists(){
		return new NodeValueList[]{isolatedComponentSizeAvg, isolatedComponentSizeMax, isolatedComponentSizeMed, isolatedComponentSizeMin,
				numberOfIsolatedComponents, largestComponentSize, largestComponentSizeFraction };
	}

	
	@Override
	public boolean readData(String folder){
		ArrayList<Double> cpointsList = new ArrayList<Double>();
		ArrayList<Integer> cptsList = new ArrayList<Integer>();
		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
				if("FRAGMENTATION_CRITICAL_POINT".equals(single[0])){
					this.criticalPoint = (int) Math.round(Double.valueOf(single[1]));
				} else if((single[0].startsWith("FRAGMENTATION_CRITICAL_POINT_"))){
					cptsList.add(Integer.parseInt(single[0].replace("FRAGMENTATION_CRITICAL_POINT_", "")));
					cpointsList.add(Double.valueOf(single[1])); // Index important?
				}  
			}
		}
		
		if(cptsList.size() != cpointsList.size())
			return false;
		
		this.criticalPoints = new double[cpointsList.size()];
		this.cpts = new int[cptsList.size()];
		for(int i = 0; i < criticalPoints.length; i++){
			this.criticalPoints[i] = cpointsList.get(i);
			this.cpts[i] = cptsList.get(i);
		}
		
		
		
		
		/* Node-value list */
		
		this.isolatedComponentSizeAvg = new NodeValueList("FRAGMENTATION_ISOLATED_COMPONENT_SIZE_AVG", readDistribution(folder, "FRAGMENTATION_ISOLATED_COMPONENT_SIZE_AVG"));
		this.isolatedComponentSizeMax = new NodeValueList("FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MAX" , readDistribution(folder, "FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MAX"));
		this.isolatedComponentSizeMed = new NodeValueList("FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MED" , readDistribution(folder, "FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MED"));
		this.isolatedComponentSizeMin = new NodeValueList("FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MIN" , readDistribution(folder, "FRAGMENTATION_ISOLATED_COMPONENT_SIZE_MIN"));
		this.numberOfIsolatedComponents = new NodeValueList("FRAGMENTATION_NUMBER_OF_ISOLATED_COMPONENTS" , readDistribution(folder, "FRAGMENTATION_NUMBER_OF_ISOLATED_COMPONENTS"));
		this.largestComponentSize = new NodeValueList("FRAGMENTATION_LARGEST_COMPONENT_SIZE" , readDistribution(folder, "FRAGMENTATION_LARGEST_COMPONENT_SIZE"));
		this.largestComponentSizeFraction = new NodeValueList("FRAGMENTATION_LARGEST_COMPONENT_SIZE_FRACTION" , readDistribution(folder, "FRAGMENTATION_LARGEST_COMPONENT_SIZE_FRACTION"));
		
		return true;
	}
	
	
	protected abstract Partition partition(Graph g, Node[] sorted,
			boolean[] exclude);

}

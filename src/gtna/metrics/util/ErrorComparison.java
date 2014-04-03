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
 * DistributionComparator.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.util;

import gtna.data.NodeValueList;
import gtna.data.Series;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Tim
 * 
 */
public class ErrorComparison extends Metric {

	private Metric metric;
	private Series[] series2;
	private Series[] series1;
	private Series seriesMy;

	private ArrayList<Distribution> distributions = new ArrayList<Distribution>();
	private ArrayList<Distribution> nodevaluelists = new ArrayList<Distribution>();
	private ArrayList<Single> singles = new ArrayList<Single>();
	
	public static final int RUNWITHRUN = 0;
	public static final int BASEWITHRUN = 1;
	
	private int type;

	/**
	 * @param key
	 */
	public ErrorComparison(Metric comparedMetric, Series[] base,
			Series[] changed, int type) {
		super(base[0].getNetwork().getNameShort() + "_COMP_" + comparedMetric.getDescriptionShort());
		String metricKey = base[0].getNetwork().getNameShort() + "_COMP_" + comparedMetric.getDescriptionShort();

		this.type = type;
		
		
		
		Config.appendToList(metricKey, "Error Comparison");
		Config.appendToList(metricKey + "_NAME_LONG", "ErrorComparison");
		Config.appendToList(metricKey + "_NAME_SHORT", "ec");

		Config.appendToList(metricKey + "_DATA_KEYS", "");
		Config.appendToList(metricKey + "_DATA_PLOTS", "");

		Config.appendToList(metricKey + "_SINGLES_KEYS", "");
		Config.appendToList(metricKey + "_SINGLES_PLOTS", "");
		Config.appendToList(metricKey + "_TABLE_KEYS", "");

		this.metric = comparedMetric;
		this.series1 = base;
		this.series2 = changed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if (!applicable()) {
			throw new IllegalArgumentException(
					"No computation possible! The given Series are not containing the specified metric.");
		}
		
		distributions = new ArrayList<Distribution>();
		nodevaluelists = new ArrayList<Distribution>();
		singles = new ArrayList<Single>();

		
		Metric b = getMetric(series1[0].getMetrics(), metric);
		
		Metric c = getMetric(series2[0].getMetrics(), metric);

		if (b == null) {
			throw new IllegalArgumentException(
					"Could not find the metric in the base (1) series");
		}
		if (c == null) {
			throw new IllegalArgumentException(
					"Could not find the metric in the changed (2) series");
		}

		/*
		 * Read Metric values from files.
		 */
		@SuppressWarnings("static-access")
		int r = seriesMy.getCurrentRun();
		if(this.type == RUNWITHRUN){
			b.readData(series1[0].getMetricFolder(r, b));
		} else if (this.type == BASEWITHRUN){
			b.readData(series1[0].getFolder(b));
		} else {
			throw new IllegalArgumentException("Comparison Type not accepted: " + this.type);
		}
		c.readData(series2[0].getMetricFolder(r, c));

		/*
		 * Compare Metric values
		 */
		compareSingles(b.getSingles(), c.getSingles());
		compareDistributions(b.getDistributions(), c.getDistributions());
		compareNodeValueLists(b.getNodeValueLists(), c.getNodeValueLists());

	}

	/**
	 * @param baseNVL
	 * @param changedNVL
	 */
	private void compareNodeValueLists(NodeValueList[] baseNVL,
			NodeValueList[] changedNVL) {
		
		for(NodeValueList bNvl : baseNVL){
			for(NodeValueList cNvl : changedNVL){
				if(bNvl.getKey().equalsIgnoreCase(cNvl.getKey())){
					nodevaluelists.addAll(Arrays.asList(compareNodeValueList(bNvl, cNvl)));
				}
			}
		}

	}
	
	private Distribution[] compareNodeValueList(NodeValueList base, NodeValueList changed){
		// assume: base.length == changed.length
		
		
		return new Distribution[]{};
		
//		double[] borders = computeBorders(base, changed);
//		
//		return this.compareDistribution(base.toDistribution(0.01, borders ), changed.toDistribution(0.01, borders));
		
		
//		double[] bV = base.getValues();
//		double[] cV = changed.getValues();
//		
//		double[] compared = new double[bV.length];
//		double[] comparedAbsolute = new double[bV.length];
//		
//		for(int i = 0; i<bV.length; i++){
//			compared[i] = (cV[i] - bV[i]) / bV[i];
//			comparedAbsolute[i] = (cV[i] - bV[i]);
//		}
//		
//		
//		return new NodeValueList[]{
//				new NodeValueList(base.getKey() + "-relative", compared),
//				new NodeValueList(base.getKey() + "-absolute", comparedAbsolute)
//		};
//		
//		return new NodeValueList[0];
		
	}
	
	

	/**
	 * @param base
	 * @param changed
	 * @return
	 */
	private double[] computeBorders(NodeValueList base, NodeValueList changed) {
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		for(double d : base.getValues()){
			max = (d > max) ? d : max;
			min = (d < min) ? d : min;
		}
		
		for(double d : changed.getValues()){
			max = (d > max) ? d : max;
			min = (d < min) ? d : min;
		}
				
		return new double[] {min, max};
				
	}

	/**
	 * @param baseDistributions
	 * @param changedDistributions
	 */
	private void compareDistributions(Distribution[] baseDistributions,
			Distribution[] changedDistributions) {
	
		// find the corresponding Distributions:
		for (Distribution bD : baseDistributions) {
			for (Distribution cD : changedDistributions) {
				if (bD.getKey().equalsIgnoreCase(cD.getKey())) {
					distributions.addAll(Arrays.asList(compareDistribution(bD,
							cD)));
				}
			}
		}
	}

	/**
	 * @param bD
	 *            base Distribution
	 * @param cD
	 *            changed Distribution
	 * @return
	 */
	private Distribution[] compareDistribution(Distribution bD, Distribution cD) {

		ArrayList<Distribution> compared = new ArrayList<Distribution>();

		double[][] baseValues = bD.getDistribution();
		double[][] changedValues = cD.getDistribution();

		int range = Math.max(baseValues.length, changedValues.length);
		double[][] comparedValues = new double[range][2];
		double[][] absoluteValues = new double[range][2];

		double b; // value of base distribution
		double c; // value of changed distribution
		for (int i = 0; i < range; i++) {

			// get values or set to zero
			if (i < baseValues.length)
				b = baseValues[i][1];
			else
				b = 0;

			if (i < changedValues.length)
				c = changedValues[i][1];
			else
				c = 0;

			comparedValues[i][0] = baseValues[i][0];
			absoluteValues[i][0] = baseValues[i][0];
			comparedValues[i][1] = b - c;
			absoluteValues[i][1] = Math.abs(b-c);
		}

		return new Distribution[] {
			new Distribution(bD.getKey(), comparedValues),
			new Distribution(bD.getKey() + "-absolute", absoluteValues)
		};
	}

	/**
	 * @param bSingles
	 *            - Singles of the metric on the base graph
	 * @param cSingles
	 *            - Singles of the metric on the changed graphs
	 */
	private void compareSingles(Single[] bSingles, Single[] cSingles) {

		// compare single by single by iterating over the base graph singles and
		// searching the changed graph single
		for (Single s1 : bSingles) {
			for (Single s2 : cSingles) {
				if (s1.getKey().equalsIgnoreCase(s2.getKey())) {
					singles.addAll(Arrays.asList(compareSingle(s1, s2)));
					break;
				}
			}
		}

	}

	private Single[] compareSingle(Single s1, Single s2) {
		double v1 = s1.getValue();
		double v2 = s2.getValue();

		Single[] compared = new Single[2];
		compared[0] = new Single(s1.getKey() + "_COMP_R", (v2 - v1) / v1);
		compared[1] = new Single(s1.getKey() + "_COMP_A", v2 - v1);

		return compared;
	}

	/**
	 * @param metrics
	 * @param metric2
	 * @return
	 */
	private Metric getMetric(Metric[] metrics, Metric metric2) {
		for (Metric m : metrics) {
			if (m.getKey().equals(metric2.getKey())) {
				return m;
			}
		}

		return null;
	}

	/**
	 * this metric is applicable if the two series are containing an instance of
	 * the compared metric
	 * 
	 * @return
	 */
	private boolean applicable() {
		Metric[] m1 = series1[0].getMetrics();
		Metric[] m2 = series2[0].getMetrics();

		boolean foundS1 = false;
		boolean foundS2 = false;

		for (Metric m : m1) {
			if (m.getKey().equals(metric.getKey())) {
				foundS1 = true;
				break;
			}
		}

		for (Metric m : m2) {
			if (m.getKey().equals(metric.getKey())) {
				foundS2 = true;
				break;
			}
		}

		return (foundS1 && foundS2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;

		for (Distribution d : distributions) {
			String distrKey = this.key + "_" + d.getKey();
			// add Config for distribution on the fly
			writeDistributionConfig(d.getKey().replace("-absolute", ""), distrKey);
			success &= DataWriter.writeWithoutIndex(d.getDistribution(), distrKey,
					folder);

			// add Config for cdf distribution on the fly
			// writeDistributionCDFConfig(d.getKey(), distrKey);
			// success &= DataWriter.writeWithIndex(d.getCdf(), distrKey +
			// "_CDF", folder);
		}
		
		for (Distribution nvl : nodevaluelists){
			String nvlKey = this.key + "_" + nvl.getKey();
			// add Config for distribution on the fly
//			writeNodeValueListConfig(nvl.getKey().replace("-absolute", "").replace("-relative", ""), nvlKey);
			writeDistributionConfig(nvl.getKey().replace("-absolute", "").replace("-relative", "").replace("-distribution", ""), nvlKey);
			success &= DataWriter.writeWithoutIndex(nvl.getDistribution(), nvlKey,
					folder);
		}

		return success;
	}

	/**
	 * @param distrKey
	 */
	private void writeDistributionCDFConfig(String dKey, String distrKey) {
		if (Config.get(this.key + "_DATA_KEYS") == null
				|| Config.get(this.key + "_DATA_KEYS").equalsIgnoreCase("null")) {
			Config.appendToList(this.key + "_DATA_KEYS", distrKey + "_CDF");
		} else {
			Config.overwrite(
					this.key + "_DATA_KEYS",
					Config.get(this.key + "_DATA_KEYS")
							+ Config.get("CONFIG_LIST_SEPARATOR") + " "
							+ distrKey + "_CDF");
		}
		if (Config.get(this.key + "_DATA_PLOTS") == null
				|| Config.get(this.key + "_DATA_PLOTS")
						.equalsIgnoreCase("null")) {
			Config.appendToList(this.key + "_DATA_PLOTS", distrKey + "_CDF");
		} else {
			Config.overwrite(
					this.key + "_DATA_PLOTS",
					Config.get(this.key + "_DATA_PLOTS")
							+ Config.get("CONFIG_LIST_SEPARATOR") + " "
							+ distrKey + "_CDF");
		}
		Config.appendToList(distrKey + "_CDF_DATA_NAME", dKey + " comparison");
		Config.appendToList(distrKey + "_CDF_DATA_FILENAME",
				Config.get(dKey + "_DATA_FILENAME") + "-comparison"); // replace
																		// _COMP?
		Config.appendToList(distrKey + "_CDF_DATA_IS_CDF", "true");

		Config.appendToList(distrKey + "_CDF_PLOT_DATA", distrKey + "_CDF");
		Config.appendToList(distrKey + "_CDF_PLOT_FILENAME",
				Config.get(dKey + "_CDF_PLOT_FILENAME") + "-comparison");
		Config.appendToList(distrKey + "_CDF_PLOT_TITLE",
				Config.get(dKey + "_CDF_PLOT_TITLE") + " Comparison");
		Config.appendToList(distrKey + "_CDF_PLOT_X",
				Config.get(dKey + "_CDF_PLOT_X"));
		Config.appendToList(distrKey + "_CDF_PLOT_Y", "Difference");
	}

	/**
	 * @param d
	 * @param distrKey
	 */
	private void writeNodeValueListConfig(String originalNvlKey, String nvlKey) {
		if (Config.get(this.key + "_DATA_KEYS") == null
				|| Config.get(this.key + "_DATA_KEYS").equalsIgnoreCase("")) {
			Config.appendToList(this.key + "_DATA_KEYS", nvlKey);
		} else {
			Config.overwrite(this.key + "_DATA_KEYS",
					getExtendedKeyString("_DATA_KEYS", nvlKey));

		}
		if (Config.get(this.key + "_DATA_PLOTS") == null
				|| Config.get(this.key + "_DATA_PLOTS").equalsIgnoreCase("")) {
			Config.appendToList(this.key + "_DATA_PLOTS", nvlKey);
		} else {
			Config.overwrite(this.key + "_DATA_PLOTS",
					getExtendedKeyString("_DATA_PLOTS", nvlKey));
		}
		Config.appendToList(nvlKey + "_DATA_NAME", originalNvlKey + " comparison");

		if(nvlKey.contains("absolute")){
		Config.appendToList(nvlKey + "_DATA_FILENAME",
				Config.get(originalNvlKey + "_DATA_FILENAME") + "-absolute-comparison");
		} else {
			Config.appendToList(nvlKey + "_DATA_FILENAME",
					Config.get(originalNvlKey + "_DATA_FILENAME") + "-comparison");
		}

		Config.appendToList(nvlKey + "_PLOT_DATA", nvlKey);
		
		if(nvlKey.contains("absolute")){
			Config.appendToList(nvlKey + "_PLOT_FILENAME",
					Config.get(originalNvlKey + "_PLOT_FILENAME") + "-absolute-comparison");
			} else {
				Config.appendToList(nvlKey + "_PLOT_FILENAME",
						Config.get(originalNvlKey + "_PLOT_FILENAME") + "-comparison");
			}
		
		Config.appendToList(nvlKey + "_PLOT_TITLE",
				Config.get(originalNvlKey + "_PLOT_TITLE") + " Comparison");
		Config.appendToList(nvlKey + "_PLOT_X", Config.get(nvlKey + "_PLOT_X"));
		Config.appendToList(nvlKey + "_PLOT_Y", "Difference");

	}

	/**
	 * @param d
	 * @param distrKey
	 */
	private void writeDistributionConfig(String dKey, String distrKey) {
		if (Config.get(this.key + "_DATA_KEYS") == null
				|| Config.get(this.key + "_DATA_KEYS").equalsIgnoreCase("")) {
			Config.appendToList(this.key + "_DATA_KEYS", distrKey);
		} else {
			Config.overwrite(this.key + "_DATA_KEYS",
					getExtendedKeyString("_DATA_KEYS", distrKey));

		}
		if (Config.get(this.key + "_DATA_PLOTS") == null
				|| Config.get(this.key + "_DATA_PLOTS").equalsIgnoreCase("")) {
			Config.appendToList(this.key + "_DATA_PLOTS", distrKey);
		} else {
			Config.overwrite(this.key + "_DATA_PLOTS",
					getExtendedKeyString("_DATA_PLOTS", distrKey));
		}
		Config.appendToList(distrKey + "_DATA_NAME", dKey + " comparison");

		if(distrKey.contains("absolute")){
		Config.appendToList(distrKey + "_DATA_FILENAME",
				Config.get(dKey + "_DATA_FILENAME") + "-absolute-comparison");
		} else {
			Config.appendToList(distrKey + "_DATA_FILENAME",
					Config.get(dKey + "_DATA_FILENAME") + "-comparison");
		}

		Config.appendToList(distrKey + "_PLOT_DATA", distrKey);
		
		if(distrKey.contains("absolute")){
			Config.appendToList(distrKey + "_PLOT_FILENAME",
					Config.get(dKey + "_PLOT_FILENAME") + "-absolute-comparison");
			} else {
				Config.appendToList(distrKey + "_PLOT_FILENAME",
						Config.get(dKey + "_PLOT_FILENAME") + "-comparison");
			}
		
		Config.appendToList(distrKey + "_PLOT_TITLE",
				Config.get(dKey + "_PLOT_TITLE") + " Comparison");
		Config.appendToList(distrKey + "_PLOT_X", Config.get(dKey + "_PLOT_X"));
		Config.appendToList(distrKey + "_PLOT_Y", "Difference");

	}
	
	
	
	/**
	 * @param testKey
	 * @return
	 */
	private String getExtendedKeyString(String param, String testKey) {
		String oldKeys = Config.get(this.key + param);
		if (oldKeys.contains(testKey)) {
			return oldKeys;
		} else {
			return oldKeys + Config.get("CONFIG_LIST_SEPARATOR") + " "
					+ testKey;
		}
	}

	/**
	 * The ErrorComparison Metric does not read persisted values.
	 */
	@Override
	public boolean readData(String folder) {
		// TODO currently no reading of values
		return false;
	}

	@Override
	public Single[] getSingles() {

		for (Single s : singles) {
			String cleanKey = s.getKey().replace("_COMP_A", "")
					.replace("_COMP_R", "");

			if (Config.get(cleanKey + "_PLOT_FILENAME") == null) {
				System.out.println("! " + cleanKey + " - " + s.getKey());
			}
			writeSingleConfigGeneral(s);
			writeSingleConfig(s, cleanKey);
		}
		
		ArrayList<Single> combined = new ArrayList<Single>();
		combined.addAll(singles);	
		
		// add derived Singles (min/max/med/avg of distributions)
		for(Distribution d : distributions){
			
			Single dMax = new Single(d.getKey() + "-max", d.getMax());
			Single dMin = new Single(d.getKey() + "-min", d.getMin());
			Single dMed = new Single(d.getKey() + "-med", d.getMedian());
			Single dAvg = new Single(d.getKey() + "-avg", d.getAverage());
			
			writeSingleConfigForDerivedSingle(d, dMax, "max");
			writeSingleConfigForDerivedSingle(d, dMin, "min");
			writeSingleConfigForDerivedSingle(d, dMed, "med");
			writeSingleConfigForDerivedSingle(d, dAvg, "avg");
			
			combined.add(dMax);
			combined.add(dMin);
			combined.add(dMed);
			combined.add(dAvg);
		}
		
		return combined.toArray(new Single[0]);
	}

	private void writeSingleConfigForDerivedSingle(Distribution d, Single s, String type){
		// GENERAL PART - add s.key into the lists
		if (Config.get(this.key + "_SINGLES_KEYS") == null
				|| Config.get(this.key + "_SINGLES_KEYS").equalsIgnoreCase(
						"")) {
			Config.appendToList(this.key + "_SINGLES_KEYS", s.getKey());
		} else {
			Config.overwrite(this.key + "_SINGLES_KEYS",
					getExtendedKeyString("_SINGLES_KEYS", s.getKey()));
		}
		if (Config.get(this.key + "_SINGLES_PLOTS") == null
				|| Config.get(this.key + "_SINGLES_PLOTS").equalsIgnoreCase(
						"")) {
			Config.appendToList(this.key + "_SINGLES_PLOTS", s.getKey());
		} else {
			Config.overwrite(this.key + "_SINGLES_PLOTS",
					getExtendedKeyString("_SINGLES_PLOTS", s.getKey()));
		}
		if (Config.get(this.key + "_TABLE_KEYS") == null
				|| Config.get(this.key + "_TABLE_KEYS")
						.equalsIgnoreCase("")) {
			Config.appendToList(this.key + "_TABLE_KEYS", s.getKey());
		} else {
			Config.overwrite(this.key + "_TABLE_KEYS",
					getExtendedKeyString("_TABLE_KEYS", s.getKey()));
		}
		
		// single specific properties
		Config.appendToList(s.getKey() + "_PLOT_DATA", s.getKey());
	
//		Config.appendToList(s.getKey() + "_PLOT_FILENAME", s.getKey());
		if(d.getKey().contains("-absolute")){
		Config.appendToList(s.getKey() + "_PLOT_FILENAME", Config.get(d.getKey().replace("-absolute", "") + "_PLOT_FILENAME") + "-abs-" + type);
		} else {Config.appendToList(s.getKey() + "_PLOT_FILENAME", Config.get(d.getKey().replace("-absolute", "") + "_PLOT_FILENAME") + "-" + type);
			
		}
		Config.appendToList(s.getKey() + "_PLOT_TITLE", Config.get(d.getKey().replace("-absolute", "") + "_PLOT_TITLE") + " Comparison");
		
		Config.appendToList(s.getKey() + "_PLOT_Y", "");
		
		
	}
	
	/**
	 * @param s
	 * @param cleanKey
	 */
	private void writeSingleConfig(Single s, String cleanKey) {
		Config.appendToList(s.getKey() + "_PLOT_DATA", s.getKey());
		if (s.getKey().contains("_COMP_R")) {
			Config.appendToList(s.getKey() + "_PLOT_FILENAME",
					Config.get(cleanKey + "_PLOT_FILENAME")
							+ "-relative-comparison");
		} else {
			Config.appendToList(s.getKey() + "_PLOT_FILENAME",
					Config.get(cleanKey + "_PLOT_FILENAME")
							+ "-absolute-comparison");
		}
		Config.appendToList(s.getKey() + "_PLOT_TITLE",
				Config.get(cleanKey + "_PLOT_TITLE") + " Comparison");
		if (s.getKey().contains("_COMP_R")) {
			Config.appendToList(s.getKey() + "_PLOT_Y", "Relative Difference");
		} else {
			Config.appendToList(s.getKey() + "_PLOT_Y", "Absolute Difference");
		}
	}

	/**
	 * @param s
	 */
	private void writeSingleConfigGeneral(Single s) {
		if (Config.get(this.key + "_SINGLES_KEYS") == null
				|| Config.get(this.key + "_SINGLES_KEYS").equalsIgnoreCase(
						"")) {
			Config.appendToList(this.key + "_SINGLES_KEYS", s.getKey());
		} else {
			Config.overwrite(this.key + "_SINGLES_KEYS",
					getExtendedKeyString("_SINGLES_KEYS", s.getKey()));
		}
		if (Config.get(this.key + "_SINGLES_PLOTS") == null
				|| Config.get(this.key + "_SINGLES_PLOTS").equalsIgnoreCase(
						"")) {
			Config.appendToList(this.key + "_SINGLES_PLOTS", s.getKey());
		} else {
			Config.overwrite(this.key + "_SINGLES_PLOTS",
					getExtendedKeyString("_SINGLES_PLOTS", s.getKey()));
		}
		if (Config.get(this.key + "_TABLE_KEYS") == null
				|| Config.get(this.key + "_TABLE_KEYS")
						.equalsIgnoreCase("")) {
			Config.appendToList(this.key + "_TABLE_KEYS", s.getKey());
		} else {
			Config.overwrite(this.key + "_TABLE_KEYS",
					getExtendedKeyString("_TABLE_KEYS", s.getKey()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getDistributions()
	 */
	@Override
	public Distribution[] getDistributions() {
		return distributions.toArray(new Distribution[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getNodeValueLists()
	 */
	@Override
	public NodeValueList[] getNodeValueLists() {
		return nodevaluelists.toArray(new NodeValueList[0]);
	}

}

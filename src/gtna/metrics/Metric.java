/*
 * ===========================================================
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
 * Metric.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-03 : readData, getDistributions(), getNodeValueLists() (Tim Grube)
 */
package gtna.metrics;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Metric extends ParameterList {

	public Metric(String key) {
		this(key, new Parameter[0]);
	}

	public Metric(String key, Parameter[] parameters) {
		super(key, parameters);
	}

	/**
	 * keys of all multi-scalar plots
	 * 
	 * @return keys of all multi-scalar plots
	 */
	public String[] getDataPlotKeys() {
		return Config.keys(this.key + "_DATA_PLOTS");
	}

	/**
	 * keys of all single-scalar plots
	 * 
	 * @return keys of all single-scalar plots
	 */
	public String[] getSinglePlotKeys() {
		return Config.keys(this.key + "_SINGLES_PLOTS");
	}

	/**
	 * data keys of this metric
	 * 
	 * @return data keys of this metric
	 */
	public String[] getDataKeys() {
		return Config.keys(this.key + "_DATA_KEYS");
	}

	/**
	 * single-scalar value keys
	 * 
	 * @return single-scalar valcue keys
	 */
	public String[] getSingleKeys() {
		return Config.keys(this.key + "_SINGLES_KEYS");
	}

	/**
	 * computes all metric-specific data<br>
	 * has to be called before any other method
	 * 
	 * @param g
	 */
	public abstract void computeData(Graph g, Network n,
			HashMap<String, Metric> m);

	/**
	 * writes all generated data to the specified folder
	 * 
	 * @param folder
	 * @return true if operation is successful
	 */
	public abstract boolean writeData(String folder);
	
	/**
	 * reads all persisted data from the specified folder
	 * 
	 * @param folder
	 * @return true if operation is successful
	 */
	public abstract boolean readData(String folder);

	/**
	 * all single-scalar values generated / computed by this metric
	 * 
	 * @param values
	 * @return single scalar values
	 */
	public abstract Single[] getSingles();
	
	/**
	 * all distributions (multi-values) generated / computed by this metric
	 * @return distributions
	 * 
	 */
	public abstract Distribution[] getDistributions();
	
	/**
	 * 
	 * all node-value lists (multi-values) generated / computed by this metric
	 * @return node-value lists
	 */
	public abstract NodeValueList[] getNodeValueLists();
	
	public String getRuntimeSingleName(){
		return this.getFolderName() + "_RUNTIME";
	}

	public abstract boolean applicable(Graph g, Network n,
			HashMap<String, Metric> m);

	/**
	 * @param folder
	 * @param run
	 * @return
	 */
	protected String pathForRun(String folder, String run) {
		String[] path = folder.trim().split(Config.get("FILESYSTEM_FOLDER_DELIMITER"));
		ArrayList<String> p = new ArrayList<String>();
		for(String s : path){
			p.add(s);
			p.add(Config.get("FILESYSTEM_FOLDER_DELIMITER"));
		}
		
		
		p.add(p.size()-2, run);
		p.add(p.size()-2, Config.get("FILESYSTEM_FOLDER_DELIMITER"));		
		StringBuilder sb = new StringBuilder();
		
		for(String s : p){
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * @param folder
	 * @param metrickey
	 * @return
	 */
	protected double[] readDistribution(String folder, String metrickey) { // TODO replace by read 2D Vals?
		String filename = DataWriter.filename(metrickey, folder);		
		double[][] distributionValues = DataReader.readDouble2D(filename);		
		double[] distribution = new double[distributionValues.length];
		for(int i = 0; i < distributionValues.length; i++){
			distribution[i] = distributionValues[i][1];
		}
		return distribution;
	}

	/**
	 * @param folder
	 * @param metrickey
	 * @return
	 */
	protected double[][] read2DValues(String folder, String metrickey) {
		String filename = DataWriter.filename(metrickey, folder);		
		double[][] val2D = DataReader.readDouble2D(filename);
		
		return val2D;
	}
}

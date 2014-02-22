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
 * Connectivity.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-03 : readData, getNodeValueList, getDistributions (Tim Grube)
 *
 */
package gtna.metrics.connectivity;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.partition.Partition;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public abstract class Partitioning extends Metric {
	private double largestComponent;

	private double largestComponentFraction;

	private NodeValueList components;

	private NodeValueList componentsFraction;

	public Partitioning(String key) {
		super(key);
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Partition p = this.getPartition(g);
		this.largestComponent = p.getComponents()[0].length;
		this.largestComponentFraction = this.largestComponent
				/ (double) g.getNodes().length;
		
		double[] componentsArray = new double[p.getComponents().length];
		double[] componentsFractionArray = new double[p.getComponents().length];
		for (int i = 0; i < p.getComponents().length; i++) {
			componentsArray[i] = p.getComponents()[i].length;
			componentsFractionArray[i] = componentsArray[i]
					/ (double) g.getNodes().length;
		}
		
		this.components = new NodeValueList(getComponentsKey(), componentsArray);
		this.componentsFraction = new NodeValueList(getComponentsFractionKey(), componentsFractionArray);
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.components.getValues(), getComponentsKey(), folder);
		success &= DataWriter.writeWithIndex(this.componentsFraction.getValues(),
				getComponentsFractionKey(), folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single largestComponent = new Single(this.getKey()
				+ "_LARGEST_COMPONENT", this.largestComponent);
		Single largestComponentFraction = new Single(this.getKey()
				+ "_LARGEST_COMPONENT_FRACTION", this.largestComponentFraction);
		return new Single[] { largestComponent, largestComponentFraction };
	}
	
	@Override
	public NodeValueList[] getNodeValueLists(){
		return new NodeValueList[] {components, componentsFraction};
	}
	
	@Override
	public Distribution[] getDistributions(){
		return new Distribution[0];
	}
	
	@Override 
	public boolean readData(String folder){
		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
				if((this.getKey()
						+ "_LARGEST_COMPONENT").equals(single[0])){
					this.largestComponent = Double.valueOf(single[1]);
				} else if((this.getKey()
						+ "_LARGEST_COMPONENT_FRACTION").equals(single[0])){
					this.largestComponentFraction = Double.valueOf(single[1]);					
				} 
			}
		}
		
		
		/* NODE VALUE LIST */
		
		components = new NodeValueList(getComponentsKey(), readDistribution(folder, getComponentsKey()));
		componentsFraction = new NodeValueList(getComponentsFractionKey(), readDistribution(folder, getComponentsFractionKey())); 
		
		
		
		return true;
	}

	/**
	 * @return
	 */
	private String getComponentsFractionKey() {
		return this.getKey() + "_COMPONENTS_FRACTION";
	}

	/**
	 * @return
	 */
	private String getComponentsKey() {
		return this.getKey()	+ "_COMPONENTS";
	}

	protected abstract Partition getPartition(Graph g);

}

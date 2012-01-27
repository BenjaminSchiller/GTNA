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
 * ResilienceNodeFailureWeak.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.resilience;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricImpl;
import gtna.metrics.WeakConnectivity;
import gtna.networks.Network;
import gtna.transformation.failure.Deleted;
import gtna.transformation.failure.NodeFailure;
import gtna.util.Timer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * abstract metric for resilience of graphs in terms of weak connectivity
 * here: resilience = number of nodes need to be removed so that each component of the graph
 * contains at most half the nodes
 * @author stef
 *
 */
public abstract class ResilienceNodeFailureWeak extends MetricImpl {

	 protected NodeFailure failure;
     private int deleted;
     private double fraction;
     private double[] sequence;
     private Timer runtime;
	
	/**
	 * @param key
	 */
	public ResilienceNodeFailureWeak(String key) {
		super(key);
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		runtime = new Timer();
		WeakConnectivity conn = new WeakConnectivity();
		 boolean halfed = false;
        int oldsize = g.getNodes().length;
        int newsize;
        int count = 0;
        int onePercent = Math.max(oldsize/100,1);
		this.initFailure(onePercent);
        while (!halfed && count < g.getNodes().length){
        	g.removeProperty("WEAK_CONNECTIVITY_PARTITION_0");
        	conn.computeData(g, n, m);
    		newsize = (int) conn.getValues()[0].value;
    		if (newsize <= oldsize/2){
    			halfed = true;
    			break;
    		}
    		g = this.failure.transform(g);
    		count = count + onePercent;
    		oldsize = oldsize - onePercent;
        }
        boolean found = false;
        //System.out.println(count);
        if (count == 0){
        	this.deleted = 0;
        	this.fraction = 0;
        	sequence = new double[0];
        } else {
        	GraphProperty[] prop = g.getProperties("Deleted");
        	Deleted deleted = (Deleted) prop[prop.length-1];
        	ArrayList<Integer> list = deleted.getListDeleted();
        	boolean[] isDeleted = deleted.getDeleted();
        	while (!found){
        		count--;
        		isDeleted[list.get(count)] = false;
        		g.removeProperty("WEAK_CONNECTIVITY_PARTITION_0");
        		conn.computeData(g, n, m);
        		newsize = (int) conn.getValues()[0].value;
        		oldsize++;
        		if (newsize > oldsize/2){
        			found = true;
        		}
        	}
        	this.sequence = new double[count];
        	for (int j = 0; j < count; j++){
        		this.sequence[j] = list.get(j);
        	}
        	this.deleted = this.sequence.length;
        	this.fraction = (double)this.deleted/(double)g.getNodes().length;
        	deleted.setClosed(true);
        }
        runtime.end();
	}

	/**
	 * init the failure transformation
	 */
	protected abstract void initFailure(int nr);
	

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		return DataWriter.writeWithIndex(this.sequence, this.key()+"_SEQUENCE", folder);
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getValues()
	 */
	@Override
	public Value[] getValues() {
		// TODO Auto-generated method stub
		return new Value[] {new Value(this.key() + "_DELETED", this.deleted), new Value(this.key() + "_FRACTION", this.fraction), new Value(this.key() + "_RUNTIME", this.runtime.getRuntime())};
	}

}

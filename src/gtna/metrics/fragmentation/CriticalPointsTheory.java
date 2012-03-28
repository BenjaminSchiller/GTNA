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
 * CriticalPoints.java
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
package gtna.metrics.fragmentation;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Timer;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashMap;

/**
 * @author stef
 *
 */
public class CriticalPointsTheory extends Metric {
	private boolean dir;
	//different types of node selection
	public static String RANDOM = "RANDOM"; //random deletion of nodes
	public static String LARGEST = "LARGEST"; //deletion of nodes with highest degree
	public static String LARGESTOUT = "LARGESTOUT"; //deletion of nodes with highest out-degree
	public static String LARGESTIN = "LARGESTIN"; //deletion of nodes with highest out-degree
	public static String DEGREEBOUND = "DEGREEBOUND"; //deletion of edges of nodes so that maximal degree is bounded: only undirected
	private double p;
	private double deg;
	private Timer runtime;
	private String t;
	
	public CriticalPointsTheory(boolean directed, String type){
		super("CRITICAL_POINTS", new Parameter[]{new BooleanParameter("DIRECTED",directed), new StringParameter("TYPE", type)});
		this.dir = directed;
		this.t = type;
	}
	
	

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.runtime = new Timer();
		Node[] nodes = g.getNodes();
        if (this.dir){
        	//directed: determine matrix of out-in-degree
        	int maxOut = 0;
        	int maxIn = 0;
        	for (int i = 0; i < nodes.length; i++){
        		if (nodes[i].getInDegree() > maxIn){
        			maxIn = nodes[i].getInDegree();
        		}
        		if (nodes[i].getOutDegree() > maxOut){
        			maxOut = nodes[i].getOutDegree();
        		}
        	}
        	double[][] dist = new double[maxOut+1][maxIn+1];
        	for (int i = 0; i < nodes.length; i++){
        		dist[nodes[i].getOutDegree()][nodes[i].getInDegree()]++;
        	}
        	for (int i = 0; i < dist.length; i++){
        		for (int j = 0; j < dist[i].length; j++){
        			dist[i][j] = dist[i][j]/(double)nodes.length;
        		}
        	}
        	//iterate over different deletion methods
        	boolean success = false;
        	if (this.t.equals(RANDOM)){
        		success = true;
        		this.deg = Math.max(maxIn, maxOut);
        		double[] r = getk0Directed(dist);
        		if (r[0] - r[1] <= 0){
        			this.p = 0;
        		}else {
        			this.p = (r[0]-r[1])/r[0];
        		}
        	}
        	if (this.t.equals(LARGEST)){
        		success = true;
        		double[] r = getCPDirectedLargest(dist);
        		this.p = r[0];
        		this.deg = r[1];
        	}
        	if (this.t.equals(LARGESTOUT)){
        		success = true;
        		double[] r = getCPDirectedLargestOut(dist);
        		this.p = r[0];
        		this.deg = r[1];
        	}
        	if (this.t.equals(LARGESTIN)){
        		success = true;
        		double[] r = getCPDirectedLargestIn(dist);
        		this.p = r[0];
        		this.deg = r[1];
        	}
        	if (!success){
        		throw new IllegalArgumentException("Unknown deletion type " + this.t + " directed: " + this.dir + " in CriticalPointsTheory");
        	}
        } else {
        	//undirected => derive degree vector
        	int max = 0;
        	for (int i = 0; i < nodes.length; i++){
        		if (nodes[i].getOutDegree() > max){
        			max = nodes[i].getOutDegree();
        		}
        	}
        	double[] dd = new double[max+1];
        	double sum = 0;
        	for (int i = 0; i < nodes.length; i++){
        		dd[nodes[i].getOutDegree()]++;
        		sum = sum + nodes[i].getOutDegree();
        	}
        	for (int i = 0; i < dd.length; i++){
        		dd[i] = dd[i]/sum;
        	}
        	//iterate over all possible deletion methods
        	double[] res = null; 
        	if(this.t.equals(RANDOM)){
        		res = getCPUndirectedRandom(dd);
        	}
        	if(this.t.equals(LARGEST)){
        		res = getCPUndirectedLargest(dd);
        	}
        	if(this.t.equals(DEGREEBOUND)){
        		res = getCPUndirectedMaxDegree(dd);
        	}
        	if (res == null){
        		throw new IllegalArgumentException("Unknown deletion type " + this.t + " directed: " + this.dir + " in CriticalPointsTheory");
        	}
        	this.p = res[0];
        	this.deg = res[1];
        }
        this.runtime.end();
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		return true;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		//crirtical point
		Single pR = new Single("CRITICAL_POINTS_CP",
				this.p);
		//maximal degree of remaining nodes
		Single degR = new Single("CRITICAL_POINTS_DEG",
				this.deg);
		Single runtime = new Single("CRITICAL_POINTS_RUNTIME",
				this.runtime.getRuntime());
		return new Single[]{pR,degR,runtime};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}
	
	private static double[] getCPUndirectedRandom(double[] dd){
		double k0 = getk0(dd);
		double r = 1 - 1/(k0-1);
		if (r < 0){
			return new double[]{0,dd.length-1};
		}else {
			return new double[]{r,dd.length-1};
		}
	}
	
	public static double[] getCPUndirectedLargest(double[] dist){
		double k0 = getk0(dist);
		double av = 0;
		for (int i = 1; i < dist.length; i++){
			av = av + dist[i]*i;
		}
		double r = 1 - 1/(k0-1);
		if (r < 0){
			return new double[]{0,dist.length-1};
		}else {
			double p = 0;
			double[] distN = new double[dist.length];
			double edgef = 0;
			double fpN;
			for (int j = dist.length-1; j > 0; j--){
				p = p + dist[j];
				edgef = edgef + dist[j]*j/(av);
				for (int i = 0; i < j; i++){
					distN[i] = dist[i]/(1-p);
				}
               for (int i = j; i < dist.length; i++){
            	   distN[i] = 0;
               }
			   k0 = getk0(distN,j-1);
			   fpN = 1 - 1/(k0-1);
			   if (edgef > fpN){
					return new double[]{p,j};
				}
			}
		}
		return new double[]{1,1};
	}
	
	public static double[] getCPDirectedLargest(double[][] dist){
		double av = 0;
		for (int i = 0; i < dist.length; i++){
			for (int j = 0; j < dist[i].length; j++){
				av = av + (i+j)*dist[i][j];
			}
		}
		double p = 0;
		double edgef = 0;
		double fpN;
		int degree = dist.length + dist[0].length-2;
		for (int j = 0; j < dist.length; j++){
			for (int i = 0; i < j+1; i++){
				if (dist[0].length -1 -j + i > -1){
				p = p + dist[dist.length-1-i][dist[0].length-1-j+i];
				edgef = edgef + dist[dist.length-1-i][dist[0].length-1-j+i]*degree/(av);
				}
			}
			
			double[] r = getk0Directed(dist,degree,Integer.MAX_VALUE, Integer.MAX_VALUE);
			fpN = (2*r[0]-r[1]-r[2])/(2*r[0]);
			if (edgef > fpN){
				return new double[]{p,degree-1};
			}
			degree--;
		}
		return new double[]{1,1};
	}
	
	public static double[] getCPDirectedLargestOut(double[][] dist){
		double av = 0;
		for (int i = 0; i < dist.length; i++){
			for (int j = 0; j < dist[i].length; j++){
				av = av + i*dist[i][j];
			}
		}
		double p = 0;
		double edgef = 0;
		double fpN;
		for (int j = dist.length-1; j > 0; j--){
			for (int i = 0; i < dist[j].length; i++){
				p = p + dist[j][i];
				edgef = edgef + dist[j][i]*j/(av);
			}
			double[] r = getk0Directed(dist,Integer.MAX_VALUE,j, Integer.MAX_VALUE);
			fpN = (2*r[0]-r[1]-r[2])/(2*r[0]-r[2]);
			if (edgef > fpN){
				return new double[]{p,j-1};
			}
		}
		return new double[]{1,1};
	}
	
	public static double[] getCPDirectedLargestIn(double[][] dist){
		double av = 0;
		for (int i = 0; i < dist.length; i++){
			for (int j = 0; j < dist[i].length; j++){
				av = av + (j)*dist[i][j];
			}
		}
		double p = 0;
		double edgef = 0;
		double fpN;
		for (int j = dist[0].length-1; j > 0; j--){
			for (int i = 0; i < dist.length; i++){
				p = p + dist[i][j];
				edgef = edgef + dist[i][j]*j/(av);
			}
			double[] r = getk0Directed(dist,Integer.MAX_VALUE, Integer.MAX_VALUE,j);
			fpN = (2*r[0]-r[1]-r[2])/(2*r[0]-r[1]);
			if (edgef > fpN){ 
				return new double[]{p,j-1};
			}
		}
		return new double[]{1,1};
	}
	
	
	public static double[] getCPUndirectedMaxDegree(double[] dist){
		double k0 = getk0(dist);
		double av = 0;
		for (int i = 1; i < dist.length; i++){
			av = av + dist[i]*i;
		}
		double r = 1 - 1/(k0-1);
		if (r < 0){
			return new double[]{0,dist.length-1};
		}else {
			double p = 0;
			double[] distN = dist.clone();
			double edgef = 0;
			double fpN;
			for (int j = dist.length-1; j > 0; j--){
				p = p + dist[j];
				edgef = edgef + p/(av);
				distN[j-1] = dist[j-1]+p;
				for (int i = j; i < dist.length; i++){
            	   distN[i] = 0;
               }
			   k0 = getk0(distN,j-1);
			   fpN = 1 - 1/(k0-1);
			   if (edgef > fpN){
					return new double[]{p,j};
				}
			}
		}
		return new double[]{1,1};
	}
	
	
	/**
	 * criterion for a giant component in an undirected graph:
	 * @param dist: degree distribution
	 * @param bound: maximal degree
	 * @return
	 */
	private static double getk0(double[] dist, int bound){
		double firstMoment = 0;
		double secondMoment = 0;
		for (int i = 0; i < Math.min(dist.length,bound+1); i++){
			firstMoment = firstMoment+ i*dist[i];
			secondMoment = secondMoment+ i*i*dist[i];
		}
		return secondMoment/firstMoment;
	}
	
	private static double getk0(double[] dist){
		return getk0(dist,dist.length);
	}
	
	/**
	 * criterion for giant component directed
	 * @param dist: degree distribution
	 * @param bound: upper bound on degree
	 * @param boundOut: upper bound on out degree
	 * @param boundIn: upper bound on in degree
	 * @return
	 */
	private static double[] getk0Directed(double[][] dist, int bound, int boundOut, int boundIn){
		double jk = 0;
		double j = 0;
		double k = 0;
		for (int i = 1; i < Math.min(Math.min(dist.length,bound),boundOut); i++){
			for (int i2 = 1; i2 < Math.min(Math.min(dist[i].length,bound-i),boundIn);i2++){
				jk = jk + i*i2*dist[i][i2];
				j = j + i*dist[i][i2];
				k = k + i2*dist[i][i2];
			}
		}
		return new double[]{jk,j,k};
	}
	
	private static double[] getk0Directed(double[][] dist){
		return getk0Directed(dist,Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

}

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
	private String[] types;
	public static String random = "RANDOM";
	public static String largest = "LARGEST";
	public static String maxdegree = "MAXDEGREE";
	private double pRand;
	private double pLargest;
	private double pMaxdegree;
	private double degLargest;
	private double degMaxdegree;
	private Timer runtime;
	
	public CriticalPointsTheory(boolean directed){
		super("CRITICAL_POINTS", new Parameter[]{new BooleanParameter("DIRECTED",directed)});
		this.dir = directed;
		//this.types = types;
	}
	
	private static Parameter[] makelist(String[] types, boolean dir){
		Parameter[] res = new Parameter[2];
		res[0] = new BooleanParameter("DIRECTED",dir);
		String t = "";
		if (types.length > 0){
		  t = types[0];
		  for (int i = 1; i < types.length; i++){
			  t = t + "-"+types[i];
		  }
		}	
		res[1] = new BooleanParameter("DIRECTED",dir);
		res[2] = new StringParameter("TYPE",t);
		return res;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.runtime = new Timer();
		Node[] nodes = g.getNodes();
        if (this.dir){
        	
        } else {
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
        	this.pRand = this.getCPUndirectedRandom(dd.clone());
        	double[] res = this.getCPUndirectedLargest(dd.clone());
        	this.pLargest = res[0];
        	this.degLargest = res[1];
        	res = this.getCPUndirectedMaxDegree(dd);
        	this.pMaxdegree = res[0];
        	this.degMaxdegree = res[1];
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
		Single pR = new Single("CRITICAL_POINTS_CP_RANDOM",
				this.pRand);
		Single pL = new Single("CRITICAL_POINTS_CP_LARGEST",
				this.pLargest);
		Single pM = new Single("CRITICAL_POINTS_CP_MAXDEGREE",
				this.pMaxdegree);
		Single degL = new Single("CRITICAL_POINTS_DEG_LARGEST",
				this.degLargest);
		Single degM = new Single("CRITICAL_POINTS_DEG_MAXDEGREE",
				this.degMaxdegree);
		Single runtime = new Single("CRITICAL_POINTS_RUNTIME",
				this.runtime.getRuntime());
		return new Single[]{pR,pL,pM,degL,degM,runtime};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}
	
	private double getCPUndirectedRandom(double[] dd){
		double k0 = getk0(dd);
		double r = 1 - 1/(k0-1);
		if (r < 0){
			return 0;
		}else {
			return r;
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

}

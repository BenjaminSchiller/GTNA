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
 * DegreeDistributionComparator.java
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

import gtna.data.Single;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.util.Distribution;

import java.util.Arrays;

/**
 * @author Tim
 *
 */
public class DegreeDistributionComparator extends DistributionComparator {

	private Distribution diffRelDDo;
	private Distribution diffRelDDi;
	private Distribution diffRelDD;
	private Distribution diffAbsDD;
	private Distribution diffAbsDDi;
	private Distribution diffAbsDDo;

	/**
	 * @param key
	 * @param refMetric
	 */
	public DegreeDistributionComparator(String key, Metric refMetric) {
		super(key, refMetric);
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.util.DistributionComparator#computeAbsolute()
	 */
	@Override
	public void computeAbsolute() {
		if(!(comparedMetric instanceof DegreeDistribution) ||
				!(referenceMetric instanceof DegreeDistribution))
			throw new IllegalArgumentException("Both metrics have to be an instance of a DegreeDistribution");
		
		Distribution refDD = ((DegreeDistribution)referenceMetric).getDegreeDistribution();
		Distribution refDDi = ((DegreeDistribution)referenceMetric).getInDegreeDistribution();
		Distribution refDDo = ((DegreeDistribution)referenceMetric).getOutDegreeDistribution();
	
		Distribution comDD = ((DegreeDistribution)comparedMetric).getDegreeDistribution();
		Distribution comDDi = ((DegreeDistribution)comparedMetric).getInDegreeDistribution();
		Distribution comDDo = ((DegreeDistribution)comparedMetric).getOutDegreeDistribution();
		
		
		diffAbsD = getAbsoluteErrorDistribution(refDD, comDD);
		diffAbsDi = getAbsoluteErrorDistribution(refDDi, comDDi);
		diffAbsDo = getAbsoluteErrorDistribution(refDDo, comDDo);
		
		diffAbsDD = new Distribution(diffAbsD);
		diffAbsDDi = new Distribution(diffAbsDi);
		diffAbsDDo = new Distribution(diffAbsDo);
	}

	

	/* (non-Javadoc)
	 * @see gtna.metrics.util.DistributionComparator#computeRelative()
	 */
	@Override
	public void computeRelative() {
		if(!(comparedMetric instanceof DegreeDistribution) ||
				!(referenceMetric instanceof DegreeDistribution))
			throw new IllegalArgumentException("Both metrics have to be an instance of a DegreeDistribution");
		
		Distribution refDD = ((DegreeDistribution)referenceMetric).getDegreeDistribution();
		Distribution refDDi = ((DegreeDistribution)referenceMetric).getInDegreeDistribution();
		Distribution refDDo = ((DegreeDistribution)referenceMetric).getOutDegreeDistribution();
	
		Distribution comDD = ((DegreeDistribution)comparedMetric).getDegreeDistribution();
		Distribution comDDi = ((DegreeDistribution)comparedMetric).getInDegreeDistribution();
		Distribution comDDo = ((DegreeDistribution)comparedMetric).getOutDegreeDistribution();
		
		
		double[] diffRelD = getRelativeErrorDistribution(refDD, comDD);
		double[] diffRelDi = getRelativeErrorDistribution(refDDi, comDDi);
		double[] diffRelDo = getRelativeErrorDistribution(refDDo, comDDo);
		
		diffRelDD = new Distribution(diffRelD);
		diffRelDDi = new Distribution(diffRelDi);
		diffRelDDo = new Distribution(diffRelDo);
	}

	/**
	 * @param refDD
	 * @param comDD
	 * @return
	 */
	private double[] getRelativeErrorDistribution(Distribution refDD, Distribution comDD) {
		double[] rd = new double[refDD.getDistribution().length];
		Arrays.fill(rd, 0);
		double[] ref = refDD.getDistribution();
		double[] com = comDD.getDistribution();
		
		for(int i = 0; i < ref.length; i++){
			if(i < com.length){
				rd[i] = 1 - com[i]/ref[i];
			} 
		}
		
		return rd;
		
	}
	
	
	/**
	 * @param refDD
	 * @param comDD
	 * @return
	 */
	private double[] getAbsoluteErrorDistribution(Distribution refDD,
			Distribution comDD) {
		double[] ad = new double[refDD.getDistribution().length];
		Arrays.fill(ad, 0);
		double[] ref = refDD.getDistribution();
		double[] com = comDD.getDistribution();
		
		for(int i = 0; i < ref.length; i++){
			if(i < com.length){
				ad[i] = ref[i]-com[i];
			} 
		}
		
		return ad;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		// TODO Auto-generated method stub
		return null;
	}

}

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
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.io.DataWriter;
=======
>>>>>>> computation of the error distributions
=======
import gtna.io.DataWriter;
>>>>>>> create Single/Multi values
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
<<<<<<< HEAD
<<<<<<< HEAD
	public DegreeDistributionComparator(Metric refMetric) {
		super("DEGREE_DISTRIBUTION_COMPARATOR", refMetric);
=======
	public DegreeDistributionComparator(String key, Metric refMetric) {
		super(key, refMetric);
>>>>>>> computation of the error distributions
=======
	public DegreeDistributionComparator(Metric refMetric) {
		super("DEGREE_DISTRIBUTION_COMPARATOR", refMetric);
>>>>>>> create Single/Multi values
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
		
		
<<<<<<< HEAD
<<<<<<< HEAD
		double[] diffAbsD = getAbsoluteErrorDistribution(refDD, comDD);
		double[] diffAbsDi = getAbsoluteErrorDistribution(refDDi, comDDi);
		double[] diffAbsDo = getAbsoluteErrorDistribution(refDDo, comDDo);
=======
		diffAbsD = getAbsoluteErrorDistribution(refDD, comDD);
		diffAbsDi = getAbsoluteErrorDistribution(refDDi, comDDi);
		diffAbsDo = getAbsoluteErrorDistribution(refDDo, comDDo);
>>>>>>> computation of the error distributions
=======
		double[] diffAbsD = getAbsoluteErrorDistribution(refDD, comDD);
		double[] diffAbsDi = getAbsoluteErrorDistribution(refDDi, comDDi);
		double[] diffAbsDo = getAbsoluteErrorDistribution(refDDo, comDDo);
>>>>>>> var types
		
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
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> create Single/Multi values
		boolean success = true;
		
		success &= DataWriter.writeWithIndex(this.diffAbsDD.getDistribution(),
				"DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.diffAbsDD.getCdf(),
				"DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(this.diffAbsDDi.getDistribution(),
				"DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.diffAbsDDi.getCdf(),
				"DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(this.diffAbsDDo.getDistribution(),
				"DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.diffAbsDDo.getCdf(),
				"DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_DISTRIBUTION_CDF", folder);
		
		success &= DataWriter.writeWithIndex(this.diffRelDD.getDistribution(),
				"DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.diffRelDD.getCdf(),
				"DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(this.diffRelDDi.getDistribution(),
				"DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_IN_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.diffRelDDi.getCdf(),
				"DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_IN_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(this.diffRelDDo.getDistribution(),
				"DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_OUT_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.diffRelDDo.getCdf(),
				"DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_OUT_DISTRIBUTION_CDF", folder);
		
		return success;
		
<<<<<<< HEAD
=======
		// TODO Auto-generated method stub
		return false;
>>>>>>> computation of the error distributions
=======
>>>>>>> create Single/Multi values
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> create Single/Multi values
		Single ddAbsMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_MIN",
				this.diffAbsDD.getMin());
		Single ddAbsMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_MED",
				this.diffAbsDD.getMedian());
		Single ddAbsAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_AVG",
				this.diffAbsDD.getAverage());
		Single ddAbsMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_MAX",
				this.diffAbsDD.getMax());

		Single ddiAbsMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_MIN",
				this.diffAbsDDi.getMin());
		Single ddiAbsMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_MED",
				this.diffAbsDDi.getMedian());
		Single ddiAbsAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_AVG",
				this.diffAbsDDi.getAverage());
		Single ddiAbsMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_MAX",
				this.diffAbsDDi.getMax());

		Single ddoAbsMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_MIN",
				this.diffAbsDDo.getMin());
		Single ddoAbsMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_MED",
				this.diffAbsDDo.getMedian());
		Single ddoAbsAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_AVG",
				this.diffAbsDDo.getAverage());
		Single ddoAbsMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_MAX",
				this.diffAbsDDo.getMax());
		
<<<<<<< HEAD
		Single ddRelMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_MIN",
				this.diffRelDD.getMin());
		Single ddRelMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_MED",
				this.diffRelDD.getMedian());
		Single ddRelAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_AVG",
				this.diffRelDD.getAverage());
		Single ddRelMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_MAX",
				this.diffRelDD.getMax());

		Single ddiRelMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_IN_MIN",
				this.diffRelDDi.getMin());
		Single ddiRelMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_IN_MED",
				this.diffRelDDi.getMedian());
		Single ddiRelAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_IN_AVG",
				this.diffRelDDi.getAverage());
		Single ddiRelMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_IN_MAX",
				this.diffRelDDi.getMax());

		Single ddoRelMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_OUT_MIN",
				this.diffRelDDo.getMin());
		Single ddoRelMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_OUT_MED",
				this.diffRelDDo.getMedian());
		Single ddoRelAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_OUT_AVG",
				this.diffRelDDo.getAverage());
		Single ddoRelMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_RELATIVE_OUT_MAX",
=======
		Single ddRelMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_MIN",
				this.diffRelDD.getMin());
		Single ddRelMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_MED",
				this.diffRelDD.getMedian());
		Single ddRelAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_AVG",
				this.diffRelDD.getAverage());
		Single ddRelMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_MAX",
				this.diffRelDD.getMax());

		Single ddiRelMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_MIN",
				this.diffRelDDi.getMin());
		Single ddiRelMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_MED",
				this.diffRelDDi.getMedian());
		Single ddiRelAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_AVG",
				this.diffRelDDi.getAverage());
		Single ddiRelMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_IN_MAX",
				this.diffRelDDi.getMax());

		Single ddoRelMin = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_MIN",
				this.diffRelDDo.getMin());
		Single ddoRelMed = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_MED",
				this.diffRelDDo.getMedian());
		Single ddoRelAvg = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_AVG",
				this.diffRelDDo.getAverage());
		Single ddoRelMax = new Single("DEGREE_DISTRIBUTION_COMPARATOR_ABSOLUTE_OUT_MAX",
>>>>>>> create Single/Multi values
				this.diffRelDDo.getMax());

		return new Single[] { ddAbsMin, ddAbsMed, ddAbsAvg, ddAbsMax, 
				ddiAbsMin, ddiAbsMed, ddiAbsAvg, ddiAbsMax, 
				ddoAbsMin, ddoAbsMed, ddoAbsAvg, ddoAbsMax,
				ddRelMin, ddRelMed, ddRelAvg, ddRelMax, 
				ddiRelMin, ddiRelMed, ddiRelAvg, ddiRelMax, 
				ddoRelMin, ddoRelMed, ddoRelAvg, ddoRelMax};
<<<<<<< HEAD
=======
		// TODO Auto-generated method stub
		return null;
>>>>>>> computation of the error distributions
=======
>>>>>>> create Single/Multi values
	}

}

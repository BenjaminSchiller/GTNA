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
 * Distribution.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.util;

/**
 * @author benni
 * 
 */
public class Distribution {
	private double[] distribution;

	private double[] cdf;

	private double min;

	private double median;

	private double average;

	private double max;

	public Distribution(long[] values, long sum) {
		this(Distribution.computeDistributionLong(values, sum));
	}

	public Distribution(int[] values, int sum) {
		this(Distribution.computeDistributionInt(values, sum));
	}

	public Distribution(double[] distribution) {
		this.distribution = distribution;
		this.cdf = this.computeCdf();
		this.min = this.computeMin();
		this.median = this.computeMedian();
		this.average = this.computeAverage();
		this.max = this.computeMax();
	}

	private static double[] computeDistributionLong(long[] values, long sum) {
		if(sum == 0){
			return new double[values.length];
		}
		double[] distribution = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			distribution[i] = (double) values[i] / (double) sum;
		}
		return distribution;
	}

	private static double[] computeDistributionInt(int[] values, int sum) {
		if(sum == 0){
			return new double[values.length];
		}
		double[] distribution = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			distribution[i] = (double) values[i] / (double) sum;
		}
		return distribution;
	}

	private double[] computeCdf() {
		double[] cdf = new double[this.distribution.length];
		cdf[0] = this.distribution[0];
		for (int i = 1; i < cdf.length; i++) {
			cdf[i] = cdf[i - 1] + this.distribution[i];
		}
		return cdf;
	}

	private double computeMin() {
		int index = 0;
		while (index < this.distribution.length && this.distribution[index] == 0) {
			index++;
		}
		return index;
	}

	private double computeMax() {
		return this.distribution.length - 1;
	}

	private double computeMedian() {
		int index = 0;
		double threshold = this.cdf[this.cdf.length - 1] / 2.0;
		while (index < this.cdf.length && this.cdf[index] <= threshold) {
			index++;
		}
		return index;
	}

	private double computeAverage() {
		double avg = 0;
		for (int i = 0; i < this.distribution.length; i++) {
			avg += (double) i * this.distribution[i];
		}
		return avg;
	}

	/**
	 * @return the distribution
	 */
	public double[] getDistribution() {
		return this.distribution;
	}

	/**
	 * @return the cdf
	 */
	public double[] getCdf() {
		return this.cdf;
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return this.min;
	}

	/**
	 * @return the median
	 */
	public double getMedian() {
		return this.median;
	}

	/**
	 * @return the average
	 */
	public double getAverage() {
		return this.average;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return this.max;
	}
}

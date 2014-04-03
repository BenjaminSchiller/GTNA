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
import gtna.transformation.sampling.Sample;
import gtna.util.Distribution;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Tim
 * 
 */
public class TopKCorrelation extends Metric {

	private Metric metric;
	private Series[] series2;
	private Series[] series1;
	private Series seriesMy;

	private double[] fraction;

	private double[] sorted1;

	private double[] sorted2;

	private double correlationCoefficient;

	public static enum Mode {
		RUNWITHRUN, BASEWITHRUN
	};

	/**
	 * Type <b>SAMPLE</b> compares:<br>
	 * - metric of base series<br>
	 * - with metric of changed series.<br>
	 * > nodes are mapped using the SAMPLE property saved at the base series<br>
	 * <br>
	 * Type <b>NETWORK</b> compares:<br>
	 * - metric of base series <br>
	 * - metric of changed series<br>
	 * > nodes are compared by ids without any mapping<br>
	 * 
	 * @author Tim
	 * 
	 */
	public static enum Type {
		SAMPLE, NETWORK
	};

	private Type type;
	private Mode mode;
	private Sample sampleProperty;

	/**
	 * @param key
	 */
	public TopKCorrelation(Metric comparedMetric, Series[] base,
			Series[] changed, Type t, Mode m) {
		super("TOPK", new Parameter[]{
				new StringParameter("METRIC", comparedMetric.getDescriptionShort())
		});
		
		this.type = t;
		this.mode = m;
		
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
		if (this.mode == Mode.RUNWITHRUN) {
			b.readData(series1[0].getMetricFolder(r, b));
		} else if (this.mode == Mode.BASEWITHRUN) {
			b.readData(series1[0].getFolder(b));
		} else {
			throw new IllegalArgumentException("Comparison Type not accepted: "
					+ this.type);
		}
		c.readData(series2[0].getMetricFolder(r, c));

		/*
		 * Compare Metric values
		 */

		// TODO: provide metric.getTopKNodeValueList()?
		calculateTopK(b.getNodeValueLists(), c.getNodeValueLists());

	}

	/**
	 * @param base
	 * @param changed
	 */
	private void calculateTopK(NodeValueList[] base, NodeValueList[] changed) {

		int s1count = (series1[0].getNetwork().generate()).getNodeCount();
		int s2count = (series2[0].getNetwork().generate()).getNodeCount();

		int nc = Math.max(s1count, s2count);

		this.fraction = new double[nc + 1];
		this.fraction[0] = 1;

		HashSet<Integer> s1 = new HashSet<Integer>(s1count);
		HashSet<Integer> s2 = new HashSet<Integer>(s2count);

		SortableElement[] e1 = SortableElement.convert(base[0].getValues());
		SortableElement[] e2 = SortableElement.convert(changed[0].getValues());

		double sum_xy = sumOfProducts(e1, e2);
		double sum_x = sum(e1);
		double sum_y = sum(e2);
		double sum_xx = sumOfProducts(e1, e1);
		double sum_yy = sumOfProducts(e2, e2);
		double n_ = nc;

		double c_numerator = n_ * sum_xy - sum_x * sum_y;
		double c_denominator = (n_ * sum_xx - sum_x * sum_x)
				* (n_ * sum_yy - sum_y * sum_y);

		this.correlationCoefficient = c_numerator / Math.sqrt(c_denominator);

		Arrays.sort(e1);
		Arrays.sort(e2);

		double denominator = 0;
		double count = 0;
		for (int i = 0; i < nc; i++) {
			denominator++;
			s1.add(e1[(i >= e1.length) ? e1.length - 1 : i].getIndex());
			s2.add(e2[(i >= e2.length) ? e2.length - 1 : i].getIndex());
			if (e1[(i >= e1.length) ? e1.length - 1 : i].getIndex() == e2[(i >= e2.length) ? e2.length - 1
					: i].getIndex()) {
				count++;
			} else {
				if (s1.contains(this
						.mapForward(e2[(i >= e2.length) ? e2.length - 1 : i]
								.getIndex()))) {
					count++;
				}
				if (s2.contains(this
						.mapBackward(e1[(i >= e1.length) ? e1.length - 1 : i]
								.getIndex()))) {
					count++;
				}
			}
			this.fraction[i + 1] = count / denominator;
		}

		this.sorted1 = new double[nc];
		this.sorted2 = new double[nc];
		for (int i = 0; i < nc; i++) {
			int e1i = (i >= e1.length) ? e1.length - 1 : i;
			int e2i = (i >= e2.length) ? e2.length - 1 : i;
			
			int e1ii;
			int index = e2[e2i].getIndex();
			if (index < e1.length-1)
				e1ii = index;
			else
				e1ii = e1.length-1;
			
			int e2ii;
			int index2 = e1[e1i].getIndex();
			if (index2 < e2.length-1)
				e2ii = index2;
			else
				e2ii = e2.length-1;
			
			
			this.sorted1[i] = e2[e2ii].getValue();
			this.sorted2[i] = e1[e1ii].getValue();
		}
	}

	/**
	 * @param index
	 * @return
	 */
	private int mapForward(int index) {
		if (type == Type.NETWORK) {
			return index;
		} else {
			if (sampleProperty == null)
				initProperty();

			return sampleProperty.getOldNodeId(index);
		}
	}

	/**
	 * @param index
	 * @return
	 */
	private int mapBackward(int index) {
		if (type == Type.NETWORK) {
			return index;
		} else {
			if (sampleProperty == null)
				initProperty();

			return sampleProperty.getNewNodeId(index);
		}
	}

	/**
	 * 
	 */
	private void initProperty() {
		sampleProperty = (Sample) series1[0].getNetwork().generate()
				.getProperty("SAMPLE_0"); // TODO use variable to set property
											// index
	}

	private static double sum(SortableElement[] values) {
		double sum = 0;
		for (SortableElement value : values) {
			sum += value.getValue();
		}
		return sum;
	}

	private static double sumOfProducts(SortableElement[] v1,
			SortableElement[] v2) {
		double product = 0;
		for (int i = 0; i < Math.max(v1.length, v2.length); i++) {
			product += v1[i%v1.length].getValue() * v2[i%v2.length].getValue(); //TODO!!!
		}
		return product;
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
	private boolean applicable() { // TODO: figure out if metric is comparable
									// with TopK
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

		success &= DataWriter.writeWithIndex(this.fraction, "TOPK_FRACTION",
				folder);

		success &= DataWriter.writeWithIndex(this.sorted1, "TOPK_SORTED1",
				folder);
		success &= DataWriter.writeWithIndex(this.sorted2, "TOPK_SORTED2",
				folder);

		return success;
	}

	
	/**
	 * The ErrorComparison Metric does not read persisted values.
	 */
	@Override
	public boolean readData(String folder) {
		return true;
	}

	@Override
	public Single[] getSingles() {
		Single cc = new Single("TOPK_CORRELATION_COEFFICIENT",
				this.correlationCoefficient);
		return new Single[] { cc };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getDistributions()
	 */
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getNodeValueLists()
	 */
	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {
				new NodeValueList("TOPK_SORTED1", sorted1),
				new NodeValueList("TOPK_SORTED2", sorted2) };
	}

	public static class SortableElement implements Comparable<SortableElement> {

		private int index;

		private double value;

		public SortableElement(int index, double value) {
			this.index = index;
			this.value = value;
		}

		public int getIndex() {
			return this.index;
		}

		public double getValue() {
			return this.value;
		}

		@Override
		public int compareTo(SortableElement o) {
			if (o.getValue() - this.getValue() < 0) {
				return -1;
			} else if (o.getValue() - this.getValue() > 0) {
				return 1;
			} else {
				return 0;
			}
		}

		public static SortableElement[] convert(double[] values) {
			SortableElement[] array = new SortableElement[values.length];
			for (int i = 0; i < values.length; i++) {
				array[i] = new SortableElement(i, values[i]);
			}
			return array;
		}

		public static void print(SortableElement[] elements) {
			for (SortableElement element : elements) {
				System.out.println(element.getValue() + " ("
						+ element.getIndex() + ")");
			}
		}

		public String toString() {
			return this.value + "(" + this.index + ")";
		}
	}

}

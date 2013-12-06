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
 * TopK.java
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
package gtna.metrics.centrality;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricDescriptionWrapper;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author benni
 * 
 */
public class TopK extends Metric {

	public static enum Type {
		BETWEENNESS_CENTRALITY, ROUTING_BETWEENNESS, DEGREE_CENTRALITY
	};

	private Type t1;

	private Type t2;

	private double[] fraction;

	private double[] sorted1;

	private double[] sorted2;

	private double pearsonsCorrelationCoefficient;

	private String routingKey;

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public TopK(Type t1, Type t2) {
		this(t1, t2, "ROUTING");
	}

	public TopK(Type t1, Type t2, String routingKey) {
		super("TOPK", new Parameter[] {
				new StringParameter("T1", t1.toString()),
				new StringParameter("T2", t2.toString()),
				new StringParameter("R", routingKey) });
		this.t1 = t1;
		this.t2 = t2;
		this.routingKey = routingKey;
		this.fraction = new double[0];
		this.sorted1 = new double[0];
		this.sorted2 = new double[0];
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.fraction = new double[g.getNodeCount() + 1];
		this.fraction[0] = 1;

		HashSet<Integer> s1 = new HashSet<Integer>(g.getNodeCount());
		HashSet<Integer> s2 = new HashSet<Integer>(g.getNodeCount());

		SortableElement[] e1 = this.getValues(g, m, this.t1);
		SortableElement[] e2 = this.getValues(g, m, this.t2);

		double sum_xy = sumOfProducts(e1, e2);
		double sum_x = sum(e1);
		double sum_y = sum(e2);
		double sum_xx = sumOfProducts(e1, e1);
		double sum_yy = sumOfProducts(e2, e2);
		double n_ = g.getNodeCount();

		double c_numerator = n_ * sum_xy - sum_x * sum_y;
		double c_denominator = (n_ * sum_xx - sum_x * sum_x)
				* (n_ * sum_yy - sum_y * sum_y);

		this.pearsonsCorrelationCoefficient = c_numerator
				/ Math.sqrt(c_denominator);

		Arrays.sort(e1);
		Arrays.sort(e2);

		double denominator = 0;
		double count = 0;
		for (int i = 0; i < g.getNodeCount(); i++) {
			denominator++;
			s1.add(e1[i].getIndex());
			s2.add(e2[i].getIndex());
			if (e1[i].getIndex() == e2[i].getIndex()) {
				count++;
			} else {
				if (s1.contains(e2[i].getIndex())) {
					count++;
				}
				if (s2.contains(e1[i].getIndex())) {
					count++;
				}
			}
			this.fraction[i + 1] = count / denominator;
		}

		this.sorted1 = new double[g.getNodeCount()];
		this.sorted2 = new double[g.getNodeCount()];
		for (int i = 0; i < g.getNodeCount(); i++) {
			this.sorted1[i] = e2[e1[i].getIndex()].getValue();
			this.sorted2[i] = e1[e2[i].getIndex()].getValue();
		}
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
		for (int i = 0; i < v1.length; i++) {
			product += v1[i].getValue() * v2[i].getValue();
		}
		return product;
	}

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

	@Override
	public Single[] getSingles() {
		Single pcc = new Single("TOPK_PEARSONS_CORRELATION_COEFFICIENT",
				this.pearsonsCorrelationCoefficient);
		return new Single[] { pcc };
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return this.getValues(g, m, this.t1) != null
				&& this.getValues(g, m, this.t2) != null;
	}

	private SortableElement[] getValues(Graph g, HashMap<String, Metric> m,
			Type t) {
		switch (t) {
		case BETWEENNESS_CENTRALITY:
			BetweennessCentrality bc = (BetweennessCentrality) m
					.get("BETWEENNESS_CENTRALITY");
			if (bc == null) {
				return null;
			}
			return SortableElement.convert(bc.getBetweennessCentrality());
		case DEGREE_CENTRALITY:
			double[] degree = new double[g.getNodeCount()];
			int index = 0;
			for (Node n : g.getNodes()) {
				degree[index++] = n.getDegree();
			}
			return SortableElement.convert(degree);
		case ROUTING_BETWEENNESS:
			if (m.containsKey(this.routingKey)) {
				Metric m_ = m.get(this.routingKey);
				if (m_ instanceof MetricDescriptionWrapper) {
					Routing r = (Routing) ((MetricDescriptionWrapper) m_)
							.getMetric();
					return SortableElement.convert(r.getBC());
				} else {
					Routing r = (Routing) m_;
					return SortableElement.convert(r.getBC());
				}
			}
			return null;
		default:
			return null;
		}
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

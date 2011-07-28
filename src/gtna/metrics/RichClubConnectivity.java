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
 * RichClubConnectivity.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.metrics;

//TODO reimplement RichClubConnectivity
public class RichClubConnectivity {
	// public class RichClubConnectivity extends MetricImpl implements Metric {
	// private double[] rcc;
	//
	// private double[] rccShort;
	//
	// private Timer timer;
	//
	// public RichClubConnectivity() {
	// super("RCC");
	// }
	//
	// public void computeData(Graph g, Network n, Hashtable<String, Metric> m)
	// {
	// this.timer = new Timer();
	// int[] order = NodeSorting.byDegreeDesc(g.nodes, new Random(System
	// .currentTimeMillis()));
	// int edges = 0;
	// this.rcc = new double[order.length + 1];
	// for (int p = 2; p <= order.length; p++) {
	// int newNode = order[p - 1];
	// for (int i = 0; i < p - 1; i++) {
	// if (g.nodes[newNode].hasOut(g.nodes[i])) {
	// edges++;
	// }
	// if (g.nodes[i].hasOut(g.nodes[newNode])) {
	// edges++;
	// }
	// }
	// this.rcc[p] = (double) edges / (double) (p * (p - 1));
	// }
	// this.rccShort = new double[Math.min(this.rcc.length, Config
	// .getInt("RCC_SHORT_MAX_VALUES"))];
	// for(int i=0; i<this.rccShort.length; i++){
	// this.rccShort[i] = this.rcc[i];
	// }
	// timer.end();
	// }
	//
	// public Value[] getValues(Value[] values) {
	// Value RCC_RT = new Value("RCC_RT", this.timer.rt());
	// return new Value[] { RCC_RT };
	// }
	//
	// public boolean writeData(String folder) {
	// DataWriter.writeWithIndex(this.rcc, "RCC", folder);
	// DataWriter.writeWithIndex(this.rccShort, "RCC_SHORT", folder);
	// return true;
	// }
}

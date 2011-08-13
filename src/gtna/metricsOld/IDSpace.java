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
 * IDSpace.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    "Stefanie Roos";
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-01 : v1 (BS)
 * 2011-06-09 : added skewed version (BS, SR)
 *
 */
package gtna.metricsOld;

//TODO reimplement IDSpace
public class IDSpace {
	// public class IDSpace extends MetricImpl implements Metric {
	// // private double[][] circle;
	//
	// // private double[][] circleSkewed;
	//
	// private double[][] circleBinned;
	//
	// // private double[][] line;
	//
	// public IDSpace() {
	// super("ID_SPACE");
	// }
	//
	// private void initEmpty() {
	// // this.circle = new double[][] { new double[] { 0.0, 0.00 } };
	// // this.circleSkewed = new double[][] { new double[] { 0.0, 0.00 } };
	// this.circleBinned = new double[][] { new double[] { 0.0, 0.00 } };
	// // this.line = new double[][] { new double[] { 0.0, 0.00 } };
	// }
	//
	// public void computeData(Graph g, Network n, Hashtable<String, Metric> m)
	// {
	// if (!(g.nodes[0] instanceof RingNode)) {
	// this.initEmpty();
	// }
	// // this.circle = this.computeCircle(g);
	// // this.circleSkewed = this.computeCircleSkewed(g);
	// this.circleBinned = this.computeCircleBinned(g);
	// // this.line = this.computeLine(g);
	// }
	//
	// private double[][] computeCircle(Graph g) {
	// double[][] circle = new double[g.nodes.length][2];
	// double[] ids = new double[g.nodes.length];
	// for (int i = 0; i < g.nodes.length; i++) {
	// ids[i] = ((RingNode) g.nodes[i]).getID().pos;
	// }
	// Arrays.sort(ids);
	// for (int i = 0; i < ids.length; i++) {
	// double x = Math.sin(ids[i] * 2 * Math.PI);
	// double y = Math.cos(ids[i] * 2 * Math.PI);
	// circle[i] = new double[] { x, y };
	// }
	// return circle;
	// }
	//
	// private double[][] computeCircleSkewed(Graph g) {
	// double[][] circle = new double[g.nodes.length][2];
	// double[] ids = new double[g.nodes.length];
	// for (int i = 0; i < g.nodes.length; i++) {
	// ids[i] = ((RingNode) g.nodes[i]).getID().pos;
	// }
	// Arrays.sort(ids);
	// double epsilon = Config.getDouble("ID_SPACE_CIRCLE_SKEWED_EPSILON");
	// Random rand = new Random(System.currentTimeMillis());
	// for (int i = 0; i < ids.length; i++) {
	// double off = rand.nextDouble() * epsilon;
	// double x = Math.sin(ids[i] * 2 * Math.PI) * (1 - off);
	// double y = Math.cos(ids[i] * 2 * Math.PI) * (1 - off);
	// circle[i] = new double[] { x, y };
	// }
	// return circle;
	// }
	//
	// private double[][] computeCircleBinned(Graph g) {
	// double[][] circle = new double[g.nodes.length][2];
	// double[] ids = new double[g.nodes.length];
	// for (int i = 0; i < g.nodes.length; i++) {
	// ids[i] = ((RingNode) g.nodes[i]).getID().pos;
	// }
	// double epsilon = Config.getDouble("ID_SPACE_CIRCLE_BINNED_EPSILON");
	// double stepSize = Config.getDouble("ID_SPACE_CIRCLE_BINNED_STEP_SIZE");
	// double[][] binned = Statistics.binning(ids, 0, 1, stepSize);
	// double max = 0;
	// for (int i = 0; i < binned.length; i++) {
	// if (binned[i].length > max) {
	// max = binned[i].length;
	// }
	// }
	// int index = 0;
	// for (int i = 0; i < binned.length; i++) {
	// double step = 1.0 / max;
	// for (int j = 0; j < binned[i].length; j++) {
	// double off = step * epsilon * j;
	// double x = Math.sin(binned[i][j] * 2 * Math.PI) * (0.5 + off);
	// double y = Math.cos(binned[i][j] * 2 * Math.PI) * (0.5 + off);
	// circle[index++] = new double[] { x, y };
	// }
	// }
	// return circle;
	// }
	//
	// private double[][] computeLine(Graph g) {
	// double[][] circle = new double[g.nodes.length][2];
	// double[] ids = new double[g.nodes.length];
	// for (int i = 0; i < g.nodes.length; i++) {
	// ids[i] = ((RingNode) g.nodes[i]).getID().pos;
	// }
	// Arrays.sort(ids);
	// for (int i = 0; i < ids.length; i++) {
	// double x = ids[i];
	// double y = ids[i];
	// circle[i] = new double[] { x, y };
	// }
	// return circle;
	// }
	//
	// public Value[] getValues(Value[] values) {
	// return new Value[] {};
	// }
	//
	// public boolean writeData(String folder) {
	// // DataWriter.writeWithoutIndex(this.circle, "ID_SPACE_CIRCLE", folder);
	// // DataWriter.writeWithoutIndex(this.circleSkewed,
	// // "ID_SPACE_CIRCLE_SKEWED", folder);
	// DataWriter.writeWithoutIndex(this.circleBinned,
	// "ID_SPACE_CIRCLE_BINNED", folder);
	// // DataWriter.writeWithoutIndex(this.line, "ID_SPACE_LINE", folder);
	// return true;
	// }

}
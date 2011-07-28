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
 * ClearGraph.java
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
package gtna.metrics.roles;


/**
 * @deprecated
 */
// TODO reimplement ClearGraph
public class ClearGraph {
	// private int deletedLoops;
	// private int deletedParallelEdges;
	// private int deletedDirectedParallelEdges;
	//
	// public ClearGraph() {
	//
	// }
	//
	// // delete all loops, parallel edges and antipodal edges
	// public Graph clearGraph(Graph g) {
	// g = graphDeleteLoops(g);
	// g = graphDeleteParallelEdges(g);
	// g = graphDeleteDirectedParallelEdges(g);
	// return g;
	// }
	//
	// // public Graph downsizeGraph(Graph g, int maxsize){
	// // for(int i=0;i<g.nodes.length;i++){
	// // for(int j=0;j<g.nodes[i].in.length;j++){
	// // if(g.nodes[i].in[j].index>maxsize){
	// // g.nodes[i].removeIn(g.nodes[i].in[j]);
	// // }
	// // }
	// // for(int j=0;j<g.nodes[i].out.length;j++){
	// // if(g.nodes[i].out[j].index>maxsize){
	// // g.nodes[i].removeOut(g.nodes[i].out[j]);
	// // }
	// // }
	// // }
	// // Node[] nodes = new Node[maxsize];
	// // for(int i=0; i<maxsize; i++){
	// // nodes[i] = g.nodes[i];
	// // }
	// // Graph graph = new Graph("graph", nodes);
	// // return graph;
	// // }
	//
	// // delete all parallel edges
	// public Graph graphDeleteDirectedParallelEdges(Graph g) {
	// int counter = 0;
	// for (int i = 0; i < g.nodes.length; i++) {
	// Node node_i = g.nodes[i];
	// int index = node_i.index();
	// Node[] in = node_i.in();
	// for (int j = 0; j < in.length; j++) {
	// for (int k = j+1; k < in.length; k++) {
	// if (in[j] == in[k]) {
	// g.nodes[in[k].index()].removeOut(node_i);
	// g.nodes[index]
	// .removeIn((Node) in[k]);
	// counter++;
	// }
	// }
	// }
	// }
	// System.out
	// .println("Directed Parralel Edges Deleted: " + counter + "\n");
	// this.setDeletedDirectedParallelEdges(counter);
	// return g;
	// }
	//
	// // delete all loops
	// public Graph graphDeleteLoops(Graph g) {
	// int counter = 0;
	// for (int i = 0; i < g.nodes.length; i++) {
	// Node[] in = g.nodes[i].in();
	// for (int j = 0; j < in.length; j++) {
	// if (in[j].index() == g.nodes[i].index()) {
	// g.nodes[i].removeIn(g.nodes[i]);
	// g.nodes[i].removeOut(g.nodes[i]);
	// counter++;
	// }
	// }
	// }
	// System.out.println("\n\nLoops Deleted: " + counter);
	// this.setDeletedLoops(counter);
	// return g;
	// }
	//
	// // delete all antipodal edges
	// public Graph graphDeleteParallelEdges(Graph g) {
	// int counter = 0;
	// for (int i = 0; i < g.nodes.length; i++) {
	// Node[] in = g.nodes[i].in();
	// Node[] out = g.nodes[i].out();
	// for (int j = 0; j < in.length; j++) {
	// for (int k = 0; k < out.length; k++) {
	// if (in[j].index() == out[k].index()) {
	// g.nodes[out[k].index()].removeIn(g.nodes[i]);
	// g.nodes[g.nodes[i].index()]
	// .removeOut((Node) out[k]);
	// counter++;
	// }
	// }
	// }
	// }
	// System.out.println("Parralel Edges Deleted: " + counter);
	// this.setDeletedParallelEdges(counter);
	// return g;
	// }
	//
	// // getter and setter methods
	//
	// private void setDeletedLoops(int deletedLoops) {
	// this.deletedLoops = deletedLoops;
	// }
	//
	// public int getDeletedLoops() {
	// return deletedLoops;
	// }
	//
	// private void setDeletedParallelEdges(int deletedParallelEdges) {
	// this.deletedParallelEdges = deletedParallelEdges;
	// }
	//
	// public int getDeletedParallelEdges() {
	// return deletedParallelEdges;
	// }
	//
	// private void setDeletedDirectedParallelEdges(
	// int deletedDirectedParallelEdges) {
	// this.deletedDirectedParallelEdges = deletedDirectedParallelEdges;
	// }
	//
	// public int getDeletedDirectedParallelEdges() {
	// return deletedDirectedParallelEdges;
	// }
}

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
 * LargestConnectedComponent.java
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
package gtna.transformationOld.connectivity;


/**
 * Extracts the largest connected component of the given graph, i.e., the largest
 * subset of nodes that form a connected cluster.
 * 
 * @author benni
 * 
 */
// TODO reimplement LargestConnectedComponent
public class LargestConnectedComponent{
//public class LargestConnectedComponent extends TransformationImpl implements
//		Transformation {
//	public LargestConnectedComponent() {
//		super("LARGEST_CONNECTED_COMPONENT", new String[] {}, new String[] {});
//	}
//
//	public boolean applicable(Graph g) {
//		return true;
//	}
//
//	public Graph transform(Graph g) {
//		Node[] lc = this.largestCluster(g);
//		for (int i = 0; i < lc.length; i++) {
//			lc[i].setIndex(i);
//		}
//		g.nodes = lc;
//		g.computeDegrees();
//		g.computeEdges();
//		return g;
//	}
//
//	private ArrayList<ArrayList<Node>> clusters(Graph g) {
//		ArrayList<ArrayList<Node>> clusters = new ArrayList<ArrayList<Node>>();
//		boolean[] seen = new boolean[g.nodes.length];
//		for (int i = 0; i < seen.length; i++) {
//			if (!seen[i]) {
//				ArrayList<Node> current = new ArrayList<Node>();
//				clusters.add(current);
//				Stack<Node> stack = new Stack<Node>();
//				stack.push(g.nodes[i]);
//				seen[i] = true;
//				current.add(g.nodes[i]);
//				while (!stack.empty()) {
//					Node n = stack.pop();
//					Node[] out = n.out();
//					Node[] in = n.in();
//					for (int j = 0; j < out.length; j++) {
//						if (!seen[out[j].index()]) {
//							seen[out[j].index()] = true;
//							stack.push(out[j]);
//							current.add(out[j]);
//						}
//					}
//					for (int j = 0; j < in.length; j++) {
//						if (!seen[in[j].index()]) {
//							seen[in[j].index()] = true;
//							stack.push(in[j]);
//							current.add(in[j]);
//						}
//					}
//				}
//			}
//		}
//		return clusters;
//	}
//
//	private Node[] largestCluster(Graph g) {
//		ArrayList<ArrayList<Node>> clusters = this.clusters(g);
//		ArrayList<Node> largest = clusters.get(0);
//		for (int i = 1; i < clusters.size(); i++) {
//			if (clusters.get(i).size() > largest.size()) {
//				largest = clusters.get(i);
//			}
//		}
//		return Util.toNodeImplArray(largest);
//	}
}

///*
// * ===========================================================
// * GTNA : Graph-Theoretic Network Analyzer
// * ===========================================================
// * 
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors
// * 
// * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
// * 
// * GTNA is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * 
// * GTNA is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// * 
// * ---------------------------------------
// * RemoveLargestNode.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// * 
// * Original Author: Benjamin Schiller;
// * Contributors:    -;
// * 
// * Changes since 2011-05-17
// * ---------------------------------------
//*/
//package gtna.trash.transformation.degree;
//
//
///**
// * Transforms the given graph by removing the node with the highest degree.
// * 
// * @author benni
// * 
// */
//// TODO reimplement RemoveLargestNode
//public class RemoveLargestNode{
////public class RemoveLargestNode extends TransformationImpl implements
////		Transformation {
////	public RemoveLargestNode() {
////		super("REMOVE_LARGEST_NODE", new String[] {}, new String[] {});
////	}
////
////	public boolean applicable(Graph g) {
////		return true;
////	}
////
////	public Graph transform(Graph g) {
////		Node largest = g.nodes[0];
////		int degree = largest.out().length + largest.in().length;
////		for (int i = 1; i < g.nodes.length; i++) {
////			if (g.nodes[i].out().length + g.nodes[i].in().length > degree) {
////				degree = g.nodes[i].out().length + g.nodes[i].in().length;
////				largest = g.nodes[i];
////			}
////		}
////		for (int i = 0; i < largest.out().length; i++) {
////			largest.out()[i].removeIn(largest);
////		}
////		for (int i = 0; i < largest.in().length; i++) {
////			largest.in()[i].removeOut(largest);
////		}
////		Node[] nodes = new Node[g.nodes.length - 1];
////		for (int i = 0; i < largest.index(); i++) {
////			nodes[i] = g.nodes[i];
////			nodes[i].setIndex(i);
////		}
////		for (int i = largest.index() + 1; i < g.nodes.length; i++) {
////			nodes[i - 1] = g.nodes[i];
////			nodes[i - 1].setIndex(i - 1);
////		}
////		g.nodes = nodes;
////		g.computeDegrees();
////		g.computeEdges();
////		return g;
////	}
//
//}

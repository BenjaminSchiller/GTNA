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
// * RandomID.java
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
//package gtna.trash.transformation.identifier;
//
//
///**
// * Transforms the given graph by assigning each node a random ID of the given
// * type. THe number of dimensions is another parameter used by some of these
// * types. Note that this transformation creates new node objects. Therefore, all
// * information except for the node adjacency is lost.
// * 
// * @author benni
// * 
// */
//// TODO reimplement RandomID
//public class RandomID{
////public class RandomID extends TransformationImpl implements Transformation {
////	public static final String RING_NODE = "R";
////
////	public static final String RING_NODE_MULTI_R = "RmR";
////
////	public static String GRID_NODE_EUCLIDEAN = "GE";
////
////	public static String GRID_NODE_MANHATTAN = "GM";
////
////	private int parameter;
////
////	private String type;
////
////	public RandomID(String type, int parameter) {
////		super("RANDOM_ID", new String[] { "TYPE", "PARAMETER" }, new String[] {
////				type, "" + parameter });
////		this.parameter = parameter;
////		this.type = type;
////	}
////
////	public boolean applicable(Graph g) {
////		return true;
////	}
////
////	public Graph transform(Graph g) {
////		Node[] nodes = new Node[g.nodes.length];
////		Set<String> ids = new HashSet<String>();
////		Random rand = new Random(System.currentTimeMillis());
////		if (this.type.equals(RING_NODE)) {
////			for (int i = 0; i < nodes.length; i++) {
////				double id = this.uniqueNumber(ids, rand);
////				nodes[i] = new RingNode(i, id);
////			}
////		} else if (this.type.equals(RING_NODE_MULTI_R)) {
////			double[][] IDs = new double[nodes.length][this.parameter];
////			for(int i=0; i<this.parameter; i++){
////				ids = new HashSet<String>();
////				for(int j=0; j<nodes.length; j++){
////					IDs[j][i] = this.uniqueNumber(ids, rand);
////				}
////			}
////			for(int i=0; i<nodes.length; i++){
////				nodes[i] = new RingNodeMultiR(i, IDs[i]);
////			}
////		} else if (this.type.equals(GRID_NODE_EUCLIDEAN)) {
////			for (int i = 0; i < nodes.length; i++) {
////				double[] id = this.uniqueID(ids, rand);
////				nodes[i] = new GridNodeEuclidean(i, id);
////			}
////		} else if (this.type.equals(GRID_NODE_MANHATTAN)) {
////			for (int i = 0; i < nodes.length; i++) {
////				double[] id = this.uniqueID(ids, rand);
////				nodes[i] = new GridNodeManhattan(i, id);
////			}
////		}
////		Edge[] original = g.edges();
////		Edges edges = new Edges(nodes, original.length);
////		for (int i = 0; i < original.length; i++) {
////			Node src = nodes[original[i].src.index()];
////			Node dst = nodes[original[i].dst.index()];
////			edges.add(src, dst);
////		}
////		edges.fill();
////		return new Graph(g.name, nodes, g.timer);
////	}
////
////	private double uniqueNumber(Set<String> ids, Random rand) {
////		double n = rand.nextDouble();
////		while (ids.contains(n + "")) {
////			n = rand.nextDouble();
////		}
////		ids.add(n + "");
////		return n;	
////	}
////
////	private double[] uniqueID(Set<String> ids, Random rand) {
////		double[] id = this.randomID(rand);
////		while (ids.contains(this.idToString(id))) {
////			id = this.randomID(rand);
////		}
////		ids.add(this.idToString(id));
////		return id;
////	}
////
////	private double[] randomID(Random rand) {
////		double[] id = new double[this.parameter];
////		for (int i = 0; i < id.length; i++) {
////			id[i] = rand.nextDouble();
////		}
////		return id;
////	}
////
////	private String idToString(double[] id) {
////		StringBuffer buff = new StringBuffer();
////		for (int i = 0; i < id.length; i++) {
////			buff.append(";" + id[i]);
////		}
////		return buff.toString();
////	}
//}

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
 * BuildSubGraph.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.subGraphs;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author stef
 *
 *build a subgraph of an existing graph starting from a clique of predefined size
 *in case such a clique does not exist, an empty graph is returned
 *one can restrict the maximum degree on this subgraph
 */
public class BuildSubGraphMax extends Transformation {
	
	public static String SELECTION_RANDOM = "RANDOM";
	public static String SELECTION_OUTDEGREE = "OUTDEGREE";
	int include,minDegree,maxDegree,startNodes;
	String selection;

	/**
	 * 
	 * @param include: nodes that are MAXIMALLY included in subgraph
	 * @param minDegree: minimum number of links a node has to have to nodes that are already included in the subgraph to be added
	 * @param maxDegree: maximum degree a node in the subgraph is allowed to have
	 * @param startNodes: size of the initial clique
	 * @param selection: selection of the initial clique:
	 *                   a) RANDOM: choose a clique randomly from all cliques in the graph
	 *                   b) OUTDEGREE: choose the clique for which the number of outgoing links is highest
	 */
	public BuildSubGraphMax(int include, int minDegree,int maxDegree, int startNodes, String selection) {
		super("BUILD_SUB_GRAPH_MAX", new Parameter[]{new IntParameter("INCLUDE",include), new IntParameter("MINDEGREE",minDegree), 
				new IntParameter("MAXDEGREE",maxDegree), new IntParameter("STARTNODES",startNodes),  new StringParameter("SELECTION",selection)});
		this.selection = selection;
		this.minDegree = Math.max(minDegree,1);
		this.maxDegree = maxDegree;
		this.include = include;
		this.startNodes = startNodes;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		Node[] nodesOld = g.getNodes();
		boolean[] added = new boolean[nodesOld.length]; 
		Vector<Vector<Integer>> in = new Vector<Vector<Integer>>();
		HashMap<Integer, Integer> newIndex = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> inDegree = new HashMap<Integer,Integer>();
		HashMap<Integer, Vector<Integer>> neighs = new HashMap<Integer,Vector<Integer>>();
		Random rand = new Random();
		int[] out;
		int[] start = this.getStartIndex(nodesOld, rand);
		
		Integer inDeg;
		Vector<Integer> list;
		for (int s = 0; s < start.length; s++){
			added[start[s]] = true;
			list = new Vector<Integer>();
//			for (int t = 0; t < start.length; t++){
//				if (t != s){
//					list.add(start[t]);
//				}
//			}
//			neighs.put(start[s], list);
		}
		Vector<Integer> f;
		for (int s = 0; s < start.length; s++){
			out = nodesOld[start[s]].getOutgoingEdges();
			for (int i = 0; i < out.length; i++){
				f = neighs.get(out[i]);
				if (f == null){
					f = new Vector<Integer>();
					neighs.put(out[i],f);
				}
				if (f.size() < this.maxDegree){
					f.add(start[s]);
				}
				if (!added[out[i]]){
					inDeg = inDegree.remove(out[i]);
					if (inDeg == null){
						inDeg = 0;
					}
					inDeg++;
					inDegree.put(out[i],inDeg);
					
					if (inDeg > 1){
						list = in.get(inDeg-2);
						list.removeElement(out[i]);
					}
					if (inDeg > in.size()){
						list = new Vector<Integer>();
						in.add(list);
					} else {
						list = in.get(inDeg-1);
					}
					list.add(out[i]);
				}
			}
		}
		
		int count = start.length;
		int max;
		Node last;
		while (count < this.include && count < nodesOld.length){
			max = in.size()-1;
			while (max > -1 && in.get(max).size() == 0){
				max--;
			}
			if (max < this.minDegree-1){
				break;
			}
			int s = in.get(max).remove(rand.nextInt(in.get(max).size()));
			last = nodesOld[s];
			added[s] = true;
			list = neighs.get(s);
			for (int i = 0; i < list.size(); i++){
				f = neighs.get(list.get(i));
				f.add(s);
				if (f.size() == this.maxDegree){
					out = nodesOld[list.get(i)].getOutgoingEdges();
					for (int k = 0; k < out.length; k++){
						if (!added[out[k]]){
							Vector<Integer> cur = neighs.get(out[k]);
							cur.remove(list.get(i));
							in.get(cur.size()).removeElement(out[k]);
							if (cur.size() > 0)
							in.get(cur.size()-1).add(out[k]);
						}
					}
				}
			}
			out = last.getOutgoingEdges();
			count++;
			
			for (int i = 0; i < out.length; i++){
				f = neighs.get(out[i]);
				if (f == null){
					f = new Vector<Integer>();
					neighs.put(out[i],f);
				}
				if (f.size() < this.maxDegree){
					f.add(s);
				}
				if (!added[out[i]]){
					inDeg = inDegree.remove(out[i]);
					if (inDeg == null){
						inDeg = 0;
					}
					inDeg++;
					inDegree.put(out[i],inDeg);
					
					if (inDeg > 1){
						list = in.get(inDeg-2);
						list.removeElement(out[i]);
					}
					if (inDeg > in.size()){
						list = new Vector<Integer>();
						in.add(list);
					} else {
						list = in.get(inDeg-1);
					}
					list.add(out[i]);
				} 
			}
		}
		
		Node[] nodesNew = new Node[count];
		Edges edges = new Edges(nodesNew,g.computeNumberOfEdges());
		int c = 0;
		for (int i = 0; i < added.length; i++){
			if (added[i]){
				nodesNew[c] = nodesOld[i];
				newIndex.put(i, c);
				c++;
				
			}
		}
		c = 0;
		for (int i = 0; i < added.length; i++){
			if (added[i]){
				list = neighs.get(i);
				for (int j = 0; j < list.size(); j++){
					edges.add(c, newIndex.get(list.get(j)));
				}
				c++;
			}
		}
		edges.fill();
		g.setNodes(nodesNew);
		return g;
	}
	
	

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}
	
	/**
	 * returns indices of initial nodes
	 * @param nodes
	 * @param rand
	 * @return
	 */
    private int[] getStartIndex(Node[] nodes, Random rand){
    	if (this.selection.equals(SELECTION_RANDOM)){
    		return determineCliqueRandom(rand,nodes);
    	}
    	if (this.selection.equals(SELECTION_OUTDEGREE)){
    		return determineCliqueMostLinks(rand,nodes);
    	}
	   throw new IllegalArgumentException("Selection type " + this.selection + " in BuildSubGraph not known");
    }
    
    /**
     * determine initial nodes for random case
     * @param rand
     * @param nodes
     * @return
     */
    private int[] determineCliqueRandom(Random rand, Node[] nodes){
    	if (this.startNodes == 1){
    		return new int[]{rand.nextInt(nodes.length)};
    	}
    	if (this.startNodes == 2){
    		int s = rand.nextInt(nodes.length);
    		while (nodes[s].getDegree() == 0){
    			s = rand.nextInt(nodes.length);
    		}
    		int[] out = nodes[s].getOutgoingEdges();
    		return new int[]{s, out[rand.nextInt(out.length)]};
    	}
		int[] result = new int[this.startNodes];
		int n = rand.nextInt(nodes.length);
		int count = 0;
		boolean found = false;
		while (count < nodes.length & !found){
			count++;
			if (nodes[n].getOutDegree() >= this.startNodes && nodes[n].getInDegree() >= this.startNodes){
				Vector<Node> neighs = new Vector<Node>();
				Node cur;
				for (int i = 0; i < nodes[n].getOutDegree(); i++){
					cur = nodes[nodes[n].getOutgoingEdges()[i]];
					if (cur.getInDegree() >= this.startNodes && cur.getOutDegree() >= this.startNodes)
					neighs.add(cur);
				}
				neighs.add(nodes[n]);
				int round = 0;
				int pos = 0;
				while (round < neighs.size() && neighs.size() >= this.startNodes){
					if (this.checkLinked(neighs.get(pos).getIndex(),neighs) < this.startNodes){
						if (pos == neighs.size()){
							neighs = new Vector<Node>();
						} else {
						round = 0;
						neighs.remove(pos);
						}
					} else {
						pos = (pos +1) % neighs.size();
						round++;
					}
				}
				if (neighs.size() >= this.startNodes){
					neighs.remove(neighs.get(neighs.size()-1));
					Vector<Node[]> old = determinePairs(neighs);
					Vector<Node[]> next;
					int m = 3;
					while (old.size() > 0 && m < this.startNodes){
						m++;
						next = new Vector<Node[]>();
						for (int j = 0; j < old.size(); j++){
							next.addAll(this.determineMClique(old.get(j), neighs));
						}
						old = next;
					}
					if (m == this.startNodes && old.size() > 0){
						Node[] res = old.get(rand.nextInt(old.size()));
					    for (int j = 0; j < this.startNodes -1; j++){
							result[j] = res[j].getIndex();
						}
						result[this.startNodes-1] = nodes[n].getIndex();
						return result;
					}
				}
			}
			
			n = (n + 1) % nodes.length;
		}
		if (!found){
			return new int[0];
		}
		return result;
	}
	
    /**
     * return number of links node with index index has into the set neighs
     * @param index
     * @param neighs
     * @return
     */
	private int checkLinked(int index, Vector<Node> neighs){
		int c = 0;
		for (int i = 0; i < neighs.size(); i++){
			if (neighs.get(i).hasIn(index) && neighs.get(i).hasOut(index)){
				c++;
			}
		}
		return c;
	}
	
	/**
	 * all connected pairs in neighs (bidirectional connection required)
	 * @param neighs
	 * @return
	 */
	private Vector<Node[]> determinePairs(Vector<Node> neighs){
		Vector<Node[]> res = new Vector<Node[]>();
		Node[] array = new Node[2];
		for (int i = 0; i < neighs.size(); i++){
			array[0] = neighs.get(i);
			for (int j = i+1; j < neighs.size(); j++){
				if (array[0].hasIn(neighs.get(j).getIndex()) && array[0].hasOut(neighs.get(j).getIndex())){
					array[1] = neighs.get(j);
					res.add(array.clone());
				}
			}
		}
		return res;
	}
	
	/**
	 * return all cliques that can be generated from oldClique by adding
	 * a node in neighs
	 * @param oldClique
	 * @param neighs
	 * @return
	 */
	private Vector<Node[]> determineMClique(Node[] oldClique, Vector<Node> neighs){
		Vector<Node[]> res = new Vector<Node[]>();
		Node[] array = new Node[oldClique.length+1];
		for (int i = 0; i < array.length-1; i++){
			array[i] = oldClique[i];
		}
		for (int i = 0; i < neighs.size(); i++){
			boolean ok = true;
			Node cur = neighs.get(i);
			for (int j = 0; j < oldClique.length; j++){
				if (cur.getIndex() < oldClique[j].getIndex() || !cur.hasIn(oldClique[j].getIndex())
						|| !cur.hasOut(oldClique[j].getIndex())){
					ok = false;
				}
			}
			if (ok){
				array[array.length-1] = neighs.get(i);
				res.add(array.clone());
			}
		}
		return res;
	}
	
	/**
	 * determine clique for case OUTDEGREE
	 * @param rand
	 * @param nodes
	 * @return
	 */
	 private int[] determineCliqueMostLinks(Random rand, Node[] nodes){
	    	if (this.startNodes == 1){
	    		return new int[]{this.getMaxDegree(nodes, rand)};
	    	}
	    	if (this.startNodes == 2){
	    		return getMaxPair(nodes,rand);
	    	}
			int[] result = new int[this.startNodes];
			int n = rand.nextInt(nodes.length);
			int count = 0;
			boolean found = false;
			Vector<int[]> maxIndex = new Vector<int[]>();
			int max = 0;
			int c;
			while (count < nodes.length ){
				count++;
				if (nodes[n].getOutDegree() >= this.startNodes && nodes[n].getInDegree() >= this.startNodes){
					Vector<Node> neighs = new Vector<Node>();
					Node cur;
					for (int i = 0; i < nodes[n].getOutDegree(); i++){
						cur = nodes[nodes[n].getOutgoingEdges()[i]];
						if (cur.getInDegree() >= this.startNodes && cur.getOutDegree() >= this.startNodes)
						neighs.add(cur);
					}
					neighs.add(nodes[n]);
					int round = 0;
					int pos = 0;
					while (round < neighs.size() && neighs.size() >= this.startNodes){
						if (this.checkLinked(neighs.get(pos).getIndex(),neighs) < this.startNodes){
							if (pos == neighs.size()){
								neighs = new Vector<Node>();
							} else {
							round = 0;
							neighs.remove(pos);
							}
						} else {
							pos = (pos +1) % neighs.size();
							round++;
						}
					}
					if (neighs.size() >= this.startNodes){
						neighs.remove(neighs.get(neighs.size()-1));
						Vector<Node[]> old = determinePairs(neighs);
						Vector<Node[]> next;
						int m = 3;
						while (old.size() > 0 && m < this.startNodes){
							m++;
							next = new Vector<Node[]>();
							for (int j = 0; j < old.size(); j++){
								next.addAll(this.determineMClique(old.get(j), neighs));
							}
							old = next;
						}
						if (m == this.startNodes && old.size() > 0){
							for (int i = 0; i < old.size(); i++){
								Node[] res = old.get(rand.nextInt(old.size()));
								c = 0;
								for (int j = 0; j < res.length; j++){
									c = c + res[j].getDegree();
								}
								if ( c > max){
									max = c;
									maxIndex = new Vector<int[]>();
								}
								if (c == max){
								for (int j = 0; j < this.startNodes -1; j++){
									result[j] = res[j].getIndex();
								}
								result[this.startNodes-1] = nodes[n].getIndex();
								maxIndex.add(result.clone());
								}
							}
							found = true;
						}
					}
				}
				
				n = (n + 1) % nodes.length;
			}
			if (!found){
				return new int[0];
			}
			return maxIndex.get(rand.nextInt(maxIndex.size()));
		}
	
	 /**
	  * index of highest-degree node
	  * ties are broken randomly 
	  * @param nodes
	  * @param rand
	  * @return
	  */
	private int getMaxDegree(Node[] nodes, Random rand){
		int max = 0;
		Vector<Integer> maxIndex = new Vector<Integer>();
		for (int j = 0; j < nodes.length; j++){
			if (nodes[j].getDegree() > max){
				max = nodes[j].getDegree();
				maxIndex = new Vector<Integer>();
			}
			if (nodes[j].getDegree() >= max){
				maxIndex.add(j);
			}
		}
		return maxIndex.get(rand.nextInt(maxIndex.size()));
	}
	
	/**
	 * connected pair of nodes with highest degree
	 * @param nodes
	 * @param rand
	 * @return
	 */
	private int[] getMaxPair(Node[] nodes, Random rand){
		int max = 0;
		int cur;
		Vector<int[]> maxIndex = new Vector<int[]>();
		for (int j = 0; j < nodes.length; j++){
			int[] out = nodes[j].getOutgoingEdges();
			for (int i = 0; i < out.length; i++){
				if (out[i] > j && nodes[j].hasIn(out[j])){
					cur = nodes[j].getDegree() + nodes[out[j]].getDegree();
					if (cur > max){
						max = cur;
						maxIndex = new Vector<int[]>();
					}
					if (cur == max){
						maxIndex.add(new int[]{j,out[i]});
					}
				}
			}
		}
		return maxIndex.get(rand.nextInt(maxIndex.size()));
	}
	
	

}

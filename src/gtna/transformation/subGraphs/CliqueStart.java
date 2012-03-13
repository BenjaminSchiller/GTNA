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
 * CliqueStart.java
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

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class CliqueStart extends Transformation {

	static String ADD_RANDOM = "RANDOM";
	static String ADD_LARGEST = "LARGEST";
	int k;
	int min;
	int max;
	String add;
	/**
	 * @param key
	 * @param configKeys
	 * @param configValues
	 */
	public CliqueStart(int k, int min, int max, String add) {
		super("CLIQUE_START", new Parameter[]{new IntParameter("K", k), new IntParameter("MIN", min),
				new IntParameter("MAX", max), new StringParameter("ADD", add)});
		this.add = add;
		this.min = min;
		this.max = max;
		this.k = k;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		
		return g;
	}
	
	private int[] determineClique(Random rand, Node[] nodes){
		int[] result = new int[k];
		int n = rand.nextInt(nodes.length);
		int count = 0;
		boolean found = false;
		while (count < nodes.length & !found){
			count++;
			if (nodes[n].getOutDegree() >= this.k && nodes[n].getInDegree() >= this.k){
				Vector<Node> neighs = new Vector<Node>();
				Node cur;
				for (int i = 0; i < nodes[n].getOutDegree(); i++){
					cur = nodes[nodes[n].getOutgoingEdges()[i]];
					if (cur.getInDegree() >= this.k && cur.getOutDegree() >= this.k)
					neighs.add(cur);
				}
				neighs.add(nodes[n]);
				int round = 0;
				int pos = 0;
				while (round < neighs.size() && neighs.size() >= k){
					if (this.checkLinked(neighs.get(pos).getIndex(),neighs) < k){
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
				if (neighs.size() >= k){
					neighs.remove(neighs.get(neighs.size()-1));
					Vector<Node[]> old = determinePairs(neighs);
					Vector<Node[]> next;
					int m = 3;
					while (old.size() > 0 && m < this.k){
						m++;
						next = new Vector<Node[]>();
						for (int j = 0; j < old.size(); j++){
							next.addAll(this.determineMClique(old.get(j), neighs));
						}
						old = next;
					}
					if (m == this.k && old.size() > 0){
						Node[] res = old.get(rand.nextInt(old.size()));
						for (int j = 0; j < this.k -1; j++){
							result[j] = res[j].getIndex();
						}
						result[this.k-1] = nodes[n].getIndex();
						return result;
					}
				}
			}
			
			n = (n + 1) % nodes.length;
		}
		if (!found){
			throw new IllegalArgumentException("There is no clique of required size" + this.k);
		}
		return result;
	}
	
	private int checkLinked(int index, Vector<Node> neighs){
		int c = 0;
		for (int i = 0; i < neighs.size(); i++){
			if (neighs.get(i).hasIn(index) && neighs.get(i).hasOut(index)){
				c++;
			}
		}
		return c;
	}
	
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
				res.add(array.clone());
			}
		}
		return res;
	}
	
	

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

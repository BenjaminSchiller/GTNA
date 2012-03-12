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
 * SandbergBuild.java
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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class SandbergTransformation extends Transformation {
	public static String START_NODE_RANDOM = "RANDOM";
	public static String START_NODE_LARGEST = "LARGEST";
	
	String startnode;
	int include;

	/**
	 * @param key
	 * @param configKeys
	 * @param configValues
	 */
	public SandbergTransformation(int nodesToInclude, String startnode) {
		super("SANDBERG", new Parameter[]{new IntParameter("INCLUDE",nodesToInclude), 
				new StringParameter("STARTNODE",startnode)});
		this.include = nodesToInclude;
		this.startnode = startnode;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		Node[] nodesOld = g.getNodes();
		if (this.include >= nodesOld.length){
			return g;
		}
		Node[] nodesNew = new Node[this.include];
		boolean[] added = new boolean[nodesOld.length]; 
		Vector<Vector<Integer>> in = new Vector<Vector<Integer>>();
		HashMap<Integer, Integer> newIndex = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> inDegree = new HashMap<Integer,Integer>();
		Random rand = new Random();
		int s = this.getStartIndex(nodesOld, rand);
		added[s] = true;
		int count = 1;
		int max = 1;
		Node last = nodesOld[s];
		Integer inDeg;
		Vector<Integer> list;
		while (count < this.include && count < nodesOld.length){
			int[] out = last.getOutgoingEdges();
			for (int i = 0; i < out.length; i++){
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
			
			max = in.size()-1;
			while (in.get(max).size() == 0){
				max--;
			}
			s = in.get(max).remove(rand.nextInt(in.get(max).size()));
			last = nodesOld[s];
			added[s] = true;
			count++;
		}
		
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
				int[] out = nodesNew[c].getOutgoingEdges();
				for (int j = 0; j < out.length; j++){
					if (added[out[j]]){
						edges.add(c, newIndex.get(out[j]));
					}
				}
				c++;
			}
		}
		edges.fill();
		g.setNodes(nodesNew);
		return g;
	}
	
	private int getStartIndex(Node[] nodes, Random rand){
		if (this.startnode.equals(START_NODE_RANDOM)){
			return rand.nextInt(nodes.length);
		}
		if (this.startnode.equals(START_NODE_LARGEST)){
			int max = 0;
			Vector<Integer> index = new Vector<Integer>();
			for (int i = 0; i < nodes.length; i++){
				if (nodes[i].getOutDegree() > max){
					max = nodes[i].getOutDegree();
					index = new Vector<Integer>();
					index.add(i);
				}
				if (nodes[i].getOutDegree() == max){
					index.add(i);
				}
			}
			return index.get(rand.nextInt(index.size()));
		}
		throw new IllegalArgumentException("START_NODE type in SandbergTransformation not known");
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}
	
	

}

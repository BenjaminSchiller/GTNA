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
 * MaxMinBound.java
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
package gtna.transformation.degree;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.TransformationImpl;

import java.util.HashMap;
import java.util.Random;

/**
 * @author stef
 *
 */
public class MaxMinBound extends TransformationImpl {
	private int min,max;
	private boolean[] deleted;
	private boolean[][] deletedEdges;
	private int[] curDegree;

	/**
	 * @param key
	 * @param configKeys
	 * @param configValues
	 */
	public MaxMinBound(int min, int max) {
		super("MAX_MIN_DEGREE", new String[] {"MIN", "MAX"}, new String[]{""+min, ""+max});
		this.max = max;
		this.min = min;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		Node[] nodes = g.getNodes();
		this.deleted = new boolean[nodes.length];
		this.deletedEdges = new boolean[nodes.length][];
		this.curDegree = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++){
			curDegree[i] = nodes[i].getOutDegree();
			deletedEdges[i] = new boolean[nodes[i].getOutDegree()];
		}
		for (int n = 0; n < nodes.length; n++){
			removeMin(n,nodes);
		}
		Random rand = new Random();
		for (int n = 0; n < nodes.length; n++){
			removeMax(n,nodes, rand);
		}
		
		HashMap<Integer, Integer> index = new HashMap<Integer,Integer>();
		int count = 0;
		for (int i = 0; i < deleted.length; i++){
			if (!deleted[i]){
				index.put(i,count);
				count++;
			}
		}
		Node[] nNodes = new Node[count];
		Edges edges = new Edges(nNodes, count*this.min);
		for (int i = 0; i < nodes.length; i++){
			if (!deleted[i]){
			int nr = index.get(i);
			nNodes[nr] = nodes[i];
			int[] out = nodes[i].getOutgoingEdges();
			for (int j = 0; j < out.length; j++){
				if (!deletedEdges[i][j]){
					edges.add(nr, index.get(out[j]));
				}
			}
			}
		}
		edges.fill();
		g = new Graph(g.getName());
		g.setNodes(nNodes);
		return g;
	}
	
	private void removeMin(int nr, Node[] nodes){
		if (!deleted[nr] && curDegree[nr] < this.min){
			deleted[nr] = true;
			int[] out = nodes[nr].getOutgoingEdges();
			for (int j = 0; j < out.length; j++){
				deletedEdges[nr][j] = true;
				curDegree[out[j]]--;
				int[] outk = nodes[out[j]].getOutgoingEdges();
				for (int k = 0; k < deletedEdges[out[j]].length; k++)
				{
					if (outk[k] == nr){
						deletedEdges[out[j]][k] = true;
					}
					}
				removeMin(out[j], nodes);
			}
		}
	}
	
	private void removeMax(int nr, Node[] nodes, Random rand){
		while (curDegree[nr] > this.max){
			int[] out = nodes[nr].getOutgoingEdges();
			int k = rand.nextInt(out.length);
			while (deletedEdges[nr][k]){
				k = rand.nextInt(out.length);
			}
			curDegree[nr]--;
			curDegree[out[k]]--;
			deletedEdges[nr][k] = true;
			int[] outk = nodes[out[k]].getOutgoingEdges();
			for (int j = 0; j < deletedEdges[out[k]].length; j++)
			{
				if (outk[j] == nr){
					deletedEdges[out[k]][j] = true;
				}
				}
			removeMin(out[k], nodes);
			}
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}

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
import gtna.transformation.TransformationImpl;

import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class CliqueStart extends TransformationImpl {

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
		super("CLIQUE_START", new String[]{"K", "MIN","MAX", "ADD"}, new String[] {""+k, ""+min, ""+max,""+add});
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
			if (nodes[n].getOutDegree() >= this.k){
				
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
			if (neighs.get(i).)
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

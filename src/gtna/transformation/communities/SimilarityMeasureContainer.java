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
 * IHaveNoIdeaHowToNameThis.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import java.util.ArrayList;
import java.util.HashMap;

import gtna.communities.Community;
import gtna.communities.CommunityList;
import gtna.graph.Node;
import gtna.util.parameter.Parameter;

/**
 * @author Flipp
 *
 */
public class SimilarityMeasureContainer {
	
	int[][] data;
	private SimilarityMeasure simMeasure;
	
	public SimilarityMeasureContainer(SimilarityMeasure simMeasure, int n){
		
		this.simMeasure = simMeasure;
		data = new int[n][n];
	}
	

	/**
	 * @param akt
	 * @param c
	 */
	public void addCommunityAroundNode(Node akt, Community c) {
		for(int i : c.getNodes()){
			data[akt.getIndex()][i] = 1;
		}
		
	}

	/**
	 * @return
	 */
	public CommunityList getCommunityList() {
		HashMap<Integer, ArrayList<Integer>> coms = new HashMap<Integer, ArrayList<Integer>>();
		
		double[][] sims = new double[data.length][data.length];
		int[] combuffer = new int[data.length];
		for(int i = 0; i < data.length; i++){
			combuffer[i] = i;
			coms.put(i, new ArrayList<Integer>());
			coms.get(i).add(i);
			for(int j = i+1; j < data.length; j++){
				sims[i][j] = simMeasure.calcSimilarity(data[i], data[j]);
			}
		}
		
		double maxValue = simMeasure.minValue();
		int mostSimilarNode = -1;
		
		double val = 0;
		for(int i = 0; i < data.length; i++){
			mostSimilarNode = -1;
			maxValue = simMeasure.minValue();
			for(int j = 0; j < data.length; j++){
				if(i == j)
					continue;
				
				if(j < i){
					val = sims[j][i]; 
				}
				else if(j > i){
					val = sims[i][j];
				}
				
				if(val > maxValue){
					mostSimilarNode = j;
					maxValue = val;
				}
			}
			for(int j = 0; j < i; j++){
				if(coms.get(j) == coms.get(i)){
					coms.get(mostSimilarNode).addAll(coms.get(j));
					coms.remove(j);
				}
			}

			if(combuffer[mostSimilarNode] == combuffer[i])
				continue;
			
			coms.get(combuffer[mostSimilarNode]).addAll(coms.get(i));
			
			coms.remove(i);
			for(int k = 0; k < data.length; k++)
				if(combuffer[k] == combuffer[i])
					combuffer[k] = combuffer[mostSimilarNode];
			
			combuffer[i] = combuffer[mostSimilarNode];
			
		}
		
		ArrayList<Community> ret = new ArrayList<Community>();
		int id = 0;
		for(ArrayList<Integer> akt : coms.values()){
			ret.add(new Community(id, akt));
			id++;
		}

		
		return new CommunityList(ret);		
	}

	/**
	 * @return
	 */
	public Parameter[] getParameterArray() {
		return simMeasure.getParameterArray();
	}

}

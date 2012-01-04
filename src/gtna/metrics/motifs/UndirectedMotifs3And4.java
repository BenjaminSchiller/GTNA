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
 * UndirectedMotifs3And4.java
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
package gtna.metrics.motifs;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;

import java.util.HashMap;

/**
 * @author stef
 *
 */
public class UndirectedMotifs3And4 extends MotifCounter {

	/**
	 * @param key
	 */
	public UndirectedMotifs3And4() {
		super("UNDIRECTED_MOTIFS_3AND4");
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Node[] nodes = g.getNodes();
		Edge[] neighbors, neighbors2;
		//maximal number of edges, nodes => only those that are actual part of motif are evaluated 
		//(compare MotifAnalyzer.evaluateMotif)
		Node[] motifNodes = new Node[4];
		boolean[] motifEdges = new boolean[6];
        for (int i = 0; i < nodes.length; i++){
        	motifNodes[0] = nodes[i];
        	neighbors = nodes[i].getAllEdges();
        	for (int j = 0; j < neighbors.length-1; j++){
        		motifEdges[0] = true;
        		motifNodes[1] = nodes[neighbors[j].getDst()];
        		for (int k = j + 1; k < neighbors.length; k++){
        			motifEdges[1] = true;
        			motifNodes[2] = nodes[neighbors[k].getDst()];
        			motifEdges[2] = motifNodes[1].hasNeighbor(motifNodes[2].getIndex());
        			if (!motifEdges[2]){
        				//case: motif #1 found
        				this.counts[0]++;
        				//determine TwoV (motif #3) 
        				for (int l = k + 1; l < neighbors.length; l++){
        					motifEdges[2] = true;
                			motifNodes[3] = nodes[neighbors[l].getDst()];
                			motifEdges[3] = motifNodes[3].hasNeighbor(motifNodes[1].getIndex());
                			motifEdges[4] = motifNodes[3].hasNeighbor(motifNodes[2].getIndex());
                		    if (!motifEdges[3]){
                		    	if (!motifEdges[4]){
                		    	   counts[2]++;
                		    	} 
                		    } 
                		    if (motifEdges[3] && motifEdges[4] ){
                		    	if (motifNodes[0].getIndex() < motifNodes[3].getIndex()
                		    		&& motifNodes[0].getIndex() < motifNodes[1].getIndex()){
                		    	this.counts[6]++;
                		    	}
                		    }
        				}
        				
        				//determine 4Chain (#4) + 4Loop (#6)
        				if (motifNodes[0].getIndex() < motifNodes[1].getIndex()){
        				neighbors2 = motifNodes[1].getAllEdges();
        				for (int l = 0; l < neighbors2.length; l++){
        					if (neighbors2[l].getDst() == i){
        						continue;
        					}
        					motifEdges[2] = true;
                			motifNodes[3] = nodes[neighbors2[l].getDst()];
                			if (motifNodes[3].hasNeighbor(motifNodes[0].getIndex())){
                				continue;
                			}
                			motifEdges[3] = motifNodes[3].hasNeighbor(motifNodes[2].getIndex());
                			if (!motifEdges[3]){
                				this.counts[3]++;
                			} else {
                				if(motifNodes[0].getIndex() < motifNodes[3].getIndex()){
                					this.counts[5]++;
                				}
                			}
        				}
        				}
        				
        				//determine 4Chain (#4) other direction
        				if (motifNodes[0].getIndex() < motifNodes[2].getIndex()){
        				neighbors2 = motifNodes[2].getAllEdges();
        				for (int l = 0; l < neighbors2.length; l++){
        					if (neighbors2[l].getDst() == i){
        						continue;
        					}
        					motifEdges[2] = true;
                			motifNodes[3] = nodes[neighbors2[l].getDst()];
                			if (motifNodes[3].hasNeighbor(motifNodes[0].getIndex())){
                				continue;
                			}
                			motifEdges[3] = motifNodes[3].hasNeighbor(motifNodes[1].getIndex());
                			if (!motifEdges[3]){
                				this.counts[3]++;
                			} 
        				}
        				}
        			} else {
        				//found motif #2 (need to break symmetry)
        				if (motifNodes[0].getIndex() < motifNodes[1].getIndex() &&
        						motifNodes[0].getIndex() < motifNodes[2].getIndex()){
        					this.counts[1]++;
        					
        					//determine Semi4Clique + 4Clique (#8)
        					for (int l = k+1; l < neighbors.length; l++){
        						motifEdges[3] = true;
        						motifNodes[3] = nodes[neighbors[l].getDst()];
        						motifEdges[4] = motifNodes[1].hasNeighbor(motifNodes[3].getIndex());
        						motifEdges[5] = motifNodes[2].hasNeighbor(motifNodes[3].getIndex());
        						if (motifEdges[4]&& motifEdges[5]){
        							
        						} else {
        							    if (motifEdges[4] || motifEdges[5]){
        							    	if (!motifEdges[4]){
        							    		motifEdges[4] = motifEdges[5];
        							    	}
        									this.counts[6]++;
        								}else {
        									if(motifNodes[0].getIndex() < motifNodes[3].getIndex()) {
        									 this.counts[7]++;
        									} 
        								}
        							
        								
        						}
        					}
        					
        					//determine Semi4Clique
            				neighbors2 = motifNodes[1].getAllEdges();
            				for (int l = 0; l < neighbors2.length; l++){
            					if (neighbors2[l].getDst() == i){
            						continue;
            					}
            					motifEdges[3] = true;
                    			motifNodes[3] = nodes[neighbors2[l].getDst()];
                    			if (motifNodes[3].hasNeighbor(motifNodes[0].getIndex())){
                    				continue;
                    			}
                    			motifEdges[4] = motifNodes[3].hasNeighbor(motifNodes[2].getIndex());
                    			if (motifEdges[4]){
                    				if(motifNodes[0].getIndex() < motifNodes[3].getIndex()){
                    					this.counts[6]++;
                    				}
                    			}
            				}
            				
        					
        					
        				}
        				
        				//determine 3LoopOut
        				for (int l = 0; l < neighbors.length; l++){
    						motifEdges[3] = true;
    						motifNodes[3] = nodes[neighbors[l].getDst()];
    						motifEdges[4] = motifNodes[1].hasNeighbor(motifNodes[3].getIndex());
    						motifEdges[5] = motifNodes[2].hasNeighbor(motifNodes[3].getIndex());
    						if (!motifEdges[4]&& !motifEdges[5]){
    							this.counts[4]++;
    						} 
    					}
        			}
        		}
        	}
        }

	}

}

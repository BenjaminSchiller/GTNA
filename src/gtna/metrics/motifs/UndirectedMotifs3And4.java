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
		Edge[] motifEdges = new Edge[6];
        for (int i = 0; i < nodes.length; i++){
        	motifNodes[0] = nodes[i];
        	neighbors = nodes[i].getAllEdges();
        	for (int j = 0; j < neighbors.length-1; j++){
        		motifEdges[0] = neighbors[j];
        		motifNodes[1] = nodes[neighbors[j].getDst()];
        		for (int k = j + 1; k < neighbors.length; k++){
        			motifEdges[1] = neighbors[k];
        			motifNodes[2] = nodes[neighbors[k].getDst()];
        			motifEdges[2] = motifNodes[1].
        					getLink(motifNodes[2].getIndex());
        			if (motifEdges[2] == null){
        				//case: motif #1 found
        				this.evaluateMotif(0, motifNodes, motifEdges);
        				//determine TwoV (motif #3) 
        				for (int l = k + 1; l < neighbors.length; l++){
        					motifEdges[2] = neighbors[l];
                			motifNodes[3] = nodes[neighbors[l].getNode()];
                			motifEdges[3] = motifNodes[3].getLink(motifNodes[1].getIndex());
                			motifEdges[4] = motifNodes[3].getLink(motifNodes[2].getIndex());
                		    if (motifEdges[3] == null){
                		    	if (motifEdges[4] == null){
                		    	   this.evaluateMotif(2, motifNodes, motifEdges);
                		    	} 
                		    } 
                		    if (motifEdges[3] != null && motifEdges[4] != null){
                		    	if (motifNodes[0].getIndex() < motifNodes[3].getIndex()
                		    		&& motifNodes[0].getIndex() < motifNodes[1].getIndex()){
                		    	this.evaluateMotif(6, motifNodes, motifEdges);
                		    	}
                		    }
        				}
        				
        				//determine 4Chain (#4) + 4Loop (#6)
        				if (motifNodes[0].getIndex() < motifNodes[1].getIndex()){
        				neighbors2 = motifNodes[1].getNeighbors();
        				for (int l = 0; l < neighbors2.length; l++){
        					if (neighbors2[l].getNode() == i){
        						continue;
        					}
        					motifEdges[2] = neighbors2[l];
                			motifNodes[3] = nodes[neighbors2[l].getNode()];
                			if (motifNodes[3].getLink(motifNodes[0].getIndex()) != null){
                				continue;
                			}
                			motifEdges[3] = motifNodes[3].getLink(motifNodes[2].getIndex());
                			if (motifEdges[3] == null){
                				this.evaluateMotif(3, motifNodes, motifEdges);
                			} else {
                				if(motifNodes[0].getIndex() < motifNodes[3].getIndex()){
                					this.evaluateMotif(5, motifNodes, motifEdges);
                				}
                			}
        				}
        				}
        				
        				//determine 4Chain (#4) other direction
        				if (motifNodes[0].getIndex() < motifNodes[2].getIndex()){
        				neighbors2 = motifNodes[2].getNeighbors();
        				for (int l = 0; l < neighbors2.length; l++){
        					if (neighbors2[l].getNode() == i){
        						continue;
        					}
        					motifEdges[2] = neighbors2[l];
                			motifNodes[3] = nodes[neighbors2[l].getNode()];
                			if (motifNodes[3].getLink(motifNodes[0].getIndex()) != null){
                				continue;
                			}
                			motifEdges[3] = motifNodes[3].getLink(motifNodes[1].getIndex());
                			if (motifEdges[3] == null){
                				this.evaluateMotif(3, motifNodes, motifEdges);
                			} 
        				}
        				}
        			} else {
        				//found motif #2 (need to break symmetry)
        				if (motifNodes[0].getIndex() < motifNodes[1].getIndex() &&
        						motifNodes[0].getIndex() < motifNodes[2].getIndex()){
        					this.evaluateMotif(1, motifNodes, motifEdges);
        					
        					//determine Semi4Clique + 4Clique (#8)
        					for (int l = k+1; l < neighbors.length; l++){
        						motifEdges[3] = neighbors[l];
        						motifNodes[3] = nodes[neighbors[l].getNode()];
        						motifEdges[4] = motifNodes[1].getLink(motifNodes[3].getIndex());
        						motifEdges[5] = motifNodes[2].getLink(motifNodes[3].getIndex());
        						if (motifEdges[4] == null && motifEdges[5] == null){
        							
        						} else {
        							    if (motifEdges[4] == null || motifEdges[5] == null){
        							    	if (motifEdges[4] == null){
        							    		motifEdges[4] = motifEdges[5];
        							    	}
        									this.evaluateMotif(6, motifNodes, motifEdges);
        								}else {
        									if(motifNodes[0].getIndex() < motifNodes[3].getIndex()) {
        									 this.evaluateMotif(7, motifNodes, motifEdges);
        									} 
        								}
        							
        								
        						}
        					}
        					
        					//determine Semi4Clique
            				neighbors2 = motifNodes[1].getNeighbors();
            				for (int l = 0; l < neighbors2.length; l++){
            					if (neighbors2[l].getNode() == i){
            						continue;
            					}
            					motifEdges[3] = neighbors2[l];
                    			motifNodes[3] = nodes[neighbors2[l].getNode()];
                    			if (motifNodes[3].getLink(motifNodes[0].getIndex()) != null){
                    				continue;
                    			}
                    			motifEdges[4] = motifNodes[3].getLink(motifNodes[2].getIndex());
                    			if (motifEdges[4] != null){
                    				if(motifNodes[0].getIndex() < motifNodes[3].getIndex()){
                    					this.evaluateMotif(6, motifNodes, motifEdges);
                    				}
                    			}
            				}
            				
        					
        					
        				}
        				
        				//determine 3LoopOut
        				for (int l = 0; l < neighbors.length; l++){
    						motifEdges[3] = neighbors[l];
    						motifNodes[3] = nodes[neighbors[l].getNode()];
    						motifEdges[4] = motifNodes[1].getLink(motifNodes[3].getIndex());
    						motifEdges[5] = motifNodes[2].getLink(motifNodes[3].getIndex());
    						if (motifEdges[4] == null && motifEdges[5] == null){
    							this.evaluateMotif(4, motifNodes, motifEdges);
    						} 
    					}
        			}
        		}
        	}
        }

	}

}

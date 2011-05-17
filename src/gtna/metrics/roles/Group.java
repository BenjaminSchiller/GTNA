/*
 * ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 * 
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 * 
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * Group.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.metrics.roles;



import java.util.ArrayList;

/**
 * @deprecated
 */
public class Group {
	
	  //initialize global private variables
	  private int label = 0;        	/* label of the group */
	  private int totlinks = 0;     	/* total number of links of the nodes in the group */
	  private int inlinks = 0;      	/* links inside the group */
	  private int outlinks = 0;     	/* links outside the group */
	  private double totlinksW = 0.0; 	/* Weighted links of the nodes in the group */
	  private double inlinksW = 0.0;  	/* Weighted links inside the group */
	  private double outlinksW = 0.0; 	/* weighted links outside the group */

	  
	  private ArrayList<Integer> nodeList = new ArrayList<Integer>();

	// getter and setter methods
	
	public Group(int label){
		this.setLabel(label);
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(int label) {
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public int getLabel() {
		return label;
	}


	/**
	 * @param totlinks the totlinks to set
	 */
	public void setTotlinks(int totlinks) {
		this.totlinks = totlinks;
	}

	/**
	 * @return the totlinks
	 */
	public int getTotlinks() {
		return totlinks;
	}

	/**
	 * @param inlinks the inlinks to set
	 */
	public void setInlinks(int inlinks) {
		this.inlinks = inlinks;
	}

	/**
	 * @return the inlinks
	 */
	public int getInlinks() {
		return inlinks;
	}

	/**
	 * @param outlinks the outlinks to set
	 */
	public void setOutlinks(int outlinks) {
		this.outlinks = outlinks;
	}

	/**
	 * @return the outlinks
	 */
	public int getOutlinks() {
		return outlinks;
	}

	/**
	 * @param totlinksW the totlinksW to set
	 */
	public void setTotlinksW(double totlinksW) {
		this.totlinksW = totlinksW;
	}

	/**
	 * @return the totlinksW
	 */
	public double getTotlinksW() {
		return totlinksW;
	}

	/**
	 * @param inlinksW the inlinksW to set
	 */
	public void setInlinksW(double inlinksW) {
		this.inlinksW = inlinksW;
	}

	/**
	 * @return the inlinksW
	 */
	public double getInlinksW() {
		return inlinksW;
	}

	/**
	 * @param outlinksW the outlinksW to set
	 */
	public void setOutlinksW(double outlinksW) {
		this.outlinksW = outlinksW;
	}

	/**
	 * @return the outlinksW
	 */
	public double getOutlinksW() {
		return outlinksW;
	}

	/**
	 * @param nodeList the nodeList to set
	 */
	public void setNodeList(ArrayList<Integer> nodeList) {
		this.nodeList = nodeList;
	}

	/**
	 * @return the nodeList
	 */
	public ArrayList<Integer> getNodeList() {
		return nodeList;
	}
	
	/**
	 * @param Add to nodeList
	 */
	public void addToNodeList(int node) {
		this.nodeList.add(node);
	}

	//remove given node from group-node-list
	public void removeFromNodeList(int node) {		
		for(int i=0;i<nodeList.size();i++){
			if(nodeList.get(i)==node){
				this.nodeList.remove(i);
			}
		}		
	
	}

	
	
}

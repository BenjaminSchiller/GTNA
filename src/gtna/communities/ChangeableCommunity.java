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
 * ChangeableCommunity.java
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
package gtna.communities;

import gtna.util.Config;

import java.util.ArrayList;

/**
 * @author Flipp
 *
 */
public class ChangeableCommunity extends Community {
	protected ArrayList<Integer> nodes = new ArrayList<Integer>();

	@Override
	public String toString() {
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		StringBuffer buff = new StringBuffer(this.index + sep1);
		if (this.nodes.size() == 0) {
			return buff.toString();
		}
		
		int i = 0;
		for (int akt : nodes) {
			if(i != 0)
				buff.append(sep2);
			buff.append(akt);
			i++;
		}
		return buff.toString();
	}

	/**
	 * @param index
	 * @param nodes
	 */
	public ChangeableCommunity(int index, ArrayList<Integer> nodes) {
		super(index);
		for(Integer akt : nodes){
			this.nodes.add(akt);
		}
	}

	/**
	 * @param index
	 * @param nodes
	 */
	public ChangeableCommunity(int index, int[] nodes) {
		super(index);
		for(int akt : nodes){
			this.nodes.add(akt);
		}
	}

	/**
	 * @param index
	 */
	public ChangeableCommunity(int index) {
		super(index);
	}

	/**
	 * @param string
	 */
	public ChangeableCommunity(String string) {
		super();
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		String[] temp1 = string.split(sep1);
		this.index = Integer.parseInt(temp1[0]);
		int tempInt = 0;
		if (temp1.length < 2 || temp1[1].length() == 0) {
			
		} else {
			String[] temp2 = temp1[1].split(sep2);
			
			for (int i = 0; i < temp2.length; i++) {
				tempInt = Integer.parseInt(temp2[i]);
				nodes.add(tempInt);
			}
		}
	}

	@Override
	public int[] getNodes() {
		int[] ret = new int[nodes.size()];
		int i = 0;
		for(int akt : nodes){
			ret[i] = akt;
			i++;
		}
		
		return ret;		
	}

	@Override
	public int size() {
		return nodes.size();
	}
	
	public void addNode(int add){
		nodes.add(add);
	}
	
	public void removeNode(int node){
		nodes.remove(node);
	}
	
	public boolean contains(int node){
		boolean ret = false;
		for(int akt : nodes){
			if(node == akt)
				ret = true;
		}
		
		return ret;
	}
	
	public void setIndex(int id){
		index = id;
	}
	

}

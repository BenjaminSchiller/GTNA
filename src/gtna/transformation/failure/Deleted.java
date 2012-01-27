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
 * Deleted.java
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
package gtna.transformation.failure;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;

import java.util.ArrayList;

/**
 * graph property marking deleted node and keeping track of
 * sequence of deletions
 * @author stef
 *
 */
public class Deleted implements GraphProperty {
	private boolean[] isDeleted;
	private ArrayList<Integer> listDeleted;
	private boolean closed = false;
	
     
	public Deleted(Graph g){
		this.isDeleted = new boolean[g.getNodes().length];
		this.listDeleted = new ArrayList<Integer>();
	}
	
	public void deleteNodes(int[] index){
		for (int i = 0; i < index.length; i++){
			this.isDeleted[index[i]] = true;
			this.listDeleted.add(index[i]);
		}
	}
	
	public boolean[] getDeleted(){
		return this.isDeleted;
	}
	
	public ArrayList<Integer> getListDeleted(){
		return this.listDeleted;
	}
	

	
	/* (non-Javadoc)
	 * @see gtna.graph.GraphProperty#write(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean write(String filename, String key) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.GraphProperty#read(java.lang.String, gtna.graph.Graph)
	 */
	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

}

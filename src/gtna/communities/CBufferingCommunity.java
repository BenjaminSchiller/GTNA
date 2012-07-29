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
 * CBufferingCommunity.java
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Flipp
 * 
 */
public class CBufferingCommunity extends ChangeableCommunity {
	private HashMap<Integer, Integer> cBuffer = new HashMap<Integer, Integer>();

	@Override
	public void addNode(int add) {
		super.addNode(add);
		cBuffer.put(add, add);
	}

	@Override
	public void removeNode(int node) {
		super.removeNode(node);
		cBuffer.remove(node);
	}

	public CBufferingCommunity(int index, ArrayList<Integer> nodes) {
		super(index, nodes);
		for (int akt : nodes) {
			cBuffer.put(akt, akt);
		}
	}

	public CBufferingCommunity(int index, int[] nodes) {
		super(index, nodes);
		for (int akt : nodes) {
			cBuffer.put(akt, akt);
		}
	}

	public CBufferingCommunity(int index) {
		super(index);
	}

	public CBufferingCommunity(String string) {
		super(string);
		for(int akt : nodes){
			cBuffer.put(akt, akt);
		}
	}
	
	public boolean contains(int i){
		return cBuffer.containsKey(i);
	}

}

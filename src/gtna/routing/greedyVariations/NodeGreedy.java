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
 * NodeGreedy.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.routing.greddyStef;

import java.util.HashMap;

/**
 * @author stefanie
 *
 */
public abstract class NodeGreedy extends GreedyTemplate {
      HashMap<Integer, Integer> from;
      
      public NodeGreedy(String name) {
  		super(name, new String[] {}, new String[] {});
  	}

  	public NodeGreedy(int ttl, String name) {
  		super(ttl, name);
  	}
  	
  	public NodeGreedy(String name, String[] names, String[] values) {
  		super(name, names, values);
  	}

  	public NodeGreedy(int ttl, String name, String[] names, String[] values) {
  		super(ttl, name, names, values);
  	}
	

	/* (non-Javadoc)
	 * @see gtna.routing.greddyStef.GreedyTemplate#setSets(int)
	 */
	@Override
	public void setSets(int nr) {
		from = new HashMap<Integer,Integer>();
     }

}

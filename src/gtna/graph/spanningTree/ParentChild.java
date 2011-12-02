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
 * ParentChild.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.graph.spanningTree;

/**
 * @author benni
 * 
 */
public class ParentChild {
	private int parent;
	private int child;
	private int depth;

	public ParentChild(String string) {
		String[] temp = string.split(";");
		this.parent = Integer.parseInt(temp[0]);
		this.child = Integer.parseInt(temp[1]);
		this.depth = Integer.parseInt(temp[2]);
	}

	public ParentChild(int parent, int child, int depth) {
		this.parent = parent;
		this.child = child;
		this.depth = depth;
	}

	public String toString() {
		return this.parent + ";" + this.child + ";" + this.depth;
	}

	/**
	 * @return the parent
	 */
	public int getParent() {
		return this.parent;
	}

	/**
	 * @return the child
	 */
	public int getChild() {
		return this.child;
	}

	/**
	 * @return
	 */
	public int getDepth() {
		return this.depth;
	}
}

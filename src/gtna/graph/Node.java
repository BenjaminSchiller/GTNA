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
 * Node.java
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
package gtna.graph;

public interface Node {

	public abstract int route(Node to);

	public abstract double[] routeProg(Node to);

	public abstract void init(NodeImpl[] in, NodeImpl[] out);

	public abstract Node[] in();

	public abstract Node[] out();

	public abstract int index();

	public abstract void setIndex(int index);

	public abstract void setIn(NodeImpl[] in);

	public abstract void setOut(NodeImpl[] out);

	public abstract boolean hasIn(NodeImpl n);

	public abstract boolean hasOut(NodeImpl n);

	public abstract void addIn(NodeImpl n);

	public abstract void addOut(NodeImpl n);

	public abstract void removeIn(NodeImpl n);

	public abstract void removeOut(NodeImpl n);

	public abstract String toString();

}

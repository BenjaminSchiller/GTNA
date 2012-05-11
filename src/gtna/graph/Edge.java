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
 * Edge.java
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

public class Edge {
	private int src;

	private int dst;

	public Edge(int src, int dst) {
		this.src = src;
		this.dst = dst;
	}

	public Edge(Node src, Node dst) {
		this.src = src.getIndex();
		this.dst = dst.getIndex();
	}

	public String toString() {
		return toString(this.src, this.dst);
	}

	public static String toString(int src, int dst) {
		return src + "->" + dst;
	}

	/**
	 * @return the src
	 */
	public int getSrc() {
		return this.src;
	}

	/**
	 * @param src
	 *            the src to set
	 */
	public void setSrc(int src) {
		this.src = src;
	}

	/**
	 * @return the dst
	 */
	public int getDst() {
		return this.dst;
	}

	/**
	 * @param dst
	 *            the dst to set
	 */
	public void setDst(int dst) {
		this.dst = dst;
	}

	public Boolean equals(Edge e) {
		return (e.getSrc() == this.getSrc()) && (e.getDst() == this.getDst());
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Edge)) {
			return false;
		}
		Edge e = (Edge) obj;
		return this.src == e.src && this.dst == e.dst;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}

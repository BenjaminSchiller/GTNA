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
 * RingIDMultiR.java
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
package gtna.routing.node.identifier;

/**
 * Implements an identifier similar to gtna.routing.node.identifier.RingID. The
 * only difference is that this implementation also has an attribute for the
 * reality it belongs to. This means that the network spans multiple overlays in
 * parallel and every node has a separate identifier in each overlay or reality.
 * 
 * @author benni
 * 
 */
public class RingIDMultiR extends RingID implements Identifier {
	public int reality;

	/**
	 * 
	 * @param pos
	 *            the actual identifier (position on the ring)
	 * @param reality
	 *            the reality that the identifier belongs to
	 */
	public RingIDMultiR(double pos, int reality) {
		super(pos);
		this.reality = reality;
	}

	public boolean equals(Identifier id) {
		return super.equals(id) && ((RingIDMultiR) id).reality == this.reality;
	}

	public String toString() {
		return "RIDMR(" + this.pos + "/" + this.reality + ")";
	}
}

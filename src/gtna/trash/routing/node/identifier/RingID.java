///*
// * ===========================================================
// * GTNA : Graph-Theoretic Network Analyzer
// * ===========================================================
// * 
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors
// * 
// * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
// * 
// * GTNA is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * 
// * GTNA is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// * 
// * ---------------------------------------
// * RingID.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// * 
// * Original Author: Benjamin Schiller;
// * Contributors:    -;
// * 
// * Changes since 2011-05-17
// * ---------------------------------------
// */
//package gtna.trash.routing.node.identifier;
//
///**
// * Implements an Identifier for ring-based identifier spaces like Chord or
// * Symphony. All identifiers are assumed to be chosen from the identifier space
// * [0,1). All distances are computed using a wrap-around at 1. Therefore:
// * dist(0.1, 0.3) = dist(0.3, 0.1) = 0.2 and dist(0.8, 0.1) = 0.3
// * 
// * @author benni
// * 
// */
//@Deprecated
//public class RingID implements Identifier {
//	public double pos;
//
//	/**
//	 * 
//	 * @param pos
//	 *            the actual identifier (position on the ring)
//	 */
//	public RingID(double pos) {
//		this.pos = pos;
//	}
//
//	public double dist(Identifier id) {
//		double src = this.pos;
//		double dest = ((RingID) id).pos;
//		double dist = src <= dest ? dest - src : dest + 1 - src;
//		return dist <= 0.5 ? dist : 1 - dist;
//	}
//
//	public boolean equals(Identifier id) {
//		return ((RingID) id).pos == this.pos;
//	}
//
//	public String toString() {
//		return "RID(" + this.pos + ")";
//	}
//
//	public static RingID parse(String str) {
//		double pos = Double.parseDouble(str.replace("RID(", "")
//				.replace(")", ""));
//		return new RingID(pos);
//	}
//}

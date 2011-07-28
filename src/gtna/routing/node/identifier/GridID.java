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
 * GridID.java
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


@Deprecated
public class GridID implements Identifier {
	public double[] x;

	public GridID(double[] x) {
		this.x = x;
	}

	public double dist(Identifier id) {
		GridID ID = (GridID) id;
		double sum = 0;
		for (int i = 0; i < this.x.length; i++) {
			sum += Math.pow(Math.abs(this.x[i] - ID.x[i]), this.x.length);
		}
		return Math.pow(sum, 1.0 / (double) this.x.length);
	}

	public int dimensions() {
		return this.x.length;
	}

	public boolean equals(GridID id) {
		for (int i = 0; i < this.x.length; i++) {
			if (this.x[i] != id.x[i]) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("(" + this.x[0]);
		for (int i = 1; i < this.x.length; i++) {
			buff.append("/" + this.x[i]);
		}
		buff.append(")");
		return buff.toString();
	}

	public GridID clone() {
		return new GridID(this.x.clone());
	}

	public boolean equals(Identifier id) {
		double[] x = ((GridID) id).x;
		for (int i = 0; i < this.x.length; i++) {
			if (x[i] != this.x[i]) {
				return false;
			}
		}
		return true;
	}
}

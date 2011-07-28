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
 * GridIDEuclidean.java
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
public class GridIDEuclidean implements Identifier {
	public double[] pos;

	public GridIDEuclidean(double[] pos) {
		this.pos = pos;
	}

	public int dimensions() {
		return this.pos.length;
	}

	public double dist(Identifier id) {
		double[] pos = ((GridIDEuclidean) id).pos;
		double sum = 0;
		for (int i = 0; i < this.pos.length; i++) {
			sum += (this.pos[i] - pos[i]) * (this.pos[i] - pos[i]);
		}
		return Math.sqrt(sum);
	}

	public boolean equals(Identifier id) {
		double[] pos = ((GridIDEuclidean) id).pos;
		for (int i = 0; i < this.pos.length; i++) {
			if (this.pos[i] != pos[i]) {
				return false;
			}
		}
		return true;
	}
	
	public String toString(){
		StringBuffer buff = new StringBuffer("(" + this.pos[0]);
		for(int i=1; i<this.pos.length; i++){
			buff.append(", " + this.pos[i]);
		}
		buff.append(")");
		return buff.toString();
	}
}

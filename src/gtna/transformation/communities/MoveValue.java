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
 * MoveValue.java
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
package gtna.transformation.communities;

/**
 * @author Flipp
 *
 */
public class MoveValue {

	private int newCom;
	private int oldCom;
	private double modDelta;

	/**
	 * @return
	 */
	public double getModDelta() {
		return modDelta;
	}

	/**
	 * @return
	 */
	public int getNewCom() {
		return newCom;
	}

	/**
	 * @param index
	 */
	public void setOldCom(int index) {
		this.oldCom = index;
		
	}
	
	public int getOldCom(){
		return oldCom;
	}

	/**
	 * @param d
	 */
	public void setModDelta(double d) {
		this.modDelta = d;
		
	}

	/**
	 * @param index
	 */
	public void setNewCom(int index) {
		this.newCom = index;
		
	}

}

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
 * LookaheadElement.java
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
package gtna.id.lookahead;

import gtna.id.DIdentifier;
import gtna.id.Identifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author benni
 * 
 */
public class LookaheadElement {
	@SuppressWarnings("rawtypes")
	private Identifier id;

	private int via;

	@SuppressWarnings("rawtypes")
	public LookaheadElement(Identifier id, int via) {
		this.id = id;
		this.via = via;
	}

	public LookaheadElement(String string, Constructor<DIdentifier> constructor) {
		String[] temp = string.split("->");
		try {
			this.id = (DIdentifier) constructor.newInstance(temp[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		this.via = Integer.parseInt(temp[1]);
	}

	public String toString() {
		return this.id.toString() + "->" + via;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object arg0) {
		LookaheadElement l = (LookaheadElement) arg0;
		return l.id.equals(this.id) && l.via == this.via;
	}

	/**
	 * @return the id
	 */
	@SuppressWarnings("rawtypes")
	public Identifier getId() {
		return this.id;
	}

	/**
	 * @return the via
	 */
	public int getVia() {
		return this.via;
	}
}

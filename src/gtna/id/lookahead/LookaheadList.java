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
 * LookaheadList.java
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

import gtna.id.DoublePartition;
import gtna.util.Config;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class LookaheadList {
	int index;

	private LookaheadElement[] list;

	public LookaheadList(int index, LookaheadElement[] list) {
		this.index = index;
		this.list = list;
	}

	public LookaheadList(int index, ArrayList<LookaheadElement> list) {
		this.index = index;
		this.list = new LookaheadElement[list.size()];
		for (int i = 0; i < list.size(); i++) {
			this.list[i] = list.get(i);
		}
	}

	public LookaheadList(String string, Constructor<DoublePartition> constructor) {
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		String[] temp1 = string.split(sep1);
		String[] temp2 = temp1.length > 1 ? temp1[1].split(sep2)
				: new String[0];
		this.index = Integer.parseInt(temp1[0]);
		this.list = new LookaheadElement[temp2.length];
		for (int i = 0; i < temp2.length; i++) {
			this.list[i] = new LookaheadElement(temp2[i], constructor);
		}
	}

	public String toString() {
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		StringBuffer buff = new StringBuffer(this.index + sep1);
		if (this.list.length > 0) {
			buff.append(this.list[0]);
		}
		for (int i = 1; i < this.list.length; i++) {
			buff.append(sep2 + this.list[i].toString());
		}
		return buff.toString();
	}

	public int size() {
		return this.list.length;
	}

	/**
	 * @return the list
	 */
	public LookaheadElement[] getList() {
		return this.list;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return this.index;
	}
}

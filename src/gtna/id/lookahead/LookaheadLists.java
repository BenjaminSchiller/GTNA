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
 * LookaheadLists.java
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

import gtna.graph.GraphProperty;
import gtna.id.DoublePartition;
import gtna.io.Filereader;
import gtna.io.Filewriter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class LookaheadLists extends GraphProperty {
	private LookaheadList[] lists;

	public LookaheadLists() {
		this.lists = new LookaheadList[0];
	}

	public LookaheadLists(LookaheadList[] lists) {
		this.lists = lists;
	}

	public LookaheadLists(ArrayList<LookaheadList> lists) {
		this.lists = new LookaheadList[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			this.lists[i] = lists.get(i);
		}
	}

	/**
	 * @return the lists
	 */
	public LookaheadList[] getLists() {
		return this.lists;
	}

	public LookaheadList getList(int index) {
		return this.lists[index];
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		this.writeHeader(fw, this.getClass(), key);

		this.writeParameter(fw, "Lists", this.lists.length);
		this.writeParameter(fw, "Partition class", this.lists[0].getList()[0]
				.getPartition().getClass());

		for (LookaheadList list : this.lists) {
			fw.writeln(list.toString());
		}

		return fw.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String read(String filename) {
		Filereader fr = new Filereader(filename);

		String key = this.readHeader(fr);

		this.lists = new LookaheadList[this.readInt(fr)];
		Class<?> partitionClass = this.readClass(fr);

		Constructor<DoublePartition>[] constructors = null;
		try {
			constructors = (Constructor<DoublePartition>[]) partitionClass
					.getConstructors();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		Constructor<DoublePartition> constructor = null;
		for (Constructor<DoublePartition> c : constructors) {
			if (c.getParameterTypes().length == 1
					&& c.getParameterTypes()[0].toString().equals(
							"class java.lang.String")) {
				constructor = c;
				break;
			}
		}

		String line = null;
		int index = 0;
		while ((line = fr.readLine()) != null) {
			this.lists[index] = new LookaheadList(line, constructor);
			index++;
		}

		fr.close();

		return key;
	}
}

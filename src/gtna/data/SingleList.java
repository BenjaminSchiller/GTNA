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
 * SingleList.java
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
package gtna.data;

import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.metrics.Metric;
import gtna.util.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class SingleList {
	private Metric metric;

	private Single[] singles;

	private HashMap<String, Single> map;

	public SingleList(Metric metric, Single[] singles) {
		this.metric = metric;
		this.singles = singles;
		this.map = new HashMap<String, Single>();
		for (Single s : this.singles) {
			this.map.put(s.getKey(), s);
		}
	}

	public SingleList(Metric metric, ArrayList<Single> singlesList) {
		this.metric = metric;
		this.singles = new Single[singlesList.size()];
		for (int i = 0; i < singlesList.size(); i++) {
			this.singles[i] = singlesList.get(i);
		}
		this.map = new HashMap<String, Single>();
		for (Single s : this.singles) {
			this.map.put(s.getKey(), s);
		}
	}

	public Single get(String key) {
		return this.map.get(key);
	}

	public boolean write(String filename) {
		Filewriter fw = new Filewriter(filename);
		for (Single single : this.singles) {
			fw.writeln(single.getKey() + "=" + single.getValue());
		}
		return fw.close();
	}

	public static SingleList read(Metric metric, String filename) {
		if (!(new File(filename)).exists()) {
			return null;
		}
		Filereader fr = new Filereader(filename);
		ArrayList<Single> list = new ArrayList<Single>();
		String line = null;
		String delimiter = Config.get("DATA_WRITER_DELIMITER");
		while ((line = fr.readLine()) != null) {
			if (line.trim().length() == 0) {
				continue;
			}
			String[] temp = line.split("=");
			if (temp[1].contains(delimiter)) {
				String[] temp2 = temp[1].split(delimiter);
				double[] data = new double[temp2.length];
				for (int i = 0; i < temp2.length; i++) {
					data[i] = Double.parseDouble(temp2[i]);
				}
				list.add(new Single(temp[0], Double.parseDouble(temp2[0]), data));
			} else {
				list.add(new Single(temp[0], Double.parseDouble(temp[1])));
			}
		}
		fr.close();
		return new SingleList(metric, list);
	}

	public Metric getMetric() {
		return this.metric;
	}

	public Single[] getSingles() {
		return this.singles;
	}

	public String[] getKeys() {
		String[] keys = new String[this.singles.length];
		for (int i = 0; i < this.singles.length; i++) {
			keys[i] = this.singles[i].getKey();
		}
		return keys;
	}
}

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
 * Mapping.java
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
package gtna.io.networks.googlePlus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author benni
 * 
 */
public class Mapping {
	private HashMap<String, Integer> map;

	private int cid;

	public Mapping(HashMap<String, Integer> map, int cid) {
		this.map = map;
		this.cid = cid;
	}

	public Mapping(IdList idList, int cid) {
		this.map = new HashMap<String, Integer>();
		int index = 0;
		for (String id : idList.getList()) {
			if (!this.map.containsKey(id)) {
				this.map.put(id, index++);
			}
		}
		this.cid = cid;
	}

	public static Mapping generateCrawledMapping(Crawl crawl) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (File node : crawl.getNodeList()) {
			String[] temp = node.getName().split("-");
			int tid = Integer.parseInt(temp[0]);
			int tlid = Integer.parseInt((new File(node.getParent()).getName()));
			String u_id = temp[1];
			Task task = new Task(tid, crawl.getCid(), tlid, u_id, 0, 0);
			Node n = null;
			try {
				n = Node.read(node.getAbsolutePath(), task);
			} catch (Exception e) {
				continue;
			}
			if (n.getTask().getU_id().length() == 21
					&& !map.containsKey(n.getTask().getU_id())) {
				map.put(n.getTask().getU_id(), map.size());
			}
		}
		return new Mapping(map, crawl.getCid());
	}

	// public static Mapping generateCompleteMapping(Crawl crawl) {
	// HashMap<String, Integer> map = new HashMap<String, Integer>();
	// for (File node : crawl.getNodeList()) {
	// String[] temp = node.getName().split("-");
	// int tid = Integer.parseInt(temp[0]);
	// int tlid = Integer.parseInt((new File(node.getParent()).getName()));
	// String u_id = temp[1];
	// Task task = new Task(tid, crawl.getCid(), tlid, u_id, 0, 0);
	// Node n = Node.read(node.getAbsolutePath(), task);
	// if (n.getTask().getU_id().length() == 21
	// && !map.containsKey(n.getTask().getU_id())) {
	// map.put(n.getTask().getU_id(), map.size());
	// }
	// for (User out : n.getOut()) {
	// if (out.getId().length() == 21 && !map.containsKey(out.getId())) {
	// map.put(out.getId(), map.size());
	// }
	// }
	// for (User in : n.getIn()) {
	// if (in.getId().length() == 21 && !map.containsKey(in.getId())) {
	// map.put(in.getId(), map.size());
	// }
	// }
	// }
	// return new Mapping(map, crawl.getCid());
	// }

	public void writeMapping(String filename) throws IOException {
		BufferedWriter fw = new BufferedWriter(new FileWriter(filename));
		for (Entry<String, Integer> entry : this.map.entrySet()) {
			fw.write(entry.getValue() + " " + entry.getKey() + "\n");
		}
		fw.close();
	}

	public static Mapping readMapping(String filename) throws IOException {
		File f = new File(filename);
		int cid = Integer.parseInt(f.getName().split("-")[0]);
		return Mapping.readMapping(filename, cid);
	}

	public static Mapping readMapping(String filename, int cid)
			throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(filename));
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String line = null;
		while ((line = fr.readLine()) != null) {
			if (line.length() == 0) {
				continue;
			}
			String[] temp = line.split(" ");
			map.put(temp[1], Integer.parseInt(temp[0]));
		}
		fr.close();
		return new Mapping(map, cid);
	}

	/**
	 * @return the map
	 */
	public HashMap<String, Integer> getMap() {
		return this.map;
	}

	/**
	 * @return the cid
	 */
	public int getCid() {
		return this.cid;
	}
}

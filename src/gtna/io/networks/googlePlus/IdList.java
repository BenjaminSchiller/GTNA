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
 * CrawledList.java
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author benni
 * 
 */
public class IdList {
	private ArrayList<String> list;

	private int cid;

	public IdList(ArrayList<String> list, int cid) {
		this.list = list;
		this.cid = cid;
	}

	public static IdList intersect(IdList list1, IdList list2, int cid) {
		ArrayList<String> l1 = list1.getList();
		ArrayList<String> l2 = list2.getList();
		if (list1.getList().size() > list2.getList().size()) {
			l1 = list2.getList();
			l2 = list1.getList();
		}
		HashSet<String> set2 = new HashSet<String>(l2.size());
		for (String id : l2) {
			set2.add(id);
		}
		ArrayList<String> newList = new ArrayList<String>();
		for (String id : l1) {
			if (set2.contains(id)) {
				newList.add(id);
			}
		}
		return new IdList(newList, cid);
	}

	public static IdList generateCrawledIdList(Crawl crawl) {
		ArrayList<String> list = new ArrayList<String>();
		for (File node : crawl.getNodeList()) {
			String[] temp = node.getName().split("-");
			if (temp[1].length() == 21) {
				list.add(temp[1]);
			}
		}
		return new IdList(list, crawl.getCid());
	}

	public static IdList generateSeenIdList(Crawl crawl) {
		HashSet<String> set = new HashSet<String>();
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
			if (n.getTask().getU_id().length() == 21) {
				set.add(n.getTask().getU_id());
			}
			for (User out : n.getOut()) {
				if (out.getId().length() == 21) {
					set.add(out.getId());
				}
			}
			for (User in : n.getIn()) {
				if (in.getId().length() == 21) {
					set.add(in.getId());
				}
			}
		}
		ArrayList<String> list = new ArrayList<String>(set.size());
		for (Object e : set.toArray()) {
			list.add((String) e);
		}
		return new IdList(list, crawl.getCid());
	}

	public void write(String filename) throws IOException {
		BufferedWriter fw = new BufferedWriter(new FileWriter(filename));
		for (String crawled : list) {
			fw.write(crawled + "\n");
		}
		fw.close();
	}

	public static IdList read(String filename) throws IOException {
		File f = new File(filename);
		int cid = Integer.parseInt(f.getName().split("-")[0]);
		return IdList.read(filename, cid);
	}

	public static IdList read(String filename, int cid) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader fr = new BufferedReader(new FileReader(filename));
		String line = null;
		while ((line = fr.readLine()) != null) {
			if (line.length() == 0) {
				continue;
			}
			list.add(line);
		}
		fr.close();
		Collections.sort(list);
		return new IdList(list, cid);
	}

	/**
	 * @return the list
	 */
	public ArrayList<String> getList() {
		return this.list;
	}

	/**
	 * @return the cid
	 */
	public int getCid() {
		return this.cid;
	}
}

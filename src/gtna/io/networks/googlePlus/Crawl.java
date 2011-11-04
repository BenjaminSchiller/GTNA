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
 * Crawl.java
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author benni
 * 
 */
public class Crawl implements Comparable<Crawl> {
	private int cid;

	private File folder;

	private ArrayList<File> nodeList;

	public Crawl(File folder) {
		this.folder = folder;
		this.cid = Integer.parseInt(folder.getName());
		// this.generateNodeList();
	}

	private void generateNodeList() {
		this.nodeList = new ArrayList<File>();
		File[] taskLists = this.folder.listFiles();
		for (File taskList : taskLists) {
			File[] tasks = taskList.listFiles();
			if (tasks != null) {
				for (File task : tasks) {
					this.nodeList.add(task);
				}
			}
		}
	}

	public static ArrayList<Crawl> getCrawls(String dataFolder, int mod,
			int offset) {
		ArrayList<File> crawls = new ArrayList<File>();
		File f = new File(dataFolder);
		for (File c : f.listFiles()) {
			crawls.add(c);
		}

		ArrayList<Crawl> temp = new ArrayList<Crawl>();
		for (int i = 0; i < crawls.size(); i++) {
			if ((i % mod) == offset) {
				temp.add(new Crawl(crawls.get(i)));
			}
		}

		Collections.sort(temp);

		return temp;
	}

	public static ArrayList<Crawl> getCrawls(String dataFolder, int mod,
			int offset, int minNodes, int minCid, int maxCid) {
		ArrayList<Crawl> list = Crawl.getCrawls(dataFolder, mod, offset);
		ArrayList<Crawl> crawls = new ArrayList<Crawl>();
		for (Crawl c : list) {
			if ((minNodes <= 0 || c.getNodeList().size() >= minNodes)
					&& c.getCid() >= minCid && c.getCid() <= maxCid) {
				crawls.add(c);
			}
		}
		return crawls;
	}

	public String toString() {
		return "Crawl " + this.cid + " '" + this.folder.getAbsolutePath() + "'";
	}

	/**
	 * @return the cid
	 */
	public int getCid() {
		return this.cid;
	}

	/**
	 * @return the folder
	 */
	public File getFolder() {
		return this.folder;
	}

	/**
	 * @return the nodeList
	 */
	public ArrayList<File> getNodeList() {
		if (this.nodeList == null) {
			this.generateNodeList();
		}
		return this.nodeList;
	}

	@Override
	public int compareTo(Crawl crawl) {
		return this.cid - crawl.cid;
	}
}
